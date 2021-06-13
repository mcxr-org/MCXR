/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
package org.lwjgl.openxr;

import org.lwjgl.system.FunctionProvider;

import java.util.Set;

import static org.lwjgl.openxr.LWJGLHAX.checkFunctions;
import static org.lwjgl.openxr.LWJGLHAX.reportMissing;

/** Defines the capabilities of a OpenXR {@code XRInstance}. */
public class XRCapabilities {

    // EXT_conformance_automation
    public final long
        xrSetInputDeviceActiveEXT,
        xrSetInputDeviceStateBoolEXT,
        xrSetInputDeviceStateFloatEXT,
        xrSetInputDeviceStateVector2fEXT,
        xrSetInputDeviceLocationEXT;

    // EXT_debug_utils
    public final long
        xrSetDebugUtilsObjectNameEXT,
        xrCreateDebugUtilsMessengerEXT,
        xrDestroyDebugUtilsMessengerEXT,
        xrSubmitDebugUtilsMessageEXT,
        xrSessionBeginDebugUtilsLabelRegionEXT,
        xrSessionEndDebugUtilsLabelRegionEXT,
        xrSessionInsertDebugUtilsLabelEXT;

    // EXT_hand_tracking
    public final long
        xrCreateHandTrackerEXT,
        xrDestroyHandTrackerEXT,
        xrLocateHandJointsEXT;

    // EXT_performance_settings
    public final long
        xrPerfSettingsSetPerformanceLevelEXT;

    // EXT_thermal_query
    public final long
        xrThermalGetTemperatureTrendEXT;

    // FB_color_space
    public final long
        xrEnumerateColorSpacesFB,
        xrSetColorSpaceFB;

    // FB_display_refresh_rate
    public final long
        xrEnumerateDisplayRefreshRatesFB,
        xrGetDisplayRefreshRateFB,
        xrRequestDisplayRefreshRateFB;

    // KHR_android_thread_settings
    public final long
        xrSetAndroidApplicationThreadKHR;

    // KHR_opengl_enable
    public final long
        xrGetOpenGLGraphicsRequirementsKHR;

    // KHR_opengl_es_enable
    public final long
        xrGetOpenGLESGraphicsRequirementsKHR;

    // KHR_visibility_mask
    public final long
        xrGetVisibilityMaskKHR;

    // KHR_vulkan_enable
    public final long
        xrGetVulkanInstanceExtensionsKHR,
        xrGetVulkanDeviceExtensionsKHR,
        xrGetVulkanGraphicsDeviceKHR,
        xrGetVulkanGraphicsRequirementsKHR;

    // KHR_vulkan_enable2
    public final long
        xrCreateVulkanInstanceKHR,
        xrCreateVulkanDeviceKHR,
        xrGetVulkanGraphicsDevice2KHR,
        xrGetVulkanGraphicsRequirements2KHR;

    // KHR_win32_convert_performance_counter_time
    public final long
        xrConvertWin32PerformanceCounterToTimeKHR,
        xrConvertTimeToWin32PerformanceCounterKHR;

    // MSFT_controller_model
    public final long
        xrGetControllerModelKeyMSFT,
        xrLoadControllerModelMSFT,
        xrGetControllerModelPropertiesMSFT,
        xrGetControllerModelStateMSFT;

    // MSFT_hand_tracking_mesh
    public final long
        xrCreateHandMeshSpaceMSFT,
        xrUpdateHandMeshMSFT;

    // MSFT_spatial_anchor
    public final long
        xrCreateSpatialAnchorMSFT,
        xrCreateSpatialAnchorSpaceMSFT,
        xrDestroySpatialAnchorMSFT;

    // MSFT_spatial_graph_bridge
    public final long
        xrCreateSpatialGraphNodeSpaceMSFT;

