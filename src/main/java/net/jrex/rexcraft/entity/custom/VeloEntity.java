package net.jrex.rexcraft.entity.custom;

import net.jrex.rexcraft.entity.ModEntityTypes;
import net.jrex.rexcraft.entity.variant.VeloVariant;
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
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.scores.Team;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;


public class VeloEntity extends TamableAnimal implements IAnimatable, NeutralMob {

    private static final EntityDataAccessor<Boolean> SITTING =
            SynchedEntityData.defineId(VeloEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(VeloEntity.class, EntityDataSerializers.INT);

    //Prey for T2 packs
    public static final Predicate<LivingEntity> PREY_SELECTOR_T2 = (p_30437_) -> {
        EntityType<?> entitytype = p_30437_.getType();
        return entitytype == EntityType.VILLAGER || entitytype == EntityType.SHEEP || entitytype == EntityType.PIG ||
                entitytype == EntityType.ENDERMITE || entitytype == EntityType.SILVERFISH || entitytype == EntityType.AXOLOTL ||
                entitytype == EntityType.TADPOLE || entitytype == EntityType.CAT || entitytype == EntityType.CHICKEN ||
                entitytype == EntityType.FROG || entitytype == EntityType.RABBIT || entitytype == ModEntityTypes.GECKO.get() ||
                entitytype == ModEntityTypes.HEDGY.get() || entitytype == EntityType.PLAYER || entitytype == EntityType.COW ||
                entitytype == EntityType.HORSE || entitytype == EntityType.LLAMA || entitytype == EntityType.DONKEY ||
                entitytype == EntityType.WOLF || entitytype == EntityType.TURTLE || entitytype == EntityType.EGG ||
                entitytype == EntityType.PILLAGER || entitytype == EntityType.FOX || entitytype == EntityType.MULE ||
                entitytype == EntityType.GOAT || entitytype == ModEntityTypes.BOREAL.get();
    };

    //Prey for T1 packs
    public static final Predicate<LivingEntity> PREY_SELECTOR_T1 = (p_30437_) -> {
        EntityType<?> entitytype = p_30437_.getType();
            return entitytype == EntityType.VILLAGER || entitytype == EntityType.SHEEP || entitytype == EntityType.PIG ||
                    entitytype == EntityType.ENDERMITE || entitytype == EntityType.SILVERFISH || entitytype == EntityType.AXOLOTL ||
                    entitytype == EntityType.TADPOLE || entitytype == EntityType.CAT || entitytype == EntityType.CHICKEN ||
                    entitytype == EntityType.FROG || entitytype == EntityType.RABBIT || entitytype == ModEntityTypes.GECKO.get() ||
                    entitytype == ModEntityTypes.HEDGY.get() || entitytype == EntityType.PLAYER || entitytype == EntityType.COW ||
                    entitytype == EntityType.HORSE || entitytype == EntityType.LLAMA || entitytype == EntityType.DONKEY ||
                    entitytype == EntityType.WOLF || entitytype == EntityType.TURTLE || entitytype == EntityType.EGG ||
                    entitytype == EntityType.PILLAGER || entitytype == EntityType.FOX || entitytype == EntityType.MULE;
    };

    //Base prey for single raptors
    public static final Predicate<LivingEntity> PREY_SELECTOR = (p_30437_) -> {
        EntityType<?> entitytype = p_30437_.getType();
        return  entitytype == EntityType.ENDERMITE || entitytype == EntityType.SILVERFISH || entitytype == EntityType.AXOLOTL ||
                entitytype == EntityType.TADPOLE || entitytype == EntityType.CAT || entitytype == EntityType.CHICKEN ||
                entitytype == EntityType.FROG || entitytype == EntityType.RABBIT || entitytype == ModEntityTypes.GECKO.get() || entitytype == ModEntityTypes.HEDGY.get();
    };
    
    //Chooses a number for the id
    public static int choose_id(){
        Random rand = new Random();
        return rand.nextInt(100000);
    }

    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(VeloEntity.class, EntityDataSerializers.INT);

    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(15, 25);

    //speed modifier of the entity when being ridden
    public static float speedMod = 0.001f;

    public static int attacknum = 3;

    public static float riderOffset = 0.0f;

    public static float step_height = 1.0F;
    
    public static int raptor_num = choose_id();
    
    @Nullable
    private UUID persistentAngerTarget;

    //private int destroyBlocksTick;

    private AnimationFactory factory = new AnimationFactory(this);



    public VeloEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier setAttributes() {

        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.ATTACK_DAMAGE, 5.0f)
                .add(Attributes.ATTACK_SPEED, 1.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.17f).build();
    }

