package net.sorenon.mcxr.play.openxr;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlConst;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.sorenon.mcxr.core.MCXRCore;
import net.sorenon.mcxr.play.FlatGuiManager;
import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.play.accessor.MinecraftClientExt;
import net.sorenon.mcxr.play.input.XrInput;
import net.sorenon.mcxr.play.rendering.MCXRMainTarget;
import net.sorenon.mcxr.play.rendering.RenderPass;
import net.sorenon.mcxr.play.rendering.XrCamera;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.openxr.*;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.Struct;

import java.nio.IntBuffer;

import static org.lwjgl.system.MemoryStack.stackCallocInt;
import static org.lwjgl.system.MemoryStack.stackPush;

public class XrRenderer {
    private static final OpenXR OPEN_XR = MCXRPlayClient.OPEN_XR;
    private static final Logger LOGGER = LogManager.getLogger();
    private Minecraft client;
    private MinecraftClientExt clientExt;
    private MCXRMainTarget MCXRMainTarget;


    private OpenXRInstance instance;
    private OpenXRSession session;

    public RenderPass renderPass = RenderPass.VANILLA;

    public ShaderInstance blitShader;
    public ShaderInstance guiBlitShader;

    public void setSession(OpenXRSession session) {
        this.session = session;
        this.instance = session.instance;

        if (this.client == null) {
            client = Minecraft.getInstance();
            clientExt = ((MinecraftClientExt) Minecraft.getInstance());
            MCXRMainTarget = (MCXRMainTarget) client.getMainRenderTarget();
        }
    }

    public boolean isXrMode() {
        return Minecraft.getInstance().level != null && session != null;
    }

    public void renderFrame() {
        try (MemoryStack stack = stackPush()) {
            var frameState = XrFrameState.calloc(stack).type(XR10.XR_TYPE_FRAME_STATE);

            if (isXrMode()) {
                GLFW.glfwSwapBuffers(Minecraft.getInstance().getWindow().getWindow());
            }
            instance.check(XR10.xrWaitFrame(
                    session.handle,
                    XrFrameWaitInfo.calloc(stack).type(XR10.XR_TYPE_FRAME_WAIT_INFO),
                    frameState
            ), "xrWaitFrame");

            instance.check(XR10.xrBeginFrame(
                    session.handle,
                    XrFrameBeginInfo.calloc(stack).type(XR10.XR_TYPE_FRAME_BEGIN_INFO)
            ), "xrBeginFrame");

            PointerBuffer layers = stack.callocPointer(1);

            if (frameState.shouldRender()) {
                if (this.isXrMode()) {
                    var layer = renderLayerOpenXR(frameState.predictedDisplayTime(), stack);
                    if (layer != null) {
                        layers.put(layer.address());
                    }
                } else {
                    var layer = renderLayerBlankOpenXR(frameState.predictedDisplayTime(), stack);
                    layers.put(layer.address());
                }
            }
            layers.flip();

            instance.check(XR10.xrEndFrame(
                    session.handle,
                    XrFrameEndInfo.calloc(stack)
                            .type(XR10.XR_TYPE_FRAME_END_INFO)
                            .displayTime(frameState.predictedDisplayTime())
                            .environmentBlendMode(XR10.XR_ENVIRONMENT_BLEND_MODE_OPAQUE)
                            .layers(layers)
            ), "xrEndFrame");
        }
    }

