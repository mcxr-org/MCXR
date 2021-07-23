package net.sorenon.mcxr.play.mixin;

import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.sorenon.mcxr.play.MCXRPlayClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//TODO have a look at https://github.com/LambdAurora/LambdaControls compat
@Mixin(KeyboardInput.class)
public class VrKeyboardInputMixin extends Input {

    @Inject(method = "tick", at = @At("RETURN"))
    void overwriteMovement(boolean slowDown, CallbackInfo ci) {
        if (MCXRPlayClient.INSTANCE.flatGuiManager.isScreenOpen()) return;

        var thumbstick = MCXRPlayClient.vanillaGameplayActionSet.move.currentState;
        this.movementForward = thumbstick.y();
        this.movementSideways = -thumbstick.x();

        this.pressingForward = thumbstick.y() > 0;
        this.pressingBack = thumbstick.y() < 0;
        this.pressingRight = thumbstick.x() > 0;
        this.pressingLeft = thumbstick.y() < 0;

        this.jumping = MCXRPlayClient.vanillaGameplayActionSet.jump.currentState;
    }
}
