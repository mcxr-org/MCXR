package net.sorenon.mcxr.play.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.sorenon.mcxr.core.MCXRCore;
import net.sorenon.mcxr.play.MCXRPlayClient;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends PlayerEntity {

    @Shadow
    public abstract boolean isSneaking();

    @Shadow
    public Input input;

    @Unique
    private static final Input sneakingInput = new Input();

    public ClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    /**
     * Try to move the player to the position of the headset
     * We can do this entirely on the client because minecraft's anti cheat is non-existent
     */
    @Inject(method = "tick", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/client/network/ClientPlayerEntity;sendMovementPackets()V"))
    void applyRoomscaleMovement(CallbackInfo ci) {
        Vector3d roomscaleOffset = MCXRPlayClient.roomscalePlayerOffset;
        if (MCXRCore.getCoreConfig().roomscaleMovement() && !this.hasVehicle()) {
            Vector3f viewPos = MCXRPlayClient.viewSpacePoses.getScaledPhysicalPose().getPos();

            double oldX = this.getX();
            double oldZ = this.getZ();

            boolean onGround = this.onGround;
            Input input = this.input;
            this.input = sneakingInput;
            sneakingInput.sneaking = true;
            this.move(MovementType.SELF, new Vec3d(viewPos.x - roomscaleOffset.x, 0, viewPos.z - roomscaleOffset.z));
            this.input = input;
            this.onGround = onGround;

            double deltaX = this.getX() - oldX;
            double deltaZ = this.getZ() - oldZ;

            roomscaleOffset.x += deltaX;
            roomscaleOffset.z += deltaZ;

            this.prevX += deltaX;
            this.prevZ += deltaZ;
        } else {
            roomscaleOffset.zero();
        }
    }

    @Inject(method = "startRiding", at = @At("RETURN"))
    void onStartRiding(Entity entity, boolean force, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            MCXRPlayClient.resetView();
        }
    }
}
