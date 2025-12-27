package net.jrex.rexcraft.entity.custom;

import net.jrex.rexcraft.entity.ModEntityTypes;
import net.jrex.rexcraft.entity.variant.BucklandiiVariant;
import net.jrex.rexcraft.item.ModItems;
import net.jrex.rexcraft.sound.ModSounds;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;


//Things for being angry:
//Use these things to be able to make this mob angry, make sure you have everything, or it will not work.
import java.util.function.Predicate;

public class BucklandiiEntity extends AbstractCombatDino {

    // Not in AT
    public static final Predicate<LivingEntity> PREY_SELECTOR = (p_30437_) -> {
        EntityType<?> entitytype = p_30437_.getType();
        return entitytype == EntityType.VILLAGER || entitytype == EntityType.COW || entitytype == EntityType.SHEEP || entitytype == EntityType.PIG
                || entitytype == EntityType.LLAMA || entitytype == EntityType.HORSE || entitytype == EntityType.CHICKEN || entitytype == ModEntityTypes.BOREAL.get() ||
                entitytype == EntityType.PLAYER || entitytype == EntityType.DONKEY || entitytype == EntityType.MULE
                || entitytype == EntityType.SPIDER || entitytype == EntityType.ZOMBIE || entitytype == EntityType.SKELETON
                || entitytype == ModEntityTypes.VELO.get() || entitytype == ModEntityTypes.PROTO.get() || entitytype == ModEntityTypes.ORO.get() || entitytype == ModEntityTypes.JAKA.get() || entitytype == ModEntityTypes.PRENO.get();
    };

    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(BucklandiiEntity.class, EntityDataSerializers.INT);

    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 40);

    //speed modifier of the entity when being ridden
    public float getSpeedMod(){
        return 0.001f;
    }

    //set length of the challenge animation
    public void setAnimLen(){
        this.challenge_time = 69;
    }

    public static int attacknum = 3;

    @Override
    public float getRiderOffset(){
        return 0.4f;
    }

    public float getStepHeight(){
        return 1.0F;
    }

    @Override
    public float getBaseSpeed(){
        return 0.2f;
    }

    public BucklandiiEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    //Not in AT
    public static AttributeSupplier setAttributes() {

        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.ATTACK_DAMAGE, 9.0f)
                .add(Attributes.ATTACK_SPEED, 1.0f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 3)
                .add(Attributes.MOVEMENT_SPEED, 0.2f).build();
    }

    // Not in AT
    @Override
    public void setTame(boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(45.0D);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(10.0D);
            getAttribute(Attributes.ATTACK_SPEED).setBaseValue(1.1f);
            getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(3f);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2f);


        } else {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(40.0D);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(9.0D);
            getAttribute(Attributes.ATTACK_SPEED).setBaseValue(1.1f);
            getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(3f);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2f);
        }
    }

    //Not in AT
    @Override
    protected void registerGoals() {

            this.goalSelector.addGoal(1, new FloatGoal(this));
            this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
            this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 2.0D, 10.0F, 4.0F, false));
            this.goalSelector.addGoal(2, new FollowParentGoal(this, 1.1D));
            this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 2.1D, false));
            this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
            this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F));
            this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
            this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

            this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
            this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
            this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
            //This target is for when it's mad at the player, without this it will not fight back
            //ok maybe not
            //this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
            //this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Cow.class, true));
            //this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Pig.class, true));
            //this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Sheep.class, true));
            this.targetSelector.addGoal(4, new NonTameRandomTargetGoal<>(this, LivingEntity.class, false, PREY_SELECTOR));
            this.targetSelector.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    //Not in AT
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob mob) {
        BucklandiiEntity baby = ModEntityTypes.BUCKLANDII.get().create(serverLevel);
        BucklandiiVariant variant = Util.getRandom(BucklandiiVariant.values(), this.random);
        baby.setVariant(variant);
        return baby;
    }

    //taming item
    public boolean tameItem(ItemStack pStack){
        Item item = pStack.getItem();
        return item == ModItems.CARNO_BUFF_GOLD.get() || item == ModItems.CARNO_BUFF_DIAMOND.get() || item == ModItems.CARNO_BUFF_NETH.get();
    }


    //Not in AT
    @Override
    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
        if (!pBlock.getMaterial().isLiquid()) {
            BlockState blockstate = this.level.getBlockState(pPos.above());
            SoundType soundtype = pBlock.getSoundType(level, pPos, this);
            if (blockstate.is(Blocks.SNOW)) {
                soundtype = blockstate.getSoundType(level, pPos, this);
            }

            if (soundtype == SoundType.WOOD) {
                this.playSound(SoundEvents.WOOD_STEP, soundtype.getVolume() * 0.25F, soundtype.getPitch());
            }
            if (soundtype == SoundType.STONE) {
                this.playSound(SoundEvents.STONE_STEP, soundtype.getVolume() * 0.25F, soundtype.getPitch());
            }
            if (soundtype == SoundType.NETHERITE_BLOCK) {
                this.playSound(SoundEvents.NETHERITE_BLOCK_STEP, soundtype.getVolume() * 0.25F, soundtype.getPitch());
            }else {
                this.playSound(SoundEvents.GRASS_STEP, soundtype.getVolume() * 0.25F, soundtype.getPitch());
            }

        }
    }

    //Not in AT
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.BUCKLANDII_HURT.get();
    }

    //Not in AT
    protected SoundEvent getDeathSound() {
        return ModSounds.BUCKLANDII_DEATH.get();
    }

    //Not in AT
    protected SoundEvent getAmbientSound() {

        if(this.isAngry()){
            return ModSounds.BUCKLANDII_ANGRY.get();
        }
        else {
            return ModSounds.BUCKLANDII_GROWL.get();
        }
    }

    //Not in AT
    protected float getSoundVolume() {
        return 0.6F;
    }


    //Not in AT
    @Override
    protected int calculateFallDamage(float pDistance, float pDamageMultiplier) {
        if (pDistance < 3.5){
            return 0;
        }else{
            return Mth.ceil((pDistance * 8.0F) * pDamageMultiplier);
        }
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

    //Not in AT
    /* VARIANTS */
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_146746_, DifficultyInstance p_146747_,
                                        MobSpawnType p_146748_, @Nullable SpawnGroupData p_146749_,
                                        @Nullable CompoundTag p_146750_) {
        BucklandiiVariant variant = Util.getRandom(BucklandiiVariant.values(), this.random);
        setVariant(variant);
        return super.finalizeSpawn(p_146746_, p_146747_, p_146748_, p_146749_, p_146750_);
    }

    public BucklandiiVariant getVariant() {
        return BucklandiiVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(BucklandiiVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }
    //

    // Not in AT
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


    //These are for attempting to create a custom goal:

//    /**
//     * Loops through nearby animals and finds another animal of the same type that can be mated with. Returns the first
//     * valid mate found.
//     */
//    @javax.annotation.Nullable
//    private Animal getFreePartner() {
//        List<? extends Animal> list = this.level.getNearbyEntities(this.partnerClass, PARTNER_TARGETING, this.animal, this.animal.getBoundingBox().inflate(8.0D));
//        double d0 = Double.MAX_VALUE;
//        Animal animal = null;
//
//        for(Animal animal1 : list) {
//            if (this.animal.canMate(animal1) && this.animal.distanceToSqr(animal1) < d0) {
//                animal = animal1;
//                d0 = this.animal.distanceToSqr(animal1);
//            }
//        }
//
//        return animal;
//    }

//    static class LlamaAttackWolfGoal extends NearestAttackableTargetGoal<Wolf> {
//        public LlamaAttackWolfGoal(Llama pLlama) {
//            super(pLlama, Wolf.class, 16, false, true, (p_30845_) -> {
//                return !((Wolf)p_30845_).isTame();
//            });
//        }
//
//        protected double getFollowDistance() {
//            return super.getFollowDistance() * 0.25D;
//        }
//    }

//    class PolarBearAttackPlayersGoal extends NearestAttackableTargetGoal<Player> {
//        public PolarBearAttackPlayersGoal() {
//            super(PolarBear.this, Player.class, 20, true, true, (Predicate<LivingEntity>)null);
//        }
//
//        /**
//         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
//         * method as well.
//         */
//        public boolean canUse() {
//            if (PolarBear.this.isBaby()) {
//                return false;
//            } else {
//                if (super.canUse()) {
//                    for(PolarBear polarbear : PolarBear.this.level.getEntitiesOfClass(PolarBear.class, PolarBear.this.getBoundingBox().inflate(8.0D, 4.0D, 8.0D))) {
//                        if (polarbear.isBaby()) {
//                            return true;
//                        }
//                    }
//                }
//
//                return false;
//            }
//        }
//
//        protected double getFollowDistance() {
//            return super.getFollowDistance() * 0.5D;
//        }
//    }
}
