package net.jrex.rexcraft.entity.client;

import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.entity.custom.PrenoEntity;
import net.jrex.rexcraft.entity.custom.StyracoEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class PrenoModel extends AnimatedGeoModel<PrenoEntity> {
    @Override
    public ResourceLocation getModelResource(PrenoEntity object) {
        return new ResourceLocation(RexCraft.MOD_ID, "geo/styraco.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PrenoEntity object) {
        return PrenoRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(PrenoEntity animatable) {

        if(animatable.isAngry()){
            return new ResourceLocation(RexCraft.MOD_ID, "animations/styraco_angry.animation.json");
        }
        else{return new ResourceLocation(RexCraft.MOD_ID, "animations/styraco.animation.json");}
    }
    //Look at the player!
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setLivingAnimations(PrenoEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("hhead");

        IBone neck = this.getAnimationProcessor().getBone("neck");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 360F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 360F));
        }

        if(neck != null){
            neck.setRotationX(extraData.headPitch * ((float) Math.PI / 360F));
            neck.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 360F));
        }
    }


}
