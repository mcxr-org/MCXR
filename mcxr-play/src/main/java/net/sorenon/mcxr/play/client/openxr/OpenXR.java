package net.sorenon.mcxr.play.client.openxr;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.sorenon.mcxr.core.JOMLUtil;
import net.sorenon.mcxr.core.MCXRCore;
import net.sorenon.mcxr.play.client.FlatGuiManager;
import net.sorenon.mcxr.play.client.MCXRPlayClient;
import net.sorenon.mcxr.core.Pose;
import net.sorenon.mcxr.play.client.accessor.MinecraftClientExt;
import net.sorenon.mcxr.play.client.accessor.MouseExt;
import net.sorenon.mcxr.play.client.input.ControllerPosesImpl;
import net.sorenon.mcxr.play.client.input.FlatGuiActionSet;
import net.sorenon.mcxr.play.client.input.VanillaCompatActionSet;
import net.sorenon.mcxr.play.client.rendering.MainRenderTarget;
import net.sorenon.mcxr.play.client.rendering.RenderPass;
import net.sorenon.mcxr.play.client.rendering.XrCamera;
import net.sorenon.mcxr.play.client.rendering.XrFramebuffer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Quaterniond;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWNativeWGL;
import org.lwjgl.glfw.GLFWNativeWin32;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.openxr.*;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.Platform;
import org.lwjgl.system.Struct;
import org.lwjgl.system.windows.User32;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.HashMap;

import static org.lwjgl.opengl.GL30.GL_SRGB8_ALPHA8;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * This class is where most of the OpenXR stuff happens
 */
public class OpenXR {

    //XR globals
    //Init
    public XrInstance xrInstance;
    public XrDebugUtilsMessengerEXT xrDebug;
    public long systemID;
    public Struct graphicsBinding;
    public XrSession xrSession;
    XrSpace xrAppSpace;  //The base world space in which the program runs
    XrSpace xrViewSpace;
    long glColorFormat;
    XrView.Buffer views;       //Each view represents an eye in the headset with views[0] being left and views[1] being right
    public Swapchain[] swapchains;  //One swapchain per view (for now, it could be optimal to use the same swapchain for both views)
    int viewConfigType = XR10.XR_VIEW_CONFIGURATION_TYPE_PRIMARY_STEREO;

    //Runtime
    public XrEventDataBuffer eventDataBuffer;
    public int sessionState;
    public boolean sessionRunning;

    MinecraftClient client = MinecraftClient.getInstance();
    MinecraftClientExt clientExt = ((MinecraftClientExt) MinecraftClient.getInstance());

    public static Logger LOGGER = LogManager.getLogger("MCXR");

    public static final XrPosef identityPose = XrPosef.malloc().set(
            XrQuaternionf.mallocStack().set(0, 0, 0, 1),
            XrVector3f.callocStack()
    );

    /**
     * Creates an array of XrStructs with their types pre set to @param type
     */
    private static ByteBuffer mallocAndFillBufferStack(int capacity, int sizeof, int type) {
        ByteBuffer b = stackMalloc(capacity * sizeof);

        for (int i = 0; i < capacity; i++) {
            b.position(i * sizeof);
            b.putInt(type);
        }
        b.rewind();
        return b;
    }

    private static ByteBuffer mallocAndFillBufferHeap(int capacity, int sizeof, int type) {
        ByteBuffer b = memAlloc(capacity * sizeof);

        for (int i = 0; i < capacity; i++) {
            b.position(i * sizeof);
            b.putInt(type);
        }
        b.rewind();
        return b;
    }

