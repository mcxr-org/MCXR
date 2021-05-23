package net.sorenon.minexraft.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;

public class FlatGuiManager {

    public int framebufferWidth = 1920;
    public int framebufferHeight = 1080;

    public double guiScale;

    public int scaledWidth;
    public int scaledHeight;

    public Framebuffer framebuffer;

    public void init() {
        guiScale = calcGuiScale();

        int widthFloor = (int) (framebufferWidth / guiScale);
        scaledWidth = framebufferWidth / guiScale > widthFloor ? widthFloor + 1 : widthFloor;

        int heightFloor = (int) (framebufferHeight / guiScale);
        scaledHeight = framebufferHeight / guiScale > heightFloor ? heightFloor + 1 : heightFloor;

        framebuffer = new Framebuffer(framebufferWidth, framebufferHeight, true, MinecraftClient.IS_SYSTEM_MAC);
        framebuffer.setClearColor(0, 0, 0, 0);
    }

    public double calcGuiScale() {
        int guiScale = 0;
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
