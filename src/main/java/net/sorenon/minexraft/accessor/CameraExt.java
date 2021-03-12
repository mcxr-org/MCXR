package net.sorenon.minexraft.accessor;

import net.minecraft.client.util.math.Vector3f;

public interface CameraExt {

    void setPitch(float pitch);

    void setYaw(float yaw);

    Vector3f getDiagonalPlane();
}
