package net.sorenon.minexraft.client.rendering;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.BlockView;
import net.sorenon.minexraft.client.MineXRaftClient;
import net.sorenon.minexraft.client.Pose;
import net.sorenon.minexraft.client.mixin.accessor.CameraAcc;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * Rather than altering Camera with mixins we instead replace the Camera instance entirely with an XrCamera instance.
 * My reasoning behind this is that this mod should have complete control of the camera. If another mod
 * wants to do alter the camera then chances are what they're doing wont translate well to an XR scenario.
 * But if they do need to alter the camera in an XR scenario then they can always mixin this class.
 */
public class XrCamera extends Camera {

    //The pose at the center of the viewspace
    private Pose viewSpacePose;

    private final Quaternionf rawRotation = new Quaternionf();

    /**
     * Called just before each render tick, sets the camera to the center of the headset for updating the sound engine and updates the pitch yaw of the player
     */
    public void updateXR(BlockView area, Entity focusedEntity, Pose viewSpacePose) {
        CameraAcc thiz = (CameraAcc) this;
        thiz.ready(focusedEntity != null);
        thiz.area(area);
        thiz.focusedEntity(focusedEntity);
        thiz.thirdPerson(false);
        thiz.inverseView(false);

        this.viewSpacePose = viewSpacePose;

        setPose(viewSpacePose, 1.0f);

        if (focusedEntity != null && MinecraftClient.getInstance().player == focusedEntity) {
            Entity player = MinecraftClient.getInstance().player;

            float yawMc = MineXRaftClient.viewSpacePose.getYaw();
            float pitchMc = MineXRaftClient.viewSpacePose.getPitch();
            float dYaw = yawMc - player.getYaw();
            float dPitch = pitchMc - player.getPitch();
            player.setYaw(yawMc);
            player.setPitch(pitchMc);
            player.prevYaw += dYaw;
            player.prevPitch = MathHelper.clamp(player.prevPitch + dPitch, -90, 90);
            if (player.getVehicle() != null) {
                player.getVehicle().onPassengerLookAround(player);
            }
        }
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
        setPose(viewSpacePose, 1.0f);
    }

    protected void setPose(Pose pose, float tickDelta) {
        rawRotation.set(pose.getOrientation());

        CameraAcc thiz = ((CameraAcc) this);

        thiz.pitch(pose.getPitch());
        thiz.yaw(pose.getYaw());
        this.getRotation().set(0.0F, 0.0F, 0.0F, 1.0F);
        this.getRotation().hamiltonProduct(Vec3f.POSITIVE_Y.getDegreesQuaternion(-pose.getYaw()));
        this.getRotation().hamiltonProduct(Vec3f.POSITIVE_X.getDegreesQuaternion(pose.getPitch()));

        Vector3f look = pose.getNormal();
        Vector3f up = rawRotation.transform(new Vector3f(0, 1, 0));
        Vector3f right = rawRotation.transform(new Vector3f(1, 0, 0));
        this.getHorizontalPlane().set(look.x, look.y, look.z);
        this.getVerticalPlane().set(up.x, up.y, up.z);
        thiz.diagonalPlane().set(right.x, right.y, right.z);

        Entity focusedEntity = getFocusedEntity();
        if (focusedEntity != null) {
            this.setPos(
                    MathHelper.lerp(tickDelta, focusedEntity.prevX, focusedEntity.getX()) + pose.getPos().x + MineXRaftClient.xrOffset.x,
                    MathHelper.lerp(tickDelta, focusedEntity.prevY, focusedEntity.getY()) + pose.getPos().y + MineXRaftClient.xrOffset.y,
                    MathHelper.lerp(tickDelta, focusedEntity.prevZ, focusedEntity.getZ()) + pose.getPos().z + MineXRaftClient.xrOffset.z
            );
        } else {
            this.setPos(
                    pose.getPos().x + MineXRaftClient.xrOffset.x,
                    pose.getPos().y + MineXRaftClient.xrOffset.y,
                    pose.getPos().z + MineXRaftClient.xrOffset.z
            );
        }
    }

    @Override
    public void update(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta) {
        CameraAcc thiz = (CameraAcc) this;
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
