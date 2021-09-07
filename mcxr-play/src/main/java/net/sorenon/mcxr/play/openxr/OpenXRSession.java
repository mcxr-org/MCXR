package net.sorenon.mcxr.play.openxr;

import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.play.input.ControllerPoses;
import net.sorenon.mcxr.play.input.XrInput;
import net.sorenon.mcxr.play.input.actionsets.GuiActionSet;
import net.sorenon.mcxr.play.input.actionsets.HandsActionSet;
import net.sorenon.mcxr.play.input.actionsets.VanillaGameplayActionSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.openxr.*;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.nio.LongBuffer;

import static org.lwjgl.opengl.GL21.GL_SRGB8_ALPHA8;
import static org.lwjgl.system.MemoryStack.stackPointers;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class OpenXRSession implements AutoCloseable {
    private static final Logger LOGGER = LogManager.getLogger();

    public final XrSession handle;
    public final OpenXRInstance instance;
    public final OpenXRSystem system;

    public final int viewConfigurationType;

    public XrSpace xrAppSpace;
    public XrSpace xrViewSpace;

    public long glColorFormat;
    public XrView.Buffer views;
    public OpenXRSwapchain[] swapchains;

    public int state;
    public boolean running;

    public OpenXRSession(XrSession handle, OpenXRSystem system, int viewConfigurationType) {
        this.handle = handle;
        this.system = system;
        this.instance = system.instance;
        this.viewConfigurationType = viewConfigurationType;
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
            instance.check(XR10.xrCreateReferenceSpace(handle, referenceSpaceCreateInfo, pp), "xrCreateReferenceSpace");
            xrAppSpace = new XrSpace(pp.get(0), handle);

            referenceSpaceCreateInfo.referenceSpaceType(XR10.XR_REFERENCE_SPACE_TYPE_VIEW);
            instance.check(XR10.xrCreateReferenceSpace(handle, referenceSpaceCreateInfo, pp), "xrCreateReferenceSpace");
            xrViewSpace = new XrSpace(pp.get(0), handle);
        }
    }

    public void createSwapchains() {
        try (MemoryStack stack = stackPush()) {
            IntBuffer intBuf = stack.mallocInt(1);
            instance.check(XR10.xrEnumerateViewConfigurationViews(instance.handle, system.handle, viewConfigurationType, intBuf, null), "xrEnumerateViewConfigurationViews");
            XrViewConfigurationView.Buffer viewConfigs = new XrViewConfigurationView.Buffer(
                    OpenXR.mallocAndFillBufferStack(intBuf.get(0), XrViewConfigurationView.SIZEOF, XR10.XR_TYPE_VIEW_CONFIGURATION_VIEW)
            );
            instance.check(XR10.xrEnumerateViewConfigurationViews(instance.handle, system.handle, viewConfigurationType, intBuf, viewConfigs), "xrEnumerateViewConfigurationViews");
            int viewCountNumber = intBuf.get(0);

            views = new XrView.Buffer(
                    OpenXR.mallocAndFillBufferHeap(viewCountNumber, XrView.SIZEOF, XR10.XR_TYPE_VIEW)
            );

            if (viewCountNumber == 0) {
                return;
            }
            instance.check(XR10.xrEnumerateSwapchainFormats(handle, intBuf, null), "xrEnumerateSwapchainFormats");
            LongBuffer swapchainFormats = stack.mallocLong(intBuf.get(0));
            instance.check(XR10.xrEnumerateSwapchainFormats(handle, intBuf, swapchainFormats), "xrEnumerateSwapchainFormats");

            long[] desiredSwapchainFormats = {
                    GL11.GL_RGB10_A2,
                    GL30.GL_RGBA16F,
                    GL30.GL_RGB16F,
//                    // The two below should only be used as a fallback, as they are linear color formats without enough bits for color
//                    // depth, thus leading to banding.
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
                throw new IllegalStateException("No compatible swapchain / framebuffer format availible");
            }

            swapchains = new OpenXRSwapchain[viewCountNumber];
            for (int i = 0; i < viewCountNumber; i++) {
                XrViewConfigurationView viewConfig = viewConfigs.get(i);
                XrSwapchainCreateInfo swapchainCreateInfo = XrSwapchainCreateInfo.mallocStack();

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
                instance.check(XR10.xrCreateSwapchain(handle, swapchainCreateInfo, pp), "xrCreateSwapchain");
                OpenXRSwapchain swapchain = new OpenXRSwapchain(new XrSwapchain(pp.get(0), handle), this);
                swapchain.width = swapchainCreateInfo.width();
                swapchain.height = swapchainCreateInfo.height();
                swapchain.createImages();
                swapchains[i] = swapchain;
            }
        }
    }

    public boolean handleSessionStateChangedEvent(XrEventDataSessionStateChanged stateChangedEvent) {
        int oldState = state;
        state = stateChangedEvent.state();

        LOGGER.debug(String.format("XrEventDataSessionStateChanged: state %s->%s session=%d time=%d", oldState, state, stateChangedEvent.session(), stateChangedEvent.time()));

        if ((stateChangedEvent.session() != NULL) && (stateChangedEvent.session() != handle.address())) {
            LOGGER.warn("XrEventDataSessionStateChanged for unknown session");
            return false;
        }

        switch (state) {
            case XR10.XR_SESSION_STATE_READY: {
                XrSessionBeginInfo sessionBeginInfo = XrSessionBeginInfo.mallocStack();
                sessionBeginInfo.set(XR10.XR_TYPE_SESSION_BEGIN_INFO, 0, viewConfigurationType);
                instance.check(XR10.xrBeginSession(handle, sessionBeginInfo), "xrBeginSession");
                running = true;
                return false;
            }
            case XR10.XR_SESSION_STATE_STOPPING: {
                running = false;
                instance.check(XR10.xrEndSession(handle), "xrEndSession");
                return false;
            }
            case XR10.XR_SESSION_STATE_EXITING: {
                // Do not attempt to restart because user closed this session.
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

    public void pollActions() {
        if (state != XR10.XR_SESSION_STATE_FOCUSED) {
            return;
        }

        try (var ignored = stackPush()) {
            VanillaGameplayActionSet vcActionSet = XrInput.vanillaGameplayActionSet;
            GuiActionSet guiActionSet = XrInput.guiActionSet;
            HandsActionSet handsActionSet = XrInput.handsActionSet;

            XrActiveActionSet.Buffer sets = XrActiveActionSet.callocStack(3);
            sets.get(0).actionSet(handsActionSet.getHandle());
            sets.get(1).actionSet(vcActionSet.getHandle());
            sets.get(2).actionSet(guiActionSet.getHandle());

            XrActionsSyncInfo sync_info = XrActionsSyncInfo.calloc()
                    .type(XR10.XR_TYPE_ACTIONS_SYNC_INFO)
                    .activeActionSets(sets);

            instance.check(XR10.xrSyncActions(handle, sync_info), "xrSyncActions");

            handsActionSet.sync(this);
            vcActionSet.sync(this);
            guiActionSet.sync(this);
        }

        XrInput.pollActions();
    }

    public void setPosesFromSpace(XrSpace handSpace, long time, ControllerPoses result, float scale) {
        try (MemoryStack ignored = stackPush()) {
            XrSpaceLocation space_location = XrSpaceLocation.callocStack().type(XR10.XR_TYPE_SPACE_LOCATION);
            instance.check(XR10.xrLocateSpace(handSpace, xrAppSpace, time, space_location), "xrLocateSpace");
            if ((space_location.locationFlags() & XR10.XR_SPACE_LOCATION_POSITION_VALID_BIT) != 0 &&
                    (space_location.locationFlags() & XR10.XR_SPACE_LOCATION_ORIENTATION_VALID_BIT) != 0) {

                result.updatePhysicalPose(space_location.pose(), MCXRPlayClient.yawTurn, scale);
            }
        }
    }

    @Override
    public void close() {
        for (var swapchain : swapchains) {
            swapchain.close();
        }
        views.close();
        XR10.xrDestroySpace(xrAppSpace);
        XR10.xrDestroySpace(xrViewSpace);
        XR10.xrDestroySession(handle);
    }
}
