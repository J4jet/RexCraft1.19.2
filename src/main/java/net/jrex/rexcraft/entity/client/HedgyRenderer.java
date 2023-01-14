package net.jrex.rexcraft.entity.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.entity.custom.GeckoEntity;
import net.jrex.rexcraft.entity.custom.HedgyEntity;
import net.jrex.rexcraft.entity.variant.HedgyVariant;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class HedgyRenderer extends GeoEntityRenderer<HedgyEntity> {

    public static final Map<HedgyVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(HedgyVariant.class), (p_114874_) -> {
                p_114874_.put(HedgyVariant.DARK,
                        new ResourceLocation(RexCraft.MOD_ID, "textures/entity/hedgy/hedgy1a.png"));

                p_114874_.put(HedgyVariant.LIGHT,
                        new ResourceLocation(RexCraft.MOD_ID, "textures/entity/hedgy/hedgy2a.png"));

               // p_114874_.put(HedgyVariant.WILBUR,
                        //new ResourceLocation(RexMod.MOD_ID, "textures/entity/hedgy/Wilbur.png"));
            });
    public HedgyRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new HedgyModel());
        this.shadowRadius = 0.3f;
    }

    @Override
    public ResourceLocation getTextureLocation(HedgyEntity instance) {
        String s = ChatFormatting.stripFormatting(instance.getName().getString());
        if (s != null && "Wilbur".equals(s)) {
            return new ResourceLocation(RexCraft.MOD_ID, "textures/entity/hedgy/wilbur.png");
        } else {
            return LOCATION_BY_VARIANT.get(instance.getVariant());
        }

    }

    @Override
    public RenderType getRenderType(HedgyEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        if(animatable.isBaby()) {
        stack.scale(0.2F, 0.2F, 0.2F);
    } else {
        stack.scale(0.6F, 0.6F, 0.6F);
    }
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
