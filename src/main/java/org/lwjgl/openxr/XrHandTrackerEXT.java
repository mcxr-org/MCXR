/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.openxr;

public class XrHandTrackerEXT extends org.lwjgl.openxr.DispatchableHandleSession {
    public XrHandTrackerEXT(long handle, org.lwjgl.openxr.DispatchableHandleSession session) {
        super(handle, session.getCapabilities());
    }

    public XrHandTrackerEXT(long handle, XRCapabilitiesSession capabilities) {
        super(handle, capabilities);
    }
}
