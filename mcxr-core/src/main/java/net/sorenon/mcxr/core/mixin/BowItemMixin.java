package net.sorenon.mcxr.core.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.sorenon.mcxr.core.JOMLUtil;
import net.sorenon.mcxr.core.Pose;
import net.sorenon.mcxr.core.accessor.PlayerExt;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BowItem.class)
public class BowItemMixin {
    @Redirect(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;shootFromRotation(Lnet/minecraft/world/entity/Entity;FFFFF)V"))
    private static void shootFromArm(AbstractArrow instance, Entity entity, float playerXRot, float playerYRot, float roll, float modifierZ, float modifierXYZ) {
        if(entity instanceof Player player) {
            PlayerExt acc = (PlayerExt) player;
            Pose arm = acc.getPoseForArm(player.getMainArm());
            if(acc.isXR() && arm != null) {
                Vector3f dir = arm.getOrientation().transform(new Vector3f(0, -1, 0));
                instance.shootFromRotation(entity, dir.x, dir.y, roll, modifierZ, modifierXYZ);
                instance.setPos(JOMLUtil.convert(arm.pos));
            }
        }
    }
}
