package net.jrex.rexcraft.entity.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.entity.custom.GeckoEntity;
import net.jrex.rexcraft.entity.custom.OroEntity;
import net.jrex.rexcraft.entity.variant.GeckoVariant;
import net.jrex.rexcraft.entity.variant.OroVariant;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;


public class OroRenderer extends GeoEntityRenderer<OroEntity> {

    public static final Map<OroVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(OroVariant.class), (p_114874_) -> {
                p_114874_.put(OroVariant.DOTTED,
                        new ResourceLocation(RexCraft.MOD_ID, "textures/entity/gecko/dotted.png"));
                p_114874_.put(OroVariant.DOTLESS,
                        new ResourceLocation(RexCraft.MOD_ID, "textures/entity/gecko/dotless.png"));
                p_114874_.put(OroVariant.TANGE,
                        new ResourceLocation(RexCraft.MOD_ID, "textures/entity/gecko/tange.png"));
                p_114874_.put(OroVariant.TANGE_2,
                        new ResourceLocation(RexCraft.MOD_ID, "textures/entity/gecko/tange_2.png"));
                p_114874_.put(OroVariant.INF,
                        new ResourceLocation(RexCraft.MOD_ID, "textures/entity/gecko/inferno.png"));

            });
    public OroRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new OroModel());
        this.shadowRadius = 0.1f;
    }



    @Override
    public ResourceLocation getTextureLocation(OroEntity instance) {
        //String s = ChatFormatting.stripFormatting(instance.getName().getString());
        if (instance.isDigging()) {
            return new ResourceLocation(RexCraft.MOD_ID, "textures/entity/gecko/nova.png");

        } else {
            return LOCATION_BY_VARIANT.get(instance.getVariant());
        }

    }

    @Override
    public RenderType getRenderType(OroEntity animatable, float partialTicks, PoseStack stack,
                                    @Nullable MultiBufferSource renderTypeBuffer,
                                    @Nullable VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        //Entity Size!
        if(animatable.isBaby()) {
            stack.scale(0.05F, 0.05F, 0.05F);
        } else {
            stack.scale(0.15f, 0.15f, 0.15f);
        }
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
