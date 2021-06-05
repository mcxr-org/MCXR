package net.sorenon.minexraft.client.mixin.flatgui;

import net.minecraft.client.util.InputUtil;
import net.sorenon.minexraft.client.MineXRaftClient;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InputUtil.class)
public class InputUtilMixin {

    @Inject(method = "isKeyPressed", at = @At("HEAD"), cancellable = true)
    private static void isKeyPressed(long handle, int code, CallbackInfoReturnable<Boolean> cir) {
        if (code == GLFW.GLFW_KEY_LEFT_SHIFT || code == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            if (MineXRaftClient.INSTANCE.flatGuiManager.isScreenOpen()) {
                if (MineXRaftClient.flatGuiActionSet.quickMoveState.currentState()) {
                    cir.setReturnValue(true);
                }
            }
        }
    }
}
