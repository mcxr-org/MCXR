package net.sorenon.mcxr.play.compat.iris.mixin;

import net.minecraft.client.Camera;
import net.minecraft.world.phys.Vec3;
import net.sorenon.mcxr.core.JOMLUtil;
import net.sorenon.mcxr.play.MCXRPlayClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings("UnusedMixin")
@Mixin(targets = "net/coderbot/iris/uniforms/CameraUniforms", remap = false)
@Pseudo
public class MixinCameraUniforms113 {

    @SuppressWarnings("UnresolvedMixinReference")
    @Redirect(method = "getCameraPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;getPosition()Lnet/minecraft/world/phys/Vec3;"))
    private static Vec3 mcxr$getPosition(Camera camera) {
        return JOMLUtil.convert(MCXRPlayClient.viewSpacePoses.getGamePose().getPos());
    }
}
