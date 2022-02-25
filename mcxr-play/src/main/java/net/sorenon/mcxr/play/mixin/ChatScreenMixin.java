package net.sorenon.mcxr.play.mixin;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public class ChatScreenMixin extends Screen {

    @Shadow protected EditBox input;

    protected ChatScreenMixin(Component component) {
        super(component);
    }

    @Inject(at=@At("TAIL"), method = "init")
    public void initChat(CallbackInfo ci) {
        String[] quickChat = new String[] {
                "Hello","How are you?", "I'm alright.",
                "Go to sleep please!", "I Have Phantoms!",
                "Ready to play?", "Ready when you are!",
                "Questcraft", "Are you on Quest 1 or 2?",
                "Quest 1", "Quest 2", "I'm Lagging!",
                "Wait for me!", "Have fun!", "Where are you?",
                "AFK", "BRB", "I'm Back", "/home", "/sethome",
                "/spawn", "/gamemode creative", "/gamemode survival"
        };

        for (int i = 0, j = 0, k = 0; i < quickChat.length; i++) {
            String word = quickChat[i];
            int buttonX = 170 * j;
            int buttonY = 25 * k + 10;
            int buttonWidth = 8*word.length();
            int buttonHeight = 20;

            this.addRenderableWidget(
                    new Button(buttonX, buttonY, buttonWidth, buttonHeight, new TranslatableComponent(word), (button -> {
                        this.sendMessage(word);
                    }))
            );

            if (i % 8 == 0 && i!=0) {
                j++;
                k=0;
            } else {
                k++;
            }

        }
    }

}
