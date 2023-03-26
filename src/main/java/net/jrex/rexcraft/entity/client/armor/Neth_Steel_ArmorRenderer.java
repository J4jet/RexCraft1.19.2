package net.jrex.rexcraft.entity.client.armor;

import net.jrex.rexcraft.item.custom.Neth_Steel_ArmorItem;
import net.jrex.rexcraft.item.custom.Steel_DiamondArmorItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class Neth_Steel_ArmorRenderer extends GeoArmorRenderer<Neth_Steel_ArmorItem> {
    public Neth_Steel_ArmorRenderer() {
        super(new Neth_Steel_ArmorModel());

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