    // XR10
    public final long
        xrDestroyInstance,
        xrGetInstanceProperties,
        xrPollEvent,
        xrResultToString,
        xrStructureTypeToString,
        xrGetSystem,
        xrGetSystemProperties,
        xrEnumerateEnvironmentBlendModes,
        xrCreateSession,
        xrDestroySession,
        xrEnumerateReferenceSpaces,
        xrCreateReferenceSpace,
        xrGetReferenceSpaceBoundsRect,
        xrCreateActionSpace,
        xrLocateSpace,
        xrDestroySpace,
        xrEnumerateViewConfigurations,
        xrGetViewConfigurationProperties,
        xrEnumerateViewConfigurationViews,
        xrEnumerateSwapchainFormats,
        xrCreateSwapchain,
        xrDestroySwapchain,
        xrEnumerateSwapchainImages,
        xrAcquireSwapchainImage,
        xrWaitSwapchainImage,
        xrReleaseSwapchainImage,
        xrBeginSession,
        xrEndSession,
        xrRequestExitSession,
        xrWaitFrame,
        xrBeginFrame,
        xrEndFrame,
        xrLocateViews,
        xrStringToPath,
        xrPathToString,
        xrCreateActionSet,
        xrDestroyActionSet,
        xrCreateAction,
        xrDestroyAction,
        xrSuggestInteractionProfileBindings,
        xrAttachSessionActionSets,
        xrGetCurrentInteractionProfile,
        xrGetActionStateBoolean,
        xrGetActionStateFloat,
        xrGetActionStateVector2f,
        xrGetActionStatePose,
        xrSyncActions,
        xrEnumerateBoundSourcesForAction,
        xrGetInputSourceLocalizedName,
        xrApplyHapticFeedback,
        xrStopHapticFeedback;

    /** The OpenXR API version number. */
    public final long apiVersion;

