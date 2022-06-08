package net.sorenon.mcxr.core;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Pair;

public class Teleport {

    public static Pair<Vec3, Vec3> fireRayFromHand(Player player, Vec3 start, Vec3 direction) {
        var level = player.level;

        var hitResult = level.clip(new ClipContext(start, start.add(direction.scale(7)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        var hitPos = hitResult.getLocation();

        if (hitResult.getType() != HitResult.Type.MISS) {
            var blockPos = hitResult.getBlockPos();

            var dims = player.getDimensions(player.getPose());
            if (hitResult.getDirection() == Direction.UP) {
                VoxelShape shape = Shapes.create(AABB.ofSize(hitPos, dims.width + 1.0E-6, dims.height + 1.0E-6, dims.width + 1.0E-6));
                var optional = level.findFreePosition(player, shape, hitPos.add(0, dims.height / 2, 0), dims.width, dims.height, dims.width);
                if (optional.isPresent()) {
                    return new Pair<>(null, optional.get().subtract(0, dims.height / 2, 0));
                }
            } else if (level.getBlockState(blockPos.above()).getCollisionShape(level, blockPos.above(), CollisionContext.of(player)).isEmpty()) {
                var blockState = level.getBlockState(blockPos).getCollisionShape(level, blockPos, CollisionContext.of(player));
                var pos = new Vec3(blockPos.getX() + 0.5f, blockPos.getY() + blockState.max(Direction.Axis.Y), blockPos.getZ() + 0.5f);
                if (level.noCollision(dims.makeBoundingBox(pos))) {
                    return new Pair<>(hitPos, pos);
                }
            }
        }

        return new Pair<>(hitPos, null);
    }

    public static Pair<Vec3, Boolean> fireFallRay(Player player, Vec3 hitPos1) {
        var level = player.level;

        var hitResult = level.clip(new ClipContext(hitPos1, hitPos1.subtract(0, 5, 0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        // For some reason doing a large raycast skips the first few centimeters
        if (hitResult.getType() == HitResult.Type.MISS) {
            hitResult = level.clip(new ClipContext(hitPos1, hitPos1.subtract(0, 1000, 0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        }

        var hitPos2 = hitResult.getLocation();

        var dims = player.getDimensions(player.getPose());
        VoxelShape shape = Shapes.create(AABB.ofSize(hitPos2, dims.width + 1.0E-6, dims.height + 1.0E-6, dims.width + 1.0E-6));
        return level
                .findFreePosition(player, shape, hitPos2.add(0, dims.height / 2, 0), dims.width, dims.height, dims.width)
                .map(vec3 -> new Pair<>(vec3.subtract(0, dims.height / 2, 0), true))
                .orElseGet(() -> new Pair<>(hitPos2, false));
    }

    @Nullable
    public static Vec3 tp(Player player, Vec3 start, Vec3 direction) {
        var stage1 = fireRayFromHand(player, start, direction);
        var hitPos1 = stage1.getA();
        var finalPos1 = stage1.getB();

        if (finalPos1 != null) {
            return finalPos1;
        }

        hitPos1 = hitPos1.subtract(direction.scale(0.05));

        var stage2 = fireFallRay(player, hitPos1);
        var hitPos2 = stage2.getA();

        if (stage2.getB()) {
            return hitPos2;
        }

        return null;
    }
}
