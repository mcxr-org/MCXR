package net.sorenon.mcxr.play.mixin.roomscale;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.sorenon.mcxr.core.MCXRCore;
import net.sorenon.mcxr.core.MCXRScale;
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
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(LocalPlayer.class)
public abstract class ClientPlayerEntityMixin extends Player {

    @Shadow
    public abstract boolean isShiftKeyDown();

    @Shadow
    public Input input;

    @Shadow public abstract void move(MoverType movementType, Vec3 movement);

    @Unique
    private static final Input sneakingInput = new Input();

    public ClientPlayerEntityMixin(Level world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    /**
     * Try to move the player to the position of the headset
     * We can do this entirely on the client because minecraft's anti cheat is non-existent
     * TODO handle teleportation here for vanilla servers
     */
    @Inject(method = "tick", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/client/player/LocalPlayer;sendPosition()V"))
    void applyRoomscaleMovement(CallbackInfo ci) {
        Vector3d roomscaleOffset = MCXRPlayClient.roomscalePlayerOffset;
        if (MCXRCore.getCoreConfig().roomscaleMovement() && !this.isPassenger()) {
            Vector3f viewPos = MCXRPlayClient.viewSpacePoses.getScaledPhysicalPose().getPos();

            double oldX = this.getX();
            double oldZ = this.getZ();

            boolean onGround = this.onGround;
            Input input = this.input;
            this.input = sneakingInput;
            sneakingInput.shiftKeyDown = true;

            final float invScale = 1.0f / MCXRScale.getMotionScale(this); //Counter out the method pehuki uses for scaling movement
            this.move(MoverType.SELF, new Vec3(viewPos.x - roomscaleOffset.x, 0, viewPos.z - roomscaleOffset.z).scale(invScale));
            this.input = input;
            this.onGround = onGround;

            double deltaX = this.getX() - oldX;
            double deltaZ = this.getZ() - oldZ;

            roomscaleOffset.x += deltaX;
            roomscaleOffset.z += deltaZ;

            this.xo += deltaX;
            this.zo += deltaZ;
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
