package net.sorenon.minexraft.client.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import net.sorenon.minexraft.client.MineXRaftClient;
import org.lwjgl.openxr.XrRect2Di;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GlStateManager.class)
public class GlStateManagerMixin {

    //It works *shrug*
//    @Inject(method = "viewport", at = @At("HEAD"), cancellable = true)
    private static void viewport(int x, int y, int width, int height, CallbackInfo ci) {
        XrRect2Di rect = MineXRaftClient.viewportRect;
//        if (rect != null) {
////            glViewport( TODO
////                    rect.offset().x(),
////                    rect.offset().y(),
////                    rect.extent().width(),
////                    rect.extent().height()
////            );
//            glViewport(
//                    0,
//                    0,
//                    rect.extent().width(),
//                    rect.extent().height()
//            );
//            ci.cancel();
//        }
    }
}
