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

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    void click(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir){
        MinecraftClient.getInstance().method_29970(new SaveLevelScreen(new TranslatableText("selectWorld.data_read")));
        MinecraftClient.getInstance().startIntegratedServer("New World");
        cir.setReturnValue(false);
    }
}
