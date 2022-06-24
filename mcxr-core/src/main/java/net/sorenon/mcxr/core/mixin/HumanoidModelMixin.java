package net.sorenon.mcxr.core.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.sorenon.mcxr.core.JOMLUtil;
import net.sorenon.mcxr.core.MCXRScale;
import net.sorenon.mcxr.core.Pose;
import net.sorenon.mcxr.core.accessor.PlayerExt;
import org.joml.*;
import org.joml.Math;
import org.lwjgl.system.MathUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin<T extends LivingEntity> extends AgeableListModel<T> implements ArmedModel, HeadedModel {
    @Shadow @Final public ModelPart body;
    @Shadow @Final public ModelPart rightArm;
    @Shadow @Final public ModelPart leftArm;

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At(value = "TAIL"))
    public void setupAnimInject(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        if(livingEntity instanceof Player player) {
            PlayerExt acc = (PlayerExt) player;
            if(acc.isXR()) {
                if (acc.getLeftHandPose() != null) {
                    Pose leftArm = acc.getLeftHandPose();

                    Vector3f vec3 = new Vector3f();
                    Quaternionf quat = new Quaternionf(leftArm.orientation);
                    quat.rotateY(Math.toRadians(180));
                    quat.getEulerAnglesXYZ(vec3);

                    this.leftArm.setRotation(
                            vec3.x,
                            vec3.y,
                            vec3.z
                    );
                }

                if (acc.getRightHandPose() != null) {
                    Pose rightArm = acc.getRightHandPose();
                    Vector3f vec3 = new Vector3f();
                    Quaternionf quat = new Quaternionf(rightArm.orientation);
                    quat.rotateY(Math.toRadians(180));
                    quat.getEulerAnglesXYZ(vec3);

                    this.rightArm.setRotation(
                            vec3.x,
                            vec3.y,
                            vec3.z
                    );
                }
            }
        }
    }

    @Shadow
    protected Iterable<ModelPart> headParts() {
        return null;
    }

    @Shadow
    protected Iterable<ModelPart> bodyParts() {
        return null;
    }

    @Shadow
    public void translateToHand(HumanoidArm humanoidArm, PoseStack poseStack) {

    }

    @Shadow
    public void setupAnim(T entity, float f, float g, float h, float i, float j) {

    }

    @Shadow
    public ModelPart getHead() {
        return null;
    }
}
