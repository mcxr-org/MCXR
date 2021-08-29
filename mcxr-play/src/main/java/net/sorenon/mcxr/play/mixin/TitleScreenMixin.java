package net.sorenon.mcxr.play.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.play.openxr.OpenXR;
import net.sorenon.mcxr.play.openxr.OpenXRSystem;
import org.apache.commons.lang3.text.WordUtils;
import org.lwjgl.openxr.XR10;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

    @Shadow
    @Final
    private boolean doBackgroundFade;

    @Shadow
    private long backgroundFadeStart;

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("HEAD"))
    void init(CallbackInfo ci) {
        int y = this.height / 4 + 48;
        this.addDrawableChild(new ButtonWidget(
                this.width / 2 + 104,
                y,
                90,
                20,
                new TranslatableText("mcxr.menu.reload"),
                button -> MCXRPlayClient.OPEN_XR.tryInitialize()));
    }

    @Inject(method = "render", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawStringWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
    void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        float f = this.doBackgroundFade ? (float) (Util.getMeasuringTimeMs() - this.backgroundFadeStart) / 1000.0F : 1.0F;
        float g = this.doBackgroundFade ? MathHelper.clamp(f - 1.0F, 0.0F, 1.0F) : 1.0F;
        int l = MathHelper.ceil(g * 255.0F) << 24;
        int y = this.height / 4 + 48;
        int x = this.width / 2 + 104;

        OpenXR OPEN_XR = MCXRPlayClient.OPEN_XR;

        if (!FabricLoader.getInstance().isModLoaded("sodium")) {
            DrawableHelper.drawStringWithShadow(matrices, textRenderer, "Sodium Missing!", x + 1, y + 12, 16733525 | l);
            y += 12;
        }

        if (OPEN_XR.session != null) {
            OpenXRSystem system = OPEN_XR.session.system;
            DrawableHelper.drawStringWithShadow(matrices, textRenderer, I18n.translate("mcxr.menu.session_created"), x + 1, y + 12, 16777215 | l);
            y += 12;
            for (String line : wordWrap(system.systemName, 20)) {
                DrawableHelper.drawStringWithShadow(matrices, textRenderer, line, x + 1, y + 12, 16777215 | l);
                y += 12;
            }
            DrawableHelper.drawStringWithShadow(matrices, textRenderer, I18n.translate("openxr.form_factor." + system.formFactor), x + 1, y + 12, 16777215 | l);
        } else {
            DrawableHelper.drawStringWithShadow(matrices, textRenderer, I18n.translate("mcxr.menu.session_not_created"), x + 1, y + 12, 16777215 | l);
            y += 12;
            if (OPEN_XR.createException != null) {
                String message = OPEN_XR.createException.getMessage();
                if (OPEN_XR.createException.result == XR10.XR_ERROR_FORM_FACTOR_UNAVAILABLE) {
                    message = I18n.translate("mcxr.error.form_factor_unavailable");
                }
                for (String line : wordWrap(message, 20)) {
                    DrawableHelper.drawStringWithShadow(matrices, MinecraftClient.getInstance().textRenderer, line, x + 1, y + 12, 16733525 | l);
                    y += 12;
                }
                if (mouseX > x && mouseY < y + 10 && mouseY > this.height / 4 + 48 + 12 + 10) {
                    this.renderTooltip(matrices, wordWrapText(message, 40), mouseX + 14, mouseY);
                }
            }
        }
    }

    @Unique
    public List<String> wordWrap(String string, int wrapLength) {
        return WordUtils.wrap(string, wrapLength).lines().toList();
    }

    @Unique
    public List<Text> wordWrapText(String string, int wrapLength) {
        return WordUtils.wrap(string, wrapLength).lines().map(s -> (Text) (new LiteralText(s))).toList();
    }
}