    private Struct renderLayerOpenXR(long predictedDisplayTime, MemoryStack stack) {
//        try (MemoryStack stack = stackPush()) {

        XrViewState viewState = XrViewState.calloc(stack).type(XR10.XR_TYPE_VIEW_STATE);
        IntBuffer intBuf = stackCallocInt(1);

        XrViewLocateInfo viewLocateInfo = XrViewLocateInfo.calloc(stack);
        viewLocateInfo.set(XR10.XR_TYPE_VIEW_LOCATE_INFO,
                0,
                session.viewConfigurationType,
                predictedDisplayTime,
                session.xrAppSpace
        );

        instance.check(XR10.xrLocateViews(session.handle, viewLocateInfo, viewState, intBuf, session.views), "xrLocateViews");

        if ((viewState.viewStateFlags() & XR10.XR_VIEW_STATE_POSITION_VALID_BIT) == 0 ||
                (viewState.viewStateFlags() & XR10.XR_VIEW_STATE_ORIENTATION_VALID_BIT) == 0) {
            LOGGER.error("Invalid headset position, try restarting your device");
            return null;  // There is no valid tracking poses for the views.
        }
        int viewCountOutput = intBuf.get(0);

        var projectionLayerViews = XrCompositionLayerProjectionView.calloc(viewCountOutput);

        float scalePreTick = MCXRPlayClient.getCameraScale();

        // Update hand position based on the predicted time of when the frame will be rendered! This
        // should result in a more accurate location, and reduce perceived lag.
        if (session.state == XR10.XR_SESSION_STATE_FOCUSED) {
            for (int i = 0; i < 2; i++) {
                if (!XrInput.handsActionSet.grip.isActive[i]) {
                    continue;
                }
                session.setPosesFromSpace(XrInput.handsActionSet.grip.spaces[i], predictedDisplayTime, XrInput.handsActionSet.gripPoses[i], scalePreTick);
                session.setPosesFromSpace(XrInput.handsActionSet.aim.spaces[i], predictedDisplayTime, XrInput.handsActionSet.aimPoses[i], scalePreTick);
            }
        }

        session.setPosesFromSpace(session.xrViewSpace, predictedDisplayTime, MCXRPlayClient.viewSpacePoses, scalePreTick);
        Entity cameraEntity = this.client.getCameraEntity() == null ? this.client.player : this.client.getCameraEntity();
        if (cameraEntity != null) { //TODO seriously need to tidy up poses
            Entity vehicle = cameraEntity.getVehicle();
            if (MCXRCore.getCoreConfig().roomscaleMovement() && vehicle == null) {
                MCXRPlayClient.xrOrigin.set(cameraEntity.getX() - MCXRPlayClient.roomscalePlayerOffset.x,
                        cameraEntity.getY(),
                        cameraEntity.getZ() - MCXRPlayClient.roomscalePlayerOffset.z);
            } else {
                MCXRPlayClient.xrOrigin.set(cameraEntity.getX(),
                        cameraEntity.getY(),
                        cameraEntity.getZ());
            }
            if (vehicle != null) {
                if (vehicle instanceof LivingEntity) {
                    MCXRPlayClient.xrOrigin.y += 0.60;
                } else {
                    MCXRPlayClient.xrOrigin.y += 0.54 - vehicle.getPassengersRidingOffset();
                }
            }

            MCXRPlayClient.viewSpacePoses.updateGamePose(MCXRPlayClient.xrOrigin);
            for (var poses : XrInput.handsActionSet.gripPoses) {
                poses.updateGamePose(MCXRPlayClient.xrOrigin);
            }
            for (var poses : XrInput.handsActionSet.aimPoses) {
                poses.updateGamePose(MCXRPlayClient.xrOrigin);
            }
        }
        XrCamera camera = (XrCamera) Minecraft.getInstance().gameRenderer.getMainCamera();
        camera.updateXR(this.client.level, cameraEntity, MCXRPlayClient.viewSpacePoses.getGamePose());

        FlatGuiManager FGM = MCXRPlayClient.INSTANCE.flatGuiManager;

        if (FGM.needsReset) {
            FGM.resetTransform();
        }

        long frameStartTime = Util.getNanos();
        if (Minecraft.getInstance().player != null && MCXRCore.getCoreConfig().xrAllowed()) {
            MCXRCore.INSTANCE.setPlayerHeadPose(
                    Minecraft.getInstance().player,
                    MCXRPlayClient.viewSpacePoses.getScaledPhysicalPose()
            );
        }
        clientExt.preRender(true);
        if (camera.getEntity() != null) {
            float tickDelta = client.getFrameTime();
            Entity camEntity = camera.getEntity();

            if (client.isPaused()) {
                tickDelta = 0.0f;
            }

            Entity vehicle = camEntity.getVehicle();
            if (MCXRCore.getCoreConfig().roomscaleMovement() && vehicle == null) {
                MCXRPlayClient.xrOrigin.set(Mth.lerp(tickDelta, camEntity.xo, camEntity.getX()) - MCXRPlayClient.roomscalePlayerOffset.x,
                        Mth.lerp(tickDelta, camEntity.yo, camEntity.getY()),
                        Mth.lerp(tickDelta, camEntity.zo, camEntity.getZ()) - MCXRPlayClient.roomscalePlayerOffset.z);
            } else {
                MCXRPlayClient.xrOrigin.set(Mth.lerp(tickDelta, camEntity.xo, camEntity.getX()),
                        Mth.lerp(tickDelta, camEntity.yo, camEntity.getY()),
                        Mth.lerp(tickDelta, camEntity.zo, camEntity.getZ()));
            }
            if (vehicle != null) {
                if (vehicle instanceof LivingEntity) {
                    MCXRPlayClient.xrOrigin.y += 0.60;
                } else {
                    MCXRPlayClient.xrOrigin.y += 0.54 - vehicle.getPassengersRidingOffset();
                }
            }

            MCXRPlayClient.viewSpacePoses.updateGamePose(MCXRPlayClient.xrOrigin);
            for (var poses : XrInput.handsActionSet.gripPoses) {
                poses.updateGamePose(MCXRPlayClient.xrOrigin);
            }
            for (var poses : XrInput.handsActionSet.aimPoses) {
                poses.updateGamePose(MCXRPlayClient.xrOrigin);
            }
        }

        client.getWindow().setErrorSection("Render");
        client.getProfiler().push("sound");
        client.getSoundManager().updateSource(client.gameRenderer.getMainCamera());
        client.getProfiler().pop();

        //Render GUI
        MCXRMainTarget.setFramebuffer(FGM.backFramebuffer);
        XrInput.postTick(predictedDisplayTime);
        FGM.backFramebuffer.clear(Minecraft.ON_OSX);
        clientExt.doRender(true, frameStartTime, RenderPass.GUI);

        FGM.frontFramebuffer.bindWrite(true);
        this.guiBlitShader.setSampler("DiffuseSampler", FGM.backFramebuffer.getColorTextureId());
        this.guiBlitShader.setSampler("DepthSampler", FGM.backFramebuffer.getDepthTextureId());
        this.blit(FGM.frontFramebuffer, guiBlitShader);
        FGM.frontFramebuffer.unbindWrite();

        MCXRMainTarget.resetFramebuffer();

        RenderPass.World worldRenderPass = RenderPass.World.create();

        // Render view to the appropriate part of the swapchain image.
        for (int viewIndex = 0; viewIndex < viewCountOutput; viewIndex++) {
            // Each view has a separate swapchain which is acquired, rendered to, and released.
            OpenXRSwapchain viewSwapchain = session.swapchains[viewIndex];

            instance.check(XR10.xrAcquireSwapchainImage(
                    viewSwapchain.handle,
                    XrSwapchainImageAcquireInfo.calloc(stack).type(XR10.XR_TYPE_SWAPCHAIN_IMAGE_ACQUIRE_INFO),
                    intBuf
            ), "xrAcquireSwapchainImage");

            int swapchainImageIndex = intBuf.get(0);

            instance.check(XR10.xrWaitSwapchainImage(viewSwapchain.handle,
                    XrSwapchainImageWaitInfo.calloc(stack)
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
                viewSwapchain.innerFramebuffer.setColorAttachment(xrSwapchainImageOpenGLKHR.image());
                viewSwapchain.innerFramebuffer.unbindWrite();
                MCXRMainTarget.setXrFramebuffer(viewSwapchain.framebuffer);
                worldRenderPass.fov = session.views.get(viewIndex).fov();
                worldRenderPass.eyePoses.updatePhysicalPose(session.views.get(viewIndex).pose(), MCXRPlayClient.yawTurn, scalePreTick);
                worldRenderPass.eyePoses.updateGamePose(MCXRPlayClient.xrOrigin);
                worldRenderPass.viewIndex = viewIndex;
                camera.setPose(worldRenderPass.eyePoses.getGamePose());
                clientExt.doRender(true, frameStartTime, worldRenderPass);
            }

            viewSwapchain.innerFramebuffer.bindWrite(true);
            this.blitShader.setSampler("DiffuseSampler", viewSwapchain.framebuffer.getColorTextureId());
            Uniform inverseScreenSize = this.blitShader.getUniform("InverseScreenSize");
            if (inverseScreenSize != null) {
                inverseScreenSize.set(1f / viewSwapchain.innerFramebuffer.width, 1f / viewSwapchain.innerFramebuffer.height);
            }
            viewSwapchain.framebuffer.setFilterMode(GlConst.GL_LINEAR);
            this.blit(viewSwapchain.innerFramebuffer, blitShader);
            viewSwapchain.innerFramebuffer.unbindWrite();

            if (viewIndex == viewCountOutput - 1) {
                blitToBackbuffer(viewSwapchain.framebuffer);
            }

            instance.check(XR10.xrReleaseSwapchainImage(
                    viewSwapchain.handle,
                    XrSwapchainImageReleaseInfo.calloc(stack)
                            .type(XR10.XR_TYPE_SWAPCHAIN_IMAGE_RELEASE_INFO)
            ), "xrReleaseSwapchainImage");
        }
        MCXRMainTarget.resetFramebuffer();
        camera.setPose(MCXRPlayClient.viewSpacePoses.getGamePose());
        clientExt.postRender();

        return XrCompositionLayerProjection.calloc(stack)
                .type(XR10.XR_TYPE_COMPOSITION_LAYER_PROJECTION)
                .space(session.xrAppSpace)
                .views(projectionLayerViews);
//        }
    }

    private Struct renderLayerBlankOpenXR(long predictedDisplayTime, MemoryStack stack) {
//        try (MemoryStack stack = stackPush()) {
        IntBuffer intBuf = stackCallocInt(1);

        instance.check(XR10.xrLocateViews(
                session.handle,
                XrViewLocateInfo.calloc(stack).set(XR10.XR_TYPE_VIEW_LOCATE_INFO,
                        0,
                        session.viewConfigurationType,
                        predictedDisplayTime,
                        session.xrAppSpace
                ),
                XrViewState.calloc(stack).type(XR10.XR_TYPE_VIEW_STATE),
                intBuf,
                session.views
        ), "xrLocateViews");

        int viewCountOutput = intBuf.get(0);

        var projectionLayerViews = XrCompositionLayerProjectionView.calloc(viewCountOutput);

        OpenXRSwapchain viewSwapchain = session.swapchains[0];
        instance.check(XR10.xrAcquireSwapchainImage(
                viewSwapchain.handle,
                XrSwapchainImageAcquireInfo.calloc(stack).type(XR10.XR_TYPE_SWAPCHAIN_IMAGE_ACQUIRE_INFO),
                intBuf
        ), "xrAcquireSwapchainImage");
        int swapchainImageIndex = intBuf.get(0);

        instance.check(XR10.xrWaitSwapchainImage(
                viewSwapchain.handle,
                XrSwapchainImageWaitInfo.calloc(stack)
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
        viewSwapchain.innerFramebuffer.setColorAttachment(xrSwapchainImageOpenGLKHR.image());
        viewSwapchain.innerFramebuffer.clear(Minecraft.ON_OSX);

        instance.check(XR10.xrReleaseSwapchainImage(
                viewSwapchain.handle,
                XrSwapchainImageReleaseInfo.calloc(stack).type(XR10.XR_TYPE_SWAPCHAIN_IMAGE_RELEASE_INFO)
        ), "xrReleaseSwapchainImage");

        XrCompositionLayerProjection layer = XrCompositionLayerProjection.calloc(stack).type(XR10.XR_TYPE_COMPOSITION_LAYER_PROJECTION);
        layer.space(session.xrAppSpace);
        layer.views(projectionLayerViews);
        return layer;
//        }
    }

    public void blit(RenderTarget framebuffer, ShaderInstance shader) {
        PoseStack matrixStack = RenderSystem.getModelViewStack();
        matrixStack.pushPose();
        matrixStack.setIdentity();
        RenderSystem.applyModelViewMatrix();

        int width = framebuffer.width;
        int height = framebuffer.height;

        GlStateManager._colorMask(true, true, true, true);
        GlStateManager._disableDepthTest();
        GlStateManager._depthMask(false);
        GlStateManager._viewport(0, 0, width, height);
        GlStateManager._disableBlend();

        Matrix4f matrix4f = Matrix4f.orthographic((float) width, (float) -height, 1000.0F, 3000.0F);
        RenderSystem.setProjectionMatrix(matrix4f);
        if (shader.MODEL_VIEW_MATRIX != null) {
            shader.MODEL_VIEW_MATRIX.set(Matrix4f.createTranslateMatrix(0.0F, 0.0F, -2000.0F));
        }

        if (shader.PROJECTION_MATRIX != null) {
            shader.PROJECTION_MATRIX.set(matrix4f);
        }

        shader.apply();
        float u = (float) framebuffer.viewWidth / (float) framebuffer.width;
        float v = (float) framebuffer.viewHeight / (float) framebuffer.height;
        Tesselator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_TEX_COLOR);
        bufferBuilder.vertex(0.0, height * 2, 0.0).uv(0.0F, 1 - v * 2).color(255, 255, 255, 255).endVertex();
        bufferBuilder.vertex(width * 2, 0.0, 0.0).uv(u * 2, 1).color(255, 255, 255, 255).endVertex();
        bufferBuilder.vertex(0.0, 0.0, 0.0).uv(0.0F, 1).color(255, 255, 255, 255).endVertex();
        bufferBuilder.end();
        BufferUploader._endInternal(bufferBuilder);
        shader.clear();
        GlStateManager._depthMask(true);
        GlStateManager._colorMask(true, true, true, true);

        matrixStack.popPose();
    }

    public void blitToBackbuffer(RenderTarget framebuffer) {
        //TODO render alyx-like gui over this
        ShaderInstance shader = Minecraft.getInstance().gameRenderer.blitShader;
        shader.setSampler("DiffuseSampler", framebuffer.getColorTextureId());

        PoseStack matrixStack = RenderSystem.getModelViewStack();
        matrixStack.pushPose();
        matrixStack.setIdentity();
        RenderSystem.applyModelViewMatrix();

        int width = MCXRMainTarget.windowFramebuffer.width;
        int height = MCXRMainTarget.windowFramebuffer.height;

        GlStateManager._colorMask(true, true, true, true);
        GlStateManager._disableDepthTest();
        GlStateManager._depthMask(false);
        GlStateManager._viewport(0, 0, width, height);
        GlStateManager._disableBlend();

        Matrix4f matrix4f = Matrix4f.orthographic((float) width, (float) (-height), 1000.0F, 3000.0F);
        RenderSystem.setProjectionMatrix(matrix4f);
        if (shader.MODEL_VIEW_MATRIX != null) {
            shader.MODEL_VIEW_MATRIX.set(Matrix4f.createTranslateMatrix(0, 0, -2000.0F));
        }

        if (shader.PROJECTION_MATRIX != null) {
            shader.PROJECTION_MATRIX.set(matrix4f);
        }

        shader.apply();
        float widthNormalized = (float) framebuffer.width / (float) width;
        float heightNormalized = (float) framebuffer.height / (float) height;
        float v = (widthNormalized / heightNormalized) / 2;

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        bufferBuilder.vertex(0.0, height, 0.0).uv(0.0F, v).color(255, 255, 255, 255).endVertex();
        bufferBuilder.vertex(width, height, 0.0).uv(1, v).color(255, 255, 255, 255).endVertex();
        bufferBuilder.vertex(width, 0.0, 0.0).uv(1, 1 - v).color(255, 255, 255, 255).endVertex();
        bufferBuilder.vertex(0.0, 0.0, 0.0).uv(0.0F, 1 - v).color(255, 255, 255, 255).endVertex();
        bufferBuilder.end();
        BufferUploader._endInternal(bufferBuilder);
        shader.clear();
        GlStateManager._depthMask(true);
        GlStateManager._colorMask(true, true, true, true);

        matrixStack.popPose();
    }
}
