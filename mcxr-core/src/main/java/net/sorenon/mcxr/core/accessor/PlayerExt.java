package net.sorenon.mcxr.core.accessor;

import net.minecraft.world.entity.HumanoidArm;
import net.sorenon.mcxr.core.Pose;

public interface PlayerExt {

    Pose getHeadPose();

    Pose getLeftHandPose();

    Pose getRightHandPose();

    default Pose getPoseForArm(HumanoidArm arm) {
        if (arm == HumanoidArm.LEFT) {
            return getLeftHandPose();
        } else {
            return getRightHandPose();
        }
    }

    void setIsXr(boolean isXr);

    boolean isXR();

    ThreadLocal<HumanoidArm> getOverrideTransform();

    void setHeight(float height);
}
