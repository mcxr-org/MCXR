/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.openxr;

public class XrPassthroughFB extends DispatchableHandle {
    public XrPassthroughFB(long handle, DispatchableHandle session) {
        super(handle, session.getCapabilities());
    }

    public XrPassthroughFB(long handle, XRCapabilities capabilities) {
        super(handle, capabilities);
    }
}
