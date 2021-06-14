package net.sorenon.mcxr.play.client.mixin.flatgui;

import net.minecraft.client.util.InputUtil;
import net.sorenon.mcxr.play.client.MCXRPlayClient;
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
            if (MCXRPlayClient.INSTANCE.flatGuiManager.isScreenOpen()) {
                if (MCXRPlayClient.flatGuiActionSet.quickMoveState.currentState()) {
                    cir.setReturnValue(true);
                }
            }
        }
    }
}
