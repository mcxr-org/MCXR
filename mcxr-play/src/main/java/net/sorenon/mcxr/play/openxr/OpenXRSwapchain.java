package net.sorenon.mcxr.play.openxr;

import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.sorenon.mcxr.play.rendering.XrFramebuffer;
import org.lwjgl.openxr.*;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL43.glTextureView;
import static org.lwjgl.system.MemoryStack.stackInts;
import static org.lwjgl.system.MemoryStack.stackPush;

public class OpenXRSwapchain implements AutoCloseable {
    public final XrSwapchain handle;
    public final OpenXRInstance instance;
    public final OpenXRSession session;

    public final int width;
    public final int height;
    public final int format;

    public final int[] arrayImages;
    public final int[] leftImages;
    public final int[] rightImages;

    public XrFramebuffer innerFramebuffer;
    public TextureTarget framebuffer;

    //TODO make two swapchains path for GL4ES compat

    public OpenXRSwapchain(XrSwapchain handle, OpenXRSession session, int format, int width, int height) {
        this.handle = handle;
        this.session = session;
        this.instance = session.instance;
        this.format = format;
        this.width = width;
        this.height = height;

        try (MemoryStack stack = stackPush()) {
            IntBuffer intBuf = stackInts(0);

            instance.check(XR10.xrEnumerateSwapchainImages(handle, intBuf, null), "xrEnumerateSwapchainImages");

            int imageCount = intBuf.get(0);
            XrSwapchainImageOpenGLKHR.Buffer swapchainImageBuffer = XrSwapchainImageOpenGLKHR.calloc(imageCount, stack);
            for (XrSwapchainImageOpenGLKHR image : swapchainImageBuffer) {
                image.type(KHROpenglEnable.XR_TYPE_SWAPCHAIN_IMAGE_OPENGL_KHR);
            }

            instance.check(XR10.xrEnumerateSwapchainImages(handle, intBuf, XrSwapchainImageBaseHeader.create(swapchainImageBuffer.address(), swapchainImageBuffer.capacity())), "xrEnumerateSwapchainImages");

            this.arrayImages = new int[imageCount];
            this.leftImages = new int[imageCount];
            this.rightImages = new int[imageCount];

            for (int i = 0; i < imageCount; i++) {
                var openxrImage = swapchainImageBuffer.get(i);
                arrayImages[i] = openxrImage.image();
                int[] textures = new int[2];
                glGenTextures(textures);

                glTextureView(textures[0], GL_TEXTURE_2D, arrayImages[i], this.format, 0, 1, 0, 1);
                glTextureView(textures[1], GL_TEXTURE_2D, arrayImages[i], this.format, 0, 1, 1, 1);
                leftImages[i] = textures[0];
                rightImages[i] = textures[1];
            }

            innerFramebuffer = new XrFramebuffer(width, height);
            innerFramebuffer.setClearColor(sRGBToLinear(239 / 255f), sRGBToLinear(50 / 255f), sRGBToLinear(61 / 255f), 255 / 255f);

            framebuffer = new TextureTarget(width, height, true, Minecraft.ON_OSX);
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
        if (framebuffer != null) {
            RenderSystem.recordRenderCall(() -> {
                innerFramebuffer.destroyBuffers();
                framebuffer.destroyBuffers();
            });
        }
    }
}
