/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
package org.lwjgl.openxr;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.NativeType;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.system.Checks.*;
import static org.lwjgl.system.JNI.callPPI;
import static org.lwjgl.system.JNI.callPPPPI;
import static org.lwjgl.system.MemoryUtil.memAddress;
import static org.lwjgl.system.MemoryUtil.memAddressSafe;

/** The MSFT_scene_understanding_serialization extension. */
public class MSFTSceneUnderstandingSerialization {

    /** The extension specification version. */
    public static final int XR_MSFT_scene_understanding_serialization_SPEC_VERSION = 1;

    /** The extension name. */
    public static final String XR_MSFT_SCENE_UNDERSTANDING_SERIALIZATION_EXTENSION_NAME = "XR_MSFT_scene_understanding_serialization";

    /**
     * Extends {@code XrStructureType}.
     * 
     * <h5>Enum values:</h5>
     * 
     * <ul>
     * <li>{@link #XR_TYPE_SERIALIZED_SCENE_FRAGMENT_DATA_GET_INFO_MSFT TYPE_SERIALIZED_SCENE_FRAGMENT_DATA_GET_INFO_MSFT}</li>
     * <li>{@link #XR_TYPE_SCENE_DESERIALIZE_INFO_MSFT TYPE_SCENE_DESERIALIZE_INFO_MSFT}</li>
     * </ul>
     */
    public static final int
        XR_TYPE_SERIALIZED_SCENE_FRAGMENT_DATA_GET_INFO_MSFT = 1000098000,
        XR_TYPE_SCENE_DESERIALIZE_INFO_MSFT                  = 1000098001;

    /** Extends {@code XrSceneComputeFeatureMSFT}. */
    public static final int XR_SCENE_COMPUTE_FEATURE_SERIALIZE_SCENE_MSFT = 1000098000;

    /** Extends {@code XrSceneComponentTypeMSFT}. */
    public static final int XR_SCENE_COMPONENT_TYPE_SERIALIZED_SCENE_FRAGMENT_MSFT = 1000098000;

    protected MSFTSceneUnderstandingSerialization() {
        throw new UnsupportedOperationException();
    }

    // --- [ xrDeserializeSceneMSFT ] ---

    public static int nxrDeserializeSceneMSFT(XrSceneObserverMSFT sceneObserver, long deserializeInfo) {
        long __functionAddress = sceneObserver.getCapabilities().xrDeserializeSceneMSFT;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPPI(sceneObserver.address(), deserializeInfo, __functionAddress);
    }

    @NativeType("XrResult")
    public static int xrDeserializeSceneMSFT(XrSceneObserverMSFT sceneObserver, @NativeType("XrSceneDeserializeInfoMSFT const *") XrSceneDeserializeInfoMSFT deserializeInfo) {
        return nxrDeserializeSceneMSFT(sceneObserver, deserializeInfo.address());
    }

    // --- [ xrGetSerializedSceneFragmentDataMSFT ] ---

    public static int nxrGetSerializedSceneFragmentDataMSFT(XrSceneMSFT scene, long getInfo, int countInput, long readOutput, long buffer) {
        long __functionAddress = scene.getCapabilities().xrGetSerializedSceneFragmentDataMSFT;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPPPPI(scene.address(), getInfo, countInput, readOutput, buffer, __functionAddress);
    }

    @NativeType("XrResult")
    public static int xrGetSerializedSceneFragmentDataMSFT(XrSceneMSFT scene, @NativeType("XrSerializedSceneFragmentDataGetInfoMSFT const *") XrSerializedSceneFragmentDataGetInfoMSFT getInfo, @NativeType("uint32_t *") IntBuffer readOutput, @Nullable @NativeType("uint8_t *") ByteBuffer buffer) {
        if (CHECKS) {
            check(readOutput, 1);
        }
        return nxrGetSerializedSceneFragmentDataMSFT(scene, getInfo.address(), remainingSafe(buffer), memAddress(readOutput), memAddressSafe(buffer));
    }

    /** Array version of: {@link #xrGetSerializedSceneFragmentDataMSFT GetSerializedSceneFragmentDataMSFT} */
//    @NativeType("XrResult")
//    public static int xrGetSerializedSceneFragmentDataMSFT(XrSceneMSFT scene, @NativeType("XrSerializedSceneFragmentDataGetInfoMSFT const *") XrSerializedSceneFragmentDataGetInfoMSFT getInfo, @NativeType("uint32_t *") int[] readOutput, @Nullable @NativeType("uint8_t *") ByteBuffer buffer) {
//        long __functionAddress = scene.getCapabilities().xrGetSerializedSceneFragmentDataMSFT;
//        if (CHECKS) {
//            check(__functionAddress);
//            check(readOutput, 1);
//        }
//        return callPPPPI(scene.address(), getInfo.address(), remainingSafe(buffer), readOutput, memAddressSafe(buffer), __functionAddress);
//    }

}