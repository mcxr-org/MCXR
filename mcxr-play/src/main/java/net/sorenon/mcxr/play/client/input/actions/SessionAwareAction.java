package net.sorenon.mcxr.play.client.input.actions;

import net.sorenon.mcxr.play.client.openxr.OpenXRSession;
import net.sorenon.mcxr.play.client.openxr.XrException;

public interface SessionAwareAction {

    void createHandleSession(OpenXRSession session) throws XrException;

    void destroyHandleSession();
}
