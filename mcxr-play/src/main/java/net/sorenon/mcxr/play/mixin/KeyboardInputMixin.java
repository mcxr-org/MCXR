package net.sorenon.mcxr.play.mixin;

import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.play.input.XrInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//TODO have a look at https://github.com/LambdAurora/LambdaControls compat
@Mixin(KeyboardInput.class)
public class KeyboardInputMixin extends Input {

    @Inject(method = "tick", at = @At("RETURN"))
    void overwriteMovement(boolean slowDown, CallbackInfo ci) {
        if (!MCXRPlayClient.MCXR_GAME_RENDERER.isXrMode()) return;
        if (MCXRPlayClient.INSTANCE.MCXRGuiManager.isScreenOpen()) return;

        var move = XrInput.vanillaGameplayActionSet.move.currentState;
        this.forwardImpulse = move.y();
        this.leftImpulse = -move.x();

        this.up |= move.y() > 0;
        this.down |= move.y() < 0;
        this.right |= move.x() > 0;
        this.left |= move.y() < 0;

        this.jumping |= XrInput.vanillaGameplayActionSet.jump.currentState;
    }
}
