package net.sorenon.mcxr.play.client.openxr;

import net.sorenon.mcxr.play.client.MCXRPlayClient;
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

    public OpenXRInstance(XrInstance handle) {
        this.handle = handle;
        eventDataBuffer = XrEventDataBuffer.calloc();
    }

    public OpenXRSystem getSystem(int formFactor) throws XrException {
        try (MemoryStack stack = stackPush()) {
            XrSystemGetInfo systemInfo = XrSystemGetInfo.mallocStack();
            systemInfo.set(XR10.XR_TYPE_SYSTEM_GET_INFO, 0, formFactor);

            LongBuffer lBuf = stack.longs(0);
            checkSafe(XR10.xrGetSystem(handle, systemInfo, lBuf), "xrGetSystem");
            long systemID = lBuf.get();
            if (systemID == 0) {
                throw new XrException("No compatible headset detected");
            }
            return new OpenXRSystem(this, formFactor, systemID);
        }
    }

    public OpenXRSession createSession(int viewConfigurationType, OpenXRSystem system) throws XrException {
        try (MemoryStack stack = stackPush()) {
            XrSessionCreateInfo sessionCreateInfo = XrSessionCreateInfo.mallocStack().set(
                    XR10.XR_TYPE_SESSION_CREATE_INFO,
                    system.createOpenGLBinding().address(),
                    0,
                    system.handle
            );

            PointerBuffer pp = stack.mallocPointer(1);
            checkSafe(XR10.xrCreateSession(handle, sessionCreateInfo, pp), "xrCreateSession");
            return new OpenXRSession(new XrSession(pp.get(0), handle), system, viewConfigurationType);
        }
    }

    public XrEventDataBaseHeader nextEvent() {
        // It is sufficient to just clear the XrEventDataBuffer header to
        // XR_TYPE_EVENT_DATA_BUFFER rather than recreate it every time
        eventDataBuffer.clear();
        eventDataBuffer.type(XR10.XR_TYPE_EVENT_DATA_BUFFER);

        int result = XR10.xrPollEvent(handle, eventDataBuffer);
        check(result, "xrPollEvent");
        if (result == XR10.XR_EVENT_UNAVAILABLE) {
            return null;
        }
        if (eventDataBuffer.type() == XR10.XR_TYPE_EVENT_DATA_EVENTS_LOST) {
            XrEventDataEventsLost dataEventsLost = XrEventDataEventsLost.create(eventDataBuffer.address());
            LOGGER.info(String.format("%d events lost", dataEventsLost.lostEventCount()));
        }
        return XrEventDataBaseHeader.create(eventDataBuffer.address());
    }

    public void checkSafe(int result, String method) throws XrException {
        if (result >= 0) return;

        ByteBuffer str = stackMalloc(XR10.XR_MAX_RESULT_STRING_SIZE);
        if (XR10.xrResultToString(handle, result, str) >= 0) {
            throw new XrException(method + " returned " + memUTF8(memAddress(str)));
        }
    }

    public long getPath(String pathString) {
        return paths.computeIfAbsent(pathString, s -> {
            try (MemoryStack ignored = stackPush()) {
                LongBuffer buf = stackMallocLong(1);
                int xrResult = XR10.xrStringToPath(handle, pathString, buf);
                if (xrResult == XR10.XR_ERROR_PATH_FORMAT_INVALID) {
                    throw new XrRuntimeException("Invalid path:\"" + pathString + "\"");
                } else {
                    check(xrResult, "xrStringToPath");
                }
                return buf.get();
            }
        });
    }

    public void check(int result, String method) {
        if (result >= 0) return;

        ByteBuffer str = stackMalloc(XR10.XR_MAX_RESULT_STRING_SIZE);
        if (XR10.xrResultToString(handle, result, str) >= 0) {
            throw new XrRuntimeException(method + " returned:" + memUTF8(memAddress(str)));
        }
    }

    @Override
    public void close() {
        XR10.xrDestroyInstance(handle);
        eventDataBuffer.close();
        var session = MCXRPlayClient.OPEN_XR.session;
        if (session != null) {
            session.close();
        }
        //TODO sessions
    }
}
