package net.sorenon.mcxr.core;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.sorenon.mcxr.core.accessor.PlayerEntityAcc;
import net.sorenon.mcxr.core.config.MCXRCoreConfig;
import net.sorenon.mcxr.core.config.MCXRCoreConfigImpl;
import net.sorenon.mcxr.core.mixin.ServerLoginNetworkHandlerAcc;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class MCXRCore implements ModInitializer {

    public static final ResourceLocation S2C_CONFIG = new ResourceLocation("mcxr", "config");

    public static final ResourceLocation IS_XR_PLAYER = new ResourceLocation("mcxr", "is_xr_player");
    public static final ResourceLocation POSES = new ResourceLocation("mcxr", "poses");

    public static MCXRCore INSTANCE;

    private static final Logger LOGGER = LogManager.getLogger("MCXR Core");

    public final MCXRCoreConfigImpl config = new MCXRCoreConfigImpl();

    @Override
    public void onInitialize() {
        INSTANCE = this;
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            config.xrEnabled = true;
        }

        ServerLoginNetworking.registerGlobalReceiver(S2C_CONFIG, (server, handler, understood, buf, synchronizer, responseSender) -> {
            if (understood) {
                boolean xr = buf.readBoolean();
                var profile = ((ServerLoginNetworkHandlerAcc) handler).getGameProfile();
                if (xr) {
                    LOGGER.info("Received XR login packet from " + profile.getId());
                } else {
                    LOGGER.info("Received login packet from " + profile.getId());
                }
            }
        });

        ServerLoginConnectionEvents.QUERY_START.register((handler, server, sender, synchronizer) -> {
            LOGGER.debug("Sending login packet to " + handler.getUserName());
            var buf = PacketByteBufs.create();
            sender.sendPacket(S2C_CONFIG, buf);
        });

        ServerPlayNetworking.registerGlobalReceiver(IS_XR_PLAYER,
                (server, player, handler, buf, responseSender) -> {
                    boolean isXr = buf.readBoolean();
                    server.execute(() -> {
                        PlayerEntityAcc acc = (PlayerEntityAcc) player;
                        boolean wasXr = acc.isXR();
                        acc.setIsXr(isXr);
                        if (wasXr && !isXr) {
                            player.refreshDimensions();
                        }
                    });
                });

        ServerPlayNetworking.registerGlobalReceiver(POSES,
                (server, player, handler, buf, responseSender) -> {
                    Vector3f vec = new Vector3f(buf.readFloat(), buf.readFloat(), buf.readFloat());
                    Quaternionf quat = new Quaternionf(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
                    server.execute(() -> {
                        Pose pose = new Pose();
                        pose.pos.set(vec);
                        pose.orientation.set(quat);
                        setPlayerHeadPose(player, pose);
                    });
                });
    }

    public void setPlayerHeadPose(Player player, Pose pose) {
        PlayerEntityAcc acc = (PlayerEntityAcc) player;
        acc.getHeadPose().set(pose);

        if (player instanceof LocalPlayer) {
            acc.getHeadPose().getPos().sub(JOMLUtil.convert(player.position()).get(new Vector3f()));

            FriendlyByteBuf buf = PacketByteBufs.create();
            Vector3f pos = acc.getHeadPose().getPos();
            Quaternionf quat = acc.getHeadPose().getOrientation();
            buf.writeFloat(pos.x).writeFloat(pos.y).writeFloat(pos.z);
            buf.writeFloat(quat.x).writeFloat(quat.y).writeFloat(quat.z).writeFloat(quat.w);

            ClientPlayNetworking.send(POSES, buf);
        }
    }

    public static MCXRCoreConfig getCoreConfig() {
        return INSTANCE.config;
    }

}
