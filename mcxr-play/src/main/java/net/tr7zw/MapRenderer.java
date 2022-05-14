package net.tr7zw;

//from https://github.com/tr7zw/NotEnoughAnimations/blob/d2937a586f080d699861a925668f04652f80021f/NEAShared/src/main/java/dev/tr7zw/notenoughanimations/util/MapRenderer.java
// Licence for software: LICENCE-tr7zw

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public class MapRenderer {

    private static final RenderType MAP_BACKGROUND = RenderType.text(new ResourceLocation("textures/map/map_background.png"));
    private static final RenderType MAP_BACKGROUND_CHECKERBOARD = RenderType.text(new ResourceLocation("textures/map/map_background_checkerboard.png"));

    public static void renderFirstPersonMap(PoseStack matrices, MultiBufferSource vertexConsumers, int light,
                                            ItemStack stack, boolean small, boolean lefthanded) {
        Minecraft client = Minecraft.getInstance();
        if(lefthanded) {
            matrices.mulPose(Vector3f.XP.rotationDegrees(-20f));
            matrices.mulPose(Vector3f.YP.rotationDegrees(180.0f));
            matrices.mulPose(Vector3f.ZP.rotationDegrees(180.0f));
            matrices.scale(0.38f, 0.38f, 0.38f);

            matrices.translate(-1.0, -1.8, 0.1);
        } else {
            matrices.mulPose(Vector3f.XP.rotationDegrees(-20f));
            matrices.mulPose(Vector3f.YP.rotationDegrees(180.0f));
            matrices.mulPose(Vector3f.ZP.rotationDegrees(180.0f));
            matrices.scale(0.38f, 0.38f, 0.38f);

            matrices.translate(-1.0, -1.8, 0.1);
        }
        matrices.scale(0.0138125f, 0.0138125f, 0.0138125f);

        Integer integer = MapItem.getMapId(stack);
        MapItemSavedData mapState = MapItem.getSavedData(stack, client.level);
        com.mojang.blaze3d.vertex.VertexConsumer vertexConsumer = vertexConsumers
                .getBuffer(mapState == null ? MAP_BACKGROUND : MAP_BACKGROUND_CHECKERBOARD);
        Matrix4f matrix4f = matrices.last().pose();
        vertexConsumer.vertex(matrix4f, -7.0f, 135.0f, 0.0f).color(255, 255, 255, 255).uv(0.0f, 1.0f).uv2(light)
                .endVertex();
        vertexConsumer.vertex(matrix4f, 135.0f, 135.0f, 0.0f).color(255, 255, 255, 255).uv(1.0f, 1.0f).uv2(light)
                .endVertex();
        vertexConsumer.vertex(matrix4f, 135.0f, -7.0f, 0.0f).color(255, 255, 255, 255).uv(1.0f, 0.0f).uv2(light)
                .endVertex();
        vertexConsumer.vertex(matrix4f, -7.0f, -7.0f, 0.0f).color(255, 255, 255, 255).uv(0.0f, 0.0f).uv2(light)
                .endVertex();
        // mirrored back site
        vertexConsumer = vertexConsumers
                .getBuffer(MAP_BACKGROUND);
        vertexConsumer.vertex(matrix4f, -7.0f, -7.0f, 0.0f).color(255, 255, 255, 255).uv(0.0f, 0.0f).uv2(light)
                .endVertex();
        vertexConsumer.vertex(matrix4f, 135.0f, -7.0f, 0.0f).color(255, 255, 255, 255).uv(1.0f, 0.0f).uv2(light)
                .endVertex();
        vertexConsumer.vertex(matrix4f, 135.0f, 135.0f, 0.0f).color(255, 255, 255, 255).uv(1.0f, 1.0f).uv2(light)
                .endVertex();
        vertexConsumer.vertex(matrix4f, -7.0f, 135.0f, 0.0f).color(255, 255, 255, 255).uv(0.0f, 1.0f).uv2(light)
                .endVertex();

        if (mapState != null) {
            client.gameRenderer.getMapRenderer().render(matrices, vertexConsumers, integer, mapState, false, light);
        }
    }

}
