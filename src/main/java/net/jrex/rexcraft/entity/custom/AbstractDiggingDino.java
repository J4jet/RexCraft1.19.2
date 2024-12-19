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
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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

// just oro, but missing
//getbreedoffspring
//setAttributes()
//attackPredicate
//PlayState predicate
//getOroType
//registerControllers
//all of the varient stuff under "/VARIANTS/"
//"tag.putInt("Variant",this.getTypeVariant());" in addadditionlSaveData


public abstract class AbstractDiggingDino extends TamableAnimal implements IAnimatable {

    private static final EntityDataAccessor<Boolean> SITTING =
            SynchedEntityData.defineId(AbstractDiggingDino.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> DIGGING =
            SynchedEntityData.defineId(AbstractDiggingDino.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(AbstractDiggingDino.class, EntityDataSerializers.INT);

    private int digAnimationTick;
    private DigBlockGoal digBlockGoal;

    public Block dig_block;

    public Item tame_item;

    // the different items this entity can find when digging
    public Item item_t1_1;
    public Item item_t1_2;
    public Item item_t2_1;
    public Item item_t2_2;
    public Item item_t3_1;
    public Item item_t3_2;
    public Item item_t4_1;
    public Item item_t4_2;
    public Item item_t4_3;

    private AnimationFactory factory = new AnimationFactory(this);

    public static final Predicate<LivingEntity> PREY_SELECTOR = (p_30437_) -> {
        EntityType<?> entitytype = p_30437_.getType();
        return false;
    };

    public AbstractDiggingDino(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void registerGoals() {
        this.digBlockGoal = new DigBlockGoal(this);
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 2.0D, false));
        this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 2.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(3, new PanicGoal(this, 2.0D));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(4, this.digBlockGoal);
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.25D, Ingredient.of(this.isTempt()), false));

        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Player.class, 8.0F, 2.5D, 2.5D));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, VeloEntity.class, 8.0F, 2.5D, 2.5D));

        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(4, new NonTameRandomTargetGoal<>(this, LivingEntity.class, false, PREY_SELECTOR));

    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.getItem() == ModItems.DUBIA.get();
    }

    //Used as the healing item, in the case of the gecko it's a cricket
    public boolean isHeal(ItemStack pStack){
        return pStack.getItem() == ModItems.CRICKET_ITEM.get();
    }

    //Used as the tempting item, in the case of the gecko it's a mealworm
    public Item isTempt(){
        return ModItems.CRICKET_ITEM.get();
    }


    @Override
    public AnimationFactory getFactory() {
        return factory;
    }


    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.GRASS_STEP, 0.15F, 1.0F);
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
            return this.item_t1_1;
        }
        //between 26-50
        if (rand_num > 25 && rand_num <= 50){
            return this.item_t1_2;
        }
        //between 51-65
        if (rand_num > 50 && rand_num <= 65){
            return this.item_t2_1;
        }
        //between 66-80
        if (rand_num > 65 && rand_num <= 80){
            return this.item_t2_2;
        }
        //between 81-88
        if (rand_num > 80 && rand_num <= 88){
            return this.item_t3_1;
        }
        //between 89-95
        if (rand_num > 80 && rand_num <= 95){
            return this.item_t3_2;
        }
        //between 96-97
        if (rand_num > 96 && rand_num <= 98){
            return this.item_t4_1;
        }
        //between 98-99
        if (rand_num == 99){
            return this.item_t4_2;
        }
        else{
            return this.item_t4_3;
        }
    }

    public void aiStep() {

        if (this.level.isClientSide) {
            this.digAnimationTick = Math.max(0, this.digAnimationTick - 1);
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

        Item itemForTaming = this.tame_item;

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

    @Override
    public void setTame(boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(16.0D);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4D);
            getAttribute(Attributes.ATTACK_SPEED).setBaseValue(1.0f);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.16f);
        } else {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(8.0D);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(2D);
            getAttribute(Attributes.ATTACK_SPEED).setBaseValue(1.0f);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.16f);
        }
    }

    public void handleEntityEvent(byte pId) {
        if (pId == 10) {
            this.digAnimationTick = 40;
        } else {
            super.handleEntityEvent(pId);
        }

    }

    protected void customServerAiStep() {
        this.digAnimationTick = this.digBlockGoal.getdigAnimationTick();
        super.customServerAiStep();
    }



    public class DigBlockGoal extends Goal {
        private static final int EAT_ANIMATION_TICKS = 40;
        private static final Predicate<BlockState> IS_ALT_BLOCK = BlockStatePredicate.forBlock(Blocks.GRASS);
        /** The entity owner of this AITask */
        private final AbstractDiggingDino mob;
        /** The world the digging entity is digging from */
        private final Level level;
        /** Number of ticks since the entity started to dig */
        private int digAnimationTick;

        public DigBlockGoal(AbstractDiggingDino pMob) {
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
                if (IS_ALT_BLOCK.test(this.level.getBlockState(blockpos))) {
                    return true;
                } else {
                    return this.level.getBlockState(blockpos.below()).is(mob.dig_block);
                }
            }
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            this.digAnimationTick = this.adjustedTickDelay(40);
            this.level.broadcastEntityEvent(this.mob, (byte)10);
            this.mob.getNavigation().stop();
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void stop() {
            this.digAnimationTick = 0;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            return this.digAnimationTick > 0;
        }

        /**
         * Number of ticks since the entity started to dig
         */
        public int getdigAnimationTick() {
            return this.digAnimationTick;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            this.digAnimationTick = Math.max(0, this.digAnimationTick - 1);
            if (this.digAnimationTick == this.adjustedTickDelay(4)) {
                BlockPos blockpos = this.mob.blockPosition();
                if (IS_ALT_BLOCK.test(this.level.getBlockState(blockpos))) {
                    if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this.mob)) {
                        this.level.destroyBlock(blockpos, false);
                        net.minecraft.world.item.Item item = handle_random_item();
                        mob.spawnAtLocation(item);
                        mob.gameEvent(GameEvent.ENTITY_PLACE);
                    }

                    this.mob.ate();
                } else {
                    BlockPos blockpos1 = blockpos.below();
                    if (this.level.getBlockState(blockpos1).is(mob.dig_block)) {
                        if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this.mob)) {
                            this.level.levelEvent(2001, blockpos1, Block.getId(mob.dig_block.defaultBlockState()));
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
