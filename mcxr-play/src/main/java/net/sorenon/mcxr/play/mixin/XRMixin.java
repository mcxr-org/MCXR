package net.sorenon.mcxr.play.mixin;

import net.sorenon.mcxr.play.MCXRNativeLoad;
import org.lwjgl.openxr.XR;
import org.lwjgl.openxr.XrLoaderInitInfoAndroidKHR;
import org.lwjgl.system.FunctionProvider;
import org.lwjgl.system.MemoryStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.lwjgl.system.JNI.callPI;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memGetAddress;

@Mixin(targets = "org/lwjgl/openxr/XR$GlobalCommands")
public abstract class XRMixin {
    @Mutable
    @Shadow @Final private long xrInitializeLoaderKHR;

    @Shadow protected abstract long getFunctionAddress(String name);
    @Shadow protected abstract long getFunctionAddress(String name, boolean required);

    @Inject(method = "<init>", at = @At(value = "FIELD", target = "Lorg/lwjgl/openxr/XR$GlobalCommands;xrInitializeLoaderKHR:J"), remap = false)
    private void addAndroidInstanceInit(FunctionProvider library, CallbackInfo ci) {
        xrInitializeLoaderKHR = getFunctionAddress("xrInitializeLoaderKHR", false);
        if (xrInitializeLoaderKHR != NULL) {
            try (MemoryStack stack = stackPush()) {
                long context = memGetAddress(MCXRNativeLoad.getApplicationActivityPtr());
                long jvm = memGetAddress(MCXRNativeLoad.getJVMPtr());

                var createInfo = XrLoaderInitInfoAndroidKHR
                        .calloc(stack)
                        .type$Default()
                        .next(NULL)
                        .applicationVM(jvm)
                        .applicationContext(context);

                System.out.println("XrResult:" + callPI(createInfo.address(), xrInitializeLoaderKHR));
            }
        }
    }
}
