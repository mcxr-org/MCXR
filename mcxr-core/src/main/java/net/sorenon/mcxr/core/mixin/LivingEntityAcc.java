package net.sorenon.mcxr.core.mixin;

import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityAcc {

    @Invoker
    float callGetStandingEyeHeight(Pose pose, EntityDimensions entityDimensions);
}
