package net.sorenon.mcxr_core;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.sorenon.mcxr_core.accessor.PlayerEntityAcc;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class MCXRCore implements ModInitializer {

    public static final Identifier POSES = new Identifier("mcxr", "poses");
    public static MCXRCore INSTANCE;

    @Override
    public void onInitialize() {
        INSTANCE = this;

        ServerPlayNetworking.registerGlobalReceiver(POSES,
                (server, player, handler, buf, responseSender) -> {
                    Vector3f vec = new Vector3f(buf.readFloat(), buf.readFloat(), buf.readFloat());
                    Quaternionf quat = new Quaternionf(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
                    server.execute(() -> {
                        Pose pose = new Pose();
                        pose.pos.set(vec);
                        pose.orientation.set(quat);
                        playerPose(player, pose);
                    });
                });
    }

    public void playerPose(PlayerEntity player, Pose pose) {
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
}
