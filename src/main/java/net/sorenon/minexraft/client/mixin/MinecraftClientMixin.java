package net.sorenon.minexraft.client.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SplashScreen;
import net.minecraft.client.options.CloudRenderMode;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.Option;
import net.minecraft.client.render.*;
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
import net.sorenon.minexraft.client.MineXRaftClient;
import net.sorenon.minexraft.client.OpenXR;
import net.sorenon.minexraft.client.RenderPass;
import net.sorenon.minexraft.client.input.VanillaCompatActionSet;
import net.sorenon.minexraft.client.input.XrInput;
import net.sorenon.minexraft.client.accessor.MinecraftClientEXT;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.openxr.XR10;
import org.lwjgl.openxr.XrEventDataBuffer;
import org.lwjgl.openxr.XrSessionActionSetsAttachInfo;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Queue;
import java.util.concurrent.CompletableFuture;

import static org.lwjgl.system.MemoryStack.stackPointers;
import static org.lwjgl.system.MemoryUtil.NULL;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin extends ReentrantThreadExecutor<Runnable> implements MinecraftClientEXT {

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
    public GameOptions options;

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

    @Shadow @Nullable public ClientWorld world;
    @Unique
    private Framebuffer mainRenderTarget;

    @Inject(method = "run", at = @At("HEAD"))
    void start(CallbackInfo ci) {
        OpenXR openXR = MineXRaftClient.OPEN_XR;
        openXR.eventDataBuffer = XrEventDataBuffer.calloc();
        openXR.eventDataBuffer.type(XR10.XR_TYPE_EVENT_DATA_BUFFER);

        openXR.createXRSwapchains();
        MineXRaftClient.XR_INPUT = new XrInput(openXR);
//        MineXRaftClient.XR_INPUT.makeActions();

        VanillaCompatActionSet vanillaCompatActionSet = MineXRaftClient.XR_INPUT.makeGameplayActionSet();
        // Attach the action set we just made to the session
        XrSessionActionSetsAttachInfo attach_info = XrSessionActionSetsAttachInfo.mallocStack().set(
                XR10.XR_TYPE_SESSION_ACTION_SETS_ATTACH_INFO,
                NULL,
                stackPointers(vanillaCompatActionSet.address())
        );
        openXR.check(XR10.xrAttachSessionActionSets(openXR.xrSession, attach_info));
        MineXRaftClient.vanillaCompatActionSet = vanillaCompatActionSet;

//        OpenXR.Swapchain swapchain = openXR.swapchains[0];
//        framebuffer.resize(swapchain.width, swapchain.height, true);
        mainRenderTarget = framebuffer;
        MineXRaftClient.guiFramebuffer = new Framebuffer(1920, 1080, true, IS_SYSTEM_MAC);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;render(Z)V"), method = "run")
    void loop(MinecraftClient minecraftClient, boolean tick) throws InterruptedException {
        OpenXR openXR = MineXRaftClient.OPEN_XR;
        XrInput xrInput = MineXRaftClient.XR_INPUT;
        if (openXR.pollEvents()) {
            running = false;
            return;
        }

        if (openXR.sessionRunning) {
            xrInput.pollActions();
            openXR.renderFrameOpenXR();
        } else {
            // Throttle loop since xrWaitFrame won't be called.
            Thread.sleep(250);
        }
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;init(Lnet/minecraft/client/MinecraftClient;II)V"), method = "openScreen")
    void initScreen(Screen screen, MinecraftClient client, int width, int height) {
        if (world != null) {
            MineXRaftClient.primaryRenderTarget = MineXRaftClient.guiFramebuffer;
            screen.init(client, this.window.getScaledWidth(), this.window.getScaledHeight());
            MineXRaftClient.tmpResetSize();
        } else {
            screen.init(client, width, height);
        }
    }

    /**
     * I split render into 3 functions because it makes developing easier and I couldn't find any mixins that used it
     */
    @Override
    public void preRenderXR(boolean tick, long time) {
        this.window.setPhase("Pre render");
        if (this.window.shouldClose()) {
            this.scheduleStop();
        }

        if (this.resourceReloadFuture != null && !(this.overlay instanceof SplashScreen)) {
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

        int k;
        if (tick) {
            k = this.renderTickCounter.beginRenderTick(Util.getMeasuringTimeMs());
            this.profiler.push("scheduledExecutables");
            this.runTasks();
            this.profiler.pop();
            this.profiler.push("tick");

            for (int j = 0; j < Math.min(10, k); ++j) {
                this.profiler.visit("clientTick");
                this.tick();
            }

            this.profiler.pop();
        }

        this.mouse.updateMouse();
        this.window.setPhase("Render");
        this.profiler.push("sound");
        this.soundManager.updateListenerPosition(this.gameRenderer.getCamera());
        this.profiler.pop();
    }

    @Override
    public void doRenderXR(boolean tick, long frameStartTime) {
        this.profiler.push("render");
        RenderSystem.pushMatrix();
        RenderSystem.clear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT, IS_SYSTEM_MAC);
        this.framebuffer.beginWrite(true);
        BackgroundRenderer.method_23792();
        this.profiler.push("display");
        RenderSystem.enableTexture();
        RenderSystem.enableCull();
        this.profiler.pop();
        if (!this.skipGameRender) {
            this.profiler.swap("gameRenderer");
            this.gameRenderer.render(this.paused ? this.pausedTickDelta : this.renderTickCounter.tickDelta, frameStartTime, tick);

            if (MineXRaftClient.renderPass == RenderPass.GUI) {
                this.profiler.swap("toasts");
                this.toastManager.draw(new MatrixStack());
                this.profiler.pop();

                MineXRaftClient.tmpResetSize();
                framebuffer.beginWrite(true);
            }
        }

        if (this.tickProfilerResult != null) {
            this.profiler.push("fpsPie");
            this.drawProfilerResults(new MatrixStack(), this.tickProfilerResult);
            this.profiler.pop();
        }

        this.profiler.push("blit");
        this.framebuffer.endWrite();
        RenderSystem.popMatrix();

        RenderSystem.replayQueue();
        Tessellator.getInstance().getBuffer().clear();

        /*d
        RenderSystem.pushMatrix();
        this.framebuffer.draw(this.window.getFramebufferWidth(), this.window.getFramebufferHeight());
        RenderSystem.popMatrix();
        this.profiler.swap("updateDisplay");
        this.window.swapBuffers();
         */
    }

    @Override
    public void postRenderXR(boolean tick, long frameStartTime) {
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

    public void render(){
        this.render(true);
    }

    public void setRenderTarget(Framebuffer framebuffer) {
        this.framebuffer = framebuffer;
    }

    public void popRenderTarget() {
        framebuffer = mainRenderTarget;
    }
}
