package net.jrex.rexcraft.event;

import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.entity.client.armor.GreatHelmRenderer;
import net.jrex.rexcraft.entity.client.armor.SteelArmorRenderer;
import net.jrex.rexcraft.item.custom.GreatHelmItem;
import net.jrex.rexcraft.item.custom.SteelArmorItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

@Mod.EventBusSubscriber(modid = RexCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventClientBusEvents {
    @SubscribeEvent
    public static void registerArmorRenderers(final EntityRenderersEvent.AddLayers event) {
        GeoArmorRenderer.registerArmorRenderer(SteelArmorItem.class, new SteelArmorRenderer());
        GeoArmorRenderer.registerArmorRenderer(GreatHelmItem.class, new GreatHelmRenderer());
    }

}
