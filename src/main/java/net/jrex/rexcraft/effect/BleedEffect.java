package net.jrex.rexcraft.effect;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class BleedEffect extends MobEffect {
    protected BleedEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }


    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.level.isClientSide()) {

            if(pLivingEntity.isSprinting()){
                pLivingEntity.hurt(DamageSource.GENERIC, 1.2f);
            }
            else if(pLivingEntity.isCrouching()){
                pLivingEntity.hurt(DamageSource.GENERIC, 0.1f);
            }
            else{
                pLivingEntity.hurt(DamageSource.GENERIC, 0.5f);
            }
        }
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
    //used in POSITION effect:
    // pLivingEntity.hurt(DamageSource.MAGIC, 1.0F);
}
