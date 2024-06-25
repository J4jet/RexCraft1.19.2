package net.jrex.rexcraft.entity.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.entity.custom.StyracoEntity;
import net.jrex.rexcraft.entity.custom.VeloEntity;
import net.jrex.rexcraft.entity.variant.StyracoVariant;
import net.jrex.rexcraft.entity.variant.VeloVariant;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;


public class VeloRenderer extends GeoEntityRenderer<VeloEntity> {

    public static final Map<VeloVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(VeloVariant.class), (p_114874_) -> {
                p_114874_.put(VeloVariant.F1,
                        new ResourceLocation(RexCraft.MOD_ID, "textures/entity/velo/velo_m.png"));
                p_114874_.put(VeloVariant.M1,
                        new ResourceLocation(RexCraft.MOD_ID, "textures/entity/velo/velo_f.png"));

            });
    public VeloRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new VeloModel());
        this.shadowRadius = 0.5f;
    }



    @Override
    public ResourceLocation getTextureLocation(VeloEntity instance) {

        if (instance.isBaby()){
            return new ResourceLocation(RexCraft.MOD_ID, "textures/entity/velo/velo_baby.png");

        }

        String s = ChatFormatting.stripFormatting(instance.getName().getString());

        if (s != null && "Blue".equals(s)) {
            return new ResourceLocation(RexCraft.MOD_ID, "textures/entity/velo/blue.png");

        } else {
            return LOCATION_BY_VARIANT.get(instance.getVariant());
        }

    }

    @Override
    public RenderType getRenderType(VeloEntity animatable, float partialTicks, PoseStack stack,
                                    @Nullable MultiBufferSource renderTypeBuffer,
                                    @Nullable VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        //Entity Size!
        if(animatable.isBaby()) {
            stack.scale(0.1F, 0.1F, 0.1F);
        } else {
            stack.scale(0.2f, 0.2f,0.2f);
        }
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
