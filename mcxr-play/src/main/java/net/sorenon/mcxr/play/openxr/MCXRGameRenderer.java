package net.sorenon.mcxr.play.openxr;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlConst;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
//import com.mojang.math.Vector3f;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.sorenon.mcxr.core.JOMLUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.border.WorldBorder;
import net.sorenon.mcxr.core.MCXRCore;
import net.sorenon.mcxr.core.Pose;
import net.sorenon.mcxr.core.Teleport;
import net.sorenon.mcxr.core.accessor.PlayerExt;
import net.sorenon.mcxr.core.mixin.LivingEntityAcc;
import net.sorenon.mcxr.play.MCXRGuiManager;
import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.play.PlayOptions;
import net.sorenon.mcxr.play.accessor.MinecraftExt;
import net.sorenon.mcxr.play.input.actionsets.VanillaGameplayActionSet;
import net.sorenon.mcxr.play.rendering.MCXRCamera;
import net.sorenon.mcxr.play.rendering.MCXRMainTarget;
import net.sorenon.mcxr.play.rendering.RenderPass;
import net.sorenon.mcxr.play.rendering.XrRenderTarget;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import net.sorenon.mcxr.play.input.XrInput;
import org.lwjgl.openxr.*;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.Struct;

import java.nio.IntBuffer;

import static org.lwjgl.system.MemoryStack.stackCallocInt;
import static org.lwjgl.system.MemoryStack.stackPush;

public class MCXRGameRenderer {
    private static final Logger LOGGER = LogManager.getLogger();
    private Minecraft client;
    private MinecraftExt clientExt;
    private MCXRMainTarget mainRenderTarget;
    private MCXRCamera camera;

    private OpenXRInstance instance;
    private OpenXRSession session;

    public RenderPass renderPass = RenderPass.VANILLA;
    public ShaderInstance blitShader;
    public ShaderInstance guiBlitShader;

    private boolean xrDisabled = false;
    private boolean xrReady = true;

    public void initialize(Minecraft client) {
        this.client = client;
        this.clientExt = (MinecraftExt) client;
        mainRenderTarget = (MCXRMainTarget) client.getMainRenderTarget();
        camera = (MCXRCamera) client.gameRenderer.getMainCamera();
    }

    public void setSession(OpenXRSession session) {
        this.session = session;
        if (session != null) {
            this.instance = session.instance;
        } else {
            this.instance = null;
        }
    }

    public boolean isXrMode() {
        return Minecraft.getInstance().level != null && session != null && session.running && xrReady && !xrDisabled;
    }

    public void renderFrame(boolean xrDisabled) {
        if (this.xrDisabled != xrDisabled) {
            MCXRPlayClient.resetView();
        }
        this.xrDisabled = xrDisabled;

        try (MemoryStack stack = stackPush()) {
            var frameState = XrFrameState.calloc(stack).type(XR10.XR_TYPE_FRAME_STATE);

            if (isXrMode()) {
                GLFW.glfwSwapBuffers(Minecraft.getInstance().getWindow().getWindow());
            }
            //TODO tick game and poll input during xrWaitFrame (this might not work due to the gl context belonging to the xrWaitFrame thread)
            instance.checkPanic(XR10.xrWaitFrame(
                    session.handle,
                    XrFrameWaitInfo.calloc(stack).type(XR10.XR_TYPE_FRAME_WAIT_INFO),
                    frameState
            ), "xrWaitFrame");

            xrReady = frameState.shouldRender();

            instance.checkPanic(XR10.xrBeginFrame(
                    session.handle,
                    XrFrameBeginInfo.calloc(stack).type(XR10.XR_TYPE_FRAME_BEGIN_INFO)
            ), "xrBeginFrame");

            PointerBuffer layers = stack.callocPointer(1);

            if (frameState.shouldRender()) {
                if (this.isXrMode() && !xrDisabled) {
                    var layer = renderXrGame(frameState.predictedDisplayTime(), stack);
                    if (layer != null) {
                        layers.put(layer.address());
                    }
                } else {
                    var layer = renderBlankLayer(frameState.predictedDisplayTime(), stack);
                    layers.put(layer.address());
                }
            }
            layers.flip();

            int result = XR10.xrEndFrame(
                    session.handle,
                    XrFrameEndInfo.calloc(stack)
                            .type(XR10.XR_TYPE_FRAME_END_INFO)
                            .displayTime(frameState.predictedDisplayTime())
                            .environmentBlendMode(XR10.XR_ENVIRONMENT_BLEND_MODE_OPAQUE)
                            .layers(layers)
            );
            if (result != XR10.XR_ERROR_TIME_INVALID) {
                instance.checkPanic(result, "xrEndFrame");
            } else {
                LOGGER.warn("Rendering frame took too long! (probably)");
            }
        }
    }

