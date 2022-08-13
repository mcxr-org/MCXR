/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
package org.lwjgl.openxr;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.NativeType;

import java.nio.IntBuffer;

import static org.lwjgl.system.Checks.*;
import static org.lwjgl.system.JNI.callPJPPI;
import static org.lwjgl.system.MemoryUtil.memAddress;
import static org.lwjgl.system.MemoryUtil.memAddressSafe;

/** The MSFT_composition_layer_reprojection extension. */
public class MSFTCompositionLayerReprojection {

    /** The extension specification version. */
    public static final int XR_MSFT_composition_layer_reprojection_SPEC_VERSION = 1;

    /** The extension name. */
    public static final String XR_MSFT_COMPOSITION_LAYER_REPROJECTION_EXTENSION_NAME = "XR_MSFT_composition_layer_reprojection";

    /**
     * Extends {@code XrStructureType}.
     * 
     * <h5>Enum values:</h5>
     * 
     * <ul>
     * <li>{@link #XR_TYPE_COMPOSITION_LAYER_REPROJECTION_INFO_MSFT TYPE_COMPOSITION_LAYER_REPROJECTION_INFO_MSFT}</li>
     * <li>{@link #XR_TYPE_COMPOSITION_LAYER_REPROJECTION_PLANE_OVERRIDE_MSFT TYPE_COMPOSITION_LAYER_REPROJECTION_PLANE_OVERRIDE_MSFT}</li>
     * </ul>
     */
    public static final int
        XR_TYPE_COMPOSITION_LAYER_REPROJECTION_INFO_MSFT           = 1000066000,
        XR_TYPE_COMPOSITION_LAYER_REPROJECTION_PLANE_OVERRIDE_MSFT = 1000066001;

    /** Extends {@code XrResult}. */
    public static final int XR_ERROR_REPROJECTION_MODE_UNSUPPORTED_MSFT = -1000066000;

    /**
     * XrReprojectionModeMSFT
     * 
     * <h5>Enum values:</h5>
     * 
     * <ul>
     * <li>{@link #XR_REPROJECTION_MODE_DEPTH_MSFT REPROJECTION_MODE_DEPTH_MSFT}</li>
     * <li>{@link #XR_REPROJECTION_MODE_PLANAR_FROM_DEPTH_MSFT REPROJECTION_MODE_PLANAR_FROM_DEPTH_MSFT}</li>
     * <li>{@link #XR_REPROJECTION_MODE_PLANAR_MANUAL_MSFT REPROJECTION_MODE_PLANAR_MANUAL_MSFT}</li>
     * <li>{@link #XR_REPROJECTION_MODE_ORIENTATION_ONLY_MSFT REPROJECTION_MODE_ORIENTATION_ONLY_MSFT}</li>
     * </ul>
     */
    public static final int
        XR_REPROJECTION_MODE_DEPTH_MSFT             = 1,
        XR_REPROJECTION_MODE_PLANAR_FROM_DEPTH_MSFT = 2,
        XR_REPROJECTION_MODE_PLANAR_MANUAL_MSFT     = 3,
        XR_REPROJECTION_MODE_ORIENTATION_ONLY_MSFT  = 4;

    protected MSFTCompositionLayerReprojection() {
        throw new UnsupportedOperationException();
    }

    // --- [ xrEnumerateReprojectionModesMSFT ] ---

    public static int nxrEnumerateReprojectionModesMSFT(XrInstance instance, long systemId, int viewConfigurationType, int modeCapacityInput, long modeCountOutput, long modes) {
        long __functionAddress = instance.getCapabilities().xrEnumerateReprojectionModesMSFT;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPJPPI(instance.address(), systemId, viewConfigurationType, modeCapacityInput, modeCountOutput, modes, __functionAddress);
    }

    @NativeType("XrResult")
    public static int xrEnumerateReprojectionModesMSFT(XrInstance instance, @NativeType("XrSystemId") long systemId, @NativeType("XrViewConfigurationType") int viewConfigurationType, @NativeType("uint32_t *") IntBuffer modeCountOutput, @Nullable @NativeType("XrReprojectionModeMSFT *") IntBuffer modes) {
        if (CHECKS) {
            check(modeCountOutput, 1);
        }
        return nxrEnumerateReprojectionModesMSFT(instance, systemId, viewConfigurationType, remainingSafe(modes), memAddress(modeCountOutput), memAddressSafe(modes));
    }

    /** Array version of: {@link #xrEnumerateReprojectionModesMSFT EnumerateReprojectionModesMSFT} */
//    @NativeType("XrResult")
//    public static int xrEnumerateReprojectionModesMSFT(XrInstance instance, @NativeType("XrSystemId") long systemId, @NativeType("XrViewConfigurationType") int viewConfigurationType, @NativeType("uint32_t *") int[] modeCountOutput, @Nullable @NativeType("XrReprojectionModeMSFT *") int[] modes) {
//        long __functionAddress = instance.getCapabilities().xrEnumerateReprojectionModesMSFT;
//        if (CHECKS) {
//            check(__functionAddress);
//            check(modeCountOutput, 1);
//        }
//        return callPJPPI(instance.address(), systemId, viewConfigurationType, lengthSafe(modes), modeCountOutput, modes, __functionAddress);
//    }

}