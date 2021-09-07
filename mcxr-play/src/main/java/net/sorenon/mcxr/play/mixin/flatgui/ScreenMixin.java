package net.sorenon.mcxr.play.mixin.flatgui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.sorenon.mcxr.play.MCXRPlayClient;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class ScreenMixin extends DrawableHelper {

    @Shadow
    public int width;

    @Shadow
    public int height;

    @Shadow
    @Nullable
    protected MinecraftClient client;

    @Inject(method = "renderBackground(Lnet/minecraft/client/util/math/MatrixStack;I)V", at = @At("HEAD"), cancellable = true)
    void cancelBackground(MatrixStack matrices, int vOffset, CallbackInfo ci) {
        if (MCXRPlayClient.RENDERER.isXrMode()) {
            ci.cancel();
        }
    }
}
