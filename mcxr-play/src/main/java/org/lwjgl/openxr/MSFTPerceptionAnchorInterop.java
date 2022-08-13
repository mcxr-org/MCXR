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
import static org.lwjgl.system.JNI.callPPPI;
import static org.lwjgl.system.MemoryUtil.memAddress;

/** The MSFT_perception_anchor_interop extension. */
public class MSFTPerceptionAnchorInterop {

    /** The extension specification version. */
    public static final int XR_MSFT_perception_anchor_interop_SPEC_VERSION = 1;

    /** The extension name. */
    public static final String XR_MSFT_PERCEPTION_ANCHOR_INTEROP_EXTENSION_NAME = "XR_MSFT_perception_anchor_interop";

    protected MSFTPerceptionAnchorInterop() {
        throw new UnsupportedOperationException();
    }

    // --- [ xrCreateSpatialAnchorFromPerceptionAnchorMSFT ] ---

    public static int nxrCreateSpatialAnchorFromPerceptionAnchorMSFT(XrSession session, long perceptionAnchor, long anchor) {
        long __functionAddress = session.getCapabilities().xrCreateSpatialAnchorFromPerceptionAnchorMSFT;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPPPI(session.address(), perceptionAnchor, anchor, __functionAddress);
    }

    @NativeType("XrResult")
    public static int xrCreateSpatialAnchorFromPerceptionAnchorMSFT(XrSession session, @NativeType("IUnknown *") PointerBuffer perceptionAnchor, @NativeType("XrSpatialAnchorMSFT *") PointerBuffer anchor) {
        if (CHECKS) {
            check(perceptionAnchor, 1);
            check(anchor, 1);
        }
        return nxrCreateSpatialAnchorFromPerceptionAnchorMSFT(session, memAddress(perceptionAnchor), memAddress(anchor));
    }

    // --- [ xrTryGetPerceptionAnchorFromSpatialAnchorMSFT ] ---

    public static int nxrTryGetPerceptionAnchorFromSpatialAnchorMSFT(XrSession session, XrSpatialAnchorMSFT anchor, long perceptionAnchor) {
        long __functionAddress = session.getCapabilities().xrTryGetPerceptionAnchorFromSpatialAnchorMSFT;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPPPI(session.address(), anchor.address(), perceptionAnchor, __functionAddress);
    }

    @NativeType("XrResult")
    public static int xrTryGetPerceptionAnchorFromSpatialAnchorMSFT(XrSession session, XrSpatialAnchorMSFT anchor, @NativeType("IUnknown **") PointerBuffer perceptionAnchor) {
        if (CHECKS) {
            check(perceptionAnchor, 1);
        }
        return nxrTryGetPerceptionAnchorFromSpatialAnchorMSFT(session, anchor, memAddress(perceptionAnchor));
    }

}