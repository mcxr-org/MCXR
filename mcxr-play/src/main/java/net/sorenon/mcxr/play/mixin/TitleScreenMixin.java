package net.sorenon.mcxr.play.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.Util;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.sorenon.mcxr.play.MCXRGuiManager;
import net.sorenon.mcxr.play.MCXROptionsScreen;
import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.play.openxr.MCXRGameRenderer;
import net.sorenon.mcxr.play.openxr.OpenXRState;
import net.sorenon.mcxr.play.openxr.OpenXRSystem;
import net.sorenon.mcxr.play.rendering.MCXRCamera;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

    @Shadow
    protected abstract void init();

    @Shadow
    @Final
    private boolean fading;
    @Shadow
    private long fadeInStart;
    @Unique
    private static boolean initialized = false;

    protected TitleScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("HEAD"))
    void init(CallbackInfo ci) {
        this.addRenderableWidget(
                new Button(this.width/2 + 127, this.height / 4 + 48 + 72 + 12, 45, 20, new TranslatableComponent("Reset"), (button -> {
                    assert this.minecraft != null;
                    // First we fetch the name of the system from OpenXR
                    OpenXRState OPEN_XR = MCXRPlayClient.OPEN_XR_STATE;
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

        int y = this.height / 4 + 48;
        this.addRenderableWidget(new Button(
                this.width / 2 + 104,
                y,
                90,
                20,
                new TranslatableComponent("mcxr.options.title"),
                button -> this.minecraft.setScreen(new MCXROptionsScreen(this))));
    }

    @Inject(method = "render", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/gui/screens/TitleScreen;drawString(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)V"))
    void render(PoseStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!initialized) {
            if (MCXRPlayClient.OPEN_XR_STATE.session == null) {
                MCXRPlayClient.OPEN_XR_STATE.tryInitialize();
            }
            initialized = true;
        }
        float f = this.fading ? (float) (Util.getMillis() - this.fadeInStart) / 1000.0F : 1.0F;
        float g = this.fading ? Mth.clamp(f - 1.0F, 0.0F, 1.0F) : 1.0F;
        int l = Mth.ceil(g * 255.0F) << 24;
        int y = this.height / 4 + 48;
        int x = this.width / 2 + 104;

        if (!FabricLoader.getInstance().isModLoaded("modmenu")) {
            y += 12;
        }

        if (!FabricLoader.getInstance().isModLoaded("sodium")) {
            GuiComponent.drawString(matrices, font, "Sodium Missing!", x + 1, y + 12, 16733525 | l);
            y += 12;
        }

        MCXROptionsScreen.renderStatus(this, this.font, matrices, mouseX, mouseY, x, y, l, 20);
    }
}
