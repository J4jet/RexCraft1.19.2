package net.jrex.rexcraft.entity.custom;

import net.jrex.rexcraft.entity.ModEntityTypes;
import net.jrex.rexcraft.entity.variant.BernisVariant;
import net.jrex.rexcraft.entity.variant.BorealVariant;
import net.jrex.rexcraft.item.ModItems;
import net.jrex.rexcraft.sound.ModSounds;
import net.minecraft.Util;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
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

import java.util.*;

public class BorealEntity extends AbstractChestedHorse implements IAnimatable, NeutralMob {

    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(BorealEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(BorealEntity.class, EntityDataSerializers.INT);

    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(60, 90);

    protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(BorealEntity.class, EntityDataSerializers.BYTE);
    protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID_ID = SynchedEntityData.defineId(BorealEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    //speed modifier of the entity when being ridden
    public static float speedMod = 0.0f;

    public static int attacknum = 3;

    @Nullable
    private UUID persistentAngerTarget;

    private AnimationFactory factory = new AnimationFactory(this);

    public BorealEntity(EntityType<? extends AbstractChestedHorse> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier setAttributes() {

        return AbstractChestedHorse.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 25.0D)
                .add(Attributes.ATTACK_DAMAGE, 8.0f)
                .add(Attributes.ATTACK_SPEED, 1.5f)
                .add(Attributes.ARMOR,5.0)
                .add(Attributes.ARMOR_TOUGHNESS,5.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 3)
                .add(Attributes.MOVEMENT_SPEED, 0.16f).build();
    }

    //no randomized attributes!
    @Override
    protected void randomizeAttributes(RandomSource p_218803_) {
        Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(25.0);
    }

    @Override
    protected int calculateFallDamage(float pDistance, float pDamageMultiplier) {
        return Mth.ceil((pDistance * 8.0F) * pDamageMultiplier);
    }

    @Override
    protected void registerGoals() {

        this.goalSelector.addGoal(1, new FloatGoal(this));
        //this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        //this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 2.0D, 10.0F, 6.0F, false));
        this.goalSelector.addGoal(2, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(4, new BreedGoal(this, 1.0D, BorealEntity.class));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, BucklandiiEntity.class, 10.0F, 1.2D, 1.4D));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        //this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new BorealEntity.DinoOwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        //this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {

        //if in water, use swimming anims

        if (this.isSwimming() || this.isVisuallySwimming() || this.isInWater()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("swimming", true));
            return PlayState.CONTINUE;

        }

        if (event.isMoving()) {
            if (this.isVehicle()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", true));
                return PlayState.CONTINUE;
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", true));
                return PlayState.CONTINUE;
            }

        }

        if(!event.isMoving() && event.getController().getCurrentAnimation() != null){
            String name = event.getController().getCurrentAnimation().animationName;

            //if that animation is anything other than an idle, just override it and set it to idle0
            if(name.equals("walk") || name.equals("vehicle_walk") || name.equals("swimming")){
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
    protected int rand_num(){
        Random rand = new Random();
        int rand_num = rand.nextInt(10);

        if (this.isAngry()){
            return 0;
        }

        if(rand_num > 6){
            return 1;
        }
        else{
            return 0;
        }
    }

    private PlayState attackPredicate(AnimationEvent event) {

        if (this.swinging && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            event.getController().markNeedsReload();

            //a random number is chosen between 0 and attacknum, then added to the end of "attack" to get a random attack animation!

            Random rand = new Random();

            int upperbound = attacknum;

            int rand_int = rand.nextInt(upperbound);

            if(rand_int == 0){
                event.getController().setAnimation(new AnimationBuilder().addAnimation("attack" + "1", false));
            }
            else{
                event.getController().setAnimation(new AnimationBuilder().addAnimation("attack" + rand_int, false));
            }
            this.swinging = false;
        }

        return PlayState.CONTINUE;
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob mob) {
        BorealEntity baby = ModEntityTypes.BOREAL.get().create(serverLevel);
        BorealVariant variant = Util.getRandom(BorealVariant.values(), this.random);
        baby.setVariant(variant);
        return baby;
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.getItem() == ModItems.RAB_STEW.get();
    }

    public boolean isHeal(ItemStack pStack) {
        Item item = pStack.getItem();
        return item.isEdible() && (item == ModItems.ZUCC.get() || item == ModItems.BLUEBERRY.get() || item == Items.WHEAT || item == Items.CARROT) ;
    }

    //taming item
    public boolean tameItem(ItemStack pStack) {
        Item item = pStack.getItem();
        return item == ModItems.HERB_BUFF_GOLD.get() || item == ModItems.HERB_BUFF_DIAMOND.get() || item == ModItems.HERB_BUFF_NETH.get();
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
        data.addAnimationController(new AnimationController(this, "attackController", 0, this::attackPredicate));

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {

        int i = this.calculateFallDamage(pFallDistance, pMultiplier);
        if (i <= 0) {
            return false;
        } else {
            this.hurt(pSource, (float)i);
            if (this.isVehicle()) {
                for(Entity entity : this.getIndirectPassengers()) {
                    entity.hurt(pSource, (float)i);
                }
            }

            this.playBlockFallSound();
            return true;
        }
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

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.BOREAL_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.BOREAL_DEATH.get();
    }

    protected SoundEvent getAmbientSound() {

        if (this.isAngry()) {
            return ModSounds.BOREAL_ANGRY.get();
        } else {
            return ModSounds.BOREAL_IDLE.get();
        }
    }

    @Override
    protected @NotNull SoundEvent getSwimSound() {
        return SoundEvents.GENERIC_SWIM;
    }

    @Override
    protected @NotNull SoundEvent getSwimSplashSound() {
        return SoundEvents.GENERIC_SPLASH;
    }

    protected float getSoundVolume() {
        return 0.5F;
    }

    protected boolean isImmobile() {
        return super.isImmobile() && this.isVehicle() && this.isSaddled();
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level.isClientSide && this.isAlive()) {
            //System.out.println(this.getHealth());
            if (this.random.nextInt(900) == 0 && this.deathTime == 0) {
                this.heal(2.0F);
            }
            this.followMommy();
        }
        if(this.isAngry()){
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0f);
            getAttribute(Attributes.ARMOR).setBaseValue(16.0f);
            getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(20.0f);
        }
        else if (!this.isAngry()){
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2f);
            getAttribute(Attributes.ARMOR).setBaseValue(5.0f);
            getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(3.0f);
        }

        if (!this.level.isClientSide) {
            this.updatePersistentAnger((ServerLevel)this.level, true);
        }

    }

    @Override
    public boolean isSaddleable() {
        return false;
    }

    protected void reassessTameGoals() {
    }

    public void setTame(boolean pTamed) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        if (pTamed) {
            this.entityData.set(DATA_FLAGS_ID, (byte)(b0 | 4));
        } else {
            this.entityData.set(DATA_FLAGS_ID, (byte)(b0 & -5));
        }

        this.reassessTameGoals();
    }

