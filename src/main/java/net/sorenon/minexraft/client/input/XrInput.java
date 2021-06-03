package net.sorenon.minexraft.client.input;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.sorenon.minexraft.client.MineXRaftClient;
import net.sorenon.minexraft.client.OpenXR;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.openxr.*;
import org.lwjgl.system.MemoryStack;
import oshi.util.tuples.Pair;

import java.nio.LongBuffer;

import static org.lwjgl.system.MemoryStack.stackMallocPointer;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.*;

public class XrInput {

    public final XrInstance xrInstance;
    public final XrSession xrSession;
    public final OpenXR xr;

    private boolean menuButton = false;

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
            VanillaCompatActionSet vcActionSet = MineXRaftClient.vanillaCompatActionSet;
            FlatGuiActionSet guiActionSet = MineXRaftClient.flatGuiActionSet;
            HandsActionSet handsActionSet = MineXRaftClient.handsActionSet;

            XrActiveActionSet.Buffer set = XrActiveActionSet.callocStack(3);
            set.get(0).actionSet(vcActionSet);
            set.get(1).actionSet(guiActionSet);
            set.get(2).actionSet(handsActionSet);

            XrActionsSyncInfo sync_info = XrActionsSyncInfo.mallocStack().set(
                    XR10.XR_TYPE_ACTIONS_SYNC_INFO,
                    NULL,
                    set.capacity(),
                    set
            );

            xr.check(XR10.xrSyncActions(xrSession, sync_info));

            handsActionSet.sync();
            vcActionSet.sync();
            guiActionSet.sync();
        }

        if (MineXRaftClient.INSTANCE.flatGuiManager.isScreenOpen()) {
            FlatGuiActionSet actionSet = MineXRaftClient.flatGuiActionSet;
            if (actionSet.exitState.changedSinceLastSync()) {
                if (actionSet.exitState.currentState()) {
                    MinecraftClient.getInstance().currentScreen.keyPressed(256, 0, 0);
                }
            }

            return;
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
                    Vector3f rotatedPos = new Quaternionf().rotateLocalY(MineXRaftClient.yawTurn).transform(MineXRaftClient.viewSpacePoses.getRawPhysicalPose().getPos(), new Vector3f());
                    Vector3f finalPos = MineXRaftClient.xrOffset.add(MineXRaftClient.viewSpacePoses.getPhysicalPose().getPos(), new Vector3f());

                    MineXRaftClient.xrOffset = finalPos.sub(rotatedPos).mul(1, 0, 1);

                    actionSet.thumbstickMainHandActivated = true;
                } else if (Math.abs(y) >= 0.45f) {
                    if (MinecraftClient.getInstance().player != null)
                        MinecraftClient.getInstance().player.getInventory().scrollInHotbar(-y);

                    actionSet.thumbstickMainHandActivated = true;
                }
            }
        }
        if (actionSet.inventoryState.changedSinceLastSync()) {
            if (actionSet.inventoryState.currentState()) {
                menuButton = true;
            } else if (menuButton) {
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
                menuButton = false;
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
//        if (actionSet.attackState.changedSinceLastSync()) {
//            MinecraftClient client = MinecraftClient.getInstance();
//            InputUtil.Key key = client.options.keyAttack.getDefaultKey();
//            if (actionSet.attackState.currentState()) {
//                KeyBinding.onKeyPressed(key);
//                KeyBinding.setKeyPressed(key, true);
//            } else {
//                KeyBinding.setKeyPressed(key, false);
//            }
//        }
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

    public XrAction makeBoolAction(String actionName, String actionNameLocalised, XrActionSet actionSet) {
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

    public XrAction makeVec2fAction(String actionName, String actionNameLocalised, XrActionSet actionSet) {
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
    public Pair<XrAction, XrSpace[]> makeDualPoseAction(String actionName, String actionNameLocalised, XrActionSet actionSet) {
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
