package net.sorenon.mcxr.play.mixin.rendering;

import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.play.accessor.Matrix4fExt;
import net.sorenon.mcxr.play.openxr.MCXRGameRenderer;
import net.sorenon.mcxr.play.rendering.RenderPass;
import org.joml.Math;
import org.lwjgl.openxr.XrFovf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Matrix4f.class)
public abstract class Matrix4fMixin implements Matrix4fExt {

    @Unique
    private static final MCXRGameRenderer XR_RENDERER = MCXRPlayClient.MCXR_GAME_RENDERER;

    @Shadow
    protected float m00;

    @Shadow
    protected float m01;

    @Shadow
    protected float m02;

    @Shadow
    protected float m03;

    @Shadow
    protected float m13;

    @Shadow
    protected float m12;

    @Shadow
    protected float m11;

    @Shadow
    protected float m10;

    @Shadow
    protected float m20;

    @Shadow
    protected float m21;

    @Shadow
    protected float m22;

    @Shadow
    protected float m23;

    @Shadow
    protected float m30;

    @Shadow
    protected float m31;

    @Shadow
    protected float m32;

    @Shadow
    protected float m33;

    @Override
    public void setXrProjection(XrFovf fov, float nearZ, float farZ) {
        Minecraft client = Minecraft.getInstance();
        nearZ = MCXRPlayClient.modifyProjectionMatrixDepth(nearZ, client.getCameraEntity(), client.getFrameTime());
        float tanLeft = Math.tan(fov.angleLeft());
        float tanRight = Math.tan(fov.angleRight());
        float tanDown = Math.tan(fov.angleDown());
        float tanUp = Math.tan(fov.angleUp());
        float tanAngleWidth = tanRight - tanLeft;
        float tanAngleHeight = tanUp - tanDown;
        m00 = 2.0f / tanAngleWidth;
        m10 = 0.0f;
        m20 = 0.0f;
        m30 = 0.0f;
        m01 = 0.0f;
        m11 = 2.0f / tanAngleHeight;
        m21 = 0.0f;
        m31 = 0.0f;
        m02 = (tanRight + tanLeft) / tanAngleWidth;
        m12 = (tanUp + tanDown) / tanAngleHeight;
        m22 = -(farZ + nearZ) / (farZ - nearZ);
        m32 = -1.0f;
        m03 = 0.0f;
        m13 = 0.0f;
        m23 = -(farZ * (nearZ + nearZ)) / (farZ - nearZ);
        m33 = 0.0f;
    }

    @Inject(method = "perspective", cancellable = true, at = @At("HEAD"))
    private static void overwriteProjectionMatrix(double fov, float aspectRatio, float cameraDepth, float viewDistance, CallbackInfoReturnable<Matrix4f> cir) {
        if (XR_RENDERER.renderPass instanceof RenderPass.XrWorld renderPass) {
            Matrix4f mat = new Matrix4f();
            ((Matrix4fExt) (Object) mat).setXrProjection(renderPass.fov, cameraDepth, viewDistance);
            cir.setReturnValue(mat);
        }
    }
}
