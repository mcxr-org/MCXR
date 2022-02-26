package net.sorenon.mcxr.play.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.Util;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.play.openxr.OpenXR;
import net.sorenon.mcxr.play.openxr.OpenXRInstance;
import net.sorenon.mcxr.play.openxr.OpenXRSystem;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.openxr.XR10;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;

// Izzy was here :)
@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

    @Shadow
    protected abstract void init();

    @Shadow @Final private boolean fading;
    @Shadow private long fadeInStart;
    @Unique
    private static boolean initialized = false;

    private static final Logger LOGGER = LogManager.getLogger();

    protected TitleScreenMixin(Component title) {
        super(title);
    }

    @Inject(at=@At("RETURN"), method = "createNormalMenuOptions")
    private void addResetButton(int i, int j, CallbackInfo ci) {

        // Creates our reset button
        this.addRenderableWidget(
                new Button(this.width/2 + 127, this.height / 4 + 48 + 73 + 12, 45, 20, new TranslatableComponent("Reset"), (button -> {
                    assert this.minecraft != null;
                    // First we fetch the name of the system from OpenXR
                    OpenXR OPEN_XR = MCXRPlayClient.OPEN_XR;
                    OpenXRSystem system = OPEN_XR.session.system;
                    String sys = system.systemName;

                    // Since we can assume users are on a quest 1 or 2, we will set our video settings based on those two options.
                    if (sys.equalsIgnoreCase("oculus quest2")) {

                        // quest 2 gets 6 render distance 8 sim distance.
                        this.minecraft.options.renderDistance = 6;
                        this.minecraft.options.simulationDistance = 8;

                    } else if (sys.equalsIgnoreCase("oculus quest")) {

                        // quest 1 gets 2 render distance and 4 sim distance
                        this.minecraft.options.renderDistance = 2;
                        this.minecraft.options.simulationDistance = 4;

                    }

                    // Common options for both platforms.
                    this.minecraft.options.graphicsMode = GraphicsStatus.FABULOUS;
                    this.minecraft.options.framerateLimit = 72;
                    this.minecraft.options.renderClouds = CloudStatus.OFF;

                }))
        );
    }


    @Redirect(method="createNormalMenuOptions", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/TitleScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;", ordinal = 2))
    public GuiEventListener nerfRealmsButton(TitleScreen instance, GuiEventListener guiEventListener) {
        // Creates a new empty button and hites it.
        Button wig = new Button(this.width / 2 - 100, this.height / 4 + 48 + 24 * 2, 200, 20, new TranslatableComponent("menu.online"), (button) -> {});
        wig.visible = false;

        // Returns our new button as the renderable widget. We do this because the ModMenu mod relies on the realms button being present even if its hidden.
        return this.addRenderableWidget(wig);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/gui/screens/TitleScreen;drawString(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)V"))
    void render(PoseStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!initialized) {
            if (MCXRPlayClient.OPEN_XR.session == null) {
                MCXRPlayClient.OPEN_XR.tryInitialize();
            }
            initialized = true;
        }
        float f = this.fading ? (float) (Util.getMillis() - this.fadeInStart) / 1000.0F : 1.0F;
        float g = this.fading ? Mth.clamp(f - 1.0F, 0.0F, 1.0F) : 1.0F;
        int l = Mth.ceil(g * 255.0F) << 24;
        int y = this.height / 4 + 48;
        int x = this.width / 2 + 104;

        OpenXR OPEN_XR = MCXRPlayClient.OPEN_XR;

        if (!FabricLoader.getInstance().isModLoaded("modmenu")) {
            y += 12;
        }

        if (!FabricLoader.getInstance().isModLoaded("sodium")) {
            GuiComponent.drawString(matrices, font, "Sodium Missing!", x + 1, y + 12, 16733525 | l);
            y += 12;
        }

        if (OPEN_XR.instance != null) {
            OpenXRInstance instance = OPEN_XR.instance;
            GuiComponent.drawString(matrices, font, instance.runtimeName + " " + instance.runtimeVersionString, x + 1, y + 12, 16777215 | l);
            y += 12;
        }

        if (OPEN_XR.session != null) {
            OpenXRSystem system = OPEN_XR.session.system;
            GuiComponent.drawString(matrices, font, I18n.get("mcxr.menu.session_created"), x + 1, y + 12, 16777215 | l);
            y += 12;
            for (String line : wordWrap(system.systemName, 20)) {
                GuiComponent.drawString(matrices, font, line, x + 1, y + 12, 16777215 | l);
                y += 12;
            }
            GuiComponent.drawString(matrices, font, I18n.get("openxr.form_factor." + system.formFactor), x + 1, y + 12, 16777215 | l);
        } else {
            GuiComponent.drawString(matrices, font, I18n.get("mcxr.menu.session_not_created"), x + 1, y + 12, 16777215 | l);
            y += 12;
            if (OPEN_XR.createException != null) {
                String message = OPEN_XR.createException.getMessage();
                if (OPEN_XR.createException.result == XR10.XR_ERROR_FORM_FACTOR_UNAVAILABLE) {
                    message = I18n.get("mcxr.error.form_factor_unavailable");
                }
                for (String line : wordWrap(message, 20)) {
                    GuiComponent.drawString(matrices, Minecraft.getInstance().font, line, x + 1, y + 12, 16733525 | l);
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
