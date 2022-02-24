package net.sorenon.mcxr.play.mixin;

import net.minecraft.client.CloudStatus;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.play.openxr.OpenXR;
import net.sorenon.mcxr.play.openxr.OpenXRSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PauseScreen.class)
public class GameMenuMixin extends Screen {

    private static final Logger LOGGER = LogManager.getLogger();

    protected GameMenuMixin(Component component) {
        super(component);
    }

    @Inject(at=@At("RETURN"), method = "createPauseMenu")
    private void addCustomButton(CallbackInfo ci) {
        this.addRenderableWidget(
                new Button(this.width/2 + 127, this.height / 4 + 48 + 73 + 12, 45, 20, new TranslatableComponent("Reset"), (button -> {
                    assert this.minecraft != null;

                    OpenXR OPEN_XR = MCXRPlayClient.OPEN_XR;
                    OpenXRSystem system = OPEN_XR.session.system;

                    String sys = system.systemName;

                    if (sys.equalsIgnoreCase("oculus quest2")) {
                        this.minecraft.options.renderDistance = 6;
                        this.minecraft.options.simulationDistance = 8;
                        this.minecraft.options.graphicsMode = GraphicsStatus.FABULOUS;
                        this.minecraft.options.framerateLimit = 72;
                        this.minecraft.options.renderClouds = CloudStatus.OFF;
                    } else if (sys.equalsIgnoreCase("oculus quest")) {
                        this.minecraft.options.renderDistance = 2;
                        this.minecraft.options.simulationDistance = 4;
                        this.minecraft.options.graphicsMode = GraphicsStatus.FABULOUS;
                        this.minecraft.options.framerateLimit = 72;
                        this.minecraft.options.renderClouds = CloudStatus.OFF;
                    } else {
                        LOGGER.error(sys);
                    }

                }))
        );
    }

}
