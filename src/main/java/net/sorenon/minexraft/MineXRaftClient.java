package net.sorenon.minexraft;

import net.fabricmc.api.ClientModInitializer;
import org.lwjgl.openxr.XR;
import org.lwjgl.openxr.XrFovf;
import org.lwjgl.openxr.XrRect2Di;
import org.lwjgl.system.SharedLibrary;

import java.nio.file.Paths;

import static org.lwjgl.system.APIUtil.apiCreateLibrary;
import static org.lwjgl.system.APIUtil.apiLog;

public class MineXRaftClient implements ClientModInitializer {

    public static final HelloOpenXR helloOpenXR = new HelloOpenXR();
    public static XrRect2Di viewportRect = null;
    public static XrFovf fov = null;

    @Override
    public void onInitializeClient() {
		SharedLibrary defaultOpenXRLoader = loadNative(XR.class, "C:\\Users\\soren\\Documents\\Programming\\openxr_loader_windows\\x64\\bin\\openxr_loader.dll", "openxr_loader");
		XR.create(defaultOpenXRLoader);
        helloOpenXR.createOpenXRInstance();
        helloOpenXR.initializeOpenXRSystem();

		System.out.println("Hello Fabric world!");
    }

    private static SharedLibrary loadNative(Class<?> context, String path, String libName) {
        apiLog("Loading library: " + path);

        // METHOD 1: absolute path
        if (Paths.get(path).isAbsolute()) {
            SharedLibrary lib = apiCreateLibrary(path);
            apiLog("\tSuccess");
            return lib;
        }

        throw new UnsatisfiedLinkError("Failed to locate library: " + libName);
    }
}
