package net.sorenon.mcxr.play;

import net.sorenon.mcxr.play.openxr.OpenXRInstance;
import net.sorenon.mcxr.play.openxr.XrException;
import org.lwjgl.openxr.XrExtensionProperties;
import org.lwjgl.openxr.XrSwapchain;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.SharedLibrary;
import org.lwjgl.system.Struct;

import java.util.List;

public interface MCXRPlatform {

    SharedLibrary getOpenXRLib();

    void loadNatives();

    Struct createGraphicsBinding(MemoryStack stack);

    List<String> tryEnableExtensions(XrExtensionProperties.Buffer availableExtensions) throws XrException;

    void checkGraphicsRequirements(OpenXRInstance instance, long system);

    PlatformType getPlatform();

    long xrInstanceCreateInfoNext(MemoryStack stack);

    int[] enumerateSwapchainImages(OpenXRInstance instance, XrSwapchain swapchain);

    void framebufferTextureLayer(int color, int index);

    enum PlatformType {
        Desktop,
        Quest
    }
}
