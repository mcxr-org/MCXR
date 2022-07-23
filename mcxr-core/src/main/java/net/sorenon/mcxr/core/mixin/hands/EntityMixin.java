package net.sorenon.mcxr.core.mixin.hands;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.sorenon.mcxr.core.JOMLUtil;
import net.sorenon.mcxr.core.Pose;
import net.sorenon.mcxr.core.accessor.PlayerExt;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public abstract Vec3 position();

    @Shadow
    public abstract float getEyeHeight();

    @Shadow private Vec3 position;

    @Inject(method = "getEyeY", at = @At("HEAD"), cancellable = true)
    void overrideEyeY(CallbackInfoReturnable<Double> cir) {
        if (this instanceof PlayerExt playerExt && playerExt.getOverrideTransform().get() != null) {
            cir.setReturnValue(this.position().y + this.getEyeHeight());
        }
    }

    @Inject(method = "getEyeHeight()F", at = @At("HEAD"), cancellable = true)
    void overrideEyeHeight(CallbackInfoReturnable<Float> cir) {
        if (this instanceof PlayerExt playerExt && playerExt.getOverrideTransform().get() != null) {
            cir.setReturnValue(0.1F);
        }
    }

    @Inject(method = "position", at = @At("HEAD"), cancellable = true)
    void overridePosition(CallbackInfoReturnable<Vec3> cir) {
        if (this instanceof PlayerExt playerExt && playerExt.getOverrideTransform().get() != null) {
            cir.setReturnValue(JOMLUtil.convert(playerExt.getPoseForArm(playerExt.getOverrideTransform().get()).getPos()).add(this.position));
        }
    }

    @Inject(method = "getX()D", at = @At("HEAD"), cancellable = true)
    void overrideXPosition(CallbackInfoReturnable<Double> cir) {
        if (this instanceof PlayerExt playerExt && playerExt.getOverrideTransform().get() != null) {
            cir.setReturnValue(this.position().x);
        }
    }

    @Inject(method = "getY()D", at = @At("HEAD"), cancellable = true)
    void overrideYPosition(CallbackInfoReturnable<Double> cir) {
        if (this instanceof PlayerExt playerExt && playerExt.getOverrideTransform().get() != null) {
            cir.setReturnValue(this.position().y);
        }
    }

    @Inject(method = "getZ()D", at = @At("HEAD"), cancellable = true)
    void overrideZPosition(CallbackInfoReturnable<Double> cir) {
        if (this instanceof PlayerExt playerExt && playerExt.getOverrideTransform().get() != null) {
            cir.setReturnValue(this.position().z);
        }
    }

    @Inject(method = "getXRot", at = @At("HEAD"), cancellable = true)
    void overrideXRot(CallbackInfoReturnable<Float> cir) {
        if (this instanceof PlayerExt playerExt && playerExt.getOverrideTransform().get() != null) {
            cir.setReturnValue(Pose.getMCPitch(playerExt.getPoseForArm(playerExt.getOverrideTransform().get()).getOrientation(), new Vector3f(0, -1, 0)));
        }
    }

    @Inject(method = "getYRot", at = @At("HEAD"), cancellable = true)
    void overrideYRot(CallbackInfoReturnable<Float> cir) {
        if (this instanceof PlayerExt playerExt && playerExt.getOverrideTransform() != null && playerExt.getOverrideTransform().get() != null) {
            cir.setReturnValue(Pose.getMCYaw(playerExt.getPoseForArm(playerExt.getOverrideTransform().get()).getOrientation(), new Vector3f(0, -1, 0)));
        }
    }

    @Inject(method = "getUpVector", at = @At("HEAD"), cancellable = true)
    void overrideUpVector(float f, CallbackInfoReturnable<Vec3> cir) {
        if (this instanceof PlayerExt playerExt && playerExt.getOverrideTransform().get() != null) {
            cir.setReturnValue(JOMLUtil.convert(playerExt.getPoseForArm(playerExt.getOverrideTransform().get()).getOrientation().transform(new Vector3f(0, 0, 1))));
        }
    }

    @Inject(method = "getViewVector", at = @At("HEAD"), cancellable = true)
    void overrideViewVector(float f, CallbackInfoReturnable<Vec3> cir) {
        if (this instanceof PlayerExt playerExt && playerExt.getOverrideTransform().get() != null) {
            cir.setReturnValue(JOMLUtil.convert(playerExt.getPoseForArm(playerExt.getOverrideTransform().get()).getOrientation().transform(new Vector3f(0, -1, 0))));
        }
    }
}
