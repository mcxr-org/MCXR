/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
package org.lwjgl.openxr;

import org.lwjgl.system.Callback;
import org.lwjgl.system.CallbackI;
import org.lwjgl.system.NativeType;

@FunctionalInterface
@NativeType("PFN_xrVoidFunction")
public interface XrVoidFunctionI extends CallbackI.V {

    String SIGNATURE = Callback.__stdcall("()v");

    @Override
    default String getSignature() { return SIGNATURE; }

    @Override
    default void callback(long args) {
        invoke();
    }

    void invoke();

}