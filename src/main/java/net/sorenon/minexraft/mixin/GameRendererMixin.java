package net.sorenon.minexraft.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;
import net.sorenon.minexraft.MineXRaftClient;
import net.sorenon.minexraft.XrCamera;
import net.sorenon.minexraft.accessor.MatAccessor;
import org.lwjgl.opengl.GL11;
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

    @Shadow
    @Final
    private MinecraftClient client;

    @Redirect(method = "<init>", at = @At(value = "NEW", target = "net/minecraft/client/render/Camera"))
    Camera createCamera() {
        return new XrCamera();
    }

    @Redirect(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V"))
    void cancelRenderHand(HeldItemRenderer heldItemRenderer, float tickDelta, MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, ClientPlayerEntity player, int light) {
    }

    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    void cancelBobView(MatrixStack matrixStack, float f, CallbackInfo ci) {
        ci.cancel();
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
        MineXRaftClient.guiFramebuffer.beginWrite(true);
        MineXRaftClient.primaryRenderTarget = MineXRaftClient.guiFramebuffer;
//        if (MinecraftClient.getInstance().currentScreen != null) {
//            MinecraftClient.getInstance().currentScreen.height = MinecraftClient.getInstance().getWindow().getScaledHeight();
//            MinecraftClient.getInstance().currentScreen.width = MinecraftClient.getInstance().getWindow().getScaledWidth();
//        }
        GlStateManager.clearColor(0, 0, 0, 0);
        GlStateManager.clear(GL11.GL_COLOR_BUFFER_BIT, MinecraftClient.IS_SYSTEM_MAC);
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void guiRenderEnd(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        MineXRaftClient.tmpResetSize();
        client.getFramebuffer().beginWrite(true);
    }
}