    @Override
    protected void registerGoals() {

            this.goalSelector.addGoal(1, new FloatGoal(this));
            this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
            this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 2.0D, 10.0F, 4.0F, false));
            this.goalSelector.addGoal(2, new FollowParentGoal(this, 1.1D));
            this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 2.5D, false));
            this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
            this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Player.class, 8.0F, 1.8D, 1.8D));
            this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F));
            this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
            this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

            this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
            this.targetSelector.addGoal(1, new VeloAttackEntitiesT2Goal());
            this.targetSelector.addGoal(1, new VeloAttackEntitiesT1Goal());
            this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
            this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
            this.targetSelector.addGoal(4, new NonTameRandomTargetGoal<>(this, LivingEntity.class, false, PREY_SELECTOR));
            this.targetSelector.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, true));
    }


    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {

        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", true));
            return PlayState.CONTINUE;

        }
        if (this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("sitting", true));
            return PlayState.CONTINUE;
        }

        //if the entity is not moving or sitting, and has a current animation:

        if(!event.isMoving() && !this.isSitting() && event.getController().getCurrentAnimation() != null){
            String name = event.getController().getCurrentAnimation().animationName;

            //if that animation is anything other than an idle, just override it and set it to idle0
            if(name.equals("walk") || name.equals("vehicle_walk") || name.equals("sitting")){
                event.getController().markNeedsReload();
                int rand_int = rand_num();
                event.getController().setAnimation(new AnimationBuilder().addAnimation("idle" + rand_int, false));
            }
            //if it's already idling, then just wait for the current idle anim to be over and choose a random one for the next loop
            if(event.getController().getAnimationState().equals(AnimationState.Stopped)){
                event.getController().markNeedsReload();

                //a random number is chosen between 0 and 2, then added to the end of "idle" to get a random idle animation!
                int rand_int = rand_num();

                event.getController().setAnimation(new AnimationBuilder().addAnimation("idle" + rand_int, false));
                //System.out.print(rand_int);
            }

        }

        return PlayState.CONTINUE;
    }

    /** Chooses a random number between 0 and 9, then returns 0 or 1 based on that. For Velo, 0 and 2 are the ones I want to loop more often**/
    /** 012 / 3456 / 789 **/
    protected int rand_num(){
        Random rand = new Random();
        int rand_num = rand.nextInt(10);

        if (this.isAngry()){
        // 0 / 12345 / 6789
            if(rand_num == 0){
                return 2;
            }
            if(rand_num > 5){
                return 0;
            }
            else{
                return 1;
            }

        }
        else{
            if(rand_num > 2 && rand_num < 6){
                return 0;
            }
            if(rand_num > 6){
                return 2;
            }
            else{
                return 1;
            }
        }
    }

    private PlayState attackPredicate(AnimationEvent event) {

        if(this.swinging && event.getController().getAnimationState().equals(AnimationState.Stopped)){
            event.getController().markNeedsReload();

            //a random number is chosen between 0 and attacknum, then added to the end of "attack" to get a random attack animation!

            Random rand = new Random();

            int upperbound = attacknum;

            int rand_int = rand.nextInt(upperbound);

            event.getController().setAnimation(new AnimationBuilder().addAnimation("attack" + rand_int, false));
            //System.out.print(rand_int);

            this.swinging = false;
        }

        return PlayState.CONTINUE;
    }

