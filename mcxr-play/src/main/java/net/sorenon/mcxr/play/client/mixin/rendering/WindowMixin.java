package net.sorenon.mcxr.play.client.mixin.rendering;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.WindowEventHandler;
import net.minecraft.client.WindowSettings;
import net.minecraft.client.gui.screen.SplashScreen;
import net.minecraft.client.util.MonitorTracker;
import net.minecraft.client.util.Window;
import net.sorenon.mcxr.play.client.FlatGuiManager;
import net.sorenon.mcxr.play.client.MCXRPlayClient;
import net.sorenon.mcxr.play.client.rendering.MainRenderTarget;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static org.lwjgl.system.MemoryStack.stackPointers;

@Mixin(Window.class)
public class WindowMixin {

    @Shadow
    @Final
    private long handle;

    @Unique
    private final FlatGuiManager FGM = MCXRPlayClient.INSTANCE.flatGuiManager;

    /**
     * Disables any sort of VSync or double buffering since we don't want the refresh rate of the monitor affecting the draw cycle
     * Maybe there is a way to switch buffers on a different thread to the XR rendering?
     */
//    @Redirect(at = @At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwCreateWindow(IILjava/lang/CharSequence;JJ)J"), method = "<init>")
//    private long onGlfwCreateWindow(int width, int height, CharSequence title, long monitor, long share) {
//        GLFW.glfwWindowHint(GLFW.GLFW_DOUBLEBUFFER, GLFW.GLFW_FALSE);
//        return GLFW.glfwCreateWindow(width, height, title, monitor, share);
//    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void postInit(WindowEventHandler eventHandler, MonitorTracker monitorTracker, WindowSettings settings, String videoMode, String title, CallbackInfo ci) {
        MCXRPlayClient.OPEN_XR.bindToOpenGLAndCreateSession(handle);
    }

    @Inject(method = "getFramebufferWidth", at = @At("HEAD"), cancellable = true)
    void getFramebufferWidth(CallbackInfoReturnable<Integer> cir) {
        if (MCXRPlayClient.isXrMode() && MinecraftClient.getInstance().getOverlay() instanceof SplashScreen) {
            cir.setReturnValue(MCXRPlayClient.OPEN_XR.swapchains[0].width);
            return;
        }

        if (isCustomFramebuffer()) {
            MainRenderTarget mainRenderTarget = (MainRenderTarget) MinecraftClient.getInstance().getFramebuffer();
            cir.setReturnValue(mainRenderTarget.viewportWidth);
        }
    }

    @Inject(method = "getFramebufferHeight", at = @At("HEAD"), cancellable = true)
    void getFramebufferHeight(CallbackInfoReturnable<Integer> cir) {
        if (MCXRPlayClient.isXrMode() && MinecraftClient.getInstance().getOverlay() instanceof SplashScreen) {
            cir.setReturnValue(MCXRPlayClient.OPEN_XR.swapchains[0].height);
            return;
        }

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
