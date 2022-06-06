/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.openxr;

public class XrSwapchain extends DispatchableHandle {
    public XrSwapchain(long handle, DispatchableHandle session) {
        super(handle, session.getCapabilities());
    }

    public XrSwapchain(long handle, XRCapabilities capabilities) {
        super(handle, capabilities);
    }
}