    private Struct renderXrGame(long predictedDisplayTime, MemoryStack stack) {
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

        instance.checkPanic(XR10.xrLocateViews(session.handle, viewLocateInfo, viewState, intBuf, session.viewBuffer), "xrLocateViews");

        if ((viewState.viewStateFlags() & XR10.XR_VIEW_STATE_POSITION_VALID_BIT) == 0 ||
                (viewState.viewStateFlags() & XR10.XR_VIEW_STATE_ORIENTATION_VALID_BIT) == 0) {
            LOGGER.error("Invalid headset position, try restarting your device");
            return null;
        }

        var projectionLayerViews = XrCompositionLayerProjectionView.calloc(2, stack);

        MCXRGuiManager FGM = MCXRPlayClient.INSTANCE.MCXRGuiManager;

        if (FGM.needsReset) {
            FGM.resetTransform();
        }

        long frameStartTime = Util.getNanos();

        //Ticks the game
        clientExt.preRender(true, () -> {
            //Pre-tick
            //Update poses for tick
            updatePoses(camera.getEntity(), false, predictedDisplayTime, 1.0f, MCXRPlayClient.getCameraScale());

            //Update the server-side player poses
            if (Minecraft.getInstance().player != null && MCXRCore.getCoreConfig().supportsMCXR()) {
                Player player = Minecraft.getInstance().player;
                PlayerExt acc = (PlayerExt) player;
                if (!acc.isXR()) {
                    FriendlyByteBuf buf = PacketByteBufs.create();
                    buf.writeBoolean(true);
                    ClientPlayNetworking.send(MCXRCore.IS_XR_PLAYER, buf);
                    acc.setIsXr(true);
                }
                MCXRCore.INSTANCE.setPlayerPoses(
                        Minecraft.getInstance().player,
                        MCXRPlayClient.viewSpacePoses.getMinecraftPose(),
                        XrInput.handsActionSet.gripPoses[0].getMinecraftPose(),
                        XrInput.handsActionSet.gripPoses[1].getMinecraftPose(),
                        MCXRPlayClient.viewSpacePoses.getPhysicalPose().getPos().y,
                        (float) Math.toRadians(PlayOptions.handPitchAdjust)
                );

                if (XrInput.teleport) {
                    XrInput.teleport = false;
                    int handIndex = 0;
                    if (player.getMainArm() == HumanoidArm.LEFT) {
                        handIndex = 1;
                    }

                    Pose pose = XrInput.handsActionSet.gripPoses[handIndex].getMinecraftPose();

                    Vector3f dir = pose.getOrientation().rotateX((float) java.lang.Math.toRadians(PlayOptions.handPitchAdjust), new Quaternionf()).transform(new Vector3f(0, -1, 0));

                    var pos = Teleport.tp(player, JOMLUtil.convert(pose.getPos()), JOMLUtil.convert(dir));
                    if (pos != null) {
                        ClientPlayNetworking.send(MCXRCore.TELEPORT, PacketByteBufs.empty());
                        player.setPos(pos);
                    }
                }
            } else {
                XrInput.teleport = false;
            }
        });

        Entity cameraEntity = this.client.getCameraEntity() == null ? this.client.player : this.client.getCameraEntity();

        float frameUserScale = MCXRPlayClient.getCameraScale(client.getFrameTime());
        updatePoses(cameraEntity, MCXRPlayClient.heightAdjustStand, predictedDisplayTime, client.getFrameTime(), frameUserScale);
        camera.updateXR(this.client.level, cameraEntity, MCXRPlayClient.viewSpacePoses.getMinecraftPose());

        client.getWindow().setErrorSection("Render");
        client.getProfiler().push("sound");
        client.getSoundManager().updateSource(client.gameRenderer.getMainCamera());
        client.getProfiler().pop();

        //Render GUI
        mainRenderTarget.setFramebuffer(FGM.guiRenderTarget);
        //Need to do this once framebuffer is gui
        XrInput.postTick(predictedDisplayTime);

        mainRenderTarget.clear(Minecraft.ON_OSX);
        clientExt.doRender(true, frameStartTime, RenderPass.GUI);
        mainRenderTarget.resetFramebuffer();

        FGM.guiPostProcessRenderTarget.bindWrite(true);
        this.guiBlitShader.setSampler("DiffuseSampler", FGM.guiRenderTarget.getColorTextureId());
        this.guiBlitShader.setSampler("DepthSampler", FGM.guiRenderTarget.getDepthTextureId());
        this.blit(FGM.guiPostProcessRenderTarget, guiBlitShader);
        FGM.guiPostProcessRenderTarget.unbindWrite();

        //pre-render overlays

        OpenXRSwapchain swapchain = session.swapchain;
        int swapchainImageIndex = swapchain.acquireImage();

        // Render view to the appropriate part of the swapchain image.
        for (int viewIndex = 0; viewIndex < 2; viewIndex++) {
            // Each view has a separate swapchain which is acquired, rendered to, and released.

            var subImage = projectionLayerViews.get(viewIndex)
                    .type(XR10.XR_TYPE_COMPOSITION_LAYER_PROJECTION_VIEW)
                    .pose(session.viewBuffer.get(viewIndex).pose())
                    .fov(session.viewBuffer.get(viewIndex).fov())
                    .subImage();
            subImage.swapchain(swapchain.handle);
            subImage.imageRect().offset().set(0, 0);
            subImage.imageRect().extent().set(swapchain.width, swapchain.height);
            subImage.imageArrayIndex(viewIndex);

            XrRenderTarget swapchainFramebuffer;
            if (viewIndex == 0) {
                swapchainFramebuffer = swapchain.leftFramebuffers[swapchainImageIndex];
            } else {
                swapchainFramebuffer = swapchain.rightFramebuffers[swapchainImageIndex];
            }
            mainRenderTarget.setXrFramebuffer(swapchain.renderTarget);
            RenderPass.XrWorld worldRenderPass = RenderPass.XrWorld.create();
            worldRenderPass.fov = session.viewBuffer.get(viewIndex).fov();
            worldRenderPass.eyePoses.updatePhysicalPose(session.viewBuffer.get(viewIndex).pose(), MCXRPlayClient.stageTurn, frameUserScale);
            worldRenderPass.eyePoses.updateGamePose(MCXRPlayClient.xrOrigin);
            worldRenderPass.viewIndex = viewIndex;
            camera.setPose(worldRenderPass.eyePoses.getMinecraftPose());
            clientExt.doRender(true, frameStartTime, worldRenderPass);

            swapchainFramebuffer.bindWrite(true);
            this.blitShader.setSampler("DiffuseSampler", swapchain.renderTarget.getColorTextureId());
            this.blit(swapchainFramebuffer, blitShader);
//          ==render to eyes here after eye swapchain.rendertarget is sampled and blit-ed to swapchainFramebuffer (displayed image per eye?)==
            LocalPlayer player = this.client.player;
            if(player!=null) {
                //vanilla vignette
                renderVignette(swapchainFramebuffer,cameraEntity);
                //portal
                float g = Mth.lerp(client.getDeltaFrameTime(), player.oPortalTime, player.portalTime);
                if (g > 0.0F && !player.hasEffect(MobEffects.CONFUSION)) {
                    renderPortalOverlay(swapchainFramebuffer, g);
                }
                //hurt
                int hurtTime = player.hurtTime;
                if(hurtTime>0){
                    renderOverlay(swapchainFramebuffer,new ResourceLocation("textures/misc/hurt_vr.png"),0.4f,0f,0f,hurtTime*0.06f);
                }
                //drowning
                float drownPoint =Mth.clamp(2.5f*(0.7f-player.getAirSupply()/player.getMaxAirSupply()),0f,1f);
                if(drownPoint>0f){
                    renderOverlay(swapchainFramebuffer,new ResourceLocation("textures/misc/vignette_vr.png"),0.0f,0f,0.2f,drownPoint*0.9f);
                }
                //on fire
                if(player.isOnFire()){
                    renderOverlay(swapchainFramebuffer, new ResourceLocation("textures/misc/vignette_vr.png"),1f,0.7f,0.2f,0.9f);
                }
                //frozen
                if (player.getTicksFrozen() > 0) {
                    float freeze = player.getPercentFrozen()*0.9f;
                    renderOverlay(swapchainFramebuffer,new ResourceLocation("textures/misc/vignette_vr.png"),0.85f,0.85f,1f,freeze);
                }
                //death point
                float deathPoint =Mth.clamp(2.5f*(0.7f-player.getHealth()/player.getMaxHealth()),0f,1f);
                if(deathPoint>0f){
                    renderOverlay(swapchainFramebuffer,new ResourceLocation("textures/misc/vignette_vr.png"),0.4f,0f,0f,deathPoint*0.9f);
                }
            }
            swapchainFramebuffer.unbindWrite();
        }

        blitToBackbuffer(swapchain.renderTarget);

        instance.checkPanic(XR10.xrReleaseSwapchainImage(
                swapchain.handle,
                XrSwapchainImageReleaseInfo.calloc(stack)
                        .type(XR10.XR_TYPE_SWAPCHAIN_IMAGE_RELEASE_INFO)
        ), "xrReleaseSwapchainImage");

        mainRenderTarget.resetFramebuffer();
        camera.setPose(MCXRPlayClient.viewSpacePoses.getMinecraftPose());
        clientExt.postRender();

        return XrCompositionLayerProjection.calloc(stack)
                .type(XR10.XR_TYPE_COMPOSITION_LAYER_PROJECTION)
                .space(session.xrAppSpace)
                .views(projectionLayerViews);
//        }
    }

