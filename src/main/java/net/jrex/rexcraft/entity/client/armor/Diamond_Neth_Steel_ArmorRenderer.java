package net.jrex.rexcraft.entity.client.armor;

import net.jrex.rexcraft.item.custom.Diamond_Neth_Steel_ArmorItem;
import net.jrex.rexcraft.item.custom.Gold_Neth_Steel_ArmorItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class Diamond_Neth_Steel_ArmorRenderer extends GeoArmorRenderer<Diamond_Neth_Steel_ArmorItem> {
    public Diamond_Neth_Steel_ArmorRenderer() {
        super(new Diamond_Neth_Steel_ArmorModel());

        this.headBone = "armorHead";
        this.bodyBone = "armorBody";
        this.rightArmBone = "armorRightArm";
        this.leftArmBone = "armorLeftArm";
        this.rightLegBone = "armorRightLeg";
        this.leftLegBone = "armorLeftLeg";
        this.rightBootBone = "armorRightBoot";
        this.leftBootBone = "armorLeftBoot";
    }
}
