package net.sorenon.mcxr.play.rendering;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class RenderTypeBuilder {

    private final String name;
    private final VertexFormat format;
    private final VertexFormat.Mode drawMode;
    private final int expectedBufferSize;
    private final boolean affectsCrumbling;
    private final boolean sortOnUpload;

    public final RenderType.CompositeState.CompositeStateBuilder innerBuilder;

    public RenderTypeBuilder(ResourceLocation name, VertexFormat format, VertexFormat.Mode drawMode, int expectedBufferSize, boolean affectsCrumbling, boolean sortOnUpload) {
        this.name = "mcxr_" + name.toString();
        this.format = format;
        this.drawMode = drawMode;
        this.expectedBufferSize = expectedBufferSize;
        this.affectsCrumbling = affectsCrumbling;
        this.sortOnUpload = sortOnUpload;
        this.innerBuilder = RenderType.CompositeState.builder();
    }

    public RenderType.CompositeRenderType build(boolean affectsOutline) {
        return createInner(name, format, drawMode, expectedBufferSize, affectsCrumbling, sortOnUpload, innerBuilder.createCompositeState(affectsOutline));
    }

    public static RenderType.CompositeRenderType createInner(String name, VertexFormat format, VertexFormat.Mode drawMode, int expectedBufferSize, boolean affectsCrumbling, boolean sortOnUpload, RenderType.CompositeState compositeState) {
        return RenderType.create(name, format, drawMode, expectedBufferSize, affectsCrumbling, sortOnUpload, compositeState);
    }
}
