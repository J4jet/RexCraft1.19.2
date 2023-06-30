package net.jrex.rexcraft.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.entity.custom.CricketEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

public class CricketRenderer extends GeoEntityRenderer<CricketEntity> {
    public CricketRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CricketModel());
        this.shadowRadius = 0.2f;
    }

    @Override
    public ResourceLocation getTextureLocation(CricketEntity instance) {
        return new ResourceLocation(RexCraft.MOD_ID, "textures/entity/cricket_entity.png");
    }

    @Override
    public RenderType getRenderType(CricketEntity animatable, float partialTicks, PoseStack stack,
                                    @Nullable MultiBufferSource renderTypeBuffer,
                                    @Nullable VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        if(animatable.isBaby()){
            stack.scale(0.1f, 0.1f, 0.1f);
        }
        else {
            stack.scale(0.2f, 0.2f, 0.2f);
        }
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }


}
