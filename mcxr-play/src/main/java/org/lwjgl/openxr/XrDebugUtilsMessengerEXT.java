/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.openxr;

public class XrDebugUtilsMessengerEXT extends DispatchableHandle {
    public XrDebugUtilsMessengerEXT(long handle, DispatchableHandle session) {
        super(handle, session.getCapabilities());
    }

    public XrDebugUtilsMessengerEXT(long handle, XRCapabilities capabilities) {
        super(handle, capabilities);
    }
}
