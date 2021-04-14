package net.sorenon.minexraft.client;

import net.minecraft.client.gl.Framebuffer;
import net.sorenon.minexraft.client.mixin.accessor.FramebufferExt;

public class MainRenderTarget extends Framebuffer {

    public final Framebuffer defaultFramebuffer;
    private Framebuffer currentFramebuffer;

    public MainRenderTarget(int width, int height, boolean useDepth, boolean getError) {
        super(width, height, useDepth, getError);
        defaultFramebuffer = new Framebuffer(width, height, useDepth, getError);
        setFramebuffer(defaultFramebuffer);
    }

    public void setFramebuffer(Framebuffer framebuffer) {
        this.currentFramebuffer = defaultFramebuffer;

        this.textureWidth = framebuffer.textureWidth;
        this.textureHeight = framebuffer.textureHeight;
        this.viewportWidth = framebuffer.viewportWidth;
        this.viewportHeight = framebuffer.viewportHeight;
        this.fbo = framebuffer.fbo;
        this.clearColor[0] = framebuffer.clearColor[0];
        this.clearColor[1] = framebuffer.clearColor[1];
        this.clearColor[2] = framebuffer.clearColor[2];
        this.clearColor[3] = framebuffer.clearColor[3];
        this.texFilter = framebuffer.texFilter;

        FramebufferExt thiz = ((FramebufferExt) this);
        thiz.colorAttachment(framebuffer.getColorAttachment());
        thiz.depthAttachment(framebuffer.getDepthAttachment());
    }

    public void reset() {
        setFramebuffer(defaultFramebuffer);
    }

    public Framebuffer getFramebuffer() {
        return currentFramebuffer;
    }

    public Framebuffer getDefaultFramebuffer() {
        return defaultFramebuffer;
    }

    public boolean isCustomFramebuffer() {
        return currentFramebuffer != defaultFramebuffer;
    }

    public void resize(int width, int height, boolean getError) {
        if (currentFramebuffer != null) {
            currentFramebuffer.resize(width, height, getError);
        }
    }

    public void delete() {
        currentFramebuffer.delete();
    }

    public void copyDepthFrom(Framebuffer framebuffer) {
        currentFramebuffer.copyDepthFrom(framebuffer);
    }

    public void initFbo(int width, int height, boolean getError) {
        currentFramebuffer.initFbo(width, height, getError);
    }

    public void setTexFilter(int i) {
        currentFramebuffer.setTexFilter(i);
    }

    public void checkFramebufferStatus() {
        currentFramebuffer.checkFramebufferStatus();
    }

    public void beginRead() {
        currentFramebuffer.beginRead();
    }

    public void endRead() {
        currentFramebuffer.endRead();
    }

    public void beginWrite(boolean setViewport) {
        currentFramebuffer.beginWrite(setViewport);
    }

    public void endWrite() {
        currentFramebuffer.endWrite();
    }

    public void setClearColor(float r, float g, float b, float a) {
        currentFramebuffer.setClearColor(r, g, b, a);
    }

    public void draw(int width, int height) {
        currentFramebuffer.draw(width, height);
    }

    public void draw(int width, int height, boolean bl) {
        currentFramebuffer.draw(width, height, bl);
    }

    public void clear(boolean getError) {
        currentFramebuffer.clear(getError);
    }

    public int getColorAttachment() {
        return currentFramebuffer.getColorAttachment();
    }

    public int getDepthAttachment() {
        return currentFramebuffer.getDepthAttachment();
    }
}
