package net.sorenon.mcxr.play.client.openxr;

import net.sorenon.mcxr.play.client.rendering.XrFramebuffer;
import org.lwjgl.openxr.XrSwapchain;
import org.lwjgl.openxr.XrSwapchainImageOpenGLKHR;

public class Swapchain {
    public XrSwapchain handle;
    public int width;
    public int height;
    public XrSwapchainImageOpenGLKHR.Buffer images;
    public XrFramebuffer framebuffer;
}