    public void createOpenXRInstance() {
        try (MemoryStack stack = stackPush()) {
            IntBuffer numExtensions = stack.mallocInt(1);
            check(XR10.xrEnumerateInstanceExtensionProperties((ByteBuffer) null, numExtensions, null));

            XrExtensionProperties.Buffer properties = new XrExtensionProperties.Buffer(
                    mallocAndFillBufferStack(numExtensions.get(0), XrExtensionProperties.SIZEOF, XR10.XR_TYPE_EXTENSION_PROPERTIES)
            );

            check(XR10.xrEnumerateInstanceExtensionProperties((ByteBuffer) null, numExtensions, properties));

            LOGGER.info(String.format("OpenXR loaded with %d extensions", numExtensions.get(0)));
            LOGGER.info("~~~~~~~~~~~~~~~~~~");
            PointerBuffer extensions = stack.mallocPointer(numExtensions.get(0));
            boolean missingOpenGL = true;
            while (properties.hasRemaining()) {
                XrExtensionProperties prop = properties.get();
                String extensionName = prop.extensionNameString();
                LOGGER.info(extensionName);
                extensions.put(memASCII(extensionName));
                if (extensionName.equals(KHROpenglEnable.XR_KHR_OPENGL_ENABLE_EXTENSION_NAME)) {
                    missingOpenGL = false;
                }
            }
            extensions.rewind();
            LOGGER.info("~~~~~~~~~~~~~~~~~~");

            if (missingOpenGL) {
                throw new IllegalStateException("OpenXR library does not provide required extension: " + KHROpenglEnable.XR_KHR_OPENGL_ENABLE_EXTENSION_NAME);
            }

            XrApplicationInfo applicationInfo = XrApplicationInfo.mallocStack();
            applicationInfo.apiVersion(XR10.XR_CURRENT_API_VERSION);
            applicationInfo.applicationName(stack.UTF8("[MCXR] Minecraft VR"));

            XrInstanceCreateInfo createInfo = XrInstanceCreateInfo.mallocStack();
            createInfo.set(
                    XR10.XR_TYPE_INSTANCE_CREATE_INFO,
                    0,
                    0,
                    applicationInfo,
                    null,
                    extensions
            );

            PointerBuffer instancePtr = stack.mallocPointer(1);

            int xrResult = XR10.xrCreateInstance(createInfo, instancePtr);
            if (xrResult == XR10.XR_ERROR_RUNTIME_FAILURE) {
                throw new XrResultException("Failed to create xrInstance, are you sure your headset is plugged in?");
            } else {
                check(xrResult);
            }

            xrInstance = new XrInstance(instancePtr.get(0), createInfo);

            if (xrInstance.getCapabilities().XR_EXT_debug_utils) {
                XrDebugUtilsMessengerCreateInfoEXT createInfoDebug = XrDebugUtilsMessengerCreateInfoEXT.mallocStack().set(
                        EXTDebugUtils.XR_TYPE_DEBUG_UTILS_MESSENGER_CREATE_INFO_EXT,
                        NULL,
                        EXTDebugUtils.XR_DEBUG_UTILS_MESSAGE_SEVERITY_VERBOSE_BIT_EXT |
                                EXTDebugUtils.XR_DEBUG_UTILS_MESSAGE_SEVERITY_INFO_BIT_EXT |
                                EXTDebugUtils.XR_DEBUG_UTILS_MESSAGE_SEVERITY_WARNING_BIT_EXT |
                                EXTDebugUtils.XR_DEBUG_UTILS_MESSAGE_SEVERITY_ERROR_BIT_EXT,
                        EXTDebugUtils.XR_DEBUG_UTILS_MESSAGE_TYPE_GENERAL_BIT_EXT |
                                EXTDebugUtils.XR_DEBUG_UTILS_MESSAGE_TYPE_VALIDATION_BIT_EXT |
                                EXTDebugUtils.XR_DEBUG_UTILS_MESSAGE_TYPE_PERFORMANCE_BIT_EXT |
                                EXTDebugUtils.XR_DEBUG_UTILS_MESSAGE_TYPE_CONFORMANCE_BIT_EXT,
                        new DebugCallback(),
                        NULL);

                PointerBuffer pp = stackMallocPointer(1);
                EXTDebugUtils.xrCreateDebugUtilsMessengerEXT(
                        xrInstance,
                        createInfoDebug,
                        pp
                );
//                xrDebug = new XrDebugUtilsMessengerEXT(pp.get(0), xrInstance.getCapabilities());
            }
        }
    }

    private static class DebugCallback implements XrDebugUtilsMessengerCallbackEXTI {
        @Override
        public int invoke(long severity, long types, long msgPtr, long user_data) {
            // Print the debug message we got! There's a bunch more info we could
            // add here too, but this is a pretty good start, and you can always
            // add a breakpoint this line!
            XrDebugUtilsMessengerCallbackDataEXT msg = XrDebugUtilsMessengerCallbackDataEXT.create(msgPtr);

            LOGGER.info(String.format("%s: %s", memASCII(msg.functionName()), memASCII(msg.message())));
            return 0;
        }
    }

    public void bindToOpenGLAndCreateSession(long windowHandle) {
        try (MemoryStack stack = stackPush()) {
            //Initialize OpenXR's OpenGL compatability
            XrGraphicsRequirementsOpenGLKHR graphicsRequirements = XrGraphicsRequirementsOpenGLKHR.callocStack().type(KHROpenglEnable.XR_TYPE_GRAPHICS_REQUIREMENTS_OPENGL_KHR);
            check(KHROpenglEnable.xrGetOpenGLGraphicsRequirementsKHR(xrInstance, systemID, graphicsRequirements));

            //Check if OpenGL ver is supported by OpenXR ver
            if (graphicsRequirements.minApiVersionSupported() > XR10.XR_MAKE_VERSION(GL11.glGetInteger(GL30.GL_MAJOR_VERSION), GL11.glGetInteger(GL30.GL_MINOR_VERSION), 0)) {
                LOGGER.warn("Runtime does not support desired Graphics API and/or version");
            }

            //Bind the OpenGL context to the OpenXR instance and create the session
            if (Platform.get() == Platform.WINDOWS) {
                graphicsBinding = createWinBinding(windowHandle);
            } else {
                throw new IllegalStateException();
            }

            XrSessionCreateInfo sessionCreateInfo = XrSessionCreateInfo.mallocStack().set(
                    XR10.XR_TYPE_SESSION_CREATE_INFO,
                    graphicsBinding.address(),
                    0,
                    systemID
            );

            PointerBuffer pp = stack.mallocPointer(1);
            check(XR10.xrCreateSession(xrInstance, sessionCreateInfo, pp));
            LOGGER.info(String.valueOf(pp.get(0)));
            xrSession = new XrSession(pp.get(0), xrInstance);
        }

        createXRReferenceSpaces();
    }

    private XrGraphicsBindingOpenGLWin32KHR createWinBinding(long windowHandle) {
        XrGraphicsBindingOpenGLWin32KHR graphicsBinding = XrGraphicsBindingOpenGLWin32KHR.malloc();
        graphicsBinding.set(
                KHROpenglEnable.XR_TYPE_GRAPHICS_BINDING_OPENGL_WIN32_KHR,
                NULL,
                User32.GetDC(GLFWNativeWin32.glfwGetWin32Window(windowHandle)),
                GLFWNativeWGL.glfwGetWGLContext(windowHandle)
        );
        return graphicsBinding;
    }

