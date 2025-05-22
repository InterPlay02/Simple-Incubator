package net.interplay.simple_incubator.procedures;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;

public class SpawnFireParticlesProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if ((new Object() {
			public boolean getValue(LevelAccessor world, BlockPos pos, String tag) {
				BlockEntity blockEntity = world.getBlockEntity(pos);
				if (blockEntity != null)
					return blockEntity.getPersistentData().getBoolean(tag);
				return false;
			}
		}.getValue(world, BlockPos.containing(x, y, z), "shouldSpawnParticles")) == true) {
			world.addParticle(ParticleTypes.FLAME, (Mth.nextDouble(RandomSource.create(), x - 0.2, x + 1.2)), (y - 1 + Mth.nextDouble(RandomSource.create(), 0, 1.2)), (Mth.nextDouble(RandomSource.create(), z - 0.2, z + 1.2)), 0, 0.01, 0);
			world.addParticle(ParticleTypes.SMALL_FLAME, (Mth.nextDouble(RandomSource.create(), x - 0.2, x + 1.2)), (y - 1 + Mth.nextDouble(RandomSource.create(), 0, 1.2)), (Mth.nextDouble(RandomSource.create(), z - 0.2, z + 1.2)), 0, 0.01, 0);
			world.addParticle(ParticleTypes.FLAME, (Mth.nextDouble(RandomSource.create(), x - 0.2, x + 1.2)), (y - 1 + Mth.nextDouble(RandomSource.create(), 0, 1.2)), (Mth.nextDouble(RandomSource.create(), z - 0.2, z + 1.2)), 0, 0.01, 0);
			world.addParticle(ParticleTypes.SMALL_FLAME, (Mth.nextDouble(RandomSource.create(), x - 0.2, x + 1.2)), (y - 1 + Mth.nextDouble(RandomSource.create(), 0, 1.2)), (Mth.nextDouble(RandomSource.create(), z - 0.2, z + 1.2)), 0, 0.01, 0);
		}
	}
}
