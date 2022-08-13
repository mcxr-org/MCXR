/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
package org.lwjgl.openxr;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.NativeType;

import static org.lwjgl.system.Checks.CHECKS;
import static org.lwjgl.system.Checks.check;
import static org.lwjgl.system.JNI.*;
import static org.lwjgl.system.MemoryUtil.memAddress;

/** The FB_passthrough extension. */
public class FBPassthrough {

    /** The extension specification version. */
    public static final int XR_FB_passthrough_SPEC_VERSION = 1;

    /** The extension name. */
    public static final String XR_FB_PASSTHROUGH_EXTENSION_NAME = "XR_FB_passthrough";

    /**
     * Extends {@code XrStructureType}.
     * 
     * <h5>Enum values:</h5>
     * 
     * <ul>
     * <li>{@link #XR_TYPE_SYSTEM_PASSTHROUGH_PROPERTIES_FB TYPE_SYSTEM_PASSTHROUGH_PROPERTIES_FB}</li>
     * <li>{@link #XR_TYPE_PASSTHROUGH_CREATE_INFO_FB TYPE_PASSTHROUGH_CREATE_INFO_FB}</li>
     * <li>{@link #XR_TYPE_PASSTHROUGH_LAYER_CREATE_INFO_FB TYPE_PASSTHROUGH_LAYER_CREATE_INFO_FB}</li>
     * <li>{@link #XR_TYPE_COMPOSITION_LAYER_PASSTHROUGH_FB TYPE_COMPOSITION_LAYER_PASSTHROUGH_FB}</li>
     * <li>{@link #XR_TYPE_GEOMETRY_INSTANCE_CREATE_INFO_FB TYPE_GEOMETRY_INSTANCE_CREATE_INFO_FB}</li>
     * <li>{@link #XR_TYPE_GEOMETRY_INSTANCE_TRANSFORM_FB TYPE_GEOMETRY_INSTANCE_TRANSFORM_FB}</li>
     * <li>{@link #XR_TYPE_PASSTHROUGH_STYLE_FB TYPE_PASSTHROUGH_STYLE_FB}</li>
     * <li>{@link #XR_TYPE_PASSTHROUGH_COLOR_MAP_MONO_TO_RGBA_FB TYPE_PASSTHROUGH_COLOR_MAP_MONO_TO_RGBA_FB}</li>
     * <li>{@link #XR_TYPE_PASSTHROUGH_COLOR_MAP_MONO_TO_MONO_FB TYPE_PASSTHROUGH_COLOR_MAP_MONO_TO_MONO_FB}</li>
     * <li>{@link #XR_TYPE_EVENT_DATA_PASSTHROUGH_STATE_CHANGED_FB TYPE_EVENT_DATA_PASSTHROUGH_STATE_CHANGED_FB}</li>
     * </ul>
     */
    public static final int
        XR_TYPE_SYSTEM_PASSTHROUGH_PROPERTIES_FB        = 1000118000,
        XR_TYPE_PASSTHROUGH_CREATE_INFO_FB              = 1000118001,
        XR_TYPE_PASSTHROUGH_LAYER_CREATE_INFO_FB        = 1000118002,
        XR_TYPE_COMPOSITION_LAYER_PASSTHROUGH_FB        = 1000118003,
        XR_TYPE_GEOMETRY_INSTANCE_CREATE_INFO_FB        = 1000118004,
        XR_TYPE_GEOMETRY_INSTANCE_TRANSFORM_FB          = 1000118005,
        XR_TYPE_PASSTHROUGH_STYLE_FB                    = 1000118020,
        XR_TYPE_PASSTHROUGH_COLOR_MAP_MONO_TO_RGBA_FB   = 1000118021,
        XR_TYPE_PASSTHROUGH_COLOR_MAP_MONO_TO_MONO_FB   = 1000118022,
        XR_TYPE_EVENT_DATA_PASSTHROUGH_STATE_CHANGED_FB = 1000118030;

