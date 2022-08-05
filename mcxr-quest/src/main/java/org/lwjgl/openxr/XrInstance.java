/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.openxr;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.Checks;
import org.lwjgl.system.MemoryStack;

import java.util.HashSet;

import static org.lwjgl.system.APIUtil.apiLog;
import static org.lwjgl.system.JNI.callPPPI;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.*;

public class XrInstance extends DispatchableHandle {
    public XrInstance(long handle, XrInstanceCreateInfo createInfo) {
        super(handle, getInstanceCapabilities(handle, createInfo));
    }

    private static XRCapabilities getInstanceCapabilities(long handle, XrInstanceCreateInfo ci) {
        XrApplicationInfo appInfo = ci.applicationInfo();

        long apiVersion = appInfo.apiVersion();

        return new XRCapabilities(functionName -> {
            try (MemoryStack stack = stackPush()) {
                PointerBuffer pp = stack.mallocPointer(1);
                callPPPI(handle, memAddress(functionName), memAddress(pp), XR.getGlobalCommands().xrGetInstanceProcAddr);
                long address = pp.get();
                if (address == NULL && Checks.DEBUG_FUNCTIONS) {
                    apiLog("Failed to locate address for XR instance function " + memASCII(functionName));
                }
                return address;
            }
        }, apiVersion, XR.getEnabledExtensionSet(apiVersion, ci.enabledExtensionNames()), new HashSet<>());
    }
}