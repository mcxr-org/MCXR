//package net.sorenon.minexraft.mixin;
//
//import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.render.Camera;
//import net.minecraft.client.util.math.Vector3f;
//import net.minecraft.entity.Entity;
//import net.minecraft.util.math.Quaternion;
//import net.minecraft.util.math.Vec3d;
//import net.minecraft.world.BlockView;
//import net.sorenon.minexraft.MineXRaftClient;
//import net.sorenon.minexraft.mixin.accessor.CameraExt;
//import org.joml.Math;
//import org.lwjgl.openxr.XrQuaternionf;
//import org.lwjgl.openxr.XrVector3f;
//import org.lwjgl.system.CallbackI;
//import org.spongepowered.asm.mixin.Final;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//
//@Mixin(Camera.class)
//public abstract class CameraMixin implements CameraExt {
//
//    @Shadow
//    @Final
//    private Quaternion rotation;
//
//    @Shadow
//    @Final
//    private Vector3f horizontalPlane;
//
//    @Shadow
//    @Final
//    private Vector3f verticalPlane;
//
//    @Shadow
//    @Final
//    private Vector3f diagonalPlane;
//
//    @Shadow
//    private float pitch;
//
//    @Shadow
//    private float yaw;
//
//    @Shadow
//    private Vec3d pos;
//
//    @Shadow
//    protected abstract void setPos(double x, double y, double z);
//
//    @Shadow
//    private Entity focusedEntity;
//
//    @Shadow
//    private boolean ready;
//
//    @Shadow
//    private BlockView area;
//
//    @Shadow
//    private boolean thirdPerson;
//
//    @Shadow
//    private boolean inverseView;
//
//    @Inject(method = "setRotation", at = @At("HEAD"), cancellable = true)
//    void setRot(float yaw, float pitch, CallbackInfo ci) {
////        if (MineXRaftClient.eyePose != null) {
////            XrQuaternionf quat = MineXRaftClient.eyePose.orientation();
////            float invNorm = 1.0F / (quat.x() * quat.x() + quat.y() * quat.y() + quat.z() * quat.z() + quat.w() * quat.w());
////            float x = -quat.x() * invNorm;
////            float y = -quat.y() * invNorm;
////            float z = -quat.z() * invNorm;
////            float w = quat.w() * invNorm;
////
////            this.rotation.set(x, y, z, w);
////            this.horizontalPlane.set(0.0F, 0.0F, 1.0F);
////            this.horizontalPlane.rotate(this.rotation);
////            this.verticalPlane.set(0.0F, 1.0F, 0.0F);
////            this.verticalPlane.rotate(this.rotation);
////            this.diagonalPlane.set(1.0F, 0.0F, 0.0F);
////            this.diagonalPlane.rotate(this.rotation);
////
////            float yaw1 = (float) Math.atan2(2.0D * (double) (x * w - y * z), 1.0D - 2.0D * (double) (x * x + y * y));
////            float pitch2 = (float) Math.asin(2.0D * (double) (x * z + y * w));
////            float roll = (float) Math.atan2(2.0D * (double) (z * w - x * y), 1.0D - 2.0D * (double) (y * y + z * z));
////            yaw = (yaw1);
////            pitch = (-pitch2);
////            ci.cancel();
////        }
//    }
//
//    @Inject(method = "getPos", at = @At("RETURN"), cancellable = true)
//    void pos(CallbackInfoReturnable<Vec3d> cir) {
////        if (MineXRaftClient.eyePose != null) {
////            XrVector3f pos = MineXRaftClient.eyePose.position$();
//////            MineXRaftClient.xrOrigin = cir.getReturnValue().subtract(0, focusedEntity.getStandingEyeHeight(), 0);
////
////            cir.setReturnValue(new Vec3d(pos.x(), pos.y(), pos.z()).add(MineXRaftClient.xrOrigin).add(cir.getReturnValue().subtract(0, focusedEntity.getStandingEyeHeight(), 0)));
////        }
//    }
//
//    @Override
//    public void xr_ready(boolean ready) {
//        this.ready = ready;
//    }
//
//    @Override
//    public void xr_area(BlockView area) {
//        this.area = area;
//    }
//
//    @Override
//    public void xr_focusedEntity(Entity entity) {
//        this.focusedEntity = entity;
//    }
//
//    @Override
//    public void xr_thirdPerson(boolean thirdPerson) {
//        this.thirdPerson = thirdPerson;
//    }
//
//    @Override
//    public void xr_inverseView(boolean inverseView) {
//        this.inverseView = inverseView;
//    }
//
//    @Override
//    public void xr_pitch(float pitch) {
//        this.pitch = pitch;
//    }
//
//    @Override
//    public void xr_yaw(float yaw) {
//        this.yaw = yaw;
//    }
//
//    @Override
//    public Vector3f xr_diagonalPlane() {
//        return this.diagonalPlane;
//    }
//}
