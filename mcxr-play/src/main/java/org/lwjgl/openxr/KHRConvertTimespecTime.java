/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
package org.lwjgl.openxr;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.NativeType;

import java.nio.LongBuffer;

import static org.lwjgl.system.Checks.CHECKS;
import static org.lwjgl.system.Checks.check;
import static org.lwjgl.system.JNI.callPJPI;
import static org.lwjgl.system.JNI.callPPPI;
import static org.lwjgl.system.MemoryUtil.memAddress;

/** The KHR_convert_timespec_time extension. */
public class KHRConvertTimespecTime {

    /** The extension specification version. */
    public static final int XR_KHR_convert_timespec_time_SPEC_VERSION = 1;

    /** The extension name. */
    public static final String XR_KHR_CONVERT_TIMESPEC_TIME_EXTENSION_NAME = "XR_KHR_convert_timespec_time";

    protected KHRConvertTimespecTime() {
        throw new UnsupportedOperationException();
    }

    // --- [ xrConvertTimespecTimeToTimeKHR ] ---

    public static int nxrConvertTimespecTimeToTimeKHR(XrInstance instance, long timespecTime, long time) {
        long __functionAddress = instance.getCapabilities().xrConvertTimespecTimeToTimeKHR;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPPPI(instance.address(), timespecTime, time, __functionAddress);
    }

//    @NativeType("XrResult")
//    public static int xrConvertTimespecTimeToTimeKHR(XrInstance instance, @NativeType("Timespec *") Timespec.Buffer timespecTime, @NativeType("XrTime *") LongBuffer time) {
//        if (CHECKS) {
//            check(timespecTime, 1);
//            check(time, 1);
//        }
//        return nxrConvertTimespecTimeToTimeKHR(instance, timespecTime.address(), memAddress(time));
//    }
//
//    // --- [ xrConvertTimeToTimespecTimeKHR ] ---
//
//    public static int nxrConvertTimeToTimespecTimeKHR(XrInstance instance, long time, long timespecTime) {
//        long __functionAddress = instance.getCapabilities().xrConvertTimeToTimespecTimeKHR;
//        if (CHECKS) {
//            check(__functionAddress);
//        }
//        return callPJPI(instance.address(), time, timespecTime, __functionAddress);
//    }
//
//    @NativeType("XrResult")
//    public static int xrConvertTimeToTimespecTimeKHR(XrInstance instance, @NativeType("XrTime") long time, @NativeType("Timespec *") Timespec.Buffer timespecTime) {
//        if (CHECKS) {
//            check(timespecTime, 1);
//        }
//        return nxrConvertTimeToTimespecTimeKHR(instance, time, timespecTime.address());
//    }
//
//    /** Array version of: {@link #xrConvertTimespecTimeToTimeKHR ConvertTimespecTimeToTimeKHR} */
//    @NativeType("XrResult")
//    public static int xrConvertTimespecTimeToTimeKHR(XrInstance instance, @NativeType("Timespec *") Timespec.Buffer timespecTime, @NativeType("XrTime *") long[] time) {
//        long __functionAddress = instance.getCapabilities().xrConvertTimespecTimeToTimeKHR;
//        if (CHECKS) {
//            check(__functionAddress);
//            check(timespecTime, 1);
//            check(time, 1);
//        }
//        return callPPPI(instance.address(), timespecTime.address(), time, __functionAddress);
//    }

}