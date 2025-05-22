
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.interplay.simple_incubator.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;

import net.interplay.simple_incubator.SimpleIncubatorMod;

public class SimpleIncubatorModSounds {
	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(Registries.SOUND_EVENT, SimpleIncubatorMod.MODID);
	public static final DeferredHolder<SoundEvent, SoundEvent> INCUBATOR_ON = REGISTRY.register("incubator_on", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("simple_incubator", "incubator_on")));
	public static final DeferredHolder<SoundEvent, SoundEvent> BEEP_SOUND = REGISTRY.register("beep_sound", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("simple_incubator", "beep_sound")));
	public static final DeferredHolder<SoundEvent, SoundEvent> INCUBATOR_UPDATE = REGISTRY.register("incubator_update", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("simple_incubator", "incubator_update")));
	public static final DeferredHolder<SoundEvent, SoundEvent> TRANSMITTER_UPDATE = REGISTRY.register("transmitter_update", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("simple_incubator", "transmitter_update")));
}
