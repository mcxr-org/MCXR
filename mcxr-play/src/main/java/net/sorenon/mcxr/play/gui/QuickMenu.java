package net.sorenon.mcxr.play.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.ArrayList;

public class QuickMenu extends Screen {

    private Minecraft _client;

    public QuickMenu(Component component) {
        super(component);
        _client = Minecraft.getInstance();
    }

    private void renderMenuButtons(PoseStack stack) {

        ArrayList<Button> QuickMenuButtons = new ArrayList<Button>();
        //QuickMenuButtons.add(new Button((this.width/2) - 25, this.height/2, 60, 20, new TranslatableComponent("DebugMenu"), (button) -> {

        //}));

        QuickMenuButtons.add(new Button((this.width/2) - 25, this.height/2, 70, 20, new TranslatableComponent("QuickChat"), (button ) -> {
            Minecraft.getInstance().setScreen(new QuickChat("QuickChat"));
        }));

        //QuickMenuButtons.add(new Button((this.width/2) - 25, this.height/2, 70, 20, new TranslatableComponent("Swap Hand"), (button ) -> {
        //    this.onClose();
        //}));

        //QuickMenuButtons.add(new Button((this.width/2) - 25, this.height/2, 130, 20, new TranslatableComponent("Standing / Seated Play"), (button ) -> {

        //}));

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
