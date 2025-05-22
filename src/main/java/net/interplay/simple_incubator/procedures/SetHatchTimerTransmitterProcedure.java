package net.interplay.simple_incubator.procedures;

import net.minecraft.world.item.ItemStack;

import java.io.File;

public class SetHatchTimerTransmitterProcedure {
	public static void execute() {
		com.google.gson.JsonObject configs = new com.google.gson.JsonObject();
		double hatchTimerMin = 0;
		double hatchTimerMax = 0;
		File configFile = new File("");
		ItemStack theEgg = ItemStack.EMPTY;
	}
}
