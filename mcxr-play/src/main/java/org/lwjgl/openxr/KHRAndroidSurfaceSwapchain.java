/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
package org.lwjgl.openxr;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.NativeType;

import static org.lwjgl.system.Checks.CHECKS;
import static org.lwjgl.system.Checks.check;
import static org.lwjgl.system.JNI.callPPPPI;
import static org.lwjgl.system.MemoryUtil.memAddress;

/** The KHR_android_surface_swapchain extension. */
public class KHRAndroidSurfaceSwapchain {

    /** The extension specification version. */
    public static final int XR_KHR_android_surface_swapchain_SPEC_VERSION = 4;

    /** The extension name. */
    public static final String XR_KHR_ANDROID_SURFACE_SWAPCHAIN_EXTENSION_NAME = "XR_KHR_android_surface_swapchain";

    protected KHRAndroidSurfaceSwapchain() {
        throw new UnsupportedOperationException();
    }

    // --- [ xrCreateSwapchainAndroidSurfaceKHR ] ---

    public static int nxrCreateSwapchainAndroidSurfaceKHR(XrSession session, long info, long swapchain, long surface) {
        long __functionAddress = session.getCapabilities().xrCreateSwapchainAndroidSurfaceKHR;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPPPPI(session.address(), info, swapchain, surface, __functionAddress);
    }

    @NativeType("XrResult")
    public static int xrCreateSwapchainAndroidSurfaceKHR(XrSession session, @NativeType("XrSwapchainCreateInfo const *") XrSwapchainCreateInfo info, @NativeType("XrSwapchain *") PointerBuffer swapchain, @NativeType("jobject *") PointerBuffer surface) {
        if (CHECKS) {
            check(swapchain, 1);
            check(surface, 1);
        }
        return nxrCreateSwapchainAndroidSurfaceKHR(session, info.address(), memAddress(swapchain), memAddress(surface));
    }

}