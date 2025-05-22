package net.interplay.simple_incubator.procedures;

import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.core.component.DataComponents;

import net.interplay.simple_incubator.init.SimpleIncubatorModItems;

import javax.annotation.Nullable;

import java.util.List;

@EventBusSubscriber(value = {Dist.CLIENT})
public class BatteryTooltipProcedure {
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event) {
		execute(event, event.getItemStack(), event.getToolTip());
	}

	public static void execute(ItemStack itemstack, List<Component> tooltip) {
		execute(null, itemstack, tooltip);
	}

	private static void execute(@Nullable Event event, ItemStack itemstack, List<Component> tooltip) {
		if (tooltip == null)
			return;
		if (itemstack.is(ItemTags.create(ResourceLocation.parse("simple_incubator:batteries"))) && !(itemstack.getItem() == SimpleIncubatorModItems.BATTERY_EMPTY.get())) {
			tooltip.add(1, Component.literal(("\u00A77" + Component.translatable("item.simple_incubator.battery.tooltip").getString())));
			if (itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("batteryEnergyLevel") == 0) {
				tooltip.add(2, Component.literal("\u00A77\u00A7o???? FE"));
			} else {
				tooltip.add(2, Component.literal(("\u00A77\u00A7o" + new java.text.DecimalFormat("##").format(itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("batteryEnergyLevel")) + "\u00A77\u00A7o FE")));
			}
		} else if (itemstack.getItem() == SimpleIncubatorModItems.BATTERY_EMPTY.get()) {
			tooltip.add(1, Component.literal(("\u00A77" + Component.translatable("item.simple_incubator.battery.tooltip").getString())));
			tooltip.add(2, Component.literal("\u00A77\u00A7o0 FE"));
		}
	}
}
