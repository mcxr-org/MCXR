/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.openxr;

public class XrTriangleMeshFB extends DispatchableHandle {
    public XrTriangleMeshFB(long handle, DispatchableHandle session) {
        super(handle, session.getCapabilities());
    }

    public XrTriangleMeshFB(long handle, XRCapabilities capabilities) {
        super(handle, capabilities);
    }
}
