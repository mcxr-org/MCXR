package net.sorenon.mcxr.play.client.mixin.rendering;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;
import net.sorenon.mcxr.play.client.MCXRPlayClient;
import net.sorenon.mcxr.play.client.rendering.MainRenderTarget;
import net.sorenon.mcxr.play.client.rendering.XrCamera;
import net.sorenon.mcxr.play.client.rendering.RenderPass;
import net.sorenon.mcxr.play.client.accessor.Matrix4fExt;
import org.objectweb.asm.Opcodes;
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

    /**
     * Replace the default camera with an XrCamera
     */
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
     * Replace the vanilla projection matrix
     */
    @Inject(method = "getBasicProjectionMatrix", at = @At("HEAD"), cancellable = true)
    void getXrProjectionMatrix(double d, CallbackInfoReturnable<Matrix4f> cir) {
        if (MCXRPlayClient.renderPass != RenderPass.VANILLA && MCXRPlayClient.fov != null) {
            Matrix4f proj = new Matrix4f();
            proj.loadIdentity();
            //noinspection ConstantConditions
            ((Matrix4fExt) (Object) proj).createProjectionFov(MCXRPlayClient.fov, 0.05F, this.getViewDistance() * 4);

            cir.setReturnValue(proj);
        }
    }

    @Inject(method = "onResized", at = @At("HEAD"))
    void onResized(int i, int j, CallbackInfo ci) {
        MainRenderTarget mainRenderTarget = (MainRenderTarget) client.getFramebuffer();
        mainRenderTarget.gameWidth = i;
        mainRenderTarget.gameHeight = j;
    }

    /**
     * Rotate the matrix stack using a quaternion rather than pitch and yaw
     */
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lnet/minecraft/util/math/Quaternion;)V", ordinal = 2), method = "renderWorld")
    void multiplyPitch(MatrixStack matrixStack, Quaternion pitchQuat) {
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lnet/minecraft/util/math/Quaternion;)V", ordinal = 3), method = "renderWorld")
    void multiplyYaw(MatrixStack matrixStack, Quaternion yawQuat) {
        matrixStack.multiply(((XrCamera) camera).getRawRotationInverted());
    }

    /**
     * If we are doing a gui render pass => return null to skip rendering the world
     */
    @Redirect(at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;world:Lnet/minecraft/client/world/ClientWorld;", opcode = Opcodes.GETFIELD, ordinal = 0), method = "render")
    public ClientWorld getWorld(MinecraftClient client) {
        if (MCXRPlayClient.renderPass == RenderPass.GUI) {
            return null;
        } else {
            return client.world;
        }
    }

    /**
     * If we are doing a world render pass, skip rendering the gui
     */
    @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;clear(IZ)V", ordinal = 0, shift = At.Shift.BEFORE), method = "render", cancellable = true)
    public void guiRenderStart(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        if (MCXRPlayClient.renderPass == RenderPass.WORLD) {
            ci.cancel();
        }
    }
}