    /** When true, {@link EPICViewConfigurationFov} is supported. */
    public final boolean XR_EPIC_view_configuration_fov;
    /** When true, {@link EXTConformanceAutomation} is supported. */
    public final boolean XR_EXT_conformance_automation;
    /** When true, {@link EXTDebugUtils} is supported. */
    public final boolean XR_EXT_debug_utils;
    /** When true, {@link EXTEyeGazeInteraction} is supported. */
    public final boolean XR_EXT_eye_gaze_interaction;
    /** When true, {@link EXTHandTracking} is supported. */
    public final boolean XR_EXT_hand_tracking;
    /** When true, {@link EXTHpMixedRealityController} is supported. */
    public final boolean XR_EXT_hp_mixed_reality_controller;
    /** When true, {@link EXTPerformanceSettings} is supported. */
    public final boolean XR_EXT_performance_settings;
    /** When true, {@link EXTSamsungOdysseyController} is supported. */
    public final boolean XR_EXT_samsung_odyssey_controller;
    /** When true, {@link EXTThermalQuery} is supported. */
    public final boolean XR_EXT_thermal_query;
    /** When true, {@link EXTViewConfigurationDepthRange} is supported. */
    public final boolean XR_EXT_view_configuration_depth_range;
    /** When true, {@link EXTWin32AppcontainerCompatible} is supported. */
    public final boolean XR_EXT_win32_appcontainer_compatible;
    /** When true, {@link EXTXOverlay} is supported. */
    public final boolean XR_EXTX_overlay;
    /** When true, {@link FBAndroidSurfaceSwapchainCreate} is supported. */
    public final boolean XR_FB_android_surface_swapchain_create;
    /** When true, {@link FBColorSpace} is supported. */
    public final boolean XR_FB_color_space;
    /** When true, {@link FBDisplayRefreshRate} is supported. */
    public final boolean XR_FB_display_refresh_rate;
    /** When true, {@link HTCViveCosmosControllerInteraction} is supported. */
    public final boolean XR_HTC_vive_cosmos_controller_interaction;
    /** When true, {@link HUAWEIControllerInteraction} is supported. */
    public final boolean XR_HUAWEI_controller_interaction;
    /** When true, {@link KHRAndroidCreateInstance} is supported. */
    public final boolean XR_KHR_android_create_instance;
    /** When true, {@link KHRAndroidThreadSettings} is supported. */
    public final boolean XR_KHR_android_thread_settings;
    /** When true, {@link KHRBindingModification} is supported. */
    public final boolean XR_KHR_binding_modification;
    /** When true, {@link KHRCompositionLayerColorScaleBias} is supported. */
    public final boolean XR_KHR_composition_layer_color_scale_bias;
    /** When true, {@link KHRCompositionLayerCube} is supported. */
    public final boolean XR_KHR_composition_layer_cube;
    /** When true, {@link KHRCompositionLayerCylinder} is supported. */
    public final boolean XR_KHR_composition_layer_cylinder;
    /** When true, {@link KHRCompositionLayerDepth} is supported. */
    public final boolean XR_KHR_composition_layer_depth;
    /** When true, {@link KHRCompositionLayerEquirect} is supported. */
    public final boolean XR_KHR_composition_layer_equirect;
    /** When true, {@link KHRCompositionLayerEquirect2} is supported. */
    public final boolean XR_KHR_composition_layer_equirect2;
    /** When true, {@link KHRLoaderInit} is supported. */
    public final boolean XR_KHR_loader_init;
    /** When true, {@link KHRLoaderInitAndroid} is supported. */
    public final boolean XR_KHR_loader_init_android;
    /** When true, {@link KHROpenglEnable} is supported. */
    public final boolean XR_KHR_opengl_enable;
    /** When true, {@link KHROpenglEsEnable} is supported. */
    public final boolean XR_KHR_opengl_es_enable;
    /** When true, {@link KHRVisibilityMask} is supported. */
    public final boolean XR_KHR_visibility_mask;
    /** When true, {@link KHRWin32ConvertPerformanceCounterTime} is supported. */
    public final boolean XR_KHR_win32_convert_performance_counter_time;
    /** When true, {@link MNDHeadless} is supported. */
    public final boolean XR_MND_headless;
    /** When true, {@link MNDSwapchainUsageInputAttachmentBit} is supported. */
    public final boolean XR_MND_swapchain_usage_input_attachment_bit;
    /** When true, {@link MNDXEglEnable} is supported. */
    public final boolean XR_MNDX_egl_enable;
    /** When true, {@link MSFTControllerModel} is supported. */
    public final boolean XR_MSFT_controller_model;
    /** When true, {@link MSFTFirstPersonObserver} is supported. */
    public final boolean XR_MSFT_first_person_observer;
    /** When true, {@link MSFTHandInteraction} is supported. */
    public final boolean XR_MSFT_hand_interaction;
    /** When true, {@link MSFTHandTrackingMesh} is supported. */
    public final boolean XR_MSFT_hand_tracking_mesh;
    /** When true, {@link MSFTSecondaryViewConfiguration} is supported. */
    public final boolean XR_MSFT_secondary_view_configuration;
    /** When true, {@link MSFTSpatialAnchor} is supported. */
    public final boolean XR_MSFT_spatial_anchor;
    /** When true, {@link MSFTSpatialGraphBridge} is supported. */
    public final boolean XR_MSFT_spatial_graph_bridge;
    /** When true, {@link MSFTUnboundedReferenceSpace} is supported. */
    public final boolean XR_MSFT_unbounded_reference_space;
    /** When true, {@link OCULUSAndroidSessionStateEnable} is supported. */
    public final boolean XR_OCULUS_android_session_state_enable;
    /** When true, {@link VALVEAnalogThreshold} is supported. */
    public final boolean XR_VALVE_analog_threshold;
    /** When true, {@link VARJOQuadViews} is supported. */
    public final boolean XR_VARJO_quad_views;
    /** When true, {@link XR10} is supported. */
    public final boolean OpenXR10;

