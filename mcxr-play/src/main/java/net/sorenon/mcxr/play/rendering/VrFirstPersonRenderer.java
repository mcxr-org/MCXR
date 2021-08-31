package net.sorenon.mcxr.play.rendering;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.*;
import net.minecraft.world.LightType;
import net.sorenon.fart.FartUtil;
import net.sorenon.fart.RenderStateShards;
import net.sorenon.fart.RenderTypeBuilder;
import net.sorenon.mcxr.core.JOMLUtil;
import net.sorenon.mcxr.core.MCXRCore;
import net.sorenon.mcxr.core.Pose;
import net.sorenon.mcxr.play.FlatGuiManager;
import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.play.input.XrInput;
import net.sorenon.mcxr.play.openxr.XrRenderer;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.function.Function;
import java.util.function.Supplier;

import static net.sorenon.mcxr.core.JOMLUtil.convert;

/**
 * TODO Split up and dehackify
 * TODO rip apart this class then put it back together again
 */
public class VrFirstPersonRenderer {

    private static final XrRenderer XR_RENDERER = MCXRPlayClient.RENDERER;

    private final FlatGuiManager FGM;

    private final ModelPart[] slimArmModel = new ModelPart[2];
    private final ModelPart[] armModel = new ModelPart[2];

    public VrFirstPersonRenderer(FlatGuiManager flatGuiManager) {
        this.FGM = flatGuiManager;
        for (int slim = 0; slim < 2; slim++) {
            ModelPart[] arr = slim == 0 ? armModel : slimArmModel;
            for (int hand = 0; hand < 2; hand++) {
                ModelPartBuilder builder = ModelPartBuilder.create();

                if (hand == 0) {
                    builder.uv(32, 48);
                } else {
                    builder.uv(40, 16);
                }

                if (slim == 0) {
                    builder.cuboid(0, 0, 0, 4, 12, 4, hand == 0);
                } else {
                    builder.cuboid(0.5f, 0, 0, 3, 12, 4, hand == 0);
                }

                arr[hand] = new ModelData().getRoot().addChild(
                        "arm",
                        builder,
                        ModelTransform.NONE).createPart(64, 64);
            }
        }
    }

