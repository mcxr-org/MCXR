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
        System.out.println("HELLO FROM CORE");

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
}
