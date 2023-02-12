package net.jrex.rexcraft.entity.client.armor;

import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.item.custom.GreatHelmItem;
import net.jrex.rexcraft.item.custom.SteelArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GreatHelmModel extends AnimatedGeoModel<GreatHelmItem> {
    @Override
    public ResourceLocation getModelResource(GreatHelmItem object) {
        return new ResourceLocation(RexCraft.MOD_ID, "geo/great_helm.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GreatHelmItem object) {
        return new ResourceLocation(RexCraft.MOD_ID, "textures/models/armor/great_helm.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GreatHelmItem animatable) {
        return new ResourceLocation(RexCraft.MOD_ID, "animations/great_helm.animation.json");
    }

}
