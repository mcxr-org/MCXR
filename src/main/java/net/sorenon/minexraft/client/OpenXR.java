package net.sorenon.minexraft.client;

import com.mojang.blaze3d.platform.FramebufferInfo;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Util;
import net.sorenon.minexraft.client.accessor.MinecraftClientEXT;
import net.sorenon.minexraft.client.rendering.MainRenderTarget;
import net.sorenon.minexraft.client.rendering.RenderPass;
import net.sorenon.minexraft.client.rendering.XrCamera;
import net.sorenon.minexraft.client.rendering.XrFramebuffer;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.openxr.*;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.NativeType;
import org.lwjgl.system.Struct;
import org.lwjgl.system.dyncall.DynCall;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.HashMap;

import static org.lwjgl.opengl.GL30.GL_SRGB8_ALPHA8;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * Slightly tweaked combination of
 * https://github.com/maluoi/OpenXRSamples/blob/master/SingleFileExample
 * and
 * https://github.com/ReliaSolve/OpenXR-OpenGL-Example
 * Can only run on windows until glfw is updated
 * Requires a stereo headset and an install of the OpenXR runtime to run
 */

/**
 * This class is where most of the OpenXR stuff happens, ideally this will be split into classes over time
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
    public Swapchain[] swapchains;  //One swapchain per view
    XrViewConfigurationView.Buffer viewConfigs;
    int viewConfigType = XR10.XR_VIEW_CONFIGURATION_TYPE_PRIMARY_STEREO;

    //Runtime
    public XrEventDataBuffer eventDataBuffer;
    public int sessionState;
    public boolean sessionRunning;

    MinecraftClient client = MinecraftClient.getInstance();
    MinecraftClientEXT clientExt = ((MinecraftClientEXT) MinecraftClient.getInstance());

    public static final XrPosef identityPose = XrPosef.malloc().set(
            XrQuaternionf.mallocStack().set(0, 0, 0, 1),
            XrVector3f.callocStack()
    );

    public static class Swapchain {
        XrSwapchain handle;
        public int width;
        public int height;
        XrSwapchainImageOpenGLKHR.Buffer images;
        XrFramebuffer framebuffer;
    }

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

            System.out.printf("OpenXR loaded with %d extensions:%n", numExtensions.get(0));
            System.out.println("~~~~~~~~~~~~~~~~~~");
            PointerBuffer extensions = stack.mallocPointer(numExtensions.get(0));
            boolean missingOpenGL = true;
            while (properties.hasRemaining()) {
                XrExtensionProperties prop = properties.get();
                String extensionName = prop.extensionNameString();
                System.out.println(extensionName);
                extensions.put(memASCII(extensionName));
                if (extensionName.equals(KHROpenglEnable.XR_KHR_OPENGL_ENABLE_EXTENSION_NAME)) {
                    missingOpenGL = false;
                }
            }
            extensions.rewind();
            System.out.println("~~~~~~~~~~~~~~~~~~");

            if (missingOpenGL) {
                throw new IllegalStateException("OpenXR library does not provide required extension: " + KHROpenglEnable.XR_KHR_OPENGL_ENABLE_EXTENSION_NAME);
            }

            XrApplicationInfo applicationInfo = XrApplicationInfo.mallocStack();
            applicationInfo.apiVersion(XR10.XR_CURRENT_API_VERSION);
            applicationInfo.applicationName(stack.UTF8("HelloOpenXR"));

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
            check(XR10.xrCreateInstance(createInfo, instancePtr));
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

            System.out.printf("%s: %s\n", memASCII(msg.functionName()), memASCII(msg.message()));
            return 0;
        }
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
            System.out.printf("Headset found with System ID:%d\n", systemID);
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
//            ByteBuffer buf = stack.calloc(XrSystemProperties.SIZEOF);
//            buf.putInt(XR10.XR_TYPE_SYSTEM_PROPERTIES);
//            buf.rewind();
            XrSystemProperties systemProperties = XrSystemProperties.callocStack();
            check(XR10.xrGetSystemProperties(xrInstance, systemID, systemProperties));

            System.out.printf("Headset name:%s vendor:%d \n", memUTF8(systemProperties.systemName()), systemProperties.vendorId());
            XrSystemTrackingProperties trackingProperties = systemProperties.trackingProperties();
            System.out.printf("Headset orientationTracking:%b positionTracking:%b \n", trackingProperties.orientationTracking(), trackingProperties.positionTracking());
            XrSystemGraphicsProperties graphicsProperties = systemProperties.graphicsProperties();
            System.out.printf("Headset MaxWidth:%d MaxHeight:%d MaxLayerCount:%d \n", graphicsProperties.maxSwapchainImageWidth(), graphicsProperties.maxSwapchainImageHeight(), graphicsProperties.maxLayerCount());

            IntBuffer intBuf = stack.mallocInt(1);
            check(XR10.xrEnumerateViewConfigurationViews(xrInstance, systemID, viewConfigType, intBuf, null));
            viewConfigs = new XrViewConfigurationView.Buffer(
                    mallocAndFillBufferHeap(intBuf.get(0), XrViewConfigurationView.SIZEOF, XR10.XR_TYPE_VIEW_CONFIGURATION_VIEW)
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
                    System.err.printf("XrEventDataInstanceLossPending by %d\n", instanceLossPending.lossTime());
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
                    System.out.printf("Ignoring event type %d\n", event.type());
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
                System.out.printf("%d events lost\n", dataEventsLost.lostEventCount());
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

        System.out.printf("XrEventDataSessionStateChanged: state %s->%s session=%d time=%d\n", oldState, sessionState, stateChangedEvent.session(), stateChangedEvent.time());

        if ((stateChangedEvent.session() != NULL) && (stateChangedEvent.session() != xrSession.address())) {
            System.err.println("XrEventDataSessionStateChanged for unknown session");
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
            XrFrameWaitInfo frameWaitInfo = XrFrameWaitInfo.callocStack(); //TODO wait until max(nextFrame, nextTick) this will need multithreading and most likely wont be a real issue for a while anyway
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
                if (client.world != null) {
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
            assert (viewCountOutput == viewConfigs.capacity());
            assert (viewCountOutput == swapchains.length);
            assert (viewCountOutput == 2);

            projectionLayerViews = new XrCompositionLayerProjectionView.Buffer(mallocAndFillBufferHeap(viewCountOutput, XrCompositionLayerProjectionView.SIZEOF, XR10.XR_TYPE_COMPOSITION_LAYER_PROJECTION_VIEW));

            // Update hand position based on the predicted time of when the frame will be rendered! This
            // should result in a more accurate location, and reduce perceived lag.
            if (sessionState == XR10.XR_SESSION_STATE_FOCUSED) {
                for (int i = 0; i < 2; i++) {
                    if (!MineXRaftClient.vanillaCompatActionSet.isHandActive[i]) {
                        continue;
                    }
                    setPoseFromSpace(MineXRaftClient.vanillaCompatActionSet.poseGripSpaces[i], predictedDisplayTime, MineXRaftClient.vanillaCompatActionSet.poses[i]);
                }
            }

            setPoseFromSpace(xrViewSpace, predictedDisplayTime, MineXRaftClient.viewSpacePose);

            XrCamera camera = (XrCamera) MinecraftClient.getInstance().gameRenderer.getCamera();
            camera.updateXR(this.client.world, this.client.getCameraEntity() == null ? this.client.player : this.client.getCameraEntity(), MineXRaftClient.viewSpacePose);
            MainRenderTarget mainRenderTarget = (MainRenderTarget) client.getFramebuffer();

            long frameStartTime = Util.getMeasuringTimeNano();
            clientExt.preRenderXR(true, frameStartTime);
            {
                MineXRaftClient.renderPass = RenderPass.GUI;
                mainRenderTarget.setFramebuffer(MineXRaftClient.guiFramebuffer);
                MineXRaftClient.guiFramebuffer.clear(MinecraftClient.IS_SYSTEM_MAC);
                clientExt.doRenderXR(true, frameStartTime);
                mainRenderTarget.resetFramebuffer();
                MineXRaftClient.renderPass = RenderPass.VANILLA;
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
                    GlStateManager.bindFramebuffer(FramebufferInfo.FRAME_BUFFER, 0);
                    mainRenderTarget.setXrFramebuffer(viewSwapchain.framebuffer);

                    MineXRaftClient.viewportRect = projectionLayerView.subImage().imageRect();
                    MineXRaftClient.fov = projectionLayerView.fov();
                    MineXRaftClient.eyePose.set(projectionLayerView.pose(), MineXRaftClient.yawTurn);
//                    MineXRaftClient.eyePose.set(projectionLayerView.pose());
                    MineXRaftClient.viewIndex = viewIndex;
                    if (camera.isReady()) {
                        camera.setEyePose(MineXRaftClient.eyePose, client.getTickDelta());
                    }
                    MineXRaftClient.renderPass = RenderPass.WORLD;
                    clientExt.doRenderXR(true, frameStartTime);
                    MineXRaftClient.renderPass = RenderPass.VANILLA;
                    if (camera.isReady()) {
                        camera.popEyePose();
                    }
                    MineXRaftClient.fov = null;
                    MineXRaftClient.viewportRect = null;

                    mainRenderTarget.resetFramebuffer();
                }

                XrSwapchainImageReleaseInfo releaseInfo = new XrSwapchainImageReleaseInfo(stack.calloc(XrSwapchainImageReleaseInfo.SIZEOF));
                releaseInfo.type(XR10.XR_TYPE_SWAPCHAIN_IMAGE_RELEASE_INFO);
                check(XR10.xrReleaseSwapchainImage(viewSwapchain.handle, releaseInfo));
            }
            clientExt.postRenderXR(true, frameStartTime);

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
            assert (viewCountOutput == viewConfigs.capacity());
            assert (viewCountOutput == swapchains.length);

            projectionLayerViews = new XrCompositionLayerProjectionView.Buffer(mallocAndFillBufferHeap(viewCountOutput, XrCompositionLayerProjectionView.SIZEOF, XR10.XR_TYPE_COMPOSITION_LAYER_PROJECTION_VIEW));

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

    public static class XrResultException extends RuntimeException {
        public XrResultException(String s) {
            super(s);
        }
    }

    public void setPoseFromSpace(XrSpace handSpace, long time, Pose result) {
        try (MemoryStack ignored = stackPush()) {
            XrSpaceLocation space_location = XrSpaceLocation.callocStack().type(XR10.XR_TYPE_SPACE_LOCATION);
            int res = XR10.xrLocateSpace(handSpace, xrAppSpace, time, space_location);
            if (res == XR10.XR_SUCCESS &&
                    (space_location.locationFlags() & XR10.XR_SPACE_LOCATION_POSITION_VALID_BIT) != 0 &&
                    (space_location.locationFlags() & XR10.XR_SPACE_LOCATION_ORIENTATION_VALID_BIT) != 0) {

                result.set(space_location.pose(), MineXRaftClient.yawTurn);
            }
        }
    }

    private final HashMap<String, Long> paths = new HashMap<>();

    public long getPath(String pathString) {
        return paths.computeIfAbsent(pathString, s -> {
            try (MemoryStack ignored = stackPush()) {
                LongBuffer buf = stackMallocLong(1);
                check(XR10.xrStringToPath(xrInstance, pathString, buf));
                return buf.get();
            }
        });
    }
}