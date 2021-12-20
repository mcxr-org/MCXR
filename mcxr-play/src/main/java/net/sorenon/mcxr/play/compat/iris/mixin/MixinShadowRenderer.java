package net.sorenon.mcxr.play.compat.iris.mixin;

import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.play.openxr.XrRenderer;
import net.sorenon.mcxr.play.rendering.RenderPass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net/coderbot/iris/pipeline/ShadowRenderer", remap = false)
@Pseudo
public class MixinShadowRenderer {

    @Unique
    private static final XrRenderer XR_RENDERER = MCXRPlayClient.RENDERER;

    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "renderShadows", at = @At(value = "HEAD"), cancellable = true)
    void mcxr$cancelRenderShadows(CallbackInfo ci) {
        if (XR_RENDERER.renderPass != RenderPass.VANILLA && XR_RENDERER.eye != 0) {
            ci.cancel();
        }
    }
}
