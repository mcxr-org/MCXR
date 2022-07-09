package net.sorenon.mcxr.core.config;

public class MCXRCoreConfigImpl implements MCXRCoreConfig {

    public boolean xrEnabled;

    @Override
    public boolean supportsMCXR() {
        return xrEnabled;
    }

    @Override
    public boolean dynamicPlayerHeight() {
        return false;
    }

    @Override
    public boolean dynamicPlayerEyeHeight() {
        return false;
    }

    @Override
    public boolean thinnerPlayerBoundingBox() {
        return false;
    }

    @Override
    public boolean controllerRaytracing() {
        return true;
    }

    @Override
    public boolean roomscaleMovement() {
        return false;
    }
}
