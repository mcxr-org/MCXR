/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
package org.lwjgl.openxr;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.NativeType;

import java.nio.IntBuffer;

import static org.lwjgl.system.Checks.CHECKS;
import static org.lwjgl.system.Checks.check;
import static org.lwjgl.system.JNI.callPPI;
import static org.lwjgl.system.MemoryUtil.memAddress;

/** The OCULUS_audio_device_guid extension. */
public class OCULUSAudioDeviceGuid {

    /** The extension specification version. */
    public static final int XR_OCULUS_audio_device_guid_SPEC_VERSION = 1;

    /** The extension name. */
    public static final String XR_OCULUS_AUDIO_DEVICE_GUID_EXTENSION_NAME = "XR_OCULUS_audio_device_guid";

    /** XR_MAX_AUDIO_DEVICE_STR_SIZE_OCULUS */
    public static final int XR_MAX_AUDIO_DEVICE_STR_SIZE_OCULUS = 128;

    protected OCULUSAudioDeviceGuid() {
        throw new UnsupportedOperationException();
    }

    // --- [ xrGetAudioOutputDeviceGuidOculus ] ---

    public static int nxrGetAudioOutputDeviceGuidOculus(XrInstance instance, long buffer) {
        long __functionAddress = instance.getCapabilities().xrGetAudioOutputDeviceGuidOculus;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPPI(instance.address(), buffer, __functionAddress);
    }

    @NativeType("XrResult")
    public static int xrGetAudioOutputDeviceGuidOculus(XrInstance instance, @NativeType("wchar *") IntBuffer buffer) {
        if (CHECKS) {
            check(buffer, XR_MAX_AUDIO_DEVICE_STR_SIZE_OCULUS);
        }
        return nxrGetAudioOutputDeviceGuidOculus(instance, memAddress(buffer));
    }

    // --- [ xrGetAudioInputDeviceGuidOculus ] ---

    public static int nxrGetAudioInputDeviceGuidOculus(XrInstance instance, long buffer) {
        long __functionAddress = instance.getCapabilities().xrGetAudioInputDeviceGuidOculus;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPPI(instance.address(), buffer, __functionAddress);
    }

    @NativeType("XrResult")
    public static int xrGetAudioInputDeviceGuidOculus(XrInstance instance, @NativeType("wchar *") IntBuffer buffer) {
        if (CHECKS) {
            check(buffer, XR_MAX_AUDIO_DEVICE_STR_SIZE_OCULUS);
        }
        return nxrGetAudioInputDeviceGuidOculus(instance, memAddress(buffer));
    }

    /** Array version of: {@link #xrGetAudioOutputDeviceGuidOculus GetAudioOutputDeviceGuidOculus} */
    @NativeType("XrResult")
    public static int xrGetAudioOutputDeviceGuidOculus(XrInstance instance, @NativeType("wchar *") int[] buffer) {
        long __functionAddress = instance.getCapabilities().xrGetAudioOutputDeviceGuidOculus;
        if (CHECKS) {
            check(__functionAddress);
            check(buffer, XR_MAX_AUDIO_DEVICE_STR_SIZE_OCULUS);
        }
        return callPPI(instance.address(), buffer, __functionAddress);
    }

    /** Array version of: {@link #xrGetAudioInputDeviceGuidOculus GetAudioInputDeviceGuidOculus} */
    @NativeType("XrResult")
    public static int xrGetAudioInputDeviceGuidOculus(XrInstance instance, @NativeType("wchar *") int[] buffer) {
        long __functionAddress = instance.getCapabilities().xrGetAudioInputDeviceGuidOculus;
        if (CHECKS) {
            check(__functionAddress);
            check(buffer, XR_MAX_AUDIO_DEVICE_STR_SIZE_OCULUS);
        }
        return callPPI(instance.address(), buffer, __functionAddress);
    }

}