package net.sorenon.mcxr.play.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.network.chat.Component;
import net.sorenon.mcxr.play.PlayOptions;
import net.sorenon.mcxr.play.gui.keyboard.XrEditBoxKeyboard;

public class XrEditBoxScreen extends Screen {

    private final Screen _parentScreen;
    private final XrEditBoxKeyboard keyboard;
    private ServerList servers;

    public XrEditBoxScreen(Component title, Screen parentScreen, EditBox textField) {
        super(title);
        _parentScreen = parentScreen;
        this.keyboard = new XrEditBoxKeyboard(textField, this, 30);
    }

    public XrEditBoxScreen(Component title, Screen parentscreen, EditBox textField, ServerList servers) {
        super(title);
        this.keyboard = new XrEditBoxKeyboard(textField, this, 30);
        this.servers = servers;
        this.servers.load();
        _parentScreen = parentscreen;
    }

    public Screen getParentScreen() {
        return _parentScreen;
    }

    public ServerList getServers() {
        return servers;
    }

    public void clear() {
        this.clearWidgets();
    }

    public void addRenderWidget(AbstractWidget widget) {
        this.addRenderableWidget(widget);
    }

    @Override
    protected void init() {

        if (!PlayOptions.xrUninitialized)
            keyboard.renderKeyboard(keyboard.getDefaultCharset(), this.width, this.height, 30);

        super.init();
    }

    @Override
    public void render(PoseStack poseStack, int i, int j, float f) {
        renderBackground(poseStack);
        super.render(poseStack, i, j, f);
    }
}
