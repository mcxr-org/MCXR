package net.sorenon.mcxr.core;

import net.minecraft.util.Mth;
import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Pose {

    public final Quaternionf orientation = new Quaternionf();
    public final Vector3f pos = new Vector3f();

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
        float pitch = (float) Math.asin(Mth.clamp(normal.y, -0.999999999, 0.999999999));

        return (float) -Math.toDegrees(pitch);
    }

    public static float getYawFromNormal(Vector3f normal) {
        if (normal.z < 0) {
            return (float) java.lang.Math.atan(normal.x / normal.z);
        }
        if (normal.z == 0) {
            return (float) (Math.PI / 2 * -Mth.sign(normal.x));
        }
        if (normal.z > 0) {
            return (float) (java.lang.Math.atan(normal.x / normal.z) + Math.PI);
        }
        return 0;
    }
}
