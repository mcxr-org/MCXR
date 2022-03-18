package net.sorenon.mcxr.play.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.sorenon.mcxr.play.MCXROptionsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
                new TranslatableComponent("mcxr.options.title"),
                button -> this.minecraft.setScreen(new MCXROptionsScreen(this))));
    }

    @Inject(method = "render", at = @At("RETURN"))
    void render(PoseStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        int y = this.height / 4 + 48 + -16 + 12;
        int x = this.width / 2 + 104;

        MCXROptionsScreen.renderStatus(this, this.font, matrices, mouseX, mouseY, x, y, 0, 20);
    }
}
