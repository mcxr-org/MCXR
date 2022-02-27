package net.sorenon.mcxr.core.mixin;

import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sorenon.mcxr.core.MCXRCore;
import net.sorenon.mcxr.core.MCXRScale;
import net.sorenon.mcxr.core.Pose;
import net.sorenon.mcxr.core.accessor.PlayerEntityAcc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerEntityAcc {

    @Unique
    public Pose headPose = null;

    @Unique
    public Pose leftArmPose = null;

    @Unique
    public Pose rightArmPose = null;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    void preTick(CallbackInfo ci) {
        if (this.isXR()) {
            this.refreshDimensions();
        }
    }

    @Inject(method = "getDimensions", at = @At("HEAD"), cancellable = true)
    void overrideDims(net.minecraft.world.entity.Pose _pose, CallbackInfoReturnable<EntityDimensions> cir) {
        if (!MCXRCore.getCoreConfig().dynamicPlayerHeight()) {
            return;
        }

        if (this.isXR()) {
            final float scale = MCXRScale.getScale(this);

            final float minHeight = 0.5f * scale;
            final float currentHeight = this.getBbHeight();
            final float wantedHeight = (headPose.pos.y + 0.125f * scale);
            final float deltaHeight = wantedHeight - currentHeight;

            if (deltaHeight <= 0) {
                cir.setReturnValue(
                        EntityDimensions.scalable(0.6F * scale, Math.max(wantedHeight, minHeight))
                );
                return;
            }

            AABB currentSize = this.getBoundingBox();
            List<VoxelShape> list = this.level.getEntityCollisions(this, currentSize.expandTowards(0, deltaHeight, 0));
            final double maxDeltaHeight = collideBoundingBox(this, new Vec3(0, deltaHeight, 0), currentSize, this.level, list).y;

//            AABB currentSize = this.getBoundingBox();
//            CollisionContext shapeContext = CollisionContext.of(this);
//            VoxelShape voxelShape = this.level.getWorldBorder().getCollisionShape();
//            Stream<VoxelShape> stream = Shapes.joinIsNotEmpty(voxelShape, Shapes.create(currentSize.deflate(1.0E-7)), BooleanOp.AND) ? Stream.empty() : Stream.of(voxelShape);
//            Stream<VoxelShape> stream2 = this.level.getEntityCollisions(this, currentSize.expandTowards(0, deltaHeight, 0), entity -> true);
//            RewindableStream<VoxelShape> reusableStream = new RewindableStream<>(Stream.concat(stream2, stream));
//            double maxDeltaHeight = collideBoundingBox(new Vec3(0, deltaHeight, 0), currentSize, this.level, shapeContext, reusableStream).y;

            cir.setReturnValue(
                    EntityDimensions.scalable(0.6F * scale, Math.max(currentHeight + (float) maxDeltaHeight, minHeight))
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

    @Override
    public Pose getLArmPose() {
        return leftArmPose;
    }

    @Override
    public void markLArm() {
        leftArmPose = new Pose();
    }

    @Override
    public Pose getRArmPose() {
        return rightArmPose;
    }

    @Override
    public void markRArm() {
        rightArmPose = new Pose();
    }

    @Override
    public boolean isXR() {
        return headPose != null;
    }
}
