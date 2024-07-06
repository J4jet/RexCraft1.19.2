package net.jrex.rexcraft.entity.client;

import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.entity.custom.BorealEntity;
import net.jrex.rexcraft.entity.custom.DiploEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class DiploModel extends AnimatedGeoModel<DiploEntity> {
    @Override
    public ResourceLocation getModelResource(DiploEntity object) {
        if(object.hasChest()) {
            return new ResourceLocation(RexCraft.MOD_ID, "geo/diplo_chested.geo.json");
        }

        else{return new ResourceLocation(RexCraft.MOD_ID, "geo/diplo.geo.json");}
    }

    @Override
    public ResourceLocation getTextureResource(DiploEntity object) {
        return DiploRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(DiploEntity animatable) {

            if(animatable.isAngry()){
                return new ResourceLocation(RexCraft.MOD_ID, "animations/diplo_angry.animation.json");
            }
            else{return new ResourceLocation(RexCraft.MOD_ID, "animations/diplo.animation.json");}
    }
    //Look at the player!
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setLivingAnimations(DiploEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        //trying to get the neck to move too
        IBone neck = this.getAnimationProcessor().getBone("neck");

        //trying to get the neck to move too
        IBone neck2 = this.getAnimationProcessor().getBone("neck2");



        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }

        //WWWWWOOOOOOOO YYYYEEEEAAAAHHHH BABY THAT'S WHAT I'VE BEEN WAITIN FOR
        if(neck != null){
            neck.setRotationX(extraData.headPitch * ((float) Math.PI / 360F));
            neck.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 360F));
        }

        if(neck2 != null){
            neck2.setRotationX(extraData.headPitch * ((float) Math.PI / 360F));
            neck2.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 360F));
        }
    }
}
