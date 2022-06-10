package net.sorenon.mcxr.play.openxr;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWNativeGLX;
import org.lwjgl.glfw.GLFWNativeWGL;
import org.lwjgl.glfw.GLFWNativeWin32;
import org.lwjgl.glfw.GLFWNativeX11;
import org.lwjgl.openxr.*;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.Platform;
import org.lwjgl.system.Struct;
import org.lwjgl.system.linux.X11;
import org.lwjgl.system.windows.User32;

import java.util.Objects;

import static org.lwjgl.opengl.GLX13.*;
import static org.lwjgl.system.MemoryStack.stackInts;
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

        try (var stack = stackPush()) {
            XrGraphicsRequirementsOpenGLKHR graphicsRequirements = XrGraphicsRequirementsOpenGLKHR.calloc(stack).type(KHROpenGLEnable.XR_TYPE_GRAPHICS_REQUIREMENTS_OPENGL_KHR);
            instance.checkPanic(KHROpenGLEnable.xrGetOpenGLGraphicsRequirementsKHR(instance.handle, handle, graphicsRequirements), "xrGetOpenGLGraphicsRequirementsKHR");

            XrSystemProperties systemProperties = XrSystemProperties.calloc(stack).type(XR10.XR_TYPE_SYSTEM_PROPERTIES);
            instance.checkPanic(XR10.xrGetSystemProperties(instance.handle, handle, systemProperties), "xrGetSystemProperties");
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

    public Struct createOpenGLBinding(MemoryStack stack) {
        //Bind the OpenGL context to the OpenXR instance and create the session
        Window window = Minecraft.getInstance().getWindow();
        long windowHandle = window.getWindow();
        if (Platform.get() == Platform.WINDOWS) {
            return XrGraphicsBindingOpenGLWin32KHR.malloc(stack).set(
                    KHROpenGLEnable.XR_TYPE_GRAPHICS_BINDING_OPENGL_WIN32_KHR,
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
            if(fbConfigBuf == null) {
                throw new IllegalStateException("Your framebuffer config was null, make a github issue");
            }
            long fbConfig = fbConfigBuf.get();

            return XrGraphicsBindingOpenGLXlibKHR.calloc(stack).set(
                    KHROpenGLEnable.XR_TYPE_GRAPHICS_BINDING_OPENGL_XLIB_KHR,
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
}
