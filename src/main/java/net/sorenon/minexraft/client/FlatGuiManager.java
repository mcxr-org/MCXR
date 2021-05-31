package net.sorenon.minexraft.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.sorenon.minexraft.client.rendering.ExistingTexture;
import org.jetbrains.annotations.Nullable;
import org.joml.Intersectiond;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public class FlatGuiManager {

    public int framebufferWidth = 1080;
    public int framebufferHeight = 1080;

    public final Identifier texture = new Identifier("mcxr", "gui");
    public Framebuffer framebuffer;

    public double guiScale;

    public int scaledWidth;
    public int scaledHeight;

    public Vec3d pos = null;
    public Vec3d mousePos = Vec3d.ZERO;

    public void init() {
        guiScale = calcGuiScale();

        int widthFloor = (int) (framebufferWidth / guiScale);
        scaledWidth = framebufferWidth / guiScale > widthFloor ? widthFloor + 1 : widthFloor;

        int heightFloor = (int) (framebufferHeight / guiScale);
        scaledHeight = framebufferHeight / guiScale > heightFloor ? heightFloor + 1 : heightFloor;

        framebuffer = new SimpleFramebuffer(framebufferWidth, framebufferHeight, true, MinecraftClient.IS_SYSTEM_MAC);
        framebuffer.setClearColor(0, 0, 0, 0);
        MinecraftClient.getInstance().getTextureManager().registerTexture(texture, new ExistingTexture(framebuffer.getColorAttachment()));
    }

    public double calcGuiScale() {
        int guiScale = -1;
        boolean forceUnicodeFont = MinecraftClient.getInstance().forcesUnicodeFont();

        int scale;
        scale = 1;
        while (scale != guiScale && scale < framebufferWidth && scale < framebufferHeight && framebufferWidth / (scale + 1) >= 320 && framebufferHeight / (scale + 1) >= 240) {
            ++scale;
        }

        if (forceUnicodeFont && scale % 2 != 0) {
            ++scale;
        }
        return scale;
    }

    public boolean isScreenOpen() {
        return pos != null && MinecraftClient.getInstance().currentScreen != null;
    }

    @Nullable
    public Vec3d rayIntersectPlane(Vec3d rayPos, Vec3d rayDir) {
        if (pos == null) {
            return null;
        }
        double distance = Intersectiond.intersectRayPlane(
                joml(rayPos),
                joml(rayDir),
                joml(pos),
                joml(new Vec3d(0, 0, -1)),
                0.1f
        );
        if (distance >= 0) {
            return rayDir.multiply(distance).add(rayPos);
        }
        return null;
    }

    private Vector3dc joml(Vec3d vec) {
        return new Vector3d(vec.x, vec.y, vec.z);
    }
}
