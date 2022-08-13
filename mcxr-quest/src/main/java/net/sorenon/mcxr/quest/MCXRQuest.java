package net.sorenon.mcxr.quest;

import net.fabricmc.api.ClientModInitializer;
import net.sorenon.mcxr.play.MCXRNativeLoad;
import net.sorenon.mcxr.play.MCXRPlatform;
import net.sorenon.mcxr.play.openxr.OpenXRInstance;
import org.lwjgl.openxr.*;
import org.lwjgl.system.Library;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.SharedLibrary;
import org.lwjgl.system.Struct;

import java.lang.reflect.Method;
import java.nio.IntBuffer;
import java.util.List;

import static net.sorenon.mcxr.play.MCXRPlayClient.PLATFORM;
import static org.lwjgl.system.MemoryStack.stackInts;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memGetAddress;

public class MCXRQuest implements ClientModInitializer, MCXRPlatform {

    @Override
    public void onInitializeClient() {
//        Configuration.OPENXR_EXPLICIT_INIT.set(true);
        PLATFORM = this;
        PLATFORM.loadNatives();
    }

    @Override
    public SharedLibrary getOpenXRLib() {
       return Library.loadNative(XR.class, "org.lwjgl.openxr" ,"openxr_loader", false);

    }

    @Override
    public void loadNatives() {
        XR.create("openxr_loader");
    }

    @Override
    public Struct createGraphicsBinding(MemoryStack stack) {
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
        } catch (Exception e) {
            System.out.println(e);
        }
        throw new IllegalStateException("Could not get the classes needed by reflection!");
    }

    @Override
    public List<String> tryEnableExtensions(XrExtensionProperties.Buffer availableExtensions) {
        return List.of(KHROpenglEsEnable.XR_KHR_OPENGL_ES_ENABLE_EXTENSION_NAME, KHRAndroidCreateInstance.XR_KHR_ANDROID_CREATE_INSTANCE_EXTENSION_NAME);
    }

    @Override
    public PlatformType getPlatform() {
        return PlatformType.Quest;
    }

    @Override
    public long xrInstanceCreateInfoNext(MemoryStack stack) {
        XrInstanceCreateInfoAndroidKHR androidCreateInfo = XrInstanceCreateInfoAndroidKHR.malloc(stack);
        androidCreateInfo.set(
                KHRAndroidCreateInstance.XR_TYPE_INSTANCE_CREATE_INFO_ANDROID_KHR,
                NULL,
                memGetAddress(MCXRNativeLoad.getJVMPtr()),
                memGetAddress(MCXRNativeLoad.getApplicationActivityPtr())
        );

        return androidCreateInfo.address();
    }

    @Override
    public int[] enumerateSwapchainImages(OpenXRInstance instance, XrSwapchain handle) {
        try (MemoryStack stack = stackPush()) {
            IntBuffer intBuf = stackInts(0);

            instance.checkPanic(XR10.xrEnumerateSwapchainImages(handle, intBuf, null), "xrEnumerateSwapchainImages");

            int imageCount = intBuf.get(0);
            XrSwapchainImageOpenGLESKHR.Buffer swapchainImageBuffer = XrSwapchainImageOpenGLESKHR.calloc(imageCount, stack);
            for (XrSwapchainImageOpenGLESKHR image : swapchainImageBuffer) {
                image.type(KHROpenglEsEnable.XR_TYPE_SWAPCHAIN_IMAGE_OPENGL_ES_KHR);
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
    public void checkGraphicsRequirements(OpenXRInstance instance, long system) {
        try (var stack = stackPush()) {
            XrGraphicsRequirementsOpenGLESKHR graphicsRequirements = XrGraphicsRequirementsOpenGLESKHR.calloc(stack).type(KHROpenglEsEnable.XR_TYPE_GRAPHICS_REQUIREMENTS_OPENGL_ES_KHR);
            instance.checkPanic(KHROpenglEsEnable.xrGetOpenGLESGraphicsRequirementsKHR(instance.handle, system, graphicsRequirements), "xrGetOpenGLESGraphicsRequirementsKHR");
        }
    }

    @Override
    public void framebufferTextureLayer(int color, int index) {
        MCXRNativeLoad.renderImage(color, index);
    }
}
