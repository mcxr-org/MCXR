package net.sorenon.mcxr.play.client.input.actions;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.resource.language.I18n;
import net.sorenon.mcxr.play.client.MCXRPlayClient;
import net.sorenon.mcxr.play.client.openxr.OpenXR;
import net.sorenon.mcxr.play.client.openxr.OpenXRInstance;
import net.sorenon.mcxr.play.client.openxr.OpenXRSession;
import net.sorenon.mcxr.play.client.openxr.XrException;
import org.lwjgl.PointerBuffer;
import org.lwjgl.openxr.*;
import org.lwjgl.system.MemoryStack;

import java.nio.LongBuffer;

import static org.lwjgl.system.MemoryStack.stackMallocPointer;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.*;

public class MultiPoseAction extends Action implements SessionAwareAction {

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
        try (MemoryStack ignored = stackPush()) {

            for (int i = 0; i < amount; i++) {
                var str = subactionPathsStr.get(i);
                subactionPaths.put(i, instance.getPath(str));
            }

            String localizedName = "mcxr.action." + name;
            if (I18n.hasTranslation(localizedName)) {
                localizedName = I18n.translate(localizedName);
            }

            XrActionCreateInfo actionCreateInfo = XrActionCreateInfo.mallocStack().set(
                    XR10.XR_TYPE_ACTION_CREATE_INFO,
                    NULL,
                    memUTF8(name),
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

    @Override
    public void createHandleSession(OpenXRSession session) throws XrException {
        try (var ignored = stackPush()) {
            spaces = new XrSpace[amount];
            for (int i = 0; i < amount; i++) {
                XrActionSpaceCreateInfo action_space_info = XrActionSpaceCreateInfo.mallocStack().set(
                        XR10.XR_TYPE_ACTION_SPACE_CREATE_INFO,
                        NULL,
                        handle,
                        subactionPaths.get(i),
                        OpenXR.identityPose
                );
                PointerBuffer pp = stackMallocPointer(1);
                session.instance.checkSafe(XR10.xrCreateActionSpace(MCXRPlayClient.OPEN_XR.session.handle, action_space_info, pp), "xrCreateActionSpace");
                spaces[i] = new XrSpace(pp.get(0), session.handle);
            }
        }
    }

    @Override
    public void sync(OpenXRSession session) {
        OpenXR xr = MCXRPlayClient.OPEN_XR;
        XrSession xrSession = xr.session.handle;

        for (int i = 0; i < amount; i++) {
            getInfo.subactionPath(subactionPaths.get(i));
            getInfo.action(handle);
            xr.check(XR10.xrGetActionStatePose(xrSession, getInfo, state));
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
