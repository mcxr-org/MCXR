package net.sorenon.mcxr.core.accessor;

import net.sorenon.mcxr.core.Pose;

public interface PlayerEntityAcc {

    Pose getHeadPose();

    void markVR();
}
