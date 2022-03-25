package net.sorenon.mcxr.core.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
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
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin<T extends LivingEntity> extends AgeableListModel<T> implements ArmedModel, HeadedModel {
    @Shadow @Final public ModelPart leftLeg;
    @Shadow public HumanoidModel.ArmPose leftArmPose;
    @Shadow @Final public ModelPart rightArm;
    @Shadow @Final public ModelPart leftArm;

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At(value = "TAIL"))
    public void setupAnimInject(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        /* if(livingEntity instanceof Player player) {
            PlayerEntityAcc acc = (PlayerEntityAcc) player;
            if(acc.isXR()) {
                if (acc.getLArmPose() != null) {
                    Pose leftArm = acc.getLArmPose();
                    this.leftArm.setPos(
                            leftArm.pos.x,
                            leftArm.pos.y,
                            leftArm.pos.z
                    );

                    Vec3 vec = new Vec3(0, 0, 0);
                    vec.add(
                            2 * leftArm.orientation.x * leftArm.orientation.z - 2 * leftArm.orientation.z * leftArm.orientation.w,
                            2 * leftArm.orientation.y * leftArm.orientation.z - 2 * leftArm.orientation.x * leftArm.orientation.w,
                            2 * leftArm.orientation.x * leftArm.orientation.x - 2 * leftArm.orientation.y * leftArm.orientation.y
                    );

                    this.rightArm.setRotation(
                            (float) vec.x,
                            (float) vec.y,
                            (float) vec.z
                    );
                }

                if (acc.getRArmPose() != null) {
                    Pose rightArm = acc.getRArmPose();
                    this.rightArm.setPos(
                            rightArm.pos.x,
                            rightArm.pos.y,
                            rightArm.pos.z
                    );


                    //vec.x=2*x*z - 2*y*w;
                    //vec.y=2*y*z + 2*x*w;
                    //vec.z=1 - 2*x*x - 2*y*y;

                    Vec3 vec = new Vec3(0, 0, 0);
                    vec.add(
                            2 * rightArm.orientation.x * rightArm.orientation.z - 2 * rightArm.orientation.z * rightArm.orientation.w,
                            2 * rightArm.orientation.y * rightArm.orientation.z - 2 * rightArm.orientation.x * rightArm.orientation.w,
                            2 * rightArm.orientation.x * rightArm.orientation.x - 2 * rightArm.orientation.y * rightArm.orientation.y
                    );

                    this.rightArm.setRotation(
                            (float) vec.x,
                            (float) vec.y,
                            (float) vec.z
                    );
                }
            }
        }
        */
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
