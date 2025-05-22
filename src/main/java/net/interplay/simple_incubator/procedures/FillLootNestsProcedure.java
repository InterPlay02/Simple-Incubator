package net.interplay.simple_incubator.procedures;

import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import net.interplay.simple_incubator.init.SimpleIncubatorModBlocks;

import javax.annotation.Nullable;

@EventBusSubscriber
public class FillLootNestsProcedure {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ());
	}

	public static void execute(LevelAccessor world, double x, double y, double z) {
		execute(null, world, x, y, z);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z) {
		double sx = 0;
		double sy = 0;
		double sz = 0;
		double eggNumber = 0;
		ItemStack theEgg = ItemStack.EMPTY;
		sx = -20;
		for (int index0 = 0; index0 < 40; index0++) {
			sy = -20;
			for (int index1 = 0; index1 < 40; index1++) {
				sz = -20;
				for (int index2 = 0; index2 < 40; index2++) {
					if ((world.getBlockState(BlockPos.containing(x + sx, y + sy, z + sz))).getBlock() == SimpleIncubatorModBlocks.NEST.get() && new Object() {
						public int getAmount(LevelAccessor world, BlockPos pos, int slotid) {
							if (world instanceof ILevelExtension _ext) {
								IItemHandler _itemHandler = _ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
								if (_itemHandler != null)
									return _itemHandler.getStackInSlot(slotid).getCount();
							}
							return 0;
						}
					}.getAmount(world, BlockPos.containing(x + sx, y + sy, z + sz), 0) == 0) {
						world.setBlock(BlockPos.containing(x + sx, y + sy, z + sz), Blocks.AIR.defaultBlockState(), 3);
						world.setBlock(BlockPos.containing(x + sx, y + sy, z + sz), SimpleIncubatorModBlocks.NEST.get().defaultBlockState(), 3);
						eggNumber = Mth.nextInt(RandomSource.create(), 1, 3);
						theEgg = new ItemStack(
								(BuiltInRegistries.ITEM.getOrCreateTag(ItemTags.create(ResourceLocation.parse("simple_incubator:eggs"))).getRandomElement(RandomSource.create()).orElseGet(() -> BuiltInRegistries.ITEM.wrapAsHolder(Items.AIR)).value()))
								.copy();
						if (theEgg.getItem() == Items.ENDER_DRAGON_SPAWN_EGG || theEgg.getItem() == Items.WITHER_SPAWN_EGG || theEgg.getItem() == Items.WARDEN_SPAWN_EGG || theEgg.getItem() == Blocks.DRAGON_EGG.asItem()) {
							if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x + sx, y + sy, z + sz), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
								ItemStack _setstack = theEgg.copy();
								_setstack.setCount(1);
								_itemHandlerModifiable.setStackInSlot(0, _setstack);
							}
						} else {
							if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x + sx, y + sy, z + sz), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
								ItemStack _setstack = theEgg.copy();
								_setstack.setCount((int) eggNumber);
								_itemHandlerModifiable.setStackInSlot(0, _setstack);
							}
						}
					}
					sz = sz + 1;
				}
				sy = sy + 1;
			}
			sx = sx + 1;
		}
	}
}
