package net.sorenon.mcxr.core.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.sorenon.mcxr.core.Pose;
import net.sorenon.mcxr.core.accessor.PlayerEntityAcc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import virtuoel.pehkui.api.ScaleType;

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
        if (headPose != null) {
            if (FabricLoader.getInstance().isModLoaded("pehkui")) {
                var scaleData = ScaleType.BASE.getScaleData(this);
                float scale = scaleData.getScale(1);
                cir.setReturnValue(
                        EntityDimensions.changing(0.6F * scale, (headPose.pos.y + 0.125f) * scale)
                );
            } else {
                cir.setReturnValue(
                        EntityDimensions.changing(0.6F, headPose.pos.y + 0.125f)
                );
            }
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
