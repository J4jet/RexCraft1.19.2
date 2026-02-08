package net.jrex.rexcraft.entity.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.entity.custom.RexEntity;
import net.jrex.rexcraft.entity.variant.RexVariant;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;


public class RexRenderer extends GeoEntityRenderer<RexEntity> {

    public static final Map<RexVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(RexVariant.class), (p_114874_) -> {
                p_114874_.put(RexVariant.F1,
                        new ResourceLocation(RexCraft.MOD_ID, "textures/entity/rex/rex_m.png"));
                p_114874_.put(RexVariant.M1,
                        new ResourceLocation(RexCraft.MOD_ID, "textures/entity/rex/rex_f.png"));

            });
    public RexRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new RexModel());
        this.shadowRadius = 1.0f;
    }



    @Override
    public ResourceLocation getTextureLocation(RexEntity instance) {

        if (instance.isBaby()){
            return new ResourceLocation(RexCraft.MOD_ID, "textures/entity/rex/rex_baby.png");

        }

        String s = ChatFormatting.stripFormatting(instance.getName().getString());

        if (s != null && "Rexy".equals(s) || s != null && "Roberta".equals(s)) {
            return new ResourceLocation(RexCraft.MOD_ID, "textures/entity/rex/jp.png");

        } else {
            return LOCATION_BY_VARIANT.get(instance.getVariant());
        }

    }

    @Override
    public RenderType getRenderType(RexEntity animatable, float partialTicks, PoseStack stack,
                                    @Nullable MultiBufferSource renderTypeBuffer,
                                    @Nullable VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        //Entity Size!
        if(animatable.isBaby()) {
            stack.scale(0.5F, 0.5F, 0.5F);
        } else {
            stack.scale(1.2f, 1.2f,1.2f);
        }
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