    public void initializeOpenXRSystem() {
        try (MemoryStack stack = stackPush()) {
            //Get headset
            XrSystemGetInfo systemInfo = XrSystemGetInfo.mallocStack();
            systemInfo.set(XR10.XR_TYPE_SYSTEM_GET_INFO, 0, XR10.XR_FORM_FACTOR_HEAD_MOUNTED_DISPLAY);

            LongBuffer lBuf = stack.longs(0);
            check(XR10.xrGetSystem(xrInstance, systemInfo, lBuf));
            systemID = lBuf.get();
            if (systemID == 0) {
                throw new IllegalStateException("No compatible headset detected");
            }
            LOGGER.info(String.format("Headset found with System ID:%d", systemID));
        }
    }

    public void createXRReferenceSpaces() {
        try (MemoryStack stack = stackPush()) {
            XrPosef identityPose = XrPosef.mallocStack();
            identityPose.set(
                    XrQuaternionf.mallocStack().set(0, 0, 0, 1),
                    XrVector3f.callocStack()
            );

            XrReferenceSpaceCreateInfo referenceSpaceCreateInfo = XrReferenceSpaceCreateInfo.mallocStack();
            referenceSpaceCreateInfo.set(
                    XR10.XR_TYPE_REFERENCE_SPACE_CREATE_INFO,
                    NULL,
                    XR10.XR_REFERENCE_SPACE_TYPE_STAGE,
                    identityPose
            );
            PointerBuffer pp = stack.mallocPointer(1);
            check(XR10.xrCreateReferenceSpace(xrSession, referenceSpaceCreateInfo, pp));
            xrAppSpace = new XrSpace(pp.get(0), xrSession);

            referenceSpaceCreateInfo.referenceSpaceType(XR10.XR_REFERENCE_SPACE_TYPE_VIEW);
            check(XR10.xrCreateReferenceSpace(xrSession, referenceSpaceCreateInfo, pp));
            xrViewSpace = new XrSpace(pp.get(0), xrSession);
        }
    }

    public void createXRSwapchains() {
        try (MemoryStack stack = stackPush()) {
            XrSystemProperties systemProperties = XrSystemProperties.callocStack().type(XR10.XR_TYPE_SYSTEM_PROPERTIES);
            check(XR10.xrGetSystemProperties(xrInstance, systemID, systemProperties));

            LOGGER.debug(String.format("Headset name:%s vendor:%d ", memUTF8(systemProperties.systemName()), systemProperties.vendorId()));
            XrSystemTrackingProperties trackingProperties = systemProperties.trackingProperties();
            LOGGER.debug(String.format("Headset orientationTracking:%b positionTracking:%b ", trackingProperties.orientationTracking(), trackingProperties.positionTracking()));
            XrSystemGraphicsProperties graphicsProperties = systemProperties.graphicsProperties();
            LOGGER.debug(String.format("Headset MaxWidth:%d MaxHeight:%d MaxLayerCount:%d ", graphicsProperties.maxSwapchainImageWidth(), graphicsProperties.maxSwapchainImageHeight(), graphicsProperties.maxLayerCount()));

            IntBuffer intBuf = stack.mallocInt(1);
            check(XR10.xrEnumerateViewConfigurationViews(xrInstance, systemID, viewConfigType, intBuf, null));
            XrViewConfigurationView.Buffer viewConfigs = new XrViewConfigurationView.Buffer(
                    mallocAndFillBufferStack(intBuf.get(0), XrViewConfigurationView.SIZEOF, XR10.XR_TYPE_VIEW_CONFIGURATION_VIEW)
            );
            check(XR10.xrEnumerateViewConfigurationViews(xrInstance, systemID, viewConfigType, intBuf, viewConfigs));
            int viewCountNumber = intBuf.get(0);

            views = new XrView.Buffer(
                    mallocAndFillBufferHeap(viewCountNumber, XrView.SIZEOF, XR10.XR_TYPE_VIEW)
            );

            if (viewCountNumber > 0) {
                check(XR10.xrEnumerateSwapchainFormats(xrSession, intBuf, null));
                LongBuffer swapchainFormats = stack.mallocLong(intBuf.get(0));
                check(XR10.xrEnumerateSwapchainFormats(xrSession, intBuf, swapchainFormats));

                long[] desiredSwapchainFormats = {
                        GL_SRGB8_ALPHA8,
                        //SRGB formats perform automatic inverse gamma correction inside the driver
                        //but all SRGB formats are linear so in the future we want to do inverse gamma correction ourselves
                        //and use GL_RGBA16F instead
                        GL11.GL_RGB10_A2,
                        GL30.GL_RGBA16F,
                        // The two below should only be used as a fallback, as they are linear color formats without enough bits for color
                        // depth, thus leading to banding.
                        GL11.GL_RGBA8,
                        GL31.GL_RGBA8_SNORM
                };
                for (long glFormatIter : desiredSwapchainFormats) {
                    swapchainFormats.rewind();
                    while (swapchainFormats.hasRemaining()) {
                        if (glFormatIter == swapchainFormats.get()) {
                            glColorFormat = glFormatIter;
                            break;
                        }
                    }
                    if (glColorFormat != 0) {
                        break;
                    }
                }
                if (glColorFormat == 0) {
                    throw new IllegalStateException("No compatable swapchain / framebuffer format availible");
                }

                swapchains = new Swapchain[viewCountNumber];
                for (int i = 0; i < viewCountNumber; i++) {
                    XrViewConfigurationView viewConfig = viewConfigs.get(i);
                    XrSwapchainCreateInfo swapchainCreateInfo = XrSwapchainCreateInfo.mallocStack();
                    Swapchain swapchainWrapper = new Swapchain();

                    swapchainCreateInfo.set(
                            XR10.XR_TYPE_SWAPCHAIN_CREATE_INFO,
                            NULL,
                            0,
                            XR10.XR_SWAPCHAIN_USAGE_SAMPLED_BIT | XR10.XR_SWAPCHAIN_USAGE_COLOR_ATTACHMENT_BIT,
                            glColorFormat,
                            viewConfig.recommendedSwapchainSampleCount(),
                            viewConfig.recommendedImageRectWidth(),
                            viewConfig.recommendedImageRectHeight(),
                            1,
                            1,
                            1
                    );

                    PointerBuffer pp = stack.mallocPointer(1);
                    check(XR10.xrCreateSwapchain(xrSession, swapchainCreateInfo, pp));
                    swapchainWrapper.handle = new XrSwapchain(pp.get(0), xrSession);
                    swapchainWrapper.width = swapchainCreateInfo.width();
                    swapchainWrapper.height = swapchainCreateInfo.height();

                    check(XR10.xrEnumerateSwapchainImages(swapchainWrapper.handle, intBuf, null));
                    int imageCount = intBuf.get(0);
                    XrSwapchainImageOpenGLKHR.Buffer swapchainImageBuffer = XrSwapchainImageOpenGLKHR.create(imageCount);
                    for (XrSwapchainImageOpenGLKHR image : swapchainImageBuffer) {
                        image.type(KHROpenglEnable.XR_TYPE_SWAPCHAIN_IMAGE_OPENGL_KHR);
                    }

                    check(XR10.xrEnumerateSwapchainImages(swapchainWrapper.handle, intBuf, XrSwapchainImageBaseHeader.create(swapchainImageBuffer.address(), swapchainImageBuffer.capacity())));
                    swapchainWrapper.images = swapchainImageBuffer;
                    swapchainWrapper.framebuffer = new XrFramebuffer(swapchainWrapper.width, swapchainWrapper.height);
                    swapchainWrapper.framebuffer.setClearColor(239 / 255f, 50 / 255f, 61 / 255f, 255 / 255f);
                    swapchains[i] = swapchainWrapper;
                }
            }
        }
    }

