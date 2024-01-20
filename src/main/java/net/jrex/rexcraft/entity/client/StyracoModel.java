package net.jrex.rexcraft.entity.client;

import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.entity.client.BernisRenderer;
import net.jrex.rexcraft.entity.custom.BernisEntity;
import net.jrex.rexcraft.entity.custom.StyracoEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class StyracoModel extends AnimatedGeoModel<StyracoEntity> {
    @Override
    public ResourceLocation getModelResource(StyracoEntity object) {
        if(object.isSaddled()){
            return new ResourceLocation(RexCraft.MOD_ID, "geo/styraco_saddled.geo.json");
        }
        else{return new ResourceLocation(RexCraft.MOD_ID, "geo/styraco.geo.json");}
    }

    @Override
    public ResourceLocation getTextureResource(StyracoEntity object) {
        return StyracoRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(StyracoEntity animatable) {

            if(animatable.isAngry()){
                return new ResourceLocation(RexCraft.MOD_ID, "animations/styraco_angry.animation.json");
            }
            else{return new ResourceLocation(RexCraft.MOD_ID, "animations/styraco.animation.json");}
    }
    //Look at the player!
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setLivingAnimations(StyracoEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("hhead");

        //experimental, trying to get the neck to move too
        IBone neck = this.getAnimationProcessor().getBone("neck");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 360F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 360F));
        }

        //WWWWWOOOOOOOO YYYYEEEEAAAAHHHH BABY THAT'S WHAT I'VE BEEN WAITIN FOR
        if(neck != null){
            neck.setRotationX(extraData.headPitch * ((float) Math.PI / 360F));
            neck.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 360F));
        }
    }
}
