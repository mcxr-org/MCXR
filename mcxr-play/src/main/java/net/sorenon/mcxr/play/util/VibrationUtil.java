package net.sorenon.mcxr.play.util;

import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.play.input.XrInput;
import org.lwjgl.openxr.*;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;

public class VibrationUtil {
    /**
     * Used to vibrate a controller.
     * @param duration The duration of the vibration in millis.
     * @param amplitude The strength of the vibration.
     * @param frequency The frequency of the vibration in hertz.
     * @param controllerIndex The index of the controller to vibrate 0 being left and 1 being right.
     */
    public static void vibrate(int duration, float amplitude, int frequency, int controllerIndex) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            ByteBuffer buf = stack.calloc(XrHapticVibration.SIZEOF);
            ByteBuffer infoBuf = stack.calloc(XrHapticActionInfo.SIZEOF);

            var action = controllerIndex == 0 ? XrInput.vanillaGameplayActionSet.hapticLeft.getHandle() : XrInput.vanillaGameplayActionSet.hapticRight.getHandle();

            XrHapticVibration vibration = new XrHapticVibration(buf);
            vibration.duration(duration);
            vibration.amplitude(amplitude);
            vibration.frequency(frequency);

            XrHapticActionInfo info = new XrHapticActionInfo(infoBuf);
            info.action(action);

            XR10.xrApplyHapticFeedback(MCXRPlayClient.OPEN_XR.session.handle, info, vibration);
        }
    }
}