//    @Deprecated //Forge: DO NOT USE use BlockState.canEntityDestroy
//    public static boolean canDestroy(BlockState pState) {
//        return !pState.isAir() && !pState.is(BlockTags.WITHER_IMMUNE);
//    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob mob) {
        VeloEntity baby = ModEntityTypes.VELO.get().create(serverLevel);
        VeloVariant variant = Util.getRandom(VeloVariant.values(), this.random);
        baby.setVariant(variant);
        return baby;
    }

    @Override
    public boolean isFood(ItemStack pStack){
        Item item = pStack.getItem();
        return item == ModItems.BEEFSTACK.get() || item == ModItems.PORKSTACK.get() || item == ModItems.CHICKENSTACK.get() || item == ModItems.MUTTONSTACK.get();
    }

    //Used as the healing item, in the case of the gecko it's a cricket
    //look into wolf class to see how meat works
    public boolean isHeal(ItemStack pStack){
        Item item = pStack.getItem();
        return item.isEdible() && pStack.getFoodProperties(this).isMeat();
    }

    //taming item
    public boolean tameItem(ItemStack pStack){
        Item item = pStack.getItem();
        return item == ModItems.CARNO_BUFF_IRON.get() || item == ModItems.CARNO_BUFF_GOLD.get() || item == ModItems.CARNO_BUFF_DIAMOND.get() || item == ModItems.CARNO_BUFF_NETH.get();
    }
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this,"controller",0,this::predicate));
        data.addAnimationController(new AnimationController(this,"attackController",0,this::attackPredicate));

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.GRASS_STEP, 0.15F, 4.0F);
    }

    //ToDo Remember to set the velo sounds!
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.BUCKLANDII_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.BUCKLANDII_DEATH.get();
    }

    protected SoundEvent getAmbientSound() {

        if(this.isAngry()){
            return ModSounds.BUCKLANDII_ANGRY.get();
        }
        else {
            return ModSounds.BUCKLANDII_GROWL.get();
        }
    }

    protected SoundEvent getSwimSound() {
        return SoundEvents.GENERIC_SWIM;
    }

    protected SoundEvent getSwimSplashSound() {
        return SoundEvents.GENERIC_SPLASH;
    }

    protected float getSoundVolume() {
        return 0.8F;
    }

    protected boolean isImmobile() {
        return super.isImmobile();
    }

    public void aiStep() {
        super.aiStep();

//        if(this.isAngry()){
//            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0f);
//        }
//        else if (!this.isAngry()){
//            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2f);
//        }

        if (!this.level.isClientSide && this.isAlive()) {
            if (this.random.nextInt(900) == 0 && this.deathTime == 0) {
                this.heal(1.0F);
            }
        }

        if (!this.level.isClientSide) {
            this.updatePersistentAnger((ServerLevel)this.level, true);
        }

    }


    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();

        //if the item "isFood", just use for taming
        if(isFood(itemstack)){
            return super.mobInteract(player, hand);
        }

        //if the item "isHeal" and the current health is less than the max health of the mob, eat the food and heal
        if(this.isHeal(itemstack) && this.getHealth() < this.getMaxHealth()){
            if (!player.getAbilities().instabuild) {
                itemstack.shrink(1);
            }
            this.heal(2);
            this.gameEvent(GameEvent.EAT, this);
            this.spawnTamingParticles(true);
            return InteractionResult.SUCCESS;

        }

        //if this is the item for taming, tame and set to sit
        if (this.tameItem(itemstack) && !isTame()) {
            if (this.level.isClientSide) {
                return InteractionResult.CONSUME;
            } else {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                if (!ForgeEventFactory.onAnimalTame(this, player)) {
                    if (!this.level.isClientSide) {
                        super.tame(player);
                        this.navigation.recomputePath();
                        this.setTarget(null);
                        this.level.broadcastEntityEvent(this, (byte)7);
                        setSitting(true);
                    }
                }

                return InteractionResult.SUCCESS;
            }
        }

        //this is for controlling it
        if (this.isVehicle()) {
            return super.mobInteract(player, hand);
        }

        //sit and unsit by crouching and right clicking
        if(isTame() && !this.level.isClientSide && hand == InteractionHand.MAIN_HAND && player.isCrouching()) {
            setSitting(!isSitting());
            return InteractionResult.SUCCESS;
        }

        //ride by right clicking with an empty hand
