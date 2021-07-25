package net.sorenon.mcxr.play.input;

import net.sorenon.mcxr.core.Pose;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.lwjgl.openxr.XrPosef;

//TODO find a better system for this
public class ControllerPoses {

    private final Pose rawPhysicalPose = new Pose();
    private final Pose physicalPose = new Pose();
    private final Pose gamePose = new Pose();

    /**
     * It's unlikely you will want to use this
     * @return The un-rotated pose in physical space
     */
    public Pose getRawPhysicalPose() {
        return rawPhysicalPose;
    }

    /**
     * Mostly used for GUI interactions
     * The rotated pose in physical space
     */
    public Pose getPhysicalPose() {
        return physicalPose;
    }

    /**
     * Used for the majority of gameplay related interactions
     * @return The rotated and scaled pose in in-game space
     */
    public Pose getGamePose() {
        return gamePose;
    }

    public void updatePhysicalPose(XrPosef pose, float yawTurn) {
        setPose(rawPhysicalPose, pose);
        setPose(physicalPose, pose, yawTurn);
    }

    public void updateGamePose(Vector3d origin, float scale) {
        gamePose.set(physicalPose);

        gamePose.getPos().mul(scale);
        gamePose.getPos().add((float) origin.x, (float) origin.y, (float) origin.z);
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
