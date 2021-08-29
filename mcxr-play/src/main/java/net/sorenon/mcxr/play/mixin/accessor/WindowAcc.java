package net.sorenon.mcxr.play.mixin.accessor;

import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Window.class)
public interface WindowAcc {

    @Accessor()
    boolean getCurrentFullscreen();

    @Accessor()
    boolean getVsync();

    @Accessor()
    void setCurrentFullscreen(boolean currentFullscreen);

    @Invoker()
    void invokeUpdateFullscreen(boolean vsync);
}