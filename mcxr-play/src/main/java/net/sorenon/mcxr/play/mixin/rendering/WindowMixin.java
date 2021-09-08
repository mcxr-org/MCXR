package net.sorenon.mcxr.play.mixin.rendering;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.sorenon.mcxr.play.FlatGuiManager;
import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.play.rendering.MainRenderTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Window.class)
public class WindowMixin {

    @Unique
    private final FlatGuiManager FGM = MCXRPlayClient.INSTANCE.flatGuiManager;

    /**
     * No vysnc >:(
     */
    @ModifyVariable(method = "setVsync", ordinal = 0, at = @At("HEAD"))
    boolean overwriteVsync(boolean v) {
        return false;
    }

    @Inject(method = "getFramebufferWidth", at = @At("HEAD"), cancellable = true)
    void getFramebufferWidth(CallbackInfoReturnable<Integer> cir) {
        if (isCustomFramebuffer()) {
            MainRenderTarget mainRenderTarget = (MainRenderTarget) MinecraftClient.getInstance().getFramebuffer();
            cir.setReturnValue(mainRenderTarget.viewportWidth);
        }
    }

    @Inject(method = "getFramebufferHeight", at = @At("HEAD"), cancellable = true)
    void getFramebufferHeight(CallbackInfoReturnable<Integer> cir) {
        if (isCustomFramebuffer()) {
            MainRenderTarget mainRenderTarget = (MainRenderTarget) MinecraftClient.getInstance().getFramebuffer();
            cir.setReturnValue(mainRenderTarget.viewportHeight);
        }
    }

    @Inject(method = "getWidth", at = @At("HEAD"), cancellable = true)
    void getWidth(CallbackInfoReturnable<Integer> cir) {
        getFramebufferWidth(cir);
    }

    @Inject(method = "getHeight", at = @At("HEAD"), cancellable = true)
    void getHeight(CallbackInfoReturnable<Integer> cir) {
        getFramebufferHeight(cir);
    }


    @Inject(method = "getScaledHeight", at = @At("HEAD"), cancellable = true)
    void getScaledHeight(CallbackInfoReturnable<Integer> cir) {
        if (isCustomFramebuffer()) {
            cir.setReturnValue(FGM.scaledHeight);
        }
    }

    @Inject(method = "getScaledWidth", at = @At("HEAD"), cancellable = true)
    void getScaledWidth(CallbackInfoReturnable<Integer> cir) {
        if (isCustomFramebuffer()) {
            cir.setReturnValue(FGM.scaledWidth);
        }
    }

    @Inject(method = "getScaleFactor", at = @At("HEAD"), cancellable = true)
    void getScaleFactor(CallbackInfoReturnable<Double> cir) {
        if (isCustomFramebuffer()) {
            cir.setReturnValue(FGM.guiScale);
        }
    }

    @Inject(method = "onWindowFocusChanged", at = @At("HEAD"), cancellable = true)
    void preventPauseOnUnFocus(long window, boolean focused, CallbackInfo ci) {
        ci.cancel();
    }

    @Unique
    boolean isCustomFramebuffer() {
        MainRenderTarget mainRenderTarget = (MainRenderTarget) MinecraftClient.getInstance().getFramebuffer();
        return mainRenderTarget != null && mainRenderTarget.isCustomFramebuffer();
    }
}
