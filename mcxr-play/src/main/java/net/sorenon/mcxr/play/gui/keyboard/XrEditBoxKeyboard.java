package net.sorenon.mcxr.play.gui.keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.DirectJoinServerScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.Component;
import net.sorenon.mcxr.play.gui.XrEditBoxScreen;

public class XrEditBoxKeyboard extends XrAbstract2DKeyboard {

    private final EditBox _textField;
    private final XrEditBoxScreen _editScreen;
    private final EditBox _placeholderField;
    private final int _buttonWidth;

    private boolean _shift, _caps;

    public XrEditBoxKeyboard(EditBox textField, XrEditBoxScreen editScreen, int buttonWidth) {
        super();
        _textField = textField;
        _editScreen = editScreen;

        _placeholderField = new EditBox(Minecraft.getInstance().font,
                133, 22, _textField.getWidth(), _textField.getHeight(),
                _textField, Component.translatable(""));

        _buttonWidth = buttonWidth;
    }

    @Override
    public void letterButton(Button instance) {
        String strtext = this._placeholderField.getValue() + instance.getMessage().getString();
        _placeholderField.setValue(strtext);
    }

    @Override
    public void returnButton(Button instance) {
        this._textField.setValue(_placeholderField.getValue());

        if (_editScreen.getParentScreen().getClass() == DirectJoinServerScreen.class) {

            ServerData server = new ServerData(_placeholderField.getValue(), "Server", false);
            ConnectScreen.startConnecting(_editScreen.getParentScreen(), Minecraft.getInstance(), ServerAddress.parseString(_placeholderField.getValue()), server);

        } else if (_editScreen.getParentScreen().getClass() == JoinMultiplayerScreen.class) {

            ServerData server = new ServerData(_placeholderField.getValue(), _placeholderField.getValue(), false);
            _editScreen.getServers().add(server, false);
            _editScreen.getServers().save();
            Minecraft.getInstance().setScreen(_editScreen.getParentScreen());


        } else if (_editScreen.getParentScreen().getClass() == CreativeModeInventoryScreen.class) {

            char[] searchChars = _placeholderField.getValue().toCharArray();
            Minecraft.getInstance().setScreen(_editScreen.getParentScreen());
            for (int i = 0; i < searchChars.length; i++) {
                Minecraft.getInstance().screen.charTyped(searchChars[i], 0);
            }

        } else {

            Minecraft.getInstance().setScreen(_editScreen.getParentScreen());

        }
    }

    @Override
    public void backSpaceButton(Button instance) {
        this._placeholderField.setValue(removeLastChar(this._placeholderField.getValue()));
    }

    @Override
    public void spaceButton(Button instance) {
        this._placeholderField.setValue(this._placeholderField.getValue() + ' ');
    }

    @Override
    public void tabButton(Button button) {
        this._placeholderField.setValue(this._placeholderField.getValue() + "    ");
    }

    @Override
    public void shiftButton(Button button) {
        _shift = !_shift;
        this._editScreen.clear();
        renderKeyboard(_shift? this.getShiftCharset() : _caps? this.getCapsCharset() : this.getDefaultCharset(), _editScreen.width, _editScreen.height, _buttonWidth);
    }

    @Override
    public void capsButton(Button button) {
        _caps = !_caps;
        this._editScreen.clear();
        renderKeyboard(_shift? this.getShiftCharset() : _caps? this.getCapsCharset() : this.getDefaultCharset(), _editScreen.width, _editScreen.height, _buttonWidth);
    }

    @Override
    public void renderKey(Button key) {
        _editScreen.addRenderWidget(key);
    }

    @Override
    public void afterRender() {
        _editScreen.addRenderWidget(_placeholderField);
    }
}
