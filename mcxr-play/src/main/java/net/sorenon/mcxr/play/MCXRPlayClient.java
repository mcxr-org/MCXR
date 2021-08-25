package net.sorenon.mcxr.play;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.sorenon.mcxr.core.MCXRCore;
import net.sorenon.mcxr.play.input.ControllerPoses;
import net.sorenon.mcxr.play.input.XrInput;
import net.sorenon.mcxr.play.openxr.OpenXR;
import net.sorenon.mcxr.play.openxr.XrRenderer;
import net.sorenon.mcxr.play.rendering.VrFirstPersonRenderer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.lwjgl.openxr.XR;

import java.nio.file.Path;

public class MCXRPlayClient implements ClientModInitializer {

    public static boolean resourcesInitialized = false;

    private static final Logger LOGGER = LogManager.getLogger("MCXR");

    public static final OpenXR OPEN_XR = new OpenXR();
    public static final XrRenderer RENDERER = new XrRenderer();

    public static MCXRPlayClient INSTANCE;
    public FlatGuiManager flatGuiManager = new FlatGuiManager();
    public VrFirstPersonRenderer vrFirstPersonRenderer = new VrFirstPersonRenderer(flatGuiManager);
    public static final ControllerPoses viewSpacePoses = new ControllerPoses();

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

        /*
         * For the majority of rendering
         * Hands, Shadow, Items
         */
        WorldRenderEvents.AFTER_ENTITIES.register(context -> vrFirstPersonRenderer.renderAfterEntities(context));

        /*
         * For rendering things that need access to the completed depth buffer
         * HUD, GUI, 'Crosshair', Debug lines
         * This has major issues with Iris
         */
        WorldRenderEvents.LAST.register(context -> {
            if (!MinecraftClient.getInstance().options.hudHidden) {
                vrFirstPersonRenderer.renderHud(context);
            }
        });
    }

    public static void resetView() {
        MCXRPlayClient.xrOffset = new Vector3f(0, 0, 0).sub(MCXRPlayClient.viewSpacePoses.getPhysicalPose().getPos().mul(getCameraScale(), new Vector3f())).mul(1, 0, 1);
    }

    public static float getCameraScale() {
        return getCameraScale(1.0f);
    }

    public static float getCameraScale(float delta) {
        var cam = MinecraftClient.getInstance().cameraEntity;
        if (cam == null) {
            return 1;
        } else {
            return MCXRCore.getScale(cam, delta);
        }
    }
}
