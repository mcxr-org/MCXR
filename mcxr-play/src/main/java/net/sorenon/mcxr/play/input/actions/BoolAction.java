package net.sorenon.mcxr.play.input.actions;

import net.sorenon.mcxr.play.openxr.OpenXRSession;
import org.lwjgl.openxr.XR10;
import org.lwjgl.openxr.XrActionStateBoolean;

public class BoolAction extends SingleInputAction<Boolean> {

    private static final XrActionStateBoolean state = XrActionStateBoolean.calloc().type(XR10.XR_TYPE_ACTION_STATE_BOOLEAN);

    public BoolAction(String name) {
        super(name, XR10.XR_ACTION_TYPE_BOOLEAN_INPUT);
        currentState = false;
    }

    @Override
    public void sync(OpenXRSession session) {
        getInfo.action(handle);
        session.instance.checkPanic(XR10.xrGetActionStateBoolean(session.handle, getInfo, state), "xrGetActionStateBoolean");
        this.currentState = state.currentState();
        this.changedSinceLastSync = state.changedSinceLastSync();
        this.lastChangeTime = state.lastChangeTime();
        this.isActive = state.isActive();
    }
}