    public boolean pollEvents() {
//        glfwPollEvents();
        XrEventDataBaseHeader event = readNextOpenXREvent();
        if (event == null) {
            return false;
        }

        do {
            switch (event.type()) {
                case XR10.XR_TYPE_EVENT_DATA_INSTANCE_LOSS_PENDING: {
                    XrEventDataInstanceLossPending instanceLossPending = XrEventDataInstanceLossPending.create(event.address());
                    LOGGER.warn("XrEventDataInstanceLossPending by " + instanceLossPending.lossTime());
//            *requestRestart = true;
                    return true;
                }
                case XR10.XR_TYPE_EVENT_DATA_SESSION_STATE_CHANGED: {
                    XrEventDataSessionStateChanged sessionStateChangedEvent = XrEventDataSessionStateChanged.create(event.address());
                    return OpenXRHandleSessionStateChangedEvent(sessionStateChangedEvent/*, requestRestart*/);
                }
                case XR10.XR_TYPE_EVENT_DATA_INTERACTION_PROFILE_CHANGED:
                    break;
                case XR10.XR_TYPE_EVENT_DATA_REFERENCE_SPACE_CHANGE_PENDING:
                default: {
                    LOGGER.debug(String.format("Ignoring event type %d", event.type()));
                    break;
                }
            }
            event = readNextOpenXREvent();
        }
        while (event != null);

        return false;
    }

    private XrEventDataBaseHeader readNextOpenXREvent() {
        // It is sufficient to just clear the XrEventDataBuffer header to
        // XR_TYPE_EVENT_DATA_BUFFER rather than recreate it every time

        eventDataBuffer.clear();
        eventDataBuffer.type(XR10.XR_TYPE_EVENT_DATA_BUFFER);
        int result = XR10.xrPollEvent(xrInstance, eventDataBuffer);
        check(result);
        if (result == XR10.XR_SUCCESS) {
            if (eventDataBuffer.type() == XR10.XR_TYPE_EVENT_DATA_EVENTS_LOST) {
                XrEventDataEventsLost dataEventsLost = XrEventDataEventsLost.create(eventDataBuffer.address());
                LOGGER.info(String.format("%d events lost", dataEventsLost.lostEventCount()));
            }

            return XrEventDataBaseHeader.create(eventDataBuffer.address());
        }
        if (result == XR10.XR_EVENT_UNAVAILABLE) {
            return null;
        }
        throw new IllegalStateException(String.format("[XrResult failure %d in xrPollEvent]", result));
    }

