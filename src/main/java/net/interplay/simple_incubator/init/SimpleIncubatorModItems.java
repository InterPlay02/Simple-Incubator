
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.interplay.simple_incubator.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;

import net.interplay.simple_incubator.item.RangeUpdateModuleMk4Item;
import net.interplay.simple_incubator.item.RangeUpdateModuleMk3Item;
import net.interplay.simple_incubator.item.RangeUpdateModuleMk2Item;
import net.interplay.simple_incubator.item.RangeUpdateModuleMk1Item;
import net.interplay.simple_incubator.item.IncubationTableItemItem;
import net.interplay.simple_incubator.item.EnergyUpdateModuleMk4Item;
import net.interplay.simple_incubator.item.EnergyUpdateModuleMk3Item;
import net.interplay.simple_incubator.item.EnergyUpdateModuleMk2Item;
import net.interplay.simple_incubator.item.EnergyUpdateModuleMk1Item;
import net.interplay.simple_incubator.item.BatteryMk3Item;
import net.interplay.simple_incubator.item.BatteryMk2Item;
import net.interplay.simple_incubator.item.BatteryItem;
import net.interplay.simple_incubator.item.BatteryEmptyItem;
import net.interplay.simple_incubator.SimpleIncubatorMod;

public class SimpleIncubatorModItems {
	public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(SimpleIncubatorMod.MODID);
	public static final DeferredItem<Item> INCUBATION_TABLE_ITEM = REGISTRY.register("incubation_table_item", IncubationTableItemItem::new);
	public static final DeferredItem<Item> CHARGING_STATION = block(SimpleIncubatorModBlocks.CHARGING_STATION);
	public static final DeferredItem<Item> STORAGE_MIDDLE = block(SimpleIncubatorModBlocks.STORAGE_MIDDLE);
	public static final DeferredItem<Item> INCUBATOR = block(SimpleIncubatorModBlocks.INCUBATOR);
	public static final DeferredItem<Item> STORAGE_RIGHT = block(SimpleIncubatorModBlocks.STORAGE_RIGHT);
	public static final DeferredItem<Item> BATTERY_MONITOR = block(SimpleIncubatorModBlocks.BATTERY_MONITOR);
	public static final DeferredItem<Item> BUTTON_PANEL = block(SimpleIncubatorModBlocks.BUTTON_PANEL);
	public static final DeferredItem<Item> PROGRESS_MONITOR = block(SimpleIncubatorModBlocks.PROGRESS_MONITOR);
	public static final DeferredItem<Item> BATTERY = REGISTRY.register("battery", BatteryItem::new);
	public static final DeferredItem<Item> BATTERY_EMPTY = REGISTRY.register("battery_empty", BatteryEmptyItem::new);
	public static final DeferredItem<Item> BATTERY_MK_2 = REGISTRY.register("battery_mk_2", BatteryMk2Item::new);
	public static final DeferredItem<Item> BATTERY_MK_3 = REGISTRY.register("battery_mk_3", BatteryMk3Item::new);
	public static final DeferredItem<Item> INCUBATION_TRANSMITTER = block(SimpleIncubatorModBlocks.INCUBATION_TRANSMITTER);
	public static final DeferredItem<Item> INCUBATION_RECEIVER = block(SimpleIncubatorModBlocks.INCUBATION_RECEIVER);
	public static final DeferredItem<Item> RANGE_UPDATE_MODULE_MK_1 = REGISTRY.register("range_update_module_mk_1", RangeUpdateModuleMk1Item::new);
	public static final DeferredItem<Item> RANGE_UPDATE_MODULE_MK_2 = REGISTRY.register("range_update_module_mk_2", RangeUpdateModuleMk2Item::new);
	public static final DeferredItem<Item> RANGE_UPDATE_MODULE_MK_3 = REGISTRY.register("range_update_module_mk_3", RangeUpdateModuleMk3Item::new);
	public static final DeferredItem<Item> RANGE_UPDATE_MODULE_MK_4 = REGISTRY.register("range_update_module_mk_4", RangeUpdateModuleMk4Item::new);
	public static final DeferredItem<Item> ENERGY_UPDATE_MODULE_MK_1 = REGISTRY.register("energy_update_module_mk_1", EnergyUpdateModuleMk1Item::new);
	public static final DeferredItem<Item> ENERGY_UPDATE_MODULE_MK_2 = REGISTRY.register("energy_update_module_mk_2", EnergyUpdateModuleMk2Item::new);
	public static final DeferredItem<Item> ENERGY_UPDATE_MODULE_MK_3 = REGISTRY.register("energy_update_module_mk_3", EnergyUpdateModuleMk3Item::new);
	public static final DeferredItem<Item> ENERGY_UPDATE_MODULE_MK_4 = REGISTRY.register("energy_update_module_mk_4", EnergyUpdateModuleMk4Item::new);
	public static final DeferredItem<Item> INCUBATOR_MK_2 = block(SimpleIncubatorModBlocks.INCUBATOR_MK_2);
	public static final DeferredItem<Item> INCUBATOR_MK_3 = block(SimpleIncubatorModBlocks.INCUBATOR_MK_3);
	public static final DeferredItem<Item> INCUBATOR_MK_4 = block(SimpleIncubatorModBlocks.INCUBATOR_MK_4);
	public static final DeferredItem<Item> NEST = block(SimpleIncubatorModBlocks.NEST);

	// Start of user code block custom items
	// End of user code block custom items
	private static DeferredItem<Item> block(DeferredHolder<Block, Block> block) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
	}
}
