package net.jrex.rexcraft.entity.client.armor;

import net.jrex.rexcraft.item.custom.Gold_Neth_Steel_ArmorItem;
import net.jrex.rexcraft.item.custom.Neth_Steel_ArmorItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class Gold_Neth_Steel_ArmorRenderer extends GeoArmorRenderer<Gold_Neth_Steel_ArmorItem> {
    public Gold_Neth_Steel_ArmorRenderer() {
        super(new Gold_Neth_Steel_ArmorModel());

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
