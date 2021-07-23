package net.sorenon.mcxr.play.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.GraphicsMode;
import net.minecraft.client.tutorial.TutorialStep;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameOptions.class)
public class GameOptionsMixin {

    @Shadow public TutorialStep tutorialStep;

    @Shadow public GraphicsMode graphicsMode;

    @Shadow protected MinecraftClient client;

    @Inject(method = "accept", at = @At("HEAD"))
    void editOptions(CallbackInfo ci){
        tutorialStep = TutorialStep.NONE;
        if (graphicsMode == GraphicsMode.FABULOUS) {
            graphicsMode = GraphicsMode.FAST;
            MinecraftClient.getInstance().worldRenderer.reload();
        }
    }
}
