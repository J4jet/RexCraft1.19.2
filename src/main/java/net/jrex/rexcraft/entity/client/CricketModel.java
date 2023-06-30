package net.jrex.rexcraft.entity.client;

import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.entity.custom.CricketEntity;
import net.jrex.rexcraft.entity.custom.HedgyEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class CricketModel extends AnimatedGeoModel<CricketEntity> {
    @Override
    public ResourceLocation getModelResource(CricketEntity object) {
        return new ResourceLocation(RexCraft.MOD_ID, "geo/cricket.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CricketEntity object) {
        return new ResourceLocation(RexCraft.MOD_ID, "textures/entity/cricket/cricket.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CricketEntity animatable) {
        return new ResourceLocation(RexCraft.MOD_ID, "animations/cricket.animation.json");
    }

    //Look at the player!
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setLivingAnimations(CricketEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("hhead");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 360F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 360F));
        }
    }
}
