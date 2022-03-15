package net.sorenon.mcxr.play.accessor;

import net.sorenon.mcxr.play.rendering.RenderPass;

public interface MinecraftExt {

    void preRender(boolean tick, Runnable preTick);

    void doRender(boolean tick, long frameStartTime, RenderPass renderPass);

    void postRender();
}
