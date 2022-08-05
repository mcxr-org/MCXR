package net.sorenon.mcxr.play.openxr;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.openxr.*;

import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.memAddress;
import static org.lwjgl.system.MemoryUtil.memUTF8;

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
}
