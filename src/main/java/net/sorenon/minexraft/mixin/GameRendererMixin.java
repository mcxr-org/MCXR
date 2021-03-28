package net.sorenon.minexraft.mixin;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;
import net.sorenon.minexraft.MineXRaftClient;
import net.sorenon.minexraft.XrCamera;
import net.sorenon.minexraft.accessor.MatAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = GameRenderer.class, priority = 10_000)
public abstract class GameRendererMixin {

    @Shadow
    @Final
    private Camera camera;

    @Shadow
    public abstract float getViewDistance();

    @Redirect(method = "<init>", at = @At(value = "NEW", target = "net/minecraft/client/render/Camera"))
    Camera createCamera() {
        return new XrCamera();
    }

    /**
     * If we are rendering an XR frame
     * return a projection matrix using the XR fov
     */
    @Inject(method = "getBasicProjectionMatrix", at = @At("HEAD"), cancellable = true)
    void getXrProjectionMatrix(Camera camera, float f, boolean bl, CallbackInfoReturnable<Matrix4f> cir) {
        if (MineXRaftClient.fov != null) {
            Matrix4f proj = new Matrix4f();
            proj.loadIdentity();
            //noinspection ConstantConditions
            ((MatAccessor) (Object) proj).createProjectionFov(MineXRaftClient.fov, 0.05F, this.getViewDistance() * 4);

            cir.setReturnValue(proj);
        }
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lnet/minecraft/util/math/Quaternion;)V", ordinal = 2), method = "renderWorld")
    void multiplyPitch(MatrixStack matrixStack, Quaternion pitchQuat) {
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lnet/minecraft/util/math/Quaternion;)V", ordinal = 3), method = "renderWorld")
    void multiplyYaw(MatrixStack matrixStack, Quaternion yawQuat) {
//        matrixStack.multiply(camera.getRotation());

        matrixStack.multiply(((XrCamera)camera).getRawRotationInverted());
    }
}
