package net.sorenon.mcxr.core.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.loader.api.FabricLoader;
import net.sorenon.mcxr.core.MCXRCore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MCXRCoreClient implements ClientModInitializer {

    public static MCXRCoreClient INSTANCE;
    public List<String> config = null;

    private static final Logger LOGGER = LogManager.getLogger("MCXR Core");

    public boolean fullFunc = false;

    @Override
    public void onInitializeClient() {
        INSTANCE = this;

        ClientLoginNetworking.registerGlobalReceiver(MCXRCore.S2C_CONFIG, (client, handler, bufIn, listenerAdder) -> {
            var buf = PacketByteBufs.create();
            LOGGER.info("Received login packet");
            fullFunc = true;
            buf.writeBoolean(FabricLoader.getInstance().isModLoaded("mcxr-play"));

            return CompletableFuture.completedFuture(buf);
        });
    }
}
