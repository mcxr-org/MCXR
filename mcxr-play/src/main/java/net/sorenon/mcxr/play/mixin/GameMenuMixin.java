package net.sorenon.mcxr.play.mixin;

import net.minecraft.client.CloudStatus;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.play.openxr.OpenXRState;
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
    private void addResetButton(CallbackInfo ci) {
        this.addRenderableWidget(
                new Button(this.width/2 + 127, this.height / 4 + 48 + 73 + 12, 45, 20, Component.translatable("Reset"), (button -> {
                    assert this.minecraft != null;
                    // First we fetch the name of the system from OpenXR
                    OpenXRState OPEN_XR = MCXRPlayClient.OPEN_XR_STATE;
                    OpenXRSystem system = OPEN_XR.session.system;
                    String sys = system.systemName;

                    // Since we can assume users are on a quest 1 or 2, we will set our video settings based on those two options.
                    if (sys.equalsIgnoreCase("oculus quest2")) {

                        // quest 2 gets 6 render distance 8 sim distance.
                        this.minecraft.options.renderDistance().set(8);
                        this.minecraft.options.simulationDistance().set(8);

                    } else if (sys.equalsIgnoreCase("oculus quest")) {

                        // quest 1 gets 2 render distance and 4 sim distance
                        this.minecraft.options.renderDistance().set(4);
                        this.minecraft.options.simulationDistance().set(4);

                    }

                    // Common options for both platforms.
                    this.minecraft.options.graphicsMode().set(GraphicsStatus.FAST);
                    this.minecraft.options.framerateLimit().set(72);
                    this.minecraft.options.cloudStatus().set(CloudStatus.FAST);

                }))
        );
    }

}