    boolean OpenXRHandleSessionStateChangedEvent(XrEventDataSessionStateChanged stateChangedEvent) {
        int oldState = sessionState;
        sessionState = stateChangedEvent.state();

        LOGGER.debug(String.format("XrEventDataSessionStateChanged: state %s->%s session=%d time=%d", oldState, sessionState, stateChangedEvent.session(), stateChangedEvent.time()));

        if ((stateChangedEvent.session() != NULL) && (stateChangedEvent.session() != xrSession.address())) {
            LOGGER.warn("XrEventDataSessionStateChanged for unknown session");
            return false;
        }

        switch (sessionState) {
            case XR10.XR_SESSION_STATE_READY: {
                assert (xrSession != null);
                XrSessionBeginInfo sessionBeginInfo = XrSessionBeginInfo.mallocStack();
                sessionBeginInfo.set(XR10.XR_TYPE_SESSION_BEGIN_INFO, 0, viewConfigType);
                check(XR10.xrBeginSession(xrSession, sessionBeginInfo));
                sessionRunning = true;
                return false;
            }
            case XR10.XR_SESSION_STATE_STOPPING: {
                assert (xrSession != null);
                sessionRunning = false;
                check(XR10.xrEndSession(xrSession));
                return false;
            }
            case XR10.XR_SESSION_STATE_EXITING: {
                // Do not attempt to restart because user closed this session.
//        *requestRestart = false;
                return true;
            }
            case XR10.XR_SESSION_STATE_LOSS_PENDING: {
                // Poll for a new instance.
//        *requestRestart = true;
                return true;
            }
            default:
                return false;
        }
    }


    public void renderFrameOpenXR() {
        try (MemoryStack stack = stackPush()) {
            XrFrameWaitInfo frameWaitInfo = XrFrameWaitInfo.callocStack();
            frameWaitInfo.type(XR10.XR_TYPE_FRAME_WAIT_INFO);
            XrFrameState frameState = XrFrameState.callocStack();
            frameState.type(XR10.XR_TYPE_FRAME_STATE);
            check(XR10.xrWaitFrame(xrSession, frameWaitInfo, frameState));

            XrFrameBeginInfo frameBeginInfo = XrFrameBeginInfo.callocStack();
            frameBeginInfo.type(XR10.XR_TYPE_FRAME_BEGIN_INFO);
            check(XR10.xrBeginFrame(xrSession, frameBeginInfo));

            XrCompositionLayerProjection layerProjection = XrCompositionLayerProjection.callocStack();
            layerProjection.type(XR10.XR_TYPE_COMPOSITION_LAYER_PROJECTION);
            PointerBuffer layers = stack.callocPointer(1);

            if (frameState.shouldRender()) {
                if (MCXRPlayClient.isXrMode()) {
                    if (renderLayerOpenXR(frameState.predictedDisplayTime(), layerProjection)) {
                        layers.put(layerProjection.address());
                    }
                } else {
                    if (renderLayerBlankOpenXR(frameState.predictedDisplayTime(), layerProjection)) {
                        layers.put(layerProjection.address());
                    }
                }
            }
            layers.flip();

            XrFrameEndInfo frameEndInfo = XrFrameEndInfo.mallocStack();
            frameEndInfo.set(
                    XR10.XR_TYPE_FRAME_END_INFO,
                    0,
                    frameState.predictedDisplayTime(),
                    XR10.XR_ENVIRONMENT_BLEND_MODE_OPAQUE,
                    layers.limit(),
                    layers
            );

            if (layers.limit() > 0) {
                layerProjection.views().free(); //These values were allocated in a child function so they must be freed manually as we could not use the stack
            }

            check(XR10.xrEndFrame(xrSession, frameEndInfo));
        }
    }

