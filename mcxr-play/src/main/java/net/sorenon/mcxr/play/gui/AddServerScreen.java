package net.sorenon.mcxr.play.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class AddServerScreen extends Screen {

    private final Minecraft minecraft;
    private final JoinMultiplayerScreen multiplayerScreen;
    private ServerData server;

    public AddServerScreen(Component component, JoinMultiplayerScreen multiplayerScreen) {
        super(component);
        this.multiplayerScreen = multiplayerScreen;
        minecraft = Minecraft.getInstance();
    }

    @Override
    protected void init() {

        super.init();

        EditBox editbox = new EditBox(minecraft.font, (this.width) / 2 - 100, (this.height / 2) - 10, 200, 20, new TranslatableComponent("EditBox"));
        Vr2DKeyboard kb = new Vr2DKeyboard(new TranslatableComponent("KB"), multiplayerScreen, editbox, multiplayerScreen.getServers());
        minecraft.setScreen(kb);

    }

    @Override
    public void onClose() {
        super.onClose();
    }
}
