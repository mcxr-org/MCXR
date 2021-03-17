package net.sorenon.minexraft;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWNativeGLX;
import org.lwjgl.opengl.GL11;
import org.lwjgl.openxr.*;
import org.lwjgl.system.SharedLibrary;

import java.nio.file.Paths;

import static org.lwjgl.system.APIUtil.apiCreateLibrary;
import static org.lwjgl.system.APIUtil.apiLog;

public class MineXRaftClient implements ClientModInitializer {

    public static final OpenXR OPEN_XR = new OpenXR();

    public static XrRect2Di viewportRect = null;
    public static XrFovf fov = null;
    public static XrPosef eyePose = null;
    public static XrPosef headPose = null;
    public static int viewIndex = 0;

    public static Vec3d xrOrigin = new Vec3d(1, 80, -10);

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
                RenderSystem.defaultBlendFunc();
                RenderSystem.disableTexture();
                RenderSystem.lineWidth(1.0f);

                XrPosef pose = OPEN_XR.inputState.handPose[i];
                XrVector3f pos = pose.position$();
                XrQuaternionf quat = pose.orientation();
                RenderSystem.pushMatrix();
                XrVector3f pos2 = MineXRaftClient.eyePose.position$();
                Vec3d pos1 = new Vec3d(pos.x(), pos.y(), pos.z());
                RenderSystem.translated(pos1.x - pos2.x(), pos1.y - pos2.y(), pos1.z - pos2.z());

                GL11.glPointSize(100.0f);
                BufferBuilder buffer = Tessellator.getInstance().getBuffer();
                buffer.begin(GL11.GL_POINTS, VertexFormats.POSITION_COLOR);
                buffer.vertex(0, 0, 0).color(1f, 0f, 0f, 1f).next();
                Tessellator.getInstance().draw();

                RenderSystem.pushMatrix();
                RenderSystem.multMatrix(new Matrix4f(new Quaternion(quat.x(), quat.y(), quat.z(), quat.w())));
                RenderSystem.scalef(0.01F, 0.01F, 0.01F);
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

            /*
                           RenderSystem.pushMatrix();
               RenderSystem.translatef((float)(this.scaledWidth / 2), (float)(this.scaledHeight / 2), (float)this.getZOffset());
               Camera camera = this.client.gameRenderer.getCamera();
               RenderSystem.rotatef(camera.getPitch(), -1.0F, 0.0F, 0.0F);
               RenderSystem.rotatef(camera.getYaw(), 0.0F, 1.0F, 0.0F);
               RenderSystem.scalef(-1.0F, -1.0F, -1.0F);
               RenderSystem.renderCrosshair(10);
               RenderSystem.popMatrix();
             */
        });
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
