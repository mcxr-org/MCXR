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
import static org.lwjgl.system.JNI.callPI;
import static org.lwjgl.system.JNI.callPPPI;
import static org.lwjgl.system.MemoryUtil.memAddress;

/** The FB_foveation extension. */
public class FBFoveation {

    /** The extension specification version. */
    public static final int XR_FB_foveation_SPEC_VERSION = 1;

    /** The extension name. */
    public static final String XR_FB_FOVEATION_EXTENSION_NAME = "XR_FB_foveation";

    /** Extends {@code XrObjectType}. */
    public static final int XR_OBJECT_TYPE_FOVEATION_PROFILE_FB = 1000114000;

    /**
     * Extends {@code XrStructureType}.
     * 
     * <h5>Enum values:</h5>
     * 
     * <ul>
     * <li>{@link #XR_TYPE_FOVEATION_PROFILE_CREATE_INFO_FB TYPE_FOVEATION_PROFILE_CREATE_INFO_FB}</li>
     * <li>{@link #XR_TYPE_SWAPCHAIN_CREATE_INFO_FOVEATION_FB TYPE_SWAPCHAIN_CREATE_INFO_FOVEATION_FB}</li>
     * <li>{@link #XR_TYPE_SWAPCHAIN_STATE_FOVEATION_FB TYPE_SWAPCHAIN_STATE_FOVEATION_FB}</li>
     * </ul>
     */
    public static final int
        XR_TYPE_FOVEATION_PROFILE_CREATE_INFO_FB   = 1000114000,
        XR_TYPE_SWAPCHAIN_CREATE_INFO_FOVEATION_FB = 1000114001,
        XR_TYPE_SWAPCHAIN_STATE_FOVEATION_FB       = 1000114002;

    /**
     * XrSwapchainCreateFoveationFlagBitsFB
     * 
     * <h5>Enum values:</h5>
     * 
     * <ul>
     * <li>{@link #XR_SWAPCHAIN_CREATE_FOVEATION_SCALED_BIN_BIT_FB SWAPCHAIN_CREATE_FOVEATION_SCALED_BIN_BIT_FB}</li>
     * <li>{@link #XR_SWAPCHAIN_CREATE_FOVEATION_FRAGMENT_DENSITY_MAP_BIT_FB SWAPCHAIN_CREATE_FOVEATION_FRAGMENT_DENSITY_MAP_BIT_FB}</li>
     * </ul>
     */
    public static final int
        XR_SWAPCHAIN_CREATE_FOVEATION_SCALED_BIN_BIT_FB           = 0x1,
        XR_SWAPCHAIN_CREATE_FOVEATION_FRAGMENT_DENSITY_MAP_BIT_FB = 0x2;

    protected FBFoveation() {
        throw new UnsupportedOperationException();
    }

    // --- [ xrCreateFoveationProfileFB ] ---

    public static int nxrCreateFoveationProfileFB(XrSession session, long createInfo, long profile) {
        long __functionAddress = session.getCapabilities().xrCreateFoveationProfileFB;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPPPI(session.address(), createInfo, profile, __functionAddress);
    }

    @NativeType("XrResult")
    public static int xrCreateFoveationProfileFB(XrSession session, @NativeType("XrFoveationProfileCreateInfoFB const *") XrFoveationProfileCreateInfoFB createInfo, @NativeType("XrFoveationProfileFB *") PointerBuffer profile) {
        if (CHECKS) {
            check(profile, 1);
        }
        return nxrCreateFoveationProfileFB(session, createInfo.address(), memAddress(profile));
    }

    // --- [ xrDestroyFoveationProfileFB ] ---

    @NativeType("XrResult")
    public static int xrDestroyFoveationProfileFB(XrFoveationProfileFB profile) {
        long __functionAddress = profile.getCapabilities().xrDestroyFoveationProfileFB;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPI(profile.address(), __functionAddress);
    }

}