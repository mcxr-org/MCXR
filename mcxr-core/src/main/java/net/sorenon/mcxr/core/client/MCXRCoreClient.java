package net.sorenon.mcxr.core.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.entity.player.Player;
import net.sorenon.mcxr.core.MCXRCore;
import net.sorenon.mcxr.core.Pose;
import net.sorenon.mcxr.core.accessor.PlayerExt;
import net.sorenon.mcxr.core.config.MCXRCoreConfigImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.concurrent.CompletableFuture;

public class MCXRCoreClient implements ClientModInitializer {

    public static MCXRCoreClient INSTANCE;

    private static final Logger LOGGER = LogManager.getLogger("MCXR Core");

    public boolean playInstalled = false;

    @Override
    public void onInitializeClient() {
        INSTANCE = this;

        playInstalled = FabricLoader.getInstance().isModLoaded("mcxr-play");

        ClientLoginNetworking.registerGlobalReceiver(MCXRCore.S2C_CONFIG, (client, handler, bufIn, listenerAdder) -> {
            var buf = PacketByteBufs.create();
            LOGGER.info("Received login packet");
            buf.writeBoolean(playInstalled);
            ((MCXRCoreConfigImpl) MCXRCore.getCoreConfig()).xrEnabled = true;
            return CompletableFuture.completedFuture(buf);
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) ->
                ((MCXRCoreConfigImpl) MCXRCore.getCoreConfig()).xrEnabled = false
        );

        ClientPlayNetworking.registerGlobalReceiver(MCXRCore.POSES, (client, handler, buf, listenerAdder)  -> {
            if(client.level != null) {
                Player player  = client.level.getPlayerByUUID(buf.readUUID());
                if(player != null) {
                    PlayerExt acc = (PlayerExt) player;
                    if(acc.getHeadPose() != null) {
                        acc.setIsXr(true);
                    }
                    var pose1 = new Pose();
                    var pose2 = new Pose();
                    var pose3 = new Pose();
                    pose1.read(buf);
                    acc.getHeadPose().set(pose1);
                    pose2.read(buf);
                    acc.getLeftHandPose().set(pose2);
                    pose3.read(buf);
                    acc.getRightHandPose().set(pose3);
                    acc.setHeight(buf.readFloat());
                }
            }
        });
    }
}
