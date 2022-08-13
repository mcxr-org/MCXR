/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
package org.lwjgl.openxr;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.NativeType;

import static org.lwjgl.system.Checks.CHECKS;
import static org.lwjgl.system.Checks.check;
import static org.lwjgl.system.JNI.callPPI;

/** The FB_hand_tracking_mesh extension. */
public class FBHandTrackingMesh {

    /** The extension specification version. */
    public static final int XR_FB_hand_tracking_mesh_SPEC_VERSION = 1;

    /** The extension name. */
    public static final String XR_FB_HAND_TRACKING_MESH_EXTENSION_NAME = "XR_FB_hand_tracking_mesh";

    /**
     * Extends {@code XrStructureType}.
     * 
     * <h5>Enum values:</h5>
     * 
     * <ul>
     * <li>{@link #XR_TYPE_HAND_TRACKING_MESH_FB TYPE_HAND_TRACKING_MESH_FB}</li>
     * <li>{@link #XR_TYPE_HAND_TRACKING_SCALE_FB TYPE_HAND_TRACKING_SCALE_FB}</li>
     * </ul>
     */
    public static final int
        XR_TYPE_HAND_TRACKING_MESH_FB  = 1000110001,
        XR_TYPE_HAND_TRACKING_SCALE_FB = 1000110003;

    protected FBHandTrackingMesh() {
        throw new UnsupportedOperationException();
    }

    // --- [ xrGetHandMeshFB ] ---

    public static int nxrGetHandMeshFB(XrHandTrackerEXT handTracker, long mesh) {
        long __functionAddress = handTracker.getCapabilities().xrGetHandMeshFB;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPPI(handTracker.address(), mesh, __functionAddress);
    }

    @NativeType("XrResult")
    public static int xrGetHandMeshFB(XrHandTrackerEXT handTracker, @NativeType("XrHandTrackingMeshFB *") XrHandTrackingMeshFB mesh) {
        return nxrGetHandMeshFB(handTracker, mesh.address());
    }

}