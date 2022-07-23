package net.sorenon.mcxr.core.mixin.hands;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.sorenon.mcxr.core.MCXRCore;
import net.sorenon.mcxr.core.accessor.PlayerExt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow public abstract InteractionHand getUsedItemHand();

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "releaseUsingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;releaseUsing(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;I)V"))
    void preReleaseUsing(CallbackInfo ci) {
        if (this instanceof PlayerExt playerExt && playerExt.isXR() && MCXRCore.getCoreConfig().handBasedItemUsage()) {
            playerExt.getOverrideTransform().set(MCXRCore.handToArm((LivingEntity)(Object)this, this.getUsedItemHand()));
        }
    }

    @Inject(method = "releaseUsingItem", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/world/item/ItemStack;releaseUsing(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;I)V"))
    void postReleaseUsing(CallbackInfo ci) {
        if (this instanceof PlayerExt playerExt && playerExt.isXR()) {
            playerExt.getOverrideTransform().set(null);
        }
    }
}
