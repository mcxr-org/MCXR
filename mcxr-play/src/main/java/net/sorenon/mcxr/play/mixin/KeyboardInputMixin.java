package net.sorenon.mcxr.play.mixin;

import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.play.input.XrInput;
import org.joml.Vector2f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//TODO have a look at https://github.com/LambdAurora/LambdaControls compat
@Mixin(KeyboardInput.class)
public class KeyboardInputMixin extends Input {

    @Inject(method = "tick", at = @At("RETURN"))
    void overwriteMovement(boolean slowDown, CallbackInfo ci) {
        if (MCXRPlayClient.INSTANCE.MCXRGuiManager.isScreenOpen()) return;

        var thumbstick = XrInput.vanillaGameplayActionSet.move.currentState;
        this.forwardImpulse = thumbstick.y();
        this.leftImpulse = -thumbstick.x();

        this.up = thumbstick.y() > 0;
        this.down = thumbstick.y() < 0;
        this.right = thumbstick.x() > 0;
        this.left = thumbstick.y() < 0;

        this.jumping |= XrInput.vanillaGameplayActionSet.jump.currentState;
        if(slowDown) {
            this.forwardImpulse = thumbstick.y() * 0.3f;
            this.leftImpulse = -thumbstick.x() * 0.3f;
        }
    }
}
