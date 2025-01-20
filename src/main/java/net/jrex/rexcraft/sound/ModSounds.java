package net.jrex.rexcraft.sound;

import net.jrex.rexcraft.RexCraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, RexCraft.MOD_ID);

    public static final RegistryObject<SoundEvent> HEDGY_IDLE =
            registerSoundEvent("hedgy_idle");

    public static final RegistryObject<SoundEvent> HEDGY_PAIN =
            registerSoundEvent("hedgy_pain");

    public static final RegistryObject<SoundEvent> HEDGY_DEATH =
            registerSoundEvent("hedgy_death");

    public static final RegistryObject<SoundEvent> GECKO_DEATH =
            registerSoundEvent("gecko_death");

    public static final RegistryObject<SoundEvent> GECKO_HURT =
            registerSoundEvent("gecko_hurt");

    public static final RegistryObject<SoundEvent> BUCKLANDII_DEATH =
            registerSoundEvent("bucklandii_death");

    public static final RegistryObject<SoundEvent> BUCKLANDII_HURT =
            registerSoundEvent("bucklandii_hurt");

    public static final RegistryObject<SoundEvent> BUCKLANDII_GROWL =
            registerSoundEvent("bucklandii_growl");

    public static final RegistryObject<SoundEvent> BUCKLANDII_ANGRY =
            registerSoundEvent("bucklandii_angry");

    public static final RegistryObject<SoundEvent> BERNIS_DEATH =
            registerSoundEvent("bernis_death");

    public static final RegistryObject<SoundEvent> BERNIS_HURT =
            registerSoundEvent("bernis_hurt");

    public static final RegistryObject<SoundEvent> BERNIS_IDLE =
            registerSoundEvent("bernis_idle");

    public static final RegistryObject<SoundEvent> BERNIS_ANGRY =
            registerSoundEvent("bernis_angry");

    public static final RegistryObject<SoundEvent> CRICKET_IDLE =
            registerSoundEvent("cricket_idle");

    public static final RegistryObject<SoundEvent> BOREAL_DEATH =
            registerSoundEvent("boreal_death");

    public static final RegistryObject<SoundEvent> BOREAL_HURT =
            registerSoundEvent("boreal_hurt");

    public static final RegistryObject<SoundEvent> BOREAL_IDLE =
            registerSoundEvent("boreal_idle");

    public static final RegistryObject<SoundEvent> BOREAL_ANGRY =
            registerSoundEvent("boreal_angry");

    public static final RegistryObject<SoundEvent> STYRACO_DEATH =
            registerSoundEvent("styraco_death");

    public static final RegistryObject<SoundEvent> STYRACO_HURT =
            registerSoundEvent("styraco_hurt");

    public static final RegistryObject<SoundEvent> STYRACO_IDLE =
            registerSoundEvent("styraco_idle");

    public static final RegistryObject<SoundEvent> STYRACO_ANGRY =
            registerSoundEvent("styraco_angry");

    public static final RegistryObject<SoundEvent> VELO_DEATH =
            registerSoundEvent("velo_death");

    public static final RegistryObject<SoundEvent> VELO_HURT =
            registerSoundEvent("velo_hurt");

    public static final RegistryObject<SoundEvent> VELO_ANGRY1 =
            registerSoundEvent("velo_angry1");

    public static final RegistryObject<SoundEvent> VELO_ANGRY2 =
            registerSoundEvent("velo_angry2");

    public static final RegistryObject<SoundEvent> VELO_IDLE =
            registerSoundEvent("velo_idle");

    public static final RegistryObject<SoundEvent> DIPLO_DEATH =
            registerSoundEvent("diplo_death");

    public static final RegistryObject<SoundEvent> DIPLO_HURT =
            registerSoundEvent("diplo_hurt");

    public static final RegistryObject<SoundEvent> DIPLO_IDLE =
            registerSoundEvent("diplo_idle");

    public static final RegistryObject<SoundEvent> DIPLO_ANGRY =
            registerSoundEvent("diplo_angry");

    public static final RegistryObject<SoundEvent> PROTO_DEATH =
            registerSoundEvent("proto_death");

    public static final RegistryObject<SoundEvent> PROTO_HURT =
            registerSoundEvent("proto_hurt");

    public static final RegistryObject<SoundEvent> PROTO_IDLE =
            registerSoundEvent("proto_idle");

    public static final RegistryObject<SoundEvent> PROTO_ANGRY =
            registerSoundEvent("proto_angry");

    public static final RegistryObject<SoundEvent> ORO_DEATH =
            registerSoundEvent("oro_death");

    public static final RegistryObject<SoundEvent> ORO_HURT =
            registerSoundEvent("oro_hurt");

    public static final RegistryObject<SoundEvent> ORO_IDLE =
            registerSoundEvent("oro_idle");

    public static final RegistryObject<SoundEvent> JAKA_DEATH =
            registerSoundEvent("jaka_death");

    public static final RegistryObject<SoundEvent> JAKA_HURT =
            registerSoundEvent("jaka_hurt");

    public static final RegistryObject<SoundEvent> JAKA_IDLE =
            registerSoundEvent("jaka_idle");

    public static final RegistryObject<SoundEvent> SINO_DEATH =
            registerSoundEvent("sino_death");

    public static final RegistryObject<SoundEvent> SINO_HURT =
            registerSoundEvent("sino_hurt");

    public static final RegistryObject<SoundEvent> SINO_IDLE =
            registerSoundEvent("sino_idle");



    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(RexCraft.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
