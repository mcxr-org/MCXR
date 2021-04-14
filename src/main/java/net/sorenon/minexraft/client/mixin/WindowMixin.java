package net.sorenon.minexraft.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.WindowEventHandler;
import net.minecraft.client.WindowSettings;
import net.minecraft.client.util.MonitorTracker;
import net.minecraft.client.util.Window;
import net.sorenon.minexraft.client.rendering.MainRenderTarget;
import net.sorenon.minexraft.client.OpenXR;
import net.sorenon.minexraft.client.MineXRaftClient;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWNativeWGL;
import org.lwjgl.glfw.GLFWNativeWin32;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.openxr.*;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.Platform;
import org.lwjgl.system.windows.User32;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static org.lwjgl.system.MemoryStack.stackPointers;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * This class is a bit of a disaster as a result of getting the mod off of the ground but I'm working on it
 */
@Mixin(Window.class)
public class WindowMixin {

    @Shadow
    @Final
    private long handle;

    /**
     * Disables any sort of VSync or double buffering since we don't want the refresh rate of the monitor affecting the draw cycle
     * Maybe there is a way to switch buffers on a different thread to the XR rendering?
     */
    @Redirect(at = @At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwCreateWindow(IILjava/lang/CharSequence;JJ)J"), method = "<init>")
    private long onGlfwCreateWindow(int width, int height, CharSequence title, long monitor, long share) {
        GLFW.glfwWindowHint(GLFW.GLFW_DOUBLEBUFFER, GLFW.GLFW_FALSE);
        return GLFW.glfwCreateWindow(width, height, title, monitor, share);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void postInit(WindowEventHandler eventHandler, MonitorTracker monitorTracker, WindowSettings settings, String videoMode, String title, CallbackInfo ci) {
        OpenXR openXR = MineXRaftClient.OPEN_XR;
        try (MemoryStack stack = stackPush()) {
            //Initialize OpenXR's OpenGL compatability
            XrGraphicsRequirementsOpenGLKHR graphicsRequirements = XrGraphicsRequirementsOpenGLKHR.mallocStack();
            graphicsRequirements.set(KHROpenglEnable.XR_TYPE_GRAPHICS_REQUIREMENTS_OPENGL_KHR, 0, 0, 0);
            openXR.check(KHROpenglEnable.xrGetOpenGLGraphicsRequirementsKHR(openXR.xrInstance, openXR.systemID, graphicsRequirements));

            //Check if OpenGL ver is supported by OpenXR ver
            if (graphicsRequirements.minApiVersionSupported() > XR10.XR_MAKE_VERSION(GL11.glGetInteger(GL30.GL_MAJOR_VERSION), GL11.glGetInteger(GL30.GL_MINOR_VERSION), 0)) {
                throw new IllegalStateException("Runtime does not support desired Graphics API and/or version");
            }

            //Bind the OpenGL context to the OpenXR instance and create the session
            if (Platform.get() == Platform.WINDOWS) {
                openXR.graphicsBinding = createWinBinding();
            } else {
                throw new IllegalStateException();
            }

            XrSessionCreateInfo sessionCreateInfo = XrSessionCreateInfo.mallocStack();
            sessionCreateInfo.set(
                    XR10.XR_TYPE_SESSION_CREATE_INFO,
                    openXR.graphicsBinding.address(),
                    0,
                    openXR.systemID
            );

            PointerBuffer pp = stack.mallocPointer(1);
            openXR.check(XR10.xrCreateSession(openXR.xrInstance, sessionCreateInfo, pp));
            System.out.println(pp.get(0));
            openXR.xrSession = new XrSession(pp.get(0), openXR.xrInstance);
        }

//        if (openXR.xrInstance.getCapabilities().XR_FB_color_space){
//            FBColorSpace.xrSetColorSpaceFB(openXR.xrSession, FBColorSpace.XR_COLOR_SPACE_RIFT_S_FB);
//        }

        openXR.createXRReferenceSpaces();

        MineXRaftClient.guiScale = MineXRaftClient.calcGuiScale();
//        System.exit(0);
    }

    @Unique
    private XrGraphicsBindingOpenGLWin32KHR createWinBinding() {
        XrGraphicsBindingOpenGLWin32KHR graphicsBinding = XrGraphicsBindingOpenGLWin32KHR.malloc();
        graphicsBinding.set(
                KHROpenglEnable.XR_TYPE_GRAPHICS_BINDING_OPENGL_WIN32_KHR,
                NULL,
                User32.GetDC(GLFWNativeWin32.glfwGetWin32Window(handle)),
                GLFWNativeWGL.glfwGetWGLContext(handle)
        );
        return graphicsBinding;
    }

    @Inject(method = "getFramebufferWidth", at = @At("HEAD"), cancellable = true)
    void getFramebufferWidth(CallbackInfoReturnable<Integer> cir) {
        MainRenderTarget mainRenderTarget = (MainRenderTarget) MinecraftClient.getInstance().getFramebuffer();
        if (isCustomFramebuffer(mainRenderTarget)) {
            cir.setReturnValue(mainRenderTarget.viewportWidth);
        }
    }

    @Inject(method = "getFramebufferHeight", at = @At("HEAD"), cancellable = true)
    void getFramebufferHeight(CallbackInfoReturnable<Integer> cir) {
        MainRenderTarget mainRenderTarget = (MainRenderTarget) MinecraftClient.getInstance().getFramebuffer();
        if (isCustomFramebuffer(mainRenderTarget)) {
            cir.setReturnValue(mainRenderTarget.viewportHeight);
        }
    }

    @Inject(method = "getScaledHeight", at = @At("HEAD"), cancellable = true)
    void getScaledHeight(CallbackInfoReturnable<Integer> cir) {
        MainRenderTarget mainRenderTarget = (MainRenderTarget) MinecraftClient.getInstance().getFramebuffer();
        if (isCustomFramebuffer(mainRenderTarget)) {
            int j = (int) (mainRenderTarget.viewportHeight / MineXRaftClient.guiScale);
            cir.setReturnValue(mainRenderTarget.viewportHeight / MineXRaftClient.guiScale > (double) j ? j + 1 : j);
        }
    }

    @Inject(method = "getScaledWidth", at = @At("HEAD"), cancellable = true)
    void getScaledWidth(CallbackInfoReturnable<Integer> cir) {
        MainRenderTarget mainRenderTarget = (MainRenderTarget) MinecraftClient.getInstance().getFramebuffer();
        if (isCustomFramebuffer(mainRenderTarget)) {
            int j = (int) (mainRenderTarget.viewportWidth / MineXRaftClient.guiScale);
            cir.setReturnValue(mainRenderTarget.viewportWidth / MineXRaftClient.guiScale > (double) j ? j + 1 : j);
        }
    }

    @Inject(method = "getScaleFactor", at = @At("HEAD"), cancellable = true)
    void getScaleFactor(CallbackInfoReturnable<Double> cir) {
        MainRenderTarget mainRenderTarget = (MainRenderTarget) MinecraftClient.getInstance().getFramebuffer();
        if (isCustomFramebuffer(mainRenderTarget)) {
            cir.setReturnValue(MineXRaftClient.guiScale);
        }
    }

    @Inject(method = "onWindowFocusChanged", at = @At("HEAD"), cancellable = true)
    void preventPauseOnUnFocus(long window, boolean focused, CallbackInfo ci) {
        ci.cancel();
    }
    
    @Unique
    boolean isCustomFramebuffer(MainRenderTarget mainRenderTarget){
        return mainRenderTarget != null && mainRenderTarget.isCustomFramebuffer();
    }
}
