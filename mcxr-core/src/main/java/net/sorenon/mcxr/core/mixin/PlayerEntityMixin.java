package net.sorenon.mcxr.core.mixin;

import net.minecraft.block.ShapeContext;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.collection.ReusableStream;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import net.sorenon.mcxr.core.MCXRCore;
import net.sorenon.mcxr.core.Pose;
import net.sorenon.mcxr.core.accessor.PlayerEntityAcc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.Stream;

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
        if (!MCXRCore.getCoreConfig().dynamicPlayerHeight()) {
            return;
        }

        if (headPose != null) {
            final float scale = MCXRCore.getScale(this);

            final float minHeight = 0.5f * scale;
            final float currentHeight = this.getHeight();
            final float wantedHeight = (headPose.pos.y + 0.125f) * scale;
            final float deltaHeight = wantedHeight - currentHeight;

            if (deltaHeight <= 0) {
                cir.setReturnValue(
                        EntityDimensions.changing(0.6F * scale, Math.max(wantedHeight, minHeight))
                );
                return;
            }

            Box currentSize = this.getBoundingBox();
            ShapeContext shapeContext = ShapeContext.of(this);
            VoxelShape voxelShape = this.world.getWorldBorder().asVoxelShape();
            Stream<VoxelShape> stream = VoxelShapes.matchesAnywhere(voxelShape, VoxelShapes.cuboid(currentSize.contract(1.0E-7)), BooleanBiFunction.AND) ? Stream.empty() : Stream.of(voxelShape);
            Stream<VoxelShape> stream2 = this.world.getEntityCollisions(this, currentSize.stretch(0, deltaHeight, 0), entity -> true);
            ReusableStream<VoxelShape> reusableStream = new ReusableStream<>(Stream.concat(stream2, stream));
            double maxDeltaHeight = adjustSingleAxisMovementForCollisions(new Vec3d(0, deltaHeight, 0), currentSize, this.world, shapeContext, reusableStream).y;

            cir.setReturnValue(
                    EntityDimensions.changing(0.6F * scale, Math.max(currentHeight + (float) maxDeltaHeight, minHeight))
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
