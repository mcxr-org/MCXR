package net.sorenon.mcxr.play.util;

import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.play.input.XrInput;
import org.lwjgl.openxr.*;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.system.MemoryUtil.*;

public class VibrationUtil {
    /**
     * Used to vibrate a controller.
     * @param duration The duration of the vibration in millis.
     * @param amplitude The strength of the vibration.
     * @param frequency The frequency of the vibration in hertz.
     */
    public static void vibrate(int duration, float amplitude, int frequency) {
        try (MemoryStack ignored = MemoryStack.stackPush()) {
            var action = XrInput.vanillaGameplayActionSet.haptics.getHandle();

            XrHapticVibration vibration = XrHapticVibration.malloc().set(
                    XR10.XR_TYPE_HAPTIC_VIBRATION,
                    NULL,
                    duration,
                    frequency,
                    amplitude
            );

            XrHapticActionInfo info = XrHapticActionInfo.malloc().set(
                    XR10.XR_TYPE_HAPTIC_ACTION_INFO,
                    NULL,
                    action,
                    NULL
            );

            XrHapticBaseHeader vibrationHeader = XrHapticBaseHeader.create(vibration.address());

            XR10.xrApplyHapticFeedback(MCXRPlayClient.OPEN_XR.session.handle, info, vibrationHeader);
        }
    }
}
