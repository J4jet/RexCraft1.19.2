package net.jrex.rexcraft.entity.client.armor;

import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.item.custom.SteelArmorItem;
import net.jrex.rexcraft.item.custom.Steel_DiamondArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class Steel_DiamondArmorModel extends AnimatedGeoModel<Steel_DiamondArmorItem> {
    @Override
    public ResourceLocation getModelResource(Steel_DiamondArmorItem object) {
        return new ResourceLocation(RexCraft.MOD_ID, "geo/steel_diamond.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Steel_DiamondArmorItem object) {
        return new ResourceLocation(RexCraft.MOD_ID, "textures/models/armor/steel_diamond_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Steel_DiamondArmorItem animatable) {
        return new ResourceLocation(RexCraft.MOD_ID, "animations/diamond.animation.json");
    }

}
