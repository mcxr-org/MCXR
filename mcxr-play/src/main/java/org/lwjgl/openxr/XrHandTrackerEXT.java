/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.openxr;

public class XrHandTrackerEXT extends DispatchableHandle {
    public XrHandTrackerEXT(long handle, DispatchableHandle session) {
        super(handle, session.getCapabilities());
    }

    public XrHandTrackerEXT(long handle, XRCapabilities capabilities) {
        super(handle, capabilities);
    }
}
