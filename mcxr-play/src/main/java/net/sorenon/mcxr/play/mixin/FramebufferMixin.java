package net.sorenon.mcxr.play.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import static org.lwjgl.opengl.GL11C.GL_RGBA16;
import static org.lwjgl.opengl.GL11C.GL_RGBA8;

import com.mojang.blaze3d.pipeline.RenderTarget;

@Mixin(RenderTarget.class)
public class FramebufferMixin {

    /**
     * Increasing the bit depth of the diffuse target prevents banding in dark areas
     * ATM this is only really fixable on VR headsets since they offer a higher bit depth
     * than most consumer monitors
     */
    @ModifyConstant(method = "createBuffers", constant = @Constant(intValue = GL_RGBA8))
    int increaseColorBitDepth(int value) {
        return GL_RGBA16;
    }
}
