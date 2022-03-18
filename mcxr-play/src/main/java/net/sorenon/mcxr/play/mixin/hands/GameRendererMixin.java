package net.sorenon.mcxr.play.mixin.hands;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;
import net.sorenon.mcxr.core.JOMLUtil;
import net.sorenon.mcxr.core.MCXRCore;
import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.core.Pose;
import net.sorenon.mcxr.play.PlayOptions;
import net.sorenon.mcxr.play.input.XrInput;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Unique
    private boolean enabled() {
        return MCXRCore.getCoreConfig().controllerRaytracing() && MCXRPlayClient.MCXR_GAME_RENDERER.isXrMode();
    }

    @Inject(method = "pick", at = @At(value = "INVOKE_ASSIGN", shift = At.Shift.AFTER, target = "Lnet/minecraft/world/entity/Entity;pick(DFZ)Lnet/minecraft/world/phys/HitResult;"))
    private void overrideEntity$raycast(float tickDelta, CallbackInfo ci) {
        if (enabled()) {
            Entity entity = this.minecraft.getCameraEntity();
            Pose pose = XrInput.handsActionSet.gripPoses[MCXRPlayClient.getMainHand()].getMinecraftPose();
            Vec3 pos = JOMLUtil.convert(pose.getPos());
            Vector3f dir1 = pose.getOrientation().rotateX((float) Math.toRadians(PlayOptions.handPitchAdjust), new Quaternionf()).transform(new Vector3f(0, -1, 0));
            Vec3 dir = new Vec3(dir1.x, dir1.y, dir1.z);
            Vec3 endPos = pos.add(dir.scale(this.minecraft.gameMode.getPickRange()));
            this.minecraft.hitResult = entity.level.clip(new ClipContext(pos, endPos, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity));
        }
    }

    @ModifyVariable(method = "pick", ordinal = 0,
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/entity/Entity;getEyePosition(F)Lnet/minecraft/world/phys/Vec3;")
    )
    private Vec3 alterStartPosVec(Vec3 value) {
        if (enabled()) {
            Pose pose = XrInput.handsActionSet.gripPoses[MCXRPlayClient.getMainHand()].getMinecraftPose();
            return JOMLUtil.convert(pose.getPos());
        } else {
            return value;
        }
    }

    @ModifyVariable(method = "pick", ordinal = 1,
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/entity/Entity;getViewVector(F)Lnet/minecraft/world/phys/Vec3;")
    )
    private Vec3 alterDirVec(Vec3 value) {
        if (enabled()) {
            Pose pose = XrInput.handsActionSet.gripPoses[MCXRPlayClient.getMainHand()].getMinecraftPose();
            return JOMLUtil.convert(
                    pose.getOrientation()
                            .rotateX((float) Math.toRadians(PlayOptions.handPitchAdjust), new Quaternionf())
                            .transform(new Vector3f(0, -1, 0))
            );
        } else {
            return value;
        }
    }
}
