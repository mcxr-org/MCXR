package net.sorenon.mcxr.play.mixin;

import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.util.math.MatrixStack;
import net.sorenon.mcxr.play.MCXRPlayClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SplashOverlay.class)
public class SplashOverlayMixin {

    @Shadow @Final private boolean reloading;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setOverlay(Lnet/minecraft/client/gui/screen/Overlay;)V"))
    void finishedLoadingResources(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!reloading) {
            //Only initialize once all resources have been loaded to make localized action translations work
            MCXRPlayClient.resourcesInitialized = true;
            MCXRPlayClient.OPEN_XR.tryInitialize();
        }
    }
}
