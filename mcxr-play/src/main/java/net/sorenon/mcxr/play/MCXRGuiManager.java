package net.sorenon.mcxr.play;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.sorenon.mcxr.core.JOMLUtil;
import net.sorenon.mcxr.play.rendering.UnownedTexture;
import net.sorenon.mcxr.play.rendering.MCXRCamera;
import org.jetbrains.annotations.Nullable;
import org.joml.*;

public class MCXRGuiManager {

    public final int guiFramebufferWidth = 1980;
    public final int guiFramebufferHeight = 1080;

    public final ResourceLocation guiRenderTexture = new ResourceLocation("mcxr", "net/sorenon/mcxr/play/gui");

    public RenderTarget guiRenderTarget;
    public RenderTarget guiPostProcessRenderTarget;
    public RenderTarget overlayRenderTarget;
    public double guiScale;

    public int scaledWidth;
    public int scaledHeight;

    public boolean needsReset = true;

    public float size = 1.5f;

    /**
     * The transform of the GUI in physical space
     */
    public Vec3 position = null;
    public Quaterniond orientation = new Quaterniond(0, 0, 0, 1);

    public void init() {
        guiScale = calcGuiScale();

        int widthFloor = (int) (guiFramebufferWidth / guiScale);
        scaledWidth = guiFramebufferWidth / guiScale > widthFloor ? widthFloor + 1 : widthFloor;

        int heightFloor = (int) (guiFramebufferHeight / guiScale);
        scaledHeight = guiFramebufferHeight / guiScale > heightFloor ? heightFloor + 1 : heightFloor;

        guiRenderTarget = new TextureTarget(guiFramebufferWidth, guiFramebufferHeight, true, Minecraft.ON_OSX);
        guiRenderTarget.setClearColor(0, 0, 0, 0);
        guiPostProcessRenderTarget = new TextureTarget(guiFramebufferWidth, guiFramebufferHeight, false, Minecraft.ON_OSX);
        Minecraft.getInstance().getTextureManager().register(guiRenderTexture, new UnownedTexture(guiPostProcessRenderTarget.getColorTextureId()));
        overlayRenderTarget = new TextureTarget(guiFramebufferWidth, guiFramebufferHeight, false, Minecraft.ON_OSX);
        Minecraft.getInstance().getTextureManager().register(guiRenderTexture, new UnownedTexture(overlayRenderTarget.getColorTextureId()));
    }

    @SuppressWarnings("ConditionCoveredByFurtherCondition")
    public double calcGuiScale() {
        int guiScale = 4;
        boolean forceUnicodeFont = Minecraft.getInstance().isEnforceUnicode();

        int scale;
        scale = 1;
        while (scale != guiScale && scale < guiFramebufferWidth && scale < guiFramebufferHeight && guiFramebufferWidth / (scale + 1) >= 320 && guiFramebufferHeight / (scale + 1) >= 240) {
            ++scale;
        }

        if (forceUnicodeFont && scale % 2 != 0) {
            ++scale;
        }
        return scale;
    }

    public boolean isScreenOpen() {
        return Minecraft.getInstance().screen != null;
    }

    public void handleOpenScreen(@Nullable Screen screen) {
        if (screen == null) {
            position = null;
            orientation.set(0, 0, 0, 1);
            needsReset = false;
        } else if (position == null) {
            resetTransform();
        }
    }

    public void resetTransform() {
        MCXRCamera camera = (MCXRCamera) Minecraft.getInstance().gameRenderer.getMainCamera();
        if (camera.isInitialized()) {
            orientation = JOMLUtil.convertd(camera.rotation());
            position = JOMLUtil.convert(MCXRPlayClient.viewSpacePoses.getUnscaledPhysicalPose().getPos().add(orientation.transform(new Vector3f(0, -0.5f, 1))));
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
