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

    private boolean _shift, _caps;

    private static String removeLastChar(String s) {
        return (s == null || s.length() == 0)
                ? null
                : (s.substring(0, s.length() - 1));
    }

    private void renderKeyboard() {

        if (initial == "QuickChat") {
            return;
        }

        char[][] querty = new char[][] {
                new char[] {'`', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '-', '=', '\b'},
                new char[] {'\t', 'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', '[', ']', '\\'},
                new char[] {'\f', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', ';', '\'', '\r'},
                new char[] {'■','\n','\n', 'z', 'x', 'c', 'v', 'b', 'n', 'm', ',', '.', '/'},
                new char[] {' '}
        };

        char[][] capsquerty = new char[][] {
                new char[] {'`', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '-', '=', '\b'},
                new char[] {'\t', 'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', '[', ']', '\\'},
                new char[] {'\f', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', ';', '\'', '\r'},
                new char[] {'■','\n','\n', 'Z', 'X', 'C', 'V', 'B', 'N', 'M', ',', '.', '/'},
                new char[] {' '}
        };

        char[][] shiftquerty = new char[][] {
                new char[] {'~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '+', '\b'},
                new char[] {'\t', 'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', '{', '}', '|'},
                new char[] {'\f', 'A', 'S', 'D', 'F', 'G', 'G', 'J', 'K', 'L', ':', '\"', '\r'},
                new char[] {'■','\n','\n', 'Z', 'X', 'C', 'V', 'B', 'N', 'M', '<', '>', '?'},
                new char[] {' '}
        };


        int index = 1;
        int buttonSize = 30;

        for (int i = 0; i < querty.length; i++) {

            char[] chars;

            if (_caps) {
                chars = _shift? shiftquerty[i] : capsquerty[i];
            } else {
                chars = _shift? shiftquerty[i] : querty[i];
            }

            for (int j = 0; j < chars.length; j++) {

                int buttonx = ((this.width / 2) - 8 * buttonSize) + buttonSize * (j+1);
                int buttony = (this.height/5) + (buttonSize * index);
                char character = chars[j];

                Button key = new Button(buttonx, buttony, buttonSize, 20, new TranslatableComponent(Character.toString(character)), (button) -> {
                    String stringText = this.input.getValue() + character;
                    this.input.setValue(stringText);
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


                if (character == '\t') {
                    key = new Button(buttonx, buttony, buttonSize, 20, new TranslatableComponent("Tab"), button -> {
                        this.input.setValue(this.input.getValue() + "    ");
                    });
                }

                if (character == '\n') {
                    key = new Button(buttonx, buttony, buttonSize*2, 20, new TranslatableComponent("Shift"), button -> {
                        _shift = !_shift;
                        this.clearWidgets();
                        renderKeyboard();
                    });
                    j++;
                }

                if (character == '\f') {
                    key = new Button(buttonx, buttony, buttonSize, 20, new TranslatableComponent("Caps"), button -> {
                        _caps = !_caps;
                        this.clearWidgets();
                        renderKeyboard();
                    });
                }

                key.visible = !(character == '■');

                this.addRenderableWidget(key);

            }

            index ++;
            this.addRenderableWidget(this.input);
        }
    }

    @Inject(at=@At("TAIL"), method = "init")
    public void initChat(CallbackInfo ci) {

        renderKeyboard();

    }

}
