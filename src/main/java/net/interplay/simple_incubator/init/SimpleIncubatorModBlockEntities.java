
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.interplay.simple_incubator.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.registries.BuiltInRegistries;

import net.interplay.simple_incubator.block.entity.StorageRightBlockEntity;
import net.interplay.simple_incubator.block.entity.StorageMiddleBlockEntity;
import net.interplay.simple_incubator.block.entity.ProgressMonitorBlockEntity;
import net.interplay.simple_incubator.block.entity.NestBlockEntity;
import net.interplay.simple_incubator.block.entity.IncubatorMk4BlockEntity;
import net.interplay.simple_incubator.block.entity.IncubatorMk3BlockEntity;
import net.interplay.simple_incubator.block.entity.IncubatorMk2BlockEntity;
import net.interplay.simple_incubator.block.entity.IncubatorBlockEntity;
import net.interplay.simple_incubator.block.entity.IncubationTransmitterBlockEntity;
import net.interplay.simple_incubator.block.entity.IncubationReceiverBlockEntity;
import net.interplay.simple_incubator.block.entity.ChargingStationBlockEntity;
import net.interplay.simple_incubator.block.entity.ButtonPanelBlockEntity;
import net.interplay.simple_incubator.block.entity.BatteryMonitorBlockEntity;
import net.interplay.simple_incubator.SimpleIncubatorMod;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class SimpleIncubatorModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, SimpleIncubatorMod.MODID);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> CHARGING_STATION = register("charging_station", SimpleIncubatorModBlocks.CHARGING_STATION, ChargingStationBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> STORAGE_MIDDLE = register("storage_middle", SimpleIncubatorModBlocks.STORAGE_MIDDLE, StorageMiddleBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> INCUBATOR = register("incubator", SimpleIncubatorModBlocks.INCUBATOR, IncubatorBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> STORAGE_RIGHT = register("storage_right", SimpleIncubatorModBlocks.STORAGE_RIGHT, StorageRightBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> BATTERY_MONITOR = register("battery_monitor", SimpleIncubatorModBlocks.BATTERY_MONITOR, BatteryMonitorBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> BUTTON_PANEL = register("button_panel", SimpleIncubatorModBlocks.BUTTON_PANEL, ButtonPanelBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> PROGRESS_MONITOR = register("progress_monitor", SimpleIncubatorModBlocks.PROGRESS_MONITOR, ProgressMonitorBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> INCUBATION_TRANSMITTER = register("incubation_transmitter", SimpleIncubatorModBlocks.INCUBATION_TRANSMITTER, IncubationTransmitterBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> INCUBATION_RECEIVER = register("incubation_receiver", SimpleIncubatorModBlocks.INCUBATION_RECEIVER, IncubationReceiverBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> INCUBATOR_MK_2 = register("incubator_mk_2", SimpleIncubatorModBlocks.INCUBATOR_MK_2, IncubatorMk2BlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> INCUBATOR_MK_3 = register("incubator_mk_3", SimpleIncubatorModBlocks.INCUBATOR_MK_3, IncubatorMk3BlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> INCUBATOR_MK_4 = register("incubator_mk_4", SimpleIncubatorModBlocks.INCUBATOR_MK_4, IncubatorMk4BlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> NEST = register("nest", SimpleIncubatorModBlocks.NEST, NestBlockEntity::new);

	// Start of user code block custom block entities
	// End of user code block custom block entities
	private static DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> register(String registryname, DeferredHolder<Block, Block> block, BlockEntityType.BlockEntitySupplier<?> supplier) {
		return REGISTRY.register(registryname, () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
	}

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, CHARGING_STATION.get(), (blockEntity, side) -> ((ChargingStationBlockEntity) blockEntity).getItemHandler());
		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, CHARGING_STATION.get(), (blockEntity, side) -> ((ChargingStationBlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, STORAGE_MIDDLE.get(), (blockEntity, side) -> ((StorageMiddleBlockEntity) blockEntity).getItemHandler());
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, INCUBATOR.get(), (blockEntity, side) -> ((IncubatorBlockEntity) blockEntity).getItemHandler());
		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, INCUBATOR.get(), (blockEntity, side) -> ((IncubatorBlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, STORAGE_RIGHT.get(), (blockEntity, side) -> ((StorageRightBlockEntity) blockEntity).getItemHandler());
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BATTERY_MONITOR.get(), (blockEntity, side) -> ((BatteryMonitorBlockEntity) blockEntity).getItemHandler());
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BUTTON_PANEL.get(), (blockEntity, side) -> ((ButtonPanelBlockEntity) blockEntity).getItemHandler());
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, PROGRESS_MONITOR.get(), (blockEntity, side) -> ((ProgressMonitorBlockEntity) blockEntity).getItemHandler());
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, INCUBATION_TRANSMITTER.get(), (blockEntity, side) -> ((IncubationTransmitterBlockEntity) blockEntity).getItemHandler());
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, INCUBATION_RECEIVER.get(), (blockEntity, side) -> ((IncubationReceiverBlockEntity) blockEntity).getItemHandler());
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, INCUBATOR_MK_2.get(), (blockEntity, side) -> ((IncubatorMk2BlockEntity) blockEntity).getItemHandler());
		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, INCUBATOR_MK_2.get(), (blockEntity, side) -> ((IncubatorMk2BlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, INCUBATOR_MK_3.get(), (blockEntity, side) -> ((IncubatorMk3BlockEntity) blockEntity).getItemHandler());
		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, INCUBATOR_MK_3.get(), (blockEntity, side) -> ((IncubatorMk3BlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, INCUBATOR_MK_4.get(), (blockEntity, side) -> ((IncubatorMk4BlockEntity) blockEntity).getItemHandler());
		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, INCUBATOR_MK_4.get(), (blockEntity, side) -> ((IncubatorMk4BlockEntity) blockEntity).getEnergyStorage());
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, NEST.get(), (blockEntity, side) -> ((NestBlockEntity) blockEntity).getItemHandler());
	}
}
