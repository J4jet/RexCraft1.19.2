package net.jrex.rexcraft.entity.custom;

import net.jrex.rexcraft.effect.ModEffects;
import net.jrex.rexcraft.entity.ModEntityTypes;
import net.jrex.rexcraft.entity.variant.BucklandiiVariant;
import net.jrex.rexcraft.entity.variant.StyracoVariant;
import net.jrex.rexcraft.item.ModItems;
import net.jrex.rexcraft.sound.ModSounds;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;

public class StyracoEntity extends AbstractCombatDino {

    public static final Predicate<LivingEntity> PREY_SELECTOR = (p_30437_) -> {
        EntityType<?> entitytype = p_30437_.getType();
        return entitytype == EntityType.SPIDER || entitytype == EntityType.PLAYER ;
    };

    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(StyracoEntity.class, EntityDataSerializers.INT);

    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(30, 50);

    //speed modifier of the entity when being ridden
    public float getSpeedMod(){
        return 0.0f;
    }

    @Override
    public float getBaseAttack(){
        return 10f;
    }

    //set length of the challenge animation
    public void setAnimLen(){
        this.challenge_time = 67;
    }

    @Override
    public SoundEvent getChallengedSound(){return ModSounds.STYRACO_CHALLENGED.get();}

    public static int attacknum = 3;

    @Override
    public float getRiderOffset(){
        return 0.45f;
    }

    public float getStepHeight(){
        return 1.5F;
    }

    @Override
    public float getBaseSpeed(){
        return 0.2f;
    }

    public StyracoEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier setAttributes() {

        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 46.0D)
                .add(Attributes.ATTACK_DAMAGE, 10.0f)
                .add(Attributes.ATTACK_SPEED, 1.7f)
                .add(Attributes.FOLLOW_RANGE, 5)
                .add(Attributes.KNOCKBACK_RESISTANCE, 3)
                .add(Attributes.MOVEMENT_SPEED, 0.17f).build();
    }

    @Override
    public void setTame(boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(50.0D);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(10.5D);
            getAttribute(Attributes.ATTACK_SPEED).setBaseValue(0.8f);
            getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(3f);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.18f);


        } else {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(46.0D);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(10.0D);
            getAttribute(Attributes.ATTACK_SPEED).setBaseValue(0.8f);
            getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(3f);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.18f);
        }
    }

    @Override
    protected void registerGoals() {

            this.goalSelector.addGoal(1, new FloatGoal(this));
            this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
            this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 2.0D, 10.0F, 4.0F, false));
            this.goalSelector.addGoal(2, new FollowParentGoal(this, 1.1D));
            this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 2.0D, false));
            this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
            this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F));
            this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
            this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

            this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
            this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
            this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
            this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
            //this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Cow.class, true));
            //this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Pig.class, true));
            //this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Sheep.class, true));
            this.targetSelector.addGoal(4, new NonTameRandomTargetGoal<>(this, LivingEntity.class, true, PREY_SELECTOR));
            this.targetSelector.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    @Override
    protected int calculateFallDamage(float pDistance, float pDamageMultiplier) {
        if (pDistance < 3.5){
            return 0;
        }else{
            return Mth.ceil((pDistance * 9.0F) * pDamageMultiplier);
        }
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob mob) {
        StyracoEntity baby = ModEntityTypes.STYRACO.get().create(serverLevel);
        StyracoVariant variant = Util.getRandom(StyracoVariant.values(), this.random);
        baby.setVariant(variant);
        return baby;
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        if (!super.doHurtTarget(pEntity)) {
            return false;
        } else {
            if (pEntity instanceof LivingEntity) {
                ((LivingEntity)pEntity).addEffect(new MobEffectInstance(ModEffects.BLEED.get(), 30), this);
            }

            return true;
        }
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.getItem() == ModItems.RAB_STEW.get();
    }

    //Used as the healing item, in the case of the gecko it's a cricket
    //look into wolf class to see how meat works
    public boolean isHeal(ItemStack pStack){
        Item item = pStack.getItem();
        return item.isEdible() && (item == ModItems.ZUCC.get() || item == ModItems.BLUEBERRY.get() || item == Items.WHEAT || item == Items.CARROT) ;

    }

    //taming item
    public boolean tameItem(ItemStack pStack){
        Item item = pStack.getItem();
        return item == ModItems.HERB_BUFF_GOLD.get() || item == ModItems.HERB_BUFF_DIAMOND.get() || item == ModItems.HERB_BUFF_NETH.get();
    }

    @Override
    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
        if (!pBlock.getMaterial().isLiquid()) {
            BlockState blockstate = this.level.getBlockState(pPos.above());
            SoundType soundtype = pBlock.getSoundType(level, pPos, this);
            if (blockstate.is(Blocks.SNOW)) {
                soundtype = blockstate.getSoundType(level, pPos, this);
            }

            if (soundtype == SoundType.WOOD) {
                this.playSound(SoundEvents.WOOD_STEP, soundtype.getVolume() * 0.30F, soundtype.getPitch()-0.6f);
            }
            if (soundtype == SoundType.STONE) {
                this.playSound(SoundEvents.STONE_STEP, soundtype.getVolume() * 0.30F, soundtype.getPitch()-0.6f);
            }
            if (soundtype == SoundType.NETHERITE_BLOCK) {
                this.playSound(SoundEvents.NETHERITE_BLOCK_STEP, soundtype.getVolume() * 0.30F, soundtype.getPitch()-0.6f);
            }else {
                this.playSound(SoundEvents.GRASS_STEP, soundtype.getVolume() * 0.30F, soundtype.getPitch()-0.6f);
            }

        }
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.STYRACO_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.STYRACO_DEATH.get();
    }

    protected SoundEvent getAmbientSound() {

        if(this.isAngry()){
            return ModSounds.STYRACO_ANGRY.get();
        }
        else {
            return ModSounds.STYRACO_IDLE.get();
        }
    }

    protected float getSoundVolume() {
        return 1.2F;
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_REMAINING_ANGER_TIME, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("isSitting", this.isSitting());
        tag.putBoolean("isSaddled", this.isSaddled());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.readPersistentAngerSaveData(this.level, tag);
    }

    /* VARIANTS */
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_146746_, DifficultyInstance p_146747_,
                                        MobSpawnType p_146748_, @Nullable SpawnGroupData p_146749_,
                                        @Nullable CompoundTag p_146750_) {
        StyracoVariant variant = Util.getRandom(StyracoVariant.values(), this.random);
        setVariant(variant);
        return super.finalizeSpawn(p_146746_, p_146747_, p_146748_, p_146749_, p_146750_);
    }

    public StyracoVariant getVariant() {
        return StyracoVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(StyracoVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }
    //

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.entityData.get(DATA_REMAINING_ANGER_TIME);
    }

    @Override
    public void setRemainingPersistentAngerTime(int pTime) {
        this.entityData.set(DATA_REMAINING_ANGER_TIME, pTime);
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }
}
