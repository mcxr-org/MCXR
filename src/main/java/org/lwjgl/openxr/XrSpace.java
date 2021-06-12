/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.openxr;

public class XrSpace extends DispatchableHandle {
    public XrSpace(long handle, DispatchableHandle xrInstance) {
        super(handle, xrInstance.getCapabilities());
    }
}
