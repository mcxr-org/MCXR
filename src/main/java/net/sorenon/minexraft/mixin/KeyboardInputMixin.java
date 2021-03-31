package net.sorenon.minexraft.mixin;

import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.sorenon.minexraft.MineXRaftClient;
import org.joml.Quaternionf;
import org.lwjgl.openxr.XrVector2f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin extends Input {

    @Inject(method = "tick", at = @At("RETURN"))
    void mv(boolean slowDown, CallbackInfo ci) {
        int offHand = 0;
        int mainHand = 1;
        XrVector2f thumbstick = MineXRaftClient.OPEN_XR.inputState.handThumbstick[offHand];
        this.movementForward = thumbstick.y();
        this.movementSideways = -thumbstick.x();

        this.pressingForward = thumbstick.y() > 0;
        this.pressingBack = thumbstick.y() < 0;
        this.pressingRight = thumbstick.x() > 0;
        this.pressingLeft = thumbstick.y() < 0;

        this.jumping = MineXRaftClient.OPEN_XR.inputState.aDown[mainHand];
    }
}
