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
import org.lwjgl.openxr.XrPosef;

public class XrCamera extends Camera {

    private Vec3d headPos;
    private Quaternionf headRot;

    private final Quaternionf quat = new Quaternionf();

    private final Quaternionf rawRotation = new Quaternionf();

    /**
     * Called just before each render tick, sets the camera to the center of the headset for sounds
     */
    public void updateXR(BlockView area, Entity focusedEntity, Pose viewPose) {
        CameraExt thiz = (CameraExt) this;
        thiz.ready(true);
        thiz.area(area);
        thiz.focusedEntity(focusedEntity);
        thiz.thirdPerson(false);
        thiz.inverseView(false);

        headPos = viewPose.getPosMc();
        headRot = viewPose.getOrientation();
        thiz.pitch(viewPose.getPitch());
        thiz.yaw(viewPose.getYaw());

        setPose(headPos, headRot, 1.0f);
    }

    /**
     * Called just before each frame
     */
    public void setEyePose(XrPosef pose, float tickDelta) {
        setPose(
                new Vec3d(pose.position$().x(), pose.position$().y(), pose.position$().z()),
                new Quaternionf(pose.orientation().x(), pose.orientation().y(), pose.orientation().z(), pose.orientation().w()),
                tickDelta
        );
    }

    /**
     * Called just after each frame
     */
    public void popEyePose() {
        setPose(headPos, headRot, 1.0f);
    }

    protected void setPose(Vec3d viewPos, Quaternionf viewRot, float tickDelta) {
        rawRotation.set(viewRot);

//        viewRot.rotateX((float) Math.PI, quat);
//        this.getRotation().set(quat.x, quat.y, quat.z, quat.w);
//
//        this.getHorizontalPlane().set(0.0F, 0.0F, 1.0F);
//        this.getHorizontalPlane().rotate(this.getRotation());
//        this.getVerticalPlane().set(0.0F, 1.0F, 0.0F);
//        this.getVerticalPlane().rotate(this.getRotation());
//
//        thiz.diagonalPlane().set(1.0F, 0.0F, 0.0F);
//        thiz.diagonalPlane().rotate(this.getRotation());

        Entity focusedEntity = getFocusedEntity();
        if (focusedEntity != null) {
            this.setRotation(focusedEntity.getYaw(tickDelta), focusedEntity.getPitch(tickDelta));//TODO make this not bad

            this.setPos(
                    MathHelper.lerp(tickDelta, focusedEntity.prevX, focusedEntity.getX()) + viewPos.x,
                    MathHelper.lerp(tickDelta, focusedEntity.prevY, focusedEntity.getY()) + viewPos.y,
                    MathHelper.lerp(tickDelta, focusedEntity.prevZ, focusedEntity.getZ()) + viewPos.z
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
        rawRotation.invert(quat);

        return new Quaternion(quat.x, quat.y, quat.z, quat.w);
    }
}
