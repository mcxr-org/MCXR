package net.sorenon.minexraft;

import net.fabricmc.api.ModInitializer;
import org.lwjgl.system.SharedLibrary;

import java.nio.file.Paths;

import static org.lwjgl.system.APIUtil.apiCreateLibrary;
import static org.lwjgl.system.APIUtil.apiLog;

public class MineXRaft implements ModInitializer {

	@Override
	public void onInitialize() {
//		SharedLibrary defaultOpenXRLoader = loadNative(XR.class, "C:\\Users\\soren\\Documents\\Programming\\openxr_loader_windows\\x64\\bin\\openxr_loader.dll", "openxr_loader");
//		XR.create(defaultOpenXRLoader);
//		try {
//			HelloOpenXR.main(null);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		System.out.println("Hello Fabric world!");
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
