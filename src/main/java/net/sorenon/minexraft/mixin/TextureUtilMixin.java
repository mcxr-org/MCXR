package net.sorenon.minexraft.mixin;

import net.minecraft.client.texture.TextureUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.nio.IntBuffer;

@Mixin(TextureUtil.class)
public class TextureUtilMixin {

//    @Redirect(method = "allocate(Lnet/minecraft/client/texture/NativeImage$GLFormat;IIII)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;texImage2D(IIIIIIIILjava/nio/IntBuffer;)V"))
//    static void textImage(int target, int level, int internalFormat, int width, int height, int border, int format, int type, IntBuffer pixels){
//        if (internalFormat) {
//
//        }
//    }
}
