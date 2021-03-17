package net.sorenon.minexraft.accessor;

import org.lwjgl.openxr.XrFovf;

public interface MatAccessor {

    void createProjectionFov(XrFovf fov, float nearZ, float farZ);
}
