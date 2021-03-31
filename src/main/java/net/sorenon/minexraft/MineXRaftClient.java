package net.sorenon.minexraft;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.openxr.XR;
import org.lwjgl.openxr.XrFovf;
import org.lwjgl.openxr.XrRect2Di;
import org.lwjgl.openxr.XrVector2f;
import org.lwjgl.system.SharedLibrary;

import java.nio.file.Paths;

import static org.lwjgl.system.APIUtil.apiCreateLibrary;
import static org.lwjgl.system.APIUtil.apiLog;

public class MineXRaftClient implements ClientModInitializer {

    public static final OpenXR OPEN_XR = new OpenXR();

    public static XrRect2Di viewportRect = null;
    public static Framebuffer primaryRenderTarget = null;
    public static XrFovf fov = null;
    public static Pose eyePose = new Pose();
    public static int viewIndex = 0;

    public static Framebuffer guiFramebuffer = null;

    public static void tmpResetSize() {
//        if (viewportRect != null) {
//            framebufferWidth = viewportRect.extent().width();
//            framebufferHeight = viewportRect.extent().height();
//        }
        primaryRenderTarget = MinecraftClient.getInstance().getFramebuffer();
    }

    public static final Pose headPose = new Pose();

    //    public static Vec3d xrOrigin = new Vec3d(0, 0, 0); //The center of the STAGE set at the same height of the PlayerEntity's feet
    public static Vector3f xrOffset = new Vector3f(0, 0, 0);
    public static float yawTurn = 0;

    @Override
    public void onInitializeClient() {
        SharedLibrary defaultOpenXRLoader = loadNative(XR.class, "C:\\Users\\soren\\Documents\\Programming\\openxr_loader_windows\\x64\\bin\\openxr_loader.dll", "openxr_loader");
        XR.create(defaultOpenXRLoader);
        OPEN_XR.createOpenXRInstance();
        OPEN_XR.initializeOpenXRSystem();

        System.out.println("Hello Fabric world!");
///execute in minecraft:overworld run tp @s 5804.48 137.00 -4601.16 3.23 72.30
        WorldRenderEvents.LAST.register(context -> {
            for (int i = 0; i < 2; i++) {
                if (!OPEN_XR.inputState.renderHand[i]) {
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

                XrVector2f thumbstick = OPEN_XR.inputState.handThumbstick[i];
                RenderSystem.pushMatrix();
                Pose pose = OPEN_XR.inputState.poses[i];
                Vec3d gripPos = pose.getPosMc();
                Vector3f eyePos = MineXRaftClient.eyePose.getPos();
                RenderSystem.translated(gripPos.x - eyePos.x(), gripPos.y - eyePos.y(), gripPos.z - eyePos.z());

                RenderSystem.pushMatrix();
                RenderSystem.multMatrix(new Matrix4f(pose.getOrientationMc()));
                RenderSystem.translated(thumbstick.x() * 0.05f, 0, thumbstick.y() * -0.05f);

                renderHandGui();

                RenderSystem.scalef(0.01F, 0.01F, 0.01F);

                GL11.glPointSize(100.0f);
                BufferBuilder buffer = Tessellator.getInstance().getBuffer();
                buffer.begin(GL11.GL_POINTS, VertexFormats.POSITION_COLOR);
                buffer.vertex(0, 0, 0).color(1f, 0f, 0f, 1f).next();
                Tessellator.getInstance().draw();

                RenderSystem.renderCrosshair(10);
                RenderSystem.popMatrix();

                RenderSystem.popMatrix();
            }

            RenderSystem.pushMatrix();
            RenderSystem.translated(-context.camera().getPos().x, -context.camera().getPos().y, -context.camera().getPos().z);

            GL11.glPointSize(4.0f);
            BufferBuilder buffer = Tessellator.getInstance().getBuffer();
            buffer.begin(GL11.GL_POINTS, VertexFormats.POSITION_COLOR);
            buffer.vertex(0, 0, Math.sin(GLFW.glfwGetTime() / 3)).color(1f, 0f, 0f, 1f).next();
            Tessellator.getInstance().draw();

            RenderSystem.popMatrix();

            RenderSystem.depthMask(true);
            RenderSystem.disableBlend();
            RenderSystem.enableCull();
            RenderSystem.enableTexture();
        });

        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
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
                    if (!OPEN_XR.inputState.renderHand[i]) {
                        continue;
                    }
                    Pose pose = OPEN_XR.inputState.poses[i];
                    Vec3d gripPos = pose.getPosMc();
                    Vector3f eyePos = MineXRaftClient.eyePose.getPos();
                    MatrixStack matrices = context.matrixStack();
                    matrices.push();
                    matrices.translate(gripPos.x - eyePos.x(), gripPos.y - eyePos.y(), gripPos.z - eyePos.z());
                    matrices.multiply(pose.getOrientationMc());

                    matrices.multiply(net.minecraft.client.util.math.Vector3f.POSITIVE_X.getDegreesQuaternion(-90.0F - 12));
                    matrices.multiply(net.minecraft.client.util.math.Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
                    matrices.scale(0.4f, 0.4f, 0.4f);
                    matrices.translate(0, 1 / 16f, -1.25 / 16f);

                    LivingEntity camLivEntity = (LivingEntity) camEntity;
                    ItemStack stack = i == 0 ? camLivEntity.getOffHandStack() : camLivEntity.getMainHandStack();

                    MinecraftClient.getInstance().getHeldItemRenderer().renderItem(
                            camLivEntity,
                            stack,
                            i == 0 ? ModelTransformation.Mode.THIRD_PERSON_LEFT_HAND : ModelTransformation.Mode.THIRD_PERSON_RIGHT_HAND,
                            false,
                            matrices,
                            context.consumers(),
                            LightmapTextureManager.pack(camEntity.world.getLightLevel(LightType.BLOCK, camEntity.getBlockPos()), camEntity.world.getLightLevel(LightType.SKY, camEntity.getBlockPos()))
                    );

                    matrices.pop();
                }
            }
        });
    }

    public static void resetView() {
        MineXRaftClient.xrOffset = new Vector3f(0, 0, 0).sub(MineXRaftClient.headPose.getPos()).mul(1, 0, 1);
    }

    private static void renderHandGui() {
        float handGuiScale = 1f / 4000;

//        float x = -0.1f;
//        float y0 = 0.1f;
//        float y1 = y0 - guiFramebuffer.textureHeight * handGuiScale;
//        float z0 = -0.1f;
//        float z1 = z0 + guiFramebuffer.textureWidth * handGuiScale;
//
//        int u0 = 0;
//        int u1 = 1;
//        int v0 = 0;
//        int v1 = 1;
//
//        RenderSystem.enableTexture();
//        RenderSystem.bindTexture(guiFramebuffer.getColorAttachment());
//        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
//        bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE);
//        bufferBuilder.vertex(x, y1, z1).texture(u0, v1).next();
//        bufferBuilder.vertex(x, y1, z0).texture(u1, v1).next();
//        bufferBuilder.vertex(x, y0, z0).texture(u1, v0).next();
//        bufferBuilder.vertex(x, y0, z1).texture(u0, v0).next();
//        Tessellator.getInstance().draw();
//        RenderSystem.disableTexture();

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

    private static SharedLibrary loadNative(Class<?> context, String path, String libName) {
        apiLog("Loading library: " + path);

        // METHOD 1: absolute path
        if (Paths.get(path).isAbsolute()) {
            SharedLibrary lib = apiCreateLibrary(path);
            apiLog("\tSuccess");
            return lib;
        }

        throw new UnsatisfiedLinkError("Failed to locate library: " + libName);
    }
}
