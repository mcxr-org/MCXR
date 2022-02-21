package net.sorenon.mcxr.play.rendering;

import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.server.packs.resources.ResourceManager;

public class UnownedTexture extends AbstractTexture {

    public UnownedTexture(int glID) {
        this.id = glID;
    }

    @Override
    public void load(ResourceManager manager) {

    }

    @Override
    public void releaseId() {

    }
}
