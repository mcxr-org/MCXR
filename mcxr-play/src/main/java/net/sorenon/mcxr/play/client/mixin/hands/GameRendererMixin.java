package net.sorenon.mcxr.play.client.mixin.hands;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.RaycastContext;
import net.sorenon.mcxr.core.JOMLUtil;
import net.sorenon.mcxr.play.client.MCXRPlayClient;
import net.sorenon.mcxr.core.Pose;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "updateTargetedEntity", at = @At(value = "INVOKE_ASSIGN", shift = At.Shift.AFTER, target = "Lnet/minecraft/entity/Entity;raycast(DFZ)Lnet/minecraft/util/hit/HitResult;"))
    private void overrideEntity$raycast(float tickDelta, CallbackInfo ci) {
        if (MCXRPlayClient.isXrMode()) {
            Entity entity = this.client.getCameraEntity();
            int hand = 1;
            Pose pose = MCXRPlayClient.handsActionSet.gripPoses[hand].getGamePose();
            Vec3d pos = JOMLUtil.convert(pose.getPos());
            Vector3f dir1 = pose.getOrientation().rotateX((float) Math.toRadians(MCXRPlayClient.handPitchAdjust), new Quaternionf()).transform(new Vector3f(0, -1, 0));
            Vec3d dir = new Vec3d(dir1.x, dir1.y, dir1.z);
            Vec3d endPos = pos.add(dir.multiply(this.client.interactionManager.getReachDistance()));
            this.client.crosshairTarget = entity.world.raycast(new RaycastContext(pos, endPos, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity));
        }
    }

    @ModifyVariable(method = "updateTargetedEntity", ordinal = 0,
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/Entity;getCameraPosVec(F)Lnet/minecraft/util/math/Vec3d;")
    )
    private Vec3d alterStartPosVec(Vec3d value) {
        if (MCXRPlayClient.isXrMode()) {
            int hand = 1;
            Pose pose = MCXRPlayClient.handsActionSet.gripPoses[hand].getGamePose();
            return JOMLUtil.convert(pose.getPos());
        } else {
            return value;
        }
    }

    @ModifyVariable(method = "updateTargetedEntity", ordinal = 1,
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/Entity;getRotationVec(F)Lnet/minecraft/util/math/Vec3d;")
    )
    private Vec3d alterDirVec(Vec3d value) {
        if (MCXRPlayClient.isXrMode()) {
            int hand = 1;
            Pose pose = MCXRPlayClient.handsActionSet.gripPoses[hand].getGamePose();
            return JOMLUtil.convert(
                    pose.getOrientation()
                            .rotateX((float) Math.toRadians(MCXRPlayClient.handPitchAdjust), new Quaternionf())
                            .transform(new Vector3f(0, -1, 0))
            );
        } else {
            return value;
        }
    }
}
