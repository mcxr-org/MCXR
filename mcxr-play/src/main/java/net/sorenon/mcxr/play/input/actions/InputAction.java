package net.sorenon.mcxr.play.input.actions;

import net.sorenon.mcxr.play.openxr.OpenXRSession;

public interface InputAction {
    void sync(OpenXRSession session);
}
