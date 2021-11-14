package net.sorenon.mcxr.play.mixin.flatgui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.sorenon.mcxr.play.FlatGuiManager;
import net.sorenon.mcxr.play.MCXRPlayClient;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow @Nullable public ClientLevel level;

    @Inject(method = "setScreen", at = @At("HEAD"))
    void openScreen(Screen screen, CallbackInfo ci) {
        MCXRPlayClient.INSTANCE.flatGuiManager.openScreen(screen);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;init(Lnet/minecraft/client/Minecraft;II)V"), method = "setScreen")
    void initScreen(Screen screen, Minecraft client, int widthIn, int heightIn) {
        if (level != null) {
            FlatGuiManager FGM = MCXRPlayClient.INSTANCE.flatGuiManager;
            screen.init(client, FGM.scaledWidth, FGM.scaledHeight);
        } else {
            screen.init(client, widthIn, heightIn);
        }
    }
}
