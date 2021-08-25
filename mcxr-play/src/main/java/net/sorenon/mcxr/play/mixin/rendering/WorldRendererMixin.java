package net.sorenon.mcxr.play.mixin.rendering;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.sorenon.mcxr.play.rendering.MainRenderTarget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Shadow @Final private MinecraftClient client;

    @Inject(method = "reload()V", at = @At("RETURN"))
    void onReload(CallbackInfo ci) {
        MainRenderTarget mainRenderTarget = (MainRenderTarget) client.getFramebuffer();
        mainRenderTarget.gameWidth = client.getFramebuffer().textureWidth;
        mainRenderTarget.gameHeight = client.getFramebuffer().textureHeight;
    }
}
