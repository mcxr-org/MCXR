package net.sorenon.mcxr.core;

import com.fusionflux.gravity_api.RotationAnimation;
import com.fusionflux.gravity_api.api.GravityChangerAPI;
import com.fusionflux.gravity_api.util.RotationUtil;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import virtuoel.pehkui.api.ScaleTypes;
import virtuoel.pehkui.util.ScaleUtils;

import java.util.Optional;

public class MCXRModInterop {

    private static boolean pehkui;
    private static boolean gravity;

    public static void initialize(){
        pehkui = FabricLoader.getInstance().isModLoaded("pehkui");
        gravity = FabricLoader.getInstance().isModLoaded("gravity_api");
    }

    public static float getScale(Entity entity) {
        return getScale(entity, 1.0f);
    }

    public static float getScale(Entity entity, float delta) {
        if (pehkui) {
            var scaleData = ScaleTypes.BASE.getScaleData(entity);
            return scaleData.getScale(delta);
        } else {
            return 1;
        }
    }

    public static float getMotionScale(Entity entity) {
        if (pehkui) {
            return ScaleUtils.getMotionScale(entity);
        } else {
            return 1;
        }
    }

    public static Direction getGravityDirection(Entity entity) {
        if (gravity) {
            return GravityChangerAPI.getGravityDirection(entity);
        } else {
            return Direction.DOWN;
        }
    }

    @Nullable
    public static Quaternion getGravityRotation(Entity entity, float tickDelta) {
        if (gravity) {
            Direction gravityDirection = GravityChangerAPI.getGravityDirection(entity);
            Optional<RotationAnimation> animationOptional = GravityChangerAPI.getGravityAnimation(entity);
            if (animationOptional.isEmpty()) return null;
            RotationAnimation animation = animationOptional.get();
            long timeMs = entity.level.getGameTime() * 50 + (long) (tickDelta * 50);
            return animation.getCurrentGravityRotation(gravityDirection, timeMs);
        } else {
            return null;
        }
    }

    public static AABB aabbRotateEntityToWorld(AABB aabb, Direction gravityDirection) {
        if (gravity) {
            return RotationUtil.boxPlayerToWorld(aabb, gravityDirection);
        } else {
            assert gravityDirection == Direction.DOWN;
            return aabb;
        }
    }
}
