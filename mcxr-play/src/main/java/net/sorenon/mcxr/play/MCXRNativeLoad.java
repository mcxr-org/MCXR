package net.sorenon.mcxr.play;

public class MCXRNativeLoad {
    static {
        System.loadLibrary("mcxr_loader");
    }
}
