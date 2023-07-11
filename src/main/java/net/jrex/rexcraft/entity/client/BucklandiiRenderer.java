package net.jrex.rexcraft.entity.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.entity.custom.BucklandiiEntity;
import net.jrex.rexcraft.entity.custom.GeckoEntity;
import net.jrex.rexcraft.entity.variant.BucklandiiVariant;
import net.jrex.rexcraft.entity.variant.GeckoVariant;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;


public class BucklandiiRenderer extends GeoEntityRenderer<BucklandiiEntity> {

    public static final Map<BucklandiiVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(BucklandiiVariant.class), (p_114874_) -> {
                p_114874_.put(BucklandiiVariant.BROWN_M,
                        new ResourceLocation(RexCraft.MOD_ID, "textures/entity/bucklandii/bucklandiif.png"));
                p_114874_.put(BucklandiiVariant.BROWN_F,
                        new ResourceLocation(RexCraft.MOD_ID, "textures/entity/bucklandii/bucklandiim.png"));
                p_114874_.put(BucklandiiVariant.RED_F,
                        new ResourceLocation(RexCraft.MOD_ID, "textures/entity/bucklandii/bucklandiimv2.png"));
                p_114874_.put(BucklandiiVariant.RED_M,
                        new ResourceLocation(RexCraft.MOD_ID, "textures/entity/bucklandii/bucklandiifv2.png"));

            });
    public BucklandiiRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BucklandiiModel());
        this.shadowRadius = 0.5f;
    }



    @Override
    public ResourceLocation getTextureLocation(BucklandiiEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());

    }

    @Override
    public RenderType getRenderType(BucklandiiEntity animatable, float partialTicks, PoseStack stack,
                                    @Nullable MultiBufferSource renderTypeBuffer,
                                    @Nullable VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        //Entity Size!
        if(animatable.isBaby()) {
            stack.scale(0.15F, 0.15F, 0.15F);
        } else {
            stack.scale(0.52f, 0.52f,0.52f);
        }
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
