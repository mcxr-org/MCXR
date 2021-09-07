package net.sorenon.mcxr.play.rendering;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.BlockView;
import net.sorenon.mcxr.core.JOMLUtil;
import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.core.Pose;
import net.sorenon.mcxr.play.mixin.accessor.CameraAcc;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * Rather than altering Camera with mixins we instead replace the Camera instance entirely with an XrCamera instance.
 * My reasoning behind this is that this mod should have complete control of the camera. If another mod
 * wants to do alter the camera then chances are what they're doing wont translate well to an XR scenario.
 * But if they do need to alter the camera in an XR scenario then they can always mixin this class.
 */
public class XrCamera extends Camera {

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

        setPose(viewSpacePose);

        if (focusedEntity != null && MinecraftClient.getInstance().player == focusedEntity) {
            Entity player = MinecraftClient.getInstance().player;

            float yaw = MCXRPlayClient.viewSpacePoses.getScaledPhysicalPose().getMCYaw();
            float pitch = MCXRPlayClient.viewSpacePoses.getScaledPhysicalPose().getMCPitch();
            float dYaw = yaw - player.getYaw();
            float dPitch = pitch - player.getPitch();
            player.setYaw(yaw);
            player.setPitch(pitch);
            player.prevYaw += dYaw;
            player.prevPitch = MathHelper.clamp(player.prevPitch + dPitch, -90, 90);
            if (player.getVehicle() != null) {
                player.getVehicle().onPassengerLookAround(player);
            }
        }
    }

    public void setPose(Pose pose) {
        rawRotation.set(pose.getOrientation());

        CameraAcc thiz = ((CameraAcc) this);

        thiz.pitch(pose.getMCPitch());
        thiz.yaw(pose.getMCYaw());
        this.getRotation().set(0.0F, 0.0F, 0.0F, 1.0F);
        this.getRotation().hamiltonProduct(Vec3f.POSITIVE_Y.getDegreesQuaternion(-pose.getMCYaw()));
        this.getRotation().hamiltonProduct(Vec3f.POSITIVE_X.getDegreesQuaternion(pose.getMCPitch()));

        Vector3f look = rawRotation.transform(new Vector3f(0, 0, -1));
        Vector3f up = rawRotation.transform(new Vector3f(0, 1, 0));
        Vector3f right = rawRotation.transform(new Vector3f(1, 0, 0));
        this.getHorizontalPlane().set(look.x, look.y, look.z);
        this.getVerticalPlane().set(up.x, up.y, up.z);
        thiz.diagonalPlane().set(right.x, right.y, right.z);

        this.setPos(JOMLUtil.convert(pose.getPos()));
    }

    @Override
    public void update(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta) {
        CameraAcc thiz = (CameraAcc) this;
        thiz.ready(true);
        thiz.area(area);
        thiz.focusedEntity(focusedEntity);
        thiz.thirdPerson(false);
    }

    public Quaternion getRawRotationInverted() {
        Quaternionf inv = rawRotation.invert(new Quaternionf());

        return new Quaternion(inv.x, inv.y, inv.z, inv.w);
    }
}
