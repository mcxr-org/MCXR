package net.sorenon.mcxr.play.input.actions;

import net.sorenon.mcxr.play.openxr.OpenXRSession;
import org.joml.Vector2f;
import org.lwjgl.openxr.XR10;
import org.lwjgl.openxr.XrActionStateVector2f;

public class Vec2fAction extends SingleInputAction<Vector2f> {

    private static final XrActionStateVector2f state = XrActionStateVector2f.malloc().type(XR10.XR_TYPE_ACTION_STATE_VECTOR2F);

    public Vec2fAction(String name) {
        super(name, XR10.XR_ACTION_TYPE_VECTOR2F_INPUT);
        currentState = new Vector2f();
    }

    @Override
    public void sync(OpenXRSession session) {
        getInfo.action(handle);
        session.instance.checkPanic(XR10.xrGetActionStateVector2f(session.handle, getInfo, state), "xrGetActionStateBoolean");
        this.currentState.x = state.currentState().x();
        this.currentState.y = state.currentState().y();
        this.changedSinceLastSync = state.changedSinceLastSync();
        this.lastChangeTime = state.lastChangeTime();
        this.isActive = state.isActive();
    }
}
