package net.sorenon.mcxr.play.mixin.flatgui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import net.sorenon.mcxr.play.MCXRPlayClient;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;

@Mixin(Screen.class)
public class ScreenMixin extends DrawableHelper {

    @Shadow
    protected TextRenderer textRenderer;

    @Shadow
    public int width;

    @Shadow
    public int height;

    @Shadow
    protected ItemRenderer itemRenderer;

    @Shadow
    @Nullable
    protected MinecraftClient client;

    @Inject(method = "renderBackground(Lnet/minecraft/client/util/math/MatrixStack;I)V", at = @At("HEAD"), cancellable = true)
    void cancelBackground(MatrixStack matrices, int vOffset, CallbackInfo ci) {
        if (MCXRPlayClient.isXrMode()) {
            ci.cancel();
        }
    }

    /**
     * This hack half fixes a really weird bug where alpha values in the framebuffer get overridden when they shouldn't
     * I reckon this is just a result of mojank seeping through the cracks TODO find a better fix for this (will probably need a custom gui quad frag shader)
     * @author Sorenon
     */
//    @Overwrite
    @Unique
    private void renderTooltipFromComponents(MatrixStack matrices, List<TooltipComponent> components, int x, int y) {
        if (!components.isEmpty()) {
            int i = 0;
            int j = components.size() == 1 ? -2 : 0;

            TooltipComponent tooltipComponent;
            for (Iterator var7 = components.iterator(); var7.hasNext(); j += tooltipComponent.getHeight()) {
                tooltipComponent = (TooltipComponent) var7.next();
                int k = tooltipComponent.getWidth(this.textRenderer);
                if (k > i) {
                    i = k;
                }
            }

            int l = x + 12;
            int m = y - 12;
            if (l + i > this.width) {
                l -= 28 + i;
            }

            if (m + j + 6 > this.height) {
                m = this.height - j - 6;
            }

            matrices.push();
            int p = -267386864; //black
            int q = 1347420415; //purple
            int r = 1344798847; //purple
            float f = this.itemRenderer.zOffset;
            this.itemRenderer.zOffset = 400.0F;
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            Matrix4f matrix4f = matrices.peek().getModel();

            fillGradient(matrix4f, bufferBuilder, l - 3, m - 4, l + i + 3, m - 3, 400, p, p);
            fillGradient(matrix4f, bufferBuilder, l - 3, m + j + 3, l + i + 3, m + j + 4, 400, p, p);
            fillGradient(matrix4f, bufferBuilder, l - 3, m - 3, l + i + 3, m + j + 3, 400, p, p);
            fillGradient(matrix4f, bufferBuilder, l - 4, m - 3, l - 3, m + j + 3, 400, p, p);
            fillGradient(matrix4f, bufferBuilder, l + i + 3, m - 3, l + i + 4, m + j + 3, 400, p, p);

            fillGradient(matrix4f, bufferBuilder, l - 3, m - 3 + 1, l - 3 + 1, m + j + 3 - 1, 400, q, r);
            fillGradient(matrix4f, bufferBuilder, l + i + 2, m - 3 + 1, l + i + 3, m + j + 3 - 1, 400, q, r);
            fillGradient(matrix4f, bufferBuilder, l - 3, m - 3, l + i + 3, m - 3 + 1, 400, q, q);
            fillGradient(matrix4f, bufferBuilder, l - 3, m + j + 2, l + i + 3, m + j + 3, 400, r, r);
            RenderSystem.enableDepthTest();
            RenderSystem.disableTexture();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.colorMask(true, true, true, false);
            bufferBuilder.end();
            BufferRenderer.draw(bufferBuilder);
            RenderSystem.colorMask(true, true, true, true);
            RenderSystem.disableBlend();
            RenderSystem.enableTexture();
            VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
            matrices.translate(0.0D, 0.0D, 400.0D);
            int t = m;

            int v;
            TooltipComponent tooltipComponent3;
            for(v = 0; v < components.size(); ++v) {
                tooltipComponent3 = (TooltipComponent)components.get(v);
                tooltipComponent3.drawText(this.textRenderer, l, t, matrix4f, immediate);
                t += tooltipComponent3.getHeight() + (v == 0 ? 2 : 0);
            }

            immediate.draw();
            matrices.pop();
            t = m;

            for(v = 0; v < components.size(); ++v) {
                tooltipComponent3 = (TooltipComponent)components.get(v);
                tooltipComponent3.drawItems(this.textRenderer, l, t, matrices, this.itemRenderer, 400, this.client.getTextureManager());
                t += tooltipComponent3.getHeight() + (v == 0 ? 2 : 0);
            }

            this.itemRenderer.zOffset = f;
        }
    }
}
