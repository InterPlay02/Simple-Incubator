package net.interplay.simple_incubator.procedures;

import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import net.interplay.simple_incubator.network.SimpleIncubatorModVariables;
import net.interplay.simple_incubator.init.SimpleIncubatorModBlocks;

public class ButtonPanelRightClickedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, BlockState blockstate, double hitX, double hitY, double hitZ) {
		if ((new Object() {
			public Direction getDirection(BlockState _bs) {
				Property<?> _prop = _bs.getBlock().getStateDefinition().getProperty("facing");
				if (_prop instanceof DirectionProperty _dp)
					return _bs.getValue(_dp);
				_prop = _bs.getBlock().getStateDefinition().getProperty("axis");
				return _prop instanceof EnumProperty _ep && _ep.getPossibleValues().toArray()[0] instanceof Direction.Axis ? Direction.fromAxisAndDirection((Direction.Axis) _bs.getValue(_ep), Direction.AxisDirection.POSITIVE) : Direction.NORTH;
			}
		}.getDirection(blockstate)) == Direction.NORTH) {
			if (hitX > x + 0.1 && hitX < x + 0.4 && hitY > y && hitY < y + 0.2 && hitZ > z + 0.1 && hitZ < z + 0.4) {
				if (SimpleIncubatorModVariables.MapVariables.get(world).isButtonPanelPressed == false) {
					if ((world.getBlockState(BlockPos.containing(x + 1, y - 1, z))).getBlock() == SimpleIncubatorModBlocks.CHARGING_STATION.get()
							&& (world.getBlockState(BlockPos.containing(x, y - 1, z))).getBlock() == SimpleIncubatorModBlocks.STORAGE_MIDDLE.get()
							&& (world.getBlockState(BlockPos.containing(x - 1, y - 1, z))).getBlock() == SimpleIncubatorModBlocks.INCUBATOR.get()
							&& (world.getBlockState(BlockPos.containing(x - 1, y - 1, z - 1))).getBlock() == SimpleIncubatorModBlocks.STORAGE_RIGHT.get()
							&& (world.getBlockState(BlockPos.containing(x + 1, y, z))).getBlock() == SimpleIncubatorModBlocks.BATTERY_MONITOR.get()
							&& (world.getBlockState(BlockPos.containing(x - 1, y, z - 1))).getBlock() == SimpleIncubatorModBlocks.PROGRESS_MONITOR.get() && (new Object() {
								public boolean getValue(LevelAccessor world, BlockPos pos, String tag) {
									BlockEntity blockEntity = world.getBlockEntity(pos);
									if (blockEntity != null)
										return blockEntity.getPersistentData().getBoolean(tag);
									return false;
								}
							}.getValue(world, BlockPos.containing(x - 1, y - 1, z), "isIncubatorOn")) == false && new Object() {
								public int getEnergyStored(LevelAccessor level, BlockPos pos) {
									if (level instanceof ILevelExtension _ext) {
										IEnergyStorage _entityStorage = _ext.getCapability(Capabilities.EnergyStorage.BLOCK, pos, null);
										if (_entityStorage != null)
											return _entityStorage.getEnergyStored();
									}
									return 0;
								}
							}.getEnergyStored(world, BlockPos.containing(x - 1, y - 1, z)) > 0) {
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x - 1, y - 1, z);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null)
								_blockEntity.getPersistentData().putBoolean("isIncubatorOn", true);
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
					} else {
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x - 1, y - 1, z);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null)
								_blockEntity.getPersistentData().putBoolean("isIncubatorOn", false);
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
					}
					SimpleIncubatorModVariables.MapVariables.get(world).isButtonPanelPressed = true;
					SimpleIncubatorModVariables.MapVariables.get(world).syncData(world);
					{
						int _value = 1;
						BlockPos _pos = BlockPos.containing(x, y, z);
						BlockState _bs = world.getBlockState(_pos);
						if (_bs.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
							world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
					}
					if (world instanceof Level _level) {
						if (!_level.isClientSide()) {
							_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.stone_button.click_on")), SoundSource.BLOCKS, (float) 0.5, 1);
						} else {
							_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.stone_button.click_on")), SoundSource.BLOCKS, (float) 0.5, 1, false);
						}
					}
				}
			}
		} else if ((new Object() {
			public Direction getDirection(BlockState _bs) {
				Property<?> _prop = _bs.getBlock().getStateDefinition().getProperty("facing");
				if (_prop instanceof DirectionProperty _dp)
					return _bs.getValue(_dp);
				_prop = _bs.getBlock().getStateDefinition().getProperty("axis");
				return _prop instanceof EnumProperty _ep && _ep.getPossibleValues().toArray()[0] instanceof Direction.Axis ? Direction.fromAxisAndDirection((Direction.Axis) _bs.getValue(_ep), Direction.AxisDirection.POSITIVE) : Direction.NORTH;
			}
		}.getDirection(blockstate)) == Direction.SOUTH) {
			if (hitX > x + 0.5 && hitX < x + 0.8 && hitY > y && hitY < y + 0.2 && hitZ > z + 0.6 && hitZ < z + 0.9) {
				if (SimpleIncubatorModVariables.MapVariables.get(world).isButtonPanelPressed == false) {
					if ((world.getBlockState(BlockPos.containing(x - 1, y - 1, z))).getBlock() == SimpleIncubatorModBlocks.CHARGING_STATION.get()
							&& (world.getBlockState(BlockPos.containing(x, y - 1, z))).getBlock() == SimpleIncubatorModBlocks.STORAGE_MIDDLE.get()
							&& (world.getBlockState(BlockPos.containing(x + 1, y - 1, z))).getBlock() == SimpleIncubatorModBlocks.INCUBATOR.get()
							&& (world.getBlockState(BlockPos.containing(x + 1, y - 1, z + 1))).getBlock() == SimpleIncubatorModBlocks.STORAGE_RIGHT.get()
							&& (world.getBlockState(BlockPos.containing(x - 1, y, z))).getBlock() == SimpleIncubatorModBlocks.BATTERY_MONITOR.get()
							&& (world.getBlockState(BlockPos.containing(x + 1, y, z + 1))).getBlock() == SimpleIncubatorModBlocks.PROGRESS_MONITOR.get() && (new Object() {
								public boolean getValue(LevelAccessor world, BlockPos pos, String tag) {
									BlockEntity blockEntity = world.getBlockEntity(pos);
									if (blockEntity != null)
										return blockEntity.getPersistentData().getBoolean(tag);
									return false;
								}
							}.getValue(world, BlockPos.containing(x + 1, y - 1, z), "isIncubatorOn")) == false && new Object() {
								public int getEnergyStored(LevelAccessor level, BlockPos pos) {
									if (level instanceof ILevelExtension _ext) {
										IEnergyStorage _entityStorage = _ext.getCapability(Capabilities.EnergyStorage.BLOCK, pos, null);
										if (_entityStorage != null)
											return _entityStorage.getEnergyStored();
									}
									return 0;
								}
							}.getEnergyStored(world, BlockPos.containing(x + 1, y - 1, z)) > 0) {
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x + 1, y - 1, z);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null)
								_blockEntity.getPersistentData().putBoolean("isIncubatorOn", true);
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
					} else {
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x + 1, y - 1, z);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null)
								_blockEntity.getPersistentData().putBoolean("isIncubatorOn", false);
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
					}
					SimpleIncubatorModVariables.MapVariables.get(world).isButtonPanelPressed = true;
					SimpleIncubatorModVariables.MapVariables.get(world).syncData(world);
					{
						int _value = 1;
						BlockPos _pos = BlockPos.containing(x, y, z);
						BlockState _bs = world.getBlockState(_pos);
						if (_bs.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
							world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
					}
					if (world instanceof Level _level) {
						if (!_level.isClientSide()) {
							_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.stone_button.click_on")), SoundSource.BLOCKS, (float) 0.5, 1);
						} else {
							_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.stone_button.click_on")), SoundSource.BLOCKS, (float) 0.5, 1, false);
						}
					}
				}
			}
		} else if ((new Object() {
			public Direction getDirection(BlockState _bs) {
				Property<?> _prop = _bs.getBlock().getStateDefinition().getProperty("facing");
				if (_prop instanceof DirectionProperty _dp)
					return _bs.getValue(_dp);
				_prop = _bs.getBlock().getStateDefinition().getProperty("axis");
				return _prop instanceof EnumProperty _ep && _ep.getPossibleValues().toArray()[0] instanceof Direction.Axis ? Direction.fromAxisAndDirection((Direction.Axis) _bs.getValue(_ep), Direction.AxisDirection.POSITIVE) : Direction.NORTH;
			}
		}.getDirection(blockstate)) == Direction.EAST) {
			if (hitX > x + 0.5 && hitX < x + 0.8 && hitY > y && hitY < y + 0.2 && hitZ > z + 0.1 && hitZ < z + 0.4) {
				if (SimpleIncubatorModVariables.MapVariables.get(world).isButtonPanelPressed == false) {
					if ((world.getBlockState(BlockPos.containing(x, y - 1, z + 1))).getBlock() == SimpleIncubatorModBlocks.CHARGING_STATION.get()
							&& (world.getBlockState(BlockPos.containing(x, y - 1, z))).getBlock() == SimpleIncubatorModBlocks.STORAGE_MIDDLE.get()
							&& (world.getBlockState(BlockPos.containing(x, y - 1, z - 1))).getBlock() == SimpleIncubatorModBlocks.INCUBATOR.get()
							&& (world.getBlockState(BlockPos.containing(x + 1, y - 1, z - 1))).getBlock() == SimpleIncubatorModBlocks.STORAGE_RIGHT.get()
							&& (world.getBlockState(BlockPos.containing(x, y, z + 1))).getBlock() == SimpleIncubatorModBlocks.BATTERY_MONITOR.get()
							&& (world.getBlockState(BlockPos.containing(x + 1, y, z - 1))).getBlock() == SimpleIncubatorModBlocks.PROGRESS_MONITOR.get() && (new Object() {
								public boolean getValue(LevelAccessor world, BlockPos pos, String tag) {
									BlockEntity blockEntity = world.getBlockEntity(pos);
									if (blockEntity != null)
										return blockEntity.getPersistentData().getBoolean(tag);
									return false;
								}
							}.getValue(world, BlockPos.containing(x, y - 1, z - 1), "isIncubatorOn")) == false && new Object() {
								public int getEnergyStored(LevelAccessor level, BlockPos pos) {
									if (level instanceof ILevelExtension _ext) {
										IEnergyStorage _entityStorage = _ext.getCapability(Capabilities.EnergyStorage.BLOCK, pos, null);
										if (_entityStorage != null)
											return _entityStorage.getEnergyStored();
									}
									return 0;
								}
							}.getEnergyStored(world, BlockPos.containing(x, y - 1, z - 1)) > 0) {
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x, y - 1, z - 1);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null)
								_blockEntity.getPersistentData().putBoolean("isIncubatorOn", true);
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
					} else {
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x, y - 1, z - 1);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null)
								_blockEntity.getPersistentData().putBoolean("isIncubatorOn", false);
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
					}
					SimpleIncubatorModVariables.MapVariables.get(world).isButtonPanelPressed = true;
					SimpleIncubatorModVariables.MapVariables.get(world).syncData(world);
					{
						int _value = 1;
						BlockPos _pos = BlockPos.containing(x, y, z);
						BlockState _bs = world.getBlockState(_pos);
						if (_bs.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
							world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
					}
					if (world instanceof Level _level) {
						if (!_level.isClientSide()) {
							_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.stone_button.click_on")), SoundSource.BLOCKS, (float) 0.5, 1);
						} else {
							_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.stone_button.click_on")), SoundSource.BLOCKS, (float) 0.5, 1, false);
						}
					}
				}
			}
		} else {
			if (hitX > x + 0.1 && hitX < x + 0.4 && hitY > y && hitY < y + 0.2 && hitZ > z + 0.5 && hitZ < z + 0.8) {
				if (SimpleIncubatorModVariables.MapVariables.get(world).isButtonPanelPressed == false) {
					if ((world.getBlockState(BlockPos.containing(x, y - 1, z - 1))).getBlock() == SimpleIncubatorModBlocks.CHARGING_STATION.get()
							&& (world.getBlockState(BlockPos.containing(x, y - 1, z))).getBlock() == SimpleIncubatorModBlocks.STORAGE_MIDDLE.get()
							&& (world.getBlockState(BlockPos.containing(x, y - 1, z + 1))).getBlock() == SimpleIncubatorModBlocks.INCUBATOR.get()
							&& (world.getBlockState(BlockPos.containing(x - 1, y - 1, z + 1))).getBlock() == SimpleIncubatorModBlocks.STORAGE_RIGHT.get()
							&& (world.getBlockState(BlockPos.containing(x, y, z - 1))).getBlock() == SimpleIncubatorModBlocks.BATTERY_MONITOR.get()
							&& (world.getBlockState(BlockPos.containing(x - 1, y, z + 1))).getBlock() == SimpleIncubatorModBlocks.PROGRESS_MONITOR.get() && (new Object() {
								public boolean getValue(LevelAccessor world, BlockPos pos, String tag) {
									BlockEntity blockEntity = world.getBlockEntity(pos);
									if (blockEntity != null)
										return blockEntity.getPersistentData().getBoolean(tag);
									return false;
								}
							}.getValue(world, BlockPos.containing(x, y - 1, z + 1), "isIncubatorOn")) == false && new Object() {
								public int getEnergyStored(LevelAccessor level, BlockPos pos) {
									if (level instanceof ILevelExtension _ext) {
										IEnergyStorage _entityStorage = _ext.getCapability(Capabilities.EnergyStorage.BLOCK, pos, null);
										if (_entityStorage != null)
											return _entityStorage.getEnergyStored();
									}
									return 0;
								}
							}.getEnergyStored(world, BlockPos.containing(x, y - 1, z + 1)) > 0) {
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x, y - 1, z + 1);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null)
								_blockEntity.getPersistentData().putBoolean("isIncubatorOn", true);
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
					} else {
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x, y - 1, z + 1);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null)
								_blockEntity.getPersistentData().putBoolean("isIncubatorOn", false);
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
					}
					SimpleIncubatorModVariables.MapVariables.get(world).isButtonPanelPressed = true;
					SimpleIncubatorModVariables.MapVariables.get(world).syncData(world);
					{
						int _value = 1;
						BlockPos _pos = BlockPos.containing(x, y, z);
						BlockState _bs = world.getBlockState(_pos);
						if (_bs.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
							world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
					}
					if (world instanceof Level _level) {
						if (!_level.isClientSide()) {
							_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.stone_button.click_on")), SoundSource.BLOCKS, (float) 0.5, 1);
						} else {
							_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.stone_button.click_on")), SoundSource.BLOCKS, (float) 0.5, 1, false);
						}
					}
				}
			}
		}
	}
}