    /**
     * Extends {@code XrResult}.
     * 
     * <h5>Enum values:</h5>
     * 
     * <ul>
     * <li>{@link #XR_ERROR_UNEXPECTED_STATE_PASSTHROUGH_FB ERROR_UNEXPECTED_STATE_PASSTHROUGH_FB}</li>
     * <li>{@link #XR_ERROR_FEATURE_ALREADY_CREATED_PASSTHROUGH_FB ERROR_FEATURE_ALREADY_CREATED_PASSTHROUGH_FB}</li>
     * <li>{@link #XR_ERROR_FEATURE_REQUIRED_PASSTHROUGH_FB ERROR_FEATURE_REQUIRED_PASSTHROUGH_FB}</li>
     * <li>{@link #XR_ERROR_NOT_PERMITTED_PASSTHROUGH_FB ERROR_NOT_PERMITTED_PASSTHROUGH_FB}</li>
     * <li>{@link #XR_ERROR_INSUFFICIENT_RESOURCES_PASSTHROUGH_FB ERROR_INSUFFICIENT_RESOURCES_PASSTHROUGH_FB}</li>
     * <li>{@link #XR_ERROR_UNKNOWN_PASSTHROUGH_FB ERROR_UNKNOWN_PASSTHROUGH_FB}</li>
     * </ul>
     */
    public static final int
        XR_ERROR_UNEXPECTED_STATE_PASSTHROUGH_FB        = -1000118000,
        XR_ERROR_FEATURE_ALREADY_CREATED_PASSTHROUGH_FB = -1000118001,
        XR_ERROR_FEATURE_REQUIRED_PASSTHROUGH_FB        = -1000118002,
        XR_ERROR_NOT_PERMITTED_PASSTHROUGH_FB           = -1000118003,
        XR_ERROR_INSUFFICIENT_RESOURCES_PASSTHROUGH_FB  = -1000118004,
        XR_ERROR_UNKNOWN_PASSTHROUGH_FB                 = -1000118050;

    /** XR_PASSTHROUGH_COLOR_MAP_MONO_SIZE_FB */
    public static final int XR_PASSTHROUGH_COLOR_MAP_MONO_SIZE_FB = 256;

    /**
     * Extends {@code XrObjectType}.
     * 
     * <h5>Enum values:</h5>
     * 
     * <ul>
     * <li>{@link #XR_OBJECT_TYPE_PASSTHROUGH_FB OBJECT_TYPE_PASSTHROUGH_FB}</li>
     * <li>{@link #XR_OBJECT_TYPE_PASSTHROUGH_LAYER_FB OBJECT_TYPE_PASSTHROUGH_LAYER_FB}</li>
     * <li>{@link #XR_OBJECT_TYPE_GEOMETRY_INSTANCE_FB OBJECT_TYPE_GEOMETRY_INSTANCE_FB}</li>
     * </ul>
     */
    public static final int
        XR_OBJECT_TYPE_PASSTHROUGH_FB       = 1000118000,
        XR_OBJECT_TYPE_PASSTHROUGH_LAYER_FB = 1000118002,
        XR_OBJECT_TYPE_GEOMETRY_INSTANCE_FB = 1000118004;

    /** XrPassthroughFlagBitsFB */
    public static final int XR_PASSTHROUGH_IS_RUNNING_AT_CREATION_BIT_FB = 0x1;

    /**
     * XrPassthroughLayerPurposeFB
     * 
     * <h5>Enum values:</h5>
     * 
     * <ul>
     * <li>{@link #XR_PASSTHROUGH_LAYER_PURPOSE_RECONSTRUCTION_FB PASSTHROUGH_LAYER_PURPOSE_RECONSTRUCTION_FB}</li>
     * <li>{@link #XR_PASSTHROUGH_LAYER_PURPOSE_PROJECTED_FB PASSTHROUGH_LAYER_PURPOSE_PROJECTED_FB}</li>
     * </ul>
     */
    public static final int
        XR_PASSTHROUGH_LAYER_PURPOSE_RECONSTRUCTION_FB = 0,
        XR_PASSTHROUGH_LAYER_PURPOSE_PROJECTED_FB      = 1;

