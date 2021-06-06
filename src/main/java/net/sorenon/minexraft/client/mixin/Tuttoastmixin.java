package net.sorenon.minexraft.client.mixin;

import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.toast.TutorialToast;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(TutorialToast.class)
public class Tuttoastmixin {

    /**
     * @author Sorenon
     */
    @Overwrite
    public Toast.Visibility draw(MatrixStack matrices, ToastManager manager, long startTime) {
        return Toast.Visibility.HIDE;
    }
}
