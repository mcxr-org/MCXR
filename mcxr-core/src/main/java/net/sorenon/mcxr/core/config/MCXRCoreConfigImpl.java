package net.sorenon.mcxr.core.config;

public class MCXRCoreConfigImpl implements MCXRCoreConfig {

    public boolean xrAllowed;

    @Override
    public boolean xrAllowed() {
        return xrAllowed;
    }

    @Override
    public boolean dynamicPlayerHeight() {
        return xrAllowed;
    }

    @Override
    public boolean dynamicPlayerEyeHeight() {
        return xrAllowed;
    }

    @Override
    public boolean thinnerPlayerBoundingBox() {
        return xrAllowed;
    }

    @Override
    public boolean controllerRaytracing() {
        return true;
    }
}
