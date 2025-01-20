package net.jrex.rexcraft.entity.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.entity.custom.BorealEntity;
import net.jrex.rexcraft.entity.custom.JakaEntity;
import net.jrex.rexcraft.entity.variant.BorealVariant;
import net.jrex.rexcraft.entity.variant.JakaVariant;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;


public class JakaRenderer extends GeoEntityRenderer<JakaEntity> {

    public static final Map<JakaVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(JakaVariant.class), (p_114874_) -> {
                p_114874_.put(JakaVariant.F,
                        new ResourceLocation(RexCraft.MOD_ID, "textures/entity/jaka/jaka_f.png"));
                p_114874_.put(JakaVariant.M,
                        new ResourceLocation(RexCraft.MOD_ID, "textures/entity/jaka/jaka_m.png"));

            });
    public JakaRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new JakaModel());
        this.shadowRadius = 0.15f;
    }



    @Override
    public ResourceLocation getTextureLocation(JakaEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());

    }

    @Override
    public RenderType getRenderType(JakaEntity animatable, float partialTicks, PoseStack stack,
                                    @Nullable MultiBufferSource renderTypeBuffer,
                                    @Nullable VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        //Entity Size!
        if(animatable.isBaby()) {
            stack.scale(0.06F, 0.06F, 0.06F);
        } else {
            stack.scale(0.13f, 0.13f,0.13f);
        }
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
