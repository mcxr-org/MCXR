/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.openxr;

public class XrSwapchain extends DispatchableHandle {
    public XrSwapchain(long handle, DispatchableHandle xrInstance) {
        super(handle, xrInstance.getCapabilities());
    }
}
