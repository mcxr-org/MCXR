package net.sorenon.mcxr.play.input.actions;

import net.minecraft.client.resources.language.I18n;
import net.sorenon.mcxr.play.openxr.OpenXRInstance;
import net.sorenon.mcxr.play.openxr.XrException;
import org.lwjgl.PointerBuffer;
import org.lwjgl.openxr.XR10;
import org.lwjgl.openxr.XrAction;
import org.lwjgl.openxr.XrActionCreateInfo;
import org.lwjgl.openxr.XrActionSet;

import static org.lwjgl.system.MemoryStack.stackMallocPointer;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memUTF8;

public class HapticAction extends Action {
    public HapticAction(String name) {
        super(name, XR10.XR_ACTION_TYPE_VIBRATION_OUTPUT);
    }

    @Override
    public void createHandle(XrActionSet actionSet, OpenXRInstance instance) throws XrException {
        try (var stack = stackPush()) {
            String localizedName = "mcxr.action." + this.name;
            if (I18n.exists(localizedName)) {
                localizedName = I18n.get(localizedName);
            }

            XrActionCreateInfo actionCreateInfo = XrActionCreateInfo.malloc(stack).set(
                    XR10.XR_TYPE_ACTION_CREATE_INFO,
                    NULL,
                    memUTF8("mcxr." + this.name),
                    type,
                    0,
                    null,
                    memUTF8(localizedName)
            );
            PointerBuffer pp = stackMallocPointer(1);
            instance.check(XR10.xrCreateAction(actionSet, actionCreateInfo, pp), "xrCreateAction");
            handle = new XrAction(pp.get(), actionSet);
        }
    }
}
