/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
package org.lwjgl.openxr;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.NativeType;

import java.nio.IntBuffer;

import static org.lwjgl.system.Checks.*;
import static org.lwjgl.system.JNI.*;
import static org.lwjgl.system.MemoryUtil.memAddress;
import static org.lwjgl.system.MemoryUtil.memAddressSafe;

/** The MSFT_spatial_anchor_persistence extension. */
public class MSFTSpatialAnchorPersistence {

    /** The extension specification version. */
    public static final int XR_MSFT_spatial_anchor_persistence_SPEC_VERSION = 2;

    /** The extension name. */
    public static final String XR_MSFT_SPATIAL_ANCHOR_PERSISTENCE_EXTENSION_NAME = "XR_MSFT_spatial_anchor_persistence";

    /** Extends {@code XrObjectType}. */
    public static final int XR_OBJECT_TYPE_SPATIAL_ANCHOR_STORE_CONNECTION_MSFT = 1000142000;

    /**
     * Extends {@code XrStructureType}.
     * 
     * <h5>Enum values:</h5>
     * 
     * <ul>
     * <li>{@link #XR_TYPE_SPATIAL_ANCHOR_PERSISTENCE_INFO_MSFT TYPE_SPATIAL_ANCHOR_PERSISTENCE_INFO_MSFT}</li>
     * <li>{@link #XR_TYPE_SPATIAL_ANCHOR_FROM_PERSISTED_ANCHOR_CREATE_INFO_MSFT TYPE_SPATIAL_ANCHOR_FROM_PERSISTED_ANCHOR_CREATE_INFO_MSFT}</li>
     * </ul>
     */
    public static final int
        XR_TYPE_SPATIAL_ANCHOR_PERSISTENCE_INFO_MSFT                  = 1000142000,
        XR_TYPE_SPATIAL_ANCHOR_FROM_PERSISTED_ANCHOR_CREATE_INFO_MSFT = 1000142001;

    /**
     * Extends {@code XrResult}.
     * 
     * <h5>Enum values:</h5>
     * 
     * <ul>
     * <li>{@link #XR_ERROR_SPATIAL_ANCHOR_NAME_NOT_FOUND_MSFT ERROR_SPATIAL_ANCHOR_NAME_NOT_FOUND_MSFT}</li>
     * <li>{@link #XR_ERROR_SPATIAL_ANCHOR_NAME_INVALID_MSFT ERROR_SPATIAL_ANCHOR_NAME_INVALID_MSFT}</li>
     * </ul>
     */
    public static final int
        XR_ERROR_SPATIAL_ANCHOR_NAME_NOT_FOUND_MSFT = -1000142001,
        XR_ERROR_SPATIAL_ANCHOR_NAME_INVALID_MSFT   = -1000142002;

    /** XR_MAX_SPATIAL_ANCHOR_NAME_SIZE_MSFT */
    public static final int XR_MAX_SPATIAL_ANCHOR_NAME_SIZE_MSFT = 256;

    protected MSFTSpatialAnchorPersistence() {
        throw new UnsupportedOperationException();
    }

    // --- [ xrCreateSpatialAnchorStoreConnectionMSFT ] ---

    public static int nxrCreateSpatialAnchorStoreConnectionMSFT(XrSession session, long spatialAnchorStore) {
        long __functionAddress = session.getCapabilities().xrCreateSpatialAnchorStoreConnectionMSFT;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPPI(session.address(), spatialAnchorStore, __functionAddress);
    }

    @NativeType("XrResult")
    public static int xrCreateSpatialAnchorStoreConnectionMSFT(XrSession session, @NativeType("XrSpatialAnchorStoreConnectionMSFT *") PointerBuffer spatialAnchorStore) {
        if (CHECKS) {
            check(spatialAnchorStore, 1);
        }
        return nxrCreateSpatialAnchorStoreConnectionMSFT(session, memAddress(spatialAnchorStore));
    }

    // --- [ xrDestroySpatialAnchorStoreConnectionMSFT ] ---

    @NativeType("XrResult")
    public static int xrDestroySpatialAnchorStoreConnectionMSFT(XrSpatialAnchorStoreConnectionMSFT spatialAnchorStore) {
        long __functionAddress = spatialAnchorStore.getCapabilities().xrDestroySpatialAnchorStoreConnectionMSFT;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPI(spatialAnchorStore.address(), __functionAddress);
    }

    // --- [ xrPersistSpatialAnchorMSFT ] ---

    public static int nxrPersistSpatialAnchorMSFT(XrSpatialAnchorStoreConnectionMSFT spatialAnchorStore, long spatialAnchorPersistenceInfo) {
        long __functionAddress = spatialAnchorStore.getCapabilities().xrPersistSpatialAnchorMSFT;
        if (CHECKS) {
            check(__functionAddress);
            XrSpatialAnchorPersistenceInfoMSFT.validate(spatialAnchorPersistenceInfo);
        }
        return callPPI(spatialAnchorStore.address(), spatialAnchorPersistenceInfo, __functionAddress);
    }

    @NativeType("XrResult")
    public static int xrPersistSpatialAnchorMSFT(XrSpatialAnchorStoreConnectionMSFT spatialAnchorStore, @NativeType("XrSpatialAnchorPersistenceInfoMSFT const *") XrSpatialAnchorPersistenceInfoMSFT spatialAnchorPersistenceInfo) {
        return nxrPersistSpatialAnchorMSFT(spatialAnchorStore, spatialAnchorPersistenceInfo.address());
    }

