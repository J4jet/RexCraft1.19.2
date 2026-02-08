package net.jrex.rexcraft.entity.custom;

import net.jrex.rexcraft.item.ModItems;
import net.jrex.rexcraft.sound.ModSounds;
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
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
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

public abstract class AbstractCombatDino extends TamableAnimal implements IAnimatable, NeutralMob, PlayerRideableJumping, Saddleable {

    private static final EntityDataAccessor<Boolean> SITTING =
            SynchedEntityData.defineId(AbstractCombatDino.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> WEAK =
            SynchedEntityData.defineId(AbstractCombatDino.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CHALLENGED =
            SynchedEntityData.defineId(AbstractCombatDino.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CHALLENGED_ANIMATION =
            SynchedEntityData.defineId(AbstractCombatDino.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SADDLED =
            SynchedEntityData.defineId(AbstractCombatDino.class, EntityDataSerializers.BOOLEAN);

    public static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(AbstractCombatDino.class, EntityDataSerializers.INT);

    // Used to get the base movement speed of the dinosaur
    public float getBaseSpeed(){
        return 0.17f;
    }

    // Used to get the base attack dmg of a dinosaur
    public float getBaseAttack(){
        return 20f;
    }

    // Time it takes to play the challenge animation
    public int challenge_time = 0;

    //set length of the challenge animation
    public void setAnimLen(){
        this.challenge_time = 130;
    }

    //how many different attack animations are there
    public static int attacknum = 3;

    //offset of a player riding the entity
    public float getRiderOffset(){
        return 0.0f;
    }

    //how many blocks can this entity clear
    public float getStepHeight(){
        return 1.0F;
    }

    // speed modifier if entity is being ridden
    public float getSpeedMod(){
        return 0.001f;
    }

    @Nullable
    private UUID persistentAngerTarget;

    //private int destroyBlocksTick;

    private AnimationFactory factory = new AnimationFactory(this);

    protected AbstractCombatDino(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
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
                event.getController().setAnimation(new AnimationBuilder().addAnimation("swimming2", true));
                return PlayState.CONTINUE;
            }else{
                event.getController().setAnimation(new AnimationBuilder().addAnimation("swimming", true));
                return PlayState.CONTINUE;
            }
        }

        if (event.isMoving()) {
            if(this.isVehicle()){
                event.getController().setAnimation(new AnimationBuilder().addAnimation("vehicle_walk", true));
                return PlayState.CONTINUE;
            }
            else{
                event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", true));
                return PlayState.CONTINUE;
            }

        }
        if (this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("sitting", true));
            return PlayState.CONTINUE;
        }

        //if the entity is not moving or sitting, and has a current animation:

        if(!event.isMoving() && !this.isSitting() && event.getController().getCurrentAnimation() != null){
            String name = event.getController().getCurrentAnimation().animationName;

            //if that animation is anything other than an idle, just override it and set it to idle0
            if(name.equals("walk") || name.equals("vehicle_walk") || name.equals("sitting") || name.equals("swimming") || name.equals("swimming2")){
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

    /** Chooses a random number between 0 and 9, then returns 0 or 1 based on that. **/
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

    private PlayState attackPredicate(AnimationEvent event) {

        if(this.swinging && event.getController().getAnimationState().equals(AnimationState.Stopped) && !this.isWeak()){
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

    @Override
    public boolean isFood(ItemStack pStack){
        Item item = pStack.getItem();
        return item == ModItems.RAB_STEW.get();
    }

    //Used as the healing item, in the case of the gecko it's a cricket
    //look into wolf class to see how meat works
    public boolean isHeal(ItemStack pStack){
        Item item = pStack.getItem();
        return item.isEdible() && pStack.getFoodProperties(this).isMeat() && !(item == ModItems.CARNO_BUFF_GOLD.get() || item == ModItems.CARNO_BUFF_DIAMOND.get() || item == ModItems.CARNO_BUFF_NETH.get());
    }

    //taming item
    public boolean tameItem(ItemStack pStack){
        Item item = pStack.getItem();
        return item == ModItems.CARNO_BUFF_GOLD.get() || item == ModItems.CARNO_BUFF_DIAMOND.get() || item == ModItems.CARNO_BUFF_NETH.get();
    }

    public boolean challengeItem(ItemStack pStack){
        Item item = pStack.getItem();
        return item == ModItems.DINO_OFFERING.get();
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

    protected SoundEvent getSwimSound() {
        return SoundEvents.GENERIC_SWIM;
    }

    protected SoundEvent getSwimSplashSound() {
        return SoundEvents.GENERIC_SPLASH;
    }

    public SoundEvent getChallengedSound(){return ModSounds.STYRACO_ANGRY.get();}

    protected boolean isImmobile() {
        return super.isImmobile() && this.isVehicle() && this.isSaddled();
    }

    // Function that just gets 10% of the total health
    private float getWeakNum(){
        return (this.getMaxHealth() * 0.10f);
    }

    public void aiStep() {
        super.aiStep();

//        if(this.isAngry()){
//            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0f);
//        }
//        else if (!this.isAngry()){
//            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2f);
//        }

        this.setWeak((this.getHealth() < getWeakNum()) && this.isChallenged());

        // It shouldn't be moving if it's either weak or in it's challenge animation
        if ((!this.level.isClientSide && this.isAlive()) && ((this.isWeak()) || (this.isChallenged() && this.isChallengedAnimation()))){
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0f);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(0.0f);

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
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(this.getBaseAttack());
            this.setInvulnerable(false);
        }

        if (!this.level.isClientSide && this.isAlive()) {
            //System.out.println(this.getHealth());
            if (this.random.nextInt(900) == 0 && this.deathTime == 0) {
                this.heal(0.5F);
            }
        }

        if (!this.level.isClientSide) {
            this.updatePersistentAnger((ServerLevel)this.level, true);
        }

    }

    protected void doPlayerRide(Player pPlayer) {
        if (!this.level.isClientSide) {
            pPlayer.setYRot(this.getYRot());
            pPlayer.setXRot(this.getXRot());
            pPlayer.startRiding(this);
        }

    }

    public void positionRider(@NotNull Entity pPassenger) {
        if (this.hasPassenger(pPassenger)) {
            float f = Mth.cos(this.yBodyRot * ((float)Math.PI / 180F));
            float f1 = Mth.sin(this.yBodyRot * ((float)Math.PI / 180F));

            pPassenger.setPos(this.getX() + (double)(0.3F * f1), this.getY() + this.getPassengersRidingOffset() + pPassenger.getMyRidingOffset() + this.getRiderOffset(), this.getZ() - (double)(0.3F * f));
        }
    }

    @javax.annotation.Nullable
    public LivingEntity getControllingPassenger() {
        if (this.isSaddled()) {
            Entity entity = this.getFirstPassenger();
            if (entity instanceof LivingEntity) {
                return (LivingEntity)entity;
            }
        }

        return null;
    }


    @javax.annotation.Nullable
    private Vec3 getDismountLocationInDirection(Vec3 pDirection, LivingEntity pPassenger) {
        double d0 = this.getX() + pDirection.x;
        double d1 = this.getBoundingBox().minY;
        double d2 = this.getZ() + pDirection.z;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(Pose pose : pPassenger.getDismountPoses()) {
            blockpos$mutableblockpos.set(d0, d1, d2);
            double d3 = this.getBoundingBox().maxY + 0.75D;

            while(true) {
                double d4 = this.level.getBlockFloorHeight(blockpos$mutableblockpos);
                if ((double)blockpos$mutableblockpos.getY() + d4 > d3) {
                    break;
                }

                if (DismountHelper.isBlockFloorValid(d4)) {
                    AABB aabb = pPassenger.getLocalBoundsForPose(pose);
                    Vec3 vec3 = new Vec3(d0, (double)blockpos$mutableblockpos.getY() + d4, d2);
                    if (DismountHelper.canDismountTo(this.level, pPassenger, aabb.move(vec3))) {
                        pPassenger.setPose(pose);
                        return vec3;
                    }
                }

                blockpos$mutableblockpos.move(Direction.UP);
                if (!((double)blockpos$mutableblockpos.getY() < d3)) {
                    break;
                }
            }
        }

        return null;
    }

    public @NotNull Vec3 getDismountLocationForPassenger(LivingEntity pLivingEntity) {
        Vec3 vec3 = getCollisionHorizontalEscapeVector((double)this.getBbWidth(), (double)pLivingEntity.getBbWidth(), this.getYRot() + (pLivingEntity.getMainArm() == HumanoidArm.RIGHT ? 90.0F : -90.0F));
        Vec3 vec31 = this.getDismountLocationInDirection(vec3, pLivingEntity);
        if (vec31 != null) {
            return vec31;
        } else {
            Vec3 vec32 = getCollisionHorizontalEscapeVector((double)this.getBbWidth(), (double)pLivingEntity.getBbWidth(), this.getYRot() + (pLivingEntity.getMainArm() == HumanoidArm.LEFT ? 90.0F : -90.0F));
            Vec3 vec33 = this.getDismountLocationInDirection(vec32, pLivingEntity);
            return vec33 != null ? vec33 : this.position();
        }
    }

    /*  might want         if (!itemstack.isEmpty()) {} */

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
                    this.playSound(this.getChallengedSound(),10,1);

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
                        super.tame(player);
                        this.setTarget(null);
                        this.level.broadcastEntityEvent(this, (byte)7);
                        setSitting(true);
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

        //this is for controlling it
        if (this.isVehicle()) {
            return super.mobInteract(player, hand);
        }

        //if item is a saddle, and this is saddleable, equip saddle
        if(item == Items.SADDLE && this.isTame() && !this.level.isClientSide && this.isSaddleable() && !this.isSaddled()){
            itemstack.shrink(1);
            this.equipSaddle(SoundSource.NEUTRAL);
            return InteractionResult.SUCCESS;
        }

        //sit and unsit by crouching and right clicking
        if(isTame() && !this.level.isClientSide && hand == InteractionHand.MAIN_HAND && player.isCrouching()) {
            setSitting(!isSitting());
            return InteractionResult.SUCCESS;
        }

        //ride by right clicking with an empty hand
        if(isTame() && !this.level.isClientSide && hand == InteractionHand.MAIN_HAND && !this.isSitting()) {
            this.doPlayerRide(player);
            return InteractionResult.SUCCESS;
        }

        if (this.tameItem(itemstack)) {
            return InteractionResult.PASS;
        }

        return super.mobInteract(player, hand);
    }
    //CHALLENGED_ANIMATION
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setSitting(tag.getBoolean("isSitting"));
        setSaddled(tag.getBoolean("isSaddled"));
        setWeak(tag.getBoolean("isWeak"));
        setChallenged(tag.getBoolean("isChallenged"));
        setChallengedAnimation(tag.getBoolean("isChallengedAnimation"));
        this.entityData.set(DATA_ID_TYPE_VARIANT,tag.getInt("Variant"));

        //I think this saves if it's angry?
        this.readPersistentAngerSaveData(this.level, tag);

    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("isSitting", this.isSitting());
        tag.putBoolean("isSaddled", this.isSaddled());
        tag.putBoolean("isChallenged", this.isChallenged());
        tag.putBoolean("isWeak", this.isWeak());
        tag.putBoolean("isChallengedAnimation", this.isChallengedAnimation());
        this.addPersistentAngerSaveData(tag);
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SITTING, false);
        this.entityData.define(WEAK, false);
        this.entityData.define(SADDLED, false);
        this.entityData.define(CHALLENGED, false);
        this.entityData.define(CHALLENGED_ANIMATION, false);
        this.entityData.define(DATA_ID_TYPE_VARIANT,0);
    }

    @Override
    public Team getTeam() {
        return super.getTeam();
    }

    public boolean canBeLeashed(Player player) {
        return true;
    }

    public void travel(@NotNull Vec3 pTravelVector) {

        if (this.isAlive()) {
            LivingEntity livingentity = this.getControllingPassenger();
            if (this.isVehicle() && livingentity != null) {
                //maxstepup is deped so try using this?
                // Forge - see IForgeEntity#getStepHeight
                //this.getStepHeight();
                this.maxUpStep = this.getStepHeight();
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

//                if (this.onGround) {
//                    f = 0.0F;
//                    f1 = 0.0F;
//                }

                this.flyingSpeed = this.getSpeed() * 0.1F;
                if (this.isControlledByLocalInstance()) {
                    this.setSpeed((float)this.getAttributeValue(Attributes.MOVEMENT_SPEED) + this.getSpeedMod());
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
    public void onPlayerJump(int pJumpPower) {

    }

    @Override
    public boolean canJump() {
        return false;
    }

    @Override
    public void handleStartJump(int pJumpPower) {}

    @Override
    public void handleStopJump() {}

    public void setSitting(boolean sitting) {
        this.entityData.set(SITTING, sitting);
        this.setOrderedToSit(sitting);
    }

    public void setSaddled(boolean saddled) {
        this.entityData.set(SADDLED, saddled);

    }

    public boolean isSitting() {
        return this.entityData.get(SITTING);
    }

    @Override
    public boolean isSaddleable() {
        return this.isAlive() && !this.isBaby() && this.isTame();
    }

    public void setChallenged(boolean challenged) {
        this.entityData.set(CHALLENGED, challenged);
    }

    public boolean isChallenged() {
        return this.entityData.get(CHALLENGED);
    }

    public void setWeak(boolean weak) {
        this.entityData.set(WEAK, weak);
    }

    public boolean isWeak() {
        return this.entityData.get(WEAK);
    }

    public void setChallengedAnimation(boolean challenged) {
        this.entityData.set(CHALLENGED_ANIMATION, challenged);
    }

    public boolean isChallengedAnimation() {
        return this.entityData.get(CHALLENGED_ANIMATION);
    }

    @Override
    public void equipSaddle(@Nullable SoundSource pSource) {
        this.entityData.set(SADDLED,true);
        if (pSource != null) {
            this.level.playSound(null, this, SoundEvents.HORSE_SADDLE, pSource, 0.5F, 1.0F);
        }

    }

    @Override
    public boolean isSaddled() {
        return this.entityData.get(SADDLED);
    }

}