    XRCapabilities(FunctionProvider provider, long apiVersion, Set<String> ext) {
        this.apiVersion = apiVersion;

        long[] caps = new long[97];

        XR_EPIC_view_configuration_fov = ext.contains("XR_EPIC_view_configuration_fov");
        XR_EXT_conformance_automation = check_EXT_conformance_automation(provider, caps, ext);
        XR_EXT_debug_utils = check_EXT_debug_utils(provider, caps, ext);
        XR_EXT_eye_gaze_interaction = ext.contains("XR_EXT_eye_gaze_interaction");
        XR_EXT_hand_tracking = check_EXT_hand_tracking(provider, caps, ext);
        XR_EXT_hp_mixed_reality_controller = ext.contains("XR_EXT_hp_mixed_reality_controller");
        XR_EXT_performance_settings = check_EXT_performance_settings(provider, caps, ext);
        XR_EXT_samsung_odyssey_controller = ext.contains("XR_EXT_samsung_odyssey_controller");
        XR_EXT_thermal_query = check_EXT_thermal_query(provider, caps, ext);
        XR_EXT_view_configuration_depth_range = ext.contains("XR_EXT_view_configuration_depth_range");
        XR_EXT_win32_appcontainer_compatible = ext.contains("XR_EXT_win32_appcontainer_compatible");
        XR_EXTX_overlay = ext.contains("XR_EXTX_overlay");
        XR_FB_android_surface_swapchain_create = ext.contains("XR_FB_android_surface_swapchain_create");
        XR_FB_color_space = check_FB_color_space(provider, caps, ext);
        XR_FB_display_refresh_rate = check_FB_display_refresh_rate(provider, caps, ext);
        XR_HTC_vive_cosmos_controller_interaction = ext.contains("XR_HTC_vive_cosmos_controller_interaction");
        XR_HUAWEI_controller_interaction = ext.contains("XR_HUAWEI_controller_interaction");
        XR_KHR_android_create_instance = ext.contains("XR_KHR_android_create_instance");
        XR_KHR_android_thread_settings = check_KHR_android_thread_settings(provider, caps, ext);
        XR_KHR_binding_modification = ext.contains("XR_KHR_binding_modification");
        XR_KHR_composition_layer_color_scale_bias = ext.contains("XR_KHR_composition_layer_color_scale_bias");
        XR_KHR_composition_layer_cube = ext.contains("XR_KHR_composition_layer_cube");
        XR_KHR_composition_layer_cylinder = ext.contains("XR_KHR_composition_layer_cylinder");
        XR_KHR_composition_layer_depth = ext.contains("XR_KHR_composition_layer_depth");
        XR_KHR_composition_layer_equirect = ext.contains("XR_KHR_composition_layer_equirect");
        XR_KHR_composition_layer_equirect2 = ext.contains("XR_KHR_composition_layer_equirect2");
        XR_KHR_loader_init = ext.contains("XR_KHR_loader_init");
        XR_KHR_loader_init_android = ext.contains("XR_KHR_loader_init_android");
        XR_KHR_opengl_enable = check_KHR_opengl_enable(provider, caps, ext);
        XR_KHR_opengl_es_enable = check_KHR_opengl_es_enable(provider, caps, ext);
        XR_KHR_visibility_mask = check_KHR_visibility_mask(provider, caps, ext);
//        XR_KHR_vulkan_enable = check_KHR_vulkan_enable(provider, caps, ext);
//        XR_KHR_vulkan_enable2 = check_KHR_vulkan_enable2(provider, caps, ext);
//        XR_KHR_vulkan_swapchain_format_list = ext.contains("XR_KHR_vulkan_swapchain_format_list");
        XR_KHR_win32_convert_performance_counter_time = check_KHR_win32_convert_performance_counter_time(provider, caps, ext);
        XR_MND_headless = ext.contains("XR_MND_headless");
        XR_MND_swapchain_usage_input_attachment_bit = ext.contains("XR_MND_swapchain_usage_input_attachment_bit");
        XR_MNDX_egl_enable = ext.contains("XR_MNDX_egl_enable");
        XR_MSFT_controller_model = check_MSFT_controller_model(provider, caps, ext);
        XR_MSFT_first_person_observer = ext.contains("XR_MSFT_first_person_observer");
        XR_MSFT_hand_interaction = ext.contains("XR_MSFT_hand_interaction");
        XR_MSFT_hand_tracking_mesh = check_MSFT_hand_tracking_mesh(provider, caps, ext);
        XR_MSFT_secondary_view_configuration = ext.contains("XR_MSFT_secondary_view_configuration");
        XR_MSFT_spatial_anchor = check_MSFT_spatial_anchor(provider, caps, ext);
        XR_MSFT_spatial_graph_bridge = check_MSFT_spatial_graph_bridge(provider, caps, ext);
        XR_MSFT_unbounded_reference_space = ext.contains("XR_MSFT_unbounded_reference_space");
        XR_OCULUS_android_session_state_enable = ext.contains("XR_OCULUS_android_session_state_enable");
        XR_VALVE_analog_threshold = ext.contains("XR_VALVE_analog_threshold");
        XR_VARJO_quad_views = ext.contains("XR_VARJO_quad_views");
        OpenXR10 = check_XR10(provider, caps, ext);

        xrSetInputDeviceActiveEXT = caps[0];
        xrSetInputDeviceStateBoolEXT = caps[1];
        xrSetInputDeviceStateFloatEXT = caps[2];
        xrSetInputDeviceStateVector2fEXT = caps[3];
        xrSetInputDeviceLocationEXT = caps[4];
        xrSetDebugUtilsObjectNameEXT = caps[5];
        xrCreateDebugUtilsMessengerEXT = caps[6];
        xrDestroyDebugUtilsMessengerEXT = caps[7];
        xrSubmitDebugUtilsMessageEXT = caps[8];
        xrSessionBeginDebugUtilsLabelRegionEXT = caps[9];
        xrSessionEndDebugUtilsLabelRegionEXT = caps[10];
        xrSessionInsertDebugUtilsLabelEXT = caps[11];
        xrCreateHandTrackerEXT = caps[12];
        xrDestroyHandTrackerEXT = caps[13];
        xrLocateHandJointsEXT = caps[14];
        xrPerfSettingsSetPerformanceLevelEXT = caps[15];
        xrThermalGetTemperatureTrendEXT = caps[16];
        xrEnumerateColorSpacesFB = caps[17];
        xrSetColorSpaceFB = caps[18];
        xrEnumerateDisplayRefreshRatesFB = caps[19];
        xrGetDisplayRefreshRateFB = caps[20];
        xrRequestDisplayRefreshRateFB = caps[21];
        xrSetAndroidApplicationThreadKHR = caps[22];
        xrGetOpenGLGraphicsRequirementsKHR = caps[23];
        xrGetOpenGLESGraphicsRequirementsKHR = caps[24];
        xrGetVisibilityMaskKHR = caps[25];
        xrGetVulkanInstanceExtensionsKHR = caps[26];
        xrGetVulkanDeviceExtensionsKHR = caps[27];
        xrGetVulkanGraphicsDeviceKHR = caps[28];
        xrGetVulkanGraphicsRequirementsKHR = caps[29];
        xrCreateVulkanInstanceKHR = caps[30];
        xrCreateVulkanDeviceKHR = caps[31];
        xrGetVulkanGraphicsDevice2KHR = caps[32];
        xrGetVulkanGraphicsRequirements2KHR = caps[33];
        xrConvertWin32PerformanceCounterToTimeKHR = caps[34];
        xrConvertTimeToWin32PerformanceCounterKHR = caps[35];
        xrGetControllerModelKeyMSFT = caps[36];
        xrLoadControllerModelMSFT = caps[37];
        xrGetControllerModelPropertiesMSFT = caps[38];
        xrGetControllerModelStateMSFT = caps[39];
        xrCreateHandMeshSpaceMSFT = caps[40];
        xrUpdateHandMeshMSFT = caps[41];
        xrCreateSpatialAnchorMSFT = caps[42];
        xrCreateSpatialAnchorSpaceMSFT = caps[43];
        xrDestroySpatialAnchorMSFT = caps[44];
        xrCreateSpatialGraphNodeSpaceMSFT = caps[45];
        xrDestroyInstance = caps[46];
        xrGetInstanceProperties = caps[47];
        xrPollEvent = caps[48];
        xrResultToString = caps[49];
        xrStructureTypeToString = caps[50];
        xrGetSystem = caps[51];
        xrGetSystemProperties = caps[52];
        xrEnumerateEnvironmentBlendModes = caps[53];
        xrCreateSession = caps[54];
        xrDestroySession = caps[55];
        xrEnumerateReferenceSpaces = caps[56];
        xrCreateReferenceSpace = caps[57];
        xrGetReferenceSpaceBoundsRect = caps[58];
        xrCreateActionSpace = caps[59];
        xrLocateSpace = caps[60];
        xrDestroySpace = caps[61];
        xrEnumerateViewConfigurations = caps[62];
        xrGetViewConfigurationProperties = caps[63];
        xrEnumerateViewConfigurationViews = caps[64];
        xrEnumerateSwapchainFormats = caps[65];
        xrCreateSwapchain = caps[66];
        xrDestroySwapchain = caps[67];
        xrEnumerateSwapchainImages = caps[68];
        xrAcquireSwapchainImage = caps[69];
        xrWaitSwapchainImage = caps[70];
        xrReleaseSwapchainImage = caps[71];
        xrBeginSession = caps[72];
        xrEndSession = caps[73];
        xrRequestExitSession = caps[74];
        xrWaitFrame = caps[75];
        xrBeginFrame = caps[76];
        xrEndFrame = caps[77];
        xrLocateViews = caps[78];
        xrStringToPath = caps[79];
        xrPathToString = caps[80];
        xrCreateActionSet = caps[81];
        xrDestroyActionSet = caps[82];
        xrCreateAction = caps[83];
        xrDestroyAction = caps[84];
        xrSuggestInteractionProfileBindings = caps[85];
        xrAttachSessionActionSets = caps[86];
        xrGetCurrentInteractionProfile = caps[87];
        xrGetActionStateBoolean = caps[88];
        xrGetActionStateFloat = caps[89];
        xrGetActionStateVector2f = caps[90];
        xrGetActionStatePose = caps[91];
        xrSyncActions = caps[92];
        xrEnumerateBoundSourcesForAction = caps[93];
        xrGetInputSourceLocalizedName = caps[94];
        xrApplyHapticFeedback = caps[95];
        xrStopHapticFeedback = caps[96];
    }

