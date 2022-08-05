/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.openxr;

public class XrSceneMSFT extends DispatchableHandle {
    public XrSceneMSFT(long handle, DispatchableHandle session) {
        super(handle, session.getCapabilities());
    }

    public XrSceneMSFT(long handle, XRCapabilities capabilities) {
        super(handle, capabilities);
    }
}
