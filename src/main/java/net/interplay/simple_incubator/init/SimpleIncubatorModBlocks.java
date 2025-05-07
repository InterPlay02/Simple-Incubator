
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.interplay.simple_incubator.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredBlock;

import net.minecraft.world.level.block.Block;

import net.interplay.simple_incubator.block.StorageRightBlock;
import net.interplay.simple_incubator.block.StorageMiddleBlock;
import net.interplay.simple_incubator.block.ProgressMonitorBlock;
import net.interplay.simple_incubator.block.IncubatorBlock;
import net.interplay.simple_incubator.block.ChargingStationBlock;
import net.interplay.simple_incubator.block.ButtonPanelBlock;
import net.interplay.simple_incubator.block.BatteryMonitorBlock;
import net.interplay.simple_incubator.SimpleIncubatorMod;

public class SimpleIncubatorModBlocks {
	public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(SimpleIncubatorMod.MODID);
	public static final DeferredBlock<Block> CHARGING_STATION = REGISTRY.register("charging_station", ChargingStationBlock::new);
	public static final DeferredBlock<Block> STORAGE_MIDDLE = REGISTRY.register("storage_middle", StorageMiddleBlock::new);
	public static final DeferredBlock<Block> INCUBATOR = REGISTRY.register("incubator", IncubatorBlock::new);
	public static final DeferredBlock<Block> STORAGE_RIGHT = REGISTRY.register("storage_right", StorageRightBlock::new);
	public static final DeferredBlock<Block> BATTERY_MONITOR = REGISTRY.register("battery_monitor", BatteryMonitorBlock::new);
	public static final DeferredBlock<Block> BUTTON_PANEL = REGISTRY.register("button_panel", ButtonPanelBlock::new);
	public static final DeferredBlock<Block> PROGRESS_MONITOR = REGISTRY.register("progress_monitor", ProgressMonitorBlock::new);
	// Start of user code block custom blocks
	// End of user code block custom blocks
}
