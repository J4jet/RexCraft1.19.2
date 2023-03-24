package net.jrex.rexcraft.entity.client.armor;

import net.jrex.rexcraft.item.custom.SteelArmorItem;
import net.jrex.rexcraft.item.custom.Steel_DiamondArmorItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class Steel_DiamondArmorRenderer extends GeoArmorRenderer<Steel_DiamondArmorItem> {
    public Steel_DiamondArmorRenderer() {
        super(new Steel_DiamondArmorModel());

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
