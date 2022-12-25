package net.jrex.rexcraft.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {
    public static final CreativeModeTab REXCRAFT_TAB = new CreativeModeTab("rexcrafttab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.ALLO.get());
        }
    };
}
