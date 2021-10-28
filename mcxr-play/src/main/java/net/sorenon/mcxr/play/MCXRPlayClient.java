package net.sorenon.mcxr.play;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.sorenon.fart.FartRenderEvents;
import net.sorenon.mcxr.core.MCXRScale;
import net.sorenon.mcxr.play.input.ControllerPoses;
import net.sorenon.mcxr.play.openxr.OpenXR;
import net.sorenon.mcxr.play.openxr.XrRenderer;
import net.sorenon.mcxr.play.rendering.RenderPass;
import net.sorenon.mcxr.play.rendering.VrFirstPersonRenderer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.lwjgl.openxr.XR;
import virtuoel.pehkui.util.ScaleUtils;

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
     * Allows the player to rotate around a central point or 'reset' their position to the center of the PlayerEntity
     */
    public static Vector3f xrOffset = new Vector3f(0, 0, 0);

    /**
     * (if roomscale movement is enabled)
     * The negated position of the player entity in physical space
     */
    public static Vector3d roomscalePlayerOffset = new Vector3d();

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
        XR.create("openxr_loader");
        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            if (RENDERER.renderPass instanceof RenderPass.World) {
                if (!MinecraftClient.getInstance().options.hudHidden && !flatGuiManager.isScreenOpen()) {
                    Camera camera = context.camera();
                    if (camera.getFocusedEntity() instanceof ClientPlayerEntity player) {
                        vrFirstPersonRenderer.renderHandsAndItems(
                                player,
                                VrFirstPersonRenderer.getLight(camera, context.world()),
                                context.matrixStack(),
                                context.consumers(),
                                context.tickDelta()
                        );
                    }
                }
            }
        });

        FartRenderEvents.LAST.register(context -> {
            if (RENDERER.renderPass instanceof RenderPass.World) {
                if (!MinecraftClient.getInstance().options.hudHidden) {
                    vrFirstPersonRenderer.renderFirstPerson(context);
                }
            }
        });
    }

    public static Identifier id(String name) {
        return new Identifier("mcxr-play", name);
    }

    public static void resetView() {
        Vector3f pos = new Vector3f(MCXRPlayClient.viewSpacePoses.getRawPhysicalPose().getPos());
        new Quaternionf().rotateLocalY(yawTurn).transform(pos);
        pos.mul(getCameraScale());

        MCXRPlayClient.xrOffset = new Vector3f(0, 0, 0).sub(pos).mul(1, 0, 1);
    }

    public static float getCameraScale() {
        return getCameraScale(1.0f);
    }

    public static float getCameraScale(float delta) {
        var cam = MinecraftClient.getInstance().cameraEntity;
        if (cam == null) {
            return 1;
        } else {
            return MCXRScale.getScale(cam, delta);
        }
    }

    public static float modifyProjectionMatrixDepth(float depth, Entity entity, float tickDelta) {
        if (FabricLoader.getInstance().isModLoaded("pehkui")) {
            return ScaleUtils.modifyProjectionMatrixDepth(MCXRPlayClient.getCameraScale(tickDelta), depth, entity, tickDelta);
        }
        return depth;
    }
}
