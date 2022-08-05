/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.openxr;

public class XrPassthroughLayerFB extends DispatchableHandle {
    public XrPassthroughLayerFB(long handle, DispatchableHandle session) {
        super(handle, session.getCapabilities());
    }

    public XrPassthroughLayerFB(long handle, XRCapabilities capabilities) {
        super(handle, capabilities);
    }
}
