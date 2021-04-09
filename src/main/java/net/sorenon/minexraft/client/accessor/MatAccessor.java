package net.sorenon.minexraft.client.accessor;

import org.lwjgl.openxr.XrFovf;

public interface MatAccessor {

    void createProjectionFov(XrFovf fov, float nearZ, float farZ);
}
