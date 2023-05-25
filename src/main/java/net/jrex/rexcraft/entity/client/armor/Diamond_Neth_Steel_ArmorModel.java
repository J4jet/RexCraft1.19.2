package net.jrex.rexcraft.entity.client.armor;

import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.item.custom.Diamond_Neth_Steel_ArmorItem;
import net.jrex.rexcraft.item.custom.Gold_Neth_Steel_ArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class Diamond_Neth_Steel_ArmorModel extends AnimatedGeoModel<Diamond_Neth_Steel_ArmorItem> {
    @Override
    public ResourceLocation getModelResource(Diamond_Neth_Steel_ArmorItem object) {
        return new ResourceLocation(RexCraft.MOD_ID, "geo/diamond_neth_steel_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Diamond_Neth_Steel_ArmorItem object) {
        return new ResourceLocation(RexCraft.MOD_ID, "textures/models/armor/diamond_neth_steel_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Diamond_Neth_Steel_ArmorItem animatable) {
        return new ResourceLocation(RexCraft.MOD_ID, "animations/diamond_neth_steel_armor.animation.json");
    }

}
