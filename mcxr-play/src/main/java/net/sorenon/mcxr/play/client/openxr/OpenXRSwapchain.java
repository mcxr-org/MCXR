package net.sorenon.mcxr.play.client.openxr;

import com.mojang.blaze3d.systems.RenderSystem;
import net.sorenon.mcxr.play.client.rendering.XrFramebuffer;
import org.lwjgl.openxr.*;
import org.lwjgl.system.MemoryStack;

import java.io.Closeable;
import java.nio.IntBuffer;

import static org.lwjgl.system.MemoryStack.stackInts;
import static org.lwjgl.system.MemoryStack.stackPush;

public class OpenXRSwapchain implements AutoCloseable {
    public final XrSwapchain handle;
    public final OpenXRInstance instance;
    public final OpenXRSession session;
    public int width;
    public int height;
    public XrSwapchainImageOpenGLKHR.Buffer images;
    public XrFramebuffer framebuffer;

    public OpenXRSwapchain(XrSwapchain handle, OpenXRSession session) {
        this.handle = handle;
        this.session = session;
        this.instance = session.instance;
    }

    public void createImages() {
        try (MemoryStack stack = stackPush()) {
            IntBuffer intBuf = stackInts(0);

            instance.check(XR10.xrEnumerateSwapchainImages(handle, intBuf, null), "xrEnumerateSwapchainImages");

            int imageCount = intBuf.get(0);
            XrSwapchainImageOpenGLKHR.Buffer swapchainImageBuffer = XrSwapchainImageOpenGLKHR.create(imageCount);
            for (XrSwapchainImageOpenGLKHR image : swapchainImageBuffer) {
                image.type(KHROpenglEnable.XR_TYPE_SWAPCHAIN_IMAGE_OPENGL_KHR);
            }

            instance.check(XR10.xrEnumerateSwapchainImages(handle, intBuf, XrSwapchainImageBaseHeader.create(swapchainImageBuffer.address(), swapchainImageBuffer.capacity())), "xrEnumerateSwapchainImages");

            images = swapchainImageBuffer;
            framebuffer = new XrFramebuffer(width, height);
            framebuffer.setClearColor(239 / 255f, 50 / 255f, 61 / 255f, 255 / 255f);
        }
    }

    @Override
    public void close() {
        XR10.xrDestroySwapchain(handle);
        images.close();
        RenderSystem.recordRenderCall(() -> framebuffer.delete());
    }
}
