package net.sorenon.mcxr.core.mixin;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.sorenon.mcxr.core.JOMLUtil;
import net.sorenon.mcxr.core.Pose;
import net.sorenon.mcxr.core.accessor.PlayerExt;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Predicate;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin extends ProjectileWeaponItem implements Vanishable  {
    @Shadow
    private static AbstractArrow getArrow(Level world, LivingEntity entity, ItemStack crossbow, ItemStack arrow) {
        return null;
    }

    public CrossbowItemMixin(Properties properties) {
        super(properties);
    }

    /**
     * @author
     * The Judge156
     */
    @Overwrite
    private static void shootProjectile(Level world, LivingEntity shooter, InteractionHand hand, ItemStack crossbow, ItemStack projectile, float soundPitch, boolean creative, float speed, float divergence, float simulated) {
        if (!world.isClientSide) {
            boolean bl = projectile.is(Items.FIREWORK_ROCKET);
            Object projectile2 = null;
            if (bl) {
                if(shooter instanceof Player player) {
                    PlayerExt acc = (PlayerExt) player;
                    Pose arm = acc.getPoseForArm(player.getMainArm());
                    if(acc.isXR() && arm != null) {
                        projectile2 = new FireworkRocketEntity(world, projectile, shooter, arm.pos.x, arm.pos.y, arm.pos.z, true);
                    }
                } else {
                    projectile2 = new FireworkRocketEntity(world, projectile, shooter, shooter.getX(), shooter.getEyeY() - 0.15000000596046448D, shooter.getZ(), true);
                }
            } else {
                projectile2 = getArrow(world, shooter, crossbow, projectile);
                if (creative || simulated != 0.0F) {
                    ((AbstractArrow)projectile2).pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                }
            }

            if (shooter instanceof CrossbowAttackMob) {
                CrossbowAttackMob crossbowAttackMob = (CrossbowAttackMob)shooter;
                crossbowAttackMob.shootCrossbowProjectile(crossbowAttackMob.getTarget(), crossbow, (Projectile)projectile2, simulated);
            } else {
                if(shooter instanceof Player player) {
                    PlayerExt acc = (PlayerExt) player;
                    Pose arm = acc.getPoseForArm(player.getMainArm());
                    if(acc.isXR() && arm != null) {
                        org.joml.Vector3f vector3f = arm.pos;
                        vector3f.rotate(arm.getOrientation());
                        ((Projectile)projectile2).shoot(vector3f.x(), vector3f.y(), vector3f.z(), speed, divergence);
                    }
                } else {
                    Vec3 vec3 = shooter.getUpVector(1.0F);
                    Quaternion quaternion = new Quaternion(new Vector3f(vec3), simulated, true);
                    Vec3 vec32 = shooter.getViewVector(1.0F);
                    Vector3f vector3f = new Vector3f(vec32);
                    vector3f.transform(quaternion);
                    ((Projectile)projectile2).shoot(vector3f.x(), vector3f.y(), vector3f.z(), speed, divergence);
                }
            }

            crossbow.hurtAndBreak(bl ? 3 : 1, shooter, (e) -> {
                e.broadcastBreakEvent(hand);
            });
            world.addFreshEntity((Entity)projectile2);
            world.playSound((Player)null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 1.0F, soundPitch);
        }
    }

    @Shadow
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return null;
    }

    @Shadow
    public int getDefaultProjectileRange() {
        return 0;
    }
}
