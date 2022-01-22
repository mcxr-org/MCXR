package net.sorenon.mcxr.play.openxr;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFWNativeWGL;
import org.lwjgl.glfw.GLFWNativeWin32;
import org.lwjgl.openxr.*;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.Platform;
import org.lwjgl.system.Struct;
import org.lwjgl.system.windows.User32;

import java.lang.reflect.Method;

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
            XrGraphicsRequirementsOpenGLESKHR graphicsRequirements = XrGraphicsRequirementsOpenGLESKHR.calloc(stack).type(KHROpenglEsEnable.XR_TYPE_GRAPHICS_REQUIREMENTS_OPENGL_ES_KHR).next(NULL);
            instance.check(KHROpenglEsEnable.xrGetOpenGLESGraphicsRequirementsKHR(instance.handle, handle, graphicsRequirements), "xrGetOpenGLESGraphicsRequirementsKHR");

            XrSystemProperties systemProperties = XrSystemProperties.calloc(stack).type(XR10.XR_TYPE_SYSTEM_PROPERTIES);
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

    public Struct createOpenGLESBinding(MemoryStack stack) {
        //Bind the OpenGL context to the OpenXR instance and create the session
        try {
            Class<?> clazz = Class.forName("org.lwjgl.glfw.CallbackBridge");
            Method eglDisplay = clazz.getDeclaredMethod("getEGLDisplayPtr");
            Method eglConfig = clazz.getDeclaredMethod("getEGLConfigPtr");
            Method eglContext = clazz.getDeclaredMethod("getEGLContextPtr");
            long eglDisplayPtr = (long) eglDisplay.invoke(null);
            long eglConfigPtr = (long) eglConfig.invoke(null);
            long eglContextPtr = (long) eglContext.invoke(null);
            return XrGraphicsBindingOpenGLESAndroidKHR.calloc(stack).set(
                    KHROpenglEsEnable.XR_TYPE_GRAPHICS_BINDING_OPENGL_ES_ANDROID_KHR,
                    NULL,
                    eglDisplayPtr,
                    eglConfigPtr,
                    eglContextPtr
            );
        } catch(Exception e)  {
            System.out.println(e);
        }
        throw new IllegalStateException("Could not get the classes needed by reflection!");
    }
}
