package net.sorenon.mcxr.play;

import com.electronwill.nightconfig.core.file.FileConfig;
import net.fabricmc.loader.api.FabricLoader;

public class PlayOptions {

    private static FileConfig fileConfig;

    public static boolean xrUninitialized = false;
    public static boolean xrPaused = false;

    public static MoveDirectionPose walkDirection = MoveDirectionPose.LeftHand;
    public static MoveDirectionPose swimDirection = MoveDirectionPose.RightHand;
    public static MoveDirectionPose flyDirection = MoveDirectionPose.RightHand;

    /**
     * The angle to rotate the player's in-game hand for a more comfortable experience
     * May be different for different controllers -> needs testing
     */
    public static float handPitchAdjust = 30;

    public static float SSAA = 1;

    public static void init() {
        fileConfig = FileConfig.of(FabricLoader.getInstance().getConfigDir().resolve("mcxr-play.toml"));
    }

    public static void save() {
        fileConfig.set("disableMCXR", xrUninitialized);
        fileConfig.set("pauseMCXR", xrPaused);

        fileConfig.set("walkDirection", walkDirection);
        fileConfig.set("swimDirection", swimDirection);
        fileConfig.set("flyDirection", flyDirection);

        fileConfig.set("handPitchAdjust", handPitchAdjust);

        fileConfig.set("SSAA", SSAA);
        fileConfig.save();
    }

    public static void load() {
        fileConfig.load();
        xrUninitialized = fileConfig.getOrElse("disableMCXR", false);
        xrPaused = fileConfig.getOrElse("pauseMCXR", false);

        walkDirection = fileConfig.getEnumOrElse("walkDirection", MoveDirectionPose.LeftHand);
        swimDirection = fileConfig.getEnumOrElse("swimDirection", MoveDirectionPose.RightHand);
        flyDirection = fileConfig.getEnumOrElse("flyDirection", MoveDirectionPose.RightHand);

        handPitchAdjust = fileConfig.<Number>getOrElse("handPitchAdjust", 30f).floatValue();

        SSAA = fileConfig.<Number>getOrElse("SSAA", 1).floatValue();
    }
}
