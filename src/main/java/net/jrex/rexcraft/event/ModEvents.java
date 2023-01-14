package net.jrex.rexcraft.event;

import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.entity.ModEntityTypes;
import net.jrex.rexcraft.entity.custom.GeckoEntity;
import net.jrex.rexcraft.entity.custom.HedgyEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


public class ModEvents {

    @Mod.EventBusSubscriber(modid = RexCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEventBusEvents {
        @SubscribeEvent
        public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
            event.put(ModEntityTypes.GECKO.get(), GeckoEntity.setAttributes());
            event.put(ModEntityTypes.HEDGY.get(), HedgyEntity.setAttributes());
        }

    }


}
