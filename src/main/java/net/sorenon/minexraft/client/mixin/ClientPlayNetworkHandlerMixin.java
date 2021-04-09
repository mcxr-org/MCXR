package net.sorenon.minexraft.client.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.sorenon.minexraft.client.MineXRaftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Inject(method = "onGameJoin", at = @At("RETURN"))
    void postGameJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        MineXRaftClient.resetView();
    }

    @Inject(method = "onPlayerRespawn", at = @At("RETURN"))
    void postRespawn(PlayerRespawnS2CPacket packet, CallbackInfo ci) {
        MineXRaftClient.resetView();
    }
}