    private void updatePoses(Entity camEntity,
                             boolean calculateHeightAdjust,
                             long predictedDisplayTime,
                             float delta,
                             float scale) {
        if (session.state == XR10.XR_SESSION_STATE_FOCUSED) {
            for (int i = 0; i < 2; i++) {
                if (!XrInput.handsActionSet.grip.isActive[i]) {
                    continue;
                }
                session.setPosesFromSpace(XrInput.handsActionSet.grip.spaces[i], predictedDisplayTime, XrInput.handsActionSet.gripPoses[i], scale);
                session.setPosesFromSpace(XrInput.handsActionSet.aim.spaces[i], predictedDisplayTime, XrInput.handsActionSet.aimPoses[i], scale);
            }
            session.setPosesFromSpace(session.xrViewSpace, predictedDisplayTime, MCXRPlayClient.viewSpacePoses, scale);
        }

        if (camEntity != null) { //TODO seriously need to tidy up poses
            if (client.isPaused()) {
                delta = 1.0f;
            }

            if (calculateHeightAdjust && MCXRPlayClient.heightAdjustStand && camEntity instanceof LivingEntity livingEntity) {
                float userHeight = MCXRPlayClient.viewSpacePoses.getPhysicalPose().getPos().y();
                float playerEyeHeight = ((LivingEntityAcc) livingEntity).callGetStandingEyeHeight(livingEntity.getPose(), livingEntity.getDimensions(livingEntity.getPose()));

                MCXRPlayClient.heightAdjust = playerEyeHeight - userHeight;
            }

            Entity vehicle = camEntity.getVehicle();
            if (MCXRCore.getCoreConfig().roomscaleMovement() && vehicle == null) {
                MCXRPlayClient.xrOrigin.set(Mth.lerp(delta, camEntity.xo, camEntity.getX()) - MCXRPlayClient.playerPhysicalPosition.x,
                        Mth.lerp(delta, camEntity.yo, camEntity.getY()),
                        Mth.lerp(delta, camEntity.zo, camEntity.getZ()) - MCXRPlayClient.playerPhysicalPosition.z);
            } else {
                MCXRPlayClient.xrOrigin.set(Mth.lerp(delta, camEntity.xo, camEntity.getX()),
                        Mth.lerp(delta, camEntity.yo, camEntity.getY()),
                        Mth.lerp(delta, camEntity.zo, camEntity.getZ()));
            }
            if (vehicle != null) {
                if (vehicle instanceof LivingEntity) {
                    MCXRPlayClient.xrOrigin.y += 0.60;
                } else {
                    MCXRPlayClient.xrOrigin.y += 0.54 - vehicle.getPassengersRidingOffset();
                }
            }
            if (MCXRPlayClient.heightAdjustStand) {
                MCXRPlayClient.xrOrigin.y += MCXRPlayClient.heightAdjust;
            }

            MCXRPlayClient.viewSpacePoses.updateGamePose(MCXRPlayClient.xrOrigin);
            for (var poses : XrInput.handsActionSet.gripPoses) {
                poses.updateGamePose(MCXRPlayClient.xrOrigin);
            }
            for (var poses : XrInput.handsActionSet.aimPoses) {
                poses.updateGamePose(MCXRPlayClient.xrOrigin);
            }
        }
    }

