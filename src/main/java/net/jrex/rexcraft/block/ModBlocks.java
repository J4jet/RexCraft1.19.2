package net.jrex.rexcraft.block;

import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.block.custom.BlueberryCropBlock;
import net.jrex.rexcraft.item.ModCreativeModeTab;
import net.jrex.rexcraft.item.ModItems;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, RexCraft.MOD_ID);

    public static final RegistryObject<Block> WEED = registerBlock("weed",
            () -> new FlowerBlock(MobEffects.MOVEMENT_SLOWDOWN, 3,
                    BlockBehaviour.Properties.copy(Blocks.DANDELION)), ModCreativeModeTab.REXCRAFT_TAB);

    public static final RegistryObject<Block> BLUEBERRY_BUSH = registerBlock("blueberry_bush",
            () -> new FlowerBlock(MobEffects.MOVEMENT_SLOWDOWN, 3,
                    BlockBehaviour.Properties.copy(Blocks.DANDELION)), ModCreativeModeTab.REXCRAFT_TAB);

    public static final RegistryObject<Block> STEEL_BLOCK = registerBlock("steel_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.HEAVY_METAL).strength(7f,10f).requiresCorrectToolForDrops()), ModCreativeModeTab.REXCRAFT_TAB);

    public static final RegistryObject<Block> BLUEBERRY_CROP = BLOCKS.register("blueberry_crop",
            () -> new BlueberryCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab){
        RegistryObject<T> toReturn = BLOCKS.register(name,block);
        registerBlockItem(name,toReturn,tab);

        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

    public static  void  register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }


}
