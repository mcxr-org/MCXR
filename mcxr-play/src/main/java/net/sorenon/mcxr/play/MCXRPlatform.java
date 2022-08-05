package net.sorenon.mcxr.play;

import net.sorenon.mcxr.play.openxr.XrException;
import org.lwjgl.openxr.XrExtensionProperties;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.Struct;

import java.util.List;

public interface MCXRPlatform {

    void loadNatives();

    Struct createGraphicsBinding(MemoryStack stack);

    List<String> tryEnableExtensions(XrExtensionProperties.Buffer availableExtensions) throws XrException;

    PlatformType getPlatform();

    default long xrInstanceCreateInfoNext() {
        return 0;
    }

    enum PlatformType {
        Desktop,
        Quest
    }
}
