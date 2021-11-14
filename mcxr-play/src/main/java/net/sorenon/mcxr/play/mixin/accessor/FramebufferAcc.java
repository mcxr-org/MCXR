package net.sorenon.mcxr.play.mixin.accessor;

import com.mojang.blaze3d.pipeline.RenderTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderTarget.class)
public interface FramebufferAcc {

    @Accessor("colorTextureId")
    void colorAttachment(int colorAttachment);

    @Accessor("depthBufferId")
    void depthAttachment(int depthAttachment);
}
