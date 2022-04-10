package net.sorenon.mcxr.play.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.network.chat.TranslatableComponent;
import net.sorenon.mcxr.play.gui.Vr2DKeyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EditBox.class)
public abstract class EditBoxMixin {

    @Redirect(method = "mouseClicked", at=@At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/EditBox;moveCursorTo(I)V"))
    public void mouseClickedMixin(EditBox instance, int cursor) {
        assert Minecraft.getInstance().screen != null;

        if (Minecraft.getInstance().screen.getClass() == ChatScreen.class) {
            instance.setCursorPosition(cursor);
            return;
        }

        instance.setCursorPosition(cursor);
        Minecraft.getInstance().setScreen(new Vr2DKeyboard(new TranslatableComponent("Keyboard"),Minecraft.getInstance().screen, instance));

    }
}
