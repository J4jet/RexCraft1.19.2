package net.jrex.rexcraft.entity.client.armor;

import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.item.custom.SteelArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SteelArmorModel extends AnimatedGeoModel<SteelArmorItem> {
    @Override
    public ResourceLocation getModelResource(SteelArmorItem object) {
        return new ResourceLocation(RexCraft.MOD_ID, "geo/steel_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SteelArmorItem object) {
        return new ResourceLocation(RexCraft.MOD_ID, "textures/models/armor/steel_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SteelArmorItem animatable) {
        return new ResourceLocation(RexCraft.MOD_ID, "animations/armor.animation.json");
    }

}
