package net.sorenon.mcxr.play.client.mixin.rendering;

import net.minecraft.client.gl.WindowFramebuffer;
import net.sorenon.mcxr.play.client.rendering.MainRenderTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WindowFramebuffer.class)
public class WindowFramebufferMixin {

    @Inject(method = "initSize", at = @At("HEAD"), cancellable = true)
    void cancel(int width, int height, CallbackInfo ci){
        if ((Object)this instanceof MainRenderTarget) {
            ci.cancel();
        }
    }
}
