package net.sorenon.mcxr.play.mixin;

import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Locale;

@Mixin(ChatScreen.class)
public class ChatScreenMixin extends Screen {

    @Shadow protected EditBox input;

    @Shadow @Final private String initial;

    @Shadow CommandSuggestions commandSuggestions;

    protected ChatScreenMixin(Component component) {
        super(component);
    }

    @Inject(at=@At("TAIL"), method = "init")
    public void initChat(CallbackInfo ci) {

        //reduce number of command suggestions to not overlap keyboard
        this.commandSuggestions = new CommandSuggestions(this.minecraft, this, this.input, this.font, false, false, 1, 5, true, -805306368);
        this.commandSuggestions.updateCommandInfo();

    }

}