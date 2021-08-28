package net.sorenon.mcxr.play.rendering;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.sorenon.mcxr.play.mixin.accessor.FramebufferAcc;
import org.lwjgl.opengl.GL30;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL30.*;

/*
The framebuffer class is most likely the largest cause of compatibility issues between rendering mods and the game itself.
This is due to Minecraft and these mods expecting the framebuffer to be the same size between frames (excluding after a resize)
they are designed with a constantly sized framebuffer in mind despite the fact that in an XR environment the size of the
framebuffer can change between draws in the same frame

These are the methods that i can think of to deal with this:
1. Redesign each mod to be able to handle multiple sizes of framebuffer
2. Assume all swapchain images are the same size
3. Resize the framebuffer between draws if needed
4. Don't do anything and let the world burn
5. Allocate each framebuffer according to the largest swapchain image and then just change
the size of the viewport rather than the size of the framebuffer

And my thoughts on each:
1. God no
2. Easiest solution and works well with iris
3. Also an easy solution and only effects a small demographic of players
4. Eh maybe
5. Difficult but could work

Method 3 has been implemented
If method 3 has too great of a performance issue (which i doubt) i will look at method 5
 */

/**
 * XrFramebuffer is a framebuffer which accepts a color texture for rendering to rather than creating its own
 * TODO accept depth textures as well
 */
public class XrFramebuffer extends SimpleFramebuffer {

    public XrFramebuffer(int width, int height) {
        super(width, height, true, MinecraftClient.IS_SYSTEM_MAC);
    }

    @Override
    public void initFbo(int width, int height, boolean getError) {
        RenderSystem.assertThread(RenderSystem::isOnRenderThreadOrInit);
        int i = RenderSystem.maxSupportedTextureSize();
        if (width > 0 && width <= i && height > 0 && height <= i) {
            this.viewportWidth = width;
            this.viewportHeight = height;
            this.textureWidth = width;
            this.textureHeight = height;
            this.fbo = GlStateManager.glGenFramebuffers();
//            this.colorAttachment = TextureUtil.generateTextureId();
            if (this.useDepthAttachment) {
                this.depthAttachment = TextureUtil.generateTextureId();
                GlStateManager._bindTexture(this.depthAttachment);
                GlStateManager._texParameter(3553, 10241, 9728);
                GlStateManager._texParameter(3553, 10240, 9728);
                GlStateManager._texParameter(3553, 34892, 0);
                GlStateManager._texParameter(3553, 10242, 33071);
                GlStateManager._texParameter(3553, 10243, 33071);
                GlStateManager._texImage2D(3553, 0, 6402, this.textureWidth, this.textureHeight, 0, 6402, 5126, (IntBuffer)null);
            }

            this.setTexFilter(9728);
//            GlStateManager._bindTexture(this.colorAttachment);
//            GlStateManager._texParameter(3553, 10242, 33071);
//            GlStateManager._texParameter(3553, 10243, 33071);
//            GlStateManager._texImage2D(3553, 0, 32856, this.textureWidth, this.textureHeight, 0, 6408, 5121, (IntBuffer)null);
            GlStateManager._glBindFramebuffer(36160, this.fbo);
//            GlStateManager._glFramebufferTexture2D(36160, 36064, 3553, this.colorAttachment, 0);
            if (this.useDepthAttachment) {
                GlStateManager._glFramebufferTexture2D(36160, 36096, 3553, this.depthAttachment, 0);
            }

            this.checkFramebufferStatus();
            this.clear(getError);
            this.endRead();
        } else {
            throw new IllegalArgumentException("Window " + width + "x" + height + " size out of bounds (max. size: " + i + ")");
        }
    }

    public void setColorAttachment(int colorAttachment) {
        ((FramebufferAcc) this).colorAttachment(colorAttachment);
        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);
        GlStateManager._glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, colorAttachment, 0);
    }
}