    private boolean renderLayerOpenXR(long predictedDisplayTime, XrCompositionLayerProjection layer) {
        try (MemoryStack stack = stackPush()) {
            XrCompositionLayerProjectionView.Buffer projectionLayerViews;

            XrViewState viewState = new XrViewState(stack.calloc(XrViewState.SIZEOF));
            viewState.type(XR10.XR_TYPE_VIEW_STATE);
            IntBuffer intBuf = stack.mallocInt(1);

            XrViewLocateInfo viewLocateInfo = new XrViewLocateInfo(stack.malloc(XrViewLocateInfo.SIZEOF));
            viewLocateInfo.set(XR10.XR_TYPE_VIEW_LOCATE_INFO,
                    0,
                    viewConfigType,
                    predictedDisplayTime,
                    xrAppSpace
            );

            check(XR10.xrLocateViews(xrSession, viewLocateInfo, viewState, intBuf, views));

            if ((viewState.viewStateFlags() & XR10.XR_VIEW_STATE_POSITION_VALID_BIT) == 0 ||
                    (viewState.viewStateFlags() & XR10.XR_VIEW_STATE_ORIENTATION_VALID_BIT) == 0) {
                return false;  // There is no valid tracking poses for the views.
            }
            int viewCountOutput = intBuf.get(0);
            assert (viewCountOutput == views.capacity());
            assert (viewCountOutput == swapchains.length);
            assert (viewCountOutput == 2);

            projectionLayerViews = new XrCompositionLayerProjectionView.Buffer(mallocAndFillBufferHeap(viewCountOutput, XrCompositionLayerProjectionView.SIZEOF, XR10.XR_TYPE_COMPOSITION_LAYER_PROJECTION_VIEW));

            // Update hand position based on the predicted time of when the frame will be rendered! This
            // should result in a more accurate location, and reduce perceived lag.
            if (sessionState == XR10.XR_SESSION_STATE_FOCUSED) {
                for (int i = 0; i < 2; i++) {
                    if (!MCXRPlayClient.handsActionSet.isHandActive[i]) {
                        continue;
                    }
                    setPosesFromSpace(MCXRPlayClient.handsActionSet.poseGripSpaces[i], predictedDisplayTime, MCXRPlayClient.handsActionSet.gripPoses[i]);
                    setPosesFromSpace(MCXRPlayClient.handsActionSet.poseAimSpaces[i], predictedDisplayTime, MCXRPlayClient.handsActionSet.aimPoses[i]);
                }
            }

            setPosesFromSpace(xrViewSpace, predictedDisplayTime, MCXRPlayClient.viewSpacePoses);

            XrCamera camera = (XrCamera) MinecraftClient.getInstance().gameRenderer.getCamera();
            camera.updateXR(this.client.world, this.client.getCameraEntity() == null ? this.client.player : this.client.getCameraEntity(), MCXRPlayClient.viewSpacePoses.getGamePose());
            MainRenderTarget mainRenderTarget = (MainRenderTarget) client.getFramebuffer();

            long frameStartTime = Util.getMeasuringTimeNano();
//            MCXRCore.pose.set(MineXRaftClient.viewSpacePoses.getPhysicalPose());
            if (MinecraftClient.getInstance().player != null ) {
                MCXRCore.INSTANCE.playerPose(
                        MinecraftClient.getInstance().player,
                        MCXRPlayClient.viewSpacePoses.getPhysicalPose());
            }
            clientExt.preRenderXR(true, () -> {
                if (camera.getFocusedEntity() != null) {
                    float tickDelta = client.getTickDelta();
                    Entity entity = camera.getFocusedEntity();
                    MCXRPlayClient.xrOrigin.set(MathHelper.lerp(tickDelta, entity.prevX, entity.getX()) + MCXRPlayClient.xrOffset.x,
                            MathHelper.lerp(tickDelta, entity.prevY, entity.getY()) + MCXRPlayClient.xrOffset.y,
                            MathHelper.lerp(tickDelta, entity.prevZ, entity.getZ()) + MCXRPlayClient.xrOffset.z);
                    MCXRPlayClient.viewSpacePoses.updateGamePose();
                    for (var poses : MCXRPlayClient.handsActionSet.gripPoses) {
                        poses.updateGamePose();
                    }
                    for (var poses : MCXRPlayClient.handsActionSet.aimPoses) {
                        poses.updateGamePose();
                    }
                }
            });

            {
                FlatGuiManager FGM = MCXRPlayClient.INSTANCE.flatGuiManager;
                MCXRPlayClient.renderPass = RenderPass.GUI;
                mainRenderTarget.setFramebuffer(FGM.framebuffer);
                MouseExt mouse = ((MouseExt) MinecraftClient.getInstance().mouse);
                if (FGM.isScreenOpen()) {
                    Pose pose = MCXRPlayClient.handsActionSet.gripPoses[MCXRPlayClient.mainHand].getGamePose();
                    Vector3d pos = new Vector3d(pose.getPos());
                    Vector3f dir = pose.getOrientation().rotateX((float) Math.toRadians(MCXRPlayClient.handPitchAdjust), new Quaternionf()).transform(new Vector3f(0, -1, 0));
                    Vector3d result = FGM.guiRaycast(pos, new Vector3d(dir));
                    if (result != null) {
                        Vector3d vec = result.sub(JOMLUtil.convert(FGM.pos));
                        FGM.rot.invert(new Quaterniond()).transform(vec);
                        vec.y *= ((double) FGM.framebufferWidth / FGM.framebufferHeight);

                        ((MouseExt) MinecraftClient.getInstance().mouse).cursorPos(
                                FGM.framebufferWidth * (0.5 - vec.x),
                                FGM.framebufferHeight * (1 - vec.y)
                        );
                    }
                    FlatGuiActionSet actionSet = MCXRPlayClient.flatGuiActionSet;
                    if (actionSet.pickupState.changedSinceLastSync() || actionSet.quickMoveState.changedSinceLastSync()) {
                        if (actionSet.pickupState.currentState() || actionSet.quickMoveState.currentState()) {
                            mouse.mouseButton(GLFW.GLFW_MOUSE_BUTTON_LEFT, GLFW.GLFW_PRESS, 0);
                        } else {
                            mouse.mouseButton(GLFW.GLFW_MOUSE_BUTTON_LEFT, GLFW.GLFW_RELEASE, 0);
                        }
                    }
                    if (actionSet.splitState.changedSinceLastSync()) {
                        if (actionSet.splitState.currentState()) {
                            mouse.mouseButton(GLFW.GLFW_MOUSE_BUTTON_RIGHT, GLFW.GLFW_PRESS, 0);
                        } else {
                            mouse.mouseButton(GLFW.GLFW_MOUSE_BUTTON_RIGHT, GLFW.GLFW_RELEASE, 0);
                        }
                    }
                    if (actionSet.scrollState.changedSinceLastSync()) {
                        XrVector2f state = actionSet.scrollState.currentState();
                        double sensitivity = 0.25;
                        mouse.mouseScroll(-state.x() * sensitivity, state.y() * sensitivity);
                    }
                } else {
                    VanillaCompatActionSet actionSet = MCXRPlayClient.vanillaCompatActionSet;
                    if (actionSet.attackState.changedSinceLastSync()) {
                        if (actionSet.attackState.currentState()) {
                            mouse.mouseButton(GLFW.GLFW_MOUSE_BUTTON_LEFT, GLFW.GLFW_PRESS, 0);
                        } else {
                            mouse.mouseButton(GLFW.GLFW_MOUSE_BUTTON_LEFT, GLFW.GLFW_RELEASE, 0);
                        }
                    }
                    if (actionSet.inventoryState.currentState()) {
                        long heldTime = predictedDisplayTime - actionSet.inventoryState.lastChangeTime();
                        if (heldTime * 1E-09 > 1) {
                            client.openPauseMenu(false);
                            MCXRPlayClient.XR_INPUT.menuButton = false;
                        }
                    }
                }

                FGM.framebuffer.clear(MinecraftClient.IS_SYSTEM_MAC);
                clientExt.doRenderXR(true, frameStartTime);
                mainRenderTarget.resetFramebuffer();
                MCXRPlayClient.renderPass = RenderPass.VANILLA;
            }
            // Render view to the appropriate part of the swapchain image.
            for (int viewIndex = 0; viewIndex < viewCountOutput; viewIndex++) {
                // Each view has a separate swapchain which is acquired, rendered to, and released.
                Swapchain viewSwapchain = swapchains[viewIndex];

                XrSwapchainImageAcquireInfo acquireInfo = new XrSwapchainImageAcquireInfo(stack.calloc(XrSwapchainImageAcquireInfo.SIZEOF));
                acquireInfo.type(XR10.XR_TYPE_SWAPCHAIN_IMAGE_ACQUIRE_INFO);

                check(XR10.xrAcquireSwapchainImage(viewSwapchain.handle, acquireInfo, intBuf));
                int swapchainImageIndex = intBuf.get(0);

                XrSwapchainImageWaitInfo waitInfo = new XrSwapchainImageWaitInfo(stack.malloc(XrSwapchainImageWaitInfo.SIZEOF));
                waitInfo.set(XR10.XR_TYPE_SWAPCHAIN_IMAGE_WAIT_INFO, 0, XR10.XR_INFINITE_DURATION);

                check(XR10.xrWaitSwapchainImage(viewSwapchain.handle, waitInfo));

                XrCompositionLayerProjectionView projectionLayerView = projectionLayerViews.get(viewIndex);
                projectionLayerView.pose(views.get(viewIndex).pose());
                projectionLayerView.fov(views.get(viewIndex).fov());
                projectionLayerView.subImage().swapchain(viewSwapchain.handle);
                projectionLayerView.subImage().imageRect().offset().set(0, 0);
                projectionLayerView.subImage().imageRect().extent().set(viewSwapchain.width, viewSwapchain.height);

                {
                    XrSwapchainImageOpenGLKHR xrSwapchainImageOpenGLKHR = viewSwapchain.images.get(swapchainImageIndex);
                    viewSwapchain.framebuffer.setColorAttachment(xrSwapchainImageOpenGLKHR.image());
                    viewSwapchain.framebuffer.endWrite();
                    mainRenderTarget.setXrFramebuffer(viewSwapchain.framebuffer);
                    MCXRPlayClient.fov = views.get(viewIndex).fov();
                    MCXRPlayClient.eyePoses.updatePhysicalPose(views.get(viewIndex).pose(), MCXRPlayClient.yawTurn);
                    MCXRPlayClient.viewIndex = viewIndex;
                    camera.setPose(MCXRPlayClient.eyePoses.getGamePose());
                    MCXRPlayClient.renderPass = RenderPass.WORLD;
                    clientExt.doRenderXR(true, frameStartTime);
                    MCXRPlayClient.renderPass = RenderPass.VANILLA;
                    MCXRPlayClient.fov = null;
                }

                XrSwapchainImageReleaseInfo releaseInfo = new XrSwapchainImageReleaseInfo(stack.calloc(XrSwapchainImageReleaseInfo.SIZEOF));
                releaseInfo.type(XR10.XR_TYPE_SWAPCHAIN_IMAGE_RELEASE_INFO);
                check(XR10.xrReleaseSwapchainImage(viewSwapchain.handle, releaseInfo));
            }
            mainRenderTarget.resetFramebuffer();
            camera.setPose(MCXRPlayClient.viewSpacePoses.getGamePose());
            clientExt.postRenderXR(true);

            layer.space(xrAppSpace);
            layer.views(projectionLayerViews);
            return true;
        }
    }

