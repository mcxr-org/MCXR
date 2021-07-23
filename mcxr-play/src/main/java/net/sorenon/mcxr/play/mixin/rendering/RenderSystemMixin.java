package net.sorenon.mcxr.play.mixin.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RenderSystem.class)
public class RenderSystemMixin {

    /**
     * @see WindowMixin#onGlfwCreateWindow
     * GLFW has been set up as single buffer to avoid vsync, so calling glfwSwapBuffers does nothing
     * glFlush must be called instead
     */
//    @Redirect(method = "flipFrame", at = @At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwSwapBuffers(J)V"))
//    private static void glfwSwapBuffers(long window) {
//        GL11.glFlush();
//    }
}
