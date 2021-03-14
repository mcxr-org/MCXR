package net.sorenon.minexraft.accessor;

import net.minecraft.client.gl.Framebuffer;

public interface MinecraftClientEXT {

    void preRenderXR(boolean tick, long frameStartTime);

    void doRenderXR(boolean tick, long frameStartTime);

    void postRenderXR(boolean tick, long frameStartTime);

    Framebuffer swapchainFramebuffer();

    void setFramebuffer(Framebuffer framebuffer);
}