    private static boolean check_EXT_conformance_automation(FunctionProvider provider, long[] caps, Set<String> ext) {
        if (!ext.contains("XR_EXT_conformance_automation")) {
            return false;
        }

        return checkFunctions(provider, caps, new int[] {
            0, 1, 2, 3, 4
        },
            "xrSetInputDeviceActiveEXT", "xrSetInputDeviceStateBoolEXT", "xrSetInputDeviceStateFloatEXT", "xrSetInputDeviceStateVector2fEXT", 
            "xrSetInputDeviceLocationEXT"
        ) || reportMissing("XR", "XR_EXT_conformance_automation");
    }

    private static boolean check_EXT_debug_utils(FunctionProvider provider, long[] caps, Set<String> ext) {
        if (!ext.contains("XR_EXT_debug_utils")) {
            return false;
        }

        return checkFunctions(provider, caps, new int[] {
            5, 6, 7, 8, 9, 10, 11
        },
            "xrSetDebugUtilsObjectNameEXT", "xrCreateDebugUtilsMessengerEXT", "xrDestroyDebugUtilsMessengerEXT", "xrSubmitDebugUtilsMessageEXT", 
            "xrSessionBeginDebugUtilsLabelRegionEXT", "xrSessionEndDebugUtilsLabelRegionEXT", "xrSessionInsertDebugUtilsLabelEXT"
        ) || reportMissing("XR", "XR_EXT_debug_utils");
    }

