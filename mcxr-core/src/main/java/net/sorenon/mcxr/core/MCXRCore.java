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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
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

    public static final ResourceLocation POSE_HEAD = new ResourceLocation("mcxr", "posehead");
    public static final ResourceLocation POSE_LHAND = new ResourceLocation("mcxr", "poselhand");
    public static final ResourceLocation POSE_RHAND = new ResourceLocation("mcxr", "poserhand");
    public static MCXRCore INSTANCE;

    private static final Logger LOGGER = LogManager.getLogger("MCXR Core");

    public final MCXRCoreConfigImpl config = new MCXRCoreConfigImpl();

    @Override
    public void onInitialize() {
        INSTANCE = this;
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            config.xrEnabled = true;
        }

        initNetworkListeners();
    }

    public void initNetworkListeners() {
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

        ServerPlayNetworking.registerGlobalReceiver(POSE_HEAD,
                (server, player, handler, buf, responseSender) -> {
                    Vector3f vec = new Vector3f(buf.readFloat(), buf.readFloat(), buf.readFloat());
                    Quaternionf quat = new Quaternionf(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
                    server.execute(() -> {
                        Pose pose = new Pose();
                        pose.pos.set(vec);
                        pose.orientation.set(quat);
                        setPlayerHeadPose(player, pose);
                        sendToAllOtherPlayers(player, POSE_HEAD, pose, server);
                    });
                });

        ServerPlayNetworking.registerGlobalReceiver(POSE_LHAND,
                (server, player, handler, buf, responseSender) -> {
                    Vector3f vec = new Vector3f(buf.readFloat(), buf.readFloat(), buf.readFloat());
                    Quaternionf quat = new Quaternionf(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
                    server.execute(() -> {
                        Pose pose = new Pose();
                        pose.pos.set(vec);
                        pose.orientation.set(quat);
                        setPlayerLeftArmPose(player, pose, pose);
                        sendToAllOtherPlayers(player, POSE_LHAND, pose, server);
                    });
                });

        ServerPlayNetworking.registerGlobalReceiver(POSE_RHAND,
                (server, player, handler, buf, responseSender) -> {
                    Vector3f vec = new Vector3f(buf.readFloat(), buf.readFloat(), buf.readFloat());
                    Quaternionf quat = new Quaternionf(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
                    server.execute(() -> {
                        Pose pose = new Pose();
                        pose.pos.set(vec);
                        pose.orientation.set(quat);
                        setPlayerRightArmPose(player, pose, pose);
                        sendToAllOtherPlayers(player, POSE_RHAND, pose, server);
                    });
                });
    }

    private void sendToAllOtherPlayers(ServerPlayer player, ResourceLocation channel, Pose pose, MinecraftServer server) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        Vector3f pos = pose.pos;
        Quaternionf quat = pose.orientation;
        buf.writeFloat(pos.x).writeFloat(pos.y).writeFloat(pos.z);
        buf.writeFloat(quat.x).writeFloat(quat.y).writeFloat(quat.z).writeFloat(quat.w);
        buf.writeUUID(player.getUUID());
        for(ServerPlayer player1 : server.getPlayerList().getPlayers()) {
            if(player1 != player) {
                ServerPlayNetworking.send(player1, channel, buf);
            }
        }
    }

    public void setPlayerHeadPose(Player player, Pose pose) {
        PlayerEntityAcc acc = (PlayerEntityAcc) player;
        if (acc.getHeadPose() == null) {
            acc.markVR();
        }
        acc.getHeadPose().set(pose);

        if (player.level.isClientSide && player instanceof LocalPlayer) {
            FriendlyByteBuf buf = PacketByteBufs.create();
            Vector3f pos = pose.pos;
            Quaternionf quat = pose.orientation;
            buf.writeFloat(pos.x).writeFloat(pos.y).writeFloat(pos.z);
            buf.writeFloat(quat.x).writeFloat(quat.y).writeFloat(quat.z).writeFloat(quat.w);

            ClientPlayNetworking.send(POSE_HEAD, buf);
        }
    }

    public void setPlayerLeftArmPose(Player player, Pose aim, Pose rotation) {
        PlayerEntityAcc acc = (PlayerEntityAcc) player;
        if (acc.getLArmPose() == null) {
            acc.markLArm();
        }
        acc.getLArmPose().pos.set(aim.pos);
        acc.getLArmPose().orientation.set(rotation.orientation);

        if (player.level.isClientSide && player instanceof LocalPlayer) {
            FriendlyByteBuf buf = PacketByteBufs.create();
            Vector3f pos = aim.pos;
            Quaternionf quat = rotation.orientation;
            buf.writeFloat(pos.x).writeFloat(pos.y).writeFloat(pos.z);
            buf.writeFloat(quat.x).writeFloat(quat.y).writeFloat(quat.z).writeFloat(quat.w);

            ClientPlayNetworking.send(POSE_LHAND, buf);
        }
    }

    public void setPlayerRightArmPose(Player player, Pose aim, Pose rotation) {
        PlayerEntityAcc acc = (PlayerEntityAcc) player;
        if (acc.getRArmPose() == null) {
            acc.markRArm();
        }
        acc.getRArmPose().pos.set(aim.pos);
        acc.getRArmPose().orientation.set(rotation.orientation);

        if (player.level.isClientSide && player instanceof LocalPlayer) {
            FriendlyByteBuf buf = PacketByteBufs.create();
            Vector3f pos = aim.pos;
            Quaternionf quat = rotation.orientation;
            buf.writeFloat(pos.x).writeFloat(pos.y).writeFloat(pos.z);
            buf.writeFloat(quat.x).writeFloat(quat.y).writeFloat(quat.z).writeFloat(quat.w);

            ClientPlayNetworking.send(POSE_RHAND, buf);
        }
    }

    public static MCXRCoreConfig getCoreConfig() {
        return INSTANCE.config;
    }

}
