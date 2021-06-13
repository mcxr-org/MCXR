/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.openxr;

public class XrDebugUtilsMessengerEXT extends DispatchableHandle {
    public XrDebugUtilsMessengerEXT(long handle, DispatchableHandle xrInstance) {
        super(handle, xrInstance.getCapabilities());
    }
}
