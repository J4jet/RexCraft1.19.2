package net.jrex.rexcraft.entity.client.armor;

import net.jrex.rexcraft.item.custom.Diamond_Neth_Steel_ArmorItem;
import net.jrex.rexcraft.item.custom.Steel_Neth_Steel_ArmorItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class Steel_Neth_Steel_ArmorRenderer extends GeoArmorRenderer<Steel_Neth_Steel_ArmorItem> {
    public Steel_Neth_Steel_ArmorRenderer() {
        super(new Steel_Neth_Steel_ArmorModel());

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
