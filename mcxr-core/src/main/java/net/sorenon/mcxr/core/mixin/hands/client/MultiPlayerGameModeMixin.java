package net.sorenon.mcxr.core.mixin.hands.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.sorenon.mcxr.core.MCXRCore;
import net.sorenon.mcxr.core.accessor.PlayerExt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

    @Inject(method = "useItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;useOn(Lnet/minecraft/world/item/context/UseOnContext;)Lnet/minecraft/world/InteractionResult;"))
    void preUse(LocalPlayer player, ClientLevel world, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        PlayerExt playerExt = ((PlayerExt) player);
        if (playerExt.isXR() && MCXRCore.getCoreConfig().handBasedItemUsage()) {
            playerExt.getOverrideTransform().set(MCXRCore.handToArm(player, hand));
        }
    }

    @Inject(method = "useItemOn", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/world/item/ItemStack;useOn(Lnet/minecraft/world/item/context/UseOnContext;)Lnet/minecraft/world/InteractionResult;"))
    void postUse(LocalPlayer player, ClientLevel world, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        PlayerExt playerExt = ((PlayerExt) player);
        if (playerExt.isXR()) {
            playerExt.getOverrideTransform().set(null);
        }
    }
}
