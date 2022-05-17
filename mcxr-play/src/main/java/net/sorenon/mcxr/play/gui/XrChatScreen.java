package net.sorenon.mcxr.play.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.ChatScreen;
import net.sorenon.mcxr.play.PlayOptions;
import net.sorenon.mcxr.play.gui.keyboard.XrChatKeyboard;

public class XrChatScreen extends ChatScreen {

    private XrChatKeyboard _keyboard;

    public XrChatScreen(String string) {
        super(string);

    }

    public void clear() {
        this.clearWidgets();
    }

    public void addRenderWidget(AbstractWidget widget) {
        this.addRenderableWidget(widget);
    }

    @Override
    protected void init() {

        super.init();
        
        if (!PlayOptions.xrUninitialized)
            _keyboard = new XrChatKeyboard(this.input, this, 30);
            _keyboard.renderKeyboard(_keyboard.getDefaultCharset(), this.width, this.height, 30);

    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
