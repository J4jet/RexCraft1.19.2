package net.jrex.rexcraft.entity.custom;

import net.jrex.rexcraft.entity.ModEntityTypes;
import net.jrex.rexcraft.entity.client.GeckoRenderer;
import net.jrex.rexcraft.entity.variant.GeckoVariant;
import net.jrex.rexcraft.item.ModItems;
import net.jrex.rexcraft.sound.ModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Endermite;

import net.minecraft.world.entity.monster.Silverfish;


import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
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

public class GeckoEntity extends TamableAnimal implements IAnimatable {

    private static final EntityDataAccessor<Boolean> SITTING =
            SynchedEntityData.defineId(GeckoEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(GeckoEntity.class, EntityDataSerializers.INT);

    private AnimationFactory factory = new AnimationFactory(this);



    public GeckoEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    @Nullable



    public static AttributeSupplier setAttributes() {

        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.ATTACK_DAMAGE, 2.0f)
                .add(Attributes.ATTACK_SPEED, 1.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.16f).build();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 2.0D, false));
        this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 2.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(3, new PanicGoal(this, 2.0D));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.25D, Ingredient.of(ModItems.WORM.get()), false));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Player.class, 8.0F, 2.5D, 2.5D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Silverfish.class, false));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Endermite.class, false));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, CricketEntity.class, false));

    }



    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.gecko.walk", true));
            return PlayState.CONTINUE;
        }

        //if(this.isSprinting())

        if (this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.gecko.sitting", true));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.gecko.idle", true));
        return PlayState.CONTINUE;
    }

    private PlayState attackPredicate(AnimationEvent event) {

        if(this.swinging && event.getController().getAnimationState().equals(AnimationState.Stopped)){
            event.getController().markNeedsReload();
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.gecko.attack", false));
            this.swinging = false;
        }

        return PlayState.CONTINUE;
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob mob) {
        GeckoEntity baby = ModEntityTypes.GECKO.get().create(serverLevel);
        GeckoVariant variant = Util.getRandom(GeckoVariant.values(), this.random);
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
    public int getGeckoType() {
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
                    }
                }

                return InteractionResult.SUCCESS;
            }
        }

        if(isTame() && !this.level.isClientSide && hand == InteractionHand.MAIN_HAND) {
            setSitting(!isSitting());
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
        this.entityData.set(DATA_ID_TYPE_VARIANT,tag.getInt("Variant"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("isSitting", this.isSitting());
        tag.putInt("Variant",this.getTypeVariant());
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SITTING, false);
        this.entityData.define(DATA_ID_TYPE_VARIANT,0);
    }

    public void setSitting(boolean sitting) {
        this.entityData.set(SITTING, sitting);
        this.setOrderedToSit(sitting);
    }

    public boolean isSitting() {
        return this.entityData.get(SITTING);
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

    /* VARIANTS */
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_146746_, DifficultyInstance p_146747_,
                                        MobSpawnType p_146748_, @Nullable SpawnGroupData p_146749_,
                                        @Nullable CompoundTag p_146750_) {
        GeckoVariant variant = Util.getRandom(GeckoVariant.values(), this.random);
        setVariant(variant);
        return super.finalizeSpawn(p_146746_, p_146747_, p_146748_, p_146749_, p_146750_);
    }

    public GeckoVariant getVariant() {
        return GeckoVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(GeckoVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }
}
