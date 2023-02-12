package net.jrex.rexcraft.entity.client.armor;

import net.jrex.rexcraft.item.custom.GreatHelmItem;
import net.jrex.rexcraft.item.custom.SteelArmorItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class GreatHelmRenderer extends GeoArmorRenderer<GreatHelmItem> {
    public GreatHelmRenderer() {
        super(new GreatHelmModel());

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
