package net.sorenon.mcxr.play.openxr;

import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GLX13;
import org.lwjgl.openxr.*;
import org.lwjgl.system.NativeType;
import org.lwjgl.system.Platform;
import org.lwjgl.system.Struct;
import org.lwjgl.system.windows.User32;

import java.util.Objects;

import static org.lwjgl.system.APIUtil.apiGetFunctionAddress;
import static org.lwjgl.system.Checks.CHECKS;
import static org.lwjgl.system.Checks.check;
import static org.lwjgl.system.JNI.invokePP;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.*;

public class OpenXRSystem {
    private static final Logger LOGGER = LogManager.getLogger();

    public final OpenXRInstance instance;
    public final int formFactor;
    public final long handle;

    public final String systemName;
    public final int vendor;
    public final boolean orientationTracking;
    public final boolean positionTracking;
    public final int maxWidth;
    public final int maxHeight;
    public final int maxLayerCount;

    public OpenXRSystem(OpenXRInstance instance, int formFactor, long handle) {
        this.instance = instance;
        this.formFactor = formFactor;
        this.handle = handle;

        try (var ignored = stackPush()) {
            XrGraphicsRequirementsOpenGLKHR graphicsRequirements = XrGraphicsRequirementsOpenGLKHR.callocStack().type(KHROpenglEnable.XR_TYPE_GRAPHICS_REQUIREMENTS_OPENGL_KHR);
            instance.check(KHROpenglEnable.xrGetOpenGLGraphicsRequirementsKHR(instance.handle, handle, graphicsRequirements), "xrGetOpenGLGraphicsRequirementsKHR");

            XrSystemProperties systemProperties = XrSystemProperties.callocStack().type(XR10.XR_TYPE_SYSTEM_PROPERTIES);
            instance.check(XR10.xrGetSystemProperties(instance.handle, handle, systemProperties), "xrGetSystemProperties");
            XrSystemTrackingProperties trackingProperties = systemProperties.trackingProperties();
            XrSystemGraphicsProperties graphicsProperties = systemProperties.graphicsProperties();

            systemName = memUTF8(memAddress(systemProperties.systemName()));
            vendor = systemProperties.vendorId();
            orientationTracking = trackingProperties.orientationTracking();
            positionTracking = trackingProperties.positionTracking();
            maxWidth = graphicsProperties.maxSwapchainImageWidth();
            maxHeight = graphicsProperties.maxSwapchainImageHeight();
            maxLayerCount = graphicsProperties.maxLayerCount();

            LOGGER.info(String.format("Found device with id: %d", handle));
            LOGGER.info(String.format("Headset Name:%s Vendor:%d ", systemName, vendor));
            LOGGER.info(String.format("Headset Orientation Tracking:%b Position Tracking:%b ", orientationTracking, positionTracking));
            LOGGER.info(String.format("Headset Max Width:%d Max Height:%d Max Layer Count:%d ", maxWidth, maxHeight, maxLayerCount));
        }
    }

    public Struct createOpenGLBinding() {
        //Bind the OpenGL context to the OpenXR instance and create the session
        long windowHandle = MinecraftClient.getInstance().getWindow().getHandle();
        if (Platform.get() == Platform.WINDOWS) {
            return XrGraphicsBindingOpenGLWin32KHR.mallocStack().set(
                    KHROpenglEnable.XR_TYPE_GRAPHICS_BINDING_OPENGL_WIN32_KHR,
                    NULL,
                    User32.GetDC(GLFWNativeWin32.glfwGetWin32Window(windowHandle)),
                    GLFWNativeWGL.glfwGetWGLContext(windowHandle)
            );
        } else if (Platform.get() == Platform.LINUX) {
            //Possible TODO Wayland + XCB (look at https://github.com/Admicos/minecraft-wayland)
            long xDisplay = GLFWNativeX11.glfwGetX11Display();
            long fbConfig = glfwGetGLXFBConfig(windowHandle);
            return XrGraphicsBindingOpenGLXlibKHR.callocStack().set(
                    KHROpenglEnable.XR_TYPE_GRAPHICS_BINDING_OPENGL_XLIB_KHR,
                    NULL,
                    xDisplay,
                    (int) Objects.requireNonNull(GLX13.glXGetVisualFromFBConfig(xDisplay, fbConfig)).visualid(),
                    fbConfig,
                    GLFWNativeGLX.glfwGetGLXWindow(windowHandle),
                    GLFWNativeGLX.glfwGetGLXContext(windowHandle)
            );
        } else {
            throw new IllegalStateException("Macos not supported");
        }
    }

    //TODO document this and the hack enabling it + disable sha1 warning from LWJGL
    @NativeType("GLXFBConfig")
    public static long glfwGetGLXFBConfig(@NativeType("GLFWwindow *") long window) {
        long __functionAddress = apiGetFunctionAddress(GLFW.getLibrary(), "glfwGetGLXFBConfig");
        if (CHECKS) {
            check(window);
        }
        return invokePP(window, __functionAddress);
    }
}
