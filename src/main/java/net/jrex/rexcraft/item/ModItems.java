package net.jrex.rexcraft.item;

import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.block.ModBlocks;
import net.jrex.rexcraft.entity.ModEntityTypes;
import net.jrex.rexcraft.item.custom.*;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

    public class ModItems {
        public static final DeferredRegister<Item> ITEMS =
                DeferredRegister.create(ForgeRegistries.ITEMS, RexCraft.MOD_ID);

        public static final RegistryObject<Item> WORM = ITEMS.register("worm",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_MISC)));

        public static final RegistryObject<Item> CRICKET_ITEM = ITEMS.register("cricket_item",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_MISC)));
        public static final RegistryObject<Item> BLUEBERRY_SEEDS = ITEMS.register("blueberry_seeds",
                () -> new ItemNameBlockItem(ModBlocks.BLUEBERRY_CROP.get(),
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_FOODS)));

        public static final RegistryObject<Item> ZUCC_SEEDS = ITEMS.register("zucc_seeds",
                () -> new ItemNameBlockItem(ModBlocks.ZUCC_CROP.get(),
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_FOODS)));

        public static final RegistryObject<Item> BLUEBERRY = ITEMS.register("blueberry",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_FOODS)
                        .food(new FoodProperties.Builder().nutrition(1).saturationMod(1f).build())));
        public static final RegistryObject<Item> DUBIA = ITEMS.register("dubia",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_MISC)));
        public static final RegistryObject<Item> ALLO = ITEMS.register("allo",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_DINOSAURS)));

        public static final RegistryObject<Item> CARNO_BUFF_IRON = ITEMS.register("carno_buff_iron",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_DINOSAURS).food(new FoodProperties.Builder().nutrition(5).saturationMod(0.1F).effect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.5F).meat().build())));

        //(new FoodProperties.Builder()).nutrition(4).saturationMod(0.1F).effect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.8F).meat().build();

        public static final RegistryObject<Item> CARNO_BUFF_GOLD = ITEMS.register("carno_buff_gold",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_DINOSAURS).food(new FoodProperties.Builder().nutrition(6).saturationMod(0.2F).effect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.5F).meat().build())));

        public static final RegistryObject<Item> CARNO_BUFF_DIAMOND = ITEMS.register("carno_buff_diamond",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_DINOSAURS).food(new FoodProperties.Builder().nutrition(7).saturationMod(0.3F).effect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.5F).meat().build())));

        public static final RegistryObject<Item> CARNO_BUFF_NETH = ITEMS.register("carno_buff_neth",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_DINOSAURS).food(new FoodProperties.Builder().nutrition(8).saturationMod(0.4F).effect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.5F).meat().build())));

        public static final RegistryObject<Item> BONESTACK = ITEMS.register("bonestack",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_DINOSAURS)));

        public static final RegistryObject<Item> BEEFSTACK = ITEMS.register("beefstack",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_FOODS).food(new FoodProperties.Builder().nutrition(4).saturationMod(0.1F).effect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.5F).meat().build())));

        public static final RegistryObject<Item> PORKSTACK = ITEMS.register("porkstack",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_FOODS).food(new FoodProperties.Builder().nutrition(4).saturationMod(0.1F).effect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.5F).meat().build())));

        public static final RegistryObject<Item> MUTTONSTACK = ITEMS.register("muttonstack",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_FOODS).food(new FoodProperties.Builder().nutrition(4).saturationMod(0.1F).effect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.5F).meat().build())));

        public static final RegistryObject<Item> CHICKENSTACK = ITEMS.register("chickenstack",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_FOODS).food(new FoodProperties.Builder().nutrition(4).saturationMod(0.1F).effect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.8F).meat().build())));

        public static final RegistryObject<Item> CODSTACK = ITEMS.register("codstack",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_FOODS)
                        .food(new FoodProperties.Builder().nutrition(4).saturationMod(0.1f).build())));

        public static final RegistryObject<Item> SALMONSTACK = ITEMS.register("salmonstack",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_FOODS)
                        .food(new FoodProperties.Builder().nutrition(5).saturationMod(0.1f).build())));

        public static final RegistryObject<Item> BEET_STACK = ITEMS.register("beet_stack",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_FOODS)
                        .food(new FoodProperties.Builder().nutrition(4).saturationMod(0.2f).build())));

        public static final RegistryObject<Item> BLUEBERRY_STACK = ITEMS.register("blueberry_stack",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_FOODS)
                        .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.1f).build())));

        public static final RegistryObject<Item> CARROT_STACK = ITEMS.register("carrot_stack",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_FOODS)
                        .food(new FoodProperties.Builder().nutrition(3).saturationMod(0.1f).build())));

        public static final RegistryObject<Item> POTATO_STACK = ITEMS.register("potato_stack",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_FOODS)
                        .food(new FoodProperties.Builder().nutrition(3).saturationMod(0.1f).build())));

        public static final RegistryObject<Item> SWEETBERRY_STACK = ITEMS.register("sweetberry_stack",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_FOODS)
                        .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.1f).build())));

        public static final RegistryObject<Item> ZUCC_STACK = ITEMS.register("zucc_stack",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_FOODS)
                        .food(new FoodProperties.Builder().nutrition(4).saturationMod(0.1f).build())));

        public static final RegistryObject<Item> ZUCC = ITEMS.register("zucc",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_FOODS)
                        .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.1f).build())));

        public static final RegistryObject<Item> HERB_BUFF_NETH_IRON = ITEMS.register("herb_buff_iron",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_DINOSAURS)
                        .food(new FoodProperties.Builder().nutrition(5).saturationMod(0.1f).build())));

        public static final RegistryObject<Item> HERB_BUFF_GOLD = ITEMS.register("herb_buff_gold",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_DINOSAURS)
                        .food(new FoodProperties.Builder().nutrition(6).saturationMod(0.1f).build())));

        public static final RegistryObject<Item> HERB_BUFF_DIAMOND = ITEMS.register("herb_buff_diamond",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_DINOSAURS)
                        .food(new FoodProperties.Builder().nutrition(7).saturationMod(0.1f).build())));

        public static final RegistryObject<Item> HERB_BUFF_NETH = ITEMS.register("herb_buff_neth",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_DINOSAURS)
                        .food(new FoodProperties.Builder().nutrition(8).saturationMod(0.1f).build())));

        public static final RegistryObject<Item> ZUCC_DOUGH = ITEMS.register("zucc_dough",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_FOODS).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.1F).effect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.4F).build())));

        public static final RegistryObject<Item> ZUCC_BREAD = ITEMS.register("zucc_bread",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_FOODS)
                        .food(new FoodProperties.Builder().nutrition(8).saturationMod(0.6f).build())));

        public static final RegistryObject<Item> IRON_BOWL = ITEMS.register("iron_bowl",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_DINOSAURS)));

        public static final RegistryObject<Item> GOLD_BOWL = ITEMS.register("gold_bowl",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_DINOSAURS)));

        public static final RegistryObject<Item> DIAMOND_BOWL = ITEMS.register("diamond_bowl",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_DINOSAURS)));

        public static final RegistryObject<Item> NETH_BOWL = ITEMS.register("neth_bowl",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_DINOSAURS)));

        public static final RegistryObject<Item> MEG_JAW = ITEMS.register("meg_jaw",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_DINOSAURS)));

        public static final RegistryObject<Item> GECKO_TAIL = ITEMS.register("gecko_tail",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_MISC)));

        public static final RegistryObject<Item> HEDGEHOG_QUILL = ITEMS.register("hedgehog_quill",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_MISC)));
        public static final RegistryObject<Item> CAT_TREAT = ITEMS.register("cat_treat",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_MISC)));

        public static final RegistryObject<Item> GECKO_SPAWN_EGG = ITEMS.register("gecko_spawn_egg",
                () -> new ForgeSpawnEggItem(ModEntityTypes.GECKO, 0xFFD133, 0x25241F,
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_MISC)));

        public static final RegistryObject<Item> BUCKLANDII_SPAWN_EGG = ITEMS.register("bucklandii_spawn_egg",
                () -> new ForgeSpawnEggItem(ModEntityTypes.BUCKLANDII, 0x25241F, 0xFFD133,
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_DINOSAURS)));

        public static final RegistryObject<Item> BERNIS_SPAWN_EGG = ITEMS.register("bernis_spawn_egg",
                () -> new ForgeSpawnEggItem(ModEntityTypes.BERNIS, 0x25241F, 0xFFD133,
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_DINOSAURS)));

        public static final RegistryObject<Item> HEDGY_SPAWN_EGG = ITEMS.register("hedgy_spawn_egg",
                () -> new ForgeSpawnEggItem(ModEntityTypes.HEDGY, 0x502e22, 0xffede6,
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_MISC)));

        public static final RegistryObject<Item> CRICKET_SPAWN_EGG = ITEMS.register("cricket_spawn_egg",
                () -> new ForgeSpawnEggItem(ModEntityTypes.CRICKET, 0x502e22, 0x25241F,
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_MISC)));
        public static final RegistryObject<Item> STEEL_INGOT = ITEMS.register("steel_ingot",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));

        public static final RegistryObject<Item> DIAMOND_SHARD = ITEMS.register("diamond_shard",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));

        public static final RegistryObject<Item> WITHER_ES = ITEMS.register("wither_es",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));

        public static final RegistryObject<Item> POLEAXE = ITEMS.register("poleaxe",
                () -> new SwordItem(ModTiers.STEEL, 7, -2.9f, new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> DIAMOND_POLEAXE = ITEMS.register("diamond_poleaxe",
                () -> new SwordItem(Tiers.DIAMOND, 8, -2.9f, new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> NETHERITE_POLEAXE = ITEMS.register("netherite_poleaxe",
                () -> new SwordItem(Tiers.NETHERITE, 9, -2.9f, new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));

        public static final RegistryObject<Item> LONGSWORD = ITEMS.register("longsword",
                () -> new BleedSwordItem(ModTiers.STEEL, 3, -2.5f, new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> DIAMOND_LONGSWORD = ITEMS.register("diamond_longsword",
                () -> new BleedSwordItem(Tiers.DIAMOND, 4, -2.5f, new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> NETHERITE_LONGSWORD = ITEMS.register("netherite_longsword",
                () -> new BleedSwordItem(Tiers.NETHERITE, 5, -2.5f, new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> WITHER_LONGSWORD = ITEMS.register("wither_longsword",
                () -> new WitherSwordItem(Tiers.NETHERITE, 6, -2.5f, new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> TRAINING_SWORD = ITEMS.register("training_sword",
                () -> new SwordItem(ModTiers.STEEL, -3, -2.5f, new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));

        public static final RegistryObject<Item> FLAIL = ITEMS.register("flail",
                () -> new SwordItem(ModTiers.STEEL, 9, -3.5f, new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> DIAMOND_FLAIL = ITEMS.register("diamond_flail",
                () -> new SwordItem(Tiers.DIAMOND, 10, -3.5f, new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> NETHERITE_FLAIL = ITEMS.register("netherite_flail",
                () -> new SwordItem(Tiers.NETHERITE, 11, -3.5f, new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));

        public static final RegistryObject<Item> POLEAXE_HEAD = ITEMS.register("poleaxe_head",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> POLEAXE_SHAFT = ITEMS.register("poleaxe_shaft",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> POLEAXE_POMMEL = ITEMS.register("poleaxe_pommel",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> LONGSWORD_BLADE = ITEMS.register("longsword_blade",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> LONGSWORD_GUARD = ITEMS.register("longsword_guard",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> LONGSWORD_POMMEL = ITEMS.register("longsword_pommel",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> FLAIL_HEAD = ITEMS.register("flail_head",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> FLAIL_CHAIN = ITEMS.register("flail_chain",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> FLAIL_HANDLE = ITEMS.register("flail_handle",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));

        public static final RegistryObject<Item> DAGGER = ITEMS.register("dagger",
                () -> new BleedSwordItem(ModTiers.STEEL, -3, -1.8f, new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> DAGGER_BLADE = ITEMS.register("dagger_blade",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> DAGGER_POMMEL = ITEMS.register("dagger_pommel",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));

        public static final RegistryObject<Item> DIAMOND_DAGGER = ITEMS.register("diamond_dagger",
                () -> new BleedSwordItem(Tiers.DIAMOND, -2, -1.8f, new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> NETHERITE_DAGGER = ITEMS.register("netherite_dagger",
                () -> new BleedSwordItem(Tiers.NETHERITE, -1, -1.8f, new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));



        public static final RegistryObject<Item> STEEL_HELMET = ITEMS.register("steel_helmet",
                () -> new SteelArmorItem(ModArmorMaterials.STEEL, EquipmentSlot.HEAD,
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> STEEL_CHESTPLATE = ITEMS.register("steel_chestplate",
                () -> new SteelArmorItem(ModArmorMaterials.STEEL, EquipmentSlot.CHEST,
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> STEEL_LEGGING = ITEMS.register("steel_leggings",
                () -> new SteelArmorItem(ModArmorMaterials.STEEL, EquipmentSlot.LEGS,
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> STEEL_BOOTS = ITEMS.register("steel_boots",
                () -> new SteelArmorItem(ModArmorMaterials.STEEL, EquipmentSlot.FEET,
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> GREAT_HELM = ITEMS.register("great_helm",
                () -> new GreatHelmItem(ModArmorMaterials.STEEL, EquipmentSlot.HEAD,
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));

        public static final RegistryObject<Item> STEEL_DIAMOND_HELMET = ITEMS.register("steel_diamond_helmet",
                () -> new Steel_DiamondArmorItem(ModArmorMaterials.STEEL_DIAMOND, EquipmentSlot.HEAD,
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> STEEL_DIAMOND_CHESTPLATE = ITEMS.register("steel_diamond_chestplate",
                () -> new Steel_DiamondArmorItem(ModArmorMaterials.STEEL_DIAMOND, EquipmentSlot.CHEST,
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> STEEL_DIAMOND_LEGGING = ITEMS.register("steel_diamond_leggings",
                () -> new Steel_DiamondArmorItem(ModArmorMaterials.STEEL_DIAMOND, EquipmentSlot.LEGS,
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> STEEL_DIAMOND_BOOTS = ITEMS.register("steel_diamond_boots",
                () -> new Steel_DiamondArmorItem(ModArmorMaterials.STEEL_DIAMOND, EquipmentSlot.FEET,
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));

        public static final RegistryObject<Item> NETH_STEEL_HELMET = ITEMS.register("neth_steel_helmet",
                () -> new Neth_Steel_ArmorItem(ModArmorMaterials.STEEL_NETHER, EquipmentSlot.HEAD,
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> NETH_STEEL_CHESTPLATE = ITEMS.register("neth_steel_chestplate",
                () -> new Neth_Steel_ArmorItem(ModArmorMaterials.STEEL_NETHER, EquipmentSlot.CHEST,
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> NETH_STEEL_LEGGING = ITEMS.register("neth_steel_leggings",
                () -> new Neth_Steel_ArmorItem(ModArmorMaterials.STEEL_NETHER, EquipmentSlot.LEGS,
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> NETH_STEEL_BOOTS = ITEMS.register("neth_steel_boots",
                () -> new Neth_Steel_ArmorItem(ModArmorMaterials.STEEL_NETHER, EquipmentSlot.FEET,
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));

        public static final RegistryObject<Item> GOLD_NETH_STEEL_HELMET = ITEMS.register("gold_neth_steel_helmet",
                () -> new Gold_Neth_Steel_ArmorItem(ModArmorMaterials.STEEL_NETHER, EquipmentSlot.HEAD,
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> GOLD_NETH_STEEL_CHESTPLATE = ITEMS.register("gold_neth_steel_chestplate",
                () -> new Gold_Neth_Steel_ArmorItem(ModArmorMaterials.STEEL_NETHER, EquipmentSlot.CHEST,
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> GOLD_NETH_STEEL_LEGGING = ITEMS.register("gold_neth_steel_leggings",
                () -> new Gold_Neth_Steel_ArmorItem(ModArmorMaterials.STEEL_NETHER, EquipmentSlot.LEGS,
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> GOLD_NETH_STEEL_BOOTS = ITEMS.register("gold_neth_steel_boots",
                () -> new Gold_Neth_Steel_ArmorItem(ModArmorMaterials.STEEL_NETHER, EquipmentSlot.FEET,
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));

        public static final RegistryObject<Item> DIAMOND_NETH_STEEL_HELMET = ITEMS.register("diamond_neth_steel_helmet",
                () -> new Diamond_Neth_Steel_ArmorItem(ModArmorMaterials.STEEL_NETHER, EquipmentSlot.HEAD,
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> DIAMOND_NETH_STEEL_CHESTPLATE = ITEMS.register("diamond_neth_steel_chestplate",
                () -> new Diamond_Neth_Steel_ArmorItem(ModArmorMaterials.STEEL_NETHER, EquipmentSlot.CHEST,
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> DIAMOND_NETH_STEEL_LEGGING = ITEMS.register("diamond_neth_steel_leggings",
                () -> new Diamond_Neth_Steel_ArmorItem(ModArmorMaterials.STEEL_NETHER, EquipmentSlot.LEGS,
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> DIAMOND_NETH_STEEL_BOOTS = ITEMS.register("diamond_neth_steel_boots",
                () -> new Diamond_Neth_Steel_ArmorItem(ModArmorMaterials.STEEL_NETHER, EquipmentSlot.FEET,
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));

        public static final RegistryObject<Item> STEEL_NETH_STEEL_HELMET = ITEMS.register("steel_neth_steel_helmet",
                () -> new Steel_Neth_Steel_ArmorItem(ModArmorMaterials.STEEL_NETHER, EquipmentSlot.HEAD,
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> STEEL_NETH_STEEL_CHESTPLATE = ITEMS.register("steel_neth_steel_chestplate",
                () -> new Steel_Neth_Steel_ArmorItem(ModArmorMaterials.STEEL_NETHER, EquipmentSlot.CHEST,
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> STEEL_NETH_STEEL_LEGGING = ITEMS.register("steel_neth_steel_leggings",
                () -> new Steel_Neth_Steel_ArmorItem(ModArmorMaterials.STEEL_NETHER, EquipmentSlot.LEGS,
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> STEEL_NETH_STEEL_BOOTS = ITEMS.register("steel_neth_steel_boots",
                () -> new Steel_Neth_Steel_ArmorItem(ModArmorMaterials.STEEL_NETHER, EquipmentSlot.FEET,
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));

        public static void register(IEventBus eventBus) {
            ITEMS.register(eventBus);
        }
}
