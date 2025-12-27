package net.jrex.rexcraft;

import com.mojang.logging.LogUtils;
import net.jrex.rexcraft.block.ModBlocks;
import net.jrex.rexcraft.effect.ModEffects;
import net.jrex.rexcraft.entity.ModEntityTypes;
import net.jrex.rexcraft.entity.client.*;
import net.jrex.rexcraft.item.ModItems;
import net.jrex.rexcraft.painting.ModPaintings;
import net.jrex.rexcraft.sound.ModSounds;
import net.jrex.rexcraft.world.feature.ModConfiguredFeatures;
import net.jrex.rexcraft.world.feature.ModPlacedFeatures;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import software.bernie.geckolib3.GeckoLib;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(RexCraft.MOD_ID)
public class RexCraft
{
    public static final String MOD_ID = "rexcraft";
    private static final Logger LOGGER = LogUtils.getLogger();

    public RexCraft()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        ModPaintings.register(modEventBus);

        ModConfiguredFeatures.regester(modEventBus);
        ModPlacedFeatures.register(modEventBus);

        ModEntityTypes.register(modEventBus);
        ModSounds.register(modEventBus);
        ModEffects.register(modEventBus);

        GeckoLib.initialize();

        modEventBus.addListener(this::commonSetup);


        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            SpawnPlacements.register(ModEntityTypes.GECKO.get(),
                    SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AgeableMob::checkMobSpawnRules);
            SpawnPlacements.register(ModEntityTypes.HEDGY.get(),
                    SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AgeableMob::checkMobSpawnRules);
            SpawnPlacements.register(ModEntityTypes.CRICKET.get(),
                    SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AgeableMob::checkMobSpawnRules);
            SpawnPlacements.register(ModEntityTypes.BUCKLANDII.get(),
                    SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AgeableMob::checkMobSpawnRules);
            SpawnPlacements.register(ModEntityTypes.BERNIS.get(),
                    SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AgeableMob::checkMobSpawnRules);
            SpawnPlacements.register(ModEntityTypes.BOREAL.get(),
                    SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AgeableMob::checkMobSpawnRules);
            SpawnPlacements.register(ModEntityTypes.STYRACO.get(),
                    SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AgeableMob::checkMobSpawnRules);
            SpawnPlacements.register(ModEntityTypes.VELO.get(),
                    SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AgeableMob::checkMobSpawnRules);
            SpawnPlacements.register(ModEntityTypes.DIPLO.get(),
                    SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AgeableMob::checkMobSpawnRules);
            SpawnPlacements.register(ModEntityTypes.ORO.get(),
                    SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AgeableMob::checkMobSpawnRules);
            SpawnPlacements.register(ModEntityTypes.PROTO.get(),
                    SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AgeableMob::checkMobSpawnRules);
            SpawnPlacements.register(ModEntityTypes.JAKA.get(),
                    SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AgeableMob::checkMobSpawnRules);
            SpawnPlacements.register(ModEntityTypes.SINO.get(),
                    SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AgeableMob::checkMobSpawnRules);
            SpawnPlacements.register(ModEntityTypes.PRENO.get(),
                    SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AgeableMob::checkMobSpawnRules);
        });
    }


    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

            EntityRenderers.register(ModEntityTypes.GECKO.get(), GeckoRenderer::new);
            EntityRenderers.register(ModEntityTypes.HEDGY.get(), HedgyRenderer::new);
            EntityRenderers.register(ModEntityTypes.CRICKET.get(), CricketRenderer::new);
            EntityRenderers.register(ModEntityTypes.BUCKLANDII.get(), BucklandiiRenderer::new);
            EntityRenderers.register(ModEntityTypes.BERNIS.get(), BernisRenderer::new);
            EntityRenderers.register(ModEntityTypes.BOREAL.get(), BorealRenderer::new);
            EntityRenderers.register(ModEntityTypes.STYRACO.get(), StyracoRenderer::new);
            EntityRenderers.register(ModEntityTypes.VELO.get(), VeloRenderer::new);
            EntityRenderers.register(ModEntityTypes.DIPLO.get(), DiploRenderer::new);
            EntityRenderers.register(ModEntityTypes.ORO.get(), OroRenderer::new);
            EntityRenderers.register(ModEntityTypes.PROTO.get(), ProtoRenderer::new);
            EntityRenderers.register(ModEntityTypes.JAKA.get(), JakaRenderer::new);
            EntityRenderers.register(ModEntityTypes.SINO.get(), SinoRenderer::new);
            EntityRenderers.register(ModEntityTypes.PRENO.get(), PrenoRenderer::new);
            EntityRenderers.register(ModEntityTypes.REX.get(), RexRenderer::new);
            //ItemBlockRenderTypes.setRenderLayer(ModBlocks.BLUEBERRY_CROP.get(), RenderType.cutout());
            //ItemBlockRenderTypes.setRenderLayer(ModBlocks.ZUCC_CROP.get(), RenderType.cutout());

        }
    }
}
