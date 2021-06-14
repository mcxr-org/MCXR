package net.sorenon.mcxr.play.client.input;

import net.sorenon.mcxr.play.client.ControllerPoses;
import net.sorenon.mcxr.play.client.MCXRPlayClient;
import net.sorenon.mcxr.core.Pose;
import org.joml.Quaternionf;
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
        setPose(rawPhysicalPose, pose);
        setPose(physicalPose, pose, yawTurn);
        updateGamePose();
    }

    public void updateGamePose() {
        gamePose.set(physicalPose);
        gamePose.getPos().add((float) MCXRPlayClient.xrOrigin.x, (float) MCXRPlayClient.xrOrigin.y, (float) MCXRPlayClient.xrOrigin.z);
    }

    public static void setPose(Pose pose, XrPosef xrPosef) {
        pose.pos.set(xrPosef.position$().x(), xrPosef.position$().y(), xrPosef.position$().z());
        pose.orientation.set(xrPosef.orientation().x(), xrPosef.orientation().y(), xrPosef.orientation().z(), xrPosef.orientation().w());
    }

    public static void setPose(Pose pose, XrPosef xrPosef, float turnYaw) {
        pose.pos.set(xrPosef.position$().x(), xrPosef.position$().y(), xrPosef.position$().z());
        pose.orientation.set(xrPosef.orientation().x(), xrPosef.orientation().y(), xrPosef.orientation().z(), xrPosef.orientation().w());
        pose.orientation.rotateLocalY(turnYaw);

        new Quaternionf().rotateLocalY(turnYaw).transform(pose.pos);
    }
}
