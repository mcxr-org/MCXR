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
import static org.lwjgl.system.JNI.*;
import static org.lwjgl.system.MemoryUtil.memAddress;

/** The VARJO_marker_tracking extension. */
public class VARJOMarkerTracking {

    /** The extension specification version. */
    public static final int XR_VARJO_marker_tracking_SPEC_VERSION = 1;

    /** The extension name. */
    public static final String XR_VARJO_MARKER_TRACKING_EXTENSION_NAME = "XR_VARJO_marker_tracking";

    /**
     * Extends {@code XrStructureType}.
     * 
     * <h5>Enum values:</h5>
     * 
     * <ul>
     * <li>{@link #XR_TYPE_SYSTEM_MARKER_TRACKING_PROPERTIES_VARJO TYPE_SYSTEM_MARKER_TRACKING_PROPERTIES_VARJO}</li>
     * <li>{@link #XR_TYPE_EVENT_DATA_MARKER_TRACKING_UPDATE_VARJO TYPE_EVENT_DATA_MARKER_TRACKING_UPDATE_VARJO}</li>
     * <li>{@link #XR_TYPE_MARKER_SPACE_CREATE_INFO_VARJO TYPE_MARKER_SPACE_CREATE_INFO_VARJO}</li>
     * </ul>
     */
    public static final int
        XR_TYPE_SYSTEM_MARKER_TRACKING_PROPERTIES_VARJO = 1000124000,
        XR_TYPE_EVENT_DATA_MARKER_TRACKING_UPDATE_VARJO = 1000124001,
        XR_TYPE_MARKER_SPACE_CREATE_INFO_VARJO          = 1000124002;

    /**
     * Extends {@code XrResult}.
     * 
     * <h5>Enum values:</h5>
     * 
     * <ul>
     * <li>{@link #XR_ERROR_MARKER_NOT_TRACKED_VARJO ERROR_MARKER_NOT_TRACKED_VARJO}</li>
     * <li>{@link #XR_ERROR_MARKER_ID_INVALID_VARJO ERROR_MARKER_ID_INVALID_VARJO}</li>
     * </ul>
     */
    public static final int
        XR_ERROR_MARKER_NOT_TRACKED_VARJO = -1000124000,
        XR_ERROR_MARKER_ID_INVALID_VARJO  = -1000124001;

    protected VARJOMarkerTracking() {
        throw new UnsupportedOperationException();
    }

    // --- [ xrSetMarkerTrackingVARJO ] ---

    @NativeType("XrResult")
    public static int xrSetMarkerTrackingVARJO(XrSession session, @NativeType("XrBool32") boolean enabled) {
        long __functionAddress = session.getCapabilities().xrSetMarkerTrackingVARJO;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPI(session.address(), enabled ? 1 : 0, __functionAddress);
    }

    // --- [ xrSetMarkerTrackingTimeoutVARJO ] ---

//    @NativeType("XrResult")
//    public static int xrSetMarkerTrackingTimeoutVARJO(XrSession session, @NativeType("uint64_t") long markerId, @NativeType("XrDuration") long timeout) {
//        long __functionAddress = session.getCapabilities().xrSetMarkerTrackingTimeoutVARJO;
//        if (CHECKS) {
//            check(__functionAddress);
//        }
//        return callPJJI(session.address(), markerId, timeout, __functionAddress);
//    }

    // --- [ xrSetMarkerTrackingPredictionVARJO ] ---

    @NativeType("XrResult")
    public static int xrSetMarkerTrackingPredictionVARJO(XrSession session, @NativeType("uint64_t") long markerId, @NativeType("XrBool32") boolean enabled) {
        long __functionAddress = session.getCapabilities().xrSetMarkerTrackingPredictionVARJO;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPJI(session.address(), markerId, enabled ? 1 : 0, __functionAddress);
    }

    // --- [ xrGetMarkerSizeVARJO ] ---

    public static int nxrGetMarkerSizeVARJO(XrSession session, long markerId, long size) {
        long __functionAddress = session.getCapabilities().xrGetMarkerSizeVARJO;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPJPI(session.address(), markerId, size, __functionAddress);
    }

    @NativeType("XrResult")
    public static int xrGetMarkerSizeVARJO(XrSession session, @NativeType("uint64_t") long markerId, @NativeType("XrExtent2Df *") XrExtent2Df size) {
        return nxrGetMarkerSizeVARJO(session, markerId, size.address());
    }

    // --- [ xrCreateMarkerSpaceVARJO ] ---

    public static int nxrCreateMarkerSpaceVARJO(XrSession session, long createInfo, long space) {
        long __functionAddress = session.getCapabilities().xrCreateMarkerSpaceVARJO;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPPPI(session.address(), createInfo, space, __functionAddress);
    }

    @NativeType("XrResult")
    public static int xrCreateMarkerSpaceVARJO(XrSession session, @NativeType("XrMarkerSpaceCreateInfoVARJO const *") XrMarkerSpaceCreateInfoVARJO createInfo, @NativeType("XrSpace *") PointerBuffer space) {
        if (CHECKS) {
            check(space, 1);
        }
        return nxrCreateMarkerSpaceVARJO(session, createInfo.address(), memAddress(space));
    }

}