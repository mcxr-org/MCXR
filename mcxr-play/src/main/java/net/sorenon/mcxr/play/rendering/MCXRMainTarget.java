package net.sorenon.mcxr.play.rendering;

import net.minecraft.client.Minecraft;
import net.sorenon.mcxr.play.mixin.accessor.RenderTargetAcc;
import com.mojang.blaze3d.pipeline.MainTarget;
import com.mojang.blaze3d.pipeline.RenderTarget;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class provides a system to change the main framebuffer that the game renders to while providing the illusion that
 * it is always the same framebuffer. This limits compat issues while still providing a simple interface for changing the
 * size and target of the main framebuffer.
 * However, this class is also the main incompatibility issue with Canvas so a different way to achieve this is needed
 */
//TODO Find a way to make this compatible with Canvas's PrimaryFrameBuffer / FabulousFrameBuffer?
public class MCXRMainTarget extends MainTarget {

    public static final Logger LOGGER = Logger.getLogger("MCXR");

    //The framebuffer used for rendering to the window
    public final MainTarget minecraftMainRenderTarget;

    //The framebuffer that is affected by draw calls
    private RenderTarget currentFramebuffer;

    //The current dimensions of all the vanilla framebuffers
    public int minecraftFramebufferWidth;
    public int minecraftFramebufferHeight;

    public MCXRMainTarget(int width, int height) {
        super(width, height);
        minecraftMainRenderTarget = new MainTarget(width, height);
        setFramebuffer(minecraftMainRenderTarget);
        minecraftFramebufferWidth = width;
        minecraftFramebufferHeight = height;
    }

    //Used to set the current framebuffer without resizing the dimensions of the other framebuffers
    //This is meant for the defaultFramebuffer and any framebuffers used in rendering gui
    public void setFramebuffer(RenderTarget framebuffer) {
        this.currentFramebuffer = framebuffer;

        this.width = framebuffer.width;
        this.height = framebuffer.height;
        this.viewWidth = framebuffer.viewWidth;
        this.viewHeight = framebuffer.viewHeight;
        this.frameBufferId = framebuffer.frameBufferId;
//        this.clearColor[0] = framebuffer.clearColor[0];
//        this.clearColor[1] = framebuffer.clearColor[1];
//        this.clearColor[2] = framebuffer.clearColor[2];
//        this.clearColor[3] = framebuffer.clearColor[3];
        this.filterMode = framebuffer.filterMode;

        RenderTargetAcc acc = ((RenderTargetAcc) this);
        acc.setColorTextureId(framebuffer.getColorTextureId());
        acc.setDepthBufferId(framebuffer.getDepthTextureId());
    }

    public void setXrFramebuffer(RenderTarget framebuffer) {
        setFramebuffer(framebuffer);
        if (framebuffer.width != minecraftFramebufferWidth ||
                framebuffer.height != minecraftFramebufferHeight) {
            Minecraft.getInstance().gameRenderer.resize(framebuffer.width, framebuffer.height);
            LOGGER.log(Level.FINE, "Resizing GameRenderer");
        }
    }

    public void resetFramebuffer() {
        setFramebuffer(minecraftMainRenderTarget);
    }

    public RenderTarget getFramebuffer() {
        return currentFramebuffer;
    }

    public RenderTarget getMinecraftMainRenderTarget() {
        return minecraftMainRenderTarget;
    }

    public boolean isCustomFramebuffer() {
        return currentFramebuffer != minecraftMainRenderTarget;
    }

    public void resize(int width, int height, boolean getError) {
        if (minecraftMainRenderTarget != null) {
            minecraftMainRenderTarget.resize(width, height, getError);
        }
    }

    public void destroyBuffers() {
        minecraftMainRenderTarget.destroyBuffers();
    }

    public void copyDepthFrom(RenderTarget framebuffer) {
        currentFramebuffer.copyDepthFrom(framebuffer);
    }

    public void createBuffers(int width, int height, boolean getError) {
        currentFramebuffer.createBuffers(width, height, getError);
    }

    public void setFilterMode(int i) {
        currentFramebuffer.setFilterMode(i);
    }

    public void checkStatus() {
        currentFramebuffer.checkStatus();
    }

    public void bindRead() {
        currentFramebuffer.bindRead();
    }

    public void unbindRead() {
        currentFramebuffer.unbindRead();
    }

    public void bindWrite(boolean setViewport) {
        currentFramebuffer.bindWrite(setViewport);
    }

    public void unbindWrite() {
        currentFramebuffer.unbindWrite();
    }

    public void setClearColor(float r, float g, float b, float a) {
        currentFramebuffer.setClearColor(r, g, b, a);
    }

    public void blitToScreen(int width, int height) {
        currentFramebuffer.blitToScreen(width, height);
    }

    public void blitToScreen(int width, int height, boolean bl) {
        currentFramebuffer.blitToScreen(width, height, bl);
    }

    public void clear(boolean getError) {
        currentFramebuffer.clear(getError);
    }

    public int getColorTextureId() {
        return currentFramebuffer.getColorTextureId();
    }

    public int getDepthTextureId() {
        return currentFramebuffer.getDepthTextureId();
    }
}
