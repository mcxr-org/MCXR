package net.sorenon.mcxr.desktop;

import com.mojang.blaze3d.platform.Window;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.sorenon.mcxr.play.MCXRPlatform;
import net.sorenon.mcxr.play.openxr.OpenXRInstance;
import net.sorenon.mcxr.play.openxr.XrException;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWNativeGLX;
import org.lwjgl.glfw.GLFWNativeWGL;
import org.lwjgl.glfw.GLFWNativeWin32;
import org.lwjgl.glfw.GLFWNativeX11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.openxr.*;
import org.lwjgl.system.*;
import org.lwjgl.system.linux.X11;
import org.lwjgl.system.windows.User32;

import java.nio.IntBuffer;
import java.util.List;
import java.util.Objects;

import static net.sorenon.mcxr.play.MCXRPlayClient.PLATFORM;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GLX13.*;
import static org.lwjgl.system.MemoryStack.stackInts;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class MCXRDesktop implements ClientModInitializer, MCXRPlatform {

    @Override
    public void onInitializeClient() {

        PLATFORM = this;
        PLATFORM.loadNatives();
    }

    @Override
    public SharedLibrary getOpenXRLib() {
        return Library.loadNative(XR.class, "openxr_loader", false);

    }

    @Override
    public void loadNatives() {
        XR.create("openxr_loader");
    }

    @Override
    public Struct createGraphicsBinding(MemoryStack stack) {
        //Bind the OpenGL context to the OpenXR instance and create the session
        Window window = Minecraft.getInstance().getWindow();
        long windowHandle = window.getWindow();
        if (Platform.get() == Platform.WINDOWS) {
            return XrGraphicsBindingOpenGLWin32KHR.calloc(stack).set(
                    KHROpenglEnable.XR_TYPE_GRAPHICS_BINDING_OPENGL_WIN32_KHR,
                    NULL,
                    User32.GetDC(GLFWNativeWin32.glfwGetWin32Window(windowHandle)),
                    GLFWNativeWGL.glfwGetWGLContext(windowHandle)
            );
        } else if (Platform.get() == Platform.LINUX) {
            //Possible TODO Wayland + XCB (look at https://github.com/Admicos/minecraft-wayland)
            long xDisplay = GLFWNativeX11.glfwGetX11Display();

            long glXContext = GLFWNativeGLX.glfwGetGLXContext(windowHandle);
            long glXWindowHandle = GLFWNativeGLX.glfwGetGLXWindow(windowHandle);

            int fbXID = glXQueryDrawable(xDisplay, glXWindowHandle, GLX_FBCONFIG_ID);
            PointerBuffer fbConfigBuf = glXChooseFBConfig(xDisplay, X11.XDefaultScreen(xDisplay), stackInts(GLX_FBCONFIG_ID, fbXID, 0));
            if (fbConfigBuf == null) {
                throw new IllegalStateException("Your framebuffer config was null, make a github issue");
            }
            long fbConfig = fbConfigBuf.get();

            return XrGraphicsBindingOpenGLXlibKHR.calloc(stack).set(
                    KHROpenglEnable.XR_TYPE_GRAPHICS_BINDING_OPENGL_XLIB_KHR,
                    NULL,
                    xDisplay,
                    (int) Objects.requireNonNull(glXGetVisualFromFBConfig(xDisplay, fbConfig)).visualid(),
                    fbConfig,
                    glXWindowHandle,
                    glXContext
            );
        } else {
            throw new IllegalStateException("Macos not supported");
        }
    }

    @Override
    public List<String> tryEnableExtensions(XrExtensionProperties.Buffer availableExtensions) throws XrException {
        while (availableExtensions.hasRemaining()) {
            XrExtensionProperties prop = availableExtensions.get();
            String extensionName = prop.extensionNameString();
            if (extensionName.equals(KHROpenglEnable.XR_KHR_OPENGL_ENABLE_EXTENSION_NAME)) {
                return List.of(KHROpenglEnable.XR_KHR_OPENGL_ENABLE_EXTENSION_NAME);
            }
        }

        throw new XrException(0, "OpenXR runtime does not support OpenGL, try using SteamVR instead");
    }

    @Override
    public PlatformType getPlatform() {
        return PlatformType.Desktop;
    }

    @Override
    public long xrInstanceCreateInfoNext(MemoryStack stack) {
        return 0;
    }

    @Override
    public int[] enumerateSwapchainImages(OpenXRInstance instance, XrSwapchain handle) {
        try (MemoryStack stack = stackPush()) {
            IntBuffer intBuf = stackInts(0);

            instance.checkPanic(XR10.xrEnumerateSwapchainImages(handle, intBuf, null), "xrEnumerateSwapchainImages");

            int imageCount = intBuf.get(0);
            XrSwapchainImageOpenGLKHR.Buffer swapchainImageBuffer = XrSwapchainImageOpenGLKHR.calloc(imageCount, stack);
            for (XrSwapchainImageOpenGLKHR image : swapchainImageBuffer) {
                image.type(KHROpenglEnable.XR_TYPE_SWAPCHAIN_IMAGE_OPENGL_KHR);
            }

            instance.checkPanic(XR10.xrEnumerateSwapchainImages(handle, intBuf, XrSwapchainImageBaseHeader.create(swapchainImageBuffer.address(), swapchainImageBuffer.capacity())), "xrEnumerateSwapchainImages");

            var arr = new int[imageCount];
            for (int i = 0; i < imageCount; i++) {
                arr[i] = swapchainImageBuffer.get(i).image();
            }
            return arr;
        }
    }

    @Override
    public void framebufferTextureLayer(int color, int index) {
        GL30.glFramebufferTextureLayer(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, color, 0, index);
    }

    @Override
    public void checkGraphicsRequirements(OpenXRInstance instance, long system) {
        try (var stack = stackPush()) {
            XrGraphicsRequirementsOpenGLKHR graphicsRequirements = XrGraphicsRequirementsOpenGLKHR.calloc(stack).type(KHROpenglEnable.XR_TYPE_GRAPHICS_REQUIREMENTS_OPENGL_KHR);
            instance.checkPanic(KHROpenglEnable.xrGetOpenGLGraphicsRequirementsKHR(instance.handle, system, graphicsRequirements), "xrGetOpenGLGraphicsRequirementsKHR");
        }
    }
}
