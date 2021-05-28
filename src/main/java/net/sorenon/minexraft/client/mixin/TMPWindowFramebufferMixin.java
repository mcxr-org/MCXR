package net.sorenon.minexraft.client.mixin;

import net.minecraft.client.gl.WindowFramebuffer;
import net.sorenon.minexraft.client.rendering.MainRenderTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WindowFramebuffer.class)
public class TMPWindowFramebufferMixin {

    @Inject(method = "initSize", at = @At("HEAD"), cancellable = true)
    void cancel(int width, int height, CallbackInfo ci){
        if ((Object)this instanceof MainRenderTarget) {
            ci.cancel();
        }
    }
}