//        if(isTame() && !this.level.isClientSide && hand == InteractionHand.MAIN_HAND && !this.isSitting()) {
//            this.doPlayerRide(player);
//            return InteractionResult.SUCCESS;
//        }

        if (this.tameItem(itemstack)) {
            return InteractionResult.PASS;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setSitting(tag.getBoolean("isSitting"));
        this.entityData.set(DATA_ID_TYPE_VARIANT,tag.getInt("Variant"));

        //I think this saves if it's angry?
        this.readPersistentAngerSaveData(this.level, tag);

    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("isSitting", this.isSitting());
        tag.putInt("Variant",this.getTypeVariant());
        this.addPersistentAngerSaveData(tag);
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SITTING, false);
        this.entityData.define(DATA_ID_TYPE_VARIANT,0);
        this.entityData.define(DATA_REMAINING_ANGER_TIME, 0);
    }

    @Override
    public Team getTeam() {
        return super.getTeam();
    }

    public boolean canBeLeashed(Player player) {
        return true;
    }

    @Override
    public void setTame(boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(15.0D);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(5.0D);
            getAttribute(Attributes.ATTACK_SPEED).setBaseValue(1.1f);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.17f);


        } else {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(10.0D);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(5.0D);
            getAttribute(Attributes.ATTACK_SPEED).setBaseValue(1.1f);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.17f);
        }
    }

    /* VARIANTS */
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_146746_, DifficultyInstance p_146747_,
                                        MobSpawnType p_146748_, @Nullable SpawnGroupData p_146749_,
                                        @Nullable CompoundTag p_146750_) {
        VeloVariant variant = Util.getRandom(VeloVariant.values(), this.random);
        setVariant(variant);
        return super.finalizeSpawn(p_146746_, p_146747_, p_146748_, p_146749_, p_146750_);
    }

    public VeloVariant getVariant() {
        return VeloVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(VeloVariant variant) {
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

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@javax.annotation.Nullable UUID pTarget) {
        this.persistentAngerTarget = pTarget;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    public void setSitting(boolean sitting) {
        this.entityData.set(SITTING, sitting);
        this.setOrderedToSit(sitting);
    }


    public boolean isSitting() {
        return this.entityData.get(SITTING);
    }

    // Tier 1 Entities: Players, Villagers, and other's of that size range.
    // A single raptor would not try to hunt these, but when backed up by a small pack, these are now game.
    class VeloAttackEntitiesT1Goal extends NearestAttackableTargetGoal<LivingEntity> {
        public VeloAttackEntitiesT1Goal() {
            super(VeloEntity.this, LivingEntity.class, 20, true, true, PREY_SELECTOR_T1);
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {

            //Number of velos needed to attack this tier of entity
            int total_needed = 3;

            //Total velos
            int total_velos = 0;
            /*
             * If this velo is a baby, or it's a tamed raptor, return false (don't attack the player)
             * */
            if (VeloEntity.this.isBaby() || VeloEntity.this.isTame()) {
                return false;
            } else {
                if (super.canUse()) {
                    // For every velo around, if it's not a baby, count it
                    for(VeloEntity velo : VeloEntity.this.level.getEntitiesOfClass(VeloEntity.class, VeloEntity.this.getBoundingBox().inflate(15.0D, 8.0D, 15.0D))) {
                        if (!velo.isBaby()) {
                            total_velos += 1;
                        }
                    }

                    //if there are at least 3 raptors around, attack the player
                    //else, do not attack the player
                    return total_velos >= total_needed;

                }

                return false;
            }
        }

        protected double getFollowDistance() {
            return super.getFollowDistance() * 1.4D;
        }
    }

    class VeloAttackEntitiesT2Goal extends NearestAttackableTargetGoal<LivingEntity> {
        public VeloAttackEntitiesT2Goal() {
            super(VeloEntity.this, LivingEntity.class, 20, true, true, PREY_SELECTOR_T2);
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {

            //Number of velos needed to attack this tier of entity
            int total_needed = 5;

            //Total velos
            int total_velos = 0;
            /*
             * If this velo is a baby, or it's a tamed raptor, return false (don't attack the player)
             * */
            if (VeloEntity.this.isBaby() || VeloEntity.this.isTame()) {
                return false;
            } else {
                if (super.canUse()) {
                    // For every velo around, if it's not a baby, count it
                    for(VeloEntity velo : VeloEntity.this.level.getEntitiesOfClass(VeloEntity.class, VeloEntity.this.getBoundingBox().inflate(15.0D, 8.0D, 15.0D))) {
                        if (!velo.isBaby()) {
                            total_velos += 1;
                        }
                    }

                    //if there are at least 3 raptors around, attack the player
                    //else, do not attack the player
                    return total_velos >= total_needed;

                }

                return false;
            }
        }

        protected double getFollowDistance() {
            return super.getFollowDistance() * 1.4D;
        }
    }
    
    class VeloFollowLeaderGoal extends FollowParentGoal {
        private final VeloEntity animal;
        private Animal parent;

        public VeloFollowLeaderGoal(VeloEntity pAnimal, double pSpeedModifier) {
            super(pAnimal, pSpeedModifier);
            this.animal = pAnimal;
        }

        @Override
        public boolean canUse() {
            if (this.animal.getAge() <= 0) {
                return false;
            } else {
                List<? extends VeloEntity> list = this.animal.level.getEntitiesOfClass(this.animal.getClass(), this.animal.getBoundingBox().inflate(8.0D, 4.0D, 8.0D));
                Animal animal = null;
                double d0 = Double.MAX_VALUE;

                for (VeloEntity animal1 : list) {
                    if (animal1.getAge() >= 0 && raptor_num == 9) {
                        double d1 = this.animal.distanceToSqr(animal1);
                        if (!(d1 > d0)) {
                            d0 = d1;
                            animal = animal1;
                        }
                    }
                }

                if (animal == null) {
                    return false;
                } else if (d0 < 9.0D) {
                    return false;
                } else {
                    this.parent = animal;
                    return true;
                }
            }
        }
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

    // have it return the PRAY_SELECTION based on who's around

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
