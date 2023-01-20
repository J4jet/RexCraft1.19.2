package net.jrex.rexcraft.painting;

import net.jrex.rexcraft.RexCraft;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModPaintings {
    public static final DeferredRegister<PaintingVariant> PAINTING_VARIANTS =
            DeferredRegister.create(ForgeRegistries.PAINTING_VARIANTS, RexCraft.MOD_ID);


    public static final RegistryObject<PaintingVariant> ACE_1 = PAINTING_VARIANTS.register("ace_1",
            () -> new PaintingVariant(48,32));
    public static final RegistryObject<PaintingVariant> ACE_2 = PAINTING_VARIANTS.register("ace_2",
            () -> new PaintingVariant(48,32));

    public static final RegistryObject<PaintingVariant> CASTLE_1 = PAINTING_VARIANTS.register("castle_1",
            () -> new PaintingVariant(80,48));
    public static final RegistryObject<PaintingVariant> CASTLE_2 = PAINTING_VARIANTS.register("castle_2",
            () -> new PaintingVariant(80,48));

    public static final RegistryObject<PaintingVariant> GOONS_1 = PAINTING_VARIANTS.register("goons_1",
            () -> new PaintingVariant(80,48));
    public static final RegistryObject<PaintingVariant> GOONS_2 = PAINTING_VARIANTS.register("goons_2",
            () -> new PaintingVariant(48,32));
    public static final RegistryObject<PaintingVariant> GOONS_3 = PAINTING_VARIANTS.register("goons_3",
            () -> new PaintingVariant(64,32));

    public static final RegistryObject<PaintingVariant> HALO_1 = PAINTING_VARIANTS.register("halo_1",
            () -> new PaintingVariant(48,32));
    public static final RegistryObject<PaintingVariant> HALO_2 = PAINTING_VARIANTS.register("halo_2",
            () -> new PaintingVariant(48,32));
    public static final RegistryObject<PaintingVariant> HALO_3 = PAINTING_VARIANTS.register("halo_3",
            () -> new PaintingVariant(48,32));

    public static final RegistryObject<PaintingVariant> JREX = PAINTING_VARIANTS.register("jrex",
            () -> new PaintingVariant(16,16));

    public static final RegistryObject<PaintingVariant> LADS = PAINTING_VARIANTS.register("lads",
            () -> new PaintingVariant(80,48));

    public static final RegistryObject<PaintingVariant> LAZER = PAINTING_VARIANTS.register("lazer",
            () -> new PaintingVariant(48,32));

    public static final RegistryObject<PaintingVariant> NOVA_1 = PAINTING_VARIANTS.register("nova_1",
            () -> new PaintingVariant(32,32));
    public static final RegistryObject<PaintingVariant> NOVA_2 = PAINTING_VARIANTS.register("nova_2",
            () -> new PaintingVariant(16,32));

    public static final RegistryObject<PaintingVariant> OJ_1 = PAINTING_VARIANTS.register("oj_1",
            () -> new PaintingVariant(48,32));
    public static final RegistryObject<PaintingVariant> OJ_2 = PAINTING_VARIANTS.register("oj_2",
            () -> new PaintingVariant(48,32));

    public static final RegistryObject<PaintingVariant> PATH = PAINTING_VARIANTS.register("path",
            () -> new PaintingVariant(48,32));

    public static final RegistryObject<PaintingVariant> SQUAD = PAINTING_VARIANTS.register("squad",
            () -> new PaintingVariant(48,32));


    public static final RegistryObject<PaintingVariant> T2 = PAINTING_VARIANTS.register("t2",
            () -> new PaintingVariant(64,32));

    public static final RegistryObject<PaintingVariant> TJ_1 = PAINTING_VARIANTS.register("tj_1",
            () -> new PaintingVariant(16,32));

    public static final RegistryObject<PaintingVariant> WARDEN = PAINTING_VARIANTS.register("warden",
            () -> new PaintingVariant(80,48));

    public static final RegistryObject<PaintingVariant> WARDENS = PAINTING_VARIANTS.register("wardens",
            () -> new PaintingVariant(80,48));

    public static final RegistryObject<PaintingVariant> WHERE_IS_MY_LETTUCE = PAINTING_VARIANTS.register("where_is_my_lettuce",
            () -> new PaintingVariant(16,32));

    public static final RegistryObject<PaintingVariant> WILBUR_1 = PAINTING_VARIANTS.register("wilbur_1",
            () -> new PaintingVariant(32,16));
    public static final RegistryObject<PaintingVariant> WILBUR_2 = PAINTING_VARIANTS.register("wilbur_2",
            () -> new PaintingVariant(16,32));
    public static final RegistryObject<PaintingVariant> WILBUR_3 = PAINTING_VARIANTS.register("wilbur_3",
            () -> new PaintingVariant(16,32));
    public static final RegistryObject<PaintingVariant> WILBUR_4 = PAINTING_VARIANTS.register("wilbur_4",
            () -> new PaintingVariant(16,16));
    public static final RegistryObject<PaintingVariant> WILBUR_5 = PAINTING_VARIANTS.register("wilbur_5",
            () -> new PaintingVariant(16,16));
    public static final RegistryObject<PaintingVariant> WILBUR_6 = PAINTING_VARIANTS.register("wilbur_6",
            () -> new PaintingVariant(32,16));

    public static final RegistryObject<PaintingVariant> LILY_1 = PAINTING_VARIANTS.register("lily_1",
            () -> new PaintingVariant(32,16));
    public static final RegistryObject<PaintingVariant> LILY_2 = PAINTING_VARIANTS.register("lily_2",
            () -> new PaintingVariant(16,32));
    public static final RegistryObject<PaintingVariant> LILY_3 = PAINTING_VARIANTS.register("lily_3",
            () -> new PaintingVariant(48,32));




    public static void register(IEventBus eventBus){
        PAINTING_VARIANTS.register(eventBus);
    }
}
