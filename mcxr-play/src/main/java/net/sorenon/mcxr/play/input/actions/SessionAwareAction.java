package net.sorenon.mcxr.play.input.actions;

import net.sorenon.mcxr.play.openxr.OpenXRSession;
import net.sorenon.mcxr.play.openxr.XrException;

public interface SessionAwareAction {

    void createHandleSession(OpenXRSession session) throws XrException;

    void destroyHandleSession();
}
