package net.jrex.rexcraft.entity.client;

import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.entity.custom.RexEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class RexModel extends AnimatedGeoModel<RexEntity> {
    @Override
    public ResourceLocation getModelResource(RexEntity object) {

        if (object.isBaby()){
            return new ResourceLocation(RexCraft.MOD_ID, "geo/rex_baby.geo.json");

        } else if (object.isSaddled()) {
            return new ResourceLocation(RexCraft.MOD_ID, "geo/rex_saddled.geo.json");
        } else{
            return new ResourceLocation(RexCraft.MOD_ID, "geo/rex.geo.json");
        }
    }

    @Override
    public ResourceLocation getTextureResource(RexEntity object) {
        return RexRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(RexEntity animatable) {

            if(animatable.isAngry()){
                return new ResourceLocation(RexCraft.MOD_ID, "animations/rex_angry.animation.json");
            } else if (animatable.isBaby()) {
                return new ResourceLocation(RexCraft.MOD_ID, "animations/rex_baby.animation.json");

            } else{return new ResourceLocation(RexCraft.MOD_ID, "animations/rex.animation.json");}
    }
    //Look at the player!
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setLivingAnimations(RexEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("hhead");

        //experimental, trying to get the neck to move too
        IBone neck = this.getAnimationProcessor().getBone("neck");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 270));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 270));
        }

        //WWWWWOOOOOOOO YYYYEEEEAAAAHHHH BABY THAT'S WHAT I'VE BEEN WAITIN FOR
        if(neck != null){
            neck.setRotationX(extraData.headPitch * ((float) Math.PI / 270));
            neck.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 270));
        }
    }
}
