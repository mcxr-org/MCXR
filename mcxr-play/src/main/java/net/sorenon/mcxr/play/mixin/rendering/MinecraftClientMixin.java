package net.sorenon.mcxr.play.mixin.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.WindowFramebuffer;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Option;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.MetricsData;
import net.minecraft.util.Util;
import net.minecraft.util.profiler.ProfileResult;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.snooper.Snooper;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.play.accessor.MinecraftClientExt;
import net.sorenon.mcxr.play.mixin.accessor.WindowAcc;
import net.sorenon.mcxr.play.openxr.OpenXR;
import net.sorenon.mcxr.play.openxr.XrRenderer;
import net.sorenon.mcxr.play.rendering.MainRenderTarget;
import net.sorenon.mcxr.play.rendering.RenderPass;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Queue;
import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin extends ReentrantThreadExecutor<Runnable> implements MinecraftClientExt {

    @Shadow
    private volatile boolean running;

    public MinecraftClientMixin(String string) {
        super(string);
    }

    @Shadow
    private Profiler profiler;

    @Shadow
    @Final
    private Window window;

    @Shadow
    public abstract void scheduleStop();

    @Shadow
    @Nullable
    private CompletableFuture<Void> resourceReloadFuture;

    @Shadow
    @Nullable
    public Overlay overlay;

    @Shadow
    public abstract CompletableFuture<Void> reloadResources();

    @Shadow
    @Final
    private Queue<Runnable> renderTaskQueue;

    @Shadow
    @Final
    private RenderTickCounter renderTickCounter;

    @Shadow
    public abstract void tick();

    @Shadow
    @Final
    public Mouse mouse;

    @Shadow
    @Final
    private SoundManager soundManager;

    @Shadow
    @Final
    public GameRenderer gameRenderer;

    @Mutable
    @Shadow
    @Final
    private Framebuffer framebuffer;

    @Shadow
    public boolean skipGameRender;

    @Shadow
    private boolean paused;

    @Shadow
    private float pausedTickDelta;

    @Shadow
    @Final
    private ToastManager toastManager;

    @Shadow
    @Nullable
    private ProfileResult tickProfilerResult;

    @Shadow
    protected abstract void drawProfilerResults(MatrixStack matrices, ProfileResult profileResult);

    @Shadow
    private int fpsCounter;

    @Shadow
    public abstract boolean isIntegratedServerRunning();

    @Shadow
    @Nullable
    public Screen currentScreen;

    @Shadow
    @Final
    public MetricsData metricsData;

    @Shadow
    private long lastMetricsSampleTime;

    @Shadow
    private long nextDebugInfoUpdateTime;

    @Shadow
    private static int currentFps;

    @Shadow
    public String fpsDebugString;

    @Shadow
    @Final
    private Snooper snooper;

    @Shadow
    @Nullable
    private IntegratedServer server;

    @Shadow
    @Final
    public static boolean IS_SYSTEM_MAC;

    @Shadow
    protected abstract void render(boolean tick);

    @Shadow
    @Nullable
    public ClientWorld world;

    @Shadow
    @Final
    public GameOptions options;

    @Unique
    private static final XrRenderer XR_RENDERER = MCXRPlayClient.RENDERER;

    @Redirect(method = "<init>", at = @At(value = "NEW", target = "net/minecraft/client/gl/WindowFramebuffer"))
    WindowFramebuffer createFramebuffer(int width, int height) {
        return new MainRenderTarget(width, height);
    }

    @Inject(method = "run", at = @At("HEAD"))
    void start(CallbackInfo ci) {
        MCXRPlayClient.INSTANCE.flatGuiManager.init();
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;render(Z)V"), method = "run")
    void loop(MinecraftClient minecraftClient, boolean tick) {
        OpenXR openXR = MCXRPlayClient.OPEN_XR;
        if (openXR.loop()) {
            //Just render normally
            render(tick);
        }
    }

    /**
     * @see MinecraftClient#render
     * To help performance and make debugging easier, render has been split into these three functions
     * This could have some compatibility issues but I have only found one mixin (computercraft) which this may affect
     * ASMR's more advanced transformers could help with this in the future
     */
    @Override
    public void preRender(boolean tick) {
        this.window.setPhase("Pre render");
        if (this.window.shouldClose()) {
            this.scheduleStop();
        }

        if (this.resourceReloadFuture != null && !(this.overlay instanceof SplashOverlay)) {
            CompletableFuture<Void> completableFuture = this.resourceReloadFuture;
            this.resourceReloadFuture = null;
            this.reloadResources().thenRun(() -> {
                completableFuture.complete(null);
            });
        }

        Runnable runnable;
        while ((runnable = this.renderTaskQueue.poll()) != null) {
            runnable.run();
        }

        if (tick) {
            int i = this.renderTickCounter.beginRenderTick(Util.getMeasuringTimeMs());
            this.profiler.push("scheduledExecutables");
            this.runTasks();
            this.profiler.pop();
            this.profiler.push("tick");

            for (int j = 0; j < Math.min(10, i); ++j) {
                this.profiler.visit("clientTick");
                this.tick();
            }

            this.profiler.pop();
        }
    }

    @Override
    public void doRender(boolean tick, long frameStartTime, RenderPass renderPass) {
        XR_RENDERER.renderPass = renderPass;
        this.profiler.push("render");
        MatrixStack matrixStack = RenderSystem.getModelViewStack();
        matrixStack.push();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.clear(16640, IS_SYSTEM_MAC);
        this.framebuffer.beginWrite(true);
        BackgroundRenderer.method_23792();
        this.profiler.push("display");
        RenderSystem.enableTexture();
        RenderSystem.enableCull();
        this.profiler.pop();
        if (!this.skipGameRender) {
            this.profiler.swap("gameRenderer");
            this.gameRenderer.render(this.paused ? this.pausedTickDelta : this.renderTickCounter.tickDelta, frameStartTime, tick);

            if (XR_RENDERER.renderPass == RenderPass.GUI || XR_RENDERER.renderPass == RenderPass.VANILLA) {
                this.profiler.swap("toasts");
                this.toastManager.draw(new MatrixStack());
                this.profiler.pop();
            }
        }

        if (this.tickProfilerResult != null) {
            this.profiler.push("fpsPie");
            this.drawProfilerResults(new MatrixStack(), this.tickProfilerResult);
            this.profiler.pop();
        }

        this.profiler.push("blit");
        this.framebuffer.endWrite();
        matrixStack.pop();

        XR_RENDERER.renderPass = RenderPass.VANILLA;
    }

    @Override
    public void postRender() {
        GLFW.glfwPollEvents();
        RenderSystem.replayQueue();
        Tessellator.getInstance().getBuffer().clear();
//        GLFW.glfwSwapBuffers(window.getHandle());
        WindowAcc windowAcc = ((WindowAcc) (Object) window);
        if (window.isFullscreen() != windowAcc.getCurrentFullscreen()) {
            windowAcc.setCurrentFullscreen(window.isFullscreen());
            windowAcc.invokeUpdateFullscreen(windowAcc.getVsync());
        }
        GLFW.glfwPollEvents();

        this.window.setPhase("Post render");
        ++this.fpsCounter;
        boolean bl = this.isIntegratedServerRunning() && (this.currentScreen != null && this.currentScreen.isPauseScreen() || this.overlay != null && this.overlay.pausesGame()) && !this.server.isRemote();
        if (this.paused != bl) {
            if (this.paused) {
                this.pausedTickDelta = this.renderTickCounter.tickDelta;
            } else {
                this.renderTickCounter.tickDelta = this.pausedTickDelta;
            }

            this.paused = bl;
        }

        long m = Util.getMeasuringTimeNano();
        this.metricsData.pushSample(m - this.lastMetricsSampleTime);
        this.lastMetricsSampleTime = m;
        this.profiler.push("fpsUpdate");

        while (Util.getMeasuringTimeMs() >= this.nextDebugInfoUpdateTime + 1000L) {
            currentFps = this.fpsCounter;
            this.fpsDebugString = String.format("%d fps T: %s%s%s%s B: %d", currentFps, (double) this.options.maxFps == Option.FRAMERATE_LIMIT.getMax() ? "inf" : this.options.maxFps, this.options.enableVsync ? " vsync" : "", this.options.graphicsMode.toString(), this.options.cloudRenderMode == CloudRenderMode.OFF ? "" : (this.options.cloudRenderMode == CloudRenderMode.FAST ? " fast-clouds" : " fancy-clouds"), this.options.biomeBlendRadius);
            this.nextDebugInfoUpdateTime += 1000L;
            this.fpsCounter = 0;
            this.snooper.update();
            if (!this.snooper.isActive()) {
                this.snooper.method_5482();
            }
        }

        this.profiler.pop();
    }
}
