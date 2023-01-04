package net.jrex.rexcraft.entity.client;

import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.entity.custom.GeckoEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GeckoModel  extends AnimatedGeoModel<GeckoEntity> {
    @Override
    public ResourceLocation getModelResource(GeckoEntity object) {
        return new ResourceLocation(RexCraft.MOD_ID, "geo/gecko.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GeckoEntity object) {
        return new ResourceLocation(RexCraft.MOD_ID, "textures/entity/gecko/nova.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GeckoEntity animatable) {
        return new ResourceLocation(RexCraft.MOD_ID, "animations/gecko.animations.json");
    }
}
