/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.openxr;

public class XrDebugUtilsMessengerEXT extends org.lwjgl.openxr.DispatchableHandleSession {
    public XrDebugUtilsMessengerEXT(long handle, org.lwjgl.openxr.DispatchableHandleSession session) {
        super(handle, session.getCapabilities());
    }

    public XrDebugUtilsMessengerEXT(long handle, XRCapabilitiesSession capabilities) {
        super(handle, capabilities);
    }
}
