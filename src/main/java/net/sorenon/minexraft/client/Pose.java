package net.sorenon.minexraft.client;

import net.minecraft.util.math.MathHelper;
import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.openxr.XrPosef;

public class Pose {

    private final Quaternionf orientation = new Quaternionf();
    private final Vector3f pos = new Vector3f();

    public void set(XrPosef pose) {
        pos.set(pose.position$().x(), pose.position$().y(), pose.position$().z());
        orientation.set(pose.orientation().x(), pose.orientation().y(), pose.orientation().z(), pose.orientation().w());
    }

    public void set(XrPosef pose, float turnYaw) {
        pos.set(pose.position$().x(), pose.position$().y(), pose.position$().z());
        orientation.set(pose.orientation().x(), pose.orientation().y(), pose.orientation().z(), pose.orientation().w());
        orientation.rotateLocalY(turnYaw);

        new Quaternionf().rotateLocalY(turnYaw).transform(pos);
    }

    public void set(Pose pose) {
        pos.set(pose.pos);
        orientation.set(pose.orientation);
    }

    public Vector3f getPos() {
        return pos;
    }

    public Quaternionf getOrientation() {
        return orientation;
    }

    public float getMCYaw() {
        Vector3f normal = orientation.transform(new Vector3f(0, 0, -1));
        float yaw = getYawFromNormal(normal);
        return (float) -Math.toDegrees(yaw) + 180;
    }

    public float getMCPitch() {
        Vector3f normal = orientation.transform(new Vector3f(0, 0, -1));
        float pitch = (float) Math.asin(MathHelper.clamp(normal.y, -0.999999999, 0.999999999));

        return (float) -Math.toDegrees(pitch);
    }

    public static float getYawFromNormal(Vector3f normal) {
        if (normal.z < 0) {
            return (float) java.lang.Math.atan(normal.x / normal.z);
        }
        if (normal.z == 0) {
            return (float) (Math.PI / 2 * -MathHelper.sign(normal.x));
        }
        if (normal.z > 0) {
            return (float) (java.lang.Math.atan(normal.x / normal.z) + Math.PI);
        }
        return 0;
    }
}
