package net.jrex.rexcraft.entity;

import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.entity.custom.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, RexCraft.MOD_ID);

    public static final RegistryObject<EntityType<GeckoEntity>> GECKO =
            ENTITY_TYPES.register("gecko",
                    () -> EntityType.Builder.of(GeckoEntity::new, MobCategory.CREATURE)
                            //Hitbox Size!
                            .sized(0.4f, 0.3f)
                            .build(new ResourceLocation(RexCraft.MOD_ID, "gecko").toString()));

    public static final RegistryObject<EntityType<HedgyEntity>> HEDGY =
            ENTITY_TYPES.register("hedgy",
                    () -> EntityType.Builder.of(HedgyEntity::new, MobCategory.CREATURE)
                            .sized(0.8f, 0.6f)
                            .build(new ResourceLocation(RexCraft.MOD_ID, "hedgy").toString()));

    public static final RegistryObject<EntityType<CricketEntity>> CRICKET =
            ENTITY_TYPES.register("cricket",
                    () -> EntityType.Builder.of(CricketEntity::new, MobCategory.CREATURE)
                            .sized(0.1f, 0.1f)
                            .build(new ResourceLocation(RexCraft.MOD_ID, "cricket").toString()));

    public static final RegistryObject<EntityType<BucklandiiEntity>> BUCKLANDII =
            ENTITY_TYPES.register("bucklandii",
                    () -> EntityType.Builder.of(BucklandiiEntity::new, MobCategory.CREATURE)
                            //Hitbox Size!
                            .sized(1.5f, 2f)
                            .build(new ResourceLocation(RexCraft.MOD_ID, "bucklandii").toString()));

    public static final RegistryObject<EntityType<BernisEntity>> BERNIS =
            ENTITY_TYPES.register("bernis",
                    () -> EntityType.Builder.of(BernisEntity::new, MobCategory.CREATURE)
                            //Hitbox Size!
                            .sized(1.5f, 2f)
                            .build(new ResourceLocation(RexCraft.MOD_ID, "bernis").toString()));

    public static final RegistryObject<EntityType<BorealEntity>> BOREAL =
            ENTITY_TYPES.register("boreal",
                    () -> EntityType.Builder.of(BorealEntity::new, MobCategory.CREATURE)
                            //Hitbox Size!
                            .sized(2.0f, 1.2f)
                            .build(new ResourceLocation(RexCraft.MOD_ID, "boreal").toString()));

    public static final RegistryObject<EntityType<StyracoEntity>> STYRACO =
            ENTITY_TYPES.register("styraco",
                    () -> EntityType.Builder.of(StyracoEntity::new, MobCategory.CREATURE)
                            //Hitbox Size!
                            .sized(2.0f, 2.0f)
                            .build(new ResourceLocation(RexCraft.MOD_ID, "styraco").toString()));

    public static final RegistryObject<EntityType<VeloEntity>> VELO =
            ENTITY_TYPES.register("velo",
                    () -> EntityType.Builder.of(VeloEntity::new, MobCategory.CREATURE)
                            //Hitbox Size!
                            .sized(0.5f, 1f)
                            .build(new ResourceLocation(RexCraft.MOD_ID, "velo").toString()));

    public static final RegistryObject<EntityType<DiploEntity>> DIPLO =
            ENTITY_TYPES.register("diplo",
                    () -> EntityType.Builder.of(DiploEntity::new, MobCategory.CREATURE)
                            //Hitbox Size!
                            .sized(4.3f, 5.1f)
                            .build(new ResourceLocation(RexCraft.MOD_ID, "diplo").toString()));

    public static final RegistryObject<EntityType<OroEntity>> ORO =
            ENTITY_TYPES.register("oro",
                    () -> EntityType.Builder.of(OroEntity::new, MobCategory.CREATURE)
                            //Hitbox Size!
                            .sized(0.5f, 0.9f)
                            .build(new ResourceLocation(RexCraft.MOD_ID, "oro").toString()));

    public static final RegistryObject<EntityType<ProtoEntity>> PROTO =
            ENTITY_TYPES.register("proto",
                    () -> EntityType.Builder.of(ProtoEntity::new, MobCategory.CREATURE)
                            //Hitbox Size!
                            .sized(0.8f, 0.8f)
                            .build(new ResourceLocation(RexCraft.MOD_ID, "proto").toString()));

    public static final RegistryObject<EntityType<JakaEntity>> JAKA =
            ENTITY_TYPES.register("jaka",
                    () -> EntityType.Builder.of(JakaEntity::new, MobCategory.CREATURE)
                            //Hitbox Size!
                            .sized(0.8f, 0.8f)
                            .build(new ResourceLocation(RexCraft.MOD_ID, "jaka").toString()));

    public static final RegistryObject<EntityType<SinoEntity>> SINO =
            ENTITY_TYPES.register("sino",
                    () -> EntityType.Builder.of(SinoEntity::new, MobCategory.CREATURE)
                            //Hitbox Size!
                            .sized(0.4f, 0.4f)
                            .build(new ResourceLocation(RexCraft.MOD_ID, "sino").toString()));

    public static final RegistryObject<EntityType<PrenoEntity>> PRENO =
            ENTITY_TYPES.register("preno",
                    () -> EntityType.Builder.of(PrenoEntity::new, MobCategory.CREATURE)
                            //Hitbox Size!
                            .sized(1.0f, 1.0f)
                            .build(new ResourceLocation(RexCraft.MOD_ID, "preno").toString()));

    public static final RegistryObject<EntityType<RexEntity>> REX =
            ENTITY_TYPES.register("rex",
                    () -> EntityType.Builder.of(RexEntity::new, MobCategory.CREATURE)
                            //Hitbox Size!
                            .sized(3.0f, 3f)
                            .build(new ResourceLocation(RexCraft.MOD_ID, "rex").toString()));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
