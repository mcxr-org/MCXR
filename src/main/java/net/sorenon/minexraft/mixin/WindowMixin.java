package net.sorenon.minexraft.mixin;

import net.minecraft.client.WindowEventHandler;
import net.minecraft.client.WindowSettings;
import net.minecraft.client.util.MonitorTracker;
import net.minecraft.client.util.Window;
import net.sorenon.minexraft.OpenXR;
import net.sorenon.minexraft.MineXRaftClient;
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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

@Mixin(Window.class)
public class WindowMixin {

    @Shadow
    @Final
    private long handle;

    @Shadow
    private double scaleFactor;

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwCreateWindow(IILjava/lang/CharSequence;JJ)J"), method = "<init>")
    private long onGlfwCreateWindow(int width, int height, CharSequence title, long monitor, long share) {
        GLFW.glfwWindowHint(GLFW.GLFW_DOUBLEBUFFER, GLFW.GLFW_FALSE); //Disable vsync

        return GLFW.glfwCreateWindow(width, height, title, monitor, share);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void postInit(WindowEventHandler eventHandler, MonitorTracker monitorTracker, WindowSettings settings, String videoMode, String title, CallbackInfo ci) {
        //TODO initialize session only on user command
        OpenXR openXR = MineXRaftClient.OPEN_XR;
        try (MemoryStack stack = stackPush()) {
            //Initialize OpenXR's OpenGL compatability
            XrGraphicsRequirementsOpenGLKHR graphicsRequirements = XrGraphicsRequirementsOpenGLKHR.mallocStack();
            graphicsRequirements.set(KHROpenglEnable.XR_TYPE_GRAPHICS_REQUIREMENTS_OPENGL_KHR, 0, 0, 0);
            openXR.xrCheck(KHROpenglEnable.xrGetOpenGLGraphicsRequirementsKHR(openXR.xrInstance, openXR.systemID, graphicsRequirements));

            //Check if OpenGL ver is supported by OpenXR ver
            if (graphicsRequirements.minApiVersionSupported() > XR10.XR_MAKE_VERSION(GL11.glGetInteger(GL30.GL_MAJOR_VERSION), GL11.glGetInteger(GL30.GL_MINOR_VERSION), 0)) {
                throw new IllegalStateException("Runtime does not support desired Graphics API and/or version");
            }

            //Bind the OpenGL context to the OpenXR instance and create the session
            if (Platform.get() == Platform.WINDOWS) {
                XrGraphicsBindingOpenGLWin32KHR graphicsBinding = XrGraphicsBindingOpenGLWin32KHR.malloc();
                graphicsBinding.set(
                        KHROpenglEnable.XR_TYPE_GRAPHICS_BINDING_OPENGL_WIN32_KHR,
                        NULL,
                        User32.GetDC(GLFWNativeWin32.glfwGetWin32Window(handle)),
                        GLFWNativeWGL.glfwGetWGLContext(handle)
                );
                openXR.graphicsBinding = graphicsBinding;
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
            openXR.xrCheck(XR10.xrCreateSession(openXR.xrInstance, sessionCreateInfo, pp));
            System.out.println(pp.get(0));
            openXR.xrSession = new XrSession(pp.get(0), openXR.xrInstance);
        }

        openXR.createXRReferenceSpaces();
        openXR.createXRSwapchains();
        openXR.createOpenGLResourses();
        openXR.makeActions();
    }

    private double sca2 = 1;
    @Inject(method = "calculateScaleFactor", at = @At("HEAD"))
    void sca(int guiScale, boolean forceUnicodeFont, CallbackInfoReturnable<Integer> cir){
        int framebufferWidth = 1920;
        int framebufferHeight = 1080;
        
        int i;
        for(i = 1; i != guiScale && i < framebufferWidth && i < framebufferHeight && framebufferWidth / (i + 1) >= 320 && framebufferHeight / (i + 1) >= 240; ++i) {
        }

        if (forceUnicodeFont && i % 2 != 0) {
            ++i;
        }
        sca2 = i;
    } 
    
    @Inject(method = "getFramebufferWidth", at = @At("HEAD"), cancellable = true)
    void frameBufferWidth(CallbackInfoReturnable<Integer> cir) {
        XrRect2Di rect = MineXRaftClient.viewportRect;
        if (rect != null) {
            cir.setReturnValue(MineXRaftClient.primaryRenderTarget.textureWidth);
        }
    }

    @Inject(method = "getFramebufferHeight", at = @At("HEAD"), cancellable = true)
    void frameBufferHeight(CallbackInfoReturnable<Integer> cir) {
        XrRect2Di rect = MineXRaftClient.viewportRect;
        if (rect != null) {
            cir.setReturnValue(MineXRaftClient.primaryRenderTarget.textureHeight);
        }
    }

    @Inject(method = "getScaledHeight", at = @At("HEAD"), cancellable = true)
    void scframeBufferHeight(CallbackInfoReturnable<Integer> cir) {
        XrRect2Di rect = MineXRaftClient.viewportRect;
        if (rect != null) {
            int j = (int) (MineXRaftClient.primaryRenderTarget.textureHeight / sca2);
            cir.setReturnValue(MineXRaftClient.primaryRenderTarget.textureHeight / sca2 > (double) j ? j + 1 : j);
        }
    }

    @Inject(method = "getScaledWidth", at = @At("HEAD"), cancellable = true)
    void scframeBufferWidth(CallbackInfoReturnable<Integer> cir) {
        XrRect2Di rect = MineXRaftClient.viewportRect;
        if (rect != null) {
            int j = (int) (MineXRaftClient.primaryRenderTarget.textureWidth / sca2);
            cir.setReturnValue(MineXRaftClient.primaryRenderTarget.viewportWidth / sca2 > (double) j ? j + 1 : j);
        }
    }

    @Inject(method = "getScaleFactor", at = @At("HEAD"), cancellable = true)
    void f(CallbackInfoReturnable<Double> cir) {
        XrRect2Di rect = MineXRaftClient.viewportRect;
        if (rect != null) {
            cir.setReturnValue(sca2);
        }
    }


//    @Inject(method = "swapBuffers", at = @At("HEAD"), cancellable = true)
//    private void swapBuffers(CallbackInfo ci){
//        ci.cancel();
//    }

    @Inject(method = "onWindowFocusChanged", at = @At("HEAD"), cancellable = true)
    void foc(long window, boolean focused, CallbackInfo ci) {
        ci.cancel();
    }
}
