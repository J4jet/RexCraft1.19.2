package net.jrex.rexcraft.entity.client.armor;

import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.item.custom.Diamond_Neth_Steel_ArmorItem;
import net.jrex.rexcraft.item.custom.Steel_Neth_Steel_ArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class Steel_Neth_Steel_ArmorModel extends AnimatedGeoModel<Steel_Neth_Steel_ArmorItem> {
    @Override
    public ResourceLocation getModelResource(Steel_Neth_Steel_ArmorItem object) {
        return new ResourceLocation(RexCraft.MOD_ID, "geo/steel_neth_steel_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Steel_Neth_Steel_ArmorItem object) {
        return new ResourceLocation(RexCraft.MOD_ID, "textures/models/armor/steel_neth_steel_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Steel_Neth_Steel_ArmorItem animatable) {
        return new ResourceLocation(RexCraft.MOD_ID, "animations/steel_neth_steel_armor.animation.json");
    }

}
