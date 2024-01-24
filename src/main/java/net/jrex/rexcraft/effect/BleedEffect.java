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
                pLivingEntity.hurt(DamageSource.GENERIC, 1.5f);
            }
            else if(pLivingEntity.isCrouching()){
                pLivingEntity.hurt(DamageSource.GENERIC, 0.4f);
            }
            else{
                pLivingEntity.hurt(DamageSource.GENERIC, 0.8f);
            }
        }
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {

        //used in POSITION effect:
        // pLivingEntity.hurt(DamageSource.MAGIC, 1.0F);
        int j = 18 >> pAmplifier;
        if (j > 0) {
            return pDuration % j == 0;
        } else {
            return true;
        }
    }
}
