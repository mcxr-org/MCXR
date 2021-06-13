package net.sorenon.minexraft.client;

import net.sorenon.mcxr_core.Pose;

public interface ControllerPoses {

    Pose getRawPhysicalPose();

    Pose getPhysicalPose();

    Pose getRawGamePose();

    Pose getGamePose();
}
