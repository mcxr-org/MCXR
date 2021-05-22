/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.openxr;

public class XrSwapchain extends org.lwjgl.openxr.DispatchableHandleSession {
    public XrSwapchain(long handle, org.lwjgl.openxr.DispatchableHandleSession session) {
        super(handle, session.getCapabilities());
    }

    public XrSwapchain(long handle, XRCapabilitiesSession capabilities) {
        super(handle, capabilities);
    }
}
