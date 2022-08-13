/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.openxr;

public class XrSpatialAnchorMSFT extends DispatchableHandle {
    public XrSpatialAnchorMSFT(long handle, DispatchableHandle session) {
        super(handle, session.getCapabilities());
    }

    public XrSpatialAnchorMSFT(long handle, XRCapabilities capabilities) {
        super(handle, capabilities);
    }
}