    private boolean renderLayerBlankOpenXR(long predictedDisplayTime, XrCompositionLayerProjection layer) {
        try (MemoryStack stack = stackPush()) {
            XrCompositionLayerProjectionView.Buffer projectionLayerViews;

            XrViewState viewState = new XrViewState(stack.calloc(XrViewState.SIZEOF));
            viewState.type(XR10.XR_TYPE_VIEW_STATE);
            IntBuffer intBuf = stack.mallocInt(1);

            XrViewLocateInfo viewLocateInfo = new XrViewLocateInfo(stack.malloc(XrViewLocateInfo.SIZEOF));
            viewLocateInfo.set(XR10.XR_TYPE_VIEW_LOCATE_INFO,
                    0,
                    viewConfigType,
                    predictedDisplayTime,
                    xrAppSpace
            );

            check(XR10.xrLocateViews(xrSession, viewLocateInfo, viewState, intBuf, views));

            if ((viewState.viewStateFlags() & XR10.XR_VIEW_STATE_POSITION_VALID_BIT) == 0 ||
                    (viewState.viewStateFlags() & XR10.XR_VIEW_STATE_ORIENTATION_VALID_BIT) == 0) {
                return false;  // There is no valid tracking poses for the views.
            }
            int viewCountOutput = intBuf.get(0);
            assert (viewCountOutput == views.capacity());
            assert (viewCountOutput == swapchains.length);

            projectionLayerViews = new XrCompositionLayerProjectionView.Buffer(mallocAndFillBufferHeap(viewCountOutput, XrCompositionLayerProjectionView.SIZEOF, XR10.XR_TYPE_COMPOSITION_LAYER_PROJECTION_VIEW));

            //For some reason this has to be called between frame start and frame end or the OpenXR runtime crashes
            clientExt.render();

            // Render view to the appropriate part of the swapchain image.
            for (int viewIndex = 0; viewIndex < viewCountOutput; viewIndex++) {
                // Each view has a separate swapchain which is acquired, rendered to, and released.
                Swapchain viewSwapchain = swapchains[viewIndex];

                XrSwapchainImageAcquireInfo acquireInfo = new XrSwapchainImageAcquireInfo(stack.calloc(XrSwapchainImageAcquireInfo.SIZEOF));
                acquireInfo.type(XR10.XR_TYPE_SWAPCHAIN_IMAGE_ACQUIRE_INFO);

                check(XR10.xrAcquireSwapchainImage(viewSwapchain.handle, acquireInfo, intBuf));
                int swapchainImageIndex = intBuf.get(0);

                XrSwapchainImageWaitInfo waitInfo = new XrSwapchainImageWaitInfo(stack.malloc(XrSwapchainImageWaitInfo.SIZEOF));
                waitInfo.set(XR10.XR_TYPE_SWAPCHAIN_IMAGE_WAIT_INFO, 0, XR10.XR_INFINITE_DURATION);

                check(XR10.xrWaitSwapchainImage(viewSwapchain.handle, waitInfo));

                XrCompositionLayerProjectionView projectionLayerView = projectionLayerViews.get(viewIndex);
                projectionLayerView.pose(views.get(viewIndex).pose());
                projectionLayerView.fov(views.get(viewIndex).fov());
                projectionLayerView.subImage().swapchain(viewSwapchain.handle);
                projectionLayerView.subImage().imageRect().offset().set(0, 0);
                projectionLayerView.subImage().imageRect().extent().set(viewSwapchain.width, viewSwapchain.height);

                XrSwapchainImageOpenGLKHR xrSwapchainImageOpenGLKHR = viewSwapchain.images.get(swapchainImageIndex);
                viewSwapchain.framebuffer.setColorAttachment(xrSwapchainImageOpenGLKHR.image());
                viewSwapchain.framebuffer.clear(MinecraftClient.IS_SYSTEM_MAC);

                XrSwapchainImageReleaseInfo releaseInfo = new XrSwapchainImageReleaseInfo(stack.calloc(XrSwapchainImageReleaseInfo.SIZEOF));
                releaseInfo.type(XR10.XR_TYPE_SWAPCHAIN_IMAGE_RELEASE_INFO);
                check(XR10.xrReleaseSwapchainImage(viewSwapchain.handle, releaseInfo));
            }

            layer.space(xrAppSpace);
            layer.views(projectionLayerViews);
            return true;
        }
    }

