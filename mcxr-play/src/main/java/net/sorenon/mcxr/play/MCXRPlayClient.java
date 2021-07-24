package net.sorenon.mcxr.play;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.sorenon.mcxr.core.MCXRCore;
import net.sorenon.mcxr.play.input.ControllerPosesImpl;
import net.sorenon.mcxr.play.input.XrInput;
import net.sorenon.mcxr.play.input.actions.Action;
import net.sorenon.mcxr.play.input.actions.SessionAwareAction;
import net.sorenon.mcxr.play.input.actionsets.GuiActionSet;
import net.sorenon.mcxr.play.input.actionsets.HandsActionSet;
import net.sorenon.mcxr.play.input.actionsets.VanillaGameplayActionSet;
import net.sorenon.mcxr.play.openxr.*;
import net.sorenon.mcxr.play.rendering.RenderPass;
import net.sorenon.mcxr.play.rendering.VrFirstPersonRenderer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.lwjgl.openxr.*;
import org.lwjgl.system.MemoryStack;
import oshi.util.tuples.Pair;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

import static org.lwjgl.system.MemoryStack.stackPointers;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class MCXRPlayClient implements ClientModInitializer {
    private static final Logger LOGGER = LogManager.getLogger("MCXR");

    public static final OpenXR OPEN_XR = new OpenXR();
    public static MCXRPlayClient INSTANCE;
    public static XrInput XR_INPUT;
    public static HandsActionSet handsActionSet = new HandsActionSet();
    public static VanillaGameplayActionSet vanillaGameplayActionSet = new VanillaGameplayActionSet();
    public static GuiActionSet guiActionSet = new GuiActionSet();
    public FlatGuiManager flatGuiManager = new FlatGuiManager();
    public VrFirstPersonRenderer vrFirstPersonRenderer = new VrFirstPersonRenderer(flatGuiManager);

    public static RenderPass renderPass = RenderPass.VANILLA;
    public static XrFovf fov = null;
    public static int viewIndex = 0;

    public static final ControllerPosesImpl eyePoses = new ControllerPosesImpl();
    public static final ControllerPosesImpl viewSpacePoses = new ControllerPosesImpl();

    /**
     * The center of the STAGE set at the same height of the PlayerEntity's feet in in-game space
     * This value is added to translate a physical position to an in-game position
     */
    public static Vector3d xrOrigin = new Vector3d(0, 0, 0);

    /**
     * Used when room-space movement is disabled
     * Allows the player to rotate around a central point or 'reset' their position to the center of the PlayerEntity
     */
    public static Vector3f xrOffset = new Vector3f(0, 0, 0);

    /**
     * Allows the player to turn in-game without turning IRL
     */
    public static float yawTurn = 0;

    /**
     * The angle to rotate the player's in-game hand for a more comfortable experience
     * May be different for different controllers -> needs testing
     */
    public static float handPitchAdjust = 30;

    public static int mainHand = 1;

    @Override
    public void onInitializeClient() {
        INSTANCE = this;
        Path path = FabricLoader.getInstance().getGameDir().resolve("mods").resolve("openxr_loader.dll");
        if (!path.toFile().exists()) {
            throw new RuntimeException("Could not find OpenXR loader in mods folder");
        }

        XR.create(path.toString());

        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            vrFirstPersonRenderer.renderAfterEntities(context);
        });

        WorldRenderEvents.LAST.register(context -> {
            if (!MinecraftClient.getInstance().options.hudHidden) {
                vrFirstPersonRenderer.renderHud(context);
            }
        });
    }

    public void postRenderManagerInit() throws XrException {
        XR_INPUT = new XrInput(OPEN_XR);

        OpenXRInstance instance = OPEN_XR.instance;
        OpenXRSession session = OPEN_XR.session;

        handsActionSet.createHandle(instance);
        vanillaGameplayActionSet.createHandle(instance);
        guiActionSet.createHandle(instance);

        HashMap<String, List<Pair<Action, String>>> bindingsMap = new HashMap<>();
        handsActionSet.getBindings(bindingsMap);
        vanillaGameplayActionSet.getBindings(bindingsMap);
        guiActionSet.getBindings(bindingsMap);

        for (var action : handsActionSet.actions()) {
            if (action instanceof SessionAwareAction saa) {
                saa.createHandleSession(session);
            }
        }

        try (MemoryStack stack = stackPush()) {
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

        flatGuiManager.init();
    }

    public static void resetView() {
        MCXRPlayClient.xrOffset = new Vector3f(0, 0, 0).sub(MCXRPlayClient.viewSpacePoses.getPhysicalPose().getPos().mul(getScale(), new Vector3f())).mul(1, 0, 1);
    }

    public static boolean isXrMode() {
        return MinecraftClient.getInstance().world != null || OPEN_XR.instance == null;
    }

    public static float getScale() {
        var player = MinecraftClient.getInstance().player;
        if (player == null) {
            return 1;
        } else {
            return MCXRCore.getScale(player);
        }
    }
}
