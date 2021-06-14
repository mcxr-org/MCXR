package net.sorenon.mcxr.core;

import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import org.joml.*;

public class JOMLUtil {

    /**
     * Vector 3 start
     */

    public static Vec3d convert(Vector3dc vec) {
        return new Vec3d(vec.x(), vec.y(), vec.z());
    }

    public static Vec3d convert(Vector3fc vec) {
        return new Vec3d(vec.x(), vec.y(), vec.z());
    }

    public static Vector3d convert(Vec3d vec) {
        return new Vector3d(vec.x, vec.y, vec.z);
    }

    /**
     * Quaternion start
     */

    public Quaternion convert(Quaterniondc quat) {
        return new Quaternion((float) quat.x(), (float) quat.y(), (float) quat.z(), (float) quat.w());
    }

    public static Quaternion convert(Quaternionfc quat) {
        return new Quaternion(quat.x(), quat.y(), quat.z(), quat.w());
    }

    public static Quaternionf convert(Quaternion quat) {
        return new Quaternionf(quat.getX(), quat.getY(), quat.getZ(), quat.getW());
    }

    public static Quaterniond convertd(Quaternion quat) {
        return new Quaterniond(quat.getX(), quat.getY(), quat.getZ(), quat.getW());
    }
}
