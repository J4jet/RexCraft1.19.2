package net.jrex.rexcraft.item.custom;

import net.jrex.rexcraft.effect.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class BleedSwordItem extends SwordItem {
    public BleedSwordItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    //somewhere there is a Forge attribute thing that changes the reach of an item
    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pTarget.addEffect(new MobEffectInstance(ModEffects.BLEED.get(), 40), pAttacker);
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
