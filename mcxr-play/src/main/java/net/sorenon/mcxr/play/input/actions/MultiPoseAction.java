package net.sorenon.mcxr.play.input.actions;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.resources.language.I18n;
import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.play.openxr.OpenXRState;
import net.sorenon.mcxr.play.openxr.OpenXRInstance;
import net.sorenon.mcxr.play.openxr.OpenXRSession;
import net.sorenon.mcxr.play.openxr.XrException;
import org.lwjgl.PointerBuffer;
import org.lwjgl.openxr.*;

import java.nio.LongBuffer;

import static org.lwjgl.system.MemoryStack.stackMallocPointer;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.*;

public class MultiPoseAction extends Action implements SessionAwareAction, InputAction {

    private static final XrActionStateGetInfo getInfo = XrActionStateGetInfo.calloc().type(XR10.XR_TYPE_ACTION_STATE_GET_INFO);
    private static final XrActionStatePose state = XrActionStatePose.calloc().type(XR10.XR_TYPE_ACTION_STATE_POSE);

    public final int amount;
    public final ImmutableList<String> subactionPathsStr;
    public boolean[] isActive;
    public XrSpace[] spaces;

    private final LongBuffer subactionPaths;

    public MultiPoseAction(String name, String[] subactionPathsStr) {
        super(name, XR10.XR_ACTION_TYPE_POSE_INPUT);
        this.subactionPathsStr = ImmutableList.copyOf(subactionPathsStr);
        this.amount = subactionPathsStr.length;
        this.subactionPaths = memCallocLong(amount);
        this.isActive = new boolean[amount];
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
            instance.check(XR10.xrCreateAction(actionSet, actionCreateInfo, pp), "xrCreateAction");
            handle = new XrAction(pp.get(), actionSet);
        }
    }

    @Override
    public void createHandleSession(OpenXRSession session) throws XrException {
        try (var stack = stackPush()) {
            spaces = new XrSpace[amount];
            for (int i = 0; i < amount; i++) {
                XrActionSpaceCreateInfo action_space_info = XrActionSpaceCreateInfo.malloc(stack).set(
                        XR10.XR_TYPE_ACTION_SPACE_CREATE_INFO,
                        NULL,
                        handle,
                        subactionPaths.get(i),
                        OpenXRState.POSE_IDENTITY
                );
                PointerBuffer pp = stackMallocPointer(1);
                session.instance.check(XR10.xrCreateActionSpace(MCXRPlayClient.OPEN_XR_STATE.session.handle, action_space_info, pp), "xrCreateActionSpace");
                spaces[i] = new XrSpace(pp.get(0), session.handle);
            }
        }
    }

    @Override
    public void sync(OpenXRSession session) {
        for (int i = 0; i < amount; i++) {
            getInfo.subactionPath(subactionPaths.get(i));
            getInfo.action(handle);
            session.instance.checkPanic(XR10.xrGetActionStatePose(session.handle, getInfo, state), "xrGetActionStatePose");
            isActive[i] = state.isActive();
        }
    }

    @Override
    public void destroyHandleSession() {
        if (spaces != null) {
            for (var space : spaces) {
                XR10.xrDestroySpace(space);
            }
        }
    }

    @Override
    public void close() {
        super.close();
        memFree(subactionPaths);
    }
}
