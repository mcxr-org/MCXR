package net.sorenon.minexraft.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.sorenon.minexraft.MineXRaftClient;
import net.sorenon.minexraft.accessor.CameraExt;
import org.lwjgl.openxr.XrQuaternionf;
import org.lwjgl.openxr.XrVector3f;
import org.lwjgl.system.CallbackI;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public abstract class CameraMixin implements CameraExt {

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

    @Shadow private float pitch;

    @Shadow private float yaw;

    @Shadow private Vec3d pos;

    @Shadow protected abstract void setPos(double x, double y, double z);

    @Shadow private Entity focusedEntity;

    @Inject(method = "setRotation", at = @At("HEAD"), cancellable = true)
    void setRot(float yaw, float pitch, CallbackInfo ci) {
        if (MineXRaftClient.eyePose != null) {
            XrQuaternionf quat = MineXRaftClient.eyePose.orientation();
            float invNorm = 1.0F / (quat.x() * quat.x() + quat.y() * quat.y() + quat.z() * quat.z() + quat.w() * quat.w());
            float x = -quat.x() * invNorm;
            float y = -quat.y() * invNorm;
            float z = -quat.z() * invNorm;
            float w = quat.w() * invNorm;

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

    @Inject(method = "getPos", at = @At("RETURN"), cancellable = true)
    void pos(CallbackInfoReturnable<Vec3d> cir){
        if (MineXRaftClient.eyePose != null) {
            XrVector3f pos = MineXRaftClient.eyePose.position$();
            MineXRaftClient.xrOrigin = cir.getReturnValue().subtract(0, focusedEntity.getStandingEyeHeight(), 0);

//            cir.setReturnValue(cir.getReturnValue().add(pos.x(), pos.y() - focusedEntity.getStandingEyeHeight(), pos.z()));
            cir.setReturnValue(new Vec3d(pos.x(), pos.y(), pos.z()).add(MineXRaftClient.xrOrigin));
        }
    }

    @Override
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    @Override
    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    @Override
    public Vector3f getDiagonalPlane() {
        return diagonalPlane;
    }
}
