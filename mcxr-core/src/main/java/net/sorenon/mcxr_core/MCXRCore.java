package net.sorenon.mcxr_core;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class MCXRCore implements ModInitializer {

    public static final Identifier HEADSET_POS = new Identifier("mcxr", "headset_pos");

    @Override
    public void onInitialize() {
        System.out.println("HELLO FROM CORE");

        ServerPlayNetworking.registerGlobalReceiver(HEADSET_POS,
                (server, player, handler, buf, responseSender) -> {
                });
    }
}
