package net.sorenon.minexraft;

import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.sorenon.minexraft.mixin.accessor.CameraExt;
import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.openxr.XrPosef;

public class XrCamera extends Camera {

    private Pose headPose;

    private final Quaternionf rawRotation = new Quaternionf();

    /**
     * Called just before each render tick, sets the camera to the center of the headset for updating the sound engine
     */
    public void updateXR(BlockView area, Entity focusedEntity, Pose viewPose) {
        CameraExt thiz = (CameraExt) this;
        thiz.ready(true);
        thiz.area(area);
        thiz.focusedEntity(focusedEntity);
        thiz.thirdPerson(false);
        thiz.inverseView(false);

        headPose = viewPose;

        setPose(viewPose, 1.0f);
    }

    /**
     * Called just before each frame
     */
    public void setEyePose(Pose pose, float tickDelta) {
        setPose(
                pose,
                tickDelta
        );
    }

    /**
     * Called just after each frame
     */
    public void popEyePose() {
        setPose(headPose, 1.0f);
    }

    protected void setPose(Pose pose, float tickDelta) {
        rawRotation.set(pose.getOrientation());

        Entity focusedEntity = getFocusedEntity();
        if (focusedEntity != null) {
            CameraExt thiz = ((CameraExt) this);

            thiz.pitch(pose.getPitch());
            thiz.yaw(pose.getYaw());
            this.getRotation().set(0.0F, 0.0F, 0.0F, 1.0F);
            this.getRotation().hamiltonProduct(net.minecraft.client.util.math.Vector3f.POSITIVE_Y.getDegreesQuaternion(-pose.getYaw()));
            this.getRotation().hamiltonProduct(net.minecraft.client.util.math.Vector3f.POSITIVE_X.getDegreesQuaternion(pose.getPitch()));

            Vector3f look = pose.getNormal();
            Vector3f up = rawRotation.transform(new Vector3f(0, 1, 0));
            Vector3f right = rawRotation.transform(new Vector3f(1, 0, 0));
            this.getHorizontalPlane().set(look.x, look.y, look.z);
            this.getVerticalPlane().set(up.x, up.y, up.z);
            thiz.diagonalPlane().set(right.x, right.y, right.z);

            this.setPos(
                    MathHelper.lerp(tickDelta, focusedEntity.prevX, focusedEntity.getX()) + pose.getPos().x + MineXRaftClient.xrOffset.x,
                    MathHelper.lerp(tickDelta, focusedEntity.prevY, focusedEntity.getY()) + pose.getPos().y + MineXRaftClient.xrOffset.y,
                    MathHelper.lerp(tickDelta, focusedEntity.prevZ, focusedEntity.getZ()) + pose.getPos().z + MineXRaftClient.xrOffset.z
            );
        }
    }

    @Override
    public void update(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta) {
        CameraExt thiz = (CameraExt) this;
        thiz.ready(true);
        thiz.area(area);
        thiz.focusedEntity(focusedEntity);
        thiz.thirdPerson(false);
        thiz.inverseView(false);
    }

    public Quaternion getRawRotationInverted() {
        Quaternionf inv = rawRotation.invert(new Quaternionf());

        return new Quaternion(inv.x, inv.y, inv.z, inv.w);
    }
}
