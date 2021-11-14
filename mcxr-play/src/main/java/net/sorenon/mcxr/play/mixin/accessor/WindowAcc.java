package net.sorenon.mcxr.play.mixin.accessor;

import com.mojang.blaze3d.platform.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Window.class)
public interface WindowAcc {

    @Accessor()
    boolean getActuallyFullscreen();

    @Accessor()
    boolean getVsync();

    @Accessor()
    void setActuallyFullscreen(boolean currentFullscreen);

    @Invoker()
    void invokeUpdateFullscreen(boolean vsync);
}