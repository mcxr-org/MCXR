package net.sorenon.mcxr.play.compat.svc;

// import de.maxhenkel.voicechat.gui.VoiceChatScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;

public class SimpleVoiceChatCompat {
    public static void createButton(ArrayList<Button> buttons, int width, int height) {
        buttons.add(new Button((width/2) - 25, height/2 + 20, 70+ Minecraft.getInstance().font.width("Simple Voice Chat Settings"), 20, Component.translatable("Simple Voice Chat Settings"), (button) -> {
            // Minecraft.getInstance().setScreen(new VoiceChatScreen());
        }));
    }
}
