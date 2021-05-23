package org.lwjgl.openxr;

import org.lwjgl.system.FunctionProvider;
import org.lwjgl.system.SharedLibrary;
import org.lwjgl.system.dyncall.DynCall;

import java.nio.file.Paths;

import static org.lwjgl.system.APIUtil.apiCreateLibrary;
import static org.lwjgl.system.APIUtil.apiLog;
import static org.lwjgl.system.MemoryUtil.NULL;

public class LWJGLHAX {

    public static boolean reportMissing(String api, String extension) {
        apiLog("[" + api + "] " + extension + " was reported as available but an entry point is missing.");
        return false;
    }

    public static boolean checkFunctions(FunctionProvider provider, long[] caps, int[] indices, String... functions) {
        boolean available = true;
        for (int i = 0; i < indices.length; i++) {
            int index = indices[i];
            if (index < 0 || caps[index] != NULL) {
                continue;
            }
            long address = provider.getFunctionAddress(functions[i]);
            if (address == NULL) {
                available = false;
                continue;
            }
            caps[index] = address;
        }
        return available;
    }

    public static SharedLibrary loadNative(Class<?> context, String path, String libName) {
        apiLog("Loading library: " + path);

        // METHOD 1: absolute path
        if (Paths.get(path).isAbsolute()) {
            SharedLibrary lib = apiCreateLibrary(path);
            apiLog("\tSuccess");
            return lib;
        }

        throw new UnsatisfiedLinkError("Failed to locate library: " + libName);
    }

    private static final long vm = DynCall.dcNewCallVM(4096);

    /**
     * Minecraft uses an old version of lwjgl so we have to create custom methods to call certain native functions
     */
    public static int callPPJPI(long param0, long param1, long param2, long param3, long __functionAddress) {
        DynCall.dcMode(vm, DynCall.DC_CALL_C_DEFAULT);
        DynCall.dcReset(vm);
        DynCall.dcArgPointer(vm, param0);
        DynCall.dcArgPointer(vm, param1);
        DynCall.dcArgLongLong(vm, param2);
        DynCall.dcArgPointer(vm, param3);
        return DynCall.dcCallInt(vm, __functionAddress);
    }
}
