package net.sorenon.mcxr.play.mixin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Shader;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.resource.ResourceManager;
import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.play.rendering.VrFirstPersonRenderer;
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

    @Shadow @Final private Map<String, Shader> shaders;

    @Inject(method = "loadShaders", at = @At(value = "TAIL"))
    void loadGuiShader(ResourceManager manager, CallbackInfo ci) {
        ArrayList<Pair<Shader, Consumer<Shader>>> loadingShaders = new ArrayList<>();
        try {
            loadingShaders.add(Pair.of(new Shader(manager, "blit_screen_mcxr", VertexFormats.BLIT_SCREEN), (shader) -> MCXRPlayClient.RENDERER.blitShader = shader));
            loadingShaders.add(Pair.of(new Shader(manager, "gui_blit_screen_mcxr", VertexFormats.BLIT_SCREEN), (shader) -> MCXRPlayClient.RENDERER.guiBlitShader = shader));
        } catch (IOException e) {
            throw new RuntimeException("[MCXR] Could not load custom shaders", e);
        }
        loadingShaders.forEach((pair) -> {
            Shader shader = pair.getFirst();
            this.shaders.put(shader.getName(), shader);
            pair.getSecond().accept(shader);
        });
    }
}
