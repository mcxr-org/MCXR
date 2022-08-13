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
import static org.lwjgl.system.JNI.callPPPI;
import static org.lwjgl.system.MemoryUtil.memAddress;
import static org.lwjgl.system.MemoryUtil.memAddressSafe;

/** The HTCX_vive_tracker_interaction extension. */
public class HTCXViveTrackerInteraction {

    /** The extension specification version. */
    public static final int XR_HTCX_vive_tracker_interaction_SPEC_VERSION = 1;

    /** The extension name. */
    public static final String XR_HTCX_VIVE_TRACKER_INTERACTION_EXTENSION_NAME = "XR_HTCX_vive_tracker_interaction";

    /**
     * Extends {@code XrStructureType}.
     * 
     * <h5>Enum values:</h5>
     * 
     * <ul>
     * <li>{@link #XR_TYPE_VIVE_TRACKER_PATHS_HTCX TYPE_VIVE_TRACKER_PATHS_HTCX}</li>
     * <li>{@link #XR_TYPE_EVENT_DATA_VIVE_TRACKER_CONNECTED_HTCX TYPE_EVENT_DATA_VIVE_TRACKER_CONNECTED_HTCX}</li>
     * </ul>
     */
    public static final int
        XR_TYPE_VIVE_TRACKER_PATHS_HTCX                = 1000103000,
        XR_TYPE_EVENT_DATA_VIVE_TRACKER_CONNECTED_HTCX = 1000103001;

    protected HTCXViveTrackerInteraction() {
        throw new UnsupportedOperationException();
    }

    // --- [ xrEnumerateViveTrackerPathsHTCX ] ---

    public static int nxrEnumerateViveTrackerPathsHTCX(XrInstance instance, int pathCapacityInput, long pathCountOutput, long paths) {
        long __functionAddress = instance.getCapabilities().xrEnumerateViveTrackerPathsHTCX;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPPPI(instance.address(), pathCapacityInput, pathCountOutput, paths, __functionAddress);
    }

    @NativeType("XrResult")
    public static int xrEnumerateViveTrackerPathsHTCX(XrInstance instance, @NativeType("uint32_t *") IntBuffer pathCountOutput, @Nullable @NativeType("XrViveTrackerPathsHTCX *") XrViveTrackerPathsHTCX.Buffer paths) {
        if (CHECKS) {
            check(pathCountOutput, 1);
        }
        return nxrEnumerateViveTrackerPathsHTCX(instance, remainingSafe(paths), memAddress(pathCountOutput), memAddressSafe(paths));
    }

    /** Array version of: {@link #xrEnumerateViveTrackerPathsHTCX EnumerateViveTrackerPathsHTCX} */
//    @NativeType("XrResult")
//    public static int xrEnumerateViveTrackerPathsHTCX(XrInstance instance, @NativeType("uint32_t *") int[] pathCountOutput, @Nullable @NativeType("XrViveTrackerPathsHTCX *") XrViveTrackerPathsHTCX.Buffer paths) {
//        long __functionAddress = instance.getCapabilities().xrEnumerateViveTrackerPathsHTCX;
//        if (CHECKS) {
//            check(__functionAddress);
//            check(pathCountOutput, 1);
//        }
//        return callPPPI(instance.address(), remainingSafe(paths), pathCountOutput, memAddressSafe(paths), __functionAddress);
//    }

}