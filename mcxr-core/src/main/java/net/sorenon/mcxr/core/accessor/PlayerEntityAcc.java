package net.sorenon.mcxr.core.accessor;

import net.sorenon.mcxr.core.Pose;

public interface PlayerEntityAcc {

    Pose getHeadPose();

    void markVR();

    Pose getLArmPose();

    void markLArm();

    Pose getRArmPose();

    void markRArm();

    boolean isXR();
}