    public void tame(Player pPlayer) {
        this.setTame(true);
        this.setOwnerUUID(pPlayer.getUUID());
        if (pPlayer instanceof ServerPlayer) {
            CriteriaTriggers.TAME_ANIMAL.trigger((ServerPlayer)pPlayer, this);
        }

    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (!this.isBaby()) {
            if (this.isTamed() && player.isSecondaryUseActive()) {
                this.doPlayerRide(player);
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }

            if (this.isVehicle()) {
                return super.mobInteract(player, hand);
            }
        }

        if (!itemstack.isEmpty()) {

            if (this.tameItem(itemstack) && !isTamed()) {
                if (this.level.isClientSide) {
                    return InteractionResult.CONSUME;
                } else {
                    if (!player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }

                    if (!ForgeEventFactory.onAnimalTame(this, player)) {
                        if (!this.level.isClientSide) {
                            this.tame(player);
                            this.setTamed(true);
                            this.spawnTamingParticles(true);
                            this.navigation.recomputePath();
                            this.setTarget(null);
                            this.level.broadcastEntityEvent(this, (byte)7);
                        }
                    }

                    return InteractionResult.SUCCESS;
                }
            }

            if (this.isFood(itemstack)) {

                if (!this.level.isClientSide && !this.isBaby() && this.canFallInLove()) {
                    this.usePlayerItem(player, hand, itemstack);
                    this.setInLove(player);
                    return InteractionResult.SUCCESS;
                }

                else if (this.isBaby()) {

                    this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), 0.0D, 0.0D, 0.0D);
                }

                if (this.level.isClientSide) {
                    return InteractionResult.CONSUME;
                }
            }

