package net.interplay.simple_incubator.procedures;

import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;

import javax.annotation.Nullable;

import java.io.IOException;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

@EventBusSubscriber
public class DropSpawnEggProcedure {
	@SubscribeEvent
	public static void onEntityDeath(LivingDeathEvent event) {
		if (event.getEntity() != null) {
			execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity());
		}
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(null, world, x, y, z, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		File configFile = new File("");
		double chanceToDropEgg = 0;
		com.google.gson.JsonObject mainConfig = new com.google.gson.JsonObject();
		com.google.gson.JsonObject configs = new com.google.gson.JsonObject();
		configFile = new File((FMLPaths.GAMEDIR.get().toString() + "/config/"), File.separator + "SimpleIncubator.json");
		if (configFile.exists()) {
			{
				try {
					BufferedReader bufferedReader = new BufferedReader(new FileReader(configFile));
					StringBuilder jsonstringbuilder = new StringBuilder();
					String line;
					while ((line = bufferedReader.readLine()) != null) {
						jsonstringbuilder.append(line);
					}
					bufferedReader.close();
					mainConfig = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
					configs = mainConfig.get("modConfigs").getAsJsonObject();
					chanceToDropEgg = configs.get("chanceToDropEgg").getAsDouble();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (Math.random() < chanceToDropEgg) {
			if (world instanceof ServerLevel _level) {
				ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z,
						new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.parse(((BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString() + "_spawn_egg")).toLowerCase(java.util.Locale.ENGLISH)))));
				entityToSpawn.setPickUpDelay(10);
				_level.addFreshEntity(entityToSpawn);
			}
		}
	}
}
