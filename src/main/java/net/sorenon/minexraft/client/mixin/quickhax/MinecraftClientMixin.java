package net.sorenon.minexraft.client.mixin.quickhax;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    /**
     * @author Sorenon
     * nb fabulous graphics seems to have similar issues to canvas
     */
    @Overwrite
    public static boolean isFabulousGraphicsOrBetter() {
        return false;
    }
}
