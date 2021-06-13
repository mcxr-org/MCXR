package net.sorenon.minexraft.client.accessor;

public interface MinecraftClientExt {

    void preRenderXR(boolean tick, Runnable afterTick);

    void doRenderXR(boolean tick, long frameStartTime);

    void postRenderXR(boolean tick);

    void render();
}
