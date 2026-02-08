package net.jrex.rexcraft.entity.custom;

import net.jrex.rexcraft.entity.ModEntityTypes;
import net.jrex.rexcraft.entity.goal.LargeDinoBreedGoal;
import net.jrex.rexcraft.entity.variant.RexVariant;
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
import net.minecraft.world.DifficultyInstance;
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

import java.util.function.Predicate;

public class RexEntity extends AbstractCombatDino {

    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 40);


    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(RexEntity.class, EntityDataSerializers.INT);
    public static final Predicate<LivingEntity> PREY_SELECTOR = (p_30437_) -> {
        EntityType<?> entitytype = p_30437_.getType();
        return entitytype == EntityType.VILLAGER || entitytype == EntityType.COW || entitytype == EntityType.SHEEP || entitytype == EntityType.PIG
                || entitytype == EntityType.LLAMA || entitytype == EntityType.HORSE || entitytype == EntityType.CHICKEN || entitytype == ModEntityTypes.BOREAL.get() ||
                entitytype == EntityType.PLAYER || entitytype == EntityType.DONKEY || entitytype == EntityType.MULE
                || entitytype == EntityType.SPIDER || entitytype == EntityType.ZOMBIE || entitytype == EntityType.SKELETON
                || entitytype == ModEntityTypes.VELO.get() || entitytype == ModEntityTypes.PROTO.get() || entitytype == ModEntityTypes.ORO.get() || entitytype == ModEntityTypes.JAKA.get() || entitytype == ModEntityTypes.PRENO.get()
                || entitytype == ModEntityTypes.BUCKLANDII.get() || entitytype == ModEntityTypes.STYRACO.get() || entitytype == ModEntityTypes.BERNIS.get();
    };

    public RexEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier setAttributes() {
//net.minecraftforge.common.ForgeMod.SWIM_SPEED.get()
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 175.0D)
                .add(Attributes.ATTACK_DAMAGE, 40.0f)
                .add(Attributes.ATTACK_SPEED, 0.8f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 3)
                .add(Attributes.MOVEMENT_SPEED, 0.25f).build();
    }

    @Override
    public void setTame(boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(180.0D);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(40.0D);
            getAttribute(Attributes.ATTACK_SPEED).setBaseValue(0.7f);
            getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(3f);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.getBaseSpeed());


        } else {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(175.0D);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(40.0D);
            getAttribute(Attributes.ATTACK_SPEED).setBaseValue(0.6f);
            getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(3f);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.getBaseSpeed());
        }

    }

    @Override
    protected void registerGoals() {

        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1.0D, 11.0F, 8.0F, false));
        this.goalSelector.addGoal(2, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.44D, true));
        this.goalSelector.addGoal(3, new LargeDinoBreedGoal(this, 1.0D));
        //this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(4, new NonTameRandomTargetGoal<>(this, LivingEntity.class, false, PREY_SELECTOR));
        this.targetSelector.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    @Override
    public float getRiderOffset(){
        return 1.2f;
    }

    public float getStepHeight(){
        return 2.0F;
    }

    @Override
    public float getBaseSpeed(){
        return 0.25f;
    }

    @Override
    public float getBaseAttack(){
        return 40f;
    }

    //speed modifier of the entity when being ridden
    public float getSpeedMod(){
        return -0.1f;
    }

    //taming item
    public boolean tameItem(ItemStack pStack){
        Item item = pStack.getItem();
        return item == ModItems.CARNO_BUFF_NETH.get();
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob mob) {
        RexEntity baby = ModEntityTypes.REX.get().create(serverLevel);
        RexVariant variant = Util.getRandom(RexVariant.values(), this.random);
        baby.setVariant(variant);
        return baby;
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
        return ModSounds.REX_HURT.get();
    }

    //Not in AT
    protected SoundEvent getDeathSound() {
        return ModSounds.REX_DEATH.get();
    }

    @Override
    public SoundEvent getChallengedSound(){return ModSounds.REX_CHALLENGED.get();}

    //Not in AT
    protected SoundEvent getAmbientSound() {

        if(this.isAngry()){
            return ModSounds.REX_ANGRY.get();
        }
        else {
            return ModSounds.REX_IDLE.get();
        }
    }

    //Not in AT
    protected SoundEvent getSwimSound() {
        return SoundEvents.GENERIC_SWIM;
    }
    //Not in AT
    protected SoundEvent getSwimSplashSound() {
        return SoundEvents.GENERIC_SPLASH;
    }
    //Not in AT
    protected float getSoundVolume() {
        return 0.6F;
    }

    @Override
    protected int calculateFallDamage(float pDistance, float pDamageMultiplier) {
        if (pDistance < 3.5){
            return 0;
        }else{
            return Mth.ceil((pDistance * 8.0F) * pDamageMultiplier);
        }
    }

    /* VARIANTS */
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_146746_, DifficultyInstance p_146747_,
                                        MobSpawnType p_146748_, @Nullable SpawnGroupData p_146749_,
                                        @Nullable CompoundTag p_146750_) {
        RexVariant variant = Util.getRandom(RexVariant.values(), this.random);
        setVariant(variant);
        return super.finalizeSpawn(p_146746_, p_146747_, p_146748_, p_146749_, p_146750_);
    }

    public RexVariant getVariant() {
        return RexVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(RexVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
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

    //Breakin blocks

    // I think this is to balance stuff like the wither a bit, so that it doesn't just plow through everything.
    //private int destroyBlocksTick;

    public boolean inWall;

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

                        if (!blockstate.isAir() && !blockstate.is(BlockTags.ANVIL) && !blockstate.is(BlockTags.BASE_STONE_OVERWORLD) && !blockstate.is(BlockTags.NEEDS_IRON_TOOL) && !blockstate.is(BlockTags.NEEDS_DIAMOND_TOOL)) {
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

    public void aiStep() {
        super.aiStep();

        if (!this.isBaby()) {
            if (!this.level.isClientSide) {
                this.inWall = this.checkWalls(this.getBoundingBox());
            }
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        //this.destroyBlocksTick > 0
        //--this.destroyBlocksTick;
        //this.destroyBlocksTick == 0

        if (!this.isBaby()) {
            if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this)) {
                int j1 = Mth.floor(this.getY());
                int i2 = Mth.floor(this.getX());
                int j2 = Mth.floor(this.getZ());
                boolean flag = false;

                for (int j = -1; j <= 1; ++j) {
                    for (int k2 = -1; k2 <= 1; ++k2) {
                        for (int k = 0; k <= 3; ++k) {
                            int l2 = i2 + j;
                            int l = j1 + k;
                            int i1 = j2 + k2;
                            BlockPos blockpos = new BlockPos(l2, l, i1);
                            BlockState blockstate = this.level.getBlockState(blockpos);
                            if (blockstate.canEntityDestroy(this.level, blockpos, this) && net.minecraftforge.event.ForgeEventFactory.onEntityDestroyBlock(this, blockpos, blockstate)) {
                                flag = this.level.destroyBlock(blockpos, true, this) || flag;
                            }
                        }
                    }
                }

                if (flag) {
                    this.level.levelEvent((Player) null, 1022, this.blockPosition(), 0);
                }
            }
        }

        if (this.tickCount % 20 == 0) {
            this.heal(1.0F);
        }
    }

}
