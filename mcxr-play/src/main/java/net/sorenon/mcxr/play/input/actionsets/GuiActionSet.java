package net.sorenon.mcxr.play.input.actionsets;

import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.play.input.XrInput;
import net.sorenon.mcxr.play.input.actions.Action;
import net.sorenon.mcxr.play.input.actions.BoolAction;
import net.sorenon.mcxr.play.input.actions.Vec2fAction;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GuiActionSet extends ActionSet {

    public BoolAction pickup = new BoolAction("pickup"); //Pickup Stack, Swap Stack, Divide Drag, Pickup ALL
    public BoolAction split = new BoolAction("split"); //Split Stack, Swap Stack, Single Drag, Drop one
    public BoolAction quickMove = new BoolAction("quick_move"); //Shift + Click
    public BoolAction exit = new BoolAction("close");
    public BoolAction resetGUI = new BoolAction("reset_gui");
//    public XrAction middleClickAction;
    public Vec2fAction scroll = new Vec2fAction("scroll");


    public GuiActionSet() {
        super("gui", 1);
    }

    @Override
    public List<Action> actions() {
        return List.of(
                pickup,
                split,
                quickMove,
                exit,
                scroll,
                resetGUI
        );
    }

    @Override
    public boolean shouldSync() {
        return (MCXRPlayClient.INSTANCE.MCXRGuiManager.isScreenOpen() | exit.currentState | pickup.currentState) && !XrInput.vanillaGameplayActionSet.inventory.currentState;
    }

    @Override
    public void getDefaultBindings(HashMap<String, List<Pair<Action, String>>> map) {
        map.computeIfAbsent("/interaction_profiles/oculus/touch_controller", aLong -> new ArrayList<>()).addAll(
                List.of(
                        new Pair<>(pickup, "/user/hand/right/input/a/click"),
                        new Pair<>(split, "/user/hand/right/input/b/click"),
                        new Pair<>(quickMove, "/user/hand/left/input/x/click"),
                        new Pair<>(exit, "/user/hand/left/input/y/click"),
                        new Pair<>(resetGUI, "/user/hand/left/input/thumbstick/click"),
                        new Pair<>(resetGUI, "/user/hand/right/input/thumbstick/click"),
                        new Pair<>(scroll, "/user/hand/right/input/thumbstick")
                )
        );
        map.computeIfAbsent("/interaction_profiles/valve/index_controller", aLong -> new ArrayList<>()).addAll(
                List.of(
                        new Pair<>(pickup, "/user/hand/right/input/a/click"),
                        new Pair<>(split, "/user/hand/right/input/b/click"),
                        new Pair<>(quickMove, "/user/hand/left/input/a/click"),
                        new Pair<>(exit, "/user/hand/left/input/b/click"),
                        new Pair<>(resetGUI, "/user/hand/left/input/thumbstick/click"),
                        new Pair<>(resetGUI, "/user/hand/right/input/thumbstick/click"),
                        new Pair<>(scroll, "/user/hand/right/input/thumbstick")
                )
        );
        map.computeIfAbsent("/interaction_profiles/microsoft/motion_controller", aLong -> new ArrayList<>()).addAll(
                List.of(
                        new Pair<>(pickup, "/user/hand/right/input/trigger/value"),
                        new Pair<>(split, "/user/hand/right/input/trackpad/click"),
                        new Pair<>(quickMove, "/user/hand/left/input/trigger/value"),
                        new Pair<>(exit, "/user/hand/left/input/trackpad/click"),
                        new Pair<>(resetGUI, "/user/hand/left/input/thumbstick/click"),
                        new Pair<>(resetGUI, "/user/hand/right/input/thumbstick/click"),
                        new Pair<>(exit, "/user/hand/left/input/menu/click"),
                        new Pair<>(exit, "/user/hand/right/input/menu/click"),
                        new Pair<>(scroll, "/user/hand/right/input/thumbstick")
                )
        );
        if (MCXRPlayClient.OPEN_XR_STATE.instance.handle.getCapabilities().XR_EXT_hp_mixed_reality_controller) {
            map.computeIfAbsent("/interaction_profiles/hp/mixed_reality_controller", aLong -> new ArrayList<>()).addAll(
                    List.of(
                            new Pair<>(pickup, "/user/hand/right/input/a/click"),
                            new Pair<>(split, "/user/hand/right/input/b/click"),
                            new Pair<>(quickMove, "/user/hand/left/input/x/click"),
                            new Pair<>(exit, "/user/hand/left/input/y/click"),
                            new Pair<>(resetGUI, "/user/hand/left/input/thumbstick/click"),
                            new Pair<>(resetGUI, "/user/hand/right/input/thumbstick/click"),
                            new Pair<>(scroll, "/user/hand/right/input/thumbstick")
                    )
            );
        }
        if (MCXRPlayClient.OPEN_XR_STATE.instance.handle.getCapabilities().XR_HTC_vive_cosmos_controller_interaction) {
            map.computeIfAbsent("/interaction_profiles/htc/vive_cosmos_controller", aLong -> new ArrayList<>()).addAll(
                    List.of(
                            new Pair<>(pickup, "/user/hand/right/input/a/click"),
                            new Pair<>(split, "/user/hand/right/input/b/click"),
                            new Pair<>(quickMove, "/user/hand/left/input/x/click"),
                            new Pair<>(exit, "/user/hand/left/input/y/click"),
                            new Pair<>(resetGUI, "/user/hand/left/input/thumbstick/click"),
                            new Pair<>(resetGUI, "/user/hand/right/input/thumbstick/click"),
                            new Pair<>(scroll, "/user/hand/right/input/thumbstick")
                    )
            );
        }
    }
}
