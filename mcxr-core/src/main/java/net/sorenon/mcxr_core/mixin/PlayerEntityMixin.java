package net.sorenon.mcxr_core.mixin;

import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.sorenon.mcxr_core.MCXRCore;
import net.sorenon.mcxr_core.Pose;
import net.sorenon.mcxr_core.accessor.PlayerEntityAcc;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerEntityAcc {

    @Unique
    public Pose headPose = null;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    void preTick(CallbackInfo ci) {
        this.calculateDimensions();
    }

    @Inject(method = "getDimensions", at = @At("HEAD"), cancellable = true)
    void overrideDims(EntityPose _pose, CallbackInfoReturnable<EntityDimensions> cir) {
        //TODO
        //		info.setReturnValue(info.getReturnValue().scaled(ScaleUtils.getWidthScale((Entity) (Object) this), ScaleUtils.getHeightScale((Entity) (Object) this)));

//        Pose pose = MCXRCore.pose;
//        Vector3f headHalfBounds = new Vector3f(0, 0.125f, 0);
//        cir.setReturnValue(
//                EntityDimensions.changing(0.6F, pose.pos.y + Math.abs(pose.orientation.transform(headHalfBounds).y))
//        );

        if (headPose != null) {
            cir.setReturnValue(
                    EntityDimensions.changing(0.6F, headPose.pos.y + 0.125f)
            );
        }
    }

    @Override
    public Pose getHeadPose() {
        return headPose;
    }

    @Override
    public void markVR() {
        headPose = new Pose();
    }
}
