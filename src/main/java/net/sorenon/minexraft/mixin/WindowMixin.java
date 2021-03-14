package net.sorenon.minexraft.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.WindowEventHandler;
import net.minecraft.client.WindowSettings;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.util.MonitorTracker;
import net.minecraft.client.util.Window;
import net.sorenon.minexraft.HelloOpenXR;
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

import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_SRGB;
import static org.lwjgl.openxr.FBColorSpace.XR_COLOR_SPACE_UNMANAGED_FB;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

@Mixin(Window.class)
public class WindowMixin {

    @Shadow
    @Final
    private long handle;

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwCreateWindow(IILjava/lang/CharSequence;JJ)J"), method = "<init>")
    private long onGlfwCreateWindow(int width, int height, CharSequence title, long monitor, long share) {
//        GLFW.glfwWindowHint(GLFW.GLFW_DOUBLEBUFFER, GLFW.GLFW_FALSE); //Disable vsync

        return GLFW.glfwCreateWindow(width, height, title, monitor, share);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void postInit(WindowEventHandler eventHandler, MonitorTracker monitorTracker, WindowSettings settings, String videoMode, String title, CallbackInfo ci) throws InterruptedException {
//        MineXRaftClient.helloOpenXR.initializeAndBindOpenGL();
        HelloOpenXR helloOpenXR = MineXRaftClient.helloOpenXR;
        try (MemoryStack stack = stackPush()) {
            //Initialize OpenXR's OpenGL compatability
            XrGraphicsRequirementsOpenGLKHR graphicsRequirements = XrGraphicsRequirementsOpenGLKHR.mallocStack();
            graphicsRequirements.set(KHROpenglEnable.XR_TYPE_GRAPHICS_REQUIREMENTS_OPENGL_KHR, 0, 0, 0);
            KHROpenglEnable.xrGetOpenGLGraphicsRequirementsKHR(helloOpenXR.xrInstance, helloOpenXR.systemID, graphicsRequirements);

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
                helloOpenXR.graphicsBinding = graphicsBinding;
            } else {
                throw new IllegalStateException();
            }

            XrSessionCreateInfo sessionCreateInfo = XrSessionCreateInfo.mallocStack();
            sessionCreateInfo.set(
                    XR10.XR_TYPE_SESSION_CREATE_INFO,
                    helloOpenXR.graphicsBinding.address(),
                    0,
                    helloOpenXR.systemID
            );

            PointerBuffer pp = stack.mallocPointer(1);
            XR10.xrCreateSession(helloOpenXR.xrInstance, sessionCreateInfo, pp);
            System.out.println(pp.get(0));
            helloOpenXR.xrSession = new XrSession(pp.get(0), helloOpenXR.xrInstance);
        }

        helloOpenXR.createXRReferenceSpace();
        helloOpenXR.createXRSwapchains();
        helloOpenXR.createOpenGLResourses();
//        glEnable(GL_FRAMEBUFFER_SRGB); When this is on its too bright, when this is off its too dark :/
    }

//    @Inject(method = "getFramebufferWidth", at = @At("HEAD"), cancellable = true)
//    void frameBufferWidth(CallbackInfoReturnable<Integer> cir) {
//        Framebuffer fb = MinecraftClient.getInstance().getFramebuffer();
//        if (fb != null) {
//            cir.setReturnValue(fb.viewportWidth);
//        }
//    }
//
//    @Inject(method = "getFramebufferHeight", at = @At("HEAD"), cancellable = true)
//    void frameBufferHeight(CallbackInfoReturnable<Integer> cir) {
//        Framebuffer fb = MinecraftClient.getInstance().getFramebuffer();
//        if (fb != null) {
//            cir.setReturnValue(fb.viewportHeight);
//        }
//    }

//    @Inject(method = "swapBuffers", at = @At("HEAD"), cancellable = true)
//    private void swapBuffers(CallbackInfo ci){
//        ci.cancel();
//    }
}
