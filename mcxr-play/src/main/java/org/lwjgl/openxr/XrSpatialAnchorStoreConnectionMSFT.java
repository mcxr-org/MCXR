/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.openxr;

public class XrSpatialAnchorStoreConnectionMSFT extends DispatchableHandle {
    public XrSpatialAnchorStoreConnectionMSFT(long handle, DispatchableHandle session) {
        super(handle, session.getCapabilities());
    }

    public XrSpatialAnchorStoreConnectionMSFT(long handle, XRCapabilities capabilities) {
        super(handle, capabilities);
    }
}
