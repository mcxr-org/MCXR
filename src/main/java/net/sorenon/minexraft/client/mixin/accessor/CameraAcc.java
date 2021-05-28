package net.sorenon.minexraft.client.mixin.accessor;

import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Camera.class)
public interface CameraAcc {

    @Accessor("ready")
    void ready(boolean ready);

    @Accessor("area")
    void area(BlockView area);

    @Accessor("focusedEntity")
    void focusedEntity(Entity entity);

    @Accessor("thirdPerson")
    void thirdPerson(boolean thirdPerson);

    @Accessor("thirdPerson")
    void inverseView(boolean inverseView);

    @Accessor("pitch")
    void pitch(float pitch);

    @Accessor("yaw")
    void yaw(float yaw);

    @Accessor("diagonalPlane")
    Vec3f diagonalPlane();
}
