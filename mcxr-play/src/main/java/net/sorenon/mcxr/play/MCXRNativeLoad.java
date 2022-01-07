package net.sorenon.mcxr.play;

public class MCXRNativeLoad {
    public static native int load(long ptr, long app);

    static {
        System.loadLibrary("mcxr_loader");
    }

    public static native long castXrVoidFunctionType(long memAddress);
}
