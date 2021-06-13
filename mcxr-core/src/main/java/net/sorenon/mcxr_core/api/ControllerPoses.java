package net.sorenon.mcxr_core.api;

import net.sorenon.mcxr_core.Pose;

public interface ControllerPoses {

    Pose getRawPhysicalPose();

    Pose getPhysicalPose();

    Pose getRawGamePose();

    Pose getGamePose();
}