    private static boolean check_EXT_hand_tracking(FunctionProvider provider, long[] caps, Set<String> ext) {
        if (!ext.contains("XR_EXT_hand_tracking")) {
            return false;
        }

        return checkFunctions(provider, caps, new int[] {
            12, 13, 14
        },
            "xrCreateHandTrackerEXT", "xrDestroyHandTrackerEXT", "xrLocateHandJointsEXT"
        ) || reportMissing("XR", "XR_EXT_hand_tracking");
    }

    private static boolean check_EXT_performance_settings(FunctionProvider provider, long[] caps, Set<String> ext) {
        if (!ext.contains("XR_EXT_performance_settings")) {
            return false;
        }

        return checkFunctions(provider, caps, new int[] {
            15
        },
            "xrPerfSettingsSetPerformanceLevelEXT"
        ) || reportMissing("XR", "XR_EXT_performance_settings");
    }

    private static boolean check_EXT_thermal_query(FunctionProvider provider, long[] caps, Set<String> ext) {
        if (!ext.contains("XR_EXT_thermal_query")) {
            return false;
        }

        return checkFunctions(provider, caps, new int[] {
            16
        },
            "xrThermalGetTemperatureTrendEXT"
        ) || reportMissing("XR", "XR_EXT_thermal_query");
    }

