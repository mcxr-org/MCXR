package net.sorenon.minexraft.client.mixin.accessor;

import net.minecraft.client.gl.Framebuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Framebuffer.class)
public interface FramebufferAcc {

    @Accessor("colorAttachment")
    void colorAttachment(int colorAttachment);

    @Accessor("depthAttachment")
    void depthAttachment(int depthAttachment);
}
