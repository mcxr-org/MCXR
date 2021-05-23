package net.sorenon.minexraft.client.accessor;

public interface MinecraftClientExt {

    void preRenderXR(boolean tick, long frameStartTime);

    void doRenderXR(boolean tick, long frameStartTime);

    void postRenderXR(boolean tick, long frameStartTime);

    void render();
}
