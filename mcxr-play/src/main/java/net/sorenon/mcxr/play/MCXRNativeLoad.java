package net.sorenon.mcxr.play;

import org.lwjgl.PointerBuffer;
import org.lwjgl.openxr.XrLoaderInitInfoAndroidKHR;

public class MCXRNativeLoad {
    public static native long getBaseHeaderAddress(XrLoaderInitInfoAndroidKHR loader);

    static {
        System.loadLibrary("mcxr_loader");
    }
}
