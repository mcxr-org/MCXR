package net.sorenon.mcxr.play.input;

import com.mojang.math.Quaternion;
import net.sorenon.mcxr.core.JOMLUtil;
import net.sorenon.mcxr.core.Pose;
import net.sorenon.mcxr.play.MCXRPlayClient;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.lwjgl.openxr.XrPosef;

//TODO find a better system for this
public class ControllerPoses {

    private final Pose stagePose = new Pose();
    private final Pose physicalPose = new Pose();
    private final Pose unscaledPhysicalPose = new Pose();
    private final Pose gamePose = new Pose();

    //Man I love Java
    private final Quaternionf quaternionf = new Quaternionf();

    /**
     * It's unlikely you will want to use this
     */
    public Pose getStagePose() {
        return stagePose;
    }

    /**
     * The rotated pose in physical space
     */
    public Pose getUnscaledPhysicalPose() {
        return unscaledPhysicalPose;
    }

    /**
     * The rotated + scaled + adjusted for gravity pose in physical space
     */
    public Pose getPhysicalPose() {
        return physicalPose;
    }

    /**
     * Used for the majority of gameplay related interactions
     */
    public Pose getMinecraftPose() {
        return gamePose;
    }

    public void updatePhysicalPose(XrPosef pose, float yawTurn, float scale, @Nullable Quaternion gravityRotation) {
        stagePose.pos.set(pose.position$().x(), pose.position$().y(), pose.position$().z());
        stagePose.orientation.set(pose.orientation().x(), pose.orientation().y(), pose.orientation().z(), pose.orientation().w());

        unscaledPhysicalPose.set(stagePose);
        unscaledPhysicalPose.orientation.rotateLocalY(yawTurn);

        quaternionf.identity().rotateLocalY(yawTurn).transform(unscaledPhysicalPose.pos);
        unscaledPhysicalPose.pos.add(MCXRPlayClient.stagePosition);

        physicalPose.set(unscaledPhysicalPose);

        if (gravityRotation != null) {
            Quaternionf gravity = JOMLUtil.convert(gravityRotation);
            gravity.conjugate(new Quaternionf()).transform(physicalPose.pos);
            physicalPose.orientation.set(gravity.conjugate(new Quaternionf()).mul(physicalPose.orientation));
//            gravity.mul(physicalPose.orientation, physicalPose.orientation);
        }
        physicalPose.pos.mul(scale);
    }

    public void updateGamePose(Vector3d origin) {
        gamePose.set(physicalPose);
        gamePose.getPos().add((float) origin.x, (float) origin.y, (float) origin.z);
    }
}
