package net.sorenon.mcxr.play.input;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.sorenon.mcxr.core.JOMLUtil;
import net.sorenon.mcxr.core.Pose;
import net.sorenon.mcxr.play.FlatGuiManager;
import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.play.accessor.MouseExt;
import net.sorenon.mcxr.play.input.actions.Action;
import net.sorenon.mcxr.play.input.actions.SessionAwareAction;
import net.sorenon.mcxr.play.input.actionsets.GuiActionSet;
import net.sorenon.mcxr.play.input.actionsets.HandsActionSet;
import net.sorenon.mcxr.play.input.actionsets.VanillaGameplayActionSet;
import net.sorenon.mcxr.play.openxr.OpenXRInstance;
import net.sorenon.mcxr.play.openxr.OpenXRSession;
import net.sorenon.mcxr.play.openxr.XrException;
import net.sorenon.mcxr.play.openxr.XrRuntimeException;
import org.joml.Quaterniond;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.openxr.XR10;
import org.lwjgl.openxr.XrActionSuggestedBinding;
import org.lwjgl.openxr.XrInteractionProfileSuggestedBinding;
import org.lwjgl.openxr.XrSessionActionSetsAttachInfo;
import oshi.util.tuples.Pair;

import java.util.HashMap;
import java.util.List;

