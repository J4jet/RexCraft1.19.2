package net.jrex.rexcraft.entity.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.entity.custom.BernisEntity;
import net.jrex.rexcraft.entity.custom.StyracoEntity;
import net.jrex.rexcraft.entity.variant.BernisVariant;
import net.jrex.rexcraft.entity.variant.StyracoVariant;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;


public class StyracoRenderer extends GeoEntityRenderer<StyracoEntity> {

    public static final Map<StyracoVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(StyracoVariant.class), (p_114874_) -> {
                p_114874_.put(StyracoVariant.F1,
                        new ResourceLocation(RexCraft.MOD_ID, "textures/entity/styraco/styraco_m1.png"));
                p_114874_.put(StyracoVariant.M1,
                        new ResourceLocation(RexCraft.MOD_ID, "textures/entity/styraco/styraco_f1.png"));
                p_114874_.put(StyracoVariant.F2,
                        new ResourceLocation(RexCraft.MOD_ID, "textures/entity/styraco/styraco_m2.png"));
                p_114874_.put(StyracoVariant.M2,
                        new ResourceLocation(RexCraft.MOD_ID, "textures/entity/styraco/styraco_f2.png"));

            });
    public StyracoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new StyracoModel());
        this.shadowRadius = 0.5f;
    }



    @Override
    public ResourceLocation getTextureLocation(StyracoEntity instance) {
        String s = ChatFormatting.stripFormatting(instance.getName().getString());
        if (s != null && "Old Buck".equals(s)) {
            return new ResourceLocation(RexCraft.MOD_ID, "textures/entity/styraco/styraco_oldbuck.png");

        } else {
            return LOCATION_BY_VARIANT.get(instance.getVariant());
        }

    }

    @Override
    public RenderType getRenderType(StyracoEntity animatable, float partialTicks, PoseStack stack,
                                    @Nullable MultiBufferSource renderTypeBuffer,
                                    @Nullable VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        //Entity Size!
        if(animatable.isBaby()) {
            stack.scale(0.5F, 0.5F, 0.5F);
        } else {
            stack.scale(1.6f, 1.6f,1.6f);
        }
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
