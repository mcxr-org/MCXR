/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.openxr;

public class XrActionSet extends DispatchableHandle {
    public XrActionSet(long handle, DispatchableHandle xrInstance) {
        super(handle, xrInstance.getCapabilities());
    }
}
