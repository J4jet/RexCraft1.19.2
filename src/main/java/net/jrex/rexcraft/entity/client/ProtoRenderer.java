package net.jrex.rexcraft.entity.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.entity.custom.ProtoEntity;
import net.jrex.rexcraft.entity.custom.StyracoEntity;
import net.jrex.rexcraft.entity.variant.ProtoVariant;
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


public class ProtoRenderer extends GeoEntityRenderer<ProtoEntity> {

    public static final Map<ProtoVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(ProtoVariant.class), (p_114874_) -> {
                p_114874_.put(ProtoVariant.F,
                        new ResourceLocation(RexCraft.MOD_ID, "textures/entity/proto/proto_m.png"));
                p_114874_.put(ProtoVariant.M,
                        new ResourceLocation(RexCraft.MOD_ID, "textures/entity/proto/proto_f.png"));

            });
    public ProtoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ProtoModel());
        this.shadowRadius = 0.2f;
    }



    @Override
    public ResourceLocation getTextureLocation(ProtoEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());

    }

    @Override
    public RenderType getRenderType(ProtoEntity animatable, float partialTicks, PoseStack stack,
                                    @Nullable MultiBufferSource renderTypeBuffer,
                                    @Nullable VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        //Entity Size!
        if(animatable.isBaby()) {
            stack.scale(0.2F, 0.2F, 0.2F);
        } else {
            stack.scale(0.7f, 0.7f,0.7f);
        }
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
