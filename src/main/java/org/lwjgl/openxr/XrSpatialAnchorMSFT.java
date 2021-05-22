/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.openxr;

public class XrSpatialAnchorMSFT extends org.lwjgl.openxr.DispatchableHandleSession {
    public XrSpatialAnchorMSFT(long handle, org.lwjgl.openxr.DispatchableHandleSession session) {
        super(handle, session.getCapabilities());
    }

    public XrSpatialAnchorMSFT(long handle, XRCapabilitiesSession capabilities) {
        super(handle, capabilities);
    }
}
