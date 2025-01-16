package net.jrex.rexcraft.entity.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.entity.custom.PrenoEntity;
import net.jrex.rexcraft.entity.variant.PrenoVariant;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;


public class PrenoRenderer extends GeoEntityRenderer<PrenoEntity> {

    public static final Map<PrenoVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(PrenoVariant.class), (p_114874_) -> {
                p_114874_.put(PrenoVariant.F,
                        new ResourceLocation(RexCraft.MOD_ID, "textures/entity/styraco/styraco_m1.png"));
                p_114874_.put(PrenoVariant.M,
                        new ResourceLocation(RexCraft.MOD_ID, "textures/entity/styraco/styraco_f1.png"));

            });
    public PrenoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PrenoModel());
        this.shadowRadius = 0.3f;
    }



    @Override
    public ResourceLocation getTextureLocation(PrenoEntity instance) {
        if (instance.isDigging()) {
            return new ResourceLocation(RexCraft.MOD_ID, "textures/entity/styraco/styraco_oldbuck.png");

        } else {
            return LOCATION_BY_VARIANT.get(instance.getVariant());
        }

    }

    @Override
    public RenderType getRenderType(PrenoEntity animatable, float partialTicks, PoseStack stack,
                                    @Nullable MultiBufferSource renderTypeBuffer,
                                    @Nullable VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        //Entity Size!
        if(animatable.isBaby()) {
            stack.scale(0.2F, 0.2F, 0.2F);
        } else {
            stack.scale(0.7f, 0.7f,0.7f);
        }
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
