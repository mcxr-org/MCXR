package net.sorenon.mcxr.play.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.world.entity.vehicle.Boat;
import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.play.PlayOptions;
import net.sorenon.mcxr.play.input.XrInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin extends Input {

    @Inject(method = "tick", at = @At("RETURN"))
    void overwriteMovement(boolean slowDown, float f, CallbackInfo ci) {
        if (!MCXRPlayClient.MCXR_GAME_RENDERER.isXrMode()) return;
        if (MCXRPlayClient.INSTANCE.MCXRGuiManager.isScreenOpen()) return;

        var move = XrInput.vanillaGameplayActionSet.move.currentState;

        this.forwardImpulse = move.y();
        this.leftImpulse = -move.x();

        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.getVehicle() instanceof Boat) {
            this.up |= move.y() > 0.5;
            this.down |= move.y() < -0.5;
            this.right |= move.x() > 0.5;
            this.left |= move.x() < -0.5;
        } else {
            this.up |= move.y() > 0;
            this.down |= move.y() < 0;
            this.right |= move.x() > 0;
            this.left |= move.x() < 0;
        }

        this.jumping |= XrInput.vanillaGameplayActionSet.jump.currentState;

        if(slowDown) {
            this.forwardImpulse *= f;
            this.leftImpulse *= f;
        }
    }
}
