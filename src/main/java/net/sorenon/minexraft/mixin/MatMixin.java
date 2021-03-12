package net.sorenon.minexraft.mixin;

import net.minecraft.util.math.Matrix4f;
import net.sorenon.minexraft.HelloOpenXR;
import net.sorenon.minexraft.MineXRaftClient;
import net.sorenon.minexraft.accessor.MatAccessor;
import org.joml.Math;
import org.lwjgl.BufferUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.nio.FloatBuffer;

@Mixin(Matrix4f.class)
public abstract class MatMixin implements MatAccessor {

    @Shadow
    protected float a00;

    @Shadow
    protected float a01;

    @Shadow
    protected float a02;

    @Shadow
    protected float a03;

    @Shadow
    protected float a13;

    @Shadow
    protected float a12;

    @Shadow
    protected float a11;

    @Shadow
    protected float a10;

    @Shadow
    protected float a20;

    @Shadow
    protected float a21;

    @Shadow
    protected float a22;

    @Shadow
    protected float a23;

    @Shadow
    protected float a30;

    @Shadow
    protected float a31;

    @Shadow
    protected float a32;

    @Shadow
    protected float a33;

    @Shadow public abstract void writeToBuffer(FloatBuffer floatBuffer);

    //??????
    //major minor order seams to be wrong
    //TODO fix this on yarn? not sure if this is intentional
    @Override
    public void proj() {
        org.joml.Matrix4f proj = new org.joml.Matrix4f();
        HelloOpenXR.createProjectionFov(proj, MineXRaftClient.fov, 0.1f, 100f);

        a00 = proj.m00();
        a10 = proj.m01();
        a20 = proj.m02();
        a30 = proj.m03();
        a01 = proj.m10();
        a11 = proj.m11();
        a21 = proj.m12();
        a31 = proj.m13();
        a02 = proj.m20();
        a12 = proj.m21();
        a22 = proj.m22();
        a32 = proj.m23();
        a03 = proj.m30();
        a13 = proj.m31();
        a23 = proj.m32();
        a33 = proj.m33();
//        {
//            float tanLeft        = (float) Math.tan(MineXRaftClient.fov.angleLeft());
//            float tanRight       = (float) Math.tan(MineXRaftClient.fov.angleRight());
//            float tanDown        = (float) Math.tan(MineXRaftClient.fov.angleDown());
//            float tanUp          = (float) Math.tan(MineXRaftClient.fov.angleUp());
//            float tanAngleWidth  = tanRight - tanLeft;
//            float tanAngleHeight = tanUp - tanDown;
//            float nearZ = 0.1f;
//            float farZ = 100f;
//            a00 = 2.0f / tanAngleWidth;
//            a11 = 2.0f / tanAngleHeight;
//            a02 = (tanRight + tanLeft) / tanAngleWidth;
//            a12 = (tanUp + tanDown) / tanAngleHeight;
//            a22 = (farZ + nearZ) / (nearZ - farZ);
//            a32 = -1.0f;
//            a23 = 2.0F * farZ * nearZ / (nearZ - farZ);
//        }

//        Matrix4f other = Matrix4f.viewboxMatrix(MineXRaftClient.fov.angleRight() * 2, 1, 0.1f, 100f);
//
//        FloatBuffer out1 = BufferUtils.createFloatBuffer(16);
////        other.writeToBuffer(out1);
//        proj.get(out1);
//        FloatBuffer out2 = BufferUtils.createFloatBuffer(16);
//        writeToBuffer(out2);
//        for (int i = 0; i < 16; i++){
//            System.out.println(out1.get(i) + " " + out2.get(i));
//        }
    }
}
