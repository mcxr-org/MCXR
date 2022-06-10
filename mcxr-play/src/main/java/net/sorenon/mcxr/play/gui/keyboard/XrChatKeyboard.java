package net.sorenon.mcxr.play.gui.keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.sorenon.mcxr.play.gui.XrChatScreen;

public class XrChatKeyboard extends XrAbstract2DKeyboard {

    private final EditBox _chatBox;
    private final XrChatScreen _chatScreen;
    private final int _buttonWidth;

    private boolean _shift, _caps;

    public XrChatKeyboard(EditBox chatBox, XrChatScreen chatScreen, int buttonWidth) {
        _chatBox = chatBox;
        _chatScreen = chatScreen;
        _buttonWidth = buttonWidth;
    }

    @Override
    public void shiftButton(Button button) {
        _shift = !_shift;
        this._chatScreen.clear();
        renderKeyboard(_shift? this.getShiftCharset() : _caps? this.getCapsCharset() : this.getDefaultCharset(), _chatScreen.width, _chatScreen.height, _buttonWidth);
    }

    @Override
    public void capsButton(Button button) {
        _caps = !_caps;
        this._chatScreen.clear();
        renderKeyboard(_shift? this.getShiftCharset() : _caps? this.getCapsCharset() : this.getDefaultCharset(), _chatScreen.width, _chatScreen.height, _buttonWidth);
    }

    @Override
    public void tabButton(Button button) {
        _chatBox.setValue(_chatBox.getValue() + "    ");
    }

    @Override
    public void spaceButton(Button button) {
        _chatBox.setValue(_chatBox.getValue() + " ");
    }

    @Override
    public void backSpaceButton(Button instance) {
        _chatBox.setValue(removeLastChar(_chatBox.getValue()));
    }

    @Override
    public void returnButton(Button instance) {
        if (_chatBox.getValue().equals("")) {
            _chatScreen.onClose();
        } else {
            if(_chatBox.getValue().startsWith("/")) {
                Minecraft.getInstance().player.command(_chatBox.getValue().substring(1));
                _chatScreen.onClose();
            } else {
                Minecraft.getInstance().player.chat(_chatBox.getValue());
                _chatScreen.onClose();
            }
        }
    }

    @Override
    public void letterButton(Button instance) {
        String stringText = _chatBox.getValue() + instance.getMessage().getString();
        _chatBox.setValue(stringText);
    }

    @Override
    public void renderKey(Button key) {
        _chatScreen.addRenderWidget(key);
    }

    @Override
    public void afterRender() {
        //_chatScreen.addRenderWidget(_chatBox);
    }
}
