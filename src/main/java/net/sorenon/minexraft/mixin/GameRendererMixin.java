package net.sorenon.minexraft.mixin;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;
import net.sorenon.minexraft.MineXRaftClient;
import net.sorenon.minexraft.accessor.MatAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.sorenon.minexraft.MineXRaftClient.viewIndex;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_SRGB;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow @Final private Camera camera;

    @Inject(method = "getBasicProjectionMatrix", at = @At("HEAD"), cancellable = true)
    void proj(Camera camera, float f, boolean bl, CallbackInfoReturnable<Matrix4f> cir) {
        if (MineXRaftClient.fov != null) {
            MatrixStack matrixStack = new MatrixStack();
            matrixStack.peek().getModel().loadIdentity();

            net.minecraft.util.math.Matrix4f proj = new net.minecraft.util.math.Matrix4f();
            ((MatAccessor) (Object) proj).proj();

            matrixStack.peek().getModel().multiply(proj);
            cir.setReturnValue(matrixStack.peek().getModel());
        }
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lnet/minecraft/util/math/Quaternion;)V", ordinal = 2) , method = "renderWorld")
    void multiplyPitch(MatrixStack matrixStack, Quaternion pitchQuat){

    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lnet/minecraft/util/math/Quaternion;)V", ordinal = 3) , method = "renderWorld")
    void multiplyYaw(MatrixStack matrixStack, Quaternion yawQuat){
        matrixStack.multiply(camera.getRotation());
    }
}
