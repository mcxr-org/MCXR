package net.sorenon.mcxr.play.rendering;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.model.*;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.*;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
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
import org.lwjgl.opengl.GL11;

import java.util.function.Function;
import java.util.function.Supplier;

import static net.sorenon.mcxr.core.JOMLUtil.convert;

//TODO third person renderer
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
                ModelPartBuilder armBuilder = ModelPartBuilder.create();
                ModelPartBuilder sleeveBuilder = ModelPartBuilder.create();

                if (hand == 0) {
                    armBuilder.uv(32, 48);
                    sleeveBuilder.uv(48, 48);
                } else {
                    armBuilder.uv(40, 16);
                    sleeveBuilder.uv(40, 32);
                }

                if (slim == 0) {
                    armBuilder.cuboid(0, 0, 0, 4, 12, 4);
                    sleeveBuilder.cuboid(0, 0, 0, 4, 12, 4, new Dilation(0.25F));
                } else {
                    armBuilder.cuboid(0.5f, 0, 0, 3, 12, 4);
                    sleeveBuilder.cuboid(0.5f, 0, 0, 3, 12, 4, new Dilation(0.25F));
                }

                ModelData modelData = new ModelData();
                modelData.getRoot().addChild("arm", armBuilder, ModelTransform.NONE);
                modelData.getRoot().addChild("sleeve", sleeveBuilder, ModelTransform.NONE);

                arr[hand] = TexturedModelData.of(modelData, 64, 64).createModel();
            }
        }
    }

    /**
     * This function contains a log of depth hackery so each draw call has to be done in a specific order
     */
    public void renderFirstPerson(WorldRenderContext context) {
        Camera camera = context.camera();
        Entity camEntity = camera.getFocusedEntity();
        VertexConsumerProvider.Immediate consumers = (VertexConsumerProvider.Immediate) context.consumers();
        MatrixStack matrices = context.matrixStack();
        ClientWorld world = context.world();
        assert consumers != null;

        int light = getLight(camera, world);

        //Render gui
        if (FGM.position != null) {
            matrices.push();
            Vec3d pos = FGM.position.subtract(convert(((RenderPass.World) XR_RENDERER.renderPass).eyePoses.getScaledPhysicalPose().getPos()));
            matrices.translate(pos.x, pos.y, pos.z);
            matrices.multiply(new Quaternion((float) FGM.orientation.x, (float) FGM.orientation.y, (float) FGM.orientation.z, (float) FGM.orientation.w));
            renderGuiQuad(matrices.peek(), consumers);
            matrices.pop();
            consumers.drawCurrentLayer();
        }

        if (camEntity != null) {
            renderShadow(context, camEntity);

            //Render vanilla crosshair ray if controller raytracing is disabled
            if (!FGM.isScreenOpen() && !MCXRCore.getCoreConfig().controllerRaytracing()) {
                Vec3d camPos = context.camera().getPos();
                matrices.push();

                double x = MathHelper.lerp(context.tickDelta(), camEntity.lastRenderX, camEntity.getX());
                double y = MathHelper.lerp(context.tickDelta(), camEntity.lastRenderY, camEntity.getY()) + camEntity.getStandingEyeHeight();
                double z = MathHelper.lerp(context.tickDelta(), camEntity.lastRenderZ, camEntity.getZ());
                matrices.translate(x - camPos.x, y - camPos.y, z - camPos.z);

                matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-camEntity.getYaw() + 180.0F));
                matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90 - camEntity.getPitch()));

                Matrix4f model = matrices.peek().getModel();
                Matrix3f normal = matrices.peek().getNormal();

                VertexConsumer consumer = consumers.getBuffer(LINE_CUSTOM_ALWAYS.apply(4.0));
                consumer.vertex(model, 0, 0, 0).color(0f, 0f, 0f, 1f).normal(normal, 0, -1, 0).next();
                consumer.vertex(model, 0, -5, 0).color(0f, 0f, 0f, 1f).normal(normal, 0, -1, 0).next();

                consumer = consumers.getBuffer(LINE_CUSTOM.apply(2.0));
                consumer.vertex(model, 0, 0, 0).color(1f, 0f, 0f, 1f).normal(normal, 0, -1, 0).next();
                consumer.vertex(model, 0, -5, 0).color(0.7f, 0.7f, 0.7f, 1f).normal(normal, 0, -1, 0).next();

                matrices.pop();
            }
        }

        if (camEntity instanceof ClientPlayerEntity player && FGM.isScreenOpen()) {
            renderHandsAndItems(player, light, matrices, consumers, context.tickDelta());
        }

        for (int hand = 0; hand < 2; hand++) {
            if (!XrInput.handsActionSet.grip.isActive[hand]) {
                continue;
            }

            //Draw the hand ray and debug lines
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

                VertexConsumer consumer = consumers.getBuffer(LINE_CUSTOM_ALWAYS.apply(4.0));
                consumer.vertex(model, 0, 0, 0).color(0f, 0f, 0f, 1f).normal(normal, 0, -1, 0).next();
                consumer.vertex(model, 0, -5, 0).color(0f, 0f, 0f, 1f).normal(normal, 0, -1, 0).next();

                consumer = consumers.getBuffer(LINE_CUSTOM.apply(2.0));
                consumer.vertex(model, 0, 0, 0).color(1f, 0f, 0f, 1f).normal(normal, 0, -1, 0).next();
                consumer.vertex(model, 0, -5, 0).color(0.7f, 0.7f, 0.7f, 1f).normal(normal, 0, -1, 0).next();
            }

            boolean debug = MinecraftClient.getInstance().options.debugEnabled;

            if (debug) {
                FartUtil.renderCrosshair(consumers, context.matrixStack(), 0.05f, false);
            }

            matrices.pop(); //2

            if (debug) {
                matrices.multiply(
                        JOMLUtil.convert(
                                pose.getOrientation()
                        )
                );
                FartUtil.renderCrosshair(consumers, context.matrixStack(), 0.1f, false);
            }

            matrices.pop(); //1
        }

        consumers.draw();

        //Render HUD
        if (!FGM.isScreenOpen() && XrInput.handsActionSet.grip.isActive[0]) {
            matrices.push();

            transformToHand(matrices, 0, context.tickDelta());

            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-90.0F));
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F));

            matrices.translate(-2 / 16f, -12 / 16f, 0);

            matrices.push();
            matrices.translate(2 / 16f, 9 / 16f, -1 / 16f);
            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-75f));
            renderGuiQuad(matrices.peek(), consumers);
            consumers.drawCurrentLayer();
            matrices.pop();

            matrices.pop();
        }
    }

    public static int getLight(Camera camera, World world) {
        return LightmapTextureManager.pack(world.getLightLevel(LightType.BLOCK, camera.getBlockPos()), world.getLightLevel(LightType.SKY, camera.getBlockPos()));
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

    private void renderGuiQuad(MatrixStack.Entry transform, VertexConsumerProvider consumers) {
        Framebuffer guiFramebuffer = FGM.frontFramebuffer;

        float x = FGM.size / 2;
        float y = FGM.size * guiFramebuffer.textureHeight / guiFramebuffer.textureWidth;

        VertexConsumer consumer;
        Matrix4f modelMatrix = transform.getModel();
        Matrix3f normalMatrix = transform.getNormal();

        consumer = consumers.getBuffer(GUI_SHADOW.apply(MCXRPlayClient.INSTANCE.flatGuiManager.texture));
        consumer.vertex(modelMatrix, -x - 0.005f, y - 0.005f, 0).color(255, 255, 255, 255).texture(1, 1).overlay(OverlayTexture.DEFAULT_UV).light(0).normal(normalMatrix, 0, 0, -1).next();
        consumer.vertex(modelMatrix, x - 0.005f, y - 0.005f, 0).color(255, 255, 255, 255).texture(0, 1).overlay(OverlayTexture.DEFAULT_UV).light(0).normal(normalMatrix, 0, 0, -1).next();
        consumer.vertex(modelMatrix, x - 0.005f, 0 - 0.005f, 0).color(255, 255, 255, 255).texture(0, 0).overlay(OverlayTexture.DEFAULT_UV).light(0).normal(normalMatrix, 0, 0, -1).next();
        consumer.vertex(modelMatrix, -x - 0.005f, 0 - 0.005f, 0).color(255, 255, 255, 255).texture(1, 0).overlay(OverlayTexture.DEFAULT_UV).light(0).normal(normalMatrix, 0, 0, -1).next();
        consumer = consumers.getBuffer(GUI_NO_DEPTH_TEST.apply(MCXRPlayClient.INSTANCE.flatGuiManager.texture));
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

    public void renderHandsAndItems(ClientPlayerEntity player, int light, MatrixStack matrices, VertexConsumerProvider consumers, float deltaTick) {
        //Render held items
        for (int hand = 0; hand < 2; hand++) {
            if (!XrInput.handsActionSet.grip.isActive[hand]) {
                continue;
            }

            if (!FGM.isScreenOpen()) {
                ItemStack stack = hand == 0 ? player.getOffHandStack() : player.getMainHandStack();

                if (!stack.isEmpty()) {
                    boolean mainHand = hand == MCXRPlayClient.mainHand;
                    matrices.push();
                    transformToHand(matrices, hand, deltaTick);

                    if (mainHand) {
                        float swing = -0.4f * MathHelper.sin((float) (Math.sqrt(player.getHandSwingProgress(deltaTick)) * Math.PI * 2));
                        matrices.multiply(Vec3f.POSITIVE_X.getRadialQuaternion(swing));
                    }

                    MinecraftClient.getInstance().getHeldItemRenderer().renderItem(
                            player,
                            stack,
                            hand == 0 ? ModelTransformation.Mode.THIRD_PERSON_LEFT_HAND : ModelTransformation.Mode.THIRD_PERSON_RIGHT_HAND,
                            hand == 0,
                            matrices,
                            consumers,
                            light
                    );

                    matrices.pop();
                }
            }

            //Draw hand
            matrices.push();

            transformToHand(matrices, hand, deltaTick);

            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-90.0F));
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F));

            matrices.translate(-2 / 16f, -12 / 16f, 0);

            matrices.push();
            ModelPart armModel;
            if (player.getModel().equals("slim")) {
                armModel = this.slimArmModel[hand];
            } else {
                armModel = this.armModel[hand];
            }

            VertexConsumer consumer = consumers.getBuffer(RenderLayer.getEntityTranslucent(player.getSkinTexture()));
            armModel.render(matrices, consumer, light, OverlayTexture.DEFAULT_UV);
            matrices.pop();

            matrices.pop();

            consumers.getBuffer(RenderLayer.LINES); //Hello I'm a hack ;)
        }
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

        RenderTypeBuilder renderTypeBuilder = new RenderTypeBuilder(MCXRPlayClient.id("gui_no_depth_test"), VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, false, false);
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

    public static final Function<Identifier, RenderLayer> GUI_SHADOW = Util.memoize((texture) -> {
        RenderTypeBuilder renderTypeBuilder = new RenderTypeBuilder(MCXRPlayClient.id("gui_no_depth_test"), VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, false, true);
        renderTypeBuilder.innerBuilder.
                shader(RenderStateShards.shader(GameRenderer::getRenderTypeEntityTranslucentShader))
                .texture(RenderStateShards.texture(texture, false, false))
                .transparency(RenderStateShards.TRANSLUCENT_TRANSPARENCY)
                .cull(RenderStateShards.NO_CULL)
                .lightmap(RenderStateShards.LIGHTMAP)
                .overlay(RenderStateShards.OVERLAY)
                .depthTest(RenderStateShards.depthTest("GL_GREATER", GL11.GL_GREATER));
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
