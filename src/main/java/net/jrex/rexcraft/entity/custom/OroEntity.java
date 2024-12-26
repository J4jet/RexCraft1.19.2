package net.jrex.rexcraft.entity.custom;

import net.jrex.rexcraft.entity.ModEntityTypes;
import net.jrex.rexcraft.entity.variant.GeckoVariant;
import net.jrex.rexcraft.entity.variant.OroVariant;
import net.jrex.rexcraft.item.ModItems;
import net.jrex.rexcraft.sound.ModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
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

import java.util.EnumSet;
import java.util.Random;
import java.util.function.Predicate;

public class OroEntity extends TamableAnimal implements IAnimatable {

    private static final EntityDataAccessor<Boolean> SITTING =
            SynchedEntityData.defineId(OroEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> DIGGING =
            SynchedEntityData.defineId(OroEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(OroEntity.class, EntityDataSerializers.INT);

    private int eatAnimationTick;
    private EatBlockGoal eatBlockGoal;

    private AnimationFactory factory = new AnimationFactory(this);


    public OroEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier setAttributes() {

        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.ATTACK_DAMAGE, 0.5f)
                .add(Attributes.ATTACK_SPEED, 1.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.22f).build();
    }

    @Override
    public void setTame(boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(11.0D);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(0.5D);
            getAttribute(Attributes.ATTACK_SPEED).setBaseValue(1.1f);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.22f);


        } else {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(10.0D);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(0.5D);
            getAttribute(Attributes.ATTACK_SPEED).setBaseValue(1.0f);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.22f);
        }
    }

    @Override
    protected void registerGoals() {
        this.eatBlockGoal = new EatBlockGoal(this);
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 2.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(3, new PanicGoal(this, 2.0D));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(4, this.eatBlockGoal);
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.25D, Ingredient.of(ModItems.WORM.get()), false));

        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Player.class, 8.0F, 2.0D, 2.0D));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, VeloEntity.class, 8.0F, 2.5D, 2.5D));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, BucklandiiEntity.class, 8.0F, 2.5D, 2.5D));


        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

    }



    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {

        // if the entity is digging, play the digging animation.
        if (this.eatAnimationTick > 0) {
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
            if(name.equals("walk") || name.equals("sitting") || name.equals("idle0") || name.equals("idle1") || name.equals("idle") || name.equals("swimming")){
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
            if(name.equals("walk") || name.equals("sitting")){
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
            return 1;
        }
        else{
            return 1;
        }
    }

    private PlayState attackPredicate(AnimationEvent event) {

        if(this.swinging && event.getController().getAnimationState().equals(AnimationState.Stopped)){
            event.getController().markNeedsReload();
            event.getController().setAnimation(new AnimationBuilder().addAnimation("attack", false));
            this.swinging = false;
        }

        return PlayState.CONTINUE;
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob mob) {
        OroEntity baby = ModEntityTypes.ORO.get().create(serverLevel);
        OroVariant variant = Util.getRandom(OroVariant.values(), this.random);
        baby.setVariant(variant);
        return baby;
    }

    @Override
    public boolean isFood(ItemStack pStack){
        return pStack.getItem() == ModItems.DUBIA.get();
    }

    //Used as the healing item, in the case of the gecko it's a cricket
    public boolean isHeal(ItemStack pStack){
        return pStack.getItem() == ModItems.CRICKET_ITEM.get();
    }

    //DATA_ID_TYPE_VARIANT
    public int getOroType() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
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


    @Override
    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
        if (!pBlock.getMaterial().isLiquid()) {
            BlockState blockstate = this.level.getBlockState(pPos.above());
            SoundType soundtype = pBlock.getSoundType(level, pPos, this);
            if (blockstate.is(Blocks.SNOW)) {
                soundtype = blockstate.getSoundType(level, pPos, this);
            }

            if (soundtype == SoundType.WOOD) {
                this.playSound(SoundEvents.WOOD_STEP, soundtype.getVolume() * 0.15F, soundtype.getPitch());
            }
            if (soundtype == SoundType.STONE) {
                this.playSound(SoundEvents.STONE_STEP, soundtype.getVolume() * 0.15F, soundtype.getPitch());
            }
            if (soundtype == SoundType.NETHERITE_BLOCK) {
                this.playSound(SoundEvents.NETHERITE_BLOCK_STEP, soundtype.getVolume() * 0.15F, soundtype.getPitch());
            }else {
                this.playSound(SoundEvents.GRASS_STEP, soundtype.getVolume() * 0.15F, soundtype.getPitch());
            }

        }
    }


    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.GECKO_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.GECKO_DEATH.get();
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

    //helper method to determine what item is found by the entity
    public Item handle_random_item(){
        // just get a random value and return an item based on that value
        Random rand = new Random();

        //oro can "find" these items at these %s:
        //snowballs, ice - 50%
        //sticks, seeds - 30%
        //iron nuggets, copper nuggets - 15%
        //raw iron, raw copper, coal - 5%

        int rand_num = rand.nextInt(100);
        // between 0-25
        if (rand_num > 0 && rand_num <= 25){
            return Items.SNOWBALL;
        }
        //between 26-50
        if (rand_num > 25 && rand_num <= 50){
            return Items.ICE;
        }
        //between 51-65
        if (rand_num > 50 && rand_num <= 65){
            return Items.STICK;
        }
        //between 66-80
        if (rand_num > 65 && rand_num <= 80){
            return Items.WHEAT_SEEDS;
        }
        //between 81-88
        if (rand_num > 80 && rand_num <= 88){
            return Items.IRON_NUGGET;
        }
        //between 89-95
        if (rand_num > 80 && rand_num <= 95){
            return Items.RAW_COPPER;
        }
        //between 96-97
        if (rand_num > 96 && rand_num <= 98){
            return Items.COAL;
        }
        //between 98-99
        if (rand_num == 99){
            return Items.RAW_COPPER;
        }
        else{
            return Items.RAW_IRON;
        }
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    public void aiStep() {

        if (this.level.isClientSide) {
            this.eatAnimationTick = Math.max(0, this.eatAnimationTick - 1);
        }

        if (!this.level.isClientSide && this.isAlive()) {
            if (this.random.nextInt(900) == 0 && this.deathTime == 0) {
                this.heal(1.0F);
            }
        }

        super.aiStep();

    }


    /* TAMEABLE */
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();

        Item itemForTaming = ModItems.WORM.get();

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

        if (item == itemForTaming && !isTame()) {
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
                        setDigging(true);
                    }
                }

                return InteractionResult.SUCCESS;
            }
        }

        if(isTame() && !this.level.isClientSide && hand == InteractionHand.MAIN_HAND && !player.isCrouching()) {
            setSitting(!isSitting());
            return InteractionResult.SUCCESS;
        }

        //set digging by crouching and right clicking
        if(isTame() && !this.level.isClientSide && hand == InteractionHand.MAIN_HAND && player.isCrouching()) {
            //this.can_dig = !this.can_dig;
            setDigging(!isDigging());
            return InteractionResult.SUCCESS;
        }

        if (itemstack.getItem() == itemForTaming) {
            return InteractionResult.PASS;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setSitting(tag.getBoolean("isSitting"));
        setDigging(tag.getBoolean("isDigging"));
        this.entityData.set(DATA_ID_TYPE_VARIANT,tag.getInt("Variant"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("isSitting", this.isSitting());
        tag.putBoolean("isDigging", this.isDigging());
        tag.putInt("Variant",this.getTypeVariant());
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SITTING, false);
        this.entityData.define(DIGGING, false);
        this.entityData.define(DATA_ID_TYPE_VARIANT,0);
    }

    public void setSitting(boolean sitting) {
        this.entityData.set(SITTING, sitting);
        this.setOrderedToSit(sitting);
    }

    public void setDigging(boolean digging) {
        this.entityData.set(DIGGING, digging);
        this.setOrderedToSit(digging);
    }

    public boolean isSitting() {
        return this.entityData.get(SITTING);
    }

    public boolean isDigging() {
        return this.entityData.get(DIGGING);
    }

    @Override
    public Team getTeam() {
        return super.getTeam();
    }

    public boolean canBeLeashed(Player player) {
        return true;
    }

    /* VARIANTS */
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_146746_, DifficultyInstance p_146747_,
                                        MobSpawnType p_146748_, @Nullable SpawnGroupData p_146749_,
                                        @Nullable CompoundTag p_146750_) {
        OroVariant variant = Util.getRandom(OroVariant.values(), this.random);
        setVariant(variant);
        return super.finalizeSpawn(p_146746_, p_146747_, p_146748_, p_146749_, p_146750_);
    }

    public OroVariant getVariant() {
        return OroVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(OroVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public void handleEntityEvent(byte pId) {
        if (pId == 10) {
            this.eatAnimationTick = 40;
        } else {
            super.handleEntityEvent(pId);
        }

    }

    protected void customServerAiStep() {
        this.eatAnimationTick = this.eatBlockGoal.getEatAnimationTick();
        super.customServerAiStep();
    }



    public class EatBlockGoal extends Goal {
        private static final int EAT_ANIMATION_TICKS = 40;
        private static final Predicate<BlockState> IS_SNOW = BlockStatePredicate.forBlock(Blocks.SNOW);
        /** The entity owner of this AITask */
        private final OroEntity mob;
        /** The world the grass eater entity is eating from */
        private final Level level;
        /** Number of ticks since the entity started to eat grass */
        private int eatAnimationTick;

        public EatBlockGoal(OroEntity pMob) {
            this.mob = pMob;
            this.level = pMob.level;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            if ((this.mob.getRandom().nextInt(this.mob.isBaby() ? 50 : 1000) != 0) || !(mob.isTame()) || !mob.isDigging()) {
                return false;
            } else {
                BlockPos blockpos = this.mob.blockPosition();
                if (IS_SNOW.test(this.level.getBlockState(blockpos))) {
                    return true;
                } else {
                    return this.level.getBlockState(blockpos.below()).is(Blocks.SNOW_BLOCK);
                }
            }
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            this.eatAnimationTick = this.adjustedTickDelay(40);
            this.level.broadcastEntityEvent(this.mob, (byte)10);
            this.mob.getNavigation().stop();
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void stop() {
            this.eatAnimationTick = 0;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            return this.eatAnimationTick > 0;
        }

        /**
         * Number of ticks since the entity started to eat grass
         */
        public int getEatAnimationTick() {
            return this.eatAnimationTick;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            this.eatAnimationTick = Math.max(0, this.eatAnimationTick - 1);
            if (this.eatAnimationTick == this.adjustedTickDelay(4)) {
                BlockPos blockpos = this.mob.blockPosition();
                if (IS_SNOW.test(this.level.getBlockState(blockpos))) {
                    if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this.mob)) {
                        this.level.destroyBlock(blockpos, false);
                        net.minecraft.world.item.Item item = handle_random_item();
                        mob.spawnAtLocation(item);
                        mob.gameEvent(GameEvent.ENTITY_PLACE);
                    }

                    this.mob.ate();
                } else {
                    BlockPos blockpos1 = blockpos.below();
                    if (this.level.getBlockState(blockpos1).is(Blocks.SNOW_BLOCK)) {
                        if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this.mob)) {
                            this.level.levelEvent(2001, blockpos1, Block.getId(Blocks.SNOW_BLOCK.defaultBlockState()));
                            this.level.setBlock(blockpos1, Blocks.SNOW.defaultBlockState(), 2);
                            net.minecraft.world.item.Item item = handle_random_item();
                            mob.spawnAtLocation(item);
                            mob.gameEvent(GameEvent.ENTITY_PLACE);
                        }

                        this.mob.ate();
                    }
                }

            }
        }
    }

}
