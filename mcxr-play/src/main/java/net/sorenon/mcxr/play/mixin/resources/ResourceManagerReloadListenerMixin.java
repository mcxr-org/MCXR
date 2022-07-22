package net.sorenon.mcxr.play.mixin.resources;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.sorenon.mcxr.play.MCXRPlayClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResourceManagerReloadListener.class)
public interface ResourceManagerReloadListenerMixin {

    @Inject(method = "method_29490", at = @At("HEAD"))
    default void preReload(ProfilerFiller profilerFiller, ResourceManager resourceManager, CallbackInfo ci){
        MCXRPlayClient.MCXR_GAME_RENDERER.reloadingDepth += 1;
    }

    @Inject(method = "method_29490", at = @At("RETURN"))
    default void postReload(ProfilerFiller profilerFiller, ResourceManager resourceManager, CallbackInfo ci){
        MCXRPlayClient.MCXR_GAME_RENDERER.reloadingDepth -= 1;
    }
}
