package net.jrex.rexcraft.entity.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.entity.custom.DiploEntity;
import net.jrex.rexcraft.entity.variant.DiploVariant;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;


public class DiploRenderer extends GeoEntityRenderer<DiploEntity> {

    public static final Map<DiploVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(DiploVariant.class), (p_114874_) -> {
                p_114874_.put(DiploVariant.F,
                        new ResourceLocation(RexCraft.MOD_ID, "textures/entity/diplo/diplo_f.png"));
                p_114874_.put(DiploVariant.M,
                        new ResourceLocation(RexCraft.MOD_ID, "textures/entity/diplo/diplo_m.png"));

            });
    public DiploRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DiploModel());
        this.shadowRadius = 4.8f;
    }



    @Override
    public ResourceLocation getTextureLocation(DiploEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());

    }

    @Override
    public RenderType getRenderType(DiploEntity animatable, float partialTicks, PoseStack stack,
                                    @Nullable MultiBufferSource renderTypeBuffer,
                                    @Nullable VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        //Entity Size!
        if(animatable.isBaby()) {
            stack.scale(0.5F, 0.5F, 0.5F);
        } else {
            stack.scale(1.0f, 1.0f,1.0f);
        }
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
