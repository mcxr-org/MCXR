package net.sorenon.mcxr.play.input.actionsets;

import net.minecraft.client.resources.language.I18n;
import net.sorenon.mcxr.play.input.actions.Action;
import net.sorenon.mcxr.play.input.actions.InputAction;
import net.sorenon.mcxr.play.openxr.OpenXRInstance;
import net.sorenon.mcxr.play.openxr.OpenXRSession;
import net.sorenon.mcxr.play.openxr.XrException;
import org.lwjgl.PointerBuffer;
import org.lwjgl.openxr.XR10;
import org.lwjgl.openxr.XrActionSet;
import org.lwjgl.openxr.XrActionSetCreateInfo;
import oshi.util.tuples.Pair;

import java.util.HashMap;
import java.util.List;

import static org.lwjgl.system.MemoryStack.stackCallocPointer;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.*;

public abstract class ActionSet implements AutoCloseable {

    public final String name;
    private XrActionSet handle;
    private int priority;

    public ActionSet(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    public abstract List<Action> actions();

    public boolean shouldSync() {
        return true;
    }

    public abstract void getDefaultBindings(HashMap<String, List<Pair<Action, String>>> map);

    public void sync(OpenXRSession session) {
        for (var action : actions()) {
            if (action instanceof InputAction inputAction) {
                inputAction.sync(session);
            }
        }
    }

    public final void createHandle(OpenXRInstance instance) throws XrException {
        try (var stack = stackPush()) {
            String localizedName = "mcxr.actionset." + this.name;
            if (I18n.exists(localizedName)) {
                localizedName = I18n.get(localizedName);
            }

            XrActionSetCreateInfo actionSetCreateInfo = XrActionSetCreateInfo.calloc(stack).set(XR10.XR_TYPE_ACTION_SET_CREATE_INFO,
                    NULL,
                    memUTF8("mcxr." + this.name),
                    memUTF8(I18n.get(localizedName)),
                    priority
            );
            PointerBuffer pp = stackCallocPointer(1);
            instance.check(XR10.xrCreateActionSet(instance.handle, actionSetCreateInfo, pp), "xrCreateActionSet");
            handle = new XrActionSet(pp.get(0), instance.handle);

            for (var action : actions()) {
                action.createHandle(handle, instance);
            }
        }
    }

    public final XrActionSet getHandle() {
        return handle;
    }

    public final void destroyHandles() {
        if (handle != null) {
            XR10.xrDestroyActionSet(handle);
        }
    }

    @Override
    public final void close() {
        destroyHandles();
        for (var action : actions()) {
            action.close();
        }
    }
}
