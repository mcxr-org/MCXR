package net.sorenon.mcxr.play.mixin;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceManager;
import net.sorenon.mcxr.play.MCXRPlayClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Consumer;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow @Final private Map<String, ShaderInstance> shaders;

    @Inject(method = "reloadShaders", at = @At(value = "TAIL"))
    void loadGuiShader(ResourceManager manager, CallbackInfo ci) {
        ArrayList<Pair<ShaderInstance, Consumer<ShaderInstance>>> loadingShaders = new ArrayList<>();
        try {
            loadingShaders.add(Pair.of(new ShaderInstance(manager, "blit_screen_mcxr", DefaultVertexFormat.BLIT_SCREEN), (shader) -> MCXRPlayClient.MCXR_GAME_RENDERER.blitShader = shader));
            loadingShaders.add(Pair.of(new ShaderInstance(manager, "gui_blit_screen_mcxr", DefaultVertexFormat.BLIT_SCREEN), (shader) -> MCXRPlayClient.MCXR_GAME_RENDERER.guiBlitShader = shader));
        } catch (IOException e) {
            throw new RuntimeException("[MCXR] Could not load custom shaders", e);
        }
        loadingShaders.forEach((pair) -> {
            ShaderInstance shader = pair.getFirst();
            this.shaders.put(shader.getName(), shader);
            pair.getSecond().accept(shader);
        });
    }
}
