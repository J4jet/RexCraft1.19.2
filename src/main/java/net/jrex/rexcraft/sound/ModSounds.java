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

    public static final RegistryObject<SoundEvent> CRICKET_IDLE =
            registerSoundEvent("cricket_idle");



    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(RexCraft.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
