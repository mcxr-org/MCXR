package net.sorenon.mcxr_core.accessor;

import net.sorenon.mcxr_core.Pose;

public interface PlayerEntityAcc {

    Pose getHeadPose();

    void markVR();
}
