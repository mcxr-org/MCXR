package net.sorenon.mcxr.play.mixin.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.sorenon.mcxr.play.MCXRPlayClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class ScreenMixin extends GuiComponent {

    @Shadow
    public int width;

    @Shadow
    public int height;

    @Inject(method = "renderBackground(Lcom/mojang/blaze3d/vertex/PoseStack;I)V", at = @At("HEAD"), cancellable = true)
    void cancelBackground(PoseStack matrices, int vOffset, CallbackInfo ci) {
        if (MCXRPlayClient.MCXR_GAME_RENDERER.isXrMode()) {
            ci.cancel();
        }
    }
}
