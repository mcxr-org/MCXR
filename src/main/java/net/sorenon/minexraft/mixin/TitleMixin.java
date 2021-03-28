package net.sorenon.minexraft.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.SaveLevelScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TitleScreen.class)
public class TitleMixin {
//    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/TitleScreen;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V"))
//    void saf(MatrixStack matrices, int x1, int y1, int x2, int y2, int color){
//
//    }

    //Just for debugging
    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    void click(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir){
        MinecraftClient.getInstance().method_29970(new SaveLevelScreen(new TranslatableText("selectWorld.data_read")));
        MinecraftClient.getInstance().startIntegratedServer("New World");
        cir.setReturnValue(false);
    }
}
