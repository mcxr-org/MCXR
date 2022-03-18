package net.sorenon.mcxr.play;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.sorenon.fart.FartRenderEvents;
import net.sorenon.mcxr.core.MCXRCore;
import net.sorenon.mcxr.core.MCXRScale;
import net.sorenon.mcxr.play.input.ControllerPoses;
import net.sorenon.mcxr.play.openxr.MCXRGameRenderer;
import net.sorenon.mcxr.play.openxr.OpenXRState;
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

    public static Logger LOGGER = LogManager.getLogger("MCXR");

    public static final OpenXRState OPEN_XR_STATE = new OpenXRState();
    public static final MCXRGameRenderer MCXR_GAME_RENDERER = new MCXRGameRenderer();

    public static MCXRPlayClient INSTANCE;
    public MCXRGuiManager MCXRGuiManager = new MCXRGuiManager();
    public VrFirstPersonRenderer vrFirstPersonRenderer = new VrFirstPersonRenderer(MCXRGuiManager);
    public static final ControllerPoses viewSpacePoses = new ControllerPoses();

    //Stage space => Unscaled Physical Space => Physical Space => Minecraft Space
    //OpenXR         GUI                        Roomscale Logic   Minecraft Logic
    //      Rotated + Translated           Scaled          Translated

    public static boolean heightAdjustStand = false;

    public static float heightAdjust = 0;

    /**
     * The yaw rotation of STAGE space in physical space
     * Used to let the user turn
     */
    public static float stageTurn = 0;

    /**
     * The position of STAGE space in physical space
     * Used to let the user turn around one physical space position and
     * to let the user snap to the player entity position when roomscale movement is off
     */
    public static Vector3f stagePosition = new Vector3f(0, 0, 0);

    /**
     * The position of physical space in Minecraft space
     * xrOrigin = camaraEntity.pos - playerPhysicalPosition
     */
    public static Vector3d xrOrigin = new Vector3d(0, 0, 0);

    /**
     * The position of the player entity in physical space
     * If roomscale movement is disabled this vector is zero (meaning the player is at xrOrigin)
     * This is used to calculate xrOrigin
     */
    public static Vector3d playerPhysicalPosition = new Vector3d();

    public static int getMainHand() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            return player.getMainArm().ordinal();
        } else {
            return HumanoidArm.RIGHT.ordinal();
        }
    }

    @Override
    public void onInitializeClient() {
        PlayOptions.init();
        PlayOptions.load();
        PlayOptions.save();

        INSTANCE = this;
        if (!PlayOptions.xrUninitialized) {
            XR.create("openxr_loader");
        }

        ClientLifecycleEvents.CLIENT_STARTED.register(MCXR_GAME_RENDERER::initialize);

        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            if (MCXR_GAME_RENDERER.renderPass instanceof RenderPass.XrWorld) {
                if (!Minecraft.getInstance().options.hideGui && !MCXRGuiManager.isScreenOpen()) {
                    Camera camera = context.camera();
                    if (camera.getEntity() instanceof LocalPlayer player) {
                        vrFirstPersonRenderer.render(
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
            if (MCXR_GAME_RENDERER.renderPass instanceof RenderPass.XrWorld) {
                vrFirstPersonRenderer.renderLast(context);
            }
        });
    }

    public static ResourceLocation id(String name) {
        return new ResourceLocation("mcxr-play", name);
    }

    public static void resetView() {
        Vector3f pos = new Vector3f(MCXRPlayClient.viewSpacePoses.getStagePose().getPos());
        new Quaternionf().rotateLocalY(stageTurn).transform(pos);
        if (MCXRCore.getCoreConfig().roomscaleMovement()) {
            playerPhysicalPosition.set(MCXRPlayClient.viewSpacePoses.getPhysicalPose().getPos());
        } else {
            playerPhysicalPosition.zero();
        }

        MCXRPlayClient.stagePosition = new Vector3f(0, 0, 0).sub(pos).mul(1, 0, 1);
    }

    public static float getCameraScale() {
        return getCameraScale(1.0f);
    }

    public static float getCameraScale(float delta) {
        var cam = Minecraft.getInstance().cameraEntity;
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
