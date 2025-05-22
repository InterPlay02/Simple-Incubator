
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.interplay.simple_incubator.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.core.registries.Registries;

import net.interplay.simple_incubator.SimpleIncubatorMod;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class SimpleIncubatorModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SimpleIncubatorMod.MODID);

	@SubscribeEvent
	public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
		if (tabData.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
			tabData.accept(SimpleIncubatorModBlocks.NEST.get().asItem());
		} else if (tabData.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
			tabData.accept(SimpleIncubatorModItems.INCUBATION_TABLE_ITEM.get());
			tabData.accept(SimpleIncubatorModBlocks.CHARGING_STATION.get().asItem());
			tabData.accept(SimpleIncubatorModBlocks.STORAGE_MIDDLE.get().asItem());
			tabData.accept(SimpleIncubatorModBlocks.INCUBATOR.get().asItem());
			tabData.accept(SimpleIncubatorModBlocks.STORAGE_RIGHT.get().asItem());
			tabData.accept(SimpleIncubatorModBlocks.BATTERY_MONITOR.get().asItem());
			tabData.accept(SimpleIncubatorModBlocks.BUTTON_PANEL.get().asItem());
			tabData.accept(SimpleIncubatorModBlocks.PROGRESS_MONITOR.get().asItem());
			tabData.accept(SimpleIncubatorModItems.BATTERY_EMPTY.get());
			tabData.accept(SimpleIncubatorModItems.BATTERY.get());
			tabData.accept(SimpleIncubatorModItems.BATTERY_MK_2.get());
			tabData.accept(SimpleIncubatorModItems.BATTERY_MK_3.get());
			tabData.accept(SimpleIncubatorModBlocks.INCUBATION_TRANSMITTER.get().asItem());
			tabData.accept(SimpleIncubatorModBlocks.INCUBATION_RECEIVER.get().asItem());
			tabData.accept(SimpleIncubatorModItems.RANGE_UPDATE_MODULE_MK_1.get());
			tabData.accept(SimpleIncubatorModItems.RANGE_UPDATE_MODULE_MK_2.get());
			tabData.accept(SimpleIncubatorModItems.RANGE_UPDATE_MODULE_MK_3.get());
			tabData.accept(SimpleIncubatorModItems.RANGE_UPDATE_MODULE_MK_4.get());
			tabData.accept(SimpleIncubatorModItems.ENERGY_UPDATE_MODULE_MK_1.get());
			tabData.accept(SimpleIncubatorModItems.ENERGY_UPDATE_MODULE_MK_2.get());
			tabData.accept(SimpleIncubatorModItems.ENERGY_UPDATE_MODULE_MK_3.get());
			tabData.accept(SimpleIncubatorModItems.ENERGY_UPDATE_MODULE_MK_4.get());
		}
	}
}
