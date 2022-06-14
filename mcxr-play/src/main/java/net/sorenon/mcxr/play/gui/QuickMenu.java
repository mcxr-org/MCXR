package net.sorenon.mcxr.play.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.sorenon.mcxr.play.compat.svc.SimpleVoiceChatCompat;

import java.util.ArrayList;

public class QuickMenu extends Screen {

    public QuickMenu(Component component) {
        super(component);
    }

    private void renderMenuButtons(PoseStack stack) {

        ArrayList<Button> QuickMenuButtons = new ArrayList<>();

        QuickMenuButtons.add(new Button((this.width/2) - 25, this.height/2, 70, 20, Component.translatable("QuickChat"), (button ) -> {
            Minecraft.getInstance().setScreen(new net.sorenon.mcxr.play.gui.QuickChat("QuickChat"));
        }));
        if (FabricLoader.getInstance().isModLoaded("voicechat")) {
            SimpleVoiceChatCompat.createButton(QuickMenuButtons, this.width, this.height);
        }

        QuickMenuButtons.add(new Button((this.width/2) - 25, this.height/2, 70, 20, Component.translatable("Chat"), (button ) -> {
            Minecraft.getInstance().setScreen(new XrChatScreen(""));
        }));

        for (int i = 0; i < QuickMenuButtons.size(); i++) {
            Button QuickMenuButton = QuickMenuButtons.get(i);

            QuickMenuButton.x = (this.width / 2) - (QuickMenuButton.getWidth()/2);
            QuickMenuButton.y = (this.height / 3) + (i*30);

            addRenderableWidget(QuickMenuButton);
        }

    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(PoseStack poseStack, int i, int j, float f) {
        renderMenuButtons(poseStack);
        super.render(poseStack, i, j, f);
    }
}
