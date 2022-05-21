package net.sorenon.mcxr.play.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.EditServerScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.sorenon.mcxr.play.PlayOptions;
import net.sorenon.mcxr.play.gui.AddServerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(JoinMultiplayerScreen.class)
public abstract class MultiPlayerScreenMixin extends Screen {


    @Shadow protected abstract void addServerCallback(boolean confirmedAction);

    @Shadow private ServerData editingServer;

    protected MultiPlayerScreenMixin(Component component) {
        super(component);
    }

    @Redirect(method = "init", at=@At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/multiplayer/JoinMultiplayerScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;", ordinal = 2))
    public GuiEventListener addrenderableMixin(JoinMultiplayerScreen instance, GuiEventListener guiEventListener) {
        if (!PlayOptions.xrUninitialized) {
            if (FabricLoader.getInstance().isModLoaded("titleworlds")) {
                return addRenderableWidget(new Button(this.width / 2 + 54, this.height - 52, 100, 20, Component.translatable("selectServer.add"), (button) -> {
                    this.minecraft.setScreen(new AddServerScreen(Component.translatable("Add server"), instance));
                }));
            }
        }

        return addRenderableWidget(new Button(this.width / 2 + 54, this.height - 52, 100, 20, Component.translatable("selectServer.add"), (button) -> {
            this.editingServer = new ServerData(I18n.get("selectServer.defaultName", new Object[0]), "", false);
            this.minecraft.setScreen(new EditServerScreen(this, this::addServerCallback, this.editingServer));
        }));

    }
}
