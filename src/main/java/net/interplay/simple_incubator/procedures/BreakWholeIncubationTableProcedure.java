package net.interplay.simple_incubator.procedures;

import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.client.gui.screens.Screen;

import net.interplay.simple_incubator.init.SimpleIncubatorModItems;
import net.interplay.simple_incubator.init.SimpleIncubatorModBlocks;

import javax.annotation.Nullable;

@EventBusSubscriber
public class BreakWholeIncubationTableProcedure {
	@SubscribeEvent
	public static void onBlockBreak(BlockEvent.BreakEvent event) {
		execute(event, event.getLevel(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ());
	}

	public static void execute(LevelAccessor world, double x, double y, double z) {
		execute(null, world, x, y, z);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z) {
		if (!Screen.hasShiftDown()) {
			if ((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == SimpleIncubatorModBlocks.CHARGING_STATION.get() || (world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == SimpleIncubatorModBlocks.STORAGE_MIDDLE.get()
					|| (world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == SimpleIncubatorModBlocks.INCUBATOR.get() || (world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == SimpleIncubatorModBlocks.STORAGE_RIGHT.get()
					|| (world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == SimpleIncubatorModBlocks.BATTERY_MONITOR.get() || (world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == SimpleIncubatorModBlocks.BUTTON_PANEL.get()
					|| (world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == SimpleIncubatorModBlocks.PROGRESS_MONITOR.get()) {
				if (event instanceof ICancellableEvent _cancellable) {
					_cancellable.setCanceled(true);
				}
				int horizontalRadiusSquare = (int) 3 - 1;
				int verticalRadiusSquare = (int) 2 - 1;
				int yIterationsSquare = verticalRadiusSquare;
				for (int i = -yIterationsSquare; i <= yIterationsSquare; i++) {
					for (int xi = -horizontalRadiusSquare; xi <= horizontalRadiusSquare; xi++) {
						for (int zi = -horizontalRadiusSquare; zi <= horizontalRadiusSquare; zi++) {
							// Execute the desired statements within the square/cube
							if ((world.getBlockState(BlockPos.containing(x + xi, y + i, z + zi))).getBlock() == SimpleIncubatorModBlocks.CHARGING_STATION.get()
									|| (world.getBlockState(BlockPos.containing(x + xi, y + i, z + zi))).getBlock() == SimpleIncubatorModBlocks.STORAGE_MIDDLE.get()
									|| (world.getBlockState(BlockPos.containing(x + xi, y + i, z + zi))).getBlock() == SimpleIncubatorModBlocks.INCUBATOR.get()
									|| (world.getBlockState(BlockPos.containing(x + xi, y + i, z + zi))).getBlock() == SimpleIncubatorModBlocks.STORAGE_RIGHT.get()
									|| (world.getBlockState(BlockPos.containing(x + xi, y + i, z + zi))).getBlock() == SimpleIncubatorModBlocks.BATTERY_MONITOR.get()
									|| (world.getBlockState(BlockPos.containing(x + xi, y + i, z + zi))).getBlock() == SimpleIncubatorModBlocks.BUTTON_PANEL.get()
									|| (world.getBlockState(BlockPos.containing(x + xi, y + i, z + zi))).getBlock() == SimpleIncubatorModBlocks.PROGRESS_MONITOR.get()) {
								world.setBlock(BlockPos.containing(x + xi, y + i, z + zi), Blocks.AIR.defaultBlockState(), 3);
							}
						}
					}
				}
				if (world instanceof ServerLevel _level) {
					ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, new ItemStack(SimpleIncubatorModItems.INCUBATION_TABLE_ITEM.get()));
					entityToSpawn.setPickUpDelay(10);
					_level.addFreshEntity(entityToSpawn);
				}
			}
		}
	}
}