    public void check(int result) throws IllegalStateException {
        if (result >= 0) return;

        if (xrInstance != null) {
            ByteBuffer str = stackMalloc(XR10.XR_MAX_RESULT_STRING_SIZE);
            if (XR10.xrResultToString(xrInstance, result, str) >= 0) {
                throw new XrResultException(memUTF8Safe(str));
            }
        }
        throw new XrResultException("XR method returned " + result);
    }

    public void setPosesFromSpace(XrSpace handSpace, long time, ControllerPosesImpl result) {
        try (MemoryStack ignored = stackPush()) {
            XrSpaceLocation space_location = XrSpaceLocation.callocStack().type(XR10.XR_TYPE_SPACE_LOCATION);
            int res = XR10.xrLocateSpace(handSpace, xrAppSpace, time, space_location);
            if (res == XR10.XR_SUCCESS &&
                    (space_location.locationFlags() & XR10.XR_SPACE_LOCATION_POSITION_VALID_BIT) != 0 &&
                    (space_location.locationFlags() & XR10.XR_SPACE_LOCATION_ORIENTATION_VALID_BIT) != 0) {

                result.updatePhysicalPose(space_location.pose(), MCXRPlayClient.yawTurn);
            }
        }
    }

    private final HashMap<String, Long> paths = new HashMap<>();

    public long getPath(String pathString) {
        return paths.computeIfAbsent(pathString, s -> {
            try (MemoryStack ignored = stackPush()) {
                LongBuffer buf = stackMallocLong(1);
                int xrResult = XR10.xrStringToPath(xrInstance, pathString, buf);
                if (xrResult == XR10.XR_ERROR_PATH_FORMAT_INVALID) {
                    throw new XrResultException("Invalid path:\"" + pathString + "\"");
                } else {
                    check(xrResult);
                }
                return buf.get();
            }
        });
    }
}