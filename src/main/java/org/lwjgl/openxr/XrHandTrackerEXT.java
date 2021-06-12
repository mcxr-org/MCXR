/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.openxr;

public class XrHandTrackerEXT extends DispatchableHandle {
    public XrHandTrackerEXT(long handle, XrInstance xrInstance) {
        super(handle, xrInstance.getCapabilities());
    }
}
