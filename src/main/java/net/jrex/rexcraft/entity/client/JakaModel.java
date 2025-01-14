package net.jrex.rexcraft.entity.client;

import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.entity.custom.BorealEntity;
import net.jrex.rexcraft.entity.custom.JakaEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class JakaModel extends AnimatedGeoModel<JakaEntity> {
    @Override
    public ResourceLocation getModelResource(JakaEntity object) {

        return new ResourceLocation(RexCraft.MOD_ID, "geo/jaka.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(JakaEntity object) {
        return JakaRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(JakaEntity animatable) {

        if(animatable.isAngry()){
            return new ResourceLocation(RexCraft.MOD_ID, "animations/jaka_angry.animation.json");
        }

        else if(animatable.isDigging()){
            return new ResourceLocation(RexCraft.MOD_ID, "animations/jaka.digging.animation.json");
        }
        else{return new ResourceLocation(RexCraft.MOD_ID, "animations/jaka.animation.json");}
    }
    //Look at the player!
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setLivingAnimations(JakaEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("hhead");

        IBone neck = this.getAnimationProcessor().getBone("neck3");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }

        if(neck != null){
            neck.setRotationX(extraData.headPitch * ((float) Math.PI / 360F));
            neck.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 360F));
        }
    }
}
