package net.jrex.rexcraft.entity.custom;
import net.jrex.rexcraft.item.ModItems;
import net.jrex.rexcraft.sound.ModSounds;
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
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
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

public abstract class AbstractUtilDino extends AbstractChestedHorse implements IAnimatable, NeutralMob{

    protected static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(AbstractUtilDino.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> CHALLENGED =
            SynchedEntityData.defineId(AbstractUtilDino.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CHALLENGED_ANIMATION =
            SynchedEntityData.defineId(AbstractUtilDino.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> WEAK =
            SynchedEntityData.defineId(AbstractUtilDino.class, EntityDataSerializers.BOOLEAN);

    protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(AbstractUtilDino.class, EntityDataSerializers.BYTE);
    protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID_ID = SynchedEntityData.defineId(AbstractUtilDino.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(AbstractUtilDino.class, EntityDataSerializers.INT);

    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(40, 60);

    public float get_step_height(){
        return 2.0F;
    }

    //speed modifier of the entity when being ridden
    public float get_speedMod(){
        return -0.7f;
    }

    public float get_riderOffset(){
        return 1.0f;
    }

    // Used to get the base movement speed of the dinosaur
    public float getBaseSpeed(){
        return 0.17f;
    }

    // Time it takes to play the challenge animation
    public int challenge_time = 0;

    //set length of the challenge animation
    public void setAnimLen(){
        this.challenge_time = 130;
    }

    public static int attacknum = 3;

    // Function that just gets 10% of the total health
    private float getWeakNum(){
        return (this.getMaxHealth() * 0.10f);
    }

    public void setWeak(boolean weak) {
        this.entityData.set(WEAK, weak);
    }

    public boolean isWeak() {
        return this.entityData.get(WEAK);
    }

    @Nullable
    private UUID persistentAngerTarget;

    private AnimationFactory factory = new AnimationFactory(this);


    protected AbstractUtilDino(EntityType<? extends AbstractChestedHorse> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    //
    public static boolean wantsToAttack(LivingEntity ownerLastHurtBy, LivingEntity livingentity) {
        return true;
    }

    protected int rand_num(){
        Random rand = new Random();
        int rand_num = rand.nextInt(10);

        if(rand_num > 6){
            return 1;
        }
        else{
            return 0;
        }
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {

        if (this.isChallengedAnimation()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("challenge", false));
            return PlayState.CONTINUE;
        }

        if (this.isWeak()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("weak", true));
            return PlayState.CONTINUE;
        }

        //if in water, use swimming anims

        if (this.isSwimming() || this.isVisuallySwimming() || this.isInWater()){
            if (this.isVehicle()){
                event.getController().setAnimation(new AnimationBuilder().addAnimation("swimming", true));
                return PlayState.CONTINUE;
            }else{
                event.getController().setAnimation(new AnimationBuilder().addAnimation("swimming2", true));
                return PlayState.CONTINUE;
            }
        }

        if (event.isMoving()) {
            if (this.isVehicle()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("vehicle_walk", true));
                return PlayState.CONTINUE;
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", true));
                return PlayState.CONTINUE;
            }

        }
        if(!event.isMoving() && event.getController().getCurrentAnimation() != null){
            String name = event.getController().getCurrentAnimation().animationName;

            //if that animation is anything other than an idle, just override it and set it to idle0
            if(name.equals("walk") || name.equals("vehicle_walk") || name.equals("swimming") || name.equals("swimming2")){
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

    private PlayState attackPredicate(AnimationEvent event) {

        if (this.swinging && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            event.getController().markNeedsReload();

            //a random number is chosen between 0 and attacknum, then added to the end of "attack" to get a random attack animation!

            Random rand = new Random();

            int upperbound = attacknum;

            int rand_int = rand.nextInt(upperbound);

            event.getController().setAnimation(new AnimationBuilder().addAnimation("attack" + rand_int, false));

            this.swinging = false;
        }

        return PlayState.CONTINUE;
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

    public boolean challengeItem(ItemStack pStack){
        Item item = pStack.getItem();
        return item == ModItems.ALLO.get();
    }

    @Override
    public void positionRider(@NotNull Entity pPassenger) {
        if (this.hasPassenger(pPassenger)) {
            float f = Mth.cos(this.yBodyRot * ((float)Math.PI / 180F));
            float f1 = Mth.sin(this.yBodyRot * ((float)Math.PI / 180F));

            pPassenger.setPos(this.getX() + (double)(0.3F * f1), this.getY() + this.getPassengersRidingOffset() + pPassenger.getMyRidingOffset() + this.get_riderOffset(), this.getZ() - (double)(0.3F * f));
        }
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
    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
        if (!pBlock.getMaterial().isLiquid()) {
            BlockState blockstate = this.level.getBlockState(pPos.above());
            SoundType soundtype = pBlock.getSoundType(level, pPos, this);
            if (blockstate.is(Blocks.SNOW)) {
                soundtype = blockstate.getSoundType(level, pPos, this);
            }

            if (soundtype == SoundType.WOOD) {
                this.playSound(SoundEvents.WOOD_STEP, soundtype.getVolume() * 0.30F, soundtype.getPitch() - 0.6f);
            }
            if (soundtype == SoundType.STONE) {
                this.playSound(SoundEvents.STONE_STEP, soundtype.getVolume() * 0.30F, soundtype.getPitch()- 0.6f);
            }
            if (soundtype == SoundType.NETHERITE_BLOCK) {
                this.playSound(SoundEvents.NETHERITE_BLOCK_STEP, soundtype.getVolume() * 0.30F, soundtype.getPitch()- 0.6f);
            }else {
                this.playSound(SoundEvents.GRASS_STEP, soundtype.getVolume() * 0.30F, soundtype.getPitch()- 0.6f);
            }

        }
    }


    protected boolean isImmobile() {
        return super.isImmobile() && this.isVehicle() && this.isSaddled();
    }

    @Override
    public void aiStep() {
        super.aiStep();

        this.setWeak((this.getHealth() < getWeakNum()) && this.isChallenged());
        // It shouldn't be moving if it's either weak or in it's challenge animation
        if ((!this.level.isClientSide && this.isAlive()) && ((this.isWeak()) || (this.isChallenged() && this.isChallengedAnimation()))){
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0f);

            // if it's in its challenge anim, it should be invulnerable
            if (!this.level.isClientSide && this.isAlive() && this.isChallenged() && this.isChallengedAnimation()) {
                this.challenge_time = this.challenge_time - 1;
                this.setInvulnerable(true);
                getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0f);

                if (this.challenge_time < 1) {
                    this.setChallengedAnimation(false);
                }
            }
            else{
                this.setInvulnerable(false);
            }

        }
        else{
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.getBaseSpeed());
            this.setInvulnerable(false);
        }


        if (!this.level.isClientSide && this.isAlive()) {
            if (this.random.nextInt(900) == 0 && this.deathTime == 0) {
                this.heal(1.0F);
            }
            this.followMommy();
        }

        if (!this.level.isClientSide) {
            this.updatePersistentAnger((ServerLevel)this.level, true);
        }

    }

    //currently does nothing
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
                this.openCustomInventoryScreen(player);
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }

            if (this.isVehicle()) {
                return super.mobInteract(player, hand);
            }
        }

        if (!itemstack.isEmpty()) {

            //if this is the item for challenging, set challenge to true
            if (this.challengeItem(itemstack) && !isTame() && !this.isBaby()) {
                if (this.level.isClientSide) {
                    return InteractionResult.CONSUME;
                } else {
                    if (!player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }

                    if (!this.level.isClientSide) {
                        setChallenged(true);
                        setChallengedAnimation(true);
                        //start anim counter
                        this.setAnimLen();
                        this.playSound(ModSounds.STYRACO_ANGRY.get(),10,10);

                    }
                    return InteractionResult.SUCCESS;
                }
            }

            if (this.tameItem(itemstack) && !isTame() && this.isWeak()) {
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
                            this.setTarget(null);
                            this.level.broadcastEntityEvent(this, (byte)7);
                            this.spawnTamingParticles(true);
                            //remove the weak and challenged status, also heal
                            setChallenged(false);
                            setWeak(false);
                            this.heal(this.getMaxHealth());
                            this.navigation.recomputePath();
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

            if (!this.isBaby() && !this.isSaddled() && itemstack.is(Items.SADDLE)) {
                this.openCustomInventoryScreen(player);
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }
        }

        if (this.isBaby()) {
            return super.mobInteract(player, hand);
        } else {
            if (this.isTame() || this.isTamed()){
                this.doPlayerRide(player);
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }else{
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        setChallenged(tag.getBoolean("isChallenged"));
        setChallengedAnimation(tag.getBoolean("isChallengedAnimation"));
        setWeak(tag.getBoolean("isWeak"));

        UUID uuid;
        if (tag.hasUUID("Owner")) {
            uuid = tag.getUUID("Owner");
        } else {
            String s = tag.getString("Owner");
            uuid = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }
        //setSaddled(tag.getBoolean("isSaddled"));
        this.entityData.set(DATA_ID_TYPE_VARIANT,tag.getInt("Variant"));
        this.readPersistentAngerSaveData(this.level, tag);
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
        return pOtherAnimal != this && pOtherAnimal instanceof BernisEntity && this.canParent() && ((BernisEntity)pOtherAnimal).canParent();
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

        tag.putBoolean("isChallenged", this.isChallenged());
        tag.putBoolean("isWeak", this.isWeak());
        tag.putBoolean("isChallengedAnimation", this.isChallengedAnimation());

        if (this.getOwnerUUID() != null) {
            tag.putUUID("Owner", this.getOwnerUUID());
        }
        this.addPersistentAngerSaveData(tag);
    }

    public boolean isTame() {
        return (this.entityData.get(DATA_FLAGS_ID) & 4) != 0;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT,0);
        this.entityData.define(DATA_REMAINING_ANGER_TIME, 0);
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
        this.entityData.define(DATA_OWNERUUID_ID, Optional.empty());
        this.entityData.define(CHALLENGED, false);
        this.entityData.define(CHALLENGED_ANIMATION, false);
        this.entityData.define(WEAK, false);
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
                this.maxUpStep = this.get_step_height();

                if (this.onGround) {
                    Vec3 vec3 = this.getDeltaMovement();
                    this.setDeltaMovement(vec3.x, 0, vec3.z);
                }

                if (this.isControlledByLocalInstance()) {
                    this.setSpeed((float)this.getAttributeValue(Attributes.MOVEMENT_SPEED) + this.get_speedMod());
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

    public void setChallengedAnimation(boolean challenged) {
        this.entityData.set(CHALLENGED_ANIMATION, challenged);
    }

    public boolean isChallengedAnimation() {
        return this.entityData.get(CHALLENGED_ANIMATION);
    }

    public void setChallenged(boolean challenged) {
        this.entityData.set(CHALLENGED, challenged);
    }

    public boolean isChallenged() {
        return this.entityData.get(CHALLENGED);
    }

}