    // --- [ xrEnumeratePersistedSpatialAnchorNamesMSFT ] ---

    public static int nxrEnumeratePersistedSpatialAnchorNamesMSFT(XrSpatialAnchorStoreConnectionMSFT spatialAnchorStore, int spatialAnchorNamesCapacityInput, long spatialAnchorNamesCountOutput, long persistedAnchorNames) {
        long __functionAddress = spatialAnchorStore.getCapabilities().xrEnumeratePersistedSpatialAnchorNamesMSFT;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPPPI(spatialAnchorStore.address(), spatialAnchorNamesCapacityInput, spatialAnchorNamesCountOutput, persistedAnchorNames, __functionAddress);
    }

    @NativeType("XrResult")
    public static int xrEnumeratePersistedSpatialAnchorNamesMSFT(XrSpatialAnchorStoreConnectionMSFT spatialAnchorStore, @Nullable @NativeType("uint32_t *") IntBuffer spatialAnchorNamesCountOutput, @Nullable @NativeType("XrSpatialAnchorPersistenceNameMSFT *") XrSpatialAnchorPersistenceNameMSFT.Buffer persistedAnchorNames) {
        if (CHECKS) {
            checkSafe(spatialAnchorNamesCountOutput, 1);
        }
        return nxrEnumeratePersistedSpatialAnchorNamesMSFT(spatialAnchorStore, remainingSafe(persistedAnchorNames), memAddressSafe(spatialAnchorNamesCountOutput), memAddressSafe(persistedAnchorNames));
    }

    // --- [ xrCreateSpatialAnchorFromPersistedNameMSFT ] ---

    public static int nxrCreateSpatialAnchorFromPersistedNameMSFT(XrSession session, long spatialAnchorCreateInfo, long spatialAnchor) {
        long __functionAddress = session.getCapabilities().xrCreateSpatialAnchorFromPersistedNameMSFT;
        if (CHECKS) {
            check(__functionAddress);
            XrSpatialAnchorFromPersistedAnchorCreateInfoMSFT.validate(spatialAnchorCreateInfo);
        }
        return callPPPI(session.address(), spatialAnchorCreateInfo, spatialAnchor, __functionAddress);
    }

    @NativeType("XrResult")
    public static int xrCreateSpatialAnchorFromPersistedNameMSFT(XrSession session, @NativeType("XrSpatialAnchorFromPersistedAnchorCreateInfoMSFT const *") XrSpatialAnchorFromPersistedAnchorCreateInfoMSFT spatialAnchorCreateInfo, @NativeType("XrSpatialAnchorMSFT *") PointerBuffer spatialAnchor) {
        if (CHECKS) {
            check(spatialAnchor, 1);
        }
        return nxrCreateSpatialAnchorFromPersistedNameMSFT(session, spatialAnchorCreateInfo.address(), memAddress(spatialAnchor));
    }

    // --- [ xrUnpersistSpatialAnchorMSFT ] ---

    public static int nxrUnpersistSpatialAnchorMSFT(XrSpatialAnchorStoreConnectionMSFT spatialAnchorStore, long spatialAnchorPersistenceName) {
        long __functionAddress = spatialAnchorStore.getCapabilities().xrUnpersistSpatialAnchorMSFT;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPPI(spatialAnchorStore.address(), spatialAnchorPersistenceName, __functionAddress);
    }

    @NativeType("XrResult")
    public static int xrUnpersistSpatialAnchorMSFT(XrSpatialAnchorStoreConnectionMSFT spatialAnchorStore, @NativeType("XrSpatialAnchorPersistenceNameMSFT const *") XrSpatialAnchorPersistenceNameMSFT spatialAnchorPersistenceName) {
        return nxrUnpersistSpatialAnchorMSFT(spatialAnchorStore, spatialAnchorPersistenceName.address());
    }

    // --- [ xrClearSpatialAnchorStoreMSFT ] ---

    @NativeType("XrResult")
    public static int xrClearSpatialAnchorStoreMSFT(XrSpatialAnchorStoreConnectionMSFT spatialAnchorStore) {
        long __functionAddress = spatialAnchorStore.getCapabilities().xrClearSpatialAnchorStoreMSFT;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPI(spatialAnchorStore.address(), __functionAddress);
    }

    /** Array version of: {@link #xrEnumeratePersistedSpatialAnchorNamesMSFT EnumeratePersistedSpatialAnchorNamesMSFT} */
//    @NativeType("XrResult")
//    public static int xrEnumeratePersistedSpatialAnchorNamesMSFT(XrSpatialAnchorStoreConnectionMSFT spatialAnchorStore, @Nullable @NativeType("uint32_t *") int[] spatialAnchorNamesCountOutput, @Nullable @NativeType("XrSpatialAnchorPersistenceNameMSFT *") XrSpatialAnchorPersistenceNameMSFT.Buffer persistedAnchorNames) {
//        long __functionAddress = spatialAnchorStore.getCapabilities().xrEnumeratePersistedSpatialAnchorNamesMSFT;
//        if (CHECKS) {
//            check(__functionAddress);
//            checkSafe(spatialAnchorNamesCountOutput, 1);
//        }
//        return callPPPI(spatialAnchorStore.address(), remainingSafe(persistedAnchorNames), spatialAnchorNamesCountOutput, memAddressSafe(persistedAnchorNames), __functionAddress);
//    }

}