package net.sorenon.minexraft.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.sorenon.minexraft.MineXRaftClient;
import net.sorenon.minexraft.XrCamera;
import net.sorenon.minexraft.accessor.MatAccessor;
import org.lwjgl.opengl.GL11;
import org.lwjgl.openxr.XrPosef;
import org.lwjgl.openxr.XrQuaternionf;
import org.lwjgl.openxr.XrVector2f;
import org.lwjgl.openxr.XrVector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = GameRenderer.class, priority = 10_000)
public abstract class GameRendererMixin {

    @Shadow
    @Final
    private Camera camera;

    @Shadow
    public abstract float getViewDistance();

    @Shadow @Final private MinecraftClient client;

    @Shadow public abstract Matrix4f getBasicProjectionMatrix(Camera camera, float f, boolean bl);

    @Shadow public abstract Camera getCamera();

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

        matrixStack.multiply(((XrCamera) camera).getRawRotationInverted());
    }

    @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;clear(IZ)V", ordinal = 0, shift = At.Shift.BEFORE), method = "render")
    public void guiRenderStart(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        MineXRaftClient.framebufferWidth = 1920;
        MineXRaftClient.framebufferHeight = 1080;
        MineXRaftClient.guiFramebuffer.beginWrite(true);
        GlStateManager.clearColor(0,0,0,0);
        GlStateManager.clear(GL11.GL_COLOR_BUFFER_BIT, MinecraftClient.IS_SYSTEM_MAC);
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void guiRenderEnd(float tickDelta, long startTime, boolean tick, CallbackInfo ci){
        MineXRaftClient.tmpResetSize();
        client.getFramebuffer().beginWrite(true);
    }
}
