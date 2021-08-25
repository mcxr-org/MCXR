package net.sorenon.mcxr.play.openxr;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.sorenon.mcxr.core.MCXRCore;
import net.sorenon.mcxr.core.client.MCXRCoreClient;
import net.sorenon.mcxr.play.FlatGuiManager;
import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.play.accessor.MinecraftClientExt;
import net.sorenon.mcxr.play.input.ControllerPoses;
import net.sorenon.mcxr.play.input.XrInput;
import net.sorenon.mcxr.play.rendering.MainRenderTarget;
import net.sorenon.mcxr.play.rendering.RenderPass;
import net.sorenon.mcxr.play.rendering.XrCamera;
import org.lwjgl.PointerBuffer;
import org.lwjgl.openxr.*;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.Struct;

import java.nio.IntBuffer;

import static org.lwjgl.system.MemoryStack.stackCallocInt;
import static org.lwjgl.system.MemoryStack.stackPush;

public class XrRenderer {
    private static final OpenXR OPEN_XR = MCXRPlayClient.OPEN_XR;
    private MinecraftClient client;
    private MinecraftClientExt clientExt;

    private OpenXRInstance instance;
    private OpenXRSession session;

    public RenderPass renderPass = RenderPass.VANILLA;
    public XrFovf fov = null;
    public int viewIndex = 0;
    public final ControllerPoses eyePoses = new ControllerPoses();

    public void setSession(OpenXRSession session) {
        this.session = session;
        this.instance = session.instance;

        if (this.client == null) {
            client = MinecraftClient.getInstance();
            clientExt = ((MinecraftClientExt) MinecraftClient.getInstance());
        }
    }

    public boolean isXrMode() {
        return MinecraftClient.getInstance().world != null && session != null;
    }

    public void renderFrame() {
        try (MemoryStack stack = stackPush()) {
            var frameState = XrFrameState.callocStack().type(XR10.XR_TYPE_FRAME_STATE);

            instance.check(XR10.xrWaitFrame(
                    session.handle,
                    XrFrameWaitInfo.callocStack().type(XR10.XR_TYPE_FRAME_WAIT_INFO),
                    frameState
            ), "xrWaitFrame");

            instance.check(XR10.xrBeginFrame(
                    session.handle,
                    XrFrameBeginInfo.callocStack().type(XR10.XR_TYPE_FRAME_BEGIN_INFO)
            ), "xrBeginFrame");

            PointerBuffer layers = stack.callocPointer(1);

            if (frameState.shouldRender()) {
                if (this.isXrMode()) {
                    var layer = renderLayerOpenXR(frameState.predictedDisplayTime());
                    if (layer != null) {
                        layers.put(layer.address());
                    }
                } else {
                    var layer = renderLayerBlankOpenXR(frameState.predictedDisplayTime());
                    layers.put(layer.address());
                }
            }
            layers.flip();

            instance.check(XR10.xrEndFrame(
                    session.handle,
                    XrFrameEndInfo.callocStack()
                            .type(XR10.XR_TYPE_FRAME_END_INFO)
                            .displayTime(frameState.predictedDisplayTime())
                            .environmentBlendMode(XR10.XR_ENVIRONMENT_BLEND_MODE_OPAQUE)
                            .layers(layers)
            ), "xrEndFrame");
        }
    }

