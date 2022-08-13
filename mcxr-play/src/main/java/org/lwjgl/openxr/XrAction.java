/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.openxr;

public class XrAction extends DispatchableHandle {
    public XrAction(long handle, DispatchableHandle session) {
        super(handle, session.getCapabilities());
    }

    public XrAction(long handle, XRCapabilities capabilities) {
        super(handle, capabilities);
    }
}
