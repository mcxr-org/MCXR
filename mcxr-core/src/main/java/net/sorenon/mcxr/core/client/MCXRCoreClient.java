package net.sorenon.mcxr.core.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.sorenon.mcxr.core.MCXRCore;
import net.sorenon.mcxr.core.Pose;
import net.sorenon.mcxr.core.accessor.PlayerExt;
import net.sorenon.mcxr.core.config.MCXRCoreConfigImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CompletableFuture;

import static net.sorenon.mcxr.core.MCXRCore.POSES;

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
    }

    public void setPlayerPoses(
            Player player,
            Pose headPose,
            Pose leftHandPose,
            Pose rightHandPose,
//            float height,
            float handAngleAdjust
    ) {
        PlayerExt acc = (PlayerExt) player;
        acc.getHeadPose().set(headPose);
        acc.getLeftHandPose().set(leftHandPose);
        acc.getRightHandPose().set(rightHandPose);
//        acc.setHeight(height);

        acc.getHeadPose().pos.sub((float) player.getX(), (float) player.getY(), (float) player.getZ());
        acc.getLeftHandPose().pos.sub((float) player.getX(), (float) player.getY(), (float) player.getZ());
        acc.getRightHandPose().pos.sub((float) player.getX(), (float) player.getY(), (float) player.getZ());

        acc.getLeftHandPose().orientation.rotateX(handAngleAdjust);
        acc.getRightHandPose().orientation.rotateX(handAngleAdjust);

        FriendlyByteBuf buf = PacketByteBufs.create();
        acc.getHeadPose().write(buf);
        acc.getLeftHandPose().write(buf);
        acc.getRightHandPose().write(buf);
//        buf.writeFloat(height);

        ClientPlayNetworking.send(POSES, buf);
    }
}