    private Struct renderLayerOpenXR(long predictedDisplayTime) {
//        try (MemoryStack stack = stackPush()) {

        XrViewState viewState = XrViewState.callocStack().type(XR10.XR_TYPE_VIEW_STATE);
        IntBuffer intBuf = stackCallocInt(1);

        XrViewLocateInfo viewLocateInfo = XrViewLocateInfo.callocStack();
        viewLocateInfo.set(XR10.XR_TYPE_VIEW_LOCATE_INFO,
                0,
                session.viewConfigurationType,
                predictedDisplayTime,
                session.xrAppSpace
        );

        instance.check(XR10.xrLocateViews(session.handle, viewLocateInfo, viewState, intBuf, session.views), "xrLocateViews");

        if ((viewState.viewStateFlags() & XR10.XR_VIEW_STATE_POSITION_VALID_BIT) == 0 ||
                (viewState.viewStateFlags() & XR10.XR_VIEW_STATE_ORIENTATION_VALID_BIT) == 0) {
            return null;  // There is no valid tracking poses for the views.
        }
        int viewCountOutput = intBuf.get(0);

        var projectionLayerViews = XrCompositionLayerProjectionView.calloc(viewCountOutput);

        // Update hand position based on the predicted time of when the frame will be rendered! This
        // should result in a more accurate location, and reduce perceived lag.
        if (session.state == XR10.XR_SESSION_STATE_FOCUSED) {
            for (int i = 0; i < 2; i++) {
                if (!XrInput.handsActionSet.grip.isActive[i]) {
                    continue;
                }
                OPEN_XR.setPosesFromSpace(XrInput.handsActionSet.grip.spaces[i], predictedDisplayTime, XrInput.handsActionSet.gripPoses[i]);
                OPEN_XR.setPosesFromSpace(XrInput.handsActionSet.aim.spaces[i], predictedDisplayTime, XrInput.handsActionSet.aimPoses[i]);
            }
        }

        OPEN_XR.setPosesFromSpace(session.xrViewSpace, predictedDisplayTime, MCXRPlayClient.viewSpacePoses);

        XrCamera camera = (XrCamera) MinecraftClient.getInstance().gameRenderer.getCamera();
        camera.updateXR(this.client.world, this.client.getCameraEntity() == null ? this.client.player : this.client.getCameraEntity(), MCXRPlayClient.viewSpacePoses.getGamePose());
        MainRenderTarget mainRenderTarget = (MainRenderTarget) client.getFramebuffer();

        long frameStartTime = Util.getMeasuringTimeNano();
//            MCXRCore.pose.set(MineXRaftClient.viewSpacePoses.getPhysicalPose());
        if (MinecraftClient.getInstance().player != null && MCXRCoreClient.INSTANCE.playInstalled) {
            MCXRCore.INSTANCE.playerPose(
                    MinecraftClient.getInstance().player,
                    MCXRPlayClient.viewSpacePoses.getPhysicalPose());
        }
        clientExt.preRender(true);
        if (camera.getFocusedEntity() != null) {
            float tickDelta = client.getTickDelta();
            Entity camEntity = camera.getFocusedEntity();
            MCXRPlayClient.xrOrigin.set(MathHelper.lerp(tickDelta, camEntity.prevX, camEntity.getX()) + MCXRPlayClient.xrOffset.x,
                    MathHelper.lerp(tickDelta, camEntity.prevY, camEntity.getY()) + MCXRPlayClient.xrOffset.y,
                    MathHelper.lerp(tickDelta, camEntity.prevZ, camEntity.getZ()) + MCXRPlayClient.xrOffset.z);

            float scale = MCXRPlayClient.getCameraScale();
            MCXRPlayClient.viewSpacePoses.updateGamePose(MCXRPlayClient.xrOrigin, scale);
            for (var poses : XrInput.handsActionSet.gripPoses) {
                poses.updateGamePose(MCXRPlayClient.xrOrigin, scale);
            }
            for (var poses : XrInput.handsActionSet.aimPoses) {
                poses.updateGamePose(MCXRPlayClient.xrOrigin, scale);
            }
        }
//            client.mouse.updateMouse();
        client.getWindow().setPhase("Render");
        client.getProfiler().push("sound");
        client.getSoundManager().updateListenerPosition(client.gameRenderer.getCamera());
        client.getProfiler().pop();

        //Render GUI
        FlatGuiManager FGM = MCXRPlayClient.INSTANCE.flatGuiManager;
        mainRenderTarget.setFramebuffer(FGM.framebuffer);
        XrInput.postTick(predictedDisplayTime);
        FGM.framebuffer.clear(MinecraftClient.IS_SYSTEM_MAC);
        clientExt.doRender(true, frameStartTime, RenderPass.GUI);
        mainRenderTarget.resetFramebuffer();

        // Render view to the appropriate part of the swapchain image.
        for (int viewIndex = 0; viewIndex < viewCountOutput; viewIndex++) {
            // Each view has a separate swapchain which is acquired, rendered to, and released.
            OpenXRSwapchain viewSwapchain = session.swapchains[viewIndex];

            instance.check(XR10.xrAcquireSwapchainImage(
                    viewSwapchain.handle,
                    XrSwapchainImageAcquireInfo.callocStack().type(XR10.XR_TYPE_SWAPCHAIN_IMAGE_ACQUIRE_INFO),
                    intBuf
            ), "xrAcquireSwapchainImage");

            int swapchainImageIndex = intBuf.get(0);

            instance.check(XR10.xrWaitSwapchainImage(viewSwapchain.handle,
                    XrSwapchainImageWaitInfo.callocStack()
                            .type(XR10.XR_TYPE_SWAPCHAIN_IMAGE_WAIT_INFO)
                            .timeout(XR10.XR_INFINITE_DURATION)
            ), "xrWaitSwapchainImage");

            var subImage = projectionLayerViews.get(viewIndex)
                    .type(XR10.XR_TYPE_COMPOSITION_LAYER_PROJECTION_VIEW)
                    .pose(session.views.get(viewIndex).pose())
                    .fov(session.views.get(viewIndex).fov())
                    .subImage();
            subImage.swapchain(viewSwapchain.handle);
            subImage.imageRect().offset().set(0, 0);
            subImage.imageRect().extent().set(viewSwapchain.width, viewSwapchain.height);

            {
                XrSwapchainImageOpenGLKHR xrSwapchainImageOpenGLKHR = viewSwapchain.images.get(swapchainImageIndex);
                viewSwapchain.framebuffer.setColorAttachment(xrSwapchainImageOpenGLKHR.image());
                viewSwapchain.framebuffer.endWrite();
                mainRenderTarget.setXrFramebuffer(viewSwapchain.framebuffer);
                fov = session.views.get(viewIndex).fov();
                eyePoses.updatePhysicalPose(session.views.get(viewIndex).pose(), MCXRPlayClient.yawTurn);
                eyePoses.updateGamePose(MCXRPlayClient.xrOrigin, MCXRPlayClient.getCameraScale());
                this.viewIndex = viewIndex;
                camera.setPose(eyePoses.getGamePose());
                clientExt.doRender(true, frameStartTime, RenderPass.WORLD);
                fov = null;
            }

            instance.check(XR10.xrReleaseSwapchainImage(
                    viewSwapchain.handle,
                    XrSwapchainImageReleaseInfo.callocStack()
                            .type(XR10.XR_TYPE_SWAPCHAIN_IMAGE_RELEASE_INFO)
            ), "xrReleaseSwapchainImage");
        }
        mainRenderTarget.resetFramebuffer();
        camera.setPose(MCXRPlayClient.viewSpacePoses.getGamePose());
        clientExt.postRender();

        return XrCompositionLayerProjection.callocStack()
                .type(XR10.XR_TYPE_COMPOSITION_LAYER_PROJECTION)
                .space(session.xrAppSpace)
                .views(projectionLayerViews);
//        }
    }

