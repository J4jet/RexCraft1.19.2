package net.jrex.rexcraft.entity.custom;

import net.jrex.rexcraft.entity.ModEntityTypes;
import net.jrex.rexcraft.entity.goal.DinoFollowLeaderGoal;
import net.jrex.rexcraft.entity.goal.DinoOwnerHurtByTargetGoal;
import net.jrex.rexcraft.entity.goal.LargeDinoBreedGoal;
import net.jrex.rexcraft.entity.variant.DiploVariant;
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
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Difficulty;
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
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
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


import java.util.*;

public class DiploEntity extends AbstractGroupingUtilDino implements IAnimatable, NeutralMob {

    @Override
    public float get_step_height(){
        return 3.0F;
    }

    @Override
    //speed modifier of the entity when being ridden
    public float get_speedMod(){
        if (!this.isAngry()){
            return -0.5f;
        }
        else{
            return 0;
        }
    }

    @Override
    public float get_riderOffset(){
        return 1.2f;
    }

    // Used to get the base movement speed of the dinosaur
    public float getBaseSpeed(){
        return 0.23f;
    }

    public static int attacknum = 3;

    private int destroyBlocksTick;

    public boolean inWall;

    @Nullable
    private UUID persistentAngerTarget;

    public DiploEntity(EntityType<? extends AbstractChestedHorse> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier setAttributes() {

        return AbstractChestedHorse.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 260.0D)
                .add(Attributes.ATTACK_DAMAGE, 15.0f)
                .add(Attributes.ATTACK_SPEED, 0.05f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 100)
                .add(Attributes.ARMOR,5.0)
                .add(Attributes.ARMOR_TOUGHNESS,5.0)
                .add(Attributes.MOVEMENT_SPEED, 0.171f).build();
    }
    //no randomized attributes!
    @Override
    protected void randomizeAttributes(RandomSource p_218803_) {
        Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(260);
    }

    @Override
    protected void registerGoals() {

        //Sauropods this large will just walk through the water
        //this.goalSelector.addGoal(1, new FloatGoal(this));
        //this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        //this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 2.0D, 10.0F, 6.0F, false));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(2, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(2, new DinoFollowLeaderGoal(this, 1.5));
        this.goalSelector.addGoal(3, new LargeDinoBreedGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, BucklandiiEntity.class, 10.0F, 1.2D, 1.4D));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        //this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new DinoOwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        //this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    @Override
    protected int rand_num(){
        Random rand = new Random();
        int rand_num = rand.nextInt(10);

        //if it's angry, just use either idle 1 or 2
        if (this.isAngry()){
            if(rand_num > 8){
                return 0;
            }
            else{

                return 1;
            }
        }

        //if it's not angry, use idles 0-6
        if(rand_num >= 6){
            return 1;
        }
        else if(rand_num == 0){
            return 0;
        }
        else if(rand_num == 1){
            return 2;
        }
        else if(rand_num == 2){
            return 3;
        }
        else if(rand_num == 3){
            return 4;
        }
        else if(rand_num == 4){
            return 5;
        }
        else {
            return 6;
        }
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob mob) {
        DiploEntity baby = ModEntityTypes.DIPLO.get().create(serverLevel);
        DiploVariant variant = Util.getRandom(DiploVariant.values(), this.random);
        baby.setVariant(variant);
        return baby;
    }

    @Override
    //taming item
    public boolean tameItem(ItemStack pStack) {
        Item item = pStack.getItem();
        return item == ModItems.HERB_BUFF_DIAMOND.get() || item == ModItems.HERB_BUFF_NETH.get();
    }

    protected void clampRotation(Entity pEntityToUpdate) {
        pEntityToUpdate.setYBodyRot(this.getYRot());
        float f = Mth.wrapDegrees(pEntityToUpdate.getYRot() - this.getYRot());
        float f1 = Mth.clamp(f, -105.0F, 105.0F);
        pEntityToUpdate.yRotO += f1 - f;
        pEntityToUpdate.setYRot(pEntityToUpdate.getYRot() + f1 - f);
        pEntityToUpdate.setYHeadRot(pEntityToUpdate.getYRot());
    }

