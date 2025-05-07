
package net.interplay.simple_incubator.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class BatteryEmptyItem extends Item {
	public BatteryEmptyItem() {
		super(new Item.Properties().durability(10002).rarity(Rarity.COMMON));
	}
}
