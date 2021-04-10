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

    public int GetColorAttachment() {
        return ((FramebufferExt) this).colorAttachment();
    }

    public int GetDepthAttachment(){
        return ((FramebufferExt) this).depthAttachment();
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
