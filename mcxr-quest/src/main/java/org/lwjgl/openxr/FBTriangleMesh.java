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

import static org.lwjgl.system.Checks.CHECKS;
import static org.lwjgl.system.Checks.check;
import static org.lwjgl.system.JNI.*;
import static org.lwjgl.system.MemoryUtil.memAddress;

/** The FB_triangle_mesh extension. */
public class FBTriangleMesh {

    /** The extension specification version. */
    public static final int XR_FB_triangle_mesh_SPEC_VERSION = 1;

    /** The extension name. */
    public static final String XR_FB_TRIANGLE_MESH_EXTENSION_NAME = "XR_FB_triangle_mesh";

    /** Extends {@code XrStructureType}. */
    public static final int XR_TYPE_TRIANGLE_MESH_CREATE_INFO_FB = 1000117001;

    /** Extends {@code XrObjectType}. */
    public static final int XR_OBJECT_TYPE_TRIANGLE_MESH_FB = 1000117000;

    /** XrTriangleMeshFlagBitsFB */
    public static final int XR_TRIANGLE_MESH_MUTABLE_BIT_FB = 0x1;

    /**
     * XrWindingOrderFB
     * 
     * <h5>Enum values:</h5>
     * 
     * <ul>
     * <li>{@link #XR_WINDING_ORDER_UNKNOWN_FB WINDING_ORDER_UNKNOWN_FB}</li>
     * <li>{@link #XR_WINDING_ORDER_CW_FB WINDING_ORDER_CW_FB}</li>
     * <li>{@link #XR_WINDING_ORDER_CCW_FB WINDING_ORDER_CCW_FB}</li>
     * </ul>
     */
    public static final int
        XR_WINDING_ORDER_UNKNOWN_FB = 0,
        XR_WINDING_ORDER_CW_FB      = 1,
        XR_WINDING_ORDER_CCW_FB     = 2;

    protected FBTriangleMesh() {
        throw new UnsupportedOperationException();
    }

    // --- [ xrCreateTriangleMeshFB ] ---

    public static int nxrCreateTriangleMeshFB(XrSession session, long createInfo, long outTriangleMesh) {
        long __functionAddress = session.getCapabilities().xrCreateTriangleMeshFB;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPPPI(session.address(), createInfo, outTriangleMesh, __functionAddress);
    }

    @NativeType("XrResult")
    public static int xrCreateTriangleMeshFB(XrSession session, @NativeType("XrTriangleMeshCreateInfoFB const *") XrTriangleMeshCreateInfoFB createInfo, @NativeType("XrTriangleMeshFB *") PointerBuffer outTriangleMesh) {
        if (CHECKS) {
            check(outTriangleMesh, 1);
        }
        return nxrCreateTriangleMeshFB(session, createInfo.address(), memAddress(outTriangleMesh));
    }

    // --- [ xrDestroyTriangleMeshFB ] ---

    @NativeType("XrResult")
    public static int xrDestroyTriangleMeshFB(XrTriangleMeshFB mesh) {
        long __functionAddress = mesh.getCapabilities().xrDestroyTriangleMeshFB;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPI(mesh.address(), __functionAddress);
    }

    // --- [ xrTriangleMeshGetVertexBufferFB ] ---

    public static int nxrTriangleMeshGetVertexBufferFB(XrTriangleMeshFB mesh, long outVertexBuffer) {
        long __functionAddress = mesh.getCapabilities().xrTriangleMeshGetVertexBufferFB;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPPI(mesh.address(), outVertexBuffer, __functionAddress);
    }

    @NativeType("XrResult")
    public static int xrTriangleMeshGetVertexBufferFB(XrTriangleMeshFB mesh, @NativeType("XrVector3f **") PointerBuffer outVertexBuffer) {
        if (CHECKS) {
            check(outVertexBuffer, 1);
        }
        return nxrTriangleMeshGetVertexBufferFB(mesh, memAddress(outVertexBuffer));
    }

    // --- [ xrTriangleMeshGetIndexBufferFB ] ---

    public static int nxrTriangleMeshGetIndexBufferFB(XrTriangleMeshFB mesh, long outIndexBuffer) {
        long __functionAddress = mesh.getCapabilities().xrTriangleMeshGetIndexBufferFB;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPPI(mesh.address(), outIndexBuffer, __functionAddress);
    }

    @NativeType("XrResult")
    public static int xrTriangleMeshGetIndexBufferFB(XrTriangleMeshFB mesh, @NativeType("uint32_t **") PointerBuffer outIndexBuffer) {
        if (CHECKS) {
            check(outIndexBuffer, 1);
        }
        return nxrTriangleMeshGetIndexBufferFB(mesh, memAddress(outIndexBuffer));
    }

    // --- [ xrTriangleMeshBeginUpdateFB ] ---

    @NativeType("XrResult")
    public static int xrTriangleMeshBeginUpdateFB(XrTriangleMeshFB mesh) {
        long __functionAddress = mesh.getCapabilities().xrTriangleMeshBeginUpdateFB;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPI(mesh.address(), __functionAddress);
    }

    // --- [ xrTriangleMeshEndUpdateFB ] ---

    @NativeType("XrResult")
    public static int xrTriangleMeshEndUpdateFB(XrTriangleMeshFB mesh, @NativeType("uint32_t") int vertexCount, @NativeType("uint32_t") int triangleCount) {
        long __functionAddress = mesh.getCapabilities().xrTriangleMeshEndUpdateFB;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPI(mesh.address(), vertexCount, triangleCount, __functionAddress);
    }

    // --- [ xrTriangleMeshBeginVertexBufferUpdateFB ] ---

    public static int nxrTriangleMeshBeginVertexBufferUpdateFB(XrTriangleMeshFB mesh, long outVertexCount) {
        long __functionAddress = mesh.getCapabilities().xrTriangleMeshBeginVertexBufferUpdateFB;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPPI(mesh.address(), outVertexCount, __functionAddress);
    }

    @NativeType("XrResult")
    public static int xrTriangleMeshBeginVertexBufferUpdateFB(XrTriangleMeshFB mesh, @NativeType("uint32_t *") IntBuffer outVertexCount) {
        if (CHECKS) {
            check(outVertexCount, 1);
        }
        return nxrTriangleMeshBeginVertexBufferUpdateFB(mesh, memAddress(outVertexCount));
    }

    // --- [ xrTriangleMeshEndVertexBufferUpdateFB ] ---

    @NativeType("XrResult")
    public static int xrTriangleMeshEndVertexBufferUpdateFB(XrTriangleMeshFB mesh) {
        long __functionAddress = mesh.getCapabilities().xrTriangleMeshEndVertexBufferUpdateFB;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPI(mesh.address(), __functionAddress);
    }

    /** Array version of: {@link #xrTriangleMeshBeginVertexBufferUpdateFB TriangleMeshBeginVertexBufferUpdateFB} */
    @NativeType("XrResult")
    public static int xrTriangleMeshBeginVertexBufferUpdateFB(XrTriangleMeshFB mesh, @NativeType("uint32_t *") int[] outVertexCount) {
        long __functionAddress = mesh.getCapabilities().xrTriangleMeshBeginVertexBufferUpdateFB;
        if (CHECKS) {
            check(__functionAddress);
            check(outVertexCount, 1);
        }
        return callPPI(mesh.address(), outVertexCount, __functionAddress);
    }

}