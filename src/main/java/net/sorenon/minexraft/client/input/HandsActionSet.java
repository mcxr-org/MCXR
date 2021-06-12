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

public class HandsActionSet extends XrActionSet {
    public HandsActionSet(long handle, XrSession capabilities) {
        super(handle, capabilities);
    }

    public XrAction poseGrip;
    public XrAction poseAim;
    public XrSpace[] poseGripSpaces;
    public XrSpace[] poseAimSpaces;

    public boolean[] isHandActive = {false, false};

    public final ControllerPosesImpl[] gripPoses = {new ControllerPosesImpl(), new ControllerPosesImpl()};
    public final ControllerPosesImpl[] aimPoses = {new ControllerPosesImpl(), new ControllerPosesImpl()};

    public static HandsActionSet init() {
        OpenXR xr = MineXRaftClient.OPEN_XR;
        XrInput input = MineXRaftClient.XR_INPUT;
        XrInstance xrInstance = xr.xrInstance;
        XrSession xrSession = xr.xrSession;

        try (MemoryStack ignored = stackPush()) {
            XrActionSetCreateInfo actionSetCreateInfo = XrActionSetCreateInfo.mallocStack().set(XR10.XR_TYPE_ACTION_SET_CREATE_INFO,
                    NULL,
                    memASCII("hands"),
                    memASCII("Hands"),
                    0
            );
            PointerBuffer pp = stackMallocPointer(1);
            xr.check(XR10.xrCreateActionSet(xrInstance, actionSetCreateInfo, pp));
            HandsActionSet actionSet = new HandsActionSet(pp.get(0), xrSession);

            Pair<XrAction, XrSpace[]> grip = input.makeDualPoseAction("pose_grip", "Pose Grip", actionSet);
            actionSet.poseGrip = grip.getA();
            actionSet.poseGripSpaces = grip.getB();
            Pair<XrAction, XrSpace[]> aim = input.makeDualPoseAction("pose_aim", "Pose Aim", actionSet);
            actionSet.poseAim = aim.getA();
            actionSet.poseAimSpaces = aim.getB();
            return actionSet;
        }
    }

    public void getBindings(HashMap<String, List<Pair<XrAction, String>>> map) {
        map.computeIfAbsent("/interaction_profiles/khr/simple_controller", aLong -> new ArrayList<>()).addAll(
                List.of(
                        new Pair<>(poseGrip, "/user/hand/left/input/grip/pose"),
                        new Pair<>(poseGrip, "/user/hand/right/input/grip/pose")
                )
        );
        map.computeIfAbsent("/interaction_profiles/oculus/touch_controller", aLong -> new ArrayList<>()).addAll(
                List.of(
                        new Pair<>(poseGrip, "/user/hand/left/input/grip/pose"),
                        new Pair<>(poseGrip, "/user/hand/right/input/grip/pose")
                )
        );
        map.computeIfAbsent("/interaction_profiles/valve/index_controller", aLong -> new ArrayList<>()).addAll(
                List.of(
                        new Pair<>(poseGrip, "/user/hand/left/input/grip/pose"),
                        new Pair<>(poseGrip, "/user/hand/right/input/grip/pose")
                )
        );
        map.computeIfAbsent("/interaction_profiles/microsoft/motion_controller", aLong -> new ArrayList<>()).addAll(
                List.of(
                        new Pair<>(poseGrip, "/user/hand/left/input/grip/pose"),
                        new Pair<>(poseGrip, "/user/hand/right/input/grip/pose")
                )
        );
    }

    public void sync() {
        XrActionStateGetInfo get_info = XrActionStateGetInfo.callocStack().type(XR10.XR_TYPE_ACTION_STATE_GET_INFO);
        OpenXR xr = MineXRaftClient.OPEN_XR;
        XrSession xrSession = xr.xrSession;

        for (int hand = 0; hand < 2; hand++) {
            get_info.subactionPath(XrInput.HandPath.subactionPaths.get(hand));
            get_info.action(poseGrip);
            XrActionStatePose pose_state = XrActionStatePose.callocStack().type(XR10.XR_TYPE_ACTION_STATE_POSE);
            xr.check(XR10.xrGetActionStatePose(xrSession, get_info, pose_state));
            isHandActive[hand] = pose_state.isActive();
        }
    }
}
