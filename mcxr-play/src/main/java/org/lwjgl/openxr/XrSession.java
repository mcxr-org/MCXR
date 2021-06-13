/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.openxr;

public class XrSession extends DispatchableHandle {
    public XrSession(long handle, DispatchableHandle xrInstance) {
        super(handle, xrInstance.getCapabilities());
    }
}
