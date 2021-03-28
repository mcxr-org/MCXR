package net.sorenon.minexraft;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.openxr.XrPosef;

public class Pose {

    private final Quaternionf orientation = new Quaternionf();
    private final Vector3f normal = new Vector3f(0, 0, -1);
    private final Vector3f pos = new Vector3f();
    private float yaw = 0;
    private float pitch = 0;

    public void set(XrPosef pose) {
        pos.set(pose.position$().x(), pose.position$().y(), pose.position$().z());
        orientation.set(pose.orientation().x(), pose.orientation().y(), pose.orientation().z(), pose.orientation().w());
        
        orientation.transform(normal.set(0, 0, -1), normal);

        float yaw = getYawFromNormal(normal);
        float pitch = (float) Math.asin(MathHelper.clamp(normal.y, -0.999999999, 0.999999999));
        this.yaw = (float) -Math.toDegrees(yaw) + 180;
        this.pitch = (float) -Math.toDegrees(pitch);
    }

    public Vec3d getNormalMc() {
        return new Vec3d(normal.x, normal.y, normal.z);
    }

    public Vec3d getPosMc() {
        return new Vec3d(pos.x, pos.y, pos.z);
    }

    public Vector3f getNormal() {
        return normal;
    }

    public Vector3f getPos() {
        return pos;
    }

    public Quaternionf getOrientation() {
        return orientation;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public static float getYawFromNormal(Vector3f normalIn) {
        Vector3f normal = new Vector3f(normalIn);
        if (normal.y != 0) {
            if (Math.abs(normal.y) > 0.999999) {
                return 0;
            }
            normal.y = 0;
            normal.normalize();
        }

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
