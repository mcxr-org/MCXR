package net.sorenon.mcxr.play.client;

import net.sorenon.mcxr.core.Pose;

public interface ControllerPoses {

    Pose getRawPhysicalPose();

    Pose getPhysicalPose();

    Pose getRawGamePose();

    Pose getGamePose();
}