    private Struct renderBlankLayer(long predictedDisplayTime, MemoryStack stack) {
        IntBuffer intBuf = stackCallocInt(1);

        instance.checkPanic(XR10.xrLocateViews(
                session.handle,
                XrViewLocateInfo.calloc(stack).set(XR10.XR_TYPE_VIEW_LOCATE_INFO,
                        0,
                        session.viewConfigurationType,
                        predictedDisplayTime,
                        session.xrAppSpace
                ),
                XrViewState.calloc(stack).type(XR10.XR_TYPE_VIEW_STATE),
                intBuf,
                session.viewBuffer
        ), "xrLocateViews");

        int viewCountOutput = intBuf.get(0);

        var projectionLayerViews = XrCompositionLayerProjectionView.calloc(viewCountOutput);

        int swapchainImageIndex = session.swapchain.acquireImage();

        for (int viewIndex = 0; viewIndex < viewCountOutput; viewIndex++) {
            XrCompositionLayerProjectionView projectionLayerView = projectionLayerViews.get(viewIndex);
            projectionLayerView.type(XR10.XR_TYPE_COMPOSITION_LAYER_PROJECTION_VIEW);
            projectionLayerView.pose(session.viewBuffer.get(viewIndex).pose());
            projectionLayerView.fov(session.viewBuffer.get(viewIndex).fov());
            projectionLayerView.subImage().swapchain(session.swapchain.handle);
            projectionLayerView.subImage().imageRect().offset().set(0, 0);
            projectionLayerView.subImage().imageRect().extent().set(session.swapchain.width, session.swapchain.height);
            projectionLayerView.subImage().imageArrayIndex(0);
        }

        session.swapchain.leftFramebuffers[swapchainImageIndex].clear(Minecraft.ON_OSX);

        instance.checkPanic(XR10.xrReleaseSwapchainImage(
                session.swapchain.handle,
                XrSwapchainImageReleaseInfo.calloc(stack).type(XR10.XR_TYPE_SWAPCHAIN_IMAGE_RELEASE_INFO)
        ), "xrReleaseSwapchainImage");

        XrCompositionLayerProjection layer = XrCompositionLayerProjection.calloc(stack).type(XR10.XR_TYPE_COMPOSITION_LAYER_PROJECTION);
        layer.space(session.xrAppSpace);
        layer.views(projectionLayerViews);
        return layer;
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
        BufferUploader.draw(bufferBuilder.end());
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

        int width = mainRenderTarget.minecraftMainRenderTarget.width;
        int height = mainRenderTarget.minecraftMainRenderTarget.height;

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

        //maintain screen's square aspect ratio
        int xOff=0;
        int yOff=0;
        boolean fullImage=PlayOptions.fullMirror;
        if(width>height){
            if(fullImage) xOff=(width-height)/2;
            else yOff=-(width-height)/2;
        }
        else{
            if(fullImage) yOff=(height-width)/2;
            else xOff=-(height-width)/2;
        }

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        bufferBuilder.vertex(xOff, height-yOff, 0.0).uv(0.0F, 0.0f).color(255, 255, 255, 255).endVertex();
        bufferBuilder.vertex(width-xOff, height-yOff, 0.0).uv(1, 0.0f).color(255, 255, 255, 255).endVertex();
        bufferBuilder.vertex(width-xOff, yOff, 0.0).uv(1, 1.0f).color(255, 255, 255, 255).endVertex();
        bufferBuilder.vertex(xOff, yOff, 0.0).uv(0.0F, 1.0F).color(255, 255, 255, 255).endVertex();
        BufferUploader.draw(bufferBuilder.end());
        shader.clear();
        GlStateManager._depthMask(true);
        GlStateManager._colorMask(true, true, true, true);

        matrixStack.popPose();
    }

