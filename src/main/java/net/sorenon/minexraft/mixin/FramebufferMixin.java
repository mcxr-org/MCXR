package net.sorenon.minexraft.mixin;

import net.minecraft.client.gl.Framebuffer;
import net.sorenon.minexraft.accessor.FBAccessor;
import org.lwjgl.opengl.GL30;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL30.*;

@Mixin(Framebuffer.class)
public class FramebufferMixin implements FBAccessor {


    @Shadow private int colorAttachment;

    @Shadow public int fbo;

    @Override
    public void setColorTexture(int colorTexture) {
        this.colorAttachment = colorTexture;
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, colorAttachment, 0);
    }

    @Override
    public int getColorTexture() {
        return this.colorAttachment;
    }
}
