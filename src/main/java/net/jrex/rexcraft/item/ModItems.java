package net.jrex.rexcraft.item;

import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.entity.ModEntityTypes;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
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

        public static void register(IEventBus eventBus) {
            ITEMS.register(eventBus);
        }
}
