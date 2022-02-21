package net.sorenon.mcxr.play.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.play.openxr.OpenXRInstance;
import net.sorenon.mcxr.play.openxr.OpenXRState;
import net.sorenon.mcxr.play.openxr.OpenXRSystem;
import org.apache.commons.lang3.text.WordUtils;
import org.lwjgl.openxr.XR10;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PauseScreen.class)
public class PauseScreenMixin extends Screen {

    protected PauseScreenMixin(Component component) {
        super(component);
    }

    @Inject(method = "init", at = @At("HEAD"))
    void init(CallbackInfo ci) {
        int y = this.height / 4 + 48 + -16;
        this.addRenderableWidget(new Button(
                this.width / 2 + 104,
                y,
                90,
                20,
                new TranslatableComponent("mcxr.menu.reload"),
                button -> MCXRPlayClient.OPEN_XR_STATE.tryInitialize()));
    }

    @Inject(method = "render", at = @At("RETURN"))
    void render(PoseStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        int y = this.height / 4 + 48 + -16 + 12;
        int x = this.width / 2 + 104;

        OpenXRState OPEN_XR = MCXRPlayClient.OPEN_XR_STATE;

        if (!FabricLoader.getInstance().isModLoaded("sodium")) {
            GuiComponent.drawString(matrices, font, "Sodium Missing!", x + 1, y + 12, 16733525);
            y += 12;
        }

        if (OPEN_XR.instance != null) {
            OpenXRInstance instance = OPEN_XR.instance;
            GuiComponent.drawString(matrices, font, instance.runtimeName + " " + instance.runtimeVersionString, x + 1, y + 12, 16777215);
            y += 12;
        }

        if (OPEN_XR.session != null) {
            OpenXRSystem system = OPEN_XR.session.system;
            for (String line : wordWrap(system.systemName, 20)) {
                GuiComponent.drawString(matrices, font, line, x + 1, y + 12, 16777215);
                y += 12;
            }
            GuiComponent.drawString(matrices, font, I18n.get("openxr.form_factor." + system.formFactor), x + 1, y + 12, 16777215);
        } else {
            GuiComponent.drawString(matrices, font, I18n.get("mcxr.menu.session_not_created"), x + 1, y + 12, 16777215);
            y += 12;
            if (OPEN_XR.createException != null) {
                String message = OPEN_XR.createException.getMessage();
                if (OPEN_XR.createException.result == XR10.XR_ERROR_FORM_FACTOR_UNAVAILABLE) {
                    message = I18n.get("mcxr.error.form_factor_unavailable");
                }
                for (String line : wordWrap(message, 20)) {
                    GuiComponent.drawString(matrices, Minecraft.getInstance().font, line, x + 1, y + 12, 16733525);
                    y += 12;
                }
                if (mouseX > x && mouseY < y + 10 && mouseY > this.height / 4 + 48 + 12 + 10) {
                    this.renderComponentTooltip(matrices, wordWrapText(message, 40), mouseX + 14, mouseY);
                }
            }
        }
    }

    @Unique
    public List<String> wordWrap(String string, int wrapLength) {
        return WordUtils.wrap(string, wrapLength, null, true).lines().toList();
    }

    @Unique
    public List<Component> wordWrapText(String string, int wrapLength) {
        return WordUtils.wrap(string, wrapLength, null, true).lines().map(s -> (Component) (new TextComponent(s))).toList();
    }
}
