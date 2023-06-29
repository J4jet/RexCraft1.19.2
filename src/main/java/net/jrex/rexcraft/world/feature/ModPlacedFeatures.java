package net.jrex.rexcraft.world.feature;

import net.jrex.rexcraft.RexCraft;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ModPlacedFeatures {
    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES =
            DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, RexCraft.MOD_ID);

    public static final RegistryObject<PlacedFeature> WEED_PLACED = PLACED_FEATURES.register("weed_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.WEED.getHolder().get(), List.of(RarityFilter.onAverageOnceEvery(2),
                    InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));

    public static final RegistryObject<PlacedFeature> BLUEBERRY_BUSH_PLACED = PLACED_FEATURES.register("blueberry_bush_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.BLUEBERRY_BUSH.getHolder().get(), List.of(RarityFilter.onAverageOnceEvery(2),
                    InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));


    public static void register(IEventBus eventBus) {
        PLACED_FEATURES.register(eventBus);
    }
}