    public void renderAfterEntities(WorldRenderContext context) {
        Entity camEntity = context.camera().getFocusedEntity();

        if (camEntity != null) {
            renderShadow(context, camEntity);
            if (!FGM.isScreenOpen() && !MCXRCore.getCoreConfig().controllerRaytracing()) {
                MatrixStack matrices = context.matrixStack();
                Vec3d camPos = context.camera().getPos();
                matrices.push();

                double x = MathHelper.lerp(context.tickDelta(), camEntity.lastRenderX, camEntity.getX());
                double y = MathHelper.lerp(context.tickDelta(), camEntity.lastRenderY, camEntity.getY()) + camEntity.getStandingEyeHeight();
                double z = MathHelper.lerp(context.tickDelta(), camEntity.lastRenderZ, camEntity.getZ());
                matrices.translate(x - camPos.x, y - camPos.y, z - camPos.z);

                matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-camEntity.getYaw() + 180.0F));
                matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90-camEntity.getPitch()));

                Matrix4f model = matrices.peek().getModel();
                Matrix3f normal = matrices.peek().getNormal();

                VertexConsumer consumer = context.consumers().getBuffer(LINE_CUSTOM_ALWAYS.apply(4.0));
                consumer.vertex(model, 0, 0, 0).color(0f, 0f, 0f, 1f).normal(normal, 0, -1, 0).next();
                consumer.vertex(model, 0, -5, 0).color(0f, 0f, 0f, 1f).normal(normal, 0, -1, 0).next();

                consumer = context.consumers().getBuffer(LINE_CUSTOM.apply(2.0));
                consumer.vertex(model, 0, 0, 0).color(1f, 0f, 0f, 1f).normal(normal, 0, -1, 0).next();
                consumer.vertex(model, 0, -5, 0).color(0.7f, 0.7f, 0.7f, 1f).normal(normal, 0, -1, 0).next();

                matrices.pop();
            }

            if (!FGM.isScreenOpen() && camEntity instanceof LivingEntity livingEntity) {
                for (int hand = 0; hand < 2; hand++) {
                    if (!XrInput.handsActionSet.grip.isActive[hand]) {
                        continue;
                    }

                    ItemStack stack = hand == 0 ? livingEntity.getOffHandStack() : livingEntity.getMainHandStack();

                    if (!stack.isEmpty()) {
                        MatrixStack matrices = context.matrixStack();
                        boolean mainHand = hand == MCXRPlayClient.mainHand;
                        matrices.push();
                        transformToHand(matrices, hand, context.tickDelta());

                        int light = LightmapTextureManager.pack(livingEntity.world.getLightLevel(LightType.BLOCK, livingEntity.getBlockPos()), livingEntity.world.getLightLevel(LightType.SKY, livingEntity.getBlockPos()));

                        if (mainHand) {
                            float swing = -0.4f * MathHelper.sin((float) (Math.sqrt(livingEntity.getHandSwingProgress(context.tickDelta())) * Math.PI * 2));
                            matrices.multiply(Vec3f.POSITIVE_X.getRadialQuaternion(swing));
                        }

                        MinecraftClient.getInstance().getHeldItemRenderer().renderItem(
                                livingEntity,
                                stack,
                                hand == 0 ? ModelTransformation.Mode.THIRD_PERSON_LEFT_HAND : ModelTransformation.Mode.THIRD_PERSON_RIGHT_HAND,
                                hand == 0,
                                matrices,
                                context.consumers(),
                                light
                        );

                        matrices.pop();
                    }
                }
            }
        }

        hands(context);

        MatrixStack matrices = context.matrixStack();
        for (int hand = 0; hand < 2; hand++) {
            if (!XrInput.handsActionSet.grip.isActive[hand]) {
                continue;
            }

            matrices.push(); //1

            Pose pose = XrInput.handsActionSet.gripPoses[hand].getGamePose();
            Vec3d gripPos = convert(pose.getPos());
            Vector3f eyePos = ((RenderPass.World) XR_RENDERER.renderPass).eyePoses.getGamePose().getPos();
            matrices.translate(gripPos.x - eyePos.x(), gripPos.y - eyePos.y(), gripPos.z - eyePos.z());

            float scale = MCXRPlayClient.getCameraScale();
            matrices.scale(scale, scale, scale);

            matrices.push(); //2
            matrices.multiply(
                    JOMLUtil.convert(
                            pose.getOrientation()
                                    .rotateX((float) Math.toRadians(MCXRPlayClient.handPitchAdjust), new Quaternionf())
                    )
            );

            if (hand == MCXRPlayClient.mainHand && (MCXRCore.getCoreConfig().controllerRaytracing() || FGM.isScreenOpen())) {
                Matrix4f model = matrices.peek().getModel();
                Matrix3f normal = matrices.peek().getNormal();

                VertexConsumer consumer = context.consumers().getBuffer(LINE_CUSTOM_ALWAYS.apply(4.0));
                consumer.vertex(model, 0, 0, 0).color(0f, 0f, 0f, 1f).normal(normal, 0, -1, 0).next();
                consumer.vertex(model, 0, -5, 0).color(0f, 0f, 0f, 1f).normal(normal, 0, -1, 0).next();

                consumer = context.consumers().getBuffer(LINE_CUSTOM.apply(2.0));
                consumer.vertex(model, 0, 0, 0).color(1f, 0f, 0f, 1f).normal(normal, 0, -1, 0).next();
                consumer.vertex(model, 0, -5, 0).color(0.7f, 0.7f, 0.7f, 1f).normal(normal, 0, -1, 0).next();
            }

            boolean debug = MinecraftClient.getInstance().options.debugEnabled;

            if (debug) {
                FartUtil.renderCrosshair(context.consumers(), context.matrixStack(), 0.05f, false);
            }

            matrices.pop(); //2

            if (debug) {
                matrices.multiply(
                        JOMLUtil.convert(
                                pose.getOrientation()
                        )
                );
                FartUtil.renderCrosshair(context.consumers(), context.matrixStack(), 0.1f, false);
            }

            matrices.pop(); //1
        }

    }

    public void hands(WorldRenderContext context) {
        MatrixStack matrices = context.matrixStack();

        for (int hand = 0; hand < 2; hand++) {
            if (!XrInput.handsActionSet.grip.isActive[hand]) {
                continue;
            }
            matrices.push();

            transformToHand(matrices, hand, context.tickDelta());

            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-90.0F));
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F));

            matrices.translate(-2 / 16f, -12 / 16f, 0);

            //Draw hand
            if (context.camera().getFocusedEntity() instanceof ClientPlayerEntity player) {
                matrices.push();
                ModelPart armModel;
                if (player.getModel().equals("slim")) {
                    armModel = this.slimArmModel[hand];
                } else {
                    armModel = this.armModel[hand];
                }

                int light = LightmapTextureManager.pack(player.world.getLightLevel(LightType.BLOCK, player.getBlockPos()), player.world.getLightLevel(LightType.SKY, player.getBlockPos()));

                VertexConsumer consumer = context.consumers().getBuffer(RenderLayer.getEntityTranslucent(player.getSkinTexture()));
                armModel.render(matrices, consumer, light, OverlayTexture.DEFAULT_UV);
                matrices.pop();
            }

            matrices.pop();
        }
    }

    public void transformToHand(MatrixStack matrices, int hand, float tickDelta) {
        Pose pose = XrInput.handsActionSet.gripPoses[hand].getGamePose();
        Vec3d gripPos = convert(pose.getPos());
        Vector3f eyePos = ((RenderPass.World) XR_RENDERER.renderPass).eyePoses.getGamePose().getPos();

        //Transform to controller
        matrices.translate(gripPos.x - eyePos.x(), gripPos.y - eyePos.y(), gripPos.z - eyePos.z());
        matrices.multiply(convert(pose.getOrientation()));

        //Apply adjustments
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-90.0F));
        matrices.scale(0.4f, 0.4f, 0.4f);

        float scale = MCXRPlayClient.getCameraScale(tickDelta);
        matrices.scale(scale, scale, scale);

        matrices.translate(0, 1 / 16f, -1.5f / 16f);
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(MCXRPlayClient.handPitchAdjust));
    }

    public void renderShadow(WorldRenderContext context, Entity camEntity) {
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

    public void renderHud(WorldRenderContext context) {
        MatrixStack matrices = context.matrixStack();
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        VertexConsumerProvider.Immediate consumers = (VertexConsumerProvider.Immediate) context.consumers();
        assert consumers != null;

        if (FGM.pos != null) {
            matrices.push();
            Vec3d pos = FGM.pos.subtract(convert(((RenderPass.World) XR_RENDERER.renderPass).eyePoses.getPhysicalPose().getPos()));
            matrices.translate(pos.x, pos.y, pos.z);
            matrices.multiply(new Quaternion((float) FGM.rot.x, (float) FGM.rot.y, (float) FGM.rot.z, (float) FGM.rot.w));
            renderGuiQuad(matrices.peek(), consumers);
            matrices.pop();
            consumers.drawCurrentLayer();
        }

        for (int hand = 0; hand < 2; hand++) {
            if (!XrInput.handsActionSet.grip.isActive[hand]) {
                continue;
            }
            matrices.push();

            transformToHand(matrices, hand, context.tickDelta());

            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-90.0F));
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F));

            matrices.translate(-2 / 16f, -12 / 16f, 0);

            if (hand == 0 && !FGM.isScreenOpen()) {
                matrices.push();
                matrices.translate(2 / 16f, 9 / 16f, -1 / 16f);
                matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-75f));
                renderHudQuad(matrices.peek(), consumers);
                consumers.drawCurrentLayer();
                matrices.pop();
            }
            matrices.pop();
        }
    }

    private void renderGuiQuad(MatrixStack.Entry transform, VertexConsumerProvider consumers) {
        Framebuffer guiFramebuffer = FGM.frontFramebuffer;

        float size = 1f;
        float x = size / 2;
        float y = size * guiFramebuffer.textureHeight / guiFramebuffer.textureWidth;

        VertexConsumer consumer = consumers.getBuffer(GUI_NO_DEPTH_TEST.apply(MCXRPlayClient.INSTANCE.flatGuiManager.texture));
        Matrix4f modelMatrix = transform.getModel();
        Matrix3f normalMatrix = transform.getNormal();
        consumer.vertex(modelMatrix, -x, y, 0).color(255, 255, 255, 255).texture(1, 1).overlay(OverlayTexture.DEFAULT_UV).light(15728880).normal(normalMatrix, 0, 0, -1).next();
        consumer.vertex(modelMatrix, x, y, 0).color(255, 255, 255, 255).texture(0, 1).overlay(OverlayTexture.DEFAULT_UV).light(15728880).normal(normalMatrix, 0, 0, -1).next();
        consumer.vertex(modelMatrix, x, 0, 0).color(255, 255, 255, 255).texture(0, 0).overlay(OverlayTexture.DEFAULT_UV).light(15728880).normal(normalMatrix, 0, 0, -1).next();
        consumer.vertex(modelMatrix, -x, 0, 0).color(255, 255, 255, 255).texture(1, 0).overlay(OverlayTexture.DEFAULT_UV).light(15728880).normal(normalMatrix, 0, 0, -1).next();
        consumer = consumers.getBuffer(DEPTH_ONLY.apply(MCXRPlayClient.INSTANCE.flatGuiManager.texture));
        consumer.vertex(modelMatrix, -x, y, 0).color(255, 255, 255, 255).texture(1, 1).overlay(OverlayTexture.DEFAULT_UV).light(15728880).normal(normalMatrix, 0, 0, -1).next();
        consumer.vertex(modelMatrix, x, y, 0).color(255, 255, 255, 255).texture(0, 1).overlay(OverlayTexture.DEFAULT_UV).light(15728880).normal(normalMatrix, 0, 0, -1).next();
        consumer.vertex(modelMatrix, x, 0, 0).color(255, 255, 255, 255).texture(0, 0).overlay(OverlayTexture.DEFAULT_UV).light(15728880).normal(normalMatrix, 0, 0, -1).next();
        consumer.vertex(modelMatrix, -x, 0, 0).color(255, 255, 255, 255).texture(1, 0).overlay(OverlayTexture.DEFAULT_UV).light(15728880).normal(normalMatrix, 0, 0, -1).next();
    }

    private void renderHudQuad(MatrixStack.Entry transform, VertexConsumerProvider consumers) {
        Framebuffer guiFramebuffer = FGM.backFramebuffer;

        float size = 1f;
        float x = size / 2;
        float y = size * guiFramebuffer.textureHeight / guiFramebuffer.textureWidth;

        VertexConsumer consumer = consumers.getBuffer(RenderLayer.getEntityTranslucent(MCXRPlayClient.INSTANCE.flatGuiManager.texture));
        Matrix4f modelMatrix = transform.getModel();
        Matrix3f normalMatrix = transform.getNormal();
        consumer.vertex(modelMatrix, -x, y, 0).color(255, 255, 255, 255).texture(1, 1).overlay(OverlayTexture.DEFAULT_UV).light(15728880).normal(normalMatrix, 0, 0, -1).next();
        consumer.vertex(modelMatrix, x, y, 0).color(255, 255, 255, 255).texture(0, 1).overlay(OverlayTexture.DEFAULT_UV).light(15728880).normal(normalMatrix, 0, 0, -1).next();
        consumer.vertex(modelMatrix, x, 0, 0).color(255, 255, 255, 255).texture(0, 0).overlay(OverlayTexture.DEFAULT_UV).light(15728880).normal(normalMatrix, 0, 0, -1).next();
        consumer.vertex(modelMatrix, -x, 0, 0).color(255, 255, 255, 255).texture(1, 0).overlay(OverlayTexture.DEFAULT_UV).light(15728880).normal(normalMatrix, 0, 0, -1).next();
    }

    public static final Function<Identifier, RenderLayer> DEPTH_ONLY = Util.memoize((texture) -> {
        RenderTypeBuilder renderTypeBuilder = new RenderTypeBuilder(MCXRPlayClient.id("depth_only"), VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, false, false);
        renderTypeBuilder.innerBuilder
                .writeMaskState(RenderStateShards.DEPTH_WRITE)
                .shader(RenderStateShards.shader(GameRenderer::getRenderTypeEntityCutoutShader))
                .texture(RenderStateShards.texture(texture, false, false))
                .transparency(RenderStateShards.NO_TRANSPARENCY)
                .lightmap(RenderStateShards.LIGHTMAP)
                .overlay(RenderStateShards.OVERLAY);
        return renderTypeBuilder.build(true);
    });

    public static final Function<Identifier, RenderLayer> GUI_NO_DEPTH_TEST = Util.memoize((texture) -> {
        Supplier<Shader> shader = GameRenderer::getNewEntityShader;
        if (FabricLoader.getInstance().isModLoaded("iris")) {
            shader = GameRenderer::getRenderTypeEntityTranslucentShader;
        }

        RenderTypeBuilder renderTypeBuilder = new RenderTypeBuilder(MCXRPlayClient.id("gui_no_depth_test"), VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, false, true);
        renderTypeBuilder.innerBuilder.
                shader(RenderStateShards.shader(shader))
                .texture(RenderStateShards.texture(texture, false, false))
                .transparency(RenderStateShards.TRANSLUCENT_TRANSPARENCY)
                .cull(RenderStateShards.NO_CULL)
                .lightmap(RenderStateShards.LIGHTMAP)
                .overlay(RenderStateShards.OVERLAY)
                .depthTest(RenderStateShards.NO_DEPTH_TEST);
        return renderTypeBuilder.build(true);
    });

    public static final Function<Double, RenderLayer> LINE_CUSTOM_ALWAYS = Util.memoize(aDouble -> {
        RenderTypeBuilder builder = new RenderTypeBuilder(MCXRPlayClient.id("line_always"), VertexFormats.LINES, VertexFormat.DrawMode.LINES, 16, false, false);
        builder.innerBuilder
                .shader(RenderStateShards.shader(GameRenderer::getRenderTypeLinesShader))
                .lineWidth(RenderStateShards.lineWidth(aDouble))
                .layering(RenderStateShards.VIEW_OFFSET_Z_LAYERING)
                .transparency(RenderStateShards.TRANSLUCENT_TRANSPARENCY)
                .writeMaskState(RenderStateShards.COLOR_DEPTH_WRITE)
                .cull(RenderStateShards.NO_CULL)
                .depthTest(RenderStateShards.NO_DEPTH_TEST);
        return builder.build(true);
    });

    public static final Function<Double, RenderLayer> LINE_CUSTOM = Util.memoize(aDouble -> {
        RenderTypeBuilder builder = new RenderTypeBuilder(MCXRPlayClient.id("line"), VertexFormats.LINES, VertexFormat.DrawMode.LINES, 16, false, false);
        builder.innerBuilder
                .shader(RenderStateShards.shader(GameRenderer::getRenderTypeLinesShader))
                .lineWidth(RenderStateShards.lineWidth(aDouble))
                .layering(RenderStateShards.VIEW_OFFSET_Z_LAYERING)
                .transparency(RenderStateShards.TRANSLUCENT_TRANSPARENCY)
                .writeMaskState(RenderStateShards.COLOR_DEPTH_WRITE)
                .cull(RenderStateShards.NO_CULL);
        return builder.build(true);
    });
}
