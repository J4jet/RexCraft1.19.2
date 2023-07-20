package net.jrex.rexcraft.entity.client;

import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.entity.client.BucklandiiRenderer;
import net.jrex.rexcraft.entity.custom.BernisEntity;
import net.jrex.rexcraft.entity.custom.BucklandiiEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class BernisModel extends AnimatedGeoModel<BernisEntity> {
    @Override
    public ResourceLocation getModelResource(BernisEntity object) {
        if(object.isSaddled()){
            return new ResourceLocation(RexCraft.MOD_ID, "geo/bernis_saddled.geo.json");
        }
        else{return new ResourceLocation(RexCraft.MOD_ID, "geo/bernis.geo.json");}
    }

    @Override
    public ResourceLocation getTextureResource(BernisEntity object) {
        return BernisRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(BernisEntity animatable) {

            if(animatable.isAngry()){
                return new ResourceLocation(RexCraft.MOD_ID, "animations/bernis_angry.animation.json");
            }
            else{return new ResourceLocation(RexCraft.MOD_ID, "animations/bernis.animation.json");}
    }
    //Look at the player!
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setLivingAnimations(BernisEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("hhead");

        //experimental, trying to get the neck to move too
        IBone neck = this.getAnimationProcessor().getBone("neck");

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
    }
}
