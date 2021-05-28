package net.sorenon.minexraft.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.sorenon.minexraft.client.rendering.ExistingTexture;

public class FlatGuiManager {

    public int framebufferWidth = 1080;
    public int framebufferHeight = 1080;

    public final Identifier texture = new Identifier("mcxr", "gui");
    public Framebuffer framebuffer;

    public double guiScale;

    public int scaledWidth;
    public int scaledHeight;

    public Vec3d pos = new Vec3d(30, 90, 10);

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
}
