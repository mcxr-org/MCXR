package net.sorenon.minexraft.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.sorenon.minexraft.MineXRaftClient;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL21;
import org.lwjgl.openxr.XrRect2Di;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.glViewport;

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
