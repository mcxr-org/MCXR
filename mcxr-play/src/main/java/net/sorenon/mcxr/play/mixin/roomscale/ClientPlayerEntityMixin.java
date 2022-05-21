package net.sorenon.mcxr.play.mixin.roomscale;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.phys.Vec3;
import net.sorenon.mcxr.core.MCXRCore;
import net.sorenon.mcxr.core.MCXRScale;
import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.play.gui.XrSignEditScreen;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class ClientPlayerEntityMixin extends Player {

    public ClientPlayerEntityMixin(Level level, BlockPos blockPos, float f, GameProfile gameProfile, @Nullable ProfilePublicKey profilePublicKey) {
        super(level, blockPos, f, gameProfile, profilePublicKey);
    }

    @Shadow
    public abstract boolean isShiftKeyDown();

    @Shadow
    public Input input;

    @Shadow public abstract void move(MoverType movementType, Vec3 movement);

    @Unique
    private static final Input sneakingInput = new Input();

    /**
     * Try to move the player to the position of the headset
     * We can do this entirely on the client because minecraft's anti cheat is non-existent
     * TODO handle teleportation here for vanilla servers
     */
    @Inject(method = "tick", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/client/player/LocalPlayer;sendPosition()V"))
    void applyRoomscaleMovement(CallbackInfo ci) {
        if (!MCXRPlayClient.MCXR_GAME_RENDERER.isXrMode()) {
            return;
        }

        Vector3d playerPhysicalPosition = MCXRPlayClient.playerPhysicalPosition;
        if (MCXRCore.getCoreConfig().roomscaleMovement() && !this.isPassenger()) {
            //Get the user's head's position in physical space
            Vector3f viewPos = MCXRPlayClient.viewSpacePoses.getPhysicalPose().getPos();

            //Store the player entity's position
            double oldX = this.getX();
            double oldZ = this.getZ();

            //Force the player to sneak to they don't accidentally fall
            //This is because the player entity is under the user's head, not their body, so they will full if they just look over
            //TODO improve this so that if the player entity is a certain distance over a gap they fall anyway
            boolean onGround = this.onGround;
            Input input = this.input;
            this.input = sneakingInput;
            sneakingInput.shiftKeyDown = true;

            //We want to move the player entity to the user's position in physical space
            Vec3 wantedMovement = new Vec3(viewPos.x - playerPhysicalPosition.x, 0, viewPos.z - playerPhysicalPosition.z);

            //Counter out the mixin pehuki uses for scaling movement
            float invScale = 1.0f / MCXRScale.getMotionScale(this);

            this.move(MoverType.SELF, wantedMovement.scale(invScale));
            this.input = input;
            this.onGround = onGround;

            double deltaX = this.getX() - oldX;
            double deltaZ = this.getZ() - oldZ;

            //Store the position of the player in physical space
            playerPhysicalPosition.x += deltaX;
            playerPhysicalPosition.z += deltaZ;

            this.xo += deltaX;
            this.zo += deltaZ;
        } else {
            playerPhysicalPosition.zero();
        }
    }

    @Inject(method = "startRiding", at = @At("RETURN"))
    void onStartRiding(Entity entity, boolean force, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            MCXRPlayClient.resetView();
        }
    }


    @Redirect(method="openTextEdit", at=@At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V"))
    public void openSignScreen(Minecraft instance, Screen screen, SignBlockEntity sign) {
        instance.setScreen(new XrSignEditScreen(Component.translatable("Sign"), sign));
    }

}