import static org.lwjgl.system.MemoryStack.stackPointers;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public final class XrInput {
    public static final HandsActionSet handsActionSet = new HandsActionSet();
    public static final VanillaGameplayActionSet vanillaGameplayActionSet = new VanillaGameplayActionSet();
    public static final GuiActionSet guiActionSet = new GuiActionSet();

    //TODO find a way to remove this
    public static boolean menuButton = false;

    private XrInput() {
    }

    //TODO registryify this
    public static void trySetSession(OpenXRSession session) throws XrException {
        OpenXRInstance instance = session.instance;

        handsActionSet.createHandle(instance);
        vanillaGameplayActionSet.createHandle(instance);
        guiActionSet.createHandle(instance);

        HashMap<String, List<Pair<Action, String>>> bindingsMap = new HashMap<>();
        handsActionSet.getBindings(bindingsMap);
        vanillaGameplayActionSet.getBindings(bindingsMap);
        guiActionSet.getBindings(bindingsMap);

        for (var action : handsActionSet.actions()) {
            if (action instanceof SessionAwareAction sessionAwareAction) {
                sessionAwareAction.createHandleSession(session);
            }
        }

        try (var ignored = stackPush()) {
            for (var entry : bindingsMap.entrySet()) {
                var bindingsSet = entry.getValue();

                XrActionSuggestedBinding.Buffer bindings = XrActionSuggestedBinding.mallocStack(bindingsSet.size());

                for (int i = 0; i < bindingsSet.size(); i++) {
                    var binding = bindingsSet.get(i);
                    bindings.get(i).set(
                            binding.getA().getHandle(),
                            instance.getPath(binding.getB())
                    );
                }

                XrInteractionProfileSuggestedBinding suggested_binds = XrInteractionProfileSuggestedBinding.mallocStack().set(
                        XR10.XR_TYPE_INTERACTION_PROFILE_SUGGESTED_BINDING,
                        NULL,
                        instance.getPath(entry.getKey()),
                        bindings
                );

                try {
                    instance.check(XR10.xrSuggestInteractionProfileBindings(instance.handle, suggested_binds), "xrSuggestInteractionProfileBindings");
                } catch (XrRuntimeException e) {
                    StringBuilder out = new StringBuilder(e.getMessage() + "\ninteractionProfile: " + entry.getKey());
                    for (var pair : bindingsSet) {
                        out.append("\n").append(pair.getB());
                    }
                    throw new XrRuntimeException(out.toString());
                }
            }

            XrSessionActionSetsAttachInfo attach_info = XrSessionActionSetsAttachInfo.mallocStack().set(
                    XR10.XR_TYPE_SESSION_ACTION_SETS_ATTACH_INFO,
                    NULL,
                    stackPointers(vanillaGameplayActionSet.getHandle().address(), guiActionSet.getHandle().address(), handsActionSet.getHandle().address())
            );
            // Attach the action set we just made to the session
            instance.check(XR10.xrAttachSessionActionSets(session.handle, attach_info), "xrAttachSessionActionSets");
        }
    }

    /**
     * Pre-tick + Pre-render, called once every frame
     */
    public static void pollActions() {
        if (MCXRPlayClient.INSTANCE.flatGuiManager.isScreenOpen()) {
            GuiActionSet actionSet = guiActionSet;
            if (actionSet.exit.changedSinceLastSync) {
                if (actionSet.exit.currentState) {
                    MinecraftClient.getInstance().currentScreen.keyPressed(256, 0, 0);
                }
            }

            return;
        }

        VanillaGameplayActionSet actionSet = vanillaGameplayActionSet;

        if (actionSet.resetPos.changedSinceLastSync) {
            if (actionSet.resetPos.currentState) {
                MCXRPlayClient.resetView();
            }
        }

        if (actionSet.turn.changedSinceLastSync) {
            float value = actionSet.turn.currentState;
            if (actionSet.turnActivated) {
                actionSet.turnActivated = Math.abs(value) > 0.15f;
            } else if (Math.abs(value) > 0.7f) {
                MCXRPlayClient.yawTurn += Math.toRadians(22) * -Math.signum(value);
                var scale = MCXRPlayClient.getCameraScale();
                Vector3f newPos = new Quaternionf().rotateLocalY(MCXRPlayClient.yawTurn).transform(MCXRPlayClient.viewSpacePoses.getRawPhysicalPose().getPos(), new Vector3f()).mul(scale);
                Vector3f wantedPos = new Vector3f(MCXRPlayClient.viewSpacePoses.getScaledPhysicalPose().getPos());

                MCXRPlayClient.xrOffset = wantedPos.sub(newPos).mul(1, 0, 1);

                actionSet.turnActivated = true;
            }
        }

        if (actionSet.hotbar.changedSinceLastSync) {
            var value = actionSet.hotbar.currentState;
            if (actionSet.hotbarActivated) {
                actionSet.hotbarActivated = Math.abs(value) > 0.15f;
            } else if (Math.abs(value) >= 0.7f) {
                if (MinecraftClient.getInstance().player != null)
                    MinecraftClient.getInstance().player.getInventory().scrollInHotbar(-value);
                actionSet.hotbarActivated = true;
            }
        }
        if (actionSet.inventory.changedSinceLastSync) {
            if (actionSet.inventory.currentState) {
                menuButton = true;
            } else if (menuButton) {
                MinecraftClient client = MinecraftClient.getInstance();
                if (client.currentScreen == null) {
                    if (client.player != null && client.interactionManager != null) {
                        if (client.interactionManager.hasRidingInventory()) {
                            client.player.openRidingInventory();
                        } else {
                            client.getTutorialManager().onInventoryOpened();
                            client.setScreen(new InventoryScreen(client.player));
                        }
                    }
                } else {
                    client.currentScreen.keyPressed(256, 0, 0);
                }
                menuButton = false;
            }
        }
        if (actionSet.sprint.changedSinceLastSync) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (actionSet.sprint.currentState) {
                client.options.keySprint.setPressed(true);
            } else {
                client.options.keySprint.setPressed(false);
                if (client.player != null) {
                    client.player.setSprinting(false);
                }
            }
        }
        if (actionSet.sneak.changedSinceLastSync) {
            MinecraftClient client = MinecraftClient.getInstance();
            client.options.keySneak.setPressed(actionSet.sneak.currentState);
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
        if (actionSet.use.changedSinceLastSync) {
            MinecraftClient client = MinecraftClient.getInstance();
            InputUtil.Key key = client.options.keyUse.getDefaultKey();
            if (actionSet.use.currentState) {
                KeyBinding.onKeyPressed(key);
                KeyBinding.setKeyPressed(key, true);
            } else {
                KeyBinding.setKeyPressed(key, false);
            }
        }
    }

    /**
     * Post-tick + Pre-render, called once every frame
     */
    public static void postTick(long predictedDisplayTime) {
        FlatGuiManager FGM = MCXRPlayClient.INSTANCE.flatGuiManager;
        MouseExt mouse = (MouseExt) MinecraftClient.getInstance().mouse;
        if (FGM.isScreenOpen()) {
            Pose pose = handsActionSet.gripPoses[MCXRPlayClient.mainHand].getScaledPhysicalPose();
            Vector3d pos = new Vector3d(pose.getPos());
            Vector3f dir = pose.getOrientation().rotateX((float) Math.toRadians(MCXRPlayClient.handPitchAdjust), new Quaternionf()).transform(new Vector3f(0, -1, 0));
            Vector3d result = FGM.guiRaycast(pos, new Vector3d(dir));
            if (result != null) {
                Vector3d vec = result.sub(JOMLUtil.convert(FGM.pos));
                FGM.rot.invert(new Quaterniond()).transform(vec);
                vec.y *= ((double) FGM.framebufferWidth / FGM.framebufferHeight);

                ((MouseExt) MinecraftClient.getInstance().mouse).cursorPos(
                        FGM.framebufferWidth * (0.5 - vec.x),
                        FGM.framebufferHeight * (1 - vec.y)
                );
            }
            GuiActionSet actionSet = guiActionSet;
            if (actionSet.pickup.changedSinceLastSync || actionSet.quickMove.changedSinceLastSync) {
                if (actionSet.pickup.currentState || actionSet.quickMove.currentState) {
                    mouse.mouseButton(GLFW.GLFW_MOUSE_BUTTON_LEFT, GLFW.GLFW_PRESS, 0);
                } else {
                    mouse.mouseButton(GLFW.GLFW_MOUSE_BUTTON_LEFT, GLFW.GLFW_RELEASE, 0);
                }
            }
            if (actionSet.split.changedSinceLastSync) {
                if (actionSet.split.currentState) {
                    mouse.mouseButton(GLFW.GLFW_MOUSE_BUTTON_RIGHT, GLFW.GLFW_PRESS, 0);
                } else {
                    mouse.mouseButton(GLFW.GLFW_MOUSE_BUTTON_RIGHT, GLFW.GLFW_RELEASE, 0);
                }
            }
            if (actionSet.scroll.changedSinceLastSync) {
                var state = actionSet.scroll.currentState;
                double sensitivity = 0.25;
                mouse.mouseScroll(-state.x() * sensitivity, state.y() * sensitivity);
            }
        } else {
            VanillaGameplayActionSet actionSet = vanillaGameplayActionSet;
            if (actionSet.attack.changedSinceLastSync) {
                if (actionSet.attack.currentState) {
                    mouse.mouseButton(GLFW.GLFW_MOUSE_BUTTON_LEFT, GLFW.GLFW_PRESS, 0);
                } else {
                    mouse.mouseButton(GLFW.GLFW_MOUSE_BUTTON_LEFT, GLFW.GLFW_RELEASE, 0);
                }
            }
            if (actionSet.inventory.currentState) {
                long heldTime = predictedDisplayTime - actionSet.inventory.lastChangeTime;
                if (heldTime * 1E-09 > 1) {
                    MinecraftClient.getInstance().openPauseMenu(false);
                    menuButton = false;
                }
            }
        }
    }
}
