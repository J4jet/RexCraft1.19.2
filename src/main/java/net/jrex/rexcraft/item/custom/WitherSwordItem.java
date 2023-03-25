package net.jrex.rexcraft.item.custom;

import com.mojang.blaze3d.shaders.Effect;
import net.jrex.rexcraft.effect.ModEffects;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class WitherSwordItem extends SwordItem {
    public WitherSwordItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pTarget.addEffect(new MobEffectInstance(MobEffects.WITHER, 300), pAttacker);
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
