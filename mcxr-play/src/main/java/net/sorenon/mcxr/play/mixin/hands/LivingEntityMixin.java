package net.sorenon.mcxr.play.mixin.hands;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.sorenon.mcxr.play.MCXRPlayClient;
import org.joml.Math;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    //TODO figure out if this should just be the direction of the player's head

    @Shadow
    public abstract boolean isAlive();

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    private boolean isActive() {
        //noinspection ConstantConditions
        return MCXRPlayClient.MCXR_GAME_RENDERER.isXrMode() && (LivingEntity) (Object) this instanceof LocalPlayer;
    }

    @Redirect(method = "handleRelativeFrictionAndCalculateMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;moveRelative(FLnet/minecraft/world/phys/Vec3;)V"))
    public void moveRelativeLand(LivingEntity instance, float speed, Vec3 move) {
        if (isActive()) {
            Optional<Float> val = MCXRPlayClient.walkDirection.getMCYaw();
            if (val.isPresent()) {
                Vec3 inputVector = getInputVector(move, speed, val.get());
                this.setDeltaMovement(this.getDeltaMovement().add(inputVector));
                return;
            }
        }
        this.moveRelative(speed, move);
    }

    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;moveRelative(FLnet/minecraft/world/phys/Vec3;)V"))
    public void moveRelativeLiquid(LivingEntity instance, float speed, Vec3 move) {
        if (isActive() && this.isSwimming()) {
            Optional<Float> val = MCXRPlayClient.swimDirection.getMCYaw();
            if (val.isPresent()) {
                Vec3 inputVector = getInputVector(move, speed, val.get());
                this.setDeltaMovement(this.getDeltaMovement().add(inputVector));
            } else {
                this.moveRelative(speed, move);
            }
        } else {
            this.moveRelativeLand(instance, speed, move);
        }
    }

    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getLookAngle()Lnet/minecraft/world/phys/Vec3;"))
    public Vec3 getLookAngleFlying(LivingEntity instance) {
        if (isActive()) {
            Vec3 result = MCXRPlayClient.flyDirection.getLookDirection();
            if (result != null) {
                return result;
            }
        }
        return this.calculateViewVector(this.getXRot(), this.getYRot());
    }

    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getXRot()F"))
    public float getXRotFlying(LivingEntity instance) {
        if (isActive()) {
            Optional<Float> val = MCXRPlayClient.flyDirection.getMCPitch();
            if (val.isPresent()) {
                return val.get();
            }
        }
        return this.getXRot();
    }

    @Unique
    private static Vec3 getInputVector(Vec3 vec3, float f, float g) {
        double d = vec3.lengthSqr();
        if (d < 1.0E-7) {
            return Vec3.ZERO;
        } else {
            Vec3 vec32 = (d > 1.0 ? vec3.normalize() : vec3).scale((double) f);
            float h = Mth.sin(g * (float) (Math.PI / 180.0));
            float i = Mth.cos(g * (float) (Math.PI / 180.0));
            return new Vec3(vec32.x * (double) i - vec32.z * (double) h, vec32.y, vec32.z * (double) i + vec32.x * (double) h);
        }
    }
}
