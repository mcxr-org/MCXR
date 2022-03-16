package net.sorenon.mcxr.core.mixin;

import net.minecraft.world.InteractionHand;
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
import net.sorenon.mcxr.core.accessor.PlayerExt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = Player.class, priority = 10_000 /*Pehuki*/)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerExt {

    @Unique
    public boolean isXr = false;

    @Unique
    public Pose headPose = new Pose();

    @Unique
    public Pose leftHandPose = new Pose();

    @Unique
    public Pose rightHandPose = new Pose();

    @Unique
    public ThreadLocal<InteractionHand> overrideTransform = ThreadLocal.withInitial(() -> null);

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    void preTick(CallbackInfo ci) {
        if (this.isXR()) {
            this.refreshDimensions();
        }
    }

    @Inject(method = "getDimensions", at = @At("RETURN"), cancellable = true)
    void overrideDims(net.minecraft.world.entity.Pose _pose, CallbackInfoReturnable<EntityDimensions> cir) {
        boolean dynamicHeight = MCXRCore.getCoreConfig().dynamicPlayerHeight();
        boolean thinnerBB = MCXRCore.getCoreConfig().thinnerPlayerBoundingBox();
        if (!dynamicHeight && !thinnerBB) {
            return;
        }

        if (this.isXR()) {
            final float scale = MCXRScale.getScale(this);

            float width = cir.getReturnValue().width;
            if (thinnerBB) {
                width = 0.4f;
            }

            if (dynamicHeight) {
                final float minHeight = 0.5f * scale;
                final float currentHeight = this.getBbHeight();
                final float wantedHeight = (headPose.pos.y - (float) this.position().y + 0.125f * scale);
                final float deltaHeight = wantedHeight - currentHeight;

                if (deltaHeight <= 0) {
                    cir.setReturnValue(
                            EntityDimensions.scalable(width * scale, Math.max(wantedHeight, minHeight))
                    );
                    return;
                }

                AABB currentSize = this.getBoundingBox();
                List<VoxelShape> list = this.level.getEntityCollisions(this, currentSize.expandTowards(0, deltaHeight, 0));
                final double maxDeltaHeight = collideBoundingBox(this, new Vec3(0, deltaHeight, 0), currentSize, this.level, list).y;

                cir.setReturnValue(
                        EntityDimensions.scalable(width * scale, Math.max(currentHeight + (float) maxDeltaHeight, minHeight))
                );
            } else {
                cir.setReturnValue(
                        EntityDimensions.scalable(width * scale, cir.getReturnValue().height)
                );
            }
        }
    }

    @Override
    public Pose getHeadPose() {
        return headPose;
    }

    @Override
    public Pose getLeftHandPose() {
        return leftHandPose;
    }

    @Override
    public Pose getRightHandPose() {
        return rightHandPose;
    }

    @Override
    public void setIsXr(boolean isXr) {
        this.isXr = isXr;
    }

    @Override
    public boolean isXR() {
        return isXr;
    }

    @Override
    public ThreadLocal<InteractionHand> getOverrideTransform() {
        return this.overrideTransform;
    };
}