    private Struct renderLayerBlankOpenXR(long predictedDisplayTime) {
//        try (MemoryStack stack = stackPush()) {
        IntBuffer intBuf = stackCallocInt(1);

        instance.check(XR10.xrLocateViews(
                session.handle,
                XrViewLocateInfo.callocStack().set(XR10.XR_TYPE_VIEW_LOCATE_INFO,
                        0,
                        session.viewConfigurationType,
                        predictedDisplayTime,
                        session.xrAppSpace
                ),
                XrViewState.callocStack().type(XR10.XR_TYPE_VIEW_STATE),
                intBuf,
                session.views
        ), "xrLocateViews");

        int viewCountOutput = intBuf.get(0);

        var projectionLayerViews = XrCompositionLayerProjectionView.calloc(viewCountOutput);

        OpenXRSwapchain viewSwapchain = session.swapchains[0];
        instance.check(XR10.xrAcquireSwapchainImage(
                viewSwapchain.handle,
                XrSwapchainImageAcquireInfo.callocStack().type(XR10.XR_TYPE_SWAPCHAIN_IMAGE_ACQUIRE_INFO),
                intBuf
        ), "xrAcquireSwapchainImage");
        int swapchainImageIndex = intBuf.get(0);

        instance.check(XR10.xrWaitSwapchainImage(
                viewSwapchain.handle,
                XrSwapchainImageWaitInfo.callocStack()
                        .type(XR10.XR_TYPE_SWAPCHAIN_IMAGE_WAIT_INFO)
                        .timeout(XR10.XR_INFINITE_DURATION)
        ), "xrWaitSwapchainImage");

        for (int viewIndex = 0; viewIndex < viewCountOutput; viewIndex++) {
            XrCompositionLayerProjectionView projectionLayerView = projectionLayerViews.get(viewIndex);
            projectionLayerView.type(XR10.XR_TYPE_COMPOSITION_LAYER_PROJECTION_VIEW);
            projectionLayerView.pose(session.views.get(viewIndex).pose());
            projectionLayerView.fov(session.views.get(viewIndex).fov());
            projectionLayerView.subImage().swapchain(viewSwapchain.handle);
            projectionLayerView.subImage().imageRect().offset().set(0, 0);
            projectionLayerView.subImage().imageRect().extent().set(viewSwapchain.width, viewSwapchain.height);
        }

        XrSwapchainImageOpenGLKHR xrSwapchainImageOpenGLKHR = viewSwapchain.images.get(swapchainImageIndex);
        viewSwapchain.framebuffer.setColorAttachment(xrSwapchainImageOpenGLKHR.image());
        viewSwapchain.framebuffer.clear(MinecraftClient.IS_SYSTEM_MAC);

        instance.check(XR10.xrReleaseSwapchainImage(
                viewSwapchain.handle,
                XrSwapchainImageReleaseInfo.callocStack().type(XR10.XR_TYPE_SWAPCHAIN_IMAGE_RELEASE_INFO)
        ), "xrReleaseSwapchainImage");

        XrCompositionLayerProjection layer = XrCompositionLayerProjection.callocStack().type(XR10.XR_TYPE_COMPOSITION_LAYER_PROJECTION);
        layer.space(session.xrAppSpace);
        layer.views(projectionLayerViews);
        return layer;
//        }
    }
}