    private static boolean check_FB_color_space(FunctionProvider provider, long[] caps, Set<String> ext) {
        if (!ext.contains("XR_FB_color_space")) {
            return false;
        }

        return checkFunctions(provider, caps, new int[] {
            17, 18
        },
            "xrEnumerateColorSpacesFB", "xrSetColorSpaceFB"
        ) || reportMissing("XR", "XR_FB_color_space");
    }

    private static boolean check_FB_display_refresh_rate(FunctionProvider provider, long[] caps, Set<String> ext) {
        if (!ext.contains("XR_FB_display_refresh_rate")) {
            return false;
        }

        return checkFunctions(provider, caps, new int[] {
            19, 20, 21
        },
            "xrEnumerateDisplayRefreshRatesFB", "xrGetDisplayRefreshRateFB", "xrRequestDisplayRefreshRateFB"
        ) || reportMissing("XR", "XR_FB_display_refresh_rate");
    }

    private static boolean check_KHR_android_thread_settings(FunctionProvider provider, long[] caps, Set<String> ext) {
        if (!ext.contains("XR_KHR_android_thread_settings")) {
            return false;
        }

        return checkFunctions(provider, caps, new int[] {
            22
        },
            "xrSetAndroidApplicationThreadKHR"
        ) || reportMissing("XR", "XR_KHR_android_thread_settings");
    }

    private static boolean check_KHR_opengl_enable(FunctionProvider provider, long[] caps, Set<String> ext) {
        if (!ext.contains("XR_KHR_opengl_enable")) {
            return false;
        }

        return checkFunctions(provider, caps, new int[] {
            23
        },
            "xrGetOpenGLGraphicsRequirementsKHR"
        ) || reportMissing("XR", "XR_KHR_opengl_enable");
    }

    private static boolean check_KHR_opengl_es_enable(FunctionProvider provider, long[] caps, Set<String> ext) {
        if (!ext.contains("XR_KHR_opengl_es_enable")) {
            return false;
        }

        return checkFunctions(provider, caps, new int[] {
            24
        },
            "xrGetOpenGLESGraphicsRequirementsKHR"
        ) || reportMissing("XR", "XR_KHR_opengl_es_enable");
    }

    private static boolean check_KHR_visibility_mask(FunctionProvider provider, long[] caps, Set<String> ext) {
        if (!ext.contains("XR_KHR_visibility_mask")) {
            return false;
        }

        return checkFunctions(provider, caps, new int[] {
            25
        },
            "xrGetVisibilityMaskKHR"
        ) || reportMissing("XR", "XR_KHR_visibility_mask");
    }

    private static boolean check_KHR_vulkan_enable(FunctionProvider provider, long[] caps, Set<String> ext) {
        if (!ext.contains("XR_KHR_vulkan_enable")) {
            return false;
        }

        return checkFunctions(provider, caps, new int[] {
            26, 27, 28, 29
        },
            "xrGetVulkanInstanceExtensionsKHR", "xrGetVulkanDeviceExtensionsKHR", "xrGetVulkanGraphicsDeviceKHR", "xrGetVulkanGraphicsRequirementsKHR"
        ) || reportMissing("XR", "XR_KHR_vulkan_enable");
    }

    private static boolean check_KHR_vulkan_enable2(FunctionProvider provider, long[] caps, Set<String> ext) {
        if (!ext.contains("XR_KHR_vulkan_enable2")) {
            return false;
        }

        return checkFunctions(provider, caps, new int[] {
            30, 31, 32, 33
        },
            "xrCreateVulkanInstanceKHR", "xrCreateVulkanDeviceKHR", "xrGetVulkanGraphicsDevice2KHR", "xrGetVulkanGraphicsRequirements2KHR"
        ) || reportMissing("XR", "XR_KHR_vulkan_enable2");
    }

    private static boolean check_KHR_win32_convert_performance_counter_time(FunctionProvider provider, long[] caps, Set<String> ext) {
        if (!ext.contains("XR_KHR_win32_convert_performance_counter_time")) {
            return false;
        }

        return checkFunctions(provider, caps, new int[] {
            34, 35
        },
            "xrConvertWin32PerformanceCounterToTimeKHR", "xrConvertTimeToWin32PerformanceCounterKHR"
        ) || reportMissing("XR", "XR_KHR_win32_convert_performance_counter_time");
    }

