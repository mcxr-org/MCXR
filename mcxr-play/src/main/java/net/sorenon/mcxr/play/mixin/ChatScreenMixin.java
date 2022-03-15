package net.sorenon.mcxr.play.mixin;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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

    protected ChatScreenMixin(Component component) {
        super(component);
    }

    private static String removeLastChar(String s) {
        return (s == null || s.length() == 0)
                ? null
                : (s.substring(0, s.length() - 1));
    }

    @Inject(at=@At("TAIL"), method = "init")
    public void initChat(CallbackInfo ci) {

        if (initial == "QuickChat") {
            return;
        }

        char[][] querty = new char[][] {
                new char[] {'`', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '-', '=', '\b'},
                new char[] {'~', 'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', '[', ']', '\\'},
                new char[] {'\n', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', ';', '\'', '\r'},
                new char[] {'\n', '_', 'z', 'x', 'c', 'v', 'b', 'n', 'm', ',', '.', '/', '?'},
                new char[] {' '}
        };

        int index = 1;
        int buttonSize = 30;

        for (char[] chars : querty) {

            for (int j = 0; j < chars.length; j++) {

                int buttonx = ((this.width / 2) - 8 * buttonSize) + buttonSize * (j+1);
                int buttony = (this.height/5) + (buttonSize * index);
                char character = chars[j];

                Button key = new Button(buttonx, buttony, buttonSize, 20, new TranslatableComponent(Character.toString(character)), (button) -> {
                    String stringText = this.input.getValue() + character;
                    this.input.setValue(stringText.substring(0, 1).toUpperCase(Locale.ROOT) + stringText.substring(1));
                });

                if (character == '\r') {
                    key = new Button(buttonx, buttony, buttonSize, 20, new TranslatableComponent("Enter"), (button) -> {
                        if (this.input.getValue() == "") {
                            this.onClose();
                        } else {
                            this.sendMessage(this.input.getValue());
                            this.onClose();
                        }
                    });
                }

                if (character == '\b') {
                    key = new Button(buttonx, buttony, buttonSize, 20, new TranslatableComponent("Bksp"), (button ) -> {
                        this.input.setValue(removeLastChar(this.input.getValue()));
                    });
                }

                if (character == ' ') {
                    key = new Button(buttonx + 150, buttony, buttonSize + 100, 20, new TranslatableComponent("Space"), (button) -> {
                        this.input.setValue(this.input.getValue() + ' ');
                    });
                }

                key.visible = !(character == '\n');

                this.addRenderableWidget(key);

            }

            index ++;
        }

    }

}
