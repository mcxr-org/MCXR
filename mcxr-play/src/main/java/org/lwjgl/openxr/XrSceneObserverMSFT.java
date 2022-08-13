/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.openxr;

public class XrSceneObserverMSFT extends DispatchableHandle {
    public XrSceneObserverMSFT(long handle, DispatchableHandle session) {
        super(handle, session.getCapabilities());
    }

    public XrSceneObserverMSFT(long handle, XRCapabilities capabilities) {
        super(handle, capabilities);
    }
}
