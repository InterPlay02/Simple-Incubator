
package net.interplay.simple_incubator.item;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;

import net.interplay.simple_incubator.procedures.BatteryMk2ItemIsCraftedProcedure;

public class BatteryMk2Item extends Item {
	public BatteryMk2Item() {
		super(new Item.Properties().durability(250002).rarity(Rarity.COMMON));
	}

	@Override
	public void onCraftedBy(ItemStack itemstack, Level world, Player entity) {
		super.onCraftedBy(itemstack, world, entity);
		BatteryMk2ItemIsCraftedProcedure.execute(itemstack);
	}
}