            if(this.isHeal(itemstack) && this.getHealth() < this.getMaxHealth()){
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
                this.heal(2);
                this.gameEvent(GameEvent.EAT, this);
                this.spawnTamingParticles(true);
                return InteractionResult.SUCCESS;

            }

            if (!this.hasChest() && this.isTamed() && !this.isBaby() && itemstack.is(Blocks.CHEST.asItem())) {
                this.setChest(true);
                this.playChestEquipsSound();
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                this.createInventory();
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }
            //from Bernisentity, I don't want Boreal to be rideable
//            if (!this.isBaby() && !this.isSaddled() && itemstack.is(Items.SADDLE)) {
//                this.openCustomInventoryScreen(player);
//                return InteractionResult.sidedSuccess(this.level.isClientSide);
//            }
        }

        if (this.isBaby()) {
            return super.mobInteract(player, hand);
        } else {
            this.openCustomInventoryScreen(player);
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        //setSaddled(tag.getBoolean("isSaddled"));
        this.entityData.set(DATA_ID_TYPE_VARIANT,tag.getInt("Variant"));
        this.readPersistentAngerSaveData(this.level, tag);

        UUID uuid;
        if (tag.hasUUID("Owner")) {
            uuid = tag.getUUID("Owner");
        } else {
            String s = tag.getString("Owner");
            uuid = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }

        if (uuid != null) {
            try {
                this.setOwnerUUID(uuid);
                this.setTame(true);
            } catch (Throwable throwable) {
                this.setTame(false);
            }
        }

//        this.readPersistentAngerSaveData(this.level, tag);
//        if (!tag.isEmpty()) {
//            this.inventory.setItem(1, itemstack);
//        }
//    }
//
      this.updateContainerEquipment();

    }

    @javax.annotation.Nullable
    public LivingEntity getOwner() {
        try {
            UUID uuid = this.getOwnerUUID();
            return uuid == null ? null : this.level.getPlayerByUUID(uuid);
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }

    @Override
    protected boolean canParent() {
        return !this.isVehicle() && !this.isPassenger() && !this.isBaby() && this.isInLove();
    }

    @Override
    public boolean canMate(Animal pOtherAnimal) {
        return pOtherAnimal != this && pOtherAnimal instanceof BorealEntity && this.canParent() && ((BorealEntity)pOtherAnimal).canParent();
    }

    public boolean canWearArmor() {
        return false;
    }

//    @Override
//    public int getInventoryColumns() {
//        return 8;
//    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        //Caused by: java.lang.ArrayIndexOutOfBoundsException: Index 2 out of bounds for length 2
        tag.putInt("Variant",this.getTypeVariant());


        if (this.getOwnerUUID() != null) {
            tag.putUUID("Owner", this.getOwnerUUID());
        }

        this.addPersistentAngerSaveData(tag);
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT,0);
        this.entityData.define(DATA_REMAINING_ANGER_TIME, 0);
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
        this.entityData.define(DATA_OWNERUUID_ID, Optional.empty());
    }

    @javax.annotation.Nullable
    public UUID getOwnerUUID() {
        return this.entityData.get(DATA_OWNERUUID_ID).orElse((UUID)null);
    }

    public void setOwnerUUID(@javax.annotation.Nullable UUID pUuid) {
        this.entityData.set(DATA_OWNERUUID_ID, Optional.ofNullable(pUuid));
    }

    public boolean isOwnedBy(LivingEntity pEntity) {
        return pEntity == this.getOwner();
    }

    public boolean canAttack(LivingEntity pTarget) {
        return this.isOwnedBy(pTarget) ? false : super.canAttack(pTarget);
    }

    public boolean isTame() {
        return (this.entityData.get(DATA_FLAGS_ID) & 4) != 0;
    }

    /**
     * Returns whether this Entity is on the same team as the given Entity.
     */
    public boolean isAlliedTo(Entity pEntity) {
        if (this.isTame()) {
            LivingEntity livingentity = this.getOwner();
            if (pEntity == livingentity) {
                return true;
            }

            if (livingentity != null) {
                return livingentity.isAlliedTo(pEntity);
            }
        }

        return super.isAlliedTo(pEntity);
    }

    @Override
    public boolean canJump() {
        return false;
    }

    @Override
    public Team getTeam() {
        return super.getTeam();
    }

    public boolean canBeLeashed(Player player) {
        return true;
    }

    @Override
    public void travel(@NotNull Vec3 pTravelVector) {

        if (this.isAlive()) {
            LivingEntity livingentity = this.getControllingPassenger();
            if (this.isVehicle() && livingentity != null) {
                this.setYRot(livingentity.getYRot());
                this.yRotO = this.getYRot();
                this.setXRot(livingentity.getXRot() * 0.5F);
                this.setRot(this.getYRot(), this.getXRot());
                this.yBodyRot = this.getYRot();
                this.yHeadRot = this.yBodyRot;
                float f = livingentity.xxa * 0.5F;
                float f1 = livingentity.zza;

                if (this.onGround) {
                    Vec3 vec3 = this.getDeltaMovement();
                    this.setDeltaMovement(vec3.x, 0, vec3.z);
                }

                if (this.isControlledByLocalInstance()) {
                    this.setSpeed((float)this.getAttributeValue(Attributes.MOVEMENT_SPEED) + speedMod);
                    super.travel(new Vec3((double)f, pTravelVector.y, (double)f1));
                } else if (livingentity instanceof Player) {
                    this.setDeltaMovement(Vec3.ZERO);
                }


                this.tryCheckInsideBlocks();
            } else {
                this.flyingSpeed = 0.02F;
                super.travel(pTravelVector);
            }
        }
    }

    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_146746_, DifficultyInstance p_146747_,
                                        MobSpawnType p_146748_, @Nullable SpawnGroupData p_146749_,
                                        @Nullable CompoundTag p_146750_) {
        BorealVariant variant = Util.getRandom(BorealVariant.values(), this.random);
        setVariant(variant);
        return super.finalizeSpawn(p_146746_, p_146747_, p_146748_, p_146749_, p_146750_);
    }

    public BorealVariant getVariant() {
        return BorealVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(BorealVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }


    //left off here, seems since it's not TamableAnimal it doesn't sit or follow.

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.entityData.get(DATA_REMAINING_ANGER_TIME);
    }
    @Override
    public void setRemainingPersistentAngerTime(int pRemainingPersistentAngerTime) {
        this.entityData.set(DATA_REMAINING_ANGER_TIME, pRemainingPersistentAngerTime);
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID pPersistentAngerTarget) {
        this.persistentAngerTarget = pPersistentAngerTarget;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    public boolean wantsToAttack(LivingEntity pTarget, LivingEntity pOwner) {
        return true;
    }

    /** OWNER HURT BY TARGET GOAL**/

    public class DinoOwnerHurtByTargetGoal extends TargetGoal {
        private final BorealEntity tameAnimal;
        private LivingEntity ownerLastHurtBy;
        private int timestamp;

        public DinoOwnerHurtByTargetGoal(BorealEntity dino) {
            super(dino, false);
            this.tameAnimal = dino;
            this.setFlags(EnumSet.of(Goal.Flag.TARGET));
        }


        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            if (this.tameAnimal.isTamed()) {
                LivingEntity livingentity = this.tameAnimal.getOwner();
                if (livingentity == null) {
                    return false;
                } else {
                    this.ownerLastHurtBy = livingentity.getLastHurtByMob();
                    int i = livingentity.getLastHurtByMobTimestamp();
                    return i != this.timestamp && this.canAttack(this.ownerLastHurtBy, TargetingConditions.DEFAULT) && this.tameAnimal.wantsToAttack(this.ownerLastHurtBy, livingentity);
                }
            } else {
                return false;
            }
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            this.mob.setTarget(this.ownerLastHurtBy);
            LivingEntity livingentity = this.tameAnimal.getOwner();
            if (livingentity != null) {
                this.timestamp = livingentity.getLastHurtByMobTimestamp();
            }

            super.start();
        }
    }
}
