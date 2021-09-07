package net.sorenon.mcxr.core;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.sorenon.mcxr.core.accessor.PlayerEntityAcc;
import net.sorenon.mcxr.core.config.MCXRCoreConfig;
import net.sorenon.mcxr.core.config.MCXRCoreConfigImpl;
import net.sorenon.mcxr.core.mixin.ServerLoginNetworkHandlerAcc;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import virtuoel.pehkui.api.ScaleType;

public class MCXRCore implements ModInitializer {

    public static final Identifier S2C_CONFIG = new Identifier("mcxr", "config");

    public static final Identifier POSES = new Identifier("mcxr", "poses");
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
                var profile = ((ServerLoginNetworkHandlerAcc) handler).getProfile();
                if (xr) {
                    LOGGER.info("Received XR login packet from " + profile.getId());
                } else {
                    LOGGER.info("Received login packet from " + profile.getId());
                }
            }
        });

        ServerLoginConnectionEvents.QUERY_START.register((handler, server, sender, synchronizer) -> {
            LOGGER.debug("Sending login packet to " + handler.getConnectionInfo());
            var buf = PacketByteBufs.create();
            sender.sendPacket(S2C_CONFIG, buf);
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

    public void setPlayerHeadPose(PlayerEntity player, Pose pose) {
        PlayerEntityAcc acc = (PlayerEntityAcc) player;
        if (acc.getHeadPose() == null) {
            acc.markVR();
        }
        acc.getHeadPose().set(pose);

        if (player instanceof ClientPlayerEntity) {
            PacketByteBuf buf = PacketByteBufs.create();
            Vector3f pos = pose.pos;
            Quaternionf quat = pose.orientation;
            buf.writeFloat(pos.x).writeFloat(pos.y).writeFloat(pos.z);
            buf.writeFloat(quat.x).writeFloat(quat.y).writeFloat(quat.z).writeFloat(quat.w);

            ClientPlayNetworking.send(POSES, buf);
        }
    }

    public static MCXRCoreConfig getCoreConfig() {
        return INSTANCE.config;
    }

    public static float getScale(Entity entity) {
        return getScale(entity, 1.0f);
    }

    public static float getScale(Entity entity, float delta) {
        if (FabricLoader.getInstance().isModLoaded("pehkui")) {
            var scaleData = ScaleType.BASE.getScaleData(entity);
            return scaleData.getScale(delta);
        } else {
            return 1;
        }
    }
}
