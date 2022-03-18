package net.sorenon.mcxr.core.config;

public class MCXRCoreConfigImpl implements MCXRCoreConfig {

    public boolean xrEnabled;

    @Override
    public boolean supportsMCXR() {
        return xrEnabled;
    }

    @Override
    public boolean dynamicPlayerHeight() {
        return xrEnabled;
    }

    @Override
    public boolean dynamicPlayerEyeHeight() {
        return xrEnabled;
    }

    @Override
    public boolean thinnerPlayerBoundingBox() {
        return xrEnabled;
    }

    @Override
    public boolean controllerRaytracing() {
        return true;
    }

    @Override
    public boolean roomscaleMovement() {
        return true;
    }
}
