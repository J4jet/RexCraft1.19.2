package net.jrex.rexcraft.entity.client;

import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.entity.custom.GeckoEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class GeckoModel  extends AnimatedGeoModel<GeckoEntity> {
    @Override
    public ResourceLocation getModelResource(GeckoEntity object) {
        return new ResourceLocation(RexCraft.MOD_ID, "geo/gecko.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GeckoEntity object) {
        return GeckoRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(GeckoEntity animatable) {

        //the string s is being used to store the name of the gecko
        String s = ChatFormatting.stripFormatting(animatable.getName().getString());

        //if it's name is "cursed" use this set of animations instead
        if ("cursed".equals(s)) {
            return new ResourceLocation(RexCraft.MOD_ID, "animations/mildgeck.animation.json");
        } else {
            return new ResourceLocation(RexCraft.MOD_ID, "animations/gecko.animation.json");
        }
        //return new ResourceLocation(RexCraft.MOD_ID, "animations/gecko.animation.json");
    }
    //Look at the player!
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setLivingAnimations(GeckoEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        //experimental, trying to get the neck to move too
        //IBone neck = this.getAnimationProcessor().getBone("neck2");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }

        //WWWWWOOOOOOOO YYYYEEEEAAAAHHHH BABY THAT'S WHAT I'VE BEEN WAITIN FOR
//        if(neck != null){
//            neck.setRotationX(extraData.headPitch * ((float) Math.PI / 360F));
//            neck.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 360F));
//        }
    }
}
