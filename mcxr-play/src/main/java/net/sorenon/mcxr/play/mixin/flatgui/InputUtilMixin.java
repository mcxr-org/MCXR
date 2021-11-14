package net.sorenon.mcxr.play.mixin.flatgui;

import com.mojang.blaze3d.platform.InputConstants;
import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.play.input.XrInput;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InputConstants.class)
public class InputUtilMixin {

    @Inject(method = "isKeyDown", at = @At("HEAD"), cancellable = true)
    private static void isKeyPressed(long handle, int code, CallbackInfoReturnable<Boolean> cir) {
        if (code == GLFW.GLFW_KEY_LEFT_SHIFT || code == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            if (MCXRPlayClient.INSTANCE.flatGuiManager.isScreenOpen()) {
                if (XrInput.guiActionSet.quickMove.currentState) {
                    cir.setReturnValue(true);
                }
            }
        }
    }
}