    /**
     * XrPassthroughStateChangedFlagBitsFB
     * 
     * <h5>Enum values:</h5>
     * 
     * <ul>
     * <li>{@link #XR_PASSTHROUGH_STATE_CHANGED_REINIT_REQUIRED_BIT_FB PASSTHROUGH_STATE_CHANGED_REINIT_REQUIRED_BIT_FB}</li>
     * <li>{@link #XR_PASSTHROUGH_STATE_CHANGED_NON_RECOVERABLE_ERROR_BIT_FB PASSTHROUGH_STATE_CHANGED_NON_RECOVERABLE_ERROR_BIT_FB}</li>
     * <li>{@link #XR_PASSTHROUGH_STATE_CHANGED_RECOVERABLE_ERROR_BIT_FB PASSTHROUGH_STATE_CHANGED_RECOVERABLE_ERROR_BIT_FB}</li>
     * <li>{@link #XR_PASSTHROUGH_STATE_CHANGED_RESTORED_ERROR_BIT_FB PASSTHROUGH_STATE_CHANGED_RESTORED_ERROR_BIT_FB}</li>
     * </ul>
     */
    public static final int
        XR_PASSTHROUGH_STATE_CHANGED_REINIT_REQUIRED_BIT_FB       = 0x1,
        XR_PASSTHROUGH_STATE_CHANGED_NON_RECOVERABLE_ERROR_BIT_FB = 0x2,
        XR_PASSTHROUGH_STATE_CHANGED_RECOVERABLE_ERROR_BIT_FB     = 0x4,
        XR_PASSTHROUGH_STATE_CHANGED_RESTORED_ERROR_BIT_FB        = 0x8;

    protected FBPassthrough() {
        throw new UnsupportedOperationException();
    }

    // --- [ xrCreatePassthroughFB ] ---

    public static int nxrCreatePassthroughFB(XrSession session, long createInfo, long outPassthrough) {
        long __functionAddress = session.getCapabilities().xrCreatePassthroughFB;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPPPI(session.address(), createInfo, outPassthrough, __functionAddress);
    }

    @NativeType("XrResult")
    public static int xrCreatePassthroughFB(XrSession session, @NativeType("XrPassthroughCreateInfoFB const *") XrPassthroughCreateInfoFB createInfo, @NativeType("XrPassthroughFB *") PointerBuffer outPassthrough) {
        if (CHECKS) {
            check(outPassthrough, 1);
        }
        return nxrCreatePassthroughFB(session, createInfo.address(), memAddress(outPassthrough));
    }

    // --- [ xrDestroyPassthroughFB ] ---

    @NativeType("XrResult")
    public static int xrDestroyPassthroughFB(XrPassthroughFB passthrough) {
        long __functionAddress = passthrough.getCapabilities().xrDestroyPassthroughFB;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPI(passthrough.address(), __functionAddress);
    }

    // --- [ xrPassthroughStartFB ] ---

    @NativeType("XrResult")
    public static int xrPassthroughStartFB(XrPassthroughFB passthrough) {
        long __functionAddress = passthrough.getCapabilities().xrPassthroughStartFB;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPI(passthrough.address(), __functionAddress);
    }

    // --- [ xrPassthroughPauseFB ] ---

    @NativeType("XrResult")
    public static int xrPassthroughPauseFB(XrPassthroughFB passthrough) {
        long __functionAddress = passthrough.getCapabilities().xrPassthroughPauseFB;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPI(passthrough.address(), __functionAddress);
    }

    // --- [ xrCreatePassthroughLayerFB ] ---

    public static int nxrCreatePassthroughLayerFB(XrSession session, long createInfo, long outLayer) {
        long __functionAddress = session.getCapabilities().xrCreatePassthroughLayerFB;
        if (CHECKS) {
            check(__functionAddress);
            XrPassthroughLayerCreateInfoFB.validate(createInfo);
        }
        return callPPPI(session.address(), createInfo, outLayer, __functionAddress);
    }

