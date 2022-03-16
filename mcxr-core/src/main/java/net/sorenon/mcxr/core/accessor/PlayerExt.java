package net.sorenon.mcxr.core.accessor;

import net.minecraft.world.InteractionHand;
import net.sorenon.mcxr.core.Pose;

public interface PlayerExt {

    Pose getHeadPose();

    Pose getLeftHandPose();

    Pose getRightHandPose();

    void setIsXr(boolean isXr);

    boolean isXR();

    ThreadLocal<InteractionHand> getOverrideTransform();
}
