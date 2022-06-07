package net.sorenon.mcxr.play.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class QuickChat extends ChatScreen {

    private final static Logger LOGGER = LogManager.getLogger("mcxr-play");

    public QuickChat(String string) {
        super(string);
    }

    @Override
    protected void init() {

        super.init();

        Minecraft.getInstance().gui.getChat().clearMessages(true);

        String[] quickChat;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("quickchat.conf"));
            ArrayList<String> quicklist = new ArrayList<String>();
            String line;
            while (null != (line = reader.readLine())) {
                quicklist.add(line);
            }

            reader.close();
            quickChat = quicklist.toArray(new String[]{});
        } catch (FileNotFoundException e) {
            LOGGER.error(e);
            quickChat = new String[] {
                    "Hello","How are you?", "I'm alright.",
                    "Go to sleep please!", "I Have Phantoms!",
                    "Ready to play?", "Ready when you are!",
                    "MCXR", "What VR headset are you using?",
                    "Oculus Quest", "Index", "Vive", "Oculus Rift",
                    "I'm Lagging!",
                    "Wait for me!", "Have fun!", "Where are you?",
                    "AFK", "BRB", "I'm Back", "/home", "/sethome",
                    "/spawn", "/gamemode creative", "/gamemode survival"
            };
        } catch (IOException f ) {
            LOGGER.error(f);
            quickChat = new String[] {
                    "Hello","How are you?", "I'm alright.",
                    "Go to sleep please!", "I Have Phantoms!",
                    "Ready to play?", "Ready when you are!",
                    "MCXR", "What VR headset are you using?",
                    "Oculus Quest", "Index", "Vive", "Oculus Rift",
                    "I'm Lagging!",
                    "Wait for me!", "Have fun!", "Where are you?",
                    "AFK", "BRB", "I'm Back", "/home", "/sethome",
                    "/spawn", "/gamemode creative", "/gamemode survival"
            };
        }

        for (int i = 0, j = 0, k = 0; i < quickChat.length; i++) {
            String word = quickChat[i];
            int buttonX = 170 * j;
            int buttonY = 25 * k + 10;
            int buttonWidth = 8 * word.length();
            int buttonHeight = 20;

            this.addRenderableWidget(
                    new Button(buttonX, buttonY, buttonWidth, buttonHeight, Component.translatable(word), (button -> {
                        if (word.startsWith("/")) {
                            Minecraft.getInstance().player.command(word.substring(1));
                        } else {
                            Minecraft.getInstance().player.chat(word);
                        }
//                        Minecraft.getInstance().gui.getChat().clearMessages(true);
                    }))
            );

            if (i % 8 == 0 && i != 0) {
                j++;
                k = 0;
            } else {
                k++;
            }
        }
    }
}
