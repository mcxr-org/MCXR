package net.sorenon.mcxr.play.openxr;

import net.sorenon.mcxr.play.MCXRPlayClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.PointerBuffer;
import org.lwjgl.openxr.*;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.util.HashMap;

import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.memAddress;
import static org.lwjgl.system.MemoryUtil.memUTF8;

public class OpenXRInstance implements AutoCloseable {
    private static final Logger LOGGER = LogManager.getLogger();

    private final HashMap<String, Long> paths = new HashMap<>();

    public final XrInstance handle;
    public final XrEventDataBuffer eventDataBuffer;

    public String runtimeName;
    public long runtimeVersion;
    public String runtimeVersionString;

    public OpenXRInstance(XrInstance handle) {
        this.handle = handle;
        eventDataBuffer = XrEventDataBuffer.calloc();

        try (var stack = stackPush()) {
            var properties = XrInstanceProperties.calloc(stack).type$Default();
            checkPanic(XR10.xrGetInstanceProperties(handle, properties), "xrGetInstanceProperties");
            runtimeName = properties.runtimeNameString();
            runtimeVersion = properties.runtimeVersion();
            runtimeVersionString = XR10.XR_VERSION_MAJOR(runtimeVersion) + "." + XR10.XR_VERSION_MINOR(runtimeVersion) + "." + XR10.XR_VERSION_PATCH(runtimeVersion);
        }
    }

    public OpenXRSystem getSystem(int formFactor) throws XrException {
        try (MemoryStack stack = stackPush()) {
            XrSystemGetInfo systemInfo = XrSystemGetInfo.malloc(stack);
            systemInfo.set(XR10.XR_TYPE_SYSTEM_GET_INFO, 0, formFactor);

            LongBuffer lBuf = stack.longs(0);
            check(XR10.xrGetSystem(handle, systemInfo, lBuf), "xrGetSystem");
            long systemID = lBuf.get();
            if (systemID == 0) {
                throw new XrException(0, "No compatible headset detected");
            }
            return new OpenXRSystem(this, formFactor, systemID);
        }
    }

    public OpenXRSession createSession(OpenXRSystem system) throws XrException {
        try (MemoryStack stack = stackPush()) {
            XrSessionCreateInfo sessionCreateInfo = XrSessionCreateInfo.malloc(stack).set(
                    XR10.XR_TYPE_SESSION_CREATE_INFO,
                    system.createOpenGLBinding(stack).address(),
                    0,
                    system.handle
            );

            PointerBuffer handlePointer = stack.mallocPointer(1);
            check(XR10.xrCreateSession(handle, sessionCreateInfo, handlePointer), "xrCreateSession");
            return new OpenXRSession(new XrSession(handlePointer.get(0), handle), system);
        }
    }

    public boolean pollEvents() {
        XrEventDataBaseHeader event = nextEvent();
        while (event != null) {
            switch (event.type()) {
                case XR10.XR_TYPE_EVENT_DATA_INSTANCE_LOSS_PENDING: {
                    XrEventDataInstanceLossPending instanceLossPending = XrEventDataInstanceLossPending.create(event.address());
                    LOGGER.info("XrEventDataInstanceLossPending by " + instanceLossPending.lossTime());
                    return true;
                }
                case XR10.XR_TYPE_EVENT_DATA_SESSION_STATE_CHANGED: {
                    return MCXRPlayClient.OPEN_XR_STATE.session.stateChanged(XrEventDataSessionStateChanged.create(event.address()));
                }
                case XR10.XR_TYPE_EVENT_DATA_INTERACTION_PROFILE_CHANGED:
                    break;
                case XR10.XR_TYPE_EVENT_DATA_REFERENCE_SPACE_CHANGE_PENDING:
                default: {
                    LOGGER.info(String.format("Ignoring event type %d", event.type()));
                    break;
                }
            }
            event = nextEvent();
        }

        return false;
    }


    public XrEventDataBaseHeader nextEvent() {
        // It is sufficient to just clear the XrEventDataBuffer header to
        // XR_TYPE_EVENT_DATA_BUFFER rather than recreate it every time
        eventDataBuffer.clear();
        eventDataBuffer.type(XR10.XR_TYPE_EVENT_DATA_BUFFER);

        int result = XR10.xrPollEvent(handle, eventDataBuffer);
        checkPanic(result, "xrPollEvent");
        if (result == XR10.XR_EVENT_UNAVAILABLE) {
            return null;
        }
        if (eventDataBuffer.type() == XR10.XR_TYPE_EVENT_DATA_EVENTS_LOST) {
            XrEventDataEventsLost dataEventsLost = XrEventDataEventsLost.create(eventDataBuffer.address());
            LOGGER.info(String.format("%d events lost", dataEventsLost.lostEventCount()));
        }
        return XrEventDataBaseHeader.create(eventDataBuffer.address());
    }


    public long getPath(String pathString) {
        return paths.computeIfAbsent(pathString, s -> {
            try (MemoryStack ignored = stackPush()) {
                LongBuffer buf = stackMallocLong(1);
                int xrResult = XR10.xrStringToPath(handle, pathString, buf);
                if (xrResult == XR10.XR_ERROR_PATH_FORMAT_INVALID) {
                    throw new XrRuntimeException(xrResult, "Invalid path:\"" + pathString + "\"");
                } else {
                    checkPanic(xrResult, "xrStringToPath");
                }
                return buf.get();
            }
        });
    }

    public void check(int result, String method) throws XrException {
        if (result >= 0) return;

        ByteBuffer str = stackMalloc(XR10.XR_MAX_RESULT_STRING_SIZE);
        if (XR10.xrResultToString(handle, result, str) >= 0) {
            throw new XrException(result, method + " returned " + memUTF8(memAddress(str)));
        }
    }

    public void checkPanic(int result, String method) {
        if (result >= 0) return;

        ByteBuffer str = stackMalloc(XR10.XR_MAX_RESULT_STRING_SIZE);
        if (XR10.xrResultToString(handle, result, str) >= 0) {
            throw new XrRuntimeException(result, method + " returned:" + memUTF8(memAddress(str)));
        }
    }

    @Override
    public void close() {
        var session = MCXRPlayClient.OPEN_XR_STATE.session;
        if (session != null) {
            session.close();
        }
        XR10.xrDestroyInstance(handle);
        eventDataBuffer.close();
    }
}
