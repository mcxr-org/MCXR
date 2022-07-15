package net.sorenon.mcxr.core.config;

public class MCXRCoreConfigImpl implements MCXRCoreConfig {

    public boolean xrEnabled;

    @Override
    public boolean supportsMCXR() {
        return xrEnabled;
    }

    @Override
    public boolean dynamicPlayerHeight() {
        return true;
    }

    @Override
    public boolean dynamicPlayerEyeHeight() {
        return true;
    }

    @Override
    public boolean thinnerPlayerBoundingBox() {
        return true;
    }

    @Override
    public boolean controllerRaytracing() {
        return true;
    }

    @Override
    public boolean roomscaleMovement() {
        return false;
    }

    @Override
    public boolean handProjectiles() {
        return true;
    }
}
