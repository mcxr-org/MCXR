package net.sorenon.minexraft;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.sorenon.minexraft.mixin.accessor.ERDExt;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.openxr.*;
import org.lwjgl.system.SharedLibrary;

import java.nio.file.Paths;

import static org.lwjgl.system.APIUtil.apiCreateLibrary;
import static org.lwjgl.system.APIUtil.apiLog;

public class MineXRaftClient implements ClientModInitializer {

    public static final OpenXR OPEN_XR = new OpenXR();

    public static XrRect2Di viewportRect = null;
    public static int framebufferWidth = 0;
    public static int framebufferHeight = 0;
    public static XrFovf fov = null;
    public static XrPosef eyePose = null;
    public static int viewIndex = 0;

    public static void tmpResetSize(){
        if (viewportRect != null) {
            framebufferWidth = viewportRect.extent().width();
            framebufferHeight = viewportRect.extent().height();
        }
    }

    public static final Pose headPose = new Pose();

    public static Vec3d xrOrigin = new Vec3d(0, 0, 0); //The center of player's playspace set at the same height of the PlayerEntity's feet

    @Override
    public void onInitializeClient() {
//        fov = XrFovf.create();
//        fov.angleLeft(-0.80285144f);
//        fov.angleRight(0.80285144f);
//        fov.angleUp(0.8552113f);
//        fov.angleDown(-0.8552113f);
//        if (true) return;


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
                RenderSystem.defaultBlendFunc();
                RenderSystem.disableTexture();
                RenderSystem.lineWidth(1.0f);

                XrPosef pose = OPEN_XR.inputState.handPose[i];
                XrVector3f pos = pose.position$();
                XrVector2f thumbstick = OPEN_XR.inputState.handThumbstick[i];
                XrQuaternionf quat = pose.orientation();
                RenderSystem.pushMatrix();
                XrVector3f eyePos = MineXRaftClient.eyePose.position$();
                Vec3d gripPos = new Vec3d(pos.x(), pos.y(), pos.z());
                RenderSystem.translated(gripPos.x - eyePos.x(), gripPos.y - eyePos.y(), gripPos.z - eyePos.z());

                RenderSystem.pushMatrix();
                RenderSystem.multMatrix(new Matrix4f(new Quaternion(quat.x(), quat.y(), quat.z(), quat.w())));
                RenderSystem.translated(thumbstick.x() * 0.05f, 0, thumbstick.y() * -0.05f);
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

//        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
//            Entity entity = context.camera().getFocusedEntity();
//
//            if (entity != null) {
//                MatrixStack matrices = context.matrixStack();
//                Vec3d camPos = context.camera().getPos();
//                matrices.push();
//                double x = MathHelper.lerp(context.tickDelta(), entity.lastRenderX, entity.getX());
//                double y = MathHelper.lerp(context.tickDelta(), entity.lastRenderY, entity.getY());
//                double z = MathHelper.lerp(context.tickDelta(), entity.lastRenderZ, entity.getZ());
//                matrices.translate(x - camPos.x, y - camPos.y, z - camPos.z);
//
//                ERDExt.renderShadow(
//                        context.matrixStack(),
//                        context.consumers(),
//                        entity,
//                        1.0f,
//                        context.tickDelta(),
//                        context.world(),
//                        0.1f
//                );
//
//                matrices.pop();
//            }
//        });
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
