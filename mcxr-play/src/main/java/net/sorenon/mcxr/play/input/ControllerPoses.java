package net.sorenon.mcxr.play.input;

import net.sorenon.mcxr.core.Pose;
import net.sorenon.mcxr.play.MCXRPlayClient;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.lwjgl.openxr.XrPosef;

//TODO find a better system for this
public class ControllerPoses {

    private final Pose rawPhysicalPose = new Pose();
    private final Pose physicalPose = new Pose();
    private final Pose unscaledPhysicalPose = new Pose();
    private final Pose gamePose = new Pose();

    //Man I love Java
    private final Quaternionf quaternionf = new Quaternionf();

    /**
     * It's unlikely you will want to use this
     * @return The un-rotated pose in physical space
     */
    public Pose getRawPhysicalPose() {
        return rawPhysicalPose;
    }

    /**
     * The rotated + scaled pose in physical space
     */
    public Pose getScaledPhysicalPose() {
        return physicalPose;
    }

    /**
     * The rotated pose in physical space
     */
    public Pose getUnscaledPhysicalPose() {
        return unscaledPhysicalPose;
    }

    /**
     * Used for the majority of gameplay related interactions
     * @return The rotated and scaled pose in in-game space
     */
    public Pose getGamePose() {
        return gamePose;
    }

    public void updatePhysicalPose(XrPosef pose, float yawTurn, float scale) {
        rawPhysicalPose.pos.set(pose.position$().x(), pose.position$().y(), pose.position$().z());
        rawPhysicalPose.orientation.set(pose.orientation().x(), pose.orientation().y(), pose.orientation().z(), pose.orientation().w());

        physicalPose.set(rawPhysicalPose);
        physicalPose.orientation.rotateLocalY(yawTurn);

        quaternionf.identity().rotateLocalY(yawTurn).transform(physicalPose.pos);

        unscaledPhysicalPose.set(physicalPose);

        physicalPose.pos.mul(scale);

        physicalPose.pos.add(MCXRPlayClient.xrOffset);
    }

    public void updateGamePose(Vector3d origin) {
        gamePose.set(physicalPose);
        gamePose.getPos().add((float) origin.x, (float) origin.y, (float) origin.z);
    }
}
