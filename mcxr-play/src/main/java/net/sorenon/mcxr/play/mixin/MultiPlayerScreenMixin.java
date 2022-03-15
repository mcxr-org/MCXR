package net.sorenon.mcxr.play.mixin;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.sorenon.mcxr.play.gui.AddServerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(JoinMultiplayerScreen.class)
public abstract class MultiPlayerScreenMixin extends Screen {


    protected MultiPlayerScreenMixin(Component component) {
        super(component);
    }

    @Redirect(method = "init", at=@At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/multiplayer/JoinMultiplayerScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;", ordinal = 2))
    public GuiEventListener addrenderableMixin(JoinMultiplayerScreen instance, GuiEventListener guiEventListener) {
        return addRenderableWidget(new Button(this.width/2 + 54, this.height - 52, 100, 20, new TranslatableComponent("selectServer.add"), (button) -> {
            this.minecraft.setScreen(new AddServerScreen(new TranslatableComponent("Add server"), instance));
        }));
    }
}
