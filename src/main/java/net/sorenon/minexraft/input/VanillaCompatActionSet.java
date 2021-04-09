package net.sorenon.minexraft.input;

import net.sorenon.minexraft.Pose;
import org.lwjgl.openxr.*;

public class VanillaCompatActionSet extends XrActionSet {
    public VanillaCompatActionSet(long handle, XRCapabilitiesSession capabilities) {
        super(handle, capabilities);
    }

    //Bool actions
    public XrAction jumpAction;
    public XrAction inventoryAction;
    public XrAction attackAction;
    public XrAction useAction;
    public XrAction sprintAction;
    public XrAction sneakAction;

    //Vec2f actions
    public XrAction thumbstickMainHand;
    public XrAction thumbstickOffHand;

    //Pose actions
    public XrAction poseGrip;
    public XrSpace[] poseGripSpaces;

    //States
    public XrActionStateBoolean jumpState = XrActionStateBoolean.calloc().type(XR10.XR_TYPE_ACTION_STATE_BOOLEAN);
    public XrActionStateBoolean inventoryState = XrActionStateBoolean.calloc().type(XR10.XR_TYPE_ACTION_STATE_BOOLEAN);
    public XrActionStateBoolean attackState = XrActionStateBoolean.calloc().type(XR10.XR_TYPE_ACTION_STATE_BOOLEAN);
    public XrActionStateBoolean useState = XrActionStateBoolean.calloc().type(XR10.XR_TYPE_ACTION_STATE_BOOLEAN);
    public XrActionStateBoolean sprintState = XrActionStateBoolean.calloc().type(XR10.XR_TYPE_ACTION_STATE_BOOLEAN);
    public XrActionStateBoolean sneakState = XrActionStateBoolean.calloc().type(XR10.XR_TYPE_ACTION_STATE_BOOLEAN);

    public XrActionStateVector2f thumbstickMainHandState = XrActionStateVector2f.calloc().type(XR10.XR_TYPE_ACTION_STATE_VECTOR2F);
    public XrActionStateVector2f thumbstickOffHandState = XrActionStateVector2f.calloc().type(XR10.XR_TYPE_ACTION_STATE_VECTOR2F);

    public boolean[] isHandActive = {false, false};
    public Pose[] poses = {new Pose(), new Pose()};

    public boolean thumbstickMainHandActivated = false;
}
