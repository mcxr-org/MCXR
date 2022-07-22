package net.sorenon.mcxr.play.mixin.rendering;

import net.minecraft.client.renderer.LevelRenderer;
import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.play.openxr.MCXRGameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Unique
    private MCXRGameRenderer mcxrGameRenderer = MCXRPlayClient.MCXR_GAME_RENDERER;

    @Inject(method = "graphicsChanged", at = @At("HEAD"))
    void ongraphicsChanged(CallbackInfo ci) {
        mcxrGameRenderer.reloadingDepth += 1;
    }

    @Inject(method = "graphicsChanged", at = @At("RETURN"))
    void aftergraphicsChanged(CallbackInfo ci) {
        mcxrGameRenderer.reloadingDepth -= 1;
    }
}
