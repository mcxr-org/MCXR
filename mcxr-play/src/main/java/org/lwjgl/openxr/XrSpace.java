/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.openxr;

public class XrSpace extends DispatchableHandle {
    public XrSpace(long handle, DispatchableHandle session) {
        super(handle, session.getCapabilities());
    }

    public XrSpace(long handle, XRCapabilities capabilities) {
        super(handle, capabilities);
    }
}
