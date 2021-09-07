package net.sorenon.mcxr.play;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.sorenon.mcxr.core.JOMLUtil;
import net.sorenon.mcxr.play.rendering.ExistingTexture;
import net.sorenon.mcxr.play.rendering.XrCamera;
import org.jetbrains.annotations.Nullable;
import org.joml.*;

public class FlatGuiManager {

    public final int framebufferWidth = 1980;
    public final int framebufferHeight = 1080;

    public final Identifier texture = new Identifier("mcxr", "gui");
    public Framebuffer backFramebuffer;
    public Framebuffer frontFramebuffer;

    public double guiScale;

    public int scaledWidth;
    public int scaledHeight;

    public boolean needsReset = false;

    public float size = 1.5f;

    /**
     * The transform of the GUI in physical space
     */
    public Vec3d position = null;
    public Quaterniond orientation = new Quaterniond(0, 0, 0, 1);

    public void init() {
        guiScale = calcGuiScale();

        int widthFloor = (int) (framebufferWidth / guiScale);
        scaledWidth = framebufferWidth / guiScale > widthFloor ? widthFloor + 1 : widthFloor;

        int heightFloor = (int) (framebufferHeight / guiScale);
        scaledHeight = framebufferHeight / guiScale > heightFloor ? heightFloor + 1 : heightFloor;

        backFramebuffer = new SimpleFramebuffer(framebufferWidth, framebufferHeight, true, MinecraftClient.IS_SYSTEM_MAC);
        backFramebuffer.setClearColor(0, 0, 0, 0);
        frontFramebuffer = new SimpleFramebuffer(framebufferWidth, framebufferHeight, false, MinecraftClient.IS_SYSTEM_MAC);
        MinecraftClient.getInstance().getTextureManager().registerTexture(texture, new ExistingTexture(frontFramebuffer.getColorAttachment()));
    }

    @SuppressWarnings("ConditionCoveredByFurtherCondition")
    public double calcGuiScale() {
        int guiScale = 4;
        boolean forceUnicodeFont = MinecraftClient.getInstance().forcesUnicodeFont();

        int scale;
        scale = 1;
        while (scale != guiScale && scale < framebufferWidth && scale < framebufferHeight && framebufferWidth / (scale + 1) >= 320 && framebufferHeight / (scale + 1) >= 240) {
            ++scale;
        }

        if (forceUnicodeFont && scale % 2 != 0) {
            ++scale;
        }
        return scale;
    }

    public boolean isScreenOpen() {
        return position != null && MinecraftClient.getInstance().currentScreen != null;
    }

    public void openScreen(@Nullable Screen screen) {
        if (screen == null) {
            position = null;
            orientation.set(0, 0, 0, 1);
            needsReset = false;
        } else if (MinecraftClient.getInstance().currentScreen == null) {
            resetTransform();
        }
    }

    public void resetTransform() {
        XrCamera camera = (XrCamera) MinecraftClient.getInstance().gameRenderer.getCamera();
        if (camera.isReady()) {
            Quaterniond orientation = JOMLUtil.convertd(camera.getRotation());
            position = camera.getPos().add(JOMLUtil.convert(orientation.transform(new Vector3d(0, -0.5, 1)))).subtract(JOMLUtil.convert(MCXRPlayClient.xrOrigin));
            this.orientation = orientation;
            needsReset = false;
        } else {
            needsReset = true;
        }
    }

    @Nullable
    public Vector3d guiRaycast(Vector3d rayPos, Vector3d rayDir) {
        if (position == null) {
            return null;
        }
        double distance = Intersectiond.intersectRayPlane(
                rayPos,
                rayDir,
                JOMLUtil.convert(position),
                orientation.transform(new Vector3d(0, 0, -1)),
                0.1f
        );
        if (distance >= 0) {
            return rayDir.mul(distance, new Vector3d()).add(rayPos);
        }
        return null;
    }
}