    /**Keeping this here for now */
//    @Override
//    public void positionRider(@NotNull Entity pPassenger) {
//        if (this.hasPassenger(pPassenger)) {
//            float f = Mth.cos(this.yBodyRot * ((float)Math.PI / 180F));
//            float f1 = Mth.sin(this.yBodyRot * ((float)Math.PI / 180F));
//
//            pPassenger.setPos(this.getX() + (double)(0.3F * f1), this.getY() + this.getPassengersRidingOffset() + pPassenger.getMyRidingOffset() + riderOffset, this.getZ() - (double)(0.3F * f));
//            if (this.getPassengers().size() > 1) {
//                int i = this.getPassengers().indexOf(pPassenger);
//                if (i == 0) {
//                    f = 0.2F;
//                } else {
//                    f = -0.6F;
//                }
//
//                if (pPassenger instanceof Animal) {
//                    f += 0.2F;
//                }
//            }
//            Vec3 vec3 = (new Vec3((double)f, 0.0D, 0.0D)).yRot(-this.getYRot() * ((float)Math.PI / 180F) - ((float)Math.PI / 2F));
//            pPassenger.setPos(this.getX() + vec3.x, this.getY() + (double)f1, this.getZ() + vec3.z);
//            pPassenger.setYRot(pPassenger.getYRot() + this.deltaRotation);
//            pPassenger.setYHeadRot(pPassenger.getYHeadRot() + this.deltaRotation);
//            this.clampRotation(pPassenger);
//            if (pPassenger instanceof Animal && this.getPassengers().size() == this.getMaxPassengers()) {
//                int j = pPassenger.getId() % 2 == 0 ? 90 : 270;
//                pPassenger.setYBodyRot(((Animal)pPassenger).yBodyRot + (float)j);
//                pPassenger.setYHeadRot(pPassenger.getYHeadRot() + (float)j);
//            }


//        }

        /** Extra riders **/

