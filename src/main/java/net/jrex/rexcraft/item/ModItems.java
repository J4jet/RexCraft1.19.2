package net.jrex.rexcraft.item;

import net.jrex.rexcraft.RexCraft;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
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

        public static void register(IEventBus eventBus) {
            ITEMS.register(eventBus);
        }
}
