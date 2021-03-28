//package net.sorenon.minexraft.mixin;
//
//import net.minecraft.entity.Entity;
//import net.sorenon.minexraft.mixin.accessor.EntityExt;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//
//@Mixin(Entity.class)
//public class EntityMixin implements EntityExt {
//    @Shadow
//    public float pitch;
//
//    @Shadow
//    public float yaw;
//
//    @Shadow
//    public float prevPitch;
//
//    @Shadow
//    public float prevYaw;
//
//    @Override
//    public float xr_pitch() {
//        return pitch;
//    }
//
//    @Override
//    public float xr_yaw() {
//        return yaw;
//    }
//
//    @Override
//    public float xr_prevPitch() {
//        return prevPitch;
//    }
//
//    @Override
//    public float xr_prevYaw() {
//        return prevYaw;
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
//    public void xr_prevPitch(float prevPitch) {
//        this.prevPitch = prevPitch;
//    }
//
//    @Override
//    public void xr_prevYaw(float prevYaw) {
//        this.prevYaw = prevYaw;
//    }
//}
