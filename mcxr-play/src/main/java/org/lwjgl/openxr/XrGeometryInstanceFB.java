/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.openxr;

public class XrGeometryInstanceFB extends DispatchableHandle {
    public XrGeometryInstanceFB(long handle, DispatchableHandle session) {
        super(handle, session.getCapabilities());
    }

    public XrGeometryInstanceFB(long handle, XRCapabilities capabilities) {
        super(handle, capabilities);
    }
}
