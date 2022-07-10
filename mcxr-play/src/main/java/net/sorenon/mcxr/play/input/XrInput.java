package net.sorenon.mcxr.play.input;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.sorenon.mcxr.core.Pose;
import net.sorenon.mcxr.play.MCXRGuiManager;
import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.play.PlayOptions;
import net.sorenon.mcxr.play.gui.QuickMenu;
import net.sorenon.mcxr.play.input.actions.Action;
import net.sorenon.mcxr.play.input.actions.SessionAwareAction;
import net.sorenon.mcxr.play.input.actionsets.GuiActionSet;
import net.sorenon.mcxr.play.input.actionsets.HandsActionSet;
import net.sorenon.mcxr.play.input.actionsets.VanillaGameplayActionSet;
import net.sorenon.mcxr.play.mixin.accessor.MouseHandlerAcc;
import net.sorenon.mcxr.play.openxr.OpenXRInstance;
import net.sorenon.mcxr.play.openxr.OpenXRSession;
import net.sorenon.mcxr.play.openxr.XrException;
import net.sorenon.mcxr.play.openxr.XrRuntimeException;
import org.joml.Quaterniond;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.openxr.*;
import oshi.util.tuples.Pair;

import java.util.HashMap;
import java.util.List;

import static net.sorenon.mcxr.core.JOMLUtil.convert;
import static org.lwjgl.system.MemoryStack.stackPointers;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public final class XrInput {
    public static final HandsActionSet handsActionSet = new HandsActionSet();
    public static final VanillaGameplayActionSet vanillaGameplayActionSet = new VanillaGameplayActionSet();
    public static final GuiActionSet guiActionSet = new GuiActionSet();

    private static long lastPollTime = 0;
    private static long turnTime = 0;
    private static Vec3 gripPosOld = new Vec3(0,0,0);

    public static boolean teleport = false;

    public static float lastHealth = 0;

    private static int motionPoints = 0;
    private static HitResult lastHit = null;

    private XrInput() {
    }

    //TODO registryify this
    public static void reinitialize(OpenXRSession session) throws XrException {
        OpenXRInstance instance = session.instance;

        handsActionSet.createHandle(instance);
        vanillaGameplayActionSet.createHandle(instance);
        guiActionSet.createHandle(instance);

        HashMap<String, List<Pair<Action, String>>> defaultBindings = new HashMap<>();
        handsActionSet.getDefaultBindings(defaultBindings);
        vanillaGameplayActionSet.getDefaultBindings(defaultBindings);
        guiActionSet.getDefaultBindings(defaultBindings);

        try (var stack = stackPush()) {
            for (var entry : defaultBindings.entrySet()) {
                var bindingsSet = entry.getValue();

                XrActionSuggestedBinding.Buffer bindings = XrActionSuggestedBinding.malloc(bindingsSet.size(), stack);

                for (int i = 0; i < bindingsSet.size(); i++) {
                    var binding = bindingsSet.get(i);
                    bindings.get(i).set(
                            binding.getA().getHandle(),
                            instance.getPath(binding.getB())
                    );
                }

                XrInteractionProfileSuggestedBinding suggested_binds = XrInteractionProfileSuggestedBinding.malloc(stack).set(
                        XR10.XR_TYPE_INTERACTION_PROFILE_SUGGESTED_BINDING,
                        NULL,
                        instance.getPath(entry.getKey()),
                        bindings
                );

                try {
                    instance.checkPanic(XR10.xrSuggestInteractionProfileBindings(instance.handle, suggested_binds), "xrSuggestInteractionProfileBindings");
                } catch (XrRuntimeException e) {
                    StringBuilder out = new StringBuilder(e.getMessage() + "\ninteractionProfile: " + entry.getKey());
                    for (var pair : bindingsSet) {
                        out.append("\n").append(pair.getB());
                    }
                    throw new XrRuntimeException(e.result, out.toString());
                }
            }

            XrSessionActionSetsAttachInfo attach_info = XrSessionActionSetsAttachInfo.malloc(stack).set(
                    XR10.XR_TYPE_SESSION_ACTION_SETS_ATTACH_INFO,
                    NULL,
                    stackPointers(vanillaGameplayActionSet.getHandle().address(), guiActionSet.getHandle().address(), handsActionSet.getHandle().address())
            );
            // Attach the action set we just made to the session
            instance.checkPanic(XR10.xrAttachSessionActionSets(session.handle, attach_info), "xrAttachSessionActionSets");
        }

        for (var action : handsActionSet.actions()) {
            if (action instanceof SessionAwareAction sessionAwareAction) {
                sessionAwareAction.createHandleSession(session);
            }
        }
    }

    /**
     * Pre-tick + Pre-render, called once every frame
     */
    public static void pollActions() {
        long time = System.nanoTime();
        if(Minecraft.getInstance().player != null) {
            lastHealth = Minecraft.getInstance().player.getHealth();
        }

        if (lastPollTime == 0) {
            lastPollTime = time;
        }

        if (MCXRPlayClient.INSTANCE.MCXRGuiManager.isScreenOpen()) {
            if (guiActionSet.exit.changedSinceLastSync) {
                if (guiActionSet.exit.currentState) {
                    if (Minecraft.getInstance().screen != null) {
                        Minecraft.getInstance().screen.keyPressed(256, 0, 0);
                    }
                }
            }
        }

        if (MCXRPlayClient.INSTANCE.MCXRGuiManager.isScreenOpen()) {
            return;
        }

        VanillaGameplayActionSet actionSet = vanillaGameplayActionSet;

        if(actionSet.menu.currentState && actionSet.menu.changedSinceLastSync) {
            Minecraft.getInstance().pauseGame(false);
        }

        if (actionSet.teleport.changedSinceLastSync && !actionSet.teleport.currentState) {
            XrInput.teleport = true;
        }

        //==immersive controls test==
        if(PlayOptions.immersiveControls){
            Pose gripPoint = handsActionSet.gripPoses[MCXRPlayClient.getMainHand()].getStagePose();
            Vec3 gripPos = convert(gripPoint.getPos());
            float delta = (time - lastPollTime) / 1_000_000_000f;
            double velo = gripPos.distanceTo(gripPosOld)/delta;
            //delay before attacking starts/stops by building up motion points
            if(velo>1 && motionPoints < 16){
                motionPoints+=Math.abs(velo);
             }
            else if(motionPoints>0){motionPoints-=1;}

            gripPosOld = gripPos;
        }

        if (PlayOptions.smoothTurning) {
            if (Math.abs(actionSet.turn.currentState) > 0.4) {
                float delta = (time - lastPollTime) / 1_000_000_000f;

                MCXRPlayClient.stageTurn += Math.toRadians(PlayOptions.smoothTurnRate) * -Math.signum(actionSet.turn.currentState) * delta;
                Vector3f newPos = new Quaternionf().rotateLocalY(MCXRPlayClient.stageTurn).transform(MCXRPlayClient.viewSpacePoses.getStagePose().getPos(), new Vector3f());
                Vector3f wantedPos = new Vector3f(MCXRPlayClient.viewSpacePoses.getPhysicalPose().getPos());

                MCXRPlayClient.stagePosition = wantedPos.sub(newPos).mul(1, 0, 1);
            }
        } else {
            if (actionSet.turn.changedSinceLastSync) {
                float value = actionSet.turn.currentState;
                if (actionSet.turnActivated) {
                    actionSet.turnActivated = Math.abs(value) > 0.15f;
                } else if (Math.abs(value) > 0.7f) {
                    MCXRPlayClient.stageTurn += Math.toRadians(PlayOptions.snapTurnAmount) * -Math.signum(value);
                    Vector3f newPos = new Quaternionf().rotateLocalY(MCXRPlayClient.stageTurn).transform(MCXRPlayClient.viewSpacePoses.getStagePose().getPos(), new Vector3f());
                    Vector3f wantedPos = new Vector3f(MCXRPlayClient.viewSpacePoses.getPhysicalPose().getPos());

                    MCXRPlayClient.stagePosition = wantedPos.sub(newPos).mul(1, 0, 1);

                    actionSet.turnActivated = true;
                }
            }
        }

        if (actionSet.hotbar.changedSinceLastSync) {
            var value = actionSet.hotbar.currentState;
            if (actionSet.hotbarActivated) {
                actionSet.hotbarActivated = Math.abs(value) > 0.15f;
            } else if (Math.abs(value) >= 0.7f) {
                if (Minecraft.getInstance().player != null)
                    Minecraft.getInstance().player.getInventory().swapPaint(-value);
                actionSet.hotbarActivated = true;
            }
        }

        if (actionSet.hotbarLeft.currentState && actionSet.hotbarLeft.changedSinceLastSync) {
            if (Minecraft.getInstance().player != null)
                Minecraft.getInstance().player.getInventory().swapPaint(+1);
        }
        if (actionSet.hotbarRight.currentState && actionSet.hotbarRight.changedSinceLastSync) {
            if (Minecraft.getInstance().player != null)
                Minecraft.getInstance().player.getInventory().swapPaint(-1);
        }

        if (actionSet.turnLeft.currentState && actionSet.turnLeft.changedSinceLastSync) {
            MCXRPlayClient.stageTurn += Math.toRadians(22);
            Vector3f newPos = new Quaternionf().rotateLocalY(MCXRPlayClient.stageTurn).transform(MCXRPlayClient.viewSpacePoses.getStagePose().getPos(), new Vector3f());
            Vector3f wantedPos = new Vector3f(MCXRPlayClient.viewSpacePoses.getPhysicalPose().getPos());

            MCXRPlayClient.stagePosition = wantedPos.sub(newPos).mul(1, 0, 1);
        }
        if (actionSet.turnRight.currentState && actionSet.turnRight.changedSinceLastSync) {
            MCXRPlayClient.stageTurn -= Math.toRadians(22);
            Vector3f newPos = new Quaternionf().rotateLocalY(MCXRPlayClient.stageTurn).transform(MCXRPlayClient.viewSpacePoses.getStagePose().getPos(), new Vector3f());
            Vector3f wantedPos = new Vector3f(MCXRPlayClient.viewSpacePoses.getPhysicalPose().getPos());

            MCXRPlayClient.stagePosition = wantedPos.sub(newPos).mul(1, 0, 1);
        }
        if (actionSet.menu.currentState && actionSet.menu.changedSinceLastSync) {
            Minecraft.getInstance().pauseGame(false);
        }

        if (actionSet.inventory.changedSinceLastSync) {
            if (!actionSet.inventory.currentState) {
                Minecraft client = Minecraft.getInstance();
                if (client.screen == null) {
                    if (client.player != null && client.gameMode != null) {
                        if (client.gameMode.isServerControlledInventory()) {
                            client.player.sendOpenInventory();
                        } else {
                            client.getTutorial().onOpenInventory();
                            client.setScreen(new InventoryScreen(client.player));
                        }
                    }
                }
            }
        }

        if (actionSet.quickmenu.changedSinceLastSync) {
            if (!actionSet.quickmenu.currentState) {
                Minecraft client = Minecraft.getInstance();
                if (client.screen == null) {
                    client.setScreen(new QuickMenu(Component.translatable("mcxr.quickmenu")));
                }
            }
        }
        if (actionSet.sprint.changedSinceLastSync) {
            Minecraft client = Minecraft.getInstance();
            if (actionSet.sprint.currentState) {
                client.options.keySprint.setDown(true);
            } else {
                client.options.keySprint.setDown(false);
                if (client.player != null) {
                    client.player.setSprinting(false);
                }
            }
        }

        if (actionSet.sprintAnalog.changedSinceLastSync) {
            float value = actionSet.sprintAnalog.currentState;
            Minecraft client = Minecraft.getInstance();
            if (value > 0.8f && !actionSet.sprintAnalogOn) {//sprint
                actionSet.sprintAnalogOn=true;
                client.options.keySprint.setDown(true);
                //disable sneak
                if (actionSet.sneakAnalogOn){
                    actionSet.sneakAnalogOn=false;
                    client.options.keyShift.setDown(false);
                    if (client.player != null) {
                        client.player.setShiftKeyDown(true);
                    }
                }
            } else if (actionSet.sprintAnalogOn && value < 0.6f){
                actionSet.sprintAnalogOn=false;
                client.options.keySprint.setDown(false);
                if (client.player != null) {
                    client.player.setSprinting(false);
                }
            }
        }

        if (actionSet.sneak.changedSinceLastSync) {
            Minecraft client = Minecraft.getInstance();
            client.options.keyShift.setDown(actionSet.sneak.currentState);
            if (client.player != null) {
                client.player.setShiftKeyDown(true);
            }
        }

        if (actionSet.swapHands.changedSinceLastSync) {
            Minecraft client = Minecraft.getInstance();
            InputConstants.Key key = client.options.keySwapOffhand.getDefaultKey();
            if (actionSet.swapHands.currentState) {
                KeyMapping.click(key);
                KeyMapping.set(key, true);
            } else {
                KeyMapping.set(key, false);
            }
        }

        if (actionSet.sneakAnalog.changedSinceLastSync) {
            float value = actionSet.sneakAnalog.currentState;
            Minecraft client = Minecraft.getInstance();
            if (value < -0.8f && !actionSet.sneakAnalogOn) {//sneak activate
                actionSet.sneakAnalogOn=true;
                client.options.keyShift.setDown(true);
                if (client.player != null) {
                    client.player.setShiftKeyDown(true);
                }
                //disable sprint
                if (actionSet.sprintAnalogOn){
                    actionSet.sprintAnalogOn=false;
                    client.options.keySprint.setDown(false);
                    if (client.player != null) {
                        client.player.setSprinting(false);
                    }
                }

            } else if (actionSet.sneakAnalogOn && value > -0.6f){
                actionSet.sneakAnalogOn=false;
                client.options.keyShift.setDown(false);
                if (client.player != null) {
                    client.player.setShiftKeyDown(true);
                }
            }
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
            Minecraft client = Minecraft.getInstance();
            InputConstants.Key key = client.options.keyUse.getDefaultKey();
            if (actionSet.use.currentState) {
                KeyMapping.click(key);
                KeyMapping.set(key, true);
            } else {
                KeyMapping.set(key, false);
            }
        }

        lastPollTime = time;
    }

    /**
     * Post-tick + Pre-render, called once every frame
     */
    public static void postTick(long predictedDisplayTime) {
        MCXRGuiManager FGM = MCXRPlayClient.INSTANCE.MCXRGuiManager;
        MouseHandlerAcc mouseHandler = (MouseHandlerAcc) Minecraft.getInstance().mouseHandler;
        if(Minecraft.getInstance().player != null) {
            Player player = Minecraft.getInstance().player;
            if (player.getHealth() < lastHealth) {
                applyHaptics(300, 1, XR10.XR_FREQUENCY_UNSPECIFIED);
            }
            if(player.isSprinting()) {
                applyHaptics(300, 0.5f, XR10.XR_FREQUENCY_UNSPECIFIED);
            }
            if(player.getUseItemRemainingTicks() > 0) {
                applyHaptics(300, 0.6f, XR10.XR_FREQUENCY_UNSPECIFIED);
            }
        }

        if (FGM.isScreenOpen()) {
            Pose pose = handsActionSet.gripPoses[MCXRPlayClient.getMainHand()].getUnscaledPhysicalPose();
            Vector3d pos = new Vector3d(pose.getPos());
            Vector3f dir = pose.getOrientation().rotateX((float) Math.toRadians(PlayOptions.handPitchAdjust), new Quaternionf()).transform(new Vector3f(0, -1, 0));
            Vector3d result = FGM.guiRaycast(pos, new Vector3d(dir));
            if (result != null) {
                Vector3d vec = result.sub(convert(FGM.position));
                FGM.orientation.invert(new Quaterniond()).transform(vec);
                vec.y *= ((double) FGM.guiFramebufferWidth / FGM.guiFramebufferHeight);

                vec.x /= FGM.size;
                vec.y /= FGM.size;

                mouseHandler.callOnMove(
                        Minecraft.getInstance().getWindow().getWindow(),
                        FGM.guiFramebufferWidth * (0.5 - vec.x),
                        FGM.guiFramebufferHeight * (1 - vec.y)
                );
            }
            GuiActionSet actionSet = guiActionSet;
            if (actionSet.pickup.changedSinceLastSync || actionSet.quickMove.changedSinceLastSync) {
                if (actionSet.pickup.currentState || actionSet.quickMove.currentState) {
                    mouseHandler.callOnPress(Minecraft.getInstance().getWindow().getWindow(),
                            GLFW.GLFW_MOUSE_BUTTON_LEFT, GLFW.GLFW_PRESS, 0);
                } else {
                    mouseHandler.callOnPress(Minecraft.getInstance().getWindow().getWindow(),
                            GLFW.GLFW_MOUSE_BUTTON_LEFT, GLFW.GLFW_RELEASE, 0);
                }
            }

            if (actionSet.split.changedSinceLastSync) {
                if (actionSet.split.currentState) {
                    mouseHandler.callOnPress(Minecraft.getInstance().getWindow().getWindow(),
                            GLFW.GLFW_MOUSE_BUTTON_RIGHT, GLFW.GLFW_PRESS, 0);
                } else {
                    mouseHandler.callOnPress(Minecraft.getInstance().getWindow().getWindow(),
                            GLFW.GLFW_MOUSE_BUTTON_RIGHT, GLFW.GLFW_RELEASE, 0);
                }
            }
            if (actionSet.resetGUI.changedSinceLastSync && actionSet.resetGUI.currentState) {
                FGM.needsReset = true;
            }
            var scrollState = actionSet.scroll.currentState;
            //TODO replace with a better acc alg
            double sensitivity = 0.25;
            if (Math.abs(scrollState.y()) > 0.9 && scrollState.length() > 0.95) {
                mouseHandler.callOnScroll(Minecraft.getInstance().getWindow().getWindow(),
                        -scrollState.x() * sensitivity, 1.5 * Math.signum(scrollState.y()));
            } else if (Math.abs(scrollState.y()) > 0.1) {
                mouseHandler.callOnScroll(Minecraft.getInstance().getWindow().getWindow(),
                        -scrollState.x() * sensitivity, 0.1 * Math.signum(scrollState.y()));
            }
        } else {
            VanillaGameplayActionSet actionSet = vanillaGameplayActionSet;
            if (actionSet.attack.changedSinceLastSync) {
                if (actionSet.attack.currentState) {
                    mouseHandler.callOnPress(Minecraft.getInstance().getWindow().getWindow(),
                            GLFW.GLFW_MOUSE_BUTTON_LEFT, GLFW.GLFW_PRESS, 0);
                    motionPoints=0;
                    if(MCXRPlayClient.getMainHand() == 1) {
                        applyHapticsRight(300, 1, XR10.XR_FREQUENCY_UNSPECIFIED);
                    } else {
                        applyHapticsLeft(300, 1, XR10.XR_FREQUENCY_UNSPECIFIED);
                    }
                }
            }
            if (!actionSet.attack.currentState) {
                mouseHandler.callOnPress(Minecraft.getInstance().getWindow().getWindow(),
                        GLFW.GLFW_MOUSE_BUTTON_LEFT, GLFW.GLFW_RELEASE, 0);

            }
            if (actionSet.inventory.currentState) {
                long heldTime = predictedDisplayTime - actionSet.inventory.lastChangeTime;
                if (heldTime * 1E-09 > 1) {
                    Minecraft.getInstance().pauseGame(false);
                }
            }

            //==immersive control test
            if(PlayOptions.immersiveControls){
                var hitResult = Minecraft.getInstance().hitResult;
                Pose handPoint = handsActionSet.aimPoses[MCXRPlayClient.getMainHand()].getMinecraftPose();
                Vec3 handPos = convert(handPoint.getPos());
                if(motionPoints>11) {
                    if (hitResult != null) {
                        double dist = handPos.distanceTo(hitResult.getLocation());
                        if (hitResult.getType() == HitResult.Type.BLOCK && dist < 0.4) {
                            mouseHandler.callOnPress(Minecraft.getInstance().getWindow().getWindow(),
                                    GLFW.GLFW_MOUSE_BUTTON_LEFT, GLFW.GLFW_PRESS, 0);
                        } else if (hitResult.getType() == HitResult.Type.ENTITY && dist < 4) {
                            mouseHandler.callOnPress(Minecraft.getInstance().getWindow().getWindow(),
                                    GLFW.GLFW_MOUSE_BUTTON_LEFT, GLFW.GLFW_PRESS, 0);
                            motionPoints = 0;
                        }

                        if(MCXRPlayClient.getMainHand() == 1) {
                            applyHapticsRight(300, 1f, XR10.XR_FREQUENCY_UNSPECIFIED);
                        } else {
                            applyHapticsLeft(300, 1f, XR10.XR_FREQUENCY_UNSPECIFIED);
                        }
                    } //else if (hitResult.getType() !=HitResult.Type.MISS && !lastHit.equals(hitResult)){//let go if hitting new block/entity
                    // mouseHandler.callOnPress(Minecraft.getInstance().getWindow().getWindow(),
                    //GLFW.GLFW_MOUSE_BUTTON_LEFT, GLFW.GLFW_RELEASE, 0);
                    //lastHit=null;
                    //motionPoints=0;
                    //}
                } else if(motionPoints < 1) {//let go when no more motionPoints
                    if(!actionSet.attack.currentState) {//only if not pressing attack
                        mouseHandler.callOnPress(Minecraft.getInstance().getWindow().getWindow(),
                                GLFW.GLFW_MOUSE_BUTTON_LEFT, GLFW.GLFW_RELEASE, 0);
                        lastHit=null;
                    }
                }
            }
        }
    }

    public static void applyHaptics(long duration, float amplitude, float frequency) {
        applyHapticsLeft(duration, amplitude, frequency);
        applyHapticsRight(duration, amplitude, frequency);
    }

    public static void applyHapticsRight(long duration, float amplitude, float frequency) {
        try(var stack = stackPush()) {
            XrHapticVibration vibrationInfo = XrHapticVibration.calloc(stack).set(
                    XR10.XR_TYPE_HAPTIC_VIBRATION,
                    NULL,
                    duration,
                    frequency,
                    amplitude
            );

            XrHapticActionInfo hapticActionInfo = XrHapticActionInfo.calloc();
            hapticActionInfo.type(XR10.XR_TYPE_HAPTIC_ACTION_INFO);
            hapticActionInfo.action(vanillaGameplayActionSet.rightHaptic.getHandle());

            MCXRPlayClient.OPEN_XR_STATE.instance.checkPanic(XR10.xrApplyHapticFeedback(MCXRPlayClient.OPEN_XR_STATE.session.handle, hapticActionInfo, XrHapticBaseHeader.create(vibrationInfo.address())), "xrApplyHapticFeedback");
        }
    }

    public static void applyHapticsLeft(long duration, float amplitude, float frequency) {
        try(var stack = stackPush()) {
            XrHapticVibration vibrationInfo = XrHapticVibration.calloc(stack).set(
                    XR10.XR_TYPE_HAPTIC_VIBRATION,
                    NULL,
                    duration,
                    frequency,
                    amplitude
            );

            XrHapticActionInfo hapticActionInfo = XrHapticActionInfo.calloc();
            hapticActionInfo.type(XR10.XR_TYPE_HAPTIC_ACTION_INFO);
            hapticActionInfo.action(vanillaGameplayActionSet.leftHaptic.getHandle());

            MCXRPlayClient.OPEN_XR_STATE.instance.checkPanic(XR10.xrApplyHapticFeedback(MCXRPlayClient.OPEN_XR_STATE.session.handle, hapticActionInfo, XrHapticBaseHeader.create(vibrationInfo.address())), "xrApplyHapticFeedback");
        }
    }
}
