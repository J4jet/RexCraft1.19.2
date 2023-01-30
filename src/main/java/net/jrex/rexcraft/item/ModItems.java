package net.jrex.rexcraft.item;

import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.entity.ModEntityTypes;
import net.jrex.rexcraft.item.custom.BleedAxeItem;
import net.jrex.rexcraft.item.custom.BleedSwordItem;
import net.jrex.rexcraft.item.custom.ModArmorItem;
import net.minecraft.world.entity.EquipmentSlot;
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
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> DUBIA = ITEMS.register("dubia",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> ALLO = ITEMS.register("allo",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> CAT_TREAT = ITEMS.register("cat_treat",
                () -> new Item(new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));

        public static final RegistryObject<Item> GECKO_SPAWN_EGG = ITEMS.register("gecko_spawn_egg",
                () -> new ForgeSpawnEggItem(ModEntityTypes.GECKO, 0xFFD133, 0x25241F,
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> HEDGY_SPAWN_EGG = ITEMS.register("hedgy_spawn_egg",
                () -> new ForgeSpawnEggItem(ModEntityTypes.HEDGY, 0x502e22, 0xffede6,
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> STEEL_INGOT = ITEMS.register("steel_ingot",
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
                () -> new ModArmorItem(ModArmorMaterials.STEEL, EquipmentSlot.HEAD,
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> STEEL_CHESTPLATE = ITEMS.register("steel_chestplate",
                () -> new ArmorItem(ModArmorMaterials.STEEL, EquipmentSlot.CHEST,
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> STEEL_LEGGING = ITEMS.register("steel_leggings",
                () -> new ArmorItem(ModArmorMaterials.STEEL, EquipmentSlot.LEGS,
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));
        public static final RegistryObject<Item> STEEL_BOOTS = ITEMS.register("steel_boots",
                () -> new ArmorItem(ModArmorMaterials.STEEL, EquipmentSlot.FEET,
                        new Item.Properties().tab(ModCreativeModeTab.REXCRAFT_TAB)));

        public static void register(IEventBus eventBus) {
            ITEMS.register(eventBus);
        }
}
