package net.jrex.rexcraft.event;

import net.jrex.rexcraft.RexCraft;
import net.jrex.rexcraft.entity.client.armor.*;
import net.jrex.rexcraft.item.custom.*;
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
        GeoArmorRenderer.registerArmorRenderer(Steel_DiamondArmorItem.class, new Steel_DiamondArmorRenderer());
        GeoArmorRenderer.registerArmorRenderer(Neth_Steel_ArmorItem.class, new Neth_Steel_ArmorRenderer());
        GeoArmorRenderer.registerArmorRenderer(Gold_Neth_Steel_ArmorItem.class, new Gold_Neth_Steel_ArmorRenderer());
        GeoArmorRenderer.registerArmorRenderer(Diamond_Neth_Steel_ArmorItem.class, new Diamond_Neth_Steel_ArmorRenderer());
        GeoArmorRenderer.registerArmorRenderer(Steel_Neth_Steel_ArmorItem.class, new Steel_Neth_Steel_ArmorRenderer());
    }

}
