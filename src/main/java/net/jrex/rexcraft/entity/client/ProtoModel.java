package net.jrex.rexcraft.entity.client;

import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.entity.custom.ProtoEntity;
import net.jrex.rexcraft.entity.custom.StyracoEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ProtoModel extends AnimatedGeoModel<ProtoEntity> {
    @Override
    public ResourceLocation getModelResource(ProtoEntity object) {
        return new ResourceLocation(RexCraft.MOD_ID, "geo/proto.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ProtoEntity object) {
        return ProtoRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(ProtoEntity animatable) {

            if(animatable.isDigging()){
                return new ResourceLocation(RexCraft.MOD_ID, "animations/proto.digging.animation.json");
            }
            else if(animatable.isAngry()){
                return new ResourceLocation(RexCraft.MOD_ID, "animations/proto_angry.animation.json");
            }
            else{return new ResourceLocation(RexCraft.MOD_ID, "animations/proto.animation.json");}
    }
    //Look at the player!
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setLivingAnimations(ProtoEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

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
