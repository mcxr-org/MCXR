package net.sorenon.mcxr.play.mixin.rendering;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.sorenon.mcxr.play.MCXRGuiManager;
import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.play.rendering.MCXRMainTarget;
import org.lwjgl.glfw.GLFW;
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
    private final MCXRGuiManager FGM = MCXRPlayClient.INSTANCE.MCXRGuiManager;

    @ModifyVariable(method = "updateVsync", ordinal = 0, at = @At("HEAD"))
    boolean overwriteVsync(boolean v) {
        GLFW.glfwSwapInterval(0);
        return false;
    }

    @Inject(method = "getScreenWidth", at = @At("HEAD"), cancellable = true)
    void getFramebufferWidth(CallbackInfoReturnable<Integer> cir) {
        if (isCustomFramebuffer()) {
            MCXRMainTarget MCXRMainTarget = (MCXRMainTarget) Minecraft.getInstance().getMainRenderTarget();
            cir.setReturnValue(MCXRMainTarget.viewWidth);
        }
    }

    @Inject(method = "getScreenHeight", at = @At("HEAD"), cancellable = true)
    void getFramebufferHeight(CallbackInfoReturnable<Integer> cir) {
        if (isCustomFramebuffer()) {
            MCXRMainTarget MCXRMainTarget = (MCXRMainTarget) Minecraft.getInstance().getMainRenderTarget();
            cir.setReturnValue(MCXRMainTarget.viewHeight);
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


    @Inject(method = "getGuiScaledHeight", at = @At("HEAD"), cancellable = true)
    void getScaledHeight(CallbackInfoReturnable<Integer> cir) {
        if (isCustomFramebuffer()) {
            cir.setReturnValue(FGM.scaledHeight);
        }
    }

    @Inject(method = "getGuiScaledWidth", at = @At("HEAD"), cancellable = true)
    void getScaledWidth(CallbackInfoReturnable<Integer> cir) {
        if (isCustomFramebuffer()) {
            cir.setReturnValue(FGM.scaledWidth);
        }
    }

    @Inject(method = "getGuiScale", at = @At("HEAD"), cancellable = true)
    void getScaleFactor(CallbackInfoReturnable<Double> cir) {
        if (isCustomFramebuffer()) {
            cir.setReturnValue(FGM.guiScale);
        }
    }

    @Inject(method = "onFocus", at = @At("HEAD"), cancellable = true)
    void preventPauseOnUnFocus(long window, boolean focused, CallbackInfo ci) {
        ci.cancel();
    }

    @Unique
    boolean isCustomFramebuffer() {
        MCXRMainTarget MCXRMainTarget = (MCXRMainTarget) Minecraft.getInstance().getMainRenderTarget();
        return MCXRMainTarget != null && MCXRMainTarget.isCustomFramebuffer();
    }
}
