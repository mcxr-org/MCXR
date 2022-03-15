package net.sorenon.mcxr.play.input.actions;

import net.sorenon.mcxr.play.openxr.OpenXRSession;
import org.lwjgl.openxr.XR10;
import org.lwjgl.openxr.XrActionStateFloat;

public class FloatAction extends SingleInputAction<Float> {
    
    private static final XrActionStateFloat state = XrActionStateFloat.calloc().type(XR10.XR_TYPE_ACTION_STATE_FLOAT);

    public FloatAction(String name) {
        super(name, XR10.XR_ACTION_TYPE_FLOAT_INPUT);
        currentState = 0f;
    }

    @Override
    public void sync(OpenXRSession session) {
        getInfo.action(handle);
        session.instance.checkPanic(XR10.xrGetActionStateFloat(session.handle, getInfo, state), "xrGetActionStateFloat");
        this.currentState = state.currentState();
        this.changedSinceLastSync = state.changedSinceLastSync();
        this.lastChangeTime = state.lastChangeTime();
        this.isActive = state.isActive();
    }
}
