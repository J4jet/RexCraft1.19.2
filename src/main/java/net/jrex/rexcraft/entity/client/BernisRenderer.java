package net.jrex.rexcraft.entity.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.entity.custom.BernisEntity;
import net.jrex.rexcraft.entity.custom.BucklandiiEntity;
import net.jrex.rexcraft.entity.variant.BernisVariant;
import net.jrex.rexcraft.entity.variant.BucklandiiVariant;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;


public class BernisRenderer extends GeoEntityRenderer<BernisEntity> {

    public static final Map<BernisVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(BernisVariant.class), (p_114874_) -> {
                p_114874_.put(BernisVariant.GREEN_F,
                        new ResourceLocation(RexCraft.MOD_ID, "textures/entity/bernis/bernis_mv2.png"));
                p_114874_.put(BernisVariant.GREEN_M,
                        new ResourceLocation(RexCraft.MOD_ID, "textures/entity/bernis/bernis_fv2.png"));

            });
    public BernisRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BernisModel());
        this.shadowRadius = 0.5f;
    }



    @Override
    public ResourceLocation getTextureLocation(BernisEntity instance) {
        String s = ChatFormatting.stripFormatting(instance.getName().getString());
        if (s != null && "Aladar".equals(s)) {
            return new ResourceLocation(RexCraft.MOD_ID, "textures/entity/bernis/bernis_m.png");

        } else {
            return LOCATION_BY_VARIANT.get(instance.getVariant());
        }

    }

    @Override
    public RenderType getRenderType(BernisEntity animatable, float partialTicks, PoseStack stack,
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
