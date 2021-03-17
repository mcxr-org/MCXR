package net.sorenon.minexraft;

import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import net.sorenon.minexraft.accessor.CameraExt;
import org.joml.Math;
import org.lwjgl.openxr.XrQuaternionf;

//Unused for now
public class XrCamera extends Camera {

    @Override
    public void update(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta) {
        super.update(area, focusedEntity, thirdPerson, inverseView, tickDelta);
    }

    @Override
    protected void setRotation(float _f, float _f2) {
        if (MineXRaftClient.eyePose != null) {
            XrQuaternionf quat = MineXRaftClient.eyePose.orientation();
            float invNorm = 1.0F / (quat.x() * quat.x() + quat.y() * quat.y() + quat.z() * quat.z() + quat.w() * quat.w());
            float x = -quat.x() * invNorm;
            float y = -quat.y() * invNorm;
            float z = -quat.z() * invNorm;
            float w = quat.w() * invNorm;

            this.getRotation().set(x, y, z, w);
            this.getHorizontalPlane().set(0.0F, 0.0F, 1.0F);
            this.getHorizontalPlane().rotate(this.getRotation());
            this.getVerticalPlane().set(0.0F, 1.0F, 0.0F);
            this.getVerticalPlane().rotate(this.getRotation());

            CameraExt thiz = (CameraExt) this;
            thiz.getDiagonalPlane().set(1.0F, 0.0F, 0.0F);
            thiz.getDiagonalPlane().rotate(this.getRotation());

            //TODO check if this is crap
            float yaw = (float) Math.atan2(2.0D * (double)(x * w - y * z), 1.0D - 2.0D * (double)(x * x + y * y));
            float pitch = (float)Math.asin(2.0D * (double)(x * z + y * w));
            float roll = (float)Math.atan2(2.0D * (double)(z * w - x * y), 1.0D - 2.0D * (double)(y * y + z * z));
            thiz.setYaw(yaw);
            thiz.setPitch(-pitch);
        } else {
            super.setRotation(_f, _f2);
        }
    }

    @Override
    public float getPitch() {
        return super.getPitch();
    }

    @Override
    public float getYaw() {
        return super.getYaw();
    }
}
