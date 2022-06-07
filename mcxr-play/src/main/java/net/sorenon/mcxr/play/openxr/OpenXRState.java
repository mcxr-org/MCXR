package net.sorenon.mcxr.play.openxr;

import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.play.PlayOptions;
import net.sorenon.mcxr.play.input.XrInput;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.PointerBuffer;
import org.lwjgl.openxr.*;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * This class is where most of the OpenXR stuff happens
 */
public class OpenXRState {
    public OpenXRInstance instance;
    public OpenXRSession session;

    public XrException createException;

    public static Logger LOGGER = LogManager.getLogger("MCXR");

    public static final XrPosef POSE_IDENTITY = XrPosef.malloc().set(
            XrQuaternionf.malloc().set(0, 0, 0, 1),
            XrVector3f.calloc()
    );

    /**
     * Creates an array of XrStructs with their types pre set to @param type
     */
    static ByteBuffer bufferStack(int capacity, int sizeof, int type) {
        ByteBuffer b = stackCalloc(capacity * sizeof);

        for (int i = 0; i < capacity; i++) {
            b.position(i * sizeof);
            b.putInt(type);
        }
        b.rewind();
        return b;
    }

    static ByteBuffer bufferHeap(int capacity, int sizeof, int type) {
        ByteBuffer b = memCalloc(capacity * sizeof);

        for (int i = 0; i < capacity; i++) {
            b.position(i * sizeof);
            b.putInt(type);
        }
        b.rewind();
        return b;
    }

    //TODO do this asynchronously
    public void tryInitialize() {
        if (PlayOptions.xrUninitialized) {
            return;
        }
        try {
            XR.getFunctionProvider();
        } catch(IllegalStateException exception) {
            XR.create("openxr_loader");
        }

        if (session != null) session.close();
        session = null;
        MCXRPlayClient.MCXR_GAME_RENDERER.setSession(null);
        if (instance != null) instance.close();
        instance = null;

        try {
            instance = createOpenXRInstance();
            session = instance.createSession(instance.getSystem(XR10.XR_FORM_FACTOR_HEAD_MOUNTED_DISPLAY));
            session.createXRReferenceSpaces();
            session.createSwapchain();
            XrInput.reinitialize(session);
            MCXRPlayClient.MCXR_GAME_RENDERER.setSession(session);
        } catch (Exception e) {
            LOGGER.error("Exception caught while initializing OpenXR", e);
            if (e instanceof XrException xrException) {
                createException = xrException;
            }
            if (instance != null) instance.close();
            instance = null;
            MCXRPlayClient.OPEN_XR_STATE.session = null;
        }
    }

    public OpenXRInstance createOpenXRInstance() throws XrException {
        try (MemoryStack stack = stackPush()) {
            IntBuffer numExtensions = stack.mallocInt(1);
            checkPanic(XR10.xrEnumerateInstanceExtensionProperties((ByteBuffer) null, numExtensions, null));

            XrExtensionProperties.Buffer properties = new XrExtensionProperties.Buffer(
                    bufferStack(numExtensions.get(0), XrExtensionProperties.SIZEOF, XR10.XR_TYPE_EXTENSION_PROPERTIES)
            );

            checkPanic(XR10.xrEnumerateInstanceExtensionProperties((ByteBuffer) null, numExtensions, properties));

            boolean missingOpenGL = true;
            PointerBuffer extensions = stackCallocPointer(3);
            while (properties.hasRemaining()) {
                XrExtensionProperties prop = properties.get();
                String extensionName = prop.extensionNameString();
                if (extensionName.equals(KHROpenGLEnable.XR_KHR_OPENGL_ENABLE_EXTENSION_NAME)) {
                    missingOpenGL = false;
                    extensions.put(memAddress(stackUTF8(KHROpenGLEnable.XR_KHR_OPENGL_ENABLE_EXTENSION_NAME)));
                }
                if (extensionName.equals(EXTHPMixedRealityController.XR_EXT_HP_MIXED_REALITY_CONTROLLER_EXTENSION_NAME)) {
                    extensions.put(memAddress(stackUTF8(EXTHPMixedRealityController.XR_EXT_HP_MIXED_REALITY_CONTROLLER_EXTENSION_NAME)));
                }
                if (extensionName.equals(HTCViveCosmosControllerInteraction.XR_HTC_VIVE_COSMOS_CONTROLLER_INTERACTION_EXTENSION_NAME)) {
                    extensions.put(memAddress(stackUTF8(HTCViveCosmosControllerInteraction.XR_HTC_VIVE_COSMOS_CONTROLLER_INTERACTION_EXTENSION_NAME)));
                }
            }

            if (missingOpenGL) {
                throw new XrException(0, "OpenXR runtime does not support OpenGL, try using SteamVR instead");
            }

            XrApplicationInfo applicationInfo = XrApplicationInfo.malloc(stack);
            applicationInfo.apiVersion(XR10.XR_CURRENT_API_VERSION);
            applicationInfo.applicationName(stack.UTF8("[MCXR] Minecraft VR"));
            applicationInfo.applicationVersion(1);

            XrInstanceCreateInfo createInfo = XrInstanceCreateInfo.malloc(stack);
            createInfo.set(
                    XR10.XR_TYPE_INSTANCE_CREATE_INFO,
                    0,
                    0,
                    applicationInfo,
                    null,
                    extensions.flip()
            );

            PointerBuffer instancePtr = stack.mallocPointer(1);

            int xrResult = XR10.xrCreateInstance(createInfo, instancePtr);
            if (xrResult == XR10.XR_ERROR_RUNTIME_FAILURE) {
                throw new XrException(xrResult, "Failed to create xrInstance, are you sure your headset is plugged in?");
            } else if (xrResult == XR10.XR_ERROR_INSTANCE_LOST) {
                throw new XrException(xrResult, "Failed to create xrInstance due to runtime updating");
            } else if (xrResult < 0) {
                throw new XrException(xrResult, "XR method returned " + xrResult);
            }

            return new OpenXRInstance(new XrInstance(instancePtr.get(0), createInfo));
        }
    }

    /**
     * @return true if the game should just render normally
     */
    public boolean loop() {
        if (instance != null && PlayOptions.xrUninitialized) {
            if (session != null) session.close();
            session = null;
            MCXRPlayClient.MCXR_GAME_RENDERER.setSession(null);
            if (instance != null) instance.close();
            instance = null;
        }

        if (session == null) {
            return true;
        }

        if (instance.pollEvents()) {
            throw new XrRuntimeException(XR10.XR_ERROR_INSTANCE_LOST, "Instance loss pending");
        }

        if (session.running) {
            boolean disabled = PlayOptions.xrPaused;
            session.pollActions(disabled);
            MCXRPlayClient.MCXR_GAME_RENDERER.renderFrame(disabled);
            return !MCXRPlayClient.MCXR_GAME_RENDERER.isXrMode();
        }
        return true;
    }

    private void checkPanic(int result) {
        if (result >= 0) return;

        if (instance != null) {
            ByteBuffer str = stackMalloc(XR10.XR_MAX_RESULT_STRING_SIZE);
            if (XR10.xrResultToString(instance.handle, result, str) >= 0) {
                throw new XrRuntimeException(result, memUTF8Safe(str));
            }
        }
        throw new XrRuntimeException(result, "XR method returned " + result);
    }
}