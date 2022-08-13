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

/** The MSFT_scene_understanding extension. */
public class MSFTSceneUnderstanding {

    /** The extension specification version. */
    public static final int XR_MSFT_scene_understanding_SPEC_VERSION = 1;

    /** The extension name. */
    public static final String XR_MSFT_SCENE_UNDERSTANDING_EXTENSION_NAME = "XR_MSFT_scene_understanding";

    /**
     * Extends {@code XrObjectType}.
     * 
     * <h5>Enum values:</h5>
     * 
     * <ul>
     * <li>{@link #XR_OBJECT_TYPE_SCENE_OBSERVER_MSFT OBJECT_TYPE_SCENE_OBSERVER_MSFT}</li>
     * <li>{@link #XR_OBJECT_TYPE_SCENE_MSFT OBJECT_TYPE_SCENE_MSFT}</li>
     * </ul>
     */
    public static final int
        XR_OBJECT_TYPE_SCENE_OBSERVER_MSFT = 1000097000,
        XR_OBJECT_TYPE_SCENE_MSFT          = 1000097001;

    /**
     * Extends {@code XrStructureType}.
     * 
     * <h5>Enum values:</h5>
     * 
     * <ul>
     * <li>{@link #XR_TYPE_SCENE_OBSERVER_CREATE_INFO_MSFT TYPE_SCENE_OBSERVER_CREATE_INFO_MSFT}</li>
     * <li>{@link #XR_TYPE_SCENE_CREATE_INFO_MSFT TYPE_SCENE_CREATE_INFO_MSFT}</li>
     * <li>{@link #XR_TYPE_NEW_SCENE_COMPUTE_INFO_MSFT TYPE_NEW_SCENE_COMPUTE_INFO_MSFT}</li>
     * <li>{@link #XR_TYPE_VISUAL_MESH_COMPUTE_LOD_INFO_MSFT TYPE_VISUAL_MESH_COMPUTE_LOD_INFO_MSFT}</li>
     * <li>{@link #XR_TYPE_SCENE_COMPONENTS_MSFT TYPE_SCENE_COMPONENTS_MSFT}</li>
     * <li>{@link #XR_TYPE_SCENE_COMPONENTS_GET_INFO_MSFT TYPE_SCENE_COMPONENTS_GET_INFO_MSFT}</li>
     * <li>{@link #XR_TYPE_SCENE_COMPONENT_LOCATIONS_MSFT TYPE_SCENE_COMPONENT_LOCATIONS_MSFT}</li>
     * <li>{@link #XR_TYPE_SCENE_COMPONENTS_LOCATE_INFO_MSFT TYPE_SCENE_COMPONENTS_LOCATE_INFO_MSFT}</li>
     * <li>{@link #XR_TYPE_SCENE_OBJECTS_MSFT TYPE_SCENE_OBJECTS_MSFT}</li>
     * <li>{@link #XR_TYPE_SCENE_COMPONENT_PARENT_FILTER_INFO_MSFT TYPE_SCENE_COMPONENT_PARENT_FILTER_INFO_MSFT}</li>
     * <li>{@link #XR_TYPE_SCENE_OBJECT_TYPES_FILTER_INFO_MSFT TYPE_SCENE_OBJECT_TYPES_FILTER_INFO_MSFT}</li>
     * <li>{@link #XR_TYPE_SCENE_PLANES_MSFT TYPE_SCENE_PLANES_MSFT}</li>
     * <li>{@link #XR_TYPE_SCENE_PLANE_ALIGNMENT_FILTER_INFO_MSFT TYPE_SCENE_PLANE_ALIGNMENT_FILTER_INFO_MSFT}</li>
     * <li>{@link #XR_TYPE_SCENE_MESHES_MSFT TYPE_SCENE_MESHES_MSFT}</li>
     * <li>{@link #XR_TYPE_SCENE_MESH_BUFFERS_GET_INFO_MSFT TYPE_SCENE_MESH_BUFFERS_GET_INFO_MSFT}</li>
     * <li>{@link #XR_TYPE_SCENE_MESH_BUFFERS_MSFT TYPE_SCENE_MESH_BUFFERS_MSFT}</li>
     * <li>{@link #XR_TYPE_SCENE_MESH_VERTEX_BUFFER_MSFT TYPE_SCENE_MESH_VERTEX_BUFFER_MSFT}</li>
     * <li>{@link #XR_TYPE_SCENE_MESH_INDICES_UINT32_MSFT TYPE_SCENE_MESH_INDICES_UINT32_MSFT}</li>
     * <li>{@link #XR_TYPE_SCENE_MESH_INDICES_UINT16_MSFT TYPE_SCENE_MESH_INDICES_UINT16_MSFT}</li>
     * </ul>
     */
    public static final int
        XR_TYPE_SCENE_OBSERVER_CREATE_INFO_MSFT         = 1000097000,
        XR_TYPE_SCENE_CREATE_INFO_MSFT                  = 1000097001,
        XR_TYPE_NEW_SCENE_COMPUTE_INFO_MSFT             = 1000097002,
        XR_TYPE_VISUAL_MESH_COMPUTE_LOD_INFO_MSFT       = 1000097003,
        XR_TYPE_SCENE_COMPONENTS_MSFT                   = 1000097004,
        XR_TYPE_SCENE_COMPONENTS_GET_INFO_MSFT          = 1000097005,
        XR_TYPE_SCENE_COMPONENT_LOCATIONS_MSFT          = 1000097006,
        XR_TYPE_SCENE_COMPONENTS_LOCATE_INFO_MSFT       = 1000097007,
        XR_TYPE_SCENE_OBJECTS_MSFT                      = 1000097008,
        XR_TYPE_SCENE_COMPONENT_PARENT_FILTER_INFO_MSFT = 1000097009,
        XR_TYPE_SCENE_OBJECT_TYPES_FILTER_INFO_MSFT     = 1000097010,
        XR_TYPE_SCENE_PLANES_MSFT                       = 1000097011,
        XR_TYPE_SCENE_PLANE_ALIGNMENT_FILTER_INFO_MSFT  = 1000097012,
        XR_TYPE_SCENE_MESHES_MSFT                       = 1000097013,
        XR_TYPE_SCENE_MESH_BUFFERS_GET_INFO_MSFT        = 1000097014,
        XR_TYPE_SCENE_MESH_BUFFERS_MSFT                 = 1000097015,
        XR_TYPE_SCENE_MESH_VERTEX_BUFFER_MSFT           = 1000097016,
        XR_TYPE_SCENE_MESH_INDICES_UINT32_MSFT          = 1000097017,
        XR_TYPE_SCENE_MESH_INDICES_UINT16_MSFT          = 1000097018;

    /**
     * Extends {@code XrResult}.
     * 
     * <h5>Enum values:</h5>
     * 
     * <ul>
     * <li>{@link #XR_ERROR_COMPUTE_NEW_SCENE_NOT_COMPLETED_MSFT ERROR_COMPUTE_NEW_SCENE_NOT_COMPLETED_MSFT}</li>
     * <li>{@link #XR_ERROR_SCENE_COMPONENT_ID_INVALID_MSFT ERROR_SCENE_COMPONENT_ID_INVALID_MSFT}</li>
     * <li>{@link #XR_ERROR_SCENE_COMPONENT_TYPE_MISMATCH_MSFT ERROR_SCENE_COMPONENT_TYPE_MISMATCH_MSFT}</li>
     * <li>{@link #XR_ERROR_SCENE_MESH_BUFFER_ID_INVALID_MSFT ERROR_SCENE_MESH_BUFFER_ID_INVALID_MSFT}</li>
     * <li>{@link #XR_ERROR_SCENE_COMPUTE_FEATURE_INCOMPATIBLE_MSFT ERROR_SCENE_COMPUTE_FEATURE_INCOMPATIBLE_MSFT}</li>
     * <li>{@link #XR_ERROR_SCENE_COMPUTE_CONSISTENCY_MISMATCH_MSFT ERROR_SCENE_COMPUTE_CONSISTENCY_MISMATCH_MSFT}</li>
     * </ul>
     */
    public static final int
        XR_ERROR_COMPUTE_NEW_SCENE_NOT_COMPLETED_MSFT    = -1000097000,
        XR_ERROR_SCENE_COMPONENT_ID_INVALID_MSFT         = -1000097001,
        XR_ERROR_SCENE_COMPONENT_TYPE_MISMATCH_MSFT      = -1000097002,
        XR_ERROR_SCENE_MESH_BUFFER_ID_INVALID_MSFT       = -1000097003,
        XR_ERROR_SCENE_COMPUTE_FEATURE_INCOMPATIBLE_MSFT = -1000097004,
        XR_ERROR_SCENE_COMPUTE_CONSISTENCY_MISMATCH_MSFT = -1000097005;

    /**
     * XrSceneComputeFeatureMSFT
     * 
     * <h5>Enum values:</h5>
     * 
     * <ul>
     * <li>{@link #XR_SCENE_COMPUTE_FEATURE_PLANE_MSFT SCENE_COMPUTE_FEATURE_PLANE_MSFT}</li>
     * <li>{@link #XR_SCENE_COMPUTE_FEATURE_PLANE_MESH_MSFT SCENE_COMPUTE_FEATURE_PLANE_MESH_MSFT}</li>
     * <li>{@link #XR_SCENE_COMPUTE_FEATURE_VISUAL_MESH_MSFT SCENE_COMPUTE_FEATURE_VISUAL_MESH_MSFT}</li>
     * <li>{@link #XR_SCENE_COMPUTE_FEATURE_COLLIDER_MESH_MSFT SCENE_COMPUTE_FEATURE_COLLIDER_MESH_MSFT}</li>
     * </ul>
     */
    public static final int
        XR_SCENE_COMPUTE_FEATURE_PLANE_MSFT         = 1,
        XR_SCENE_COMPUTE_FEATURE_PLANE_MESH_MSFT    = 2,
        XR_SCENE_COMPUTE_FEATURE_VISUAL_MESH_MSFT   = 3,
        XR_SCENE_COMPUTE_FEATURE_COLLIDER_MESH_MSFT = 4;

    /**
     * XrSceneComputeConsistencyMSFT
     * 
     * <h5>Enum values:</h5>
     * 
     * <ul>
     * <li>{@link #XR_SCENE_COMPUTE_CONSISTENCY_SNAPSHOT_COMPLETE_MSFT SCENE_COMPUTE_CONSISTENCY_SNAPSHOT_COMPLETE_MSFT}</li>
     * <li>{@link #XR_SCENE_COMPUTE_CONSISTENCY_SNAPSHOT_INCOMPLETE_FAST_MSFT SCENE_COMPUTE_CONSISTENCY_SNAPSHOT_INCOMPLETE_FAST_MSFT}</li>
     * <li>{@link #XR_SCENE_COMPUTE_CONSISTENCY_OCCLUSION_OPTIMIZED_MSFT SCENE_COMPUTE_CONSISTENCY_OCCLUSION_OPTIMIZED_MSFT}</li>
     * </ul>
     */
    public static final int
        XR_SCENE_COMPUTE_CONSISTENCY_SNAPSHOT_COMPLETE_MSFT        = 1,
        XR_SCENE_COMPUTE_CONSISTENCY_SNAPSHOT_INCOMPLETE_FAST_MSFT = 2,
        XR_SCENE_COMPUTE_CONSISTENCY_OCCLUSION_OPTIMIZED_MSFT      = 3;

    /**
     * XrMeshComputeLodMSFT
     * 
     * <h5>Enum values:</h5>
     * 
     * <ul>
     * <li>{@link #XR_MESH_COMPUTE_LOD_COARSE_MSFT MESH_COMPUTE_LOD_COARSE_MSFT}</li>
     * <li>{@link #XR_MESH_COMPUTE_LOD_MEDIUM_MSFT MESH_COMPUTE_LOD_MEDIUM_MSFT}</li>
     * <li>{@link #XR_MESH_COMPUTE_LOD_FINE_MSFT MESH_COMPUTE_LOD_FINE_MSFT}</li>
     * <li>{@link #XR_MESH_COMPUTE_LOD_UNLIMITED_MSFT MESH_COMPUTE_LOD_UNLIMITED_MSFT}</li>
     * </ul>
     */
    public static final int
        XR_MESH_COMPUTE_LOD_COARSE_MSFT    = 1,
        XR_MESH_COMPUTE_LOD_MEDIUM_MSFT    = 2,
        XR_MESH_COMPUTE_LOD_FINE_MSFT      = 3,
        XR_MESH_COMPUTE_LOD_UNLIMITED_MSFT = 4;

    /**
     * XrSceneComponentTypeMSFT
     * 
     * <h5>Enum values:</h5>
     * 
     * <ul>
     * <li>{@link #XR_SCENE_COMPONENT_TYPE_INVALID_MSFT SCENE_COMPONENT_TYPE_INVALID_MSFT}</li>
     * <li>{@link #XR_SCENE_COMPONENT_TYPE_OBJECT_MSFT SCENE_COMPONENT_TYPE_OBJECT_MSFT}</li>
     * <li>{@link #XR_SCENE_COMPONENT_TYPE_PLANE_MSFT SCENE_COMPONENT_TYPE_PLANE_MSFT}</li>
     * <li>{@link #XR_SCENE_COMPONENT_TYPE_VISUAL_MESH_MSFT SCENE_COMPONENT_TYPE_VISUAL_MESH_MSFT}</li>
     * <li>{@link #XR_SCENE_COMPONENT_TYPE_COLLIDER_MESH_MSFT SCENE_COMPONENT_TYPE_COLLIDER_MESH_MSFT}</li>
     * </ul>
     */
    public static final int
        XR_SCENE_COMPONENT_TYPE_INVALID_MSFT       = -1,
        XR_SCENE_COMPONENT_TYPE_OBJECT_MSFT        = 1,
        XR_SCENE_COMPONENT_TYPE_PLANE_MSFT         = 2,
        XR_SCENE_COMPONENT_TYPE_VISUAL_MESH_MSFT   = 3,
        XR_SCENE_COMPONENT_TYPE_COLLIDER_MESH_MSFT = 4;

    /**
     * XrSceneObjectTypeMSFT
     * 
     * <h5>Enum values:</h5>
     * 
     * <ul>
     * <li>{@link #XR_SCENE_OBJECT_TYPE_UNCATEGORIZED_MSFT SCENE_OBJECT_TYPE_UNCATEGORIZED_MSFT}</li>
     * <li>{@link #XR_SCENE_OBJECT_TYPE_BACKGROUND_MSFT SCENE_OBJECT_TYPE_BACKGROUND_MSFT}</li>
     * <li>{@link #XR_SCENE_OBJECT_TYPE_WALL_MSFT SCENE_OBJECT_TYPE_WALL_MSFT}</li>
     * <li>{@link #XR_SCENE_OBJECT_TYPE_FLOOR_MSFT SCENE_OBJECT_TYPE_FLOOR_MSFT}</li>
     * <li>{@link #XR_SCENE_OBJECT_TYPE_CEILING_MSFT SCENE_OBJECT_TYPE_CEILING_MSFT}</li>
     * <li>{@link #XR_SCENE_OBJECT_TYPE_PLATFORM_MSFT SCENE_OBJECT_TYPE_PLATFORM_MSFT}</li>
     * <li>{@link #XR_SCENE_OBJECT_TYPE_INFERRED_MSFT SCENE_OBJECT_TYPE_INFERRED_MSFT}</li>
     * </ul>
     */
    public static final int
        XR_SCENE_OBJECT_TYPE_UNCATEGORIZED_MSFT = -1,
        XR_SCENE_OBJECT_TYPE_BACKGROUND_MSFT    = 1,
        XR_SCENE_OBJECT_TYPE_WALL_MSFT          = 2,
        XR_SCENE_OBJECT_TYPE_FLOOR_MSFT         = 3,
        XR_SCENE_OBJECT_TYPE_CEILING_MSFT       = 4,
        XR_SCENE_OBJECT_TYPE_PLATFORM_MSFT      = 5,
        XR_SCENE_OBJECT_TYPE_INFERRED_MSFT      = 6;

    /**
     * XrScenePlaneAlignmentTypeMSFT
     * 
     * <h5>Enum values:</h5>
     * 
     * <ul>
     * <li>{@link #XR_SCENE_PLANE_ALIGNMENT_TYPE_NON_ORTHOGONAL_MSFT SCENE_PLANE_ALIGNMENT_TYPE_NON_ORTHOGONAL_MSFT}</li>
     * <li>{@link #XR_SCENE_PLANE_ALIGNMENT_TYPE_HORIZONTAL_MSFT SCENE_PLANE_ALIGNMENT_TYPE_HORIZONTAL_MSFT}</li>
     * <li>{@link #XR_SCENE_PLANE_ALIGNMENT_TYPE_VERTICAL_MSFT SCENE_PLANE_ALIGNMENT_TYPE_VERTICAL_MSFT}</li>
     * </ul>
     */
    public static final int
        XR_SCENE_PLANE_ALIGNMENT_TYPE_NON_ORTHOGONAL_MSFT = 0,
        XR_SCENE_PLANE_ALIGNMENT_TYPE_HORIZONTAL_MSFT     = 1,
        XR_SCENE_PLANE_ALIGNMENT_TYPE_VERTICAL_MSFT       = 2;

    /**
     * XrSceneComputeStateMSFT
     * 
     * <h5>Enum values:</h5>
     * 
     * <ul>
     * <li>{@link #XR_SCENE_COMPUTE_STATE_NONE_MSFT SCENE_COMPUTE_STATE_NONE_MSFT}</li>
     * <li>{@link #XR_SCENE_COMPUTE_STATE_UPDATING_MSFT SCENE_COMPUTE_STATE_UPDATING_MSFT}</li>
     * <li>{@link #XR_SCENE_COMPUTE_STATE_COMPLETED_MSFT SCENE_COMPUTE_STATE_COMPLETED_MSFT}</li>
     * <li>{@link #XR_SCENE_COMPUTE_STATE_COMPLETED_WITH_ERROR_MSFT SCENE_COMPUTE_STATE_COMPLETED_WITH_ERROR_MSFT}</li>
     * </ul>
     */
    public static final int
        XR_SCENE_COMPUTE_STATE_NONE_MSFT                 = 0,
        XR_SCENE_COMPUTE_STATE_UPDATING_MSFT             = 1,
        XR_SCENE_COMPUTE_STATE_COMPLETED_MSFT            = 2,
        XR_SCENE_COMPUTE_STATE_COMPLETED_WITH_ERROR_MSFT = 3;

    protected MSFTSceneUnderstanding() {
        throw new UnsupportedOperationException();
    }

    // --- [ xrEnumerateSceneComputeFeaturesMSFT ] ---

    public static int nxrEnumerateSceneComputeFeaturesMSFT(XrInstance instance, long systemId, int featureCapacityInput, long featureCountOutput, long features) {
        long __functionAddress = instance.getCapabilities().xrEnumerateSceneComputeFeaturesMSFT;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPJPPI(instance.address(), systemId, featureCapacityInput, featureCountOutput, features, __functionAddress);
    }

    @NativeType("XrResult")
    public static int xrEnumerateSceneComputeFeaturesMSFT(XrInstance instance, @NativeType("XrSystemId") long systemId, @NativeType("uint32_t *") IntBuffer featureCountOutput, @Nullable @NativeType("XrSceneComputeFeatureMSFT *") IntBuffer features) {
        if (CHECKS) {
            check(featureCountOutput, 1);
        }
        return nxrEnumerateSceneComputeFeaturesMSFT(instance, systemId, remainingSafe(features), memAddress(featureCountOutput), memAddressSafe(features));
    }

    // --- [ xrCreateSceneObserverMSFT ] ---

    public static int nxrCreateSceneObserverMSFT(XrSession session, long createInfo, long sceneObserver) {
        long __functionAddress = session.getCapabilities().xrCreateSceneObserverMSFT;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPPPI(session.address(), createInfo, sceneObserver, __functionAddress);
    }

    @NativeType("XrResult")
    public static int xrCreateSceneObserverMSFT(XrSession session, @Nullable @NativeType("XrSceneObserverCreateInfoMSFT const *") XrSceneObserverCreateInfoMSFT createInfo, @NativeType("XrSceneObserverMSFT *") PointerBuffer sceneObserver) {
        if (CHECKS) {
            check(sceneObserver, 1);
        }
        return nxrCreateSceneObserverMSFT(session, memAddressSafe(createInfo), memAddress(sceneObserver));
    }

    // --- [ xrDestroySceneObserverMSFT ] ---

    @NativeType("XrResult")
    public static int xrDestroySceneObserverMSFT(XrSceneObserverMSFT sceneObserver) {
        long __functionAddress = sceneObserver.getCapabilities().xrDestroySceneObserverMSFT;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPI(sceneObserver.address(), __functionAddress);
    }

    // --- [ xrCreateSceneMSFT ] ---

    public static int nxrCreateSceneMSFT(XrSceneObserverMSFT sceneObserver, long createInfo, long scene) {
        long __functionAddress = sceneObserver.getCapabilities().xrCreateSceneMSFT;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPPPI(sceneObserver.address(), createInfo, scene, __functionAddress);
    }

    @NativeType("XrResult")
    public static int xrCreateSceneMSFT(XrSceneObserverMSFT sceneObserver, @Nullable @NativeType("XrSceneCreateInfoMSFT const *") XrSceneCreateInfoMSFT createInfo, @NativeType("XrSceneMSFT *") PointerBuffer scene) {
        if (CHECKS) {
            check(scene, 1);
        }
        return nxrCreateSceneMSFT(sceneObserver, memAddressSafe(createInfo), memAddress(scene));
    }

    // --- [ xrDestroySceneMSFT ] ---

    @NativeType("XrResult")
    public static int xrDestroySceneMSFT(XrSceneMSFT scene) {
        long __functionAddress = scene.getCapabilities().xrDestroySceneMSFT;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPI(scene.address(), __functionAddress);
    }

    // --- [ xrComputeNewSceneMSFT ] ---

    public static int nxrComputeNewSceneMSFT(XrSceneObserverMSFT sceneObserver, long computeInfo) {
        long __functionAddress = sceneObserver.getCapabilities().xrComputeNewSceneMSFT;
        if (CHECKS) {
            check(__functionAddress);
            XrNewSceneComputeInfoMSFT.validate(computeInfo);
        }
        return callPPI(sceneObserver.address(), computeInfo, __functionAddress);
    }

    @NativeType("XrResult")
    public static int xrComputeNewSceneMSFT(XrSceneObserverMSFT sceneObserver, @NativeType("XrNewSceneComputeInfoMSFT const *") XrNewSceneComputeInfoMSFT computeInfo) {
        return nxrComputeNewSceneMSFT(sceneObserver, computeInfo.address());
    }

    // --- [ xrGetSceneComputeStateMSFT ] ---

    public static int nxrGetSceneComputeStateMSFT(XrSceneObserverMSFT sceneObserver, long state) {
        long __functionAddress = sceneObserver.getCapabilities().xrGetSceneComputeStateMSFT;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPPI(sceneObserver.address(), state, __functionAddress);
    }

    @NativeType("XrResult")
    public static int xrGetSceneComputeStateMSFT(XrSceneObserverMSFT sceneObserver, @NativeType("XrSceneComputeStateMSFT *") IntBuffer state) {
        if (CHECKS) {
            check(state, 1);
        }
        return nxrGetSceneComputeStateMSFT(sceneObserver, memAddress(state));
    }

    // --- [ xrGetSceneComponentsMSFT ] ---

    public static int nxrGetSceneComponentsMSFT(XrSceneMSFT scene, long getInfo, long components) {
        long __functionAddress = scene.getCapabilities().xrGetSceneComponentsMSFT;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPPPI(scene.address(), getInfo, components, __functionAddress);
    }

    @NativeType("XrResult")
    public static int xrGetSceneComponentsMSFT(XrSceneMSFT scene, @NativeType("XrSceneComponentsGetInfoMSFT const *") XrSceneComponentsGetInfoMSFT getInfo, @NativeType("XrSceneComponentsMSFT *") XrSceneComponentsMSFT components) {
        return nxrGetSceneComponentsMSFT(scene, getInfo.address(), components.address());
    }

    // --- [ xrLocateSceneComponentsMSFT ] ---

    public static int nxrLocateSceneComponentsMSFT(XrSceneMSFT scene, long locateInfo, long locations) {
        long __functionAddress = scene.getCapabilities().xrLocateSceneComponentsMSFT;
        if (CHECKS) {
            check(__functionAddress);
            XrSceneComponentsLocateInfoMSFT.validate(locateInfo);
        }
        return callPPPI(scene.address(), locateInfo, locations, __functionAddress);
    }

    @NativeType("XrResult")
    public static int xrLocateSceneComponentsMSFT(XrSceneMSFT scene, @NativeType("XrSceneComponentsLocateInfoMSFT const *") XrSceneComponentsLocateInfoMSFT locateInfo, @NativeType("XrSceneComponentLocationsMSFT *") XrSceneComponentLocationsMSFT locations) {
        return nxrLocateSceneComponentsMSFT(scene, locateInfo.address(), locations.address());
    }

    // --- [ xrGetSceneMeshBuffersMSFT ] ---

    public static int nxrGetSceneMeshBuffersMSFT(XrSceneMSFT scene, long getInfo, long buffers) {
        long __functionAddress = scene.getCapabilities().xrGetSceneMeshBuffersMSFT;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPPPI(scene.address(), getInfo, buffers, __functionAddress);
    }

    @NativeType("XrResult")
    public static int xrGetSceneMeshBuffersMSFT(XrSceneMSFT scene, @NativeType("XrSceneMeshBuffersGetInfoMSFT const *") XrSceneMeshBuffersGetInfoMSFT getInfo, @NativeType("XrSceneMeshBuffersMSFT *") XrSceneMeshBuffersMSFT buffers) {
        return nxrGetSceneMeshBuffersMSFT(scene, getInfo.address(), buffers.address());
    }

    /** Array version of: {@link #xrEnumerateSceneComputeFeaturesMSFT EnumerateSceneComputeFeaturesMSFT} */
    @NativeType("XrResult")
    public static int xrEnumerateSceneComputeFeaturesMSFT(XrInstance instance, @NativeType("XrSystemId") long systemId, @NativeType("uint32_t *") int[] featureCountOutput, @Nullable @NativeType("XrSceneComputeFeatureMSFT *") int[] features) {
        long __functionAddress = instance.getCapabilities().xrEnumerateSceneComputeFeaturesMSFT;
        if (CHECKS) {
            check(__functionAddress);
            check(featureCountOutput, 1);
        }
        return callPJPPI(instance.address(), systemId, lengthSafe(features), featureCountOutput, features, __functionAddress);
    }

    /** Array version of: {@link #xrGetSceneComputeStateMSFT GetSceneComputeStateMSFT} */
    @NativeType("XrResult")
    public static int xrGetSceneComputeStateMSFT(XrSceneObserverMSFT sceneObserver, @NativeType("XrSceneComputeStateMSFT *") int[] state) {
        long __functionAddress = sceneObserver.getCapabilities().xrGetSceneComputeStateMSFT;
        if (CHECKS) {
            check(__functionAddress);
            check(state, 1);
        }
        return callPPI(sceneObserver.address(), state, __functionAddress);
    }

}