package net.jrex.rexcraft.entity.client;

import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.entity.custom.GeckoEntity;
import net.jrex.rexcraft.entity.custom.HedgyEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class HedgyModel extends AnimatedGeoModel<HedgyEntity> {


    //@Override
    //public ResourceLocation getModelLocation(HedgyEntity object) {
        //return new ResourceLocation(RexCraft.MOD_ID, "geo/hedgy.geo.json");
    //}

    @Override
    public ResourceLocation getModelResource(HedgyEntity object) {
        return new ResourceLocation(RexCraft.MOD_ID, "geo/hedgy.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HedgyEntity object) {
        return HedgyRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    //@Override
    //public ResourceLocation getAnimationFileLocation(HedgyEntity animatable) {
        //return new ResourceLocation(RexCraft.MOD_ID, "animations/hedgy.animation.json");
    //}

    @Override
    public ResourceLocation getAnimationResource(HedgyEntity animatable) {
        return new ResourceLocation(RexCraft.MOD_ID, "animations/hedgy.animation.json");
    }
    //Look at the player!
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setLivingAnimations(HedgyEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("h_head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 360F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 360F));
        }
    }

}
