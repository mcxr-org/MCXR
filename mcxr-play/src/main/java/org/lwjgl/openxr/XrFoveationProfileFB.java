/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.openxr;

public class XrFoveationProfileFB extends DispatchableHandle {
    public XrFoveationProfileFB(long handle, DispatchableHandle session) {
        super(handle, session.getCapabilities());
    }

    public XrFoveationProfileFB(long handle, XRCapabilities capabilities) {
        super(handle, capabilities);
    }
}
