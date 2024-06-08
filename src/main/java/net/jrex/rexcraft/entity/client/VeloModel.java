package net.jrex.rexcraft.entity.client;

import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.entity.custom.StyracoEntity;
import net.jrex.rexcraft.entity.custom.VeloEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class VeloModel extends AnimatedGeoModel<VeloEntity> {
    @Override
    public ResourceLocation getModelResource(VeloEntity object) {

        if (object.isBaby()){
            return new ResourceLocation(RexCraft.MOD_ID, "geo/velo_baby.geo.json");

        }
        else{
            return new ResourceLocation(RexCraft.MOD_ID, "geo/velo.geo.json");
        }
    }

    @Override
    public ResourceLocation getTextureResource(VeloEntity object) {
        return VeloRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(VeloEntity animatable) {

            if(animatable.isAngry()){
                return new ResourceLocation(RexCraft.MOD_ID, "animations/velo_angry.animation.json");
            }
            else{return new ResourceLocation(RexCraft.MOD_ID, "animations/velo.animation.json");}
    }
    //Look at the player!
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setLivingAnimations(VeloEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
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
