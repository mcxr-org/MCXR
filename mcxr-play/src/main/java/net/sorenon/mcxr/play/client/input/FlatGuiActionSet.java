package net.sorenon.mcxr.play.client.input;

import net.sorenon.mcxr.play.client.MCXRPlayClient;
import net.sorenon.mcxr.play.client.openxr.OpenXR;
import org.lwjgl.PointerBuffer;
import org.lwjgl.openxr.*;
import org.lwjgl.system.MemoryStack;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.lwjgl.system.MemoryStack.stackMallocPointer;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memASCII;

public class FlatGuiActionSet extends XrActionSet {

    /**
     * Actions which need precision to use must be on main hand
     * Thus pickup and split are on main hand
     */

    //Bool actions
    public XrAction pickupAction; //Pickup Stack, Swap Stack, Divide Drag, Pickup ALL
    public XrAction splitAction; //Split Stack, Swap Stack, Single Drag, Drop one
    public XrAction quickMoveAction;
    public XrAction exitAction;
//    public XrAction middleClickAction;

    //Vec2f actions
    public XrAction scrollAction;

    //States
    public XrActionStateBoolean pickupState = XrActionStateBoolean.calloc().type(XR10.XR_TYPE_ACTION_STATE_BOOLEAN);
    public XrActionStateBoolean splitState = XrActionStateBoolean.calloc().type(XR10.XR_TYPE_ACTION_STATE_BOOLEAN);
    public XrActionStateBoolean quickMoveState = XrActionStateBoolean.calloc().type(XR10.XR_TYPE_ACTION_STATE_BOOLEAN);
    public XrActionStateBoolean exitState = XrActionStateBoolean.calloc().type(XR10.XR_TYPE_ACTION_STATE_BOOLEAN);
    public XrActionStateVector2f scrollState = XrActionStateVector2f.calloc().type(XR10.XR_TYPE_ACTION_STATE_VECTOR2F);


    public FlatGuiActionSet(long handle, XrSession session) {
        super(handle, session);
    }

    public static FlatGuiActionSet init() {
        OpenXR xr = MCXRPlayClient.OPEN_XR;
        XrInput input = MCXRPlayClient.XR_INPUT;
        XrInstance xrInstance = xr.xrInstance.handle;
        XrSession xrSession = xr.xrSession.handle;

        try (MemoryStack ignored = stackPush()) {
            XrActionSetCreateInfo actionSetCreateInfo = XrActionSetCreateInfo.mallocStack().set(XR10.XR_TYPE_ACTION_SET_CREATE_INFO,
                    NULL,
                    memASCII("gui"),
                    memASCII("GUI"),
                    0
            );
            PointerBuffer pp = stackMallocPointer(1);
            xr.check(XR10.xrCreateActionSet(xrInstance, actionSetCreateInfo, pp));
            FlatGuiActionSet actionSet = new FlatGuiActionSet(pp.get(0), xrSession);

            actionSet.pickupAction = input.makeBoolAction("pickup", "Pickup", actionSet);
            actionSet.splitAction = input.makeBoolAction("split", "Split", actionSet);
            actionSet.quickMoveAction = input.makeBoolAction("quick_move", "QuickMove", actionSet);
            actionSet.exitAction = input.makeBoolAction("close", "Close Screen", actionSet);
            actionSet.scrollAction = input.makeVec2fAction("scroll", "Scroll", actionSet);

            return actionSet;
        }
    }

    public void getBindings(HashMap<String, List<Pair<XrAction, String>>> map) {
        map.computeIfAbsent("/interaction_profiles/oculus/touch_controller", aLong -> new ArrayList<>()).addAll(
                List.of(
                        new Pair<>(pickupAction, "/user/hand/right/input/a/click"),
                        new Pair<>(splitAction, "/user/hand/right/input/b/click"),
                        new Pair<>(quickMoveAction, "/user/hand/left/input/x/click"),
                        new Pair<>(exitAction, "/user/hand/left/input/y/click"),
                        new Pair<>(scrollAction, "/user/hand/right/input/thumbstick")
                )
        );
        map.computeIfAbsent("/interaction_profiles/valve/index_controller", aLong -> new ArrayList<>()).addAll(
                List.of(
                        new Pair<>(pickupAction, "/user/hand/right/input/a/click"),
                        new Pair<>(splitAction, "/user/hand/right/input/b/click"),
                        new Pair<>(quickMoveAction, "/user/hand/left/input/a/click"),
                        new Pair<>(exitAction, "/user/hand/left/input/b/click"),
                        new Pair<>(scrollAction, "/user/hand/right/input/thumbstick")
                )
        );
        map.computeIfAbsent("/interaction_profiles/microsoft/motion_controller", aLong -> new ArrayList<>()).addAll(
                List.of(
                        new Pair<>(pickupAction, "/user/hand/right/input/trigger/value"),
                        new Pair<>(splitAction, "/user/hand/right/input/trackpad/click"),
                        new Pair<>(quickMoveAction, "/user/hand/left/input/trigger/value"),
                        new Pair<>(exitAction, "/user/hand/left/input/trackpad/click"),
                        new Pair<>(scrollAction, "/user/hand/right/input/thumbstick")
                )
        );
    }

    public void sync() {
        XrActionStateGetInfo get_info = XrActionStateGetInfo.callocStack().type(XR10.XR_TYPE_ACTION_STATE_GET_INFO);
        OpenXR xr = MCXRPlayClient.OPEN_XR;
        XrSession xrSession = xr.xrSession.handle;

        get_info.action(pickupAction);
        xr.check(XR10.xrGetActionStateBoolean(xrSession, get_info, pickupState));
        get_info.action(splitAction);
        xr.check(XR10.xrGetActionStateBoolean(xrSession, get_info, splitState));
        get_info.action(quickMoveAction);
        xr.check(XR10.xrGetActionStateBoolean(xrSession, get_info, quickMoveState));
        get_info.action(exitAction);
        xr.check(XR10.xrGetActionStateBoolean(xrSession, get_info, exitState));
        get_info.action(scrollAction);
        xr.check(XR10.xrGetActionStateVector2f(xrSession, get_info, scrollState));
    }
}
