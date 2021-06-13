package net.sorenon.minexraft.client.input;

import net.sorenon.minexraft.api.ControllerPoses;
import net.sorenon.minexraft.client.MineXRaftClient;
import net.sorenon.minexraft.client.Pose;
import org.lwjgl.openxr.XrPosef;

public class ControllerPosesImpl implements ControllerPoses {

    private final Pose rawPhysicalPose = new Pose();
    private final Pose physicalPose = new Pose();
    private final Pose gamePose = new Pose();

    @Override
    public Pose getRawPhysicalPose() {
        return rawPhysicalPose;
    }

    @Override
    public Pose getPhysicalPose() {
        return physicalPose;
    }

    @Override
    public Pose getRawGamePose() {
        return gamePose;
    }

    @Override
    public Pose getGamePose() {
        return gamePose;
    }

    public void updatePhysicalPose(XrPosef pose, float yawTurn) {
        rawPhysicalPose.set(pose);
        physicalPose.set(pose, yawTurn);
        updateGamePose();
    }

    public void updateGamePose() {
        gamePose.set(physicalPose);
        gamePose.getPos().add((float) MineXRaftClient.xrOrigin.x, (float) MineXRaftClient.xrOrigin.y, (float) MineXRaftClient.xrOrigin.z);
    }
}
