package net.sorenon.minexraft.client.input;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Pair;
import net.sorenon.minexraft.client.MineXRaftClient;
import net.sorenon.minexraft.client.OpenXR;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.openxr.*;
import org.lwjgl.system.MemoryStack;

import java.nio.LongBuffer;

import static org.lwjgl.system.MemoryStack.stackMallocPointer;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.*;

public class XrInput {

    public final XrInstance xrInstance;
    public final XrSession xrSession;
    public final OpenXR xr;

    public XrInput(OpenXR openXR) {
        this.xrInstance = openXR.xrInstance;
        this.xrSession = openXR.xrSession;
        this.xr = openXR;
    }

    public void pollActions() {
        if (MineXRaftClient.OPEN_XR.sessionState != XR10.XR_SESSION_STATE_FOCUSED) {
            return;
        }

        try (MemoryStack stack = stackPush()) {
            // Update our action set with up-to-date input data!
            VanillaCompatActionSet actionSet = MineXRaftClient.vanillaCompatActionSet;

            XrActionsSyncInfo sync_info = XrActionsSyncInfo.mallocStack().set(
                    XR10.XR_TYPE_ACTIONS_SYNC_INFO,
                    NULL,
                    1,
                    XrActiveActionSet.callocStack(1).actionSet(actionSet)
            );

            xr.check(XR10.xrSyncActions(xrSession, sync_info));

            // Now we'll get the current states of our actions, and store them for later use
            XrActionStateGetInfo get_info = XrActionStateGetInfo.mallocStack().set(
                    XR10.XR_TYPE_ACTION_STATE_GET_INFO,
                    NULL,
                    actionSet.jumpAction,
                    0
            );

            xr.check(XR10.xrGetActionStateBoolean(xrSession, get_info, actionSet.jumpState));
            get_info.action(actionSet.attackAction);
            xr.check(XR10.xrGetActionStateBoolean(xrSession, get_info, actionSet.attackState));
            get_info.action(actionSet.useAction);
            xr.check(XR10.xrGetActionStateBoolean(xrSession, get_info, actionSet.useState));
            get_info.action(actionSet.inventoryAction);
            xr.check(XR10.xrGetActionStateBoolean(xrSession, get_info, actionSet.inventoryState));
            get_info.action(actionSet.sprintAction);
            xr.check(XR10.xrGetActionStateBoolean(xrSession, get_info, actionSet.sprintState));
            get_info.action(actionSet.sneakAction);
            xr.check(XR10.xrGetActionStateBoolean(xrSession, get_info, actionSet.sneakState));

            get_info.action(actionSet.thumbstickMainHand);
            xr.check(XR10.xrGetActionStateVector2f(xrSession, get_info, actionSet.thumbstickMainHandState));
            get_info.action(actionSet.thumbstickOffHand);
            xr.check(XR10.xrGetActionStateVector2f(xrSession, get_info, actionSet.thumbstickOffHandState));

            for (int hand = 0; hand < 2; hand++) {
                get_info.subactionPath(HandPath.subactionPaths.get(hand));
                get_info.action(MineXRaftClient.vanillaCompatActionSet.poseGrip);
                XrActionStatePose pose_state = XrActionStatePose.callocStack().type(XR10.XR_TYPE_ACTION_STATE_POSE);
                xr.check(XR10.xrGetActionStatePose(xrSession, get_info, pose_state));
                actionSet.isHandActive[hand] = pose_state.isActive();
            }
        }

        VanillaCompatActionSet actionSet = MineXRaftClient.vanillaCompatActionSet;

        if (actionSet.thumbstickMainHandState.changedSinceLastSync()) {
            XrVector2f vec = actionSet.thumbstickMainHandState.currentState();
            float x = vec.x();
            float y = vec.y();
            if (actionSet.thumbstickMainHandActivated) {
                actionSet.thumbstickMainHandActivated = !(Math.abs(x) <= 0.15 && Math.abs(y) <= 0.15);
            } else {
                if (Math.abs(x) >= 0.4f) {
                    MineXRaftClient.yawTurn += Math.toRadians(22) * -Math.signum(x);
                    Vector3f rotatedPos = new Quaternionf().rotateLocalY(MineXRaftClient.yawTurn).transform(MineXRaftClient.headPose.rawPos, new Vector3f());
                    Vector3f finalPos = MineXRaftClient.xrOffset.add(MineXRaftClient.headPose.getPos(), new Vector3f());

                    MineXRaftClient.xrOffset = finalPos.sub(rotatedPos).mul(1, 0, 1);

                    actionSet.thumbstickMainHandActivated = true;
                } else if (Math.abs(y) >= 0.45f) {
                    if (MinecraftClient.getInstance().player != null)
                        MinecraftClient.getInstance().player.inventory.scrollInHotbar(-y);

                    actionSet.thumbstickMainHandActivated = true;
                }
            }
        }
        if (actionSet.inventoryState.changedSinceLastSync()) {
            if (!actionSet.inventoryState.currentState()) {
                MinecraftClient client = MinecraftClient.getInstance();
                if (client.currentScreen == null) {
                    if (client.player != null && client.interactionManager != null) {
                        if (client.interactionManager.hasRidingInventory()) {
                            client.player.openRidingInventory();
                        } else {
                            client.getTutorialManager().onInventoryOpened();
                            client.openScreen(new InventoryScreen(client.player));
                        }
                    }
                } else {
                    client.currentScreen.keyPressed(256, 0, 0);
                }
            }
        }
        if (actionSet.sprintState.changedSinceLastSync()) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (actionSet.sprintState.currentState()) {
                client.options.keySprint.setPressed(true);
            } else {
                client.options.keySprint.setPressed(false);
                if (client.player != null) {
                    client.player.setSprinting(false);
                }
            }
        }
        if (actionSet.sneakState.changedSinceLastSync()) {
            MinecraftClient client = MinecraftClient.getInstance();
            client.options.keySneak.setPressed(actionSet.sneakState.currentState());
        }
        if (actionSet.attackState.changedSinceLastSync()) {
            MinecraftClient client = MinecraftClient.getInstance();
            InputUtil.Key key = client.options.keyAttack.getDefaultKey();
            if (actionSet.attackState.currentState()) {
                KeyBinding.onKeyPressed(key);
                KeyBinding.setKeyPressed(key, true);
            } else {
                KeyBinding.setKeyPressed(key, false);
            }
        }
        if (actionSet.useState.changedSinceLastSync()) {
            MinecraftClient client = MinecraftClient.getInstance();
            InputUtil.Key key = client.options.keyUse.getDefaultKey();
            if (actionSet.useState.currentState()) {
                KeyBinding.onKeyPressed(key);
                KeyBinding.setKeyPressed(key, true);
            } else {
                KeyBinding.setKeyPressed(key, false);
            }
        }
    }

    public VanillaCompatActionSet makeGameplayActionSet() {
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

            actionSet.jumpAction = makeBoolAction("jump", "Jump", actionSet);
            actionSet.attackAction = makeBoolAction("attack", "Attack", actionSet);
            actionSet.useAction = makeBoolAction("use", "Use", actionSet);
            actionSet.inventoryAction = makeBoolAction("inventory", "Inventory and Pause", actionSet);
            actionSet.sprintAction = makeBoolAction("sprint", "Sprint", actionSet);
            actionSet.sneakAction = makeBoolAction("sneak", "Sneak", actionSet);

            actionSet.thumbstickMainHand = makeVec2fAction("thumbstick_main_hand", "Thumbstick Main Hand", actionSet);
            actionSet.thumbstickOffHand = makeVec2fAction("thumbstick_off_hand", "Thumbstick Off Hand", actionSet);

            Pair<XrAction, XrSpace[]> grip = makeDualPoseAction("pose_grip", "Pose Grip", actionSet);
            actionSet.poseGrip = grip.getLeft();
            actionSet.poseGripSpaces = grip.getRight();

            {//TODO make these data-driven by json files
                XrActionSuggestedBinding.Buffer bindings = XrActionSuggestedBinding.mallocStack(10);
                bindings.get(0).set(actionSet.poseGrip, xr.getPath("/user/hand/left/input/grip/pose"));
                bindings.get(1).set(actionSet.poseGrip, xr.getPath("/user/hand/right/input/grip/pose"));
                bindings.get(2).set(actionSet.useAction, xr.getPath("/user/hand/left/input/trigger/value"));
                bindings.get(3).set(actionSet.attackAction, xr.getPath("/user/hand/right/input/trigger/value"));
                bindings.get(4).set(actionSet.thumbstickOffHand, xr.getPath("/user/hand/left/input/thumbstick"));
                bindings.get(5).set(actionSet.thumbstickMainHand, xr.getPath("/user/hand/right/input/thumbstick"));
                bindings.get(6).set(actionSet.inventoryAction, xr.getPath("/user/hand/left/input/y/click"));
                bindings.get(7).set(actionSet.jumpAction, xr.getPath("/user/hand/right/input/a/click"));
                bindings.get(8).set(actionSet.sprintAction, xr.getPath("/user/hand/right/input/squeeze/value"));
                bindings.get(9).set(actionSet.sneakAction, xr.getPath("/user/hand/left/input/squeeze/value"));

                XrInteractionProfileSuggestedBinding suggested_binds = XrInteractionProfileSuggestedBinding.mallocStack().set(
                        XR10.XR_TYPE_INTERACTION_PROFILE_SUGGESTED_BINDING,
                        NULL,
                        xr.getPath("/interaction_profiles/oculus/touch_controller"),
                        bindings
                );

                xr.check(XR10.xrSuggestInteractionProfileBindings(xrInstance, suggested_binds));
            }
            {
                XrActionSuggestedBinding.Buffer bindings = XrActionSuggestedBinding.mallocStack(10);
                bindings.get(0).set(actionSet.poseGrip, xr.getPath("/user/hand/left/input/grip/pose"));
                bindings.get(1).set(actionSet.poseGrip, xr.getPath("/user/hand/right/input/grip/pose"));
                bindings.get(2).set(actionSet.useAction, xr.getPath("/user/hand/left/input/trigger/value"));
                bindings.get(3).set(actionSet.attackAction, xr.getPath("/user/hand/right/input/trigger/value"));
                bindings.get(4).set(actionSet.thumbstickOffHand, xr.getPath("/user/hand/left/input/thumbstick"));
                bindings.get(5).set(actionSet.thumbstickMainHand, xr.getPath("/user/hand/right/input/thumbstick"));
                bindings.get(6).set(actionSet.inventoryAction, xr.getPath("/user/hand/left/input/b/click"));
                bindings.get(7).set(actionSet.jumpAction, xr.getPath("/user/hand/right/input/a/click"));
                bindings.get(8).set(actionSet.sprintAction, xr.getPath("/user/hand/right/input/squeeze/value"));
                bindings.get(9).set(actionSet.sneakAction, xr.getPath("/user/hand/left/input/squeeze/value"));

                XrInteractionProfileSuggestedBinding suggested_binds = XrInteractionProfileSuggestedBinding.mallocStack().set(
                        XR10.XR_TYPE_INTERACTION_PROFILE_SUGGESTED_BINDING,
                        NULL,
                        xr.getPath("/interaction_profiles/valve/index_controller"),
                        bindings
                );

                xr.check(XR10.xrSuggestInteractionProfileBindings(xrInstance, suggested_binds));
            }

            return actionSet;
        }
    }

    private XrAction makeBoolAction(String actionName, String actionNameLocalised, XrActionSet actionSet) {
        try (MemoryStack ignored = stackPush()) {
            XrActionCreateInfo actionCreateInfo = XrActionCreateInfo.mallocStack().set(
                    XR10.XR_TYPE_ACTION_CREATE_INFO,
                    NULL,
                    memASCII(actionName),
                    XR10.XR_ACTION_TYPE_BOOLEAN_INPUT,
                    0,
                    null,
                    memASCII(actionNameLocalised)
            );
            PointerBuffer pp = stackMallocPointer(1);
            xr.check(XR10.xrCreateAction(actionSet, actionCreateInfo, pp));
            return new XrAction(pp.get(), actionSet.getCapabilities());
        }
    }

    private XrAction makeVec2fAction(String actionName, String actionNameLocalised, XrActionSet actionSet) {
        try (MemoryStack ignored = stackPush()) {
            XrActionCreateInfo actionCreateInfo = XrActionCreateInfo.mallocStack().set(
                    XR10.XR_TYPE_ACTION_CREATE_INFO,
                    NULL,
                    memASCII(actionName),
                    XR10.XR_ACTION_TYPE_VECTOR2F_INPUT,
                    0,
                    null,
                    memASCII(actionNameLocalised)
            );
            PointerBuffer pp = stackMallocPointer(1);
            xr.check(XR10.xrCreateAction(actionSet, actionCreateInfo, pp));
            return new XrAction(pp.get(), actionSet.getCapabilities());
        }
    }

    @SuppressWarnings("SameParameterValue")
    private Pair<XrAction, XrSpace[]> makeDualPoseAction(String actionName, String actionNameLocalised, XrActionSet actionSet) {
        try (MemoryStack ignored = stackPush()) {
            //For each hand create actions for using poses as an input
            XrActionCreateInfo actionCreateInfo = XrActionCreateInfo.mallocStack().set(
                    XR10.XR_TYPE_ACTION_CREATE_INFO,
                    NULL,
                    memASCII(actionName),
                    XR10.XR_ACTION_TYPE_POSE_INPUT,
                    2,
                    HandPath.subactionPaths,
                    memASCII(actionNameLocalised)
            );
            PointerBuffer pp = stackMallocPointer(1);
            xr.check(XR10.xrCreateAction(actionSet, actionCreateInfo, pp));
            XrAction action = new XrAction(pp.get(0), actionSet.getCapabilities());

            //Then make the reference spaces to be able to access those poses
            XrSpace[] spaces = new XrSpace[2];
            for (int i = 0; i < 2; i++) {
                XrActionSpaceCreateInfo action_space_info = XrActionSpaceCreateInfo.mallocStack().set(
                        XR10.XR_TYPE_ACTION_SPACE_CREATE_INFO,
                        NULL,
                        action,
                        HandPath.subactionPaths.get(i),
                        OpenXR.identityPose
                );
                xr.check(XR10.xrCreateActionSpace(xrSession, action_space_info, pp));
                spaces[i] = new XrSpace(pp.get(0), xrSession);
            }

            return new Pair<>(action, spaces);
        }
    }

    public enum HandPath {
        LEFT("/user/hand/left"),
        RIGHT("/user/hand/right");

        public static final LongBuffer subactionPaths = memAllocLong(2).put(0, MineXRaftClient.OPEN_XR.getPath("/user/hand/left")).put(1, MineXRaftClient.OPEN_XR.getPath("/user/hand/right"));
        public final long subactionPath;

        HandPath(String pathString) {
            subactionPath = MineXRaftClient.OPEN_XR.getPath(pathString);
        }
    }
}
