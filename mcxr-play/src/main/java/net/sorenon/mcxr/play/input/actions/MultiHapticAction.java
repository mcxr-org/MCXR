package net.sorenon.mcxr.play.input.actions;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.resources.language.I18n;
import net.sorenon.mcxr.play.openxr.OpenXRInstance;
import net.sorenon.mcxr.play.openxr.XrException;
import org.lwjgl.PointerBuffer;
import org.lwjgl.openxr.*;

import java.nio.LongBuffer;

import static org.lwjgl.system.MemoryStack.stackMallocPointer;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.*;

public class MultiHapticAction extends Action {
    public final int amount;
    public final ImmutableList<String> subactionPathsStr;

    public final LongBuffer subactionPaths;

    public MultiHapticAction(String name, String[] subactionPathsStr) {
        super(name, XR10.XR_ACTION_TYPE_VIBRATION_OUTPUT);
        this.subactionPathsStr = ImmutableList.copyOf(subactionPathsStr);
        this.amount = subactionPathsStr.length;
        this.subactionPaths = memCallocLong(amount);
    }

    @Override
    public void createHandle(XrActionSet actionSet, OpenXRInstance instance) throws XrException {
        try (var stack = stackPush()) {

            for (int i = 0; i < amount; i++) {
                var str = subactionPathsStr.get(i);
                subactionPaths.put(i, instance.getPath(str));
            }

            String localizedName = "mcxr.action." + this.name;
            if (I18n.exists(localizedName)) {
                localizedName = I18n.get(localizedName);
            }

            XrActionCreateInfo actionCreateInfo = XrActionCreateInfo.malloc(stack).set(
                    XR10.XR_TYPE_ACTION_CREATE_INFO,
                    NULL,
                    memUTF8("mcxr." + this.name),
                    type,
                    amount,
                    subactionPaths,
                    memUTF8(localizedName)
            );
            PointerBuffer pp = stackMallocPointer(1);
            instance.checkSafe(XR10.xrCreateAction(actionSet, actionCreateInfo, pp), "xrCreateAction");
            handle = new XrAction(pp.get(), actionSet);
        }
    }
}
