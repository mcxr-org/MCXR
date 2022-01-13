package net.sorenon.mcxr.play;

public class MCXRNativeLoad {
    static {
        System.loadLibrary("mcxr_loader");
    }

    public static native long getJVMPtr();

    public static native long getCTXPtr();
}
