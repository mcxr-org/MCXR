package net.sorenon.minexraft.client.accessor;

public interface MinecraftClientEXT {

    void preRenderXR(boolean tick, long frameStartTime);

    void doRenderXR(boolean tick, long frameStartTime);

    void postRenderXR(boolean tick, long frameStartTime);
}
