package net.interplay.simple_incubator.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.interplay.simple_incubator.network.SimpleIncubatorModVariables;

public class ButtonPanelRedstoneLevelProcedure {
	public static double execute(LevelAccessor world) {
		if (SimpleIncubatorModVariables.WorldVariables.get(world).isIncubatorOn == true) {
			return 15;
		}
		return 0;
	}
}