    private static boolean check_MSFT_controller_model(FunctionProvider provider, long[] caps, Set<String> ext) {
        if (!ext.contains("XR_MSFT_controller_model")) {
            return false;
        }

        return checkFunctions(provider, caps, new int[] {
            36, 37, 38, 39
        },
            "xrGetControllerModelKeyMSFT", "xrLoadControllerModelMSFT", "xrGetControllerModelPropertiesMSFT", "xrGetControllerModelStateMSFT"
        ) || reportMissing("XR", "XR_MSFT_controller_model");
    }

    private static boolean check_MSFT_hand_tracking_mesh(FunctionProvider provider, long[] caps, Set<String> ext) {
        if (!ext.contains("XR_MSFT_hand_tracking_mesh")) {
            return false;
        }

        return checkFunctions(provider, caps, new int[] {
            40, 41
        },
            "xrCreateHandMeshSpaceMSFT", "xrUpdateHandMeshMSFT"
        ) || reportMissing("XR", "XR_MSFT_hand_tracking_mesh");
    }

    private static boolean check_MSFT_spatial_anchor(FunctionProvider provider, long[] caps, Set<String> ext) {
        if (!ext.contains("XR_MSFT_spatial_anchor")) {
            return false;
        }

        return checkFunctions(provider, caps, new int[] {
            42, 43, 44
        },
            "xrCreateSpatialAnchorMSFT", "xrCreateSpatialAnchorSpaceMSFT", "xrDestroySpatialAnchorMSFT"
        ) || reportMissing("XR", "XR_MSFT_spatial_anchor");
    }

    private static boolean check_MSFT_spatial_graph_bridge(FunctionProvider provider, long[] caps, Set<String> ext) {
        if (!ext.contains("XR_MSFT_spatial_graph_bridge")) {
            return false;
        }

        return checkFunctions(provider, caps, new int[] {
            45
        },
            "xrCreateSpatialGraphNodeSpaceMSFT"
        ) || reportMissing("XR", "XR_MSFT_spatial_graph_bridge");
    }

    private static boolean check_XR10(FunctionProvider provider, long[] caps, Set<String> ext) {
        if (!ext.contains("OpenXR10")) {
            return false;
        }

        return checkFunctions(provider, caps, new int[] {
            46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 
            83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96
        },
            "xrDestroyInstance", "xrGetInstanceProperties", "xrPollEvent", "xrResultToString", "xrStructureTypeToString", "xrGetSystem", 
            "xrGetSystemProperties", "xrEnumerateEnvironmentBlendModes", "xrCreateSession", "xrDestroySession", "xrEnumerateReferenceSpaces", 
            "xrCreateReferenceSpace", "xrGetReferenceSpaceBoundsRect", "xrCreateActionSpace", "xrLocateSpace", "xrDestroySpace", 
            "xrEnumerateViewConfigurations", "xrGetViewConfigurationProperties", "xrEnumerateViewConfigurationViews", "xrEnumerateSwapchainFormats", 
            "xrCreateSwapchain", "xrDestroySwapchain", "xrEnumerateSwapchainImages", "xrAcquireSwapchainImage", "xrWaitSwapchainImage", 
            "xrReleaseSwapchainImage", "xrBeginSession", "xrEndSession", "xrRequestExitSession", "xrWaitFrame", "xrBeginFrame", "xrEndFrame", "xrLocateViews", 
            "xrStringToPath", "xrPathToString", "xrCreateActionSet", "xrDestroyActionSet", "xrCreateAction", "xrDestroyAction", 
            "xrSuggestInteractionProfileBindings", "xrAttachSessionActionSets", "xrGetCurrentInteractionProfile", "xrGetActionStateBoolean", 
            "xrGetActionStateFloat", "xrGetActionStateVector2f", "xrGetActionStatePose", "xrSyncActions", "xrEnumerateBoundSourcesForAction", 
            "xrGetInputSourceLocalizedName", "xrApplyHapticFeedback", "xrStopHapticFeedback"
        ) || reportMissing("XR", "OpenXR10");
    }

}
