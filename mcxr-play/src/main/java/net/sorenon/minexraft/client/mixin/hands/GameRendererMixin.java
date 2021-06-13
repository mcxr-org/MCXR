package net.sorenon.minexraft.client.mixin.hands;

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
import net.sorenon.minexraft.JOMLUtil;
import net.sorenon.minexraft.client.MineXRaftClient;
import net.sorenon.minexraft.client.Pose;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    /**
     * @author Sorenon
     * TODO split this up so we aren't overwriting
     */
    @Overwrite()
    public void updateTargetedEntity(float tickDelta) {
        Entity entity = this.client.getCameraEntity();
        if (entity != null) {
            if (this.client.world != null) {
                this.client.getProfiler().push("pick");
                this.client.targetedEntity = null;
                double d = this.client.interactionManager.getReachDistance();
//                this.client.crosshairTarget = entity.raycast(d, tickDelta, false);
                int hand = 1;
                Pose pose = MineXRaftClient.handsActionSet.gripPoses[hand].getGamePose();
//                Vec3d pos = new Vec3d(MathHelper.lerp(tickDelta, entity.prevX, entity.getX()) + pose.getPos().x + MineXRaftClient.xrOffset.x,
//                        MathHelper.lerp(tickDelta, entity.prevY, entity.getY()) + pose.getPos().y + MineXRaftClient.xrOffset.y,
//                        MathHelper.lerp(tickDelta, entity.prevZ, entity.getZ()) + pose.getPos().z + MineXRaftClient.xrOffset.z);
                Vec3d pos = JOMLUtil.convert(pose.getPos());
                Vector3f dir1 = pose.getOrientation().rotateX((float) Math.toRadians(MineXRaftClient.handPitchAdjust), new Quaternionf()).transform(new Vector3f(0, -1, 0));
                Vec3d dir = new Vec3d(dir1.x, dir1.y, dir1.z);
                Vec3d endPos = pos.add(dir.multiply(d));
                this.client.crosshairTarget = entity.world.raycast(new RaycastContext(pos, endPos, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity));
                boolean bl = false;
                double e = d;
                if (this.client.interactionManager.hasExtendedReach()) {
                    e = 6.0D;
                    d = e;
                } else {
                    if (d > 3.0D) {
                        bl = true;
                    }
                }

                e *= e;
                if (this.client.crosshairTarget != null) {
                    e = this.client.crosshairTarget.getPos().squaredDistanceTo(pos);
                }

                endPos = pos.add(dir.x * d, dir.y * d, dir.z * d);
                float f = 1.0F;
                Box box = entity.getBoundingBox().stretch(dir.multiply(d)).expand(f, f, f);
                EntityHitResult entityHitResult = ProjectileUtil.raycast(entity, pos, endPos, box, (entityx) -> !entityx.isSpectator() && entityx.collides(), e);
                if (entityHitResult != null) {
                    Entity entity2 = entityHitResult.getEntity();
                    Vec3d vec3d4 = entityHitResult.getPos();
                    double g = pos.squaredDistanceTo(vec3d4);
                    if (bl && g > 9.0D) {
                        this.client.crosshairTarget = BlockHitResult.createMissed(vec3d4, Direction.getFacing(dir.x, dir.y, dir.z), new BlockPos(vec3d4));
                    } else if (g < e || this.client.crosshairTarget == null) {
                        this.client.crosshairTarget = entityHitResult;
                        if (entity2 instanceof LivingEntity || entity2 instanceof ItemFrameEntity) {
                            this.client.targetedEntity = entity2;
                        }
                    }
                }

                this.client.getProfiler().pop();
            }
        }
    }
}
