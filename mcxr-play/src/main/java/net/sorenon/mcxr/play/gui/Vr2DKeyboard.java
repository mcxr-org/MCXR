package net.sorenon.mcxr.play.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.DirectJoinServerScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.TranslatableComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Vr2DKeyboard extends Screen {

    private final EditBox _textField;
    private final EditBox _placeholderField;
    private final Screen _parentScreen;

    private boolean _shift, _caps;

    private static final Logger LOGGER = LogManager.getLogger("questcraft");

    private ServerList servers;

    public Vr2DKeyboard(TranslatableComponent title, Screen parentScreen, EditBox textField) {
        super(title);
        _textField = textField;
        _parentScreen = parentScreen;
        _placeholderField = new EditBox(Minecraft.getInstance().font,
                133, 22, _textField.getWidth(), _textField.getHeight(),
                _textField, new TranslatableComponent(""));
    }

    public Vr2DKeyboard(TranslatableComponent title, Screen parentscreen, EditBox textField, ServerList servers) {
        super(title);
        this.servers = servers;
        this.servers.load();
        _textField = textField;
        _parentScreen = parentscreen;
        _placeholderField = new EditBox(Minecraft.getInstance().font,
                133, 22, _textField.getWidth(), _textField.getHeight(),
                _textField, new TranslatableComponent(""));

    }

    public static String removeLastChar(String s) {
        return (s == null || s.length() == 0)
                ? null
                : (s.substring(0, s.length() - 1));
    }

    public void renderKeyboard() {

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
        final int buttonSize = 30;

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

                Button key = new Button(buttonx, buttony, buttonSize, 20, new TranslatableComponent(Character.toString(character)), button -> {
                    String strtext = this._placeholderField.getValue() + character;
                    _placeholderField.setValue(strtext);
                });

                if (character == '\r') {
                    key = new Button(buttonx, buttony, buttonSize*2, 20, new TranslatableComponent("Enter"), button -> {
                        this._textField.setValue(_placeholderField.getValue());

                        if (_parentScreen.getClass() == DirectJoinServerScreen.class) {
                            ServerData server = new ServerData(_placeholderField.getValue(), "Server", false);
                            ConnectScreen.startConnecting(_parentScreen, Minecraft.getInstance(), ServerAddress.parseString(_placeholderField.getValue()), server);
                        } else if (_parentScreen.getClass() == JoinMultiplayerScreen.class) {
                            ServerData server = new ServerData(_placeholderField.getValue(), _placeholderField.getValue(), false);
                            this.servers.add(server);
                            this.servers.save();
                            Minecraft.getInstance().setScreen(_parentScreen);
                        } else {
                            Minecraft.getInstance().setScreen(_parentScreen);
                        }
                    });
                }

                if (character == '\b') {
                    key = new Button(buttonx, buttony, buttonSize, 20, new TranslatableComponent("Bksp"), button -> {
                        this._placeholderField.setValue(removeLastChar(this._placeholderField.getValue()));
                    });
                }

                if (character == ' ') {
                    key = new Button(buttonx + 150, buttony, buttonSize + 100, 20, new TranslatableComponent("Space"), button -> {
                        this._placeholderField.setValue(this._placeholderField.getValue() + ' ');
                    });
                }

                if (character == '\t') {
                    key = new Button(buttonx, buttony, buttonSize, 20, new TranslatableComponent("Tab"), button -> {
                        this._placeholderField.setValue(this._placeholderField.getValue() + "    ");
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

            index++;
        }
        addRenderableWidget(_placeholderField);
    }

    @Override
    protected void init() {
        renderKeyboard();

        super.init();
    }

    @Override
    public void render(PoseStack poseStack, int i, int j, float f) {
        renderBackground(poseStack);
        super.render(poseStack, i, j, f);
    }
}
