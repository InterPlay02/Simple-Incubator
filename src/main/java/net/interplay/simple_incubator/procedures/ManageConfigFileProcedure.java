package net.interplay.simple_incubator.procedures;

import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import javax.annotation.Nullable;

import java.io.IOException;
import java.io.FileWriter;
import java.io.File;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class ManageConfigFileProcedure {
	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		execute();
	}

	public static void execute() {
		execute(null);
	}

	private static void execute(@Nullable Event event) {
		File configFile = new File("");
		com.google.gson.JsonObject modConfig = new com.google.gson.JsonObject();
		com.google.gson.JsonObject modConfigs = new com.google.gson.JsonObject();
		com.google.gson.JsonObject descriptions = new com.google.gson.JsonObject();
		com.google.gson.JsonObject hatchTimerMin = new com.google.gson.JsonObject();
		com.google.gson.JsonObject hatchTimerMax = new com.google.gson.JsonObject();
		com.google.gson.JsonObject chanceToDropEgg = new com.google.gson.JsonObject();
		configFile = new File((FMLPaths.GAMEDIR.get().toString() + "/config/"), File.separator + "SimpleIncubator.json");
		if (!configFile.exists()) {
			try {
				configFile.getParentFile().mkdirs();
				configFile.createNewFile();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
			modConfig.addProperty("hatchTimerMin", 700);
			modConfig.add("description1", hatchTimerMin);
			hatchTimerMin.addProperty("1a", "This number determines how long (in ticks) an egg will take to hatch (at minimum).");
			hatchTimerMin.addProperty("1b", "The default value is 700, which is equivalent to 35 seconds.");
			modConfig.addProperty("hatchTimerMax", 6000);
			modConfig.add("description2", hatchTimerMax);
			hatchTimerMax.addProperty("2a", "This number determines how long (in ticks) an egg will take to hatch (at max).");
			hatchTimerMax.addProperty("2b", "The default value is 6000, which is equivalent to five minutes.");
			modConfig.addProperty("chanceToDropEgg", 0.005);
			modConfig.add("description3", chanceToDropEgg);
			chanceToDropEgg.addProperty("3a", "This number determines how often a mob will drop their spawn egg.");
			chanceToDropEgg.addProperty("3b", "The default number is 0.005, which is 0.5% chance to drop. Higher values means higher chances to drop.");
			modConfigs.add("modConfigs", modConfig);
			{
				com.google.gson.Gson mainGSONBuilderVariable = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
				try {
					FileWriter fileWriter = new FileWriter(configFile);
					fileWriter.write(mainGSONBuilderVariable.toJson(modConfigs));
					fileWriter.close();
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
		}
	}
}
