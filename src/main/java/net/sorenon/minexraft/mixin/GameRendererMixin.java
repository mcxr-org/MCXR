package net.sorenon.minexraft.mixin;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import net.sorenon.minexraft.HelloOpenXR;
import net.sorenon.minexraft.MineXRaftClient;
import net.sorenon.minexraft.accessor.MatAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow
    private float zoom;

    @Shadow
    private float zoomX;

    @Shadow
    private float zoomY;

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
}
