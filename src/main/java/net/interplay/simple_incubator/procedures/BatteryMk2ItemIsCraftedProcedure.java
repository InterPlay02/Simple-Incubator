package net.interplay.simple_incubator.procedures;

import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;

public class BatteryMk2ItemIsCraftedProcedure {
	public static void execute(ItemStack itemstack) {
		{
			final String _tagName = "batteryEnergyLevel";
			final double _tagValue = 250000;
			CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putDouble(_tagName, _tagValue));
		}
	}
}
