package net.jrex.rexcraft.entity.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.entity.custom.BorealEntity;
import net.jrex.rexcraft.entity.variant.BorealVariant;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;


public class BorealRenderer extends GeoEntityRenderer<BorealEntity> {

    public static final Map<BorealVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(BorealVariant.class), (p_114874_) -> {
                p_114874_.put(BorealVariant.F,
                        new ResourceLocation(RexCraft.MOD_ID, "textures/entity/boreal/boreal_f.png"));
                p_114874_.put(BorealVariant.M,
                        new ResourceLocation(RexCraft.MOD_ID, "textures/entity/boreal/boreal_m.png"));

            });
    public BorealRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BorealModel());
        this.shadowRadius = 0.8f;
    }



    @Override
    public ResourceLocation getTextureLocation(BorealEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());

    }

    @Override
    public RenderType getRenderType(BorealEntity animatable, float partialTicks, PoseStack stack,
                                    @Nullable MultiBufferSource renderTypeBuffer,
                                    @Nullable VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        //Entity Size!
        if(animatable.isBaby()) {
            stack.scale(1.0F, 1.0F, 1.0F);
        } else {
            stack.scale(2.0f, 2.0f,2.0f);
        }
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
