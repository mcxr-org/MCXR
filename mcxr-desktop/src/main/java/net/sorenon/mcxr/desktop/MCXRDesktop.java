package net.sorenon.mcxr.desktop;

import net.fabricmc.api.ClientModInitializer;
import net.sorenon.mcxr.play.MCXRPlayPlatform;
import net.sorenon.mcxr.play.PlayOptions;
import org.lwjgl.openxr.XR;

public class MCXRDesktop implements ClientModInitializer, MCXRPlayPlatform {

    @Override
    public void onInitializeClient() {
        if (!PlayOptions.xrUninitialized) {
            XR.create("openxr_loader");
        }
    }
}
