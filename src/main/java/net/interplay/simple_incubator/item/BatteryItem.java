
package net.interplay.simple_incubator.item;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;

import net.interplay.simple_incubator.procedures.BatteryItemIsCraftedProcedure;

public class BatteryItem extends Item {
	public BatteryItem() {
		super(new Item.Properties().durability(50002).rarity(Rarity.COMMON));
	}

	@Override
	public void onCraftedBy(ItemStack itemstack, Level world, Player entity) {
		super.onCraftedBy(itemstack, world, entity);
		BatteryItemIsCraftedProcedure.execute(itemstack);
	}
}
