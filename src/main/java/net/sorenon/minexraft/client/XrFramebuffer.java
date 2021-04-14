package net.sorenon.minexraft.client;

import com.mojang.blaze3d.platform.FramebufferInfo;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.texture.TextureUtil;
import net.sorenon.minexraft.client.mixin.accessor.FramebufferExt;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL30.*;

/*
The framebuffer class is most likely the largest cause of compatibility issues between rendering mods;
Since Minecraft and these mods expect the framebuffer to be the same size between frames (excluding after a resize)
they are designed with a constantly sized framebuffer in mind despite the fact that in an XR environment
the size of the framebuffer can change between draws in the same frame

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
5. Very contrived but could work

For the first release I will use method 2 then implement method 3 later
If method 3 has too great of a performance issue (which i doubt) i will look at method 5
 */
public class XrFramebuffer extends Framebuffer {

    public XrFramebuffer(int width, int height) {
        super(width, height, true, MinecraftClient.IS_SYSTEM_MAC);
    }

    @Override
    public void initFbo(int width, int height, boolean getError) {
        RenderSystem.assertThread(RenderSystem::isOnRenderThreadOrInit);
        this.viewportWidth = width;
        this.viewportHeight = height;
        this.textureWidth = width;
        this.textureHeight = height;
        this.fbo = GlStateManager.genFramebuffers();
//        this.colorAttachment = TextureUtil.generateId();
        if (this.useDepthAttachment) {
            int depthAttachment = TextureUtil.generateId();
            ((FramebufferExt) this).depthAttachment(depthAttachment);
            GlStateManager.bindTexture(depthAttachment);
            GlStateManager.texParameter(3553, 10241, 9728);
            GlStateManager.texParameter(3553, 10240, 9728);
            GlStateManager.texParameter(3553, 10242, 10496);
            GlStateManager.texParameter(3553, 10243, 10496);
            GlStateManager.texParameter(3553, 34892, 0);
            GlStateManager.texImage2D(3553, 0, 6402, this.textureWidth, this.textureHeight, 0, 6402, 5126, null);
        }

//        this.setTexFilter(9728);
//        GlStateManager.bindTexture(this.colorAttachment);
//        GlStateManager.texImage2D(3553, 0, 32856, this.textureWidth, this.textureHeight, 0, 6408, 5121, (IntBuffer)null);
        GlStateManager.bindFramebuffer(FramebufferInfo.FRAME_BUFFER, this.fbo);
//        GlStateManager.framebufferTexture2D(FramebufferInfo.FRAME_BUFFER, FramebufferInfo.COLOR_ATTACHMENT, 3553, this.colorAttachment, 0);
        if (this.useDepthAttachment) {
            GlStateManager.framebufferTexture2D(FramebufferInfo.FRAME_BUFFER, FramebufferInfo.DEPTH_ATTACHMENT, 3553, this.getDepthAttachment(), 0);
        }

        this.checkFramebufferStatus();
        this.clear(getError);
        this.endRead();
    }

    public void SetColorAttachment(int colorAttachment) {
        ((FramebufferExt) this).colorAttachment(colorAttachment);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, colorAttachment, 0);
    }

    @Override
    public void beginWrite(boolean setViewport) {
        super.beginWrite(setViewport);
    }
}
