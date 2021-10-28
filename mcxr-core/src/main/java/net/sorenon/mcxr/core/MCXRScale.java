package net.sorenon.mcxr.core;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.util.ScaleUtils;

public class MCXRScale {
    public static float getScale(Entity entity) {
        return getScale(entity, 1.0f);
    }

    public static float getScale(Entity entity, float delta) {
        if (FabricLoader.getInstance().isModLoaded("pehkui")) {
            var scaleData = ScaleType.BASE.getScaleData(entity);
            return scaleData.getScale(delta);
        } else {
            return 1;
        }
    }

    public static float getMotionScale(Entity entity) {
        if (FabricLoader.getInstance().isModLoaded("pehkui")) {
            return ScaleUtils.getMotionScale(entity);
        } else {
            return 1;
        }
    }
}
