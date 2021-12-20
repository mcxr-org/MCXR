package net.sorenon.mcxr.play.accessor;

import org.lwjgl.openxr.XrFovf;

public interface Matrix4fExt {
    void createProjectionFov(XrFovf fov, float nearZ, float farZ);
}
