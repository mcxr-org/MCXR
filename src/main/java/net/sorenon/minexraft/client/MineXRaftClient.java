package net.sorenon.minexraft.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.sorenon.minexraft.client.input.*;
import net.sorenon.minexraft.client.openxr.OpenXR;
import net.sorenon.minexraft.client.rendering.RenderPass;
import net.sorenon.minexraft.client.rendering.VrFirstPersonRenderer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.lwjgl.openxr.*;
import org.lwjgl.system.MemoryStack;
import oshi.util.tuples.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import static org.lwjgl.system.MemoryStack.stackPointers;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class MineXRaftClient implements ClientModInitializer {

    public static final OpenXR OPEN_XR = new OpenXR();
    public static MineXRaftClient INSTANCE;
    public static XrInput XR_INPUT;
    public static VanillaCompatActionSet vanillaCompatActionSet;
    public static FlatGuiActionSet flatGuiActionSet;
    public static HandsActionSet handsActionSet;
    public FlatGuiManager flatGuiManager = new FlatGuiManager();
    public VrFirstPersonRenderer vrFirstPersonRenderer = new VrFirstPersonRenderer(flatGuiManager);

    public static RenderPass renderPass = RenderPass.VANILLA;
    public static XrFovf fov = null;
    public static int viewIndex = 0;

    public static final ControllerPosesImpl eyePoses = new ControllerPosesImpl();
    public static final ControllerPosesImpl viewSpacePoses = new ControllerPosesImpl();

    public static Vector3d xrOrigin = new Vector3d(0, 0, 0); //The center of the STAGE set at the same height of the PlayerEntity's feet
    public static Vector3f xrOffset = new Vector3f(0, 0, 0);
    public static float yawTurn = 0;

    public static float handPitchAdjust = 15;
    public static int mainHand = 1;

    private static final Logger LOGGER = LogManager.getLogger("MCXR");

    @Override
    public void onInitializeClient() {
        INSTANCE = this;
        String loaderPath = "";
        try { //TODO bundle loader binaries with the mod
            File configFile = FabricLoader.getInstance().getConfigDir().resolve("mcxr.properties").toFile();
            if (!configFile.exists()) {
                if (!configFile.createNewFile()) {
                    LOGGER.warn("Could not create config file: " + configFile.getAbsolutePath());
                }
            }
            Properties properties = new Properties();

            if (configFile.exists()) {
                try (FileInputStream inputStream = new FileInputStream(configFile)) {
                    properties.load(inputStream);
                }
            }

            if (properties.containsKey("loader_path")) {
                loaderPath = properties.getProperty("loader_path");
            } else {
                properties.put("loader_path", "");
            }

            if (loaderPath.length() == 0) {
                if (configFile.exists()) {
                    try (FileOutputStream stream = new FileOutputStream(configFile)) {
                        properties.store(stream, "");
                    }
                }
                throw new IllegalStateException("Set path to openxr loader in " + configFile.getAbsolutePath());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        XR.create(loaderPath);
        OPEN_XR.createOpenXRInstance();
        OPEN_XR.initializeOpenXRSystem();

        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            vrFirstPersonRenderer.renderAfterEntities(context);
        });

        WorldRenderEvents.LAST.register(context -> {
            if (!MinecraftClient.getInstance().options.hudHidden) {
                vrFirstPersonRenderer.renderHud();
            }
        });
    }

    public void postRenderManagerInit() {
        OPEN_XR.eventDataBuffer = XrEventDataBuffer.calloc();
        OPEN_XR.eventDataBuffer.type(XR10.XR_TYPE_EVENT_DATA_BUFFER);

        OPEN_XR.createXRSwapchains();
        XR_INPUT = new XrInput(OPEN_XR);

        handsActionSet = HandsActionSet.init();
        flatGuiActionSet = FlatGuiActionSet.init();
        vanillaCompatActionSet = VanillaCompatActionSet.init();

        HashMap<String, List<Pair<XrAction, String>>> bindingsMap = new HashMap<>();
        vanillaCompatActionSet.getBindings(bindingsMap);
        flatGuiActionSet.getBindings(bindingsMap);
        handsActionSet.getBindings(bindingsMap);

        try (MemoryStack stack = stackPush()) {
            for (var entry : bindingsMap.entrySet()) {
                var bindingsSet = entry.getValue();

                XrActionSuggestedBinding.Buffer bindings = XrActionSuggestedBinding.mallocStack(bindingsSet.size());

                for (int i = 0; i < bindingsSet.size(); i++) {
                    var binding = bindingsSet.get(i);
                    bindings.get(i).set(
                            binding.getA(),
                            OPEN_XR.getPath(binding.getB())
                    );
                }

                XrInteractionProfileSuggestedBinding suggested_binds = XrInteractionProfileSuggestedBinding.mallocStack().set(
                        XR10.XR_TYPE_INTERACTION_PROFILE_SUGGESTED_BINDING,
                        NULL,
                        OPEN_XR.getPath(entry.getKey()),
                        bindings
                );

                OPEN_XR.check(XR10.xrSuggestInteractionProfileBindings(OPEN_XR.xrInstance, suggested_binds));
            }

            XrSessionActionSetsAttachInfo attach_info = XrSessionActionSetsAttachInfo.mallocStack().set(
                    XR10.XR_TYPE_SESSION_ACTION_SETS_ATTACH_INFO,
                    NULL,
                    stackPointers(vanillaCompatActionSet.address(), flatGuiActionSet.address(), handsActionSet.address())
            );
            // Attach the action set we just made to the session
            OPEN_XR.check(XR10.xrAttachSessionActionSets(OPEN_XR.xrSession, attach_info));
        }

        flatGuiManager.init();
    }

    public static void resetView() {
        MineXRaftClient.xrOffset = new Vector3f(0, 0, 0).sub(MineXRaftClient.viewSpacePoses.getPhysicalPose().getPos()).mul(1, 0, 1);
    }

    public static boolean isXrMode() {
        return MinecraftClient.getInstance().world != null;
    }
}
