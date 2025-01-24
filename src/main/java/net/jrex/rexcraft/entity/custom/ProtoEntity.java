package net.jrex.rexcraft.entity.custom;

import net.jrex.rexcraft.entity.ModEntityTypes;
import net.jrex.rexcraft.entity.variant.ProtoVariant;
import net.jrex.rexcraft.item.ModItems;
import net.jrex.rexcraft.sound.ModSounds;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;

public class ProtoEntity extends AbstractDiggingDino{

    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(ProtoEntity.class, EntityDataSerializers.INT);

    // ____ ANGRY STUFF _____ //
    public static final Predicate<LivingEntity> PREY_SELECTOR = (p_30437_) -> {
        EntityType<?> entitytype = p_30437_.getType();
        return entitytype == ModEntityTypes.VELO.get();
    };

    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(60, 70);

    @Nullable
    private UUID persistentAngerTarget;

    // ____            _____ //

    public static int attacknum = 3;

    public ProtoEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier setAttributes() {

        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 15.0D)
                .add(Attributes.ATTACK_DAMAGE, 2.0f)
                .add(Attributes.ATTACK_SPEED, 1.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.18f).build();
    }

    @Override
    //helper method to determine what item is found by the entity
    public Item handle_random_item(){
        // just get a random value and return an item based on that value
        Random rand = new Random();
        // the different items this entity can find when digging
        Item item_t1_1 = Blocks.SAND.asItem();
        Item item_t1_2 = Items.STICK;

        Item item_t2_1 = Items.STICK;
        Item item_t2_2 = Blocks.DEAD_BUSH.asItem();

        Item item_t3_1 = Blocks.SANDSTONE.asItem();
        Item item_t3_2 = Blocks.SAND.asItem();

        Item item_t4_1 = Items.GOLD_NUGGET;
        Item item_t4_2 = Items.RAW_GOLD;
        Item item_t4_3 = Items.FLINT;

        //oro can "find" these items at these %s:
        //snowballs, ice - 50%
        //sticks, seeds - 30%
        //iron nuggets, copper nuggets - 15%
        //raw iron, raw copper, coal - 5%

        int rand_num = rand.nextInt(100);
        // between 0-25
        if (rand_num > 0 && rand_num <= 25){
            return item_t1_1;
        }
        //between 26-50
        if (rand_num > 25 && rand_num <= 50){
            return item_t1_2;
        }
        //between 51-65
        if (rand_num > 50 && rand_num <= 65){
            return item_t2_1;
        }
        //between 66-80
        if (rand_num > 65 && rand_num <= 80){
            return item_t2_2;
        }
        //between 81-88
        if (rand_num > 80 && rand_num <= 88){
            return item_t3_1;
        }
        //between 89-95
        if (rand_num > 80 && rand_num <= 95){
            return item_t3_2;
        }
        //between 96-97
        if (rand_num > 96 && rand_num <= 98){
            return item_t4_1;
        }
        //between 98-99
        if (rand_num == 99){
            return item_t4_2;
        }
        else{
            return item_t4_3;
        }
    }

    @Override
    public Block getdiggingblock(){
        return Blocks.SAND;
    }

    @Override
    public Block getALTdiggingblock(){
        return Blocks.DEAD_BUSH;
    }

    @Override
    public Block get_placable_block(){
        return Blocks.SAND;
    }

    @Override
    public void setTame(boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(16.0D);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3.0D);
            getAttribute(Attributes.ATTACK_SPEED).setBaseValue(1.1f);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.17f);


        } else {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(15.0D);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(2.0D);
            getAttribute(Attributes.ATTACK_SPEED).setBaseValue(1.0f);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.17f);
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 2.5D, false));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, BucklandiiEntity.class, 10.0F, 2.5D, 2.5D));

        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new ProtoEntity.ProtoAttackPlayersGoal());
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(4, new NonTameRandomTargetGoal<>(this, LivingEntity.class, false, PREY_SELECTOR));
        this.targetSelector.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, true));

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

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {

        // if the entity is digging, play the digging animation.
        if (this.digAnimationTick > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("digging", false));
            return PlayState.CONTINUE;
        }

        //if in water, use swimming anims

        if (this.isSwimming() || this.isVisuallySwimming() || this.isInWater()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("swimming", true));
            return PlayState.CONTINUE;

        }

        if (event.isMoving() && this.onGround) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", true));
            return PlayState.CONTINUE;

        }
        if (this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("sitting", true));
            return PlayState.CONTINUE;
        }

        if(!this.onGround && event.getController().getCurrentAnimation() != null){
            String name = event.getController().getCurrentAnimation().animationName;

            //if that animation is anything other than falling, just override it and set it to falling
            if(name.equals("walk") || name.equals("vehicle_walk") || name.equals("sitting") || name.equals("idle0") || name.equals("idle1") || name.equals("idle2") || name.equals("swimming")){
                event.getController().markNeedsReload();
                event.getController().setAnimation(new AnimationBuilder().addAnimation("falling", false));
            }
            //if it's already falling, then just wait for the current fall anim to be over and choose a random one for the next loop
            if(event.getController().getAnimationState().equals(AnimationState.Stopped)){
                event.getController().markNeedsReload();
                event.getController().setAnimation(new AnimationBuilder().addAnimation("falling", false));
                //System.out.print(rand_int);
            }

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

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob pOtherParent) {
        ProtoEntity baby = ModEntityTypes.PROTO.get().create(serverLevel);
        ProtoVariant variant = Util.getRandom(ProtoVariant.values(), this.random);
        baby.setVariant(variant);
        return baby;
    }

    /* VARIANTS */
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_146746_, DifficultyInstance p_146747_,
                                        MobSpawnType p_146748_, @Nullable SpawnGroupData p_146749_,
                                        @Nullable CompoundTag p_146750_) {
        ProtoVariant variant = Util.getRandom(ProtoVariant.values(), this.random);
        setVariant(variant);
        return super.finalizeSpawn(p_146746_, p_146747_, p_146748_, p_146749_, p_146750_);
    }

    public ProtoVariant getVariant() {
        return ProtoVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(ProtoVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public void handleEntityEvent(byte pId) {
        if (pId == 10) {
            this.digAnimationTick = 40;
        } else {
            super.handleEntityEvent(pId);
        }

    }

    //DATA_ID_TYPE_VARIANT
    public int getProtoType() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this,"controller",0,this::predicate));
        data.addAnimationController(new AnimationController(this,"attackController",0,this::attackPredicate));

    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant",this.getTypeVariant());
        this.addPersistentAngerSaveData(tag);
    }

    // ____ ANGRY STUFF _____ //
    @Override
    protected float getSoundVolume() {
        return 0.2F;
    }

    // Override the amb sounds to get different sounds when angry
    @Override
    protected SoundEvent getAmbientSound() {

        if(this.isAngry()){
            return ModSounds.PROTO_ANGRY.get();
        }
        else {
            return ModSounds.PROTO_IDLE.get();
        }
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.PROTO_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.PROTO_DEATH.get();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.readPersistentAngerSaveData(this.level, tag);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_REMAINING_ANGER_TIME, 0);
    }

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

    // Modifying aiStep to add in the angry stuff //

    @Override
    public void aiStep() {
        if (!this.level.isClientSide) {
            this.updatePersistentAnger((ServerLevel)this.level, true);
        }
        super.aiStep();
    }


    //This is just PolarBearAttackPlayersGoal but for Proto

    class ProtoAttackPlayersGoal extends NearestAttackableTargetGoal<Player> {
        public ProtoAttackPlayersGoal() {
            super(ProtoEntity.this, Player.class, 20, true, true, (Predicate<LivingEntity>)null);
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            if (ProtoEntity.this.isBaby()) {
                return false;
            } else {
                if (super.canUse()) {
                    for(ProtoEntity proto : ProtoEntity.this.level.getEntitiesOfClass(ProtoEntity.class, ProtoEntity.this.getBoundingBox().inflate(8.0D, 4.0D, 8.0D))) {
                        if (proto.isBaby()) {
                            return true;
                        }
                    }
                }

                return false;
            }
        }

        protected double getFollowDistance() {
            return super.getFollowDistance() * 0.5D;
        }
    }


}
