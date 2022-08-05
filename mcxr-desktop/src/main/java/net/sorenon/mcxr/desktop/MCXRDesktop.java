package net.sorenon.mcxr.desktop;

import net.fabricmc.api.ClientModInitializer;

public class MCXRDesktop implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        System.out.println("pain");
//        if (!PlayOptions.xrUninitialized) {
//            XR.create("openxr_loader");
//        }
    }
}
