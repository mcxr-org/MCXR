package net.sorenon.mcxr.core;

import com.mojang.math.Quaternion;
import net.minecraft.world.phys.Vec3;
import org.joml.*;

public class JOMLUtil {

    /**
     * Vector 3 start
     */

    public static Vec3 convert(Vector3dc vec) {
        return new Vec3(vec.x(), vec.y(), vec.z());
    }

    public static Vec3 convert(Vector3fc vec) {
        return new Vec3(vec.x(), vec.y(), vec.z());
    }

    public static Vector3d convert(Vec3 vec) {
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
        return new Quaternionf(quat.i(), quat.j(), quat.k(), quat.r());
    }

    public static Quaterniond convertd(Quaternion quat) {
        return new Quaterniond(quat.i(), quat.j(), quat.k(), quat.r());
    }
}
