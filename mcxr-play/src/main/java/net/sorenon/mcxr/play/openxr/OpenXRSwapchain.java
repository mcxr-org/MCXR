package net.sorenon.mcxr.play.openxr;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.sorenon.mcxr.play.rendering.XrFramebuffer;
import org.lwjgl.openxr.*;
import org.lwjgl.system.MemoryStack;

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
    public XrFramebuffer innerFramebuffer;
    public SimpleFramebuffer framebuffer;

    public OpenXRSwapchain(XrSwapchain handle, OpenXRSession session) {
        this.handle = handle;
        this.session = session;
        this.instance = session.instance;
    }

    public void createImages() {
        try (MemoryStack ignored = stackPush()) {
            IntBuffer intBuf = stackInts(0);

            instance.check(XR10.xrEnumerateSwapchainImages(handle, intBuf, null), "xrEnumerateSwapchainImages");

            int imageCount = intBuf.get(0);
            XrSwapchainImageOpenGLKHR.Buffer swapchainImageBuffer = XrSwapchainImageOpenGLKHR.create(imageCount);
            for (XrSwapchainImageOpenGLKHR image : swapchainImageBuffer) {
                image.type(KHROpenglEnable.XR_TYPE_SWAPCHAIN_IMAGE_OPENGL_KHR);
            }

            instance.check(XR10.xrEnumerateSwapchainImages(handle, intBuf, XrSwapchainImageBaseHeader.create(swapchainImageBuffer.address(), swapchainImageBuffer.capacity())), "xrEnumerateSwapchainImages");

            images = swapchainImageBuffer;
            innerFramebuffer = new XrFramebuffer(width, height);
            innerFramebuffer.setClearColor(sRGBToLinear(239 / 255f), sRGBToLinear(50 / 255f), sRGBToLinear(61 / 255f), 255 / 255f);

            framebuffer = new SimpleFramebuffer(width, height, true, MinecraftClient.IS_SYSTEM_MAC);
            framebuffer.setClearColor(239 / 255f, 50 / 255f, 61 / 255f, 255 / 255f);
        }
    }

    float sRGBToLinear(float f) {
        if (f < 0.04045f) {
            return f / 12.92f;
        } else {
            return (float) Math.pow((f + 0.055f) / 1.055f, 2.4f);
        }
    }

    @Override
    public void close() {
        XR10.xrDestroySwapchain(handle);
        if (images != null) {
            images.close();
        }
        if (framebuffer != null) {
            RenderSystem.recordRenderCall(() -> {
                innerFramebuffer.delete();
                framebuffer.delete();
            });
        }
    }
}
