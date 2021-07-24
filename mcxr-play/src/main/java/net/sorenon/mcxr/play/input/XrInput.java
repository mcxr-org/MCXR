package net.sorenon.mcxr.play.input;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.play.input.actionsets.GuiActionSet;
import net.sorenon.mcxr.play.input.actionsets.HandsActionSet;
import net.sorenon.mcxr.play.input.actionsets.VanillaGameplayActionSet;
import net.sorenon.mcxr.play.openxr.OpenXR;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.openxr.*;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.system.MemoryStack.stackPush;

public class XrInput {

    public final XrInstance xrInstance;
    public final XrSession xrSession;
    public final OpenXR xr;

    public boolean menuButton = false;

    public XrInput(OpenXR openXR) {
        this.xrInstance = openXR.instance.handle;
        this.xrSession = openXR.session.handle;
        this.xr = openXR;
    }

    public void pollActions() {
        if (MCXRPlayClient.OPEN_XR.session.state != XR10.XR_SESSION_STATE_FOCUSED) {
            return;
        }

        try (MemoryStack stack = stackPush()) {
            // Update our action set with up-to-date input data!
            VanillaGameplayActionSet vcActionSet = MCXRPlayClient.vanillaGameplayActionSet;
            GuiActionSet guiActionSet = MCXRPlayClient.guiActionSet;
            HandsActionSet handsActionSet = MCXRPlayClient.handsActionSet;

            XrActiveActionSet.Buffer sets = XrActiveActionSet.callocStack(3);
            sets.get(0).actionSet(handsActionSet.getHandle());
            sets.get(1).actionSet(vcActionSet.getHandle());
            sets.get(2).actionSet(guiActionSet.getHandle());

            XrActionsSyncInfo sync_info = XrActionsSyncInfo.calloc()
                    .type(XR10.XR_TYPE_ACTIONS_SYNC_INFO)
                    .activeActionSets(sets);

            xr.check(XR10.xrSyncActions(xrSession, sync_info));

            handsActionSet.sync(xr.session);
            vcActionSet.sync(xr.session);
            guiActionSet.sync(xr.session);
        }

        if (MCXRPlayClient.INSTANCE.flatGuiManager.isScreenOpen()) {
            GuiActionSet actionSet = MCXRPlayClient.guiActionSet;
            if (actionSet.exit.changedSinceLastSync) {
                if (actionSet.exit.currentState) {
                    MinecraftClient.getInstance().currentScreen.keyPressed(256, 0, 0);
                }
            }

            return;
        }

        VanillaGameplayActionSet actionSet = MCXRPlayClient.vanillaGameplayActionSet;

        if (actionSet.resetPos.changedSinceLastSync) {
            if (actionSet.resetPos.currentState) {
                MCXRPlayClient.resetView();
            }
        }

        if (actionSet.turn.changedSinceLastSync) {
            float value = actionSet.turn.currentState;
            if (actionSet.turnActivated) {
                actionSet.turnActivated = Math.abs(value) > 0.15f;
            } else if (Math.abs(value) > 0.6f) {
                MCXRPlayClient.yawTurn += Math.toRadians(22) * -Math.signum(value);
                var scale = MCXRPlayClient.getScale();
                Vector3f rotatedPos = new Quaternionf().rotateLocalY(MCXRPlayClient.yawTurn).transform(MCXRPlayClient.viewSpacePoses.getRawPhysicalPose().getPos(), new Vector3f()).mul(scale);
                Vector3f finalPos = MCXRPlayClient.xrOffset.add(MCXRPlayClient.viewSpacePoses.getPhysicalPose().getPos().mul(scale, new Vector3f()), new Vector3f());

                MCXRPlayClient.xrOffset = finalPos.sub(rotatedPos).mul(1, 0, 1);

                actionSet.turnActivated = true;
            }
        }

        if (actionSet.hotbar.changedSinceLastSync) {
            var value = actionSet.hotbar.currentState;
            if (actionSet.hotbarActivated) {
                actionSet.hotbarActivated = Math.abs(value) > 0.15;
            } else if (Math.abs(value) >= 0.45f) {
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
}
