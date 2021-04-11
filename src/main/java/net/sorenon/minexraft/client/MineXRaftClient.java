package net.sorenon.minexraft.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.sorenon.minexraft.client.input.VanillaCompatActionSet;
import net.sorenon.minexraft.client.input.XrInput;
import org.joml.Vector3f;
import org.lwjgl.openxr.XR;
import org.lwjgl.openxr.XrFovf;
import org.lwjgl.openxr.XrRect2Di;
import org.lwjgl.system.SharedLibrary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

import static org.lwjgl.system.APIUtil.apiCreateLibrary;
import static org.lwjgl.system.APIUtil.apiLog;

public class MineXRaftClient implements ClientModInitializer {

    public static final OpenXR OPEN_XR = new OpenXR();
    public static MineXRaftClient INSTANCE;
    public static XrInput XR_INPUT;
    public static VanillaCompatActionSet vanillaCompatActionSet;
    public static XrRenderPass renderPass = XrRenderPass.VANILLA;

    public static XrRect2Di viewportRect = null;
    public static Framebuffer primaryRenderTarget = null;
    public static XrFovf fov = null;
    public static Pose eyePose = new Pose();
    public static int viewIndex = 0;

    public static Framebuffer guiFramebuffer = null;

    public static void tmpResetSize() {
//        if (viewportRect != null) {
//            framebufferWidth = viewportRect.extent().width();
//            framebufferHeight = viewportRect.extent().height();
//        }
        primaryRenderTarget = MinecraftClient.getInstance().getFramebuffer();
    }

    public static final Pose headPose = new Pose();

    //    public static Vec3d xrOrigin = new Vec3d(0, 0, 0); //The center of the STAGE set at the same height of the PlayerEntity's feet
    public static Vector3f xrOffset = new Vector3f(0, 0, 0);
    public static float yawTurn = 0;

    public XrHandRenderer handRenderer = new XrHandRenderer();

    @Override
    public void onInitializeClient() {
        INSTANCE = this;

        String loaderPath = "";
        try { //TODO bundle loader binaries with the mod
            File configFile = FabricLoader.getInstance().getConfigDir().resolve("mcxr.properties").toFile();
            if (!configFile.exists()) {
                if (!configFile.createNewFile()) {
                    System.err.println("[MCXR] Could not create config file: " + configFile.getAbsolutePath());
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

        SharedLibrary defaultOpenXRLoader = loadNative(XR.class, loaderPath, "openxr_loader");
        XR.create(defaultOpenXRLoader);
        OPEN_XR.createOpenXRInstance();
        OPEN_XR.initializeOpenXRSystem();

        System.out.println("Hello Fabric world!");
///execute in minecraft:overworld run tp @s 5804.48 137.00 -4601.16 3.23 72.30
        WorldRenderEvents.LAST.register(context -> {
            handRenderer.renderHandsDebug(context.camera());
        });

        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            handRenderer.renderHands(context);
        });
    }

    public static void resetView() {
        MineXRaftClient.xrOffset = new Vector3f(0, 0, 0).sub(MineXRaftClient.headPose.getPos()).mul(1, 0, 1);
    }

    private static SharedLibrary loadNative(Class<?> context, String path, String libName) {
        apiLog("Loading library: " + path);

        // METHOD 1: absolute path
        if (Paths.get(path).isAbsolute()) {
            SharedLibrary lib = apiCreateLibrary(path);
            apiLog("\tSuccess");
            return lib;
        }

        throw new UnsatisfiedLinkError("Failed to locate library: " + libName);
    }
}
