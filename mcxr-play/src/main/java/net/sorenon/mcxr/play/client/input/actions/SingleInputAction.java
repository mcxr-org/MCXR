package net.sorenon.mcxr.play.client.input.actions;

import net.minecraft.client.resource.language.I18n;
import net.sorenon.mcxr.play.client.openxr.OpenXRInstance;
import net.sorenon.mcxr.play.client.openxr.XrException;
import org.lwjgl.PointerBuffer;
import org.lwjgl.openxr.*;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.system.MemoryStack.stackMallocPointer;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memUTF8;

public abstract class SingleInputAction<T> extends Action {

    protected static final XrActionStateGetInfo getInfo = XrActionStateGetInfo.calloc().type(XR10.XR_TYPE_ACTION_STATE_GET_INFO);

    public T currentState;
    public boolean changedSinceLastSync;
    public long lastChangeTime;
    public boolean isActive;

    public SingleInputAction(String name, int type) {
        super(name, type);
    }

    @Override
    public void createHandle(XrActionSet actionSet, OpenXRInstance instance) throws XrException {
        try (MemoryStack ignored = stackPush()) {
            String localizedName = "mcxr.action." + name;
            if (I18n.hasTranslation(localizedName)) {
                localizedName = I18n.translate(localizedName);
            }

            XrActionCreateInfo actionCreateInfo = XrActionCreateInfo.mallocStack().set(
                    XR10.XR_TYPE_ACTION_CREATE_INFO,
                    NULL,
                    memUTF8(name),
                    type,
                    0,
                    null,
                    memUTF8(localizedName)
            );
            PointerBuffer pp = stackMallocPointer(1);
            instance.checkSafe(XR10.xrCreateAction(actionSet, actionCreateInfo, pp), "xrCreateAction");
            handle = new XrAction(pp.get(), actionSet);
        }
    }
}
