package net.sorenon.minexraft.mixin;

import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.Quaternion;
import net.sorenon.minexraft.MineXRaftClient;
import org.lwjgl.openxr.XrQuaternionf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//TODO extend Camera
@Mixin(Camera.class)
public class CameraMixin {

    @Shadow
    @Final
    private Quaternion rotation;

    @Shadow
    @Final
    private Vector3f horizontalPlane;

    @Shadow
    @Final
    private Vector3f verticalPlane;

    @Shadow
    @Final
    private Vector3f diagonalPlane;

    @Inject(method = "setRotation", at = @At("HEAD"), cancellable = true)
    void setRot(float yaw, float pitch, CallbackInfo ci) {
        if (MineXRaftClient.pose != null) {
            XrQuaternionf quat = MineXRaftClient.pose.orientation();
            float invNorm = 1.0F / (quat.x() * quat.x() + quat.y() * quat.y() + quat.z() * quat.z() + quat.w() * quat.w());
            float x = -quat.x() * invNorm;
            float y = -quat.y() * invNorm;
            float z = -quat.z() * invNorm;
            float w = quat.w() * invNorm;

//            this.rotation.set(quat.x(), quat.y(), quat.z(), quat.w());
            this.rotation.set(x, y, z, w);
            this.horizontalPlane.set(0.0F, 0.0F, 1.0F);
            this.horizontalPlane.rotate(this.rotation);
            this.verticalPlane.set(0.0F, 1.0F, 0.0F);
            this.verticalPlane.rotate(this.rotation);
            this.diagonalPlane.set(1.0F, 0.0F, 0.0F);
            this.diagonalPlane.rotate(this.rotation);
            ci.cancel();
        }
    }
}
