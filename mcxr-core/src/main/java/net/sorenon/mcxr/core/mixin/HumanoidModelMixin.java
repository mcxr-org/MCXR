package net.sorenon.mcxr.core.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.sorenon.mcxr.core.MCXRScale;
import net.sorenon.mcxr.core.Pose;
import net.sorenon.mcxr.core.accessor.PlayerEntityAcc;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin<T extends LivingEntity> extends AgeableListModel<T> implements ArmedModel, HeadedModel {
    @Shadow public HumanoidModel.ArmPose leftArmPose;
    @Shadow @Final public ModelPart rightArm;
    @Shadow @Final public ModelPart leftArm;

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At(value = "TAIL"))
    public void setupAnimInject(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        //  if(livingEntity instanceof Player player) {
        //      PlayerEntityAcc acc = (PlayerEntityAcc) player;
        //      if(acc.isXR()) {
        //          if (acc.getLArmPose() != null) {
        //              Pose leftArm = acc.getLArmPose();
        //              this.leftArm.setPos(
        //                      leftArm.pos.z,
        //                      leftArm.pos.x,
        //                      leftArm.pos.x
        //              );
        //          }
//
        //          if (acc.getRArmPose() != null) {
        //              Pose rightArm = acc.getRArmPose();
        //              this.rightArm.setPos(
        //                      rightArm.pos.z,
        //                      rightArm.pos.x,
        //                      rightArm.pos.x
        //              );
        //          }
        //      }
        //  }
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
