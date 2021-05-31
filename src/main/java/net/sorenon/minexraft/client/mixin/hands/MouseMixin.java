package net.sorenon.minexraft.client.mixin.hands;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.util.Window;
import net.sorenon.minexraft.client.accessor.MouseExt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Mouse.class)
public abstract class MouseMixin implements MouseExt {

    @Shadow
    protected abstract void onCursorPos(long window, double x, double y);

    @Shadow
    protected abstract void onMouseButton(long window, int button, int action, int mods);

    @Redirect(method = "onCursorPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;getHandle()J"))
    long cancelCursorPos(Window window) {
        if (MinecraftClient.getInstance().world != null) {
            return -1;
        }
        return window.getHandle();
    }

    @Redirect(method = "onMouseButton", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;getHandle()J"))
    long cancelMouseButton(Window window) {
        if (MinecraftClient.getInstance().world != null) {
            return -1;
        }
        return window.getHandle();
    }

    @Override
    public void cursorPos(int x, int y) {
        this.onCursorPos(-1, x, y);
    }

    @Override
    public void mouseButton(int button, int action, int mods) {
        this.onMouseButton(-1, button, action, mods);
    }
}
