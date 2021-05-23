package net.sorenon.minexraft.client.rendering;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import net.sorenon.minexraft.client.MineXRaftClient;
import net.sorenon.minexraft.client.Pose;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

public class VrFirstPersonRenderer {

    public void renderHands(WorldRenderContext context) {
        Entity camEntity = context.camera().getFocusedEntity();

        if (camEntity != null) {
            MatrixStack matrices = context.matrixStack();
            Vec3d camPos = context.camera().getPos();
            matrices.push();
            double x = MathHelper.lerp(context.tickDelta(), camEntity.lastRenderX, camEntity.getX());
            double y = MathHelper.lerp(context.tickDelta(), camEntity.lastRenderY, camEntity.getY());
            double z = MathHelper.lerp(context.tickDelta(), camEntity.lastRenderZ, camEntity.getZ());
            matrices.translate(x - camPos.x, y - camPos.y, z - camPos.z);
            MatrixStack.Entry entry = matrices.peek();

            RenderLayer SHADOW_LAYER = RenderLayer.getEntityShadow(new Identifier("textures/misc/shadow.png"));
            VertexConsumer vertexConsumer = context.consumers().getBuffer(SHADOW_LAYER);

            float alpha = MathHelper.clamp((float) Math.sqrt(camPos.squaredDistanceTo(x, y, z)) / 2f - 0.5f, 0.25f, 1);
            float radius = camEntity.getWidth() / 2;
            float y0 = 0.005f;

            vertexConsumer.vertex(entry.getModel(), -radius, y0, -radius).color(1.0F, 1.0F, 1.0F, alpha).texture(0, 0).overlay(OverlayTexture.DEFAULT_UV).light(15728880).normal(entry.getNormal(), 0.0F, 1.0F, 0.0F).next();
            vertexConsumer.vertex(entry.getModel(), -radius, y0, radius).color(1.0F, 1.0F, 1.0F, alpha).texture(0, 1).overlay(OverlayTexture.DEFAULT_UV).light(15728880).normal(entry.getNormal(), 0.0F, 1.0F, 0.0F).next();
            vertexConsumer.vertex(entry.getModel(), radius, y0, radius).color(1.0F, 1.0F, 1.0F, alpha).texture(1, 1).overlay(OverlayTexture.DEFAULT_UV).light(15728880).normal(entry.getNormal(), 0.0F, 1.0F, 0.0F).next();
            vertexConsumer.vertex(entry.getModel(), radius, y0, -radius).color(1.0F, 1.0F, 1.0F, alpha).texture(1, 0).overlay(OverlayTexture.DEFAULT_UV).light(15728880).normal(entry.getNormal(), 0.0F, 1.0F, 0.0F).next();

            matrices.pop();
        }

        if (camEntity instanceof LivingEntity) {
            for (int i = 0; i < 2; i++) {
                if (!MineXRaftClient.vanillaCompatActionSet.isHandActive[i]) {
                    continue;
                }
                Pose pose = MineXRaftClient.vanillaCompatActionSet.poses[i];
                Vec3d gripPos = pose.getPosMc();
                Vector3f eyePos = MineXRaftClient.eyePose.getPos();
                MatrixStack matrices = context.matrixStack();
                matrices.push();
                matrices.translate(gripPos.x - eyePos.x(), gripPos.y - eyePos.y(), gripPos.z - eyePos.z());
                matrices.multiply(pose.getOrientationMc());

                int light = LightmapTextureManager.pack(camEntity.world.getLightLevel(LightType.BLOCK, camEntity.getBlockPos()), camEntity.world.getLightLevel(LightType.SKY, camEntity.getBlockPos()));

                //Transform to middle of controller
                matrices.multiply(net.minecraft.client.util.math.Vector3f.POSITIVE_X.getDegreesQuaternion(-90.0F));
                matrices.scale(0.4f, 0.4f, 0.4f);
                matrices.translate(0, 1 / 16f, -1.5f / 16f);
                matrices.multiply(net.minecraft.client.util.math.Vector3f.POSITIVE_X.getDegreesQuaternion(MineXRaftClient.handPitchAdjust));
                float swing = -0.4f * MathHelper.sin((float) (Math.sqrt(((LivingEntity) camEntity).getHandSwingProgress(context.tickDelta())) * Math.PI * 2));

                if (camEntity instanceof ClientPlayerEntity) {
                    matrices.push();
                    if (i == MineXRaftClient.mainHand && ((ClientPlayerEntity) camEntity).getMainHandStack().isEmpty()) {
                        matrices.multiply(net.minecraft.client.util.math.Vector3f.POSITIVE_X.getRadialQuaternion(swing));
                    }
                    matrices.multiply(net.minecraft.client.util.math.Vector3f.POSITIVE_X.getDegreesQuaternion(-90.0F));
                    matrices.multiply(net.minecraft.client.util.math.Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F));

                    matrices.translate(-2 / 16f, -12 / 16f, 0);
                    ClientPlayerEntity player = (ClientPlayerEntity) camEntity;
                    ModelPart armModel = new ModelPart(64, 64, 40, 16);
                    if (player.getModel().equals("slim")) {
                        armModel.addCuboid(0.5f, 0, 0, 3.0F, 12.0F, 4.0F);
                    } else {
                        armModel.addCuboid(0, 0, 0, 4.0F, 12.0F, 4.0F);
                    }

                    VertexConsumer consumer = context.consumers().getBuffer(RenderLayer.getEntityTranslucent(player.getSkinTexture()));
                    armModel.render(matrices, consumer, light, OverlayTexture.DEFAULT_UV);
                    matrices.pop();
                }

                LivingEntity camLivEntity = (LivingEntity) camEntity;

                if (i == MineXRaftClient.mainHand && !camLivEntity.getMainHandStack().isEmpty()) {
                    matrices.multiply(net.minecraft.client.util.math.Vector3f.POSITIVE_X.getRadialQuaternion(swing));
                }

                MinecraftClient.getInstance().getHeldItemRenderer().renderItem(
                        camLivEntity,
                        i == 0 ? camLivEntity.getOffHandStack() : camLivEntity.getMainHandStack(),
                        i == 0 ? ModelTransformation.Mode.THIRD_PERSON_LEFT_HAND : ModelTransformation.Mode.THIRD_PERSON_RIGHT_HAND,
                        i == 0,
                        matrices,
                        context.consumers(),
                        light
                );

                matrices.pop();
            }
        }
    }

    public void renderHandsGui() {
        for (int i = 0; i < 2; i++) {
            if (!MineXRaftClient.vanillaCompatActionSet.isHandActive[i]) {
                continue;
            }
            RenderSystem.depthMask(true);
            RenderSystem.disableCull();
            RenderSystem.enableBlend();
            RenderSystem.disableDepthTest();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableTexture();
            RenderSystem.lineWidth(1.0f);
            RenderSystem.enableAlphaTest();
            RenderSystem.defaultAlphaFunc();

            RenderSystem.pushMatrix();
            Pose pose = MineXRaftClient.vanillaCompatActionSet.poses[i];
            Vec3d gripPos = pose.getPosMc();
            Vector3f eyePos = MineXRaftClient.eyePose.getPos();
            RenderSystem.translated(gripPos.x - eyePos.x(), gripPos.y - eyePos.y(), gripPos.z - eyePos.z());

            RenderSystem.pushMatrix();
            Quaternionf quat = pose.getOrientation().rotateX((float) Math.toRadians(MineXRaftClient.handPitchAdjust), new Quaternionf());
            RenderSystem.multMatrix(new Matrix4f(new Quaternion(quat.x, quat.y, quat.z, quat.w)));

            if (i == MineXRaftClient.mainHand) {
                RenderSystem.scalef(0.01F, 0.01F, 0.01F);
                BufferBuilder buffer = Tessellator.getInstance().getBuffer();
                GlStateManager.disableTexture();
                GlStateManager.depthMask(false);
                GL11.glLineWidth(4.0F);
                buffer.begin(GL11.GL_LINES, VertexFormats.POSITION_COLOR);
                buffer.vertex(0, 0, 0).color(0f, 0f, 0f, 1f).next();
                buffer.vertex(0, -1000, 0).color(0.1f, 0.1f, 0.1f, 1f).next();
                Tessellator.getInstance().draw();

                GlStateManager.enableDepthTest();
                GL11.glLineWidth(2.0F);
                buffer.begin(GL11.GL_LINES, VertexFormats.POSITION_COLOR);
                buffer.vertex(0, 0, 0).color(1f, 0.1f, 0.1f, 1f).next();
                buffer.vertex(0, -1000, 0).color(1f, 0.1f, 0.1f, 1f).next();
                Tessellator.getInstance().draw();
                GlStateManager.disableDepthTest();
            } else {
                renderHandGui();
            }
            RenderSystem.popMatrix();

            if (MinecraftClient.getInstance().options.debugEnabled) {
                RenderSystem.scalef(0.01F, 0.01F, 0.01F);
                RenderSystem.multMatrix(new Matrix4f(pose.getOrientationMc()));

                GL11.glPointSize(100.0f);
                BufferBuilder buffer = Tessellator.getInstance().getBuffer();
                buffer.begin(GL11.GL_POINTS, VertexFormats.POSITION_COLOR);
                buffer.vertex(0, 0, 0).color(1f, 0f, 0f, 1f).next();
                Tessellator.getInstance().draw();

                RenderSystem.renderCrosshair(10);
            }

            RenderSystem.popMatrix();
        }

        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        RenderSystem.enableCull();
        RenderSystem.enableTexture();
    }

    private static void renderHandGui() {
        float handGuiScale = 1f / 4000;
        Framebuffer guiFramebuffer = MineXRaftClient.INSTANCE.flatGuiManager.framebuffer;

        float x1 = 0;
        float x0 = x1 - guiFramebuffer.textureHeight * handGuiScale;
        float y = 0.1f;
        float z0 = -0.1f;
        float z1 = z0 + guiFramebuffer.textureWidth * handGuiScale;

        int u0 = 0;
        int u1 = 1;
        int v0 = 0;
        int v1 = 1;

//        MinecraftClient client = MinecraftClient.getInstance(); I love this code so much im going to keep it
//        ScreenshotUtils.saveScreenshot(client.runDirectory, guiFramebuffer.textureWidth, guiFramebuffer.textureHeight, guiFramebuffer, null);
//        System.exit(0);

        RenderSystem.enableTexture();
        RenderSystem.bindTexture(guiFramebuffer.getColorAttachment());
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(x0, y, z1).texture(u0, v1).next();
        bufferBuilder.vertex(x0, y, z0).texture(u1, v1).next();
        bufferBuilder.vertex(x1, y, z0).texture(u1, v0).next();
        bufferBuilder.vertex(x1, y, z1).texture(u0, v0).next();
        Tessellator.getInstance().draw();
        RenderSystem.disableTexture();
    }
}
