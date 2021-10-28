package net.sorenon.mcxr.play.mixin;

import net.minecraft.util.SystemDetails;
import net.sorenon.mcxr.play.MCXRPlayClient;
import net.sorenon.mcxr.play.openxr.OpenXRInstance;
import net.sorenon.mcxr.play.openxr.OpenXRSession;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SystemDetails.class)
public abstract class SystemDetailsMixin {

    @Shadow
    public abstract void addSection(String name, String value);

    @Inject(method = "<init>", at = @At("RETURN"))
    void appendMCXR(CallbackInfo ci) {
        OpenXRInstance instance = MCXRPlayClient.OPEN_XR.instance;
        OpenXRSession session = MCXRPlayClient.OPEN_XR.session;

        if (instance == null) {
            this.addSection("XR", "No instance");
        } else {
            this.addSection("XR", "Instance created");
            this.addSection("Runtime Name", instance.runtimeName);
            this.addSection("Runtime Version", instance.runtimeVersionString);

            if (session == null) {
                this.addSection("XR", "Session not running");
            } else {
                this.addSection("XR", "Session running");
                this.addSection("Headset Name", session.system.systemName);
                this.addSection("Headset Vendor", String.valueOf(session.system.vendor));
                this.addSection("Headset Orientation Tracking", String.valueOf(session.system.orientationTracking));
                this.addSection("Headset Position Tracking", String.valueOf(session.system.positionTracking));
                this.addSection("Headset Max Width", String.valueOf(session.system.maxWidth));
                this.addSection("Headset Max Height", String.valueOf(session.system.maxHeight));
                this.addSection("Headset Max Layer Count", String.valueOf(session.system.maxLayerCount));
            }
        }
    }
}