        //      if (this.hasPassenger(pPassenger)) {
        //         float f = this.getSinglePassengerXOffset();
        //         float f1 = (float)((this.isRemoved() ? (double)0.01F : this.getPassengersRidingOffset()) + pPassenger.getMyRidingOffset());
//                 if (this.getPassengers().size() > 1) {
//                    int i = this.getPassengers().indexOf(pPassenger);
//                    if (i == 0) {
//                       f = 0.2F;
//                    } else {
//                       f = -0.6F;
//                    }
//
//                    if (pPassenger instanceof Animal) {
//                       f += 0.2F;
//                    }
//                 }
//    }


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
    protected int calculateFallDamage(float pDistance, float pDamageMultiplier) {
        if (pDistance < 3.5){
            return 0;
        }else{
            return Mth.ceil((pDistance * 65.0F) * pDamageMultiplier);
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
                this.playSound(SoundEvents.WOOD_STEP, soundtype.getVolume() * .50F, soundtype.getPitch()  - 2);
            }
            if (soundtype == SoundType.STONE) {
                this.playSound(SoundEvents.STONE_STEP, soundtype.getVolume() * .50F, soundtype.getPitch()- 2);
            }
            if (soundtype == SoundType.NETHERITE_BLOCK) {
                this.playSound(SoundEvents.NETHERITE_BLOCK_STEP, soundtype.getVolume() * .50F, soundtype.getPitch()- 2);
            }else {
                this.playSound(SoundEvents.GRASS_STEP, soundtype.getVolume() * .50F, soundtype.getPitch()- 2);
            }

        }
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.DIPLO_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.DIPLO_DEATH.get();
    }

    protected SoundEvent getAmbientSound() {

        if (this.isAngry()) {
            return ModSounds.DIPLO_ANGRY.get();
        } else {
            return ModSounds.DIPLO_IDLE.get();
        }
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
        //System.out.println(this.getHealth());
        if (!this.level.isClientSide && this.isAlive()) {
            if (this.random.nextInt(900) == 0 && this.deathTime == 0 && this.getHealth() < 200) {
                this.heal(5.0F);
            }
            this.followMommy();
        }

        if(this.isAngry()){
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0f);
            getAttribute(Attributes.ARMOR).setBaseValue(17.0f);
            getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(17.0f);
        }
        else if (!this.isAngry()){
            getAttribute(Attributes.ARMOR).setBaseValue(5.0f);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.17f);
            getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(5.0f);
        }

        if (!this.level.isClientSide) {
            this.updatePersistentAnger((ServerLevel)this.level, true);
        }

        if (!this.level.isClientSide) {
            this.inWall = this.checkWalls(this.getBoundingBox());
        }

    }

    @Override
    public boolean isSaddleable() {
        return true;
    }

    protected void reassessTameGoals() {
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
    public Team getTeam() {
        return super.getTeam();
    }

    public boolean canBeLeashed(Player player) {
        //can only be leashed if tamed
        return this.isTame();
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.91F;
    }

    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_146746_, DifficultyInstance p_146747_,
                                        MobSpawnType p_146748_, @Nullable SpawnGroupData p_146749_,
                                        @Nullable CompoundTag p_146750_) {
        DiploVariant variant = Util.getRandom(DiploVariant.values(), this.random);
        setVariant(variant);
        return super.finalizeSpawn(p_146746_, p_146747_, p_146748_, p_146749_, p_146750_);
    }

    public DiploVariant getVariant() {
        return DiploVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(DiploVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    protected boolean canAddPassenger(Entity pPassenger) {
        return this.getPassengers().size() < this.getMaxPassengers();
    }

    protected int getMaxPassengers() {
        return 2;
    }

    // Forge: Fix MC-119811 by instantly completing lerp on board
//    @Override
//    protected void addPassenger(Entity passenger) {
//        super.addPassenger(passenger);
//        if (this.isControlledByLocalInstance() && this.lerpSteps > 0) {
//            this.lerpSteps = 0;
//            this.absMoveTo(this.lerpX, this.lerpY, this.lerpZ, (float)this.lerpYRot, (float)this.lerpXRot);
//        }
//    }

    //Breaking things

    private boolean checkWalls(AABB pArea) {
        int i = Mth.floor(pArea.minX);
        int j = Mth.floor(pArea.minY);
        int k = Mth.floor(pArea.minZ);
        int l = Mth.floor(pArea.maxX);
        int i1 = Mth.floor(pArea.maxY);
        int j1 = Mth.floor(pArea.maxZ);
        boolean flag = false;
        boolean flag1 = false;

        for(int k1 = i; k1 <= l; ++k1) {
            for(int l1 = j; l1 <= i1; ++l1) {
                for(int i2 = k; i2 <= j1; ++i2) {
                    BlockPos blockpos = new BlockPos(k1, l1, i2);
                    BlockState blockstate = this.level.getBlockState(blockpos);
                    if (!blockstate.isAir() && !blockstate.is(BlockTags.DRAGON_TRANSPARENT)) {
                        //&& !blockstate.is(BlockTags.DRAGON_IMMUNE)
                        if (net.minecraftforge.common.ForgeHooks.canEntityDestroy(this.level, blockpos, this)) {
                            flag1 = this.level.removeBlock(blockpos, false) || flag1;
                        } else {
                            flag = true;
                        }
                    }
                }
            }
        }

        if (flag1) {
            BlockPos blockpos1 = new BlockPos(i + this.random.nextInt(l - i + 1), j + this.random.nextInt(i1 - j + 1), k + this.random.nextInt(j1 - k + 1));
            this.level.levelEvent(2008, blockpos1, 0);
        }

        return flag;
    }
    @Override
    protected void customServerAiStep() {
            super.customServerAiStep();

        if (ForgeEventFactory.getMobGriefingEvent(this.level, this)) {
            int j1 = Mth.floor(this.getY());
            int i2 = Mth.floor(this.getX());
            int j2 = Mth.floor(this.getZ());
            boolean flag = false;

            for(int j = -1; j <= 1; ++j) {
                for(int k2 = -1; k2 <= 1; ++k2) {
                    for(int k = 0; k <= 3; ++k) {
                        int l2 = i2 + j;
                        int l = j1 + k;
                        int i1 = j2 + k2;
                        BlockPos blockpos = new BlockPos(l2, l, i1);
                        BlockState blockstate = this.level.getBlockState(blockpos);
                        if (blockstate.canEntityDestroy(this.level, blockpos, this) && ForgeEventFactory.onEntityDestroyBlock(this, blockpos, blockstate)) {
                            flag = this.level.destroyBlock(blockpos, true, this) || flag;
                        }
                    }
                }
            }

            if (flag) {
                this.level.levelEvent((Player)null, 1022, this.blockPosition(), 0);
            }
        }

        if (this.tickCount % 20 == 0) {
                this.heal(1.0F);
            }
    }
}
