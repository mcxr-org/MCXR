package net.sorenon.minexraft.mixin.accessor;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityExt {

    @Accessor("pitch")
    float pitch();

    @Accessor("yaw")
    float yaw();

    @Accessor("prevPitch")
    float prevPitch();

    @Accessor("prevYaw")
    float prevYaw();

    @Accessor("pitch")
    void pitch(float pitch);

    @Accessor("yaw")
    void yaw(float yaw);

    @Accessor("prevPitch")
    void prevPitch(float prevPitch);

    @Accessor("prevYaw")
    void prevYaw(float prevYaw);
}