    @NativeType("XrResult")
    public static int xrCreatePassthroughLayerFB(XrSession session, @NativeType("XrPassthroughLayerCreateInfoFB const *") XrPassthroughLayerCreateInfoFB createInfo, @NativeType("XrPassthroughLayerFB *") PointerBuffer outLayer) {
        if (CHECKS) {
            check(outLayer, 1);
        }
        return nxrCreatePassthroughLayerFB(session, createInfo.address(), memAddress(outLayer));
    }

    // --- [ xrDestroyPassthroughLayerFB ] ---

    @NativeType("XrResult")
    public static int xrDestroyPassthroughLayerFB(XrPassthroughLayerFB layer) {
        long __functionAddress = layer.getCapabilities().xrDestroyPassthroughLayerFB;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPI(layer.address(), __functionAddress);
    }

    // --- [ xrPassthroughLayerPauseFB ] ---

    @NativeType("XrResult")
    public static int xrPassthroughLayerPauseFB(XrPassthroughLayerFB layer) {
        long __functionAddress = layer.getCapabilities().xrPassthroughLayerPauseFB;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPI(layer.address(), __functionAddress);
    }

    // --- [ xrPassthroughLayerResumeFB ] ---

    @NativeType("XrResult")
    public static int xrPassthroughLayerResumeFB(XrPassthroughLayerFB layer) {
        long __functionAddress = layer.getCapabilities().xrPassthroughLayerResumeFB;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPI(layer.address(), __functionAddress);
    }

    // --- [ xrPassthroughLayerSetStyleFB ] ---

    public static int nxrPassthroughLayerSetStyleFB(XrPassthroughLayerFB layer, long style) {
        long __functionAddress = layer.getCapabilities().xrPassthroughLayerSetStyleFB;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPPI(layer.address(), style, __functionAddress);
    }

    @NativeType("XrResult")
    public static int xrPassthroughLayerSetStyleFB(XrPassthroughLayerFB layer, @NativeType("XrPassthroughStyleFB const *") XrPassthroughStyleFB style) {
        return nxrPassthroughLayerSetStyleFB(layer, style.address());
    }

    // --- [ xrCreateGeometryInstanceFB ] ---

    public static int nxrCreateGeometryInstanceFB(XrSession session, long createInfo, long outGeometryInstance) {
        long __functionAddress = session.getCapabilities().xrCreateGeometryInstanceFB;
        if (CHECKS) {
            check(__functionAddress);
            XrGeometryInstanceCreateInfoFB.validate(createInfo);
        }
        return callPPPI(session.address(), createInfo, outGeometryInstance, __functionAddress);
    }

    @NativeType("XrResult")
    public static int xrCreateGeometryInstanceFB(XrSession session, @NativeType("XrGeometryInstanceCreateInfoFB const *") XrGeometryInstanceCreateInfoFB createInfo, @NativeType("XrGeometryInstanceFB *") PointerBuffer outGeometryInstance) {
        if (CHECKS) {
            check(outGeometryInstance, 1);
        }
        return nxrCreateGeometryInstanceFB(session, createInfo.address(), memAddress(outGeometryInstance));
    }

    // --- [ xrDestroyGeometryInstanceFB ] ---

    @NativeType("XrResult")
    public static int xrDestroyGeometryInstanceFB(XrGeometryInstanceFB instance) {
        long __functionAddress = instance.getCapabilities().xrDestroyGeometryInstanceFB;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPI(instance.address(), __functionAddress);
    }

    // --- [ xrGeometryInstanceSetTransformFB ] ---

    public static int nxrGeometryInstanceSetTransformFB(XrGeometryInstanceFB instance, long transformation) {
        long __functionAddress = instance.getCapabilities().xrGeometryInstanceSetTransformFB;
        if (CHECKS) {
            check(__functionAddress);
            XrGeometryInstanceTransformFB.validate(transformation);
        }
        return callPPI(instance.address(), transformation, __functionAddress);
    }

    @NativeType("XrResult")
    public static int xrGeometryInstanceSetTransformFB(XrGeometryInstanceFB instance, @NativeType("XrGeometryInstanceTransformFB const *") XrGeometryInstanceTransformFB transformation) {
        return nxrGeometryInstanceSetTransformFB(instance, transformation.address());
    }

}