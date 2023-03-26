package net.jrex.rexcraft.entity.client.armor;

import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.item.custom.Neth_Steel_ArmorItem;
import net.jrex.rexcraft.item.custom.Steel_DiamondArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class Neth_Steel_ArmorModel extends AnimatedGeoModel<Neth_Steel_ArmorItem> {
    @Override
    public ResourceLocation getModelResource(Neth_Steel_ArmorItem object) {
        return new ResourceLocation(RexCraft.MOD_ID, "geo/nether_steel_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Neth_Steel_ArmorItem object) {
        return new ResourceLocation(RexCraft.MOD_ID, "textures/models/armor/neth_steel_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Neth_Steel_ArmorItem animatable) {
        return new ResourceLocation(RexCraft.MOD_ID, "animations/neth.animation.json");
    }

}
