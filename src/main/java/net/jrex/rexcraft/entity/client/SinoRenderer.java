package net.jrex.rexcraft.entity.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.entity.custom.SinoEntity;
import net.jrex.rexcraft.entity.variant.SinoVariant;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;


public class SinoRenderer extends GeoEntityRenderer<SinoEntity> {

    public static final Map<SinoVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(SinoVariant.class), (p_114874_) -> {
                p_114874_.put(SinoVariant.F1,
                        new ResourceLocation(RexCraft.MOD_ID, "textures/entity/velo/velo_m.png"));
                p_114874_.put(SinoVariant.M1,
                        new ResourceLocation(RexCraft.MOD_ID, "textures/entity/velo/velo_f.png"));

            });
    public SinoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SinoModel());
        this.shadowRadius = 0.5f;
    }



    @Override
    public ResourceLocation getTextureLocation(SinoEntity instance) {

        if (instance.isBaby()){
            return new ResourceLocation(RexCraft.MOD_ID, "textures/entity/velo/velo_baby.png");

        }

        return LOCATION_BY_VARIANT.get(instance.getVariant());

    }

    @Override
    public RenderType getRenderType(SinoEntity animatable, float partialTicks, PoseStack stack,
                                    @Nullable MultiBufferSource renderTypeBuffer,
                                    @Nullable VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        //Entity Size!
        if(animatable.isBaby()) {
            stack.scale(0.05F, 0.05F, 0.50F);
        } else {
            stack.scale(0.1f, 0.1f,0.1f);
        }
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
