package net.sorenon.mcxr.play.compat.iris.mixin;

import com.mojang.math.Matrix4f;
import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.play.input.ControllerPoses;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("UnusedMixin")
@Mixin(targets = "net/coderbot/iris/shadow/ShadowMatrices", remap = false)
@Pseudo
public class MixinCameraUniforms {

//    @SuppressWarnings("UnresolvedMixinReference")
//    @Redirect(method = "getUnshiftedCameraPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;getPosition()Lnet/minecraft/world/phys/Vec3;", remap = true))
//    private static Vec3 mcxr$getPosition(Camera camera) {
//        return JOMLUtil.convert(MCXRPlayClient.viewSpacePoses.getGamePose().getPos());
//    }

    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "snapModelViewToGrid", at = @At("HEAD"), cancellable = true)
    private static void mcxr$snapModelViewToGrid(Matrix4f target,
                                                 float shadowIntervalSize,
                                                 double cameraX,
                                                 double cameraY,
                                                 double cameraZ,
                                                 CallbackInfo ci) {
//        System.out.println("HOWDY");
        if (MCXRPlayClient.MCXR_GAME_RENDERER.isXrMode()) {
            ci.cancel();
            var views = MCXRPlayClient.OPEN_XR_STATE.session.viewBuffer;

            ControllerPoses left = new ControllerPoses();
            ControllerPoses right = new ControllerPoses();

            left.updatePhysicalPose(views.get(0).pose(), MCXRPlayClient.stageTurn, MCXRPlayClient.getCameraScale());
            left.updateGamePose(MCXRPlayClient.xrOrigin);
            right.updatePhysicalPose(views.get(1).pose(), MCXRPlayClient.stageTurn, MCXRPlayClient.getCameraScale());
            right.updateGamePose(MCXRPlayClient.xrOrigin);
            var leftPos = left.getMinecraftPose().getPos();
            var rightPos = right.getMinecraftPose().getPos();

            float x;
            if (Math.abs(leftPos.x() - (leftPos.x() % shadowIntervalSize)) > Math.abs(rightPos.x() - (rightPos.x() % shadowIntervalSize))) {
                x = leftPos.x();
            } else {
                x = rightPos.x();
            }
            float y;
            if (Math.abs(leftPos.y() - (leftPos.y() % shadowIntervalSize)) > Math.abs(rightPos.y() - (rightPos.y() % shadowIntervalSize))) {
                y = leftPos.y();
            } else {
                y = rightPos.y();
            }
            float z;
            if (Math.abs(leftPos.z() - (leftPos.z() % shadowIntervalSize)) > Math.abs(rightPos.z() - (rightPos.z() % shadowIntervalSize))) {
                z = leftPos.z();
            } else {
                z = rightPos.z();
            }

            float deltaX = (x % shadowIntervalSize) - x;
            float deltaY = (y % shadowIntervalSize) - y;
            float deltaZ = (z % shadowIntervalSize) - z;

            float offsetX = (float) cameraX + deltaX;
            float offsetY = (float) cameraY + deltaY;
            float offsetZ = (float) cameraZ + deltaZ;

            // Calculate where we are within each grid "cell"
            // These values will be in the range of (-shadowIntervalSize, shadowIntervalSize)
            //
            // It looks like it's intended for these to be within the range [0, shadowIntervalSize), however since the
            // expression (-2.0f % 32.0f) returns -2.0f, negative inputs will result in negative outputs.
//            float offsetX = (float) cameraX % shadowIntervalSize;
//            float offsetY = (float) cameraY % shadowIntervalSize;
//            float offsetZ = (float) cameraZ % shadowIntervalSize;

            // Halve the size of each grid cell in order to move to the center of it.
            float halfIntervalSize = shadowIntervalSize / 2.0f;

            // Shift by -halfIntervalSize
            //
            // It's clear that the intent of the algorithm was to place the values into the range:
            // [-shadowIntervalSize/2, shadowIntervalSize), however due to the previously-mentioned behavior with negatives,
            // it's possible that values will lie in the range (-3shadowIntervalSize/2, shadowIntervalSize/2).
            offsetX -= halfIntervalSize;
            offsetY -= halfIntervalSize;
            offsetZ -= halfIntervalSize;

            target.multiply(Matrix4f.createTranslateMatrix(offsetX, offsetY, offsetZ));
        }
    }
}
