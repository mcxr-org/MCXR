package net.sorenon.mcxr.play.client.rendering;

import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.resource.ResourceManager;

import java.io.IOException;

public class ExistingTexture extends AbstractTexture {

    public ExistingTexture(int glID) {
        this.glId = glID;
    }

    @Override
    public void load(ResourceManager manager) throws IOException {

    }

    @Override
    public void clearGlId() {

    }
}
