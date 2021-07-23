package net.sorenon.mcxr.play.input.actionsets;

import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.play.input.actions.Action;
import net.sorenon.mcxr.play.input.actions.BoolAction;
import net.sorenon.mcxr.play.input.actions.FloatAction;
import net.sorenon.mcxr.play.input.actions.Vec2fAction;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VanillaGameplayActionSet extends ActionSet {

    public final BoolAction jump = new BoolAction("jump");
    public final BoolAction inventory = new BoolAction("inventory");
    public final BoolAction attack = new BoolAction("attack");
    public final BoolAction use = new BoolAction("use");
    public final BoolAction sprint = new BoolAction("sprint");
    public final BoolAction sneak = new BoolAction("sneak");
    public final BoolAction resetPos = new BoolAction("reset_pos");
    public final FloatAction turn = new FloatAction("turn");
    public final FloatAction hotbar = new FloatAction("hotbar");
//    public final Vec2fAction thumbstickMainHand = new Vec2fAction("thumbstick_mainhand");
    public final Vec2fAction move = new Vec2fAction("move");

    public boolean turnActivated = false;
    public boolean hotbarActivated;

    public final List<Action> actions = List.of(
            jump,
            inventory,
            attack,
            use,
            sprint,
            sneak,
            resetPos,
            turn,
            hotbar,
//            thumbstickMainHand,
            move
            );

    public VanillaGameplayActionSet() {
        super("vanilla_gameplay");
    }

    @Override
    public List<Action> actions() {
        return actions;
    }

    @Override
    public boolean shouldSync() {
        return !MCXRPlayClient.INSTANCE.flatGuiManager.isScreenOpen();
    }

    public void getBindings(HashMap<String, List<Pair<Action, String>>> map) {
        map.computeIfAbsent("/interaction_profiles/oculus/touch_controller", aLong -> new ArrayList<>()).addAll(
                List.of(
                        new Pair<>(use, "/user/hand/left/input/trigger/value"),
                        new Pair<>(attack, "/user/hand/right/input/trigger/value"),
                        new Pair<>(move, "/user/hand/left/input/thumbstick"),
                        new Pair<>(hotbar, "/user/hand/right/input/thumbstick/y"),
                        new Pair<>(turn, "/user/hand/right/input/thumbstick/x"),
                        new Pair<>(inventory, "/user/hand/left/input/y/click"),
                        new Pair<>(jump, "/user/hand/right/input/a/click"),
                        new Pair<>(sprint, "/user/hand/right/input/squeeze/value"),
                        new Pair<>(sneak, "/user/hand/left/input/squeeze/value"),
                        new Pair<>(resetPos, "/user/hand/right/input/thumbstick/click")
                )
        );
        map.computeIfAbsent("/interaction_profiles/valve/index_controller", aLong -> new ArrayList<>()).addAll(
                List.of(
                        new Pair<>(use, "/user/hand/left/input/trigger/value"),
                        new Pair<>(attack, "/user/hand/right/input/trigger/value"),
                        new Pair<>(move, "/user/hand/left/input/thumbstick"),
                        new Pair<>(hotbar, "/user/hand/right/input/thumbstick/y"),
                        new Pair<>(turn, "/user/hand/right/input/thumbstick/x"),
                        new Pair<>(inventory, "/user/hand/left/input/b/click"),
                        new Pair<>(jump, "/user/hand/right/input/a/click"),
                        new Pair<>(sprint, "/user/hand/right/input/squeeze/value"),
                        new Pair<>(sneak, "/user/hand/left/input/squeeze/value"),
                        new Pair<>(resetPos, "/user/hand/right/input/thumbstick/click")
                )
        );
        map.computeIfAbsent("/interaction_profiles/microsoft/motion_controller", aLong -> new ArrayList<>()).addAll(
                List.of(
                        new Pair<>(use, "/user/hand/left/input/trigger/value"),
                        new Pair<>(attack, "/user/hand/right/input/trigger/value"),
                        new Pair<>(move, "/user/hand/left/input/thumbstick"),
                        new Pair<>(hotbar, "/user/hand/right/input/thumbstick/y"),
                        new Pair<>(turn, "/user/hand/right/input/thumbstick/x"),
                        new Pair<>(inventory, "/user/hand/left/input/trackpad/click"),
                        new Pair<>(jump, "/user/hand/right/input/trackpad/click"),
                        new Pair<>(sprint, "/user/hand/right/input/squeeze/click"),
                        new Pair<>(sneak, "/user/hand/left/input/squeeze/click"),
                        new Pair<>(resetPos, "/user/hand/right/input/thumbstick/click")
                )
        );
    }
}