    private void renderOverlay(RenderTarget framebuffer, ResourceLocation texture, float red, float green, float blue,float alpha) {
        //ShaderInstance shader = Minecraft.getInstance().gameRenderer.blitShader;//to screen
        ShaderInstance shader = this.blitShader;//to eye

        TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        AbstractTexture abstractTexture = textureManager.getTexture(texture);

        shader.setSampler("DiffuseSampler", abstractTexture.getId());

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
        GlStateManager._enableBlend();
        GlStateManager._blendFunc(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.SourceFactor.ONE_MINUS_SRC_ALPHA.value);

        Matrix4f matrix4f = Matrix4f.orthographic((float) width, (float) -height, 1000.0F, 3000.0F);
        RenderSystem.setProjectionMatrix(matrix4f);
        if (shader.MODEL_VIEW_MATRIX != null) {
            shader.MODEL_VIEW_MATRIX.set(Matrix4f.createTranslateMatrix(0.0F, 0.0F, -2000.0F));
        }

        if (shader.PROJECTION_MATRIX != null) {
            shader.PROJECTION_MATRIX.set(matrix4f);
        }

        shader.apply();
        Tesselator tessellator = RenderSystem.renderThreadTesselator();//Tesselator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        bufferBuilder.vertex(0.0, height, -90.0).uv(0f, 0f).color(red, green, blue, alpha).endVertex();
        bufferBuilder.vertex(width, height, -90.0).uv(1f, 0f).color(red, green, blue, alpha).endVertex();
        bufferBuilder.vertex(width, 0.0, -90.0).uv(1f, 1f).color(red, green, blue, alpha).endVertex();
        bufferBuilder.vertex(0.0, 0.0, -90.0).uv(0f, 1f).color(red, green, blue, alpha).endVertex();

        BufferUploader.draw(bufferBuilder.end());
        shader.clear();
        GlStateManager._depthMask(true);
        GlStateManager._colorMask(true, true, true, true);
        //GlStateManager._disableBlend();

        matrixStack.popPose();
    }

