package net.sorenon.mcxr.play.mixin.accessor;

import com.mojang.blaze3d.pipeline.RenderTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderTarget.class)
public interface RenderTargetAcc {

    @Accessor
    void setColorTextureId(int colorAttachment);

    @Accessor
    void setDepthBufferId(int depthAttachment);
}
