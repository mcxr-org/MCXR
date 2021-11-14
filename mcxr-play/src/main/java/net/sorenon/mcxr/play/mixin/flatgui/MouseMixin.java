package net.sorenon.mcxr.play.mixin.flatgui;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.sorenon.mcxr.play.accessor.MouseExt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class MouseMixin implements MouseExt {

    @Shadow
    protected abstract void onMove(long window, double x, double y);

    @Shadow
    protected abstract void onPress(long window, int button, int action, int mods);

    @Shadow
    protected abstract void onScroll(long window, double horizontal, double vertical);

    @Redirect(method = "onMove", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/Window;getWindow()J"))
    long cancelCursorPos(Window window) {
        if (Minecraft.getInstance().level != null) {
            return -1;
        }
        return window.getWindow();
    }

    @Redirect(method = "onPress", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/Window;getWindow()J"))
    long cancelMouseButton(Window window) {
        if (Minecraft.getInstance().level != null) {
            return -1;
        }
        return window.getWindow();
    }

    @Redirect(method = "onScroll", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/Window;getWindow()J"))
    long cancelMouseScroll(Window window) {
        if (Minecraft.getInstance().level != null) {
            return -1;
        }
        return window.getWindow();
    }

    @Override
    public void cursorPos(double x, double y) {
        this.onMove(-1, x, y);
    }

    @Override
    public void mouseButton(int button, int action, int mods) {
        this.onPress(-1, button, action, mods);
    }

    @Override
    public void mouseScroll(double horizontalDistance, double verticalDistance) {
        this.onScroll(-1, horizontalDistance, verticalDistance);
    }
}