    private void renderVignette(RenderTarget framebuffer,Entity entity) {
        WorldBorder worldBorder = this.client.level.getWorldBorder();
        float f = (float)worldBorder.getDistanceToBorder(entity);
        double d = Math.min(
                worldBorder.getLerpSpeed() * (double)worldBorder.getWarningTime() * 1000.0, Math.abs(worldBorder.getLerpTarget() - worldBorder.getSize())
        );
        double e = Math.max((double)worldBorder.getWarningBlocks(), d);
        if ((double)f < e) {
            f = 1.0F - (float)((double)f / e);
        } else {
            f = 0.0F;
        }
        //RenderSystem.blendFuncSeparate(
        //        GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
        //);
        //breaks sky rendering in second viewindex, just use a pre-formatter png
        //GlStateManager._blendFuncSeparate(GlStateManager.SourceFactor.ZERO.value, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR.value, GlStateManager.SourceFactor.ONE.value, GlStateManager.DestFactor.ZERO.value);
        if (f > 0.0F) {
            f = Mth.clamp(f, 0.0F, 1.0F);
            //RenderSystem.setShaderColor(0.0F, f, f, 1.0F);
            //renderOverlay(framebuffer, new ResourceLocation("textures/misc/vignette.png"),0f,f,f,1f);
            renderOverlay(framebuffer, new ResourceLocation("textures/misc/vignette_vr.png"),0f,0f,0f,f);
        } else {
            float l = LightTexture.getBrightness(entity.level.dimensionType(), entity.level.getMaxLocalRawBrightness(new BlockPos(entity.getX(), entity.getEyeY(), entity.getZ())));
            float g = Mth.clamp(1.0F - l, 0.0F, 1.0F);
            //RenderSystem.setShaderColor(g, g, g, 1.0F);
            //renderOverlay(framebuffer, new ResourceLocation("textures/misc/vignette.png"),g,g,g,1f);
            renderOverlay(framebuffer, new ResourceLocation("textures/misc/vignette_vr.png"),0f,0f,0f,g);
        }
    }

