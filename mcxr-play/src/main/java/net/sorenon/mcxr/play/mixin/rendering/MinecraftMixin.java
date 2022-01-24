package net.sorenon.mcxr.play.mixin.rendering;

import com.mojang.blaze3d.pipeline.MainTarget;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.Util;
import net.minecraft.client.*;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.util.FrameTimer;
import net.minecraft.util.profiling.ProfileResults;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.thread.ReentrantBlockableEventLoop;
import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.play.accessor.MinecraftClientExt;
import net.sorenon.mcxr.play.mixin.accessor.WindowAcc;
import net.sorenon.mcxr.play.openxr.OpenXR;
import net.sorenon.mcxr.play.openxr.XrRenderer;
import net.sorenon.mcxr.play.rendering.MCXRMainTarget;
import net.sorenon.mcxr.play.rendering.RenderPass;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Queue;
import java.util.concurrent.CompletableFuture;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin extends ReentrantBlockableEventLoop<Runnable> implements MinecraftClientExt {

    public MinecraftMixin(String string) {
        super(string);
    }

    @Shadow
    private ProfilerFiller profiler;

    @Shadow
    @Final
    private Window window;

    @Shadow
    public abstract void stop();

    @Shadow
    @Nullable
    private CompletableFuture<Void> pendingReload;

    @Shadow
    @Nullable
    private Overlay overlay;

    @Shadow
    public abstract CompletableFuture<Void> reloadResourcePacks();

    @Shadow
    @Final
    private Queue<Runnable> progressTasks;

    @Shadow
    @Final
    private Timer timer;

    @Shadow
    public abstract void tick();

    @Shadow
    @Final
    public GameRenderer gameRenderer;

    @Mutable
    @Shadow
    @Final
    private RenderTarget mainRenderTarget;

    @Shadow
    public boolean noRender;

    @Shadow
    private boolean pause;

    @Shadow
    private float pausePartialTick;

    @Shadow
    @Final
    private ToastComponent toast;

    @Shadow
    @Nullable
    private ProfileResults fpsPieResults;

    @Shadow
    protected abstract void renderFpsMeter(PoseStack matrices, ProfileResults profileResult);

    @Shadow
    private int frames;

    @Shadow
    public abstract boolean hasSingleplayerServer();

    @Shadow
    @Nullable
    public Screen screen;

    @Shadow
    @Final
    public FrameTimer frameTimer;

    @Shadow
    private long lastNanoTime;

    @Shadow
    private long lastTime;

    @Shadow
    private static int fps;

    @Shadow
    public String fpsString;

    @Shadow
    @Nullable
    private IntegratedServer singleplayerServer;

    @Shadow
    @Final
    public static boolean ON_OSX;

    @Shadow
    protected abstract void runTick(boolean tick);

    @Shadow
    @Nullable
    public ClientLevel level;

    @Shadow
    @Final
    public Options options;

    @Unique
    private static final XrRenderer XR_RENDERER = MCXRPlayClient.RENDERER;

    @Redirect(method = "<init>", at = @At(value = "NEW", target = "com/mojang/blaze3d/pipeline/MainTarget"))
    MainTarget createFramebuffer(int width, int height) {
        return new MCXRMainTarget(width, height);
    }

    @Inject(method = "run", at = @At("HEAD"))
    void start(CallbackInfo ci) {
        MCXRPlayClient.INSTANCE.flatGuiManager.init();
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;runTick(Z)V"), method = "run")
    void loop(Minecraft minecraftClient, boolean tick) {
        OpenXR openXR = MCXRPlayClient.OPEN_XR;
        if (openXR.loop()) {
            //Just render normally
            runTick(tick);
        }
    }

    /**
     * @see Minecraft#runTick
     * To help performance and make debugging easier, render has been split into these three functions
     * This could have some compatibility issues, but I have only found one mixin (computercraft) which this may affect
     * ASMR's more advanced transformers could help with this in the future
     */
    @Override
    public void preRender(boolean tick) {
        this.window.setErrorSection("Pre render");
        if (this.window.shouldClose()) {
            this.stop();
        }

        if (this.pendingReload != null && !(this.overlay instanceof LoadingOverlay)) {
            CompletableFuture<Void> completableFuture = this.pendingReload;
            this.pendingReload = null;
            this.reloadResourcePacks().thenRun(() -> {
                completableFuture.complete(null);
            });
        }

        Runnable runnable;
        while ((runnable = this.progressTasks.poll()) != null) {
            runnable.run();
        }

        if (tick) {
            int i = this.timer.advanceTime(Util.getMillis());
            this.profiler.push("scheduledExecutables");
            this.runAllTasks();
            this.profiler.pop();
            this.profiler.push("tick");

            for (int j = 0; j < Math.min(10, i); ++j) {
                this.profiler.incrementCounter("clientTick");
                this.tick();
            }

            this.profiler.pop();
        }
    }

    @Override
    public void doRender(boolean tick, long frameStartTime, RenderPass renderPass) {
        XR_RENDERER.renderPass = renderPass;
        this.profiler.push("render");
        PoseStack matrixStack = RenderSystem.getModelViewStack();
        matrixStack.pushPose();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.clear(16640, ON_OSX);
        this.mainRenderTarget.bindWrite(true);
        FogRenderer.setupNoFog();
        this.profiler.push("display");
        RenderSystem.enableTexture();
        RenderSystem.enableCull();
        this.profiler.pop();
        if (!this.noRender) {
            this.profiler.popPush("gameRenderer");
            this.gameRenderer.render(this.pause ? this.pausePartialTick : this.timer.partialTick, frameStartTime, tick);

            if (XR_RENDERER.renderPass == RenderPass.GUI || XR_RENDERER.renderPass == RenderPass.VANILLA) {
                this.profiler.popPush("toasts");
                this.toast.render(new PoseStack());
                this.profiler.pop();
            }
        }

        if (this.fpsPieResults != null) {
            this.profiler.push("fpsPie");
            this.renderFpsMeter(new PoseStack(), this.fpsPieResults);
            this.profiler.pop();
        }

//        this.profiler.push("blit");
        this.mainRenderTarget.unbindWrite();
        matrixStack.popPose();

        XR_RENDERER.renderPass = RenderPass.VANILLA;
    }

    @Override
    public void postRender() {
        GLFW.glfwPollEvents();
        RenderSystem.replayQueue();
        Tesselator.getInstance().getBuilder().clear();
//        GLFW.glfwSwapBuffers(window.getHandle());
        WindowAcc windowAcc = ((WindowAcc) (Object) window);
        if (window.isFullscreen() != windowAcc.getActuallyFullscreen()) {
            windowAcc.setActuallyFullscreen(window.isFullscreen());
            windowAcc.invokeUpdateFullscreen(windowAcc.getVsync());
        }
        GLFW.glfwPollEvents();

        this.window.setErrorSection("Post render");
        ++this.frames;
        boolean bl = this.hasSingleplayerServer() && (this.screen != null && this.screen.isPauseScreen() || this.overlay != null && this.overlay.isPauseScreen()) && !this.singleplayerServer.isPublished();
        if (this.pause != bl) {
            if (this.pause) {
                this.pausePartialTick = this.timer.partialTick;
            } else {
                this.timer.partialTick = this.pausePartialTick;
            }

            this.pause = bl;
        }

        long m = Util.getNanos();
        this.frameTimer.logFrameDuration(m - this.lastNanoTime);
        this.lastNanoTime = m;
        this.profiler.push("fpsUpdate");

        while (Util.getMillis() >= this.lastTime + 1000L) {
            fps = this.frames;
            this.fpsString = String.format("%d fps T: %s%s%s%s B: %d", fps, (double) this.options.framerateLimit == Option.FRAMERATE_LIMIT.getMaxValue() ? "inf" : this.options.framerateLimit, this.options.enableVsync ? " vsync" : "", this.options.graphicsMode.toString(), this.options.renderClouds == CloudStatus.OFF ? "" : (this.options.renderClouds == CloudStatus.FAST ? " fast-clouds" : " fancy-clouds"), this.options.biomeBlendRadius);
            this.lastTime += 1000L;
            this.frames = 0;
        }

        this.profiler.pop();
    }
}
