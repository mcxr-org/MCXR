package net.sorenon.minexraft.client.input;

import net.sorenon.minexraft.client.MineXRaftClient;
import net.sorenon.minexraft.client.openxr.OpenXR;
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

    //States
    public XrActionStateBoolean jumpState = XrActionStateBoolean.calloc().type(XR10.XR_TYPE_ACTION_STATE_BOOLEAN);
    public XrActionStateBoolean inventoryState = XrActionStateBoolean.calloc().type(XR10.XR_TYPE_ACTION_STATE_BOOLEAN);
    public XrActionStateBoolean attackState = XrActionStateBoolean.calloc().type(XR10.XR_TYPE_ACTION_STATE_BOOLEAN);
    public XrActionStateBoolean useState = XrActionStateBoolean.calloc().type(XR10.XR_TYPE_ACTION_STATE_BOOLEAN);
    public XrActionStateBoolean sprintState = XrActionStateBoolean.calloc().type(XR10.XR_TYPE_ACTION_STATE_BOOLEAN);
    public XrActionStateBoolean sneakState = XrActionStateBoolean.calloc().type(XR10.XR_TYPE_ACTION_STATE_BOOLEAN);

    public XrActionStateVector2f thumbstickMainHandState = XrActionStateVector2f.calloc().type(XR10.XR_TYPE_ACTION_STATE_VECTOR2F);
    public XrActionStateVector2f thumbstickOffHandState = XrActionStateVector2f.calloc().type(XR10.XR_TYPE_ACTION_STATE_VECTOR2F);

    public boolean thumbstickMainHandActivated = false;

    public static VanillaCompatActionSet init() {
        OpenXR xr = MineXRaftClient.OPEN_XR;
        XrInput input = MineXRaftClient.XR_INPUT;
        XrInstance xrInstance = xr.xrInstance;
        XrSession xrSession = xr.xrSession;

        try (MemoryStack ignored = stackPush()) {
            XrActionSetCreateInfo actionSetCreateInfo = XrActionSetCreateInfo.mallocStack().set(XR10.XR_TYPE_ACTION_SET_CREATE_INFO,
                    NULL,
                    memASCII("gameplay"),
                    memASCII("Gameplay"),
                    0
            );
            PointerBuffer pp = stackMallocPointer(1);
            xr.check(XR10.xrCreateActionSet(xrInstance, actionSetCreateInfo, pp));
            VanillaCompatActionSet actionSet = new VanillaCompatActionSet(pp.get(0), xrSession.getCapabilities());

            actionSet.jumpAction = input.makeBoolAction("jump", "Jump", actionSet);
            actionSet.attackAction = input.makeBoolAction("attack", "Attack", actionSet);
            actionSet.useAction = input.makeBoolAction("use", "Use", actionSet);
            actionSet.inventoryAction = input.makeBoolAction("inventory", "Inventory and Pause", actionSet);
            actionSet.sprintAction = input.makeBoolAction("sprint", "Sprint", actionSet);
            actionSet.sneakAction = input.makeBoolAction("sneak", "Sneak", actionSet);

            actionSet.thumbstickMainHand = input.makeVec2fAction("thumbstick_main_hand", "Thumbstick Main Hand", actionSet);
            actionSet.thumbstickOffHand = input.makeVec2fAction("thumbstick_off_hand", "Thumbstick Off Hand", actionSet);

            return actionSet;
        }
    }

    public void getBindings(HashMap<String, List<Pair<XrAction, String>>> map) {
        map.computeIfAbsent("/interaction_profiles/oculus/touch_controller", aLong -> new ArrayList<>()).addAll(
                List.of(
                        new Pair<>(useAction, "/user/hand/left/input/trigger/value"),
                        new Pair<>(attackAction, "/user/hand/right/input/trigger/value"),
                        new Pair<>(thumbstickOffHand, "/user/hand/left/input/thumbstick"),
                        new Pair<>(thumbstickMainHand, "/user/hand/right/input/thumbstick"),
                        new Pair<>(inventoryAction, "/user/hand/left/input/y/click"),
                        new Pair<>(jumpAction, "/user/hand/right/input/a/click"),
                        new Pair<>(sprintAction, "/user/hand/right/input/squeeze/value"),
                        new Pair<>(sneakAction, "/user/hand/left/input/squeeze/value")
                )
        );
//        {
//            XrActionSuggestedBinding.Buffer bindings = XrActionSuggestedBinding.mallocStack(10);
//            bindings.get(0).set(poseGrip, xr.getPath("/user/hand/left/input/grip/pose"));
//            bindings.get(1).set(poseGrip, xr.getPath("/user/hand/right/input/grip/pose"));
//            bindings.get(2).set(useAction, xr.getPath("/user/hand/left/input/trigger/value"));
//            bindings.get(3).set(attackAction, xr.getPath("/user/hand/right/input/trigger/value"));
//            bindings.get(4).set(thumbstickOffHand, xr.getPath("/user/hand/left/input/thumbstick"));
//            bindings.get(5).set(thumbstickMainHand, xr.getPath("/user/hand/right/input/thumbstick"));
//            bindings.get(6).set(inventoryAction, xr.getPath("/user/hand/left/input/b/click"));
//            bindings.get(7).set(jumpAction, xr.getPath("/user/hand/right/input/a/click"));
//            bindings.get(8).set(sprintAction, xr.getPath("/user/hand/right/input/squeeze/value"));
//            bindings.get(9).set(sneakAction, xr.getPath("/user/hand/left/input/squeeze/value"));
//
//            XrInteractionProfileSuggestedBinding suggested_binds = XrInteractionProfileSuggestedBinding.mallocStack().set(
//                    XR10.XR_TYPE_INTERACTION_PROFILE_SUGGESTED_BINDING,
//                    NULL,
//                    xr.getPath("/interaction_profiles/valve/index_controller"),
//                    bindings
//            );
//
//            xr.check(XR10.xrSuggestInteractionProfileBindings(xrInstance, suggested_binds));
//        }
//        {
//            XrActionSuggestedBinding.Buffer bindings = XrActionSuggestedBinding.mallocStack(10);
////                bindings.get(0).set(poseGrip, xr.getPath("/user/hand/left/input/grip/pose"));
////                bindings.get(1).set(poseGrip, xr.getPath("/user/hand/right/input/grip/pose"));
//            bindings.get(0).set(poseGrip, xr.getPath("/user/hand/left/input/aim/pose"));
//            bindings.get(1).set(poseGrip, xr.getPath("/user/hand/right/input/aim/pose"));
//            bindings.get(2).set(useAction, xr.getPath("/user/hand/left/input/trigger/value"));
//            bindings.get(3).set(attackAction, xr.getPath("/user/hand/right/input/trigger/value"));
//            bindings.get(4).set(thumbstickOffHand, xr.getPath("/user/hand/left/input/thumbstick"));
//            bindings.get(5).set(thumbstickMainHand, xr.getPath("/user/hand/right/input/thumbstick"));
//            bindings.get(6).set(inventoryAction, xr.getPath("/user/hand/left/input/trackpad/click"));
//            bindings.get(7).set(jumpAction, xr.getPath("/user/hand/right/input/trackpad/click"));
//            bindings.get(8).set(sprintAction, xr.getPath("/user/hand/right/input/squeeze/click"));
//            bindings.get(9).set(sneakAction, xr.getPath("/user/hand/left/input/squeeze/click"));
//
//            XrInteractionProfileSuggestedBinding suggested_binds = XrInteractionProfileSuggestedBinding.mallocStack().set(
//                    XR10.XR_TYPE_INTERACTION_PROFILE_SUGGESTED_BINDING,
//                    NULL,
//                    xr.getPath("/interaction_profiles/microsoft/motion_controller"),
//                    bindings
//            );
//
//            xr.check(XR10.xrSuggestInteractionProfileBindings(xrInstance, suggested_binds));
//        }
    }

    public void sync() {
        XrActionStateGetInfo get_info = XrActionStateGetInfo.callocStack().type(XR10.XR_TYPE_ACTION_STATE_GET_INFO);
        OpenXR xr = MineXRaftClient.OPEN_XR;
        XrSession xrSession = xr.xrSession;

        get_info.action(jumpAction);
        xr.check(XR10.xrGetActionStateBoolean(xrSession, get_info, jumpState));
        get_info.action(attackAction);
        xr.check(XR10.xrGetActionStateBoolean(xrSession, get_info, attackState));
        get_info.action(useAction);
        xr.check(XR10.xrGetActionStateBoolean(xrSession, get_info, useState));
        get_info.action(inventoryAction);
        xr.check(XR10.xrGetActionStateBoolean(xrSession, get_info, inventoryState));
        get_info.action(sprintAction);
        xr.check(XR10.xrGetActionStateBoolean(xrSession, get_info, sprintState));
        get_info.action(sneakAction);
        xr.check(XR10.xrGetActionStateBoolean(xrSession, get_info, sneakState));

        get_info.action(thumbstickMainHand);
        xr.check(XR10.xrGetActionStateVector2f(xrSession, get_info, thumbstickMainHandState));
        get_info.action(thumbstickOffHand);
        xr.check(XR10.xrGetActionStateVector2f(xrSession, get_info, thumbstickOffHandState));
    }
}