    private void renderPortalOverlay(RenderTarget framebuffer, float nauseaStrength) {
        if (nauseaStrength < 1.0F) {
            nauseaStrength *= nauseaStrength;
            nauseaStrength *= nauseaStrength;
            nauseaStrength = nauseaStrength * 0.8F;
        }
        ShaderInstance shader = this.blitShader;//to eye

        TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        AbstractTexture abstractTexture = textureManager.getTexture(InventoryMenu.BLOCK_ATLAS);

        shader.setSampler("DiffuseSampler", abstractTexture.getId());

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
        GlStateManager._enableBlend();

        Matrix4f matrix4f = Matrix4f.orthographic((float) width, (float) -height, 1000.0F, 3000.0F);
        RenderSystem.setProjectionMatrix(matrix4f);
        if (shader.MODEL_VIEW_MATRIX != null) {
            shader.MODEL_VIEW_MATRIX.set(Matrix4f.createTranslateMatrix(0.0F, 0.0F, -2000.0F));
        }

        if (shader.PROJECTION_MATRIX != null) {
            shader.PROJECTION_MATRIX.set(matrix4f);
        }

        shader.apply();
        TextureAtlasSprite textureAtlasSprite = this.client.getBlockRenderer().getBlockModelShaper().getParticleIcon(Blocks.NETHER_PORTAL.defaultBlockState());
        float f = textureAtlasSprite.getU0();
        float g = textureAtlasSprite.getV0();
        float h = textureAtlasSprite.getU1();
        float i = textureAtlasSprite.getV1();
        Tesselator tessellator = RenderSystem.renderThreadTesselator();//Tesselator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        bufferBuilder.vertex(0.0, height, -90.0).uv(f, i).color(1f, 1f, 1f, nauseaStrength).endVertex();
        bufferBuilder.vertex(width, height, -90.0).uv(h, i).color(1f, 1f, 1f, nauseaStrength).endVertex();
        bufferBuilder.vertex(width, 0.0, -90.0).uv(h, g).color(1f, 1f, 1f, nauseaStrength).endVertex();
        bufferBuilder.vertex(0.0, 0.0, -90.0).uv(f, g).color(1f, 1f, 1f, nauseaStrength).endVertex();

        BufferUploader.draw(bufferBuilder.end());
        shader.clear();
        GlStateManager._depthMask(true);
        GlStateManager._colorMask(true, true, true, true);
        //GlStateManager._disableBlend();

        matrixStack.popPose();
    }
}
