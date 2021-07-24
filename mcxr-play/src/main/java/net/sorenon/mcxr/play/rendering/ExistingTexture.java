package net.sorenon.mcxr.play.rendering;

import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.resource.ResourceManager;

import java.io.IOException;

public class ExistingTexture extends AbstractTexture {

    public ExistingTexture(int glID) {
        this.glId = glID;
    }

    @Override
    public void load(ResourceManager manager) {

    }

    @Override
    public void clearGlId() {

    }
}
