package net.sorenon.mcxr.play.mixin.hands;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.phys.Vec3;
import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.play.PlayOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FireworkRocketEntity.class)
public class FireworkRocketEntityMixin {

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getLookAngle()Lnet/minecraft/world/phys/Vec3;"))
    Vec3 changeFlyDirection(LivingEntity instance) {
        if (instance instanceof LocalPlayer && MCXRPlayClient.MCXR_GAME_RENDERER.isXrMode()) {
            Vec3 result = PlayOptions.flyDirection.getLookDirection();
            if (result != null) {
                return result;
            }
        }
        return instance.getLookAngle();
    }
}
