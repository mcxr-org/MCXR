package net.sorenon.mcxr.play.mixin.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.play.openxr.MCXRGameRenderer;
import net.sorenon.mcxr.play.rendering.MCXRMainTarget;
import net.sorenon.mcxr.play.rendering.MCXRCamera;
import net.sorenon.mcxr.play.rendering.RenderPass;
import net.sorenon.mcxr.play.accessor.Matrix4fExt;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = GameRenderer.class, priority = 10_000)
public abstract class GameRendererMixin {

    @Unique
    private static final MCXRGameRenderer XR_RENDERER = MCXRPlayClient.MCXR_GAME_RENDERER;

    @Shadow
    @Final
    private Camera mainCamera;

    @Shadow public abstract float getRenderDistance();

    @Shadow @Final private Minecraft minecraft;

    @Shadow private boolean renderHand;

    /**
     * Replace the default camera with an XrCamera
     */
    @Redirect(method = "<init>", at = @At(value = "NEW", target = "net/minecraft/client/Camera"))
    Camera createCamera() {
        return new MCXRCamera();
    }

//    @Redirect(method = "renderItemInHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderHandsWithItems(FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/player/LocalPlayer;I)V"))
//    void cancelRenderHand(ItemInHandRenderer heldItemRenderer, float tickDelta, PoseStack matrices, MultiBufferSource.BufferSource vertexConsumers, LocalPlayer player, int light) {
//    }

    @Inject(method = "renderLevel", at = @At("HEAD"))
    void cancelRenderHand(CallbackInfo ci) {
        this.renderHand = !XR_RENDERER.isXrMode();
    }

    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    void cancelBobView(PoseStack matrixStack, float f, CallbackInfo ci) {
        ci.cancel();
    }

    /**
     * Replace the vanilla projection matrix
     */
    @Inject(method = "getProjectionMatrix", at = @At("HEAD"), cancellable = true)
    void getXrProjectionMatrix(double d, CallbackInfoReturnable<Matrix4f> cir) {
        if (XR_RENDERER.renderPass instanceof RenderPass.World renderPass) {
            Matrix4f proj = new Matrix4f();
            proj.setIdentity();
            //noinspection ConstantConditions
            ((Matrix4fExt) (Object) proj).createProjectionFov(renderPass.fov, 0.05F, this.getRenderDistance() * 4);

            cir.setReturnValue(proj);
        }
    }

    @Inject(method = "resize", at = @At("HEAD"))
    void onResized(int i, int j, CallbackInfo ci) {
        MCXRMainTarget MCXRMainTarget = (MCXRMainTarget) minecraft.getMainRenderTarget();
        MCXRMainTarget.gameWidth = i;
        MCXRMainTarget.gameHeight = j;
    }

    /**
     * Rotate the matrix stack using a quaternion rather than pitch and yaw
     */
    @Redirect(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lcom/mojang/math/Quaternion;)V", ordinal = 2), method = "renderLevel")
    void multiplyPitch(PoseStack matrixStack, Quaternion pitchQuat) {
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lcom/mojang/math/Quaternion;)V", ordinal = 3), method = "renderLevel")
    void multiplyYaw(PoseStack matrixStack, Quaternion yawQuat) {
        matrixStack.mulPose(((MCXRCamera) mainCamera).getRawRotationInverted());
    }

    /**
     * If we are doing a gui render pass => return null to skip rendering the world
     */
    @Redirect(at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;level:Lnet/minecraft/client/multiplayer/ClientLevel;", opcode = Opcodes.GETFIELD, ordinal = 0), method = "render")
    public ClientLevel getWorld(Minecraft client) {
        if (XR_RENDERER.renderPass == RenderPass.GUI) {
            return null;
        } else {
            return client.level;
        }
    }

    /**
     * If we are doing a world render pass => return early to skip rendering the gui
     */
    @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;clear(IZ)V", ordinal = 0, shift = At.Shift.BEFORE), method = "render", cancellable = true)
    public void guiRenderStart(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        if (XR_RENDERER.renderPass instanceof RenderPass.World) {
            ci.cancel();
        }
    }
}
