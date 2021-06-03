package net.sorenon.minexraft.api;

import net.sorenon.minexraft.client.Pose;

public interface ControllerPoses {

    Pose getRawPhysicalPose();

    Pose getPhysicalPose();

    Pose getRawGamePose();

    Pose getGamePose();
}
