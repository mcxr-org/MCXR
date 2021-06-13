package net.sorenon.minexraft.client.mixin.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.sorenon.minexraft.client.MineXRaftClient;
import net.sorenon.minexraft.client.rendering.RenderPass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    /**
     * Skip rendering the vignette then setup the GUI rendering state
     */
    @Inject(method = "renderVignetteOverlay", at = @At("HEAD"), cancellable = true)
    void cancelRenderVignette(Entity entity, CallbackInfo ci) {
        if (MineXRaftClient.renderPass != RenderPass.VANILLA) {
            RenderSystem.enableDepthTest();
            RenderSystem.defaultBlendFunc();
            ci.cancel();
        }
    }

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    void cancelRenderCrosshair(MatrixStack matrices, CallbackInfo ci){
        if (MineXRaftClient.renderPass != RenderPass.VANILLA) {
            ci.cancel();
        }
    }
}
