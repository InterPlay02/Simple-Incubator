
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

import net.interplay.simple_incubator.item.IncubationTableItemItem;
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

	// Start of user code block custom items
	// End of user code block custom items
	private static DeferredItem<Item> block(DeferredHolder<Block, Block> block) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
	}
}
