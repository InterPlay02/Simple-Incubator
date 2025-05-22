package net.interplay.simple_incubator.procedures;

import org.joml.Matrix4f;

import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.SectionPos;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.Minecraft;

import net.interplay.simple_incubator.init.SimpleIncubatorModBlocks;

import javax.annotation.Nullable;

import java.util.Map;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.systems.RenderSystem;

@EventBusSubscriber(value = Dist.CLIENT)
public class RenderBlinkingLightProcedure {
	private static BufferBuilder bufferBuilder = null;
	private static VertexBuffer vertexBuffer = null;
	private static VertexFormat.Mode mode = null;
	private static VertexFormat format = null;
	private static PoseStack poseStack = null;
	private static Matrix4f modelViewMatrix = null;
	private static Matrix4f projectionMatrix = null;
	private static boolean worldCoordinate = true;
	private static Vec3 offset = Vec3.ZERO;
	private static int currentStage = 0;
	private static int targetStage = 0; // NONE: 0, SKY: 1, WORLD: 2

	private static void add(float x, float y, float z, int color) {
		add(x, y, z, 0.0F, 0.0F, color);
	}

	private static void add(float x, float y, float z, float u, float v, int color) {
		if (bufferBuilder == null)
			return;
		if (format == DefaultVertexFormat.POSITION_COLOR) {
			bufferBuilder.addVertex(x, y, z).setColor(color);
		} else if (format == DefaultVertexFormat.POSITION_TEX_COLOR) {
			bufferBuilder.addVertex(x, y, z).setUv(u, v).setColor(color);
		}
	}

	private static boolean begin(VertexFormat.Mode mode, VertexFormat format, boolean update) {
		if (RenderBlinkingLightProcedure.bufferBuilder == null) {
			if (update)
				clear();
			if (RenderBlinkingLightProcedure.vertexBuffer == null) {
				if (format == DefaultVertexFormat.POSITION_COLOR) {
					RenderBlinkingLightProcedure.mode = mode;
					RenderBlinkingLightProcedure.format = format;
					RenderBlinkingLightProcedure.bufferBuilder = Tesselator.getInstance().begin(mode, DefaultVertexFormat.POSITION_COLOR);
					return true;
				} else if (format == DefaultVertexFormat.POSITION_TEX_COLOR) {
					RenderBlinkingLightProcedure.mode = mode;
					RenderBlinkingLightProcedure.format = format;
					RenderBlinkingLightProcedure.bufferBuilder = Tesselator.getInstance().begin(mode, DefaultVertexFormat.POSITION_TEX_COLOR);
					return true;
				}
			}
		}
		return false;
	}

	private static void clear() {
		if (vertexBuffer != null) {
			vertexBuffer.close();
			vertexBuffer = null;
		}
	}

	private static void end() {
		if (bufferBuilder == null)
			return;
		if (vertexBuffer != null)
			vertexBuffer.close();
		MeshData meshData = bufferBuilder.build();
		if (meshData == null) {
			vertexBuffer = null;
			bufferBuilder = null;
		} else {
			vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
			vertexBuffer.bind();
			vertexBuffer.upload(meshData);
			VertexBuffer.unbind();
			bufferBuilder = null;
		}
	}

	private static void offset(double x, double y, double z) {
		offset = new Vec3(x, y, z);
	}

	private static void release() {
		targetStage = 0;
	}

	private static VertexBuffer shape() {
		return vertexBuffer;
	}

	private static void system(boolean worldCoordinate) {
		RenderBlinkingLightProcedure.worldCoordinate = worldCoordinate;
	}

	private static boolean target(int targetStage) {
		if (targetStage == currentStage) {
			RenderBlinkingLightProcedure.targetStage = targetStage;
			return true;
		}
		return false;
	}

	private static void renderShape(VertexBuffer vertexBuffer, double x, double y, double z, float yaw, float pitch, float roll, float xScale, float yScale, float zScale, int color) {
		if (currentStage == 0 || currentStage != targetStage)
			return;
		if (poseStack == null || projectionMatrix == null)
			return;
		if (vertexBuffer == null)
			return;
		float i, j, k;
		if (worldCoordinate) {
			Vec3 pos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
			i = (float) (x - pos.x());
			j = (float) (y - pos.y());
			k = (float) (z - pos.z());
		} else {
			i = (float) x;
			j = (float) y;
			k = (float) z;
		}
		poseStack.pushPose();
		poseStack.mulPose(modelViewMatrix);
		poseStack.translate(i, j, k);
		poseStack.mulPose(com.mojang.math.Axis.YN.rotationDegrees(yaw));
		poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(pitch));
		poseStack.mulPose(com.mojang.math.Axis.ZN.rotationDegrees(roll));
		poseStack.scale(xScale, yScale, zScale);
		poseStack.translate(offset.x(), offset.y(), offset.z());
		RenderSystem.setShaderColor((color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, (color >>> 24) / 255.0F);
		vertexBuffer.bind();
		vertexBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, vertexBuffer.getFormat().hasUV(0) ? GameRenderer.getPositionTexColorShader() : GameRenderer.getPositionColorShader());
		VertexBuffer.unbind();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		poseStack.popPose();
	}

	@SubscribeEvent
	public static void renderLevel(RenderLevelStageEvent event) {
		if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SKY) {
			currentStage = 1;
			poseStack = new PoseStack();
			RenderSystem.depthMask(false);
			renderShapes(event);
			RenderSystem.enableCull();
			RenderSystem.depthMask(true);
			currentStage = 0;
		} else if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
			currentStage = 2;
			poseStack = event.getPoseStack();
			RenderSystem.depthMask(true);
			renderShapes(event);
			RenderSystem.enableCull();
			RenderSystem.depthMask(true);
			currentStage = 0;
		}
	}

	private static void renderShapes(RenderLevelStageEvent event) {
		Minecraft minecraft = Minecraft.getInstance();
		ClientLevel level = minecraft.level;
		Entity entity = minecraft.gameRenderer.getMainCamera().getEntity();
		if (level != null && entity != null) {
			modelViewMatrix = event.getModelViewMatrix();
			projectionMatrix = event.getProjectionMatrix();
			Vec3 pos = entity.getPosition(event.getPartialTick().getGameTimeDeltaPartialTick(false));
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			execute(event, level);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.defaultBlendFunc();
			RenderSystem.disableBlend();
			RenderSystem.enableDepthTest();
		}
	}

	public static void execute(LevelAccessor world) {
		execute(null, world);
	}

	private static void execute(@Nullable Event event, LevelAccessor world) {
		if (world instanceof ClientLevel _blockEntityContext) {
			int _scanRange = Minecraft.getInstance().options.getEffectiveRenderDistance();
			BlockPos _scanCenter = Minecraft.getInstance().player.blockPosition();
			LevelChunk _levelChunk;
			BlockState blockstateiterator;
			int positionx, positiony, positionz;
			for (int _chunkZ = -_scanRange; _chunkZ <= _scanRange; ++_chunkZ) {
				for (int _chunkX = -_scanRange; _chunkX <= _scanRange; ++_chunkX) {
					_levelChunk = _blockEntityContext.getChunk(SectionPos.blockToSectionCoord(_scanCenter.getX() + (_chunkX << 4)), SectionPos.blockToSectionCoord(_scanCenter.getZ() + (_chunkZ << 4)));
					if (_levelChunk != null) {
						for (Map.Entry<BlockPos, BlockEntity> _blockEntityEntry : _levelChunk.getBlockEntities().entrySet()) {
							blockstateiterator = _blockEntityEntry.getValue().getBlockState();
							positionx = _blockEntityEntry.getKey().getX();
							positiony = _blockEntityEntry.getKey().getY();
							positionz = _blockEntityEntry.getKey().getZ();
							if (blockstateiterator.getBlock() == SimpleIncubatorModBlocks.CHARGING_STATION.get()) {
								if (begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR, true)) {
									add(0, 0, (float) 2.31, 255 << 24 | 0 << 16 | 255 << 8 | 0);
									add(0, 0, 0, 255 << 24 | 0 << 16 | 255 << 8 | 0);
									add(0, (float) 1.05, 0, 255 << 24 | 0 << 16 | 255 << 8 | 0);
									add(0, (float) 1.05, (float) 2.31, 255 << 24 | 0 << 16 | 255 << 8 | 0);
									end();
								}
								if (target(2)) {
									if (new Object() {
										public int getEnergyStored(LevelAccessor level, BlockPos pos) {
											if (level instanceof ILevelExtension _ext) {
												IEnergyStorage _entityStorage = _ext.getCapability(Capabilities.EnergyStorage.BLOCK, pos, null);
												if (_entityStorage != null)
													return _entityStorage.getEnergyStored();
											}
											return 0;
										}
									}.getEnergyStored(world, new BlockPos(positionx, positiony, positionz)) > 0) {
										if ((new Object() {
											public Direction getDirection(BlockState _bs) {
												Property<?> _prop = _bs.getBlock().getStateDefinition().getProperty("facing");
												if (_prop instanceof DirectionProperty _dp)
													return _bs.getValue(_dp);
												_prop = _bs.getBlock().getStateDefinition().getProperty("axis");
												return _prop instanceof EnumProperty _ep && _ep.getPossibleValues().toArray()[0] instanceof Direction.Axis
														? Direction.fromAxisAndDirection((Direction.Axis) _bs.getValue(_ep), Direction.AxisDirection.POSITIVE)
														: Direction.NORTH;
											}
										}.getDirection(blockstateiterator)) == Direction.NORTH) {
											if ((blockstateiterator.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _getip19 ? blockstateiterator.getValue(_getip19) : -1) == 0
													|| (blockstateiterator.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _getip21 ? blockstateiterator.getValue(_getip21) : -1) == 2
													|| (blockstateiterator.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _getip23 ? blockstateiterator.getValue(_getip23) : -1) == 5
													|| (blockstateiterator.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _getip25 ? blockstateiterator.getValue(_getip25) : -1) == 7
													|| (blockstateiterator.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _getip27 ? blockstateiterator.getValue(_getip27) : -1) == 9) {
												renderShape(shape(), (positionx + 0.19), (positiony + 0.79), (positionz - 0.005), -90, 0, 0, (float) 0.025, (float) 0.025, (float) 0.025, (int) (new Object() {
													public double getValue(LevelAccessor world, BlockPos pos, String tag) {
														BlockEntity blockEntity = world.getBlockEntity(pos);
														if (blockEntity != null)
															return blockEntity.getPersistentData().getDouble(tag);
														return -1;
													}
												}.getValue(world, new BlockPos(positionx, positiony, positionz), "opacity")) << 24 | 0 << 16 | 255 << 8 | 0);
											} else {
												renderShape(shape(), (positionx + 0.395), (positiony + 0.79), (positionz - 0.485), -45, 0, 0, (float) 0.025, (float) 0.025, (float) 0.025, (int) (new Object() {
													public double getValue(LevelAccessor world, BlockPos pos, String tag) {
														BlockEntity blockEntity = world.getBlockEntity(pos);
														if (blockEntity != null)
															return blockEntity.getPersistentData().getDouble(tag);
														return -1;
													}
												}.getValue(world, new BlockPos(positionx, positiony, positionz), "opacity")) << 24 | 0 << 16 | 255 << 8 | 0);
											}
										} else if ((new Object() {
											public Direction getDirection(BlockState _bs) {
												Property<?> _prop = _bs.getBlock().getStateDefinition().getProperty("facing");
												if (_prop instanceof DirectionProperty _dp)
													return _bs.getValue(_dp);
												_prop = _bs.getBlock().getStateDefinition().getProperty("axis");
												return _prop instanceof EnumProperty _ep && _ep.getPossibleValues().toArray()[0] instanceof Direction.Axis
														? Direction.fromAxisAndDirection((Direction.Axis) _bs.getValue(_ep), Direction.AxisDirection.POSITIVE)
														: Direction.NORTH;
											}
										}.getDirection(blockstateiterator)) == Direction.SOUTH) {
											if ((blockstateiterator.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _getip52 ? blockstateiterator.getValue(_getip52) : -1) == 0
													|| (blockstateiterator.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _getip54 ? blockstateiterator.getValue(_getip54) : -1) == 2
													|| (blockstateiterator.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _getip56 ? blockstateiterator.getValue(_getip56) : -1) == 5
													|| (blockstateiterator.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _getip58 ? blockstateiterator.getValue(_getip58) : -1) == 7
													|| (blockstateiterator.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _getip60 ? blockstateiterator.getValue(_getip60) : -1) == 9) {
												renderShape(shape(), (positionx + 0.81), (positiony + 0.79), (positionz + 1.005), 90, 0, 0, (float) 0.025, (float) 0.025, (float) 0.025, (int) (new Object() {
													public double getValue(LevelAccessor world, BlockPos pos, String tag) {
														BlockEntity blockEntity = world.getBlockEntity(pos);
														if (blockEntity != null)
															return blockEntity.getPersistentData().getDouble(tag);
														return -1;
													}
												}.getValue(world, new BlockPos(positionx, positiony, positionz), "opacity")) << 24 | 0 << 16 | 255 << 8 | 0);
											} else {
												renderShape(shape(), (positionx + 0.605), (positiony + 0.79), (positionz + 1.485), 135, 0, 0, (float) 0.025, (float) 0.025, (float) 0.025, (int) (new Object() {
													public double getValue(LevelAccessor world, BlockPos pos, String tag) {
														BlockEntity blockEntity = world.getBlockEntity(pos);
														if (blockEntity != null)
															return blockEntity.getPersistentData().getDouble(tag);
														return -1;
													}
												}.getValue(world, new BlockPos(positionx, positiony, positionz), "opacity")) << 24 | 0 << 16 | 255 << 8 | 0);
											}
										} else if ((new Object() {
											public Direction getDirection(BlockState _bs) {
												Property<?> _prop = _bs.getBlock().getStateDefinition().getProperty("facing");
												if (_prop instanceof DirectionProperty _dp)
													return _bs.getValue(_dp);
												_prop = _bs.getBlock().getStateDefinition().getProperty("axis");
												return _prop instanceof EnumProperty _ep && _ep.getPossibleValues().toArray()[0] instanceof Direction.Axis
														? Direction.fromAxisAndDirection((Direction.Axis) _bs.getValue(_ep), Direction.AxisDirection.POSITIVE)
														: Direction.NORTH;
											}
										}.getDirection(blockstateiterator)) == Direction.EAST) {
											if ((blockstateiterator.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _getip85 ? blockstateiterator.getValue(_getip85) : -1) == 0
													|| (blockstateiterator.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _getip87 ? blockstateiterator.getValue(_getip87) : -1) == 2
													|| (blockstateiterator.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _getip89 ? blockstateiterator.getValue(_getip89) : -1) == 5
													|| (blockstateiterator.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _getip91 ? blockstateiterator.getValue(_getip91) : -1) == 7
													|| (blockstateiterator.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _getip93 ? blockstateiterator.getValue(_getip93) : -1) == 9) {
												renderShape(shape(), (positionx + 1.005), (positiony + 0.79), (positionz + 0.19), 0, 0, 0, (float) 0.025, (float) 0.025, (float) 0.025, (int) (new Object() {
													public double getValue(LevelAccessor world, BlockPos pos, String tag) {
														BlockEntity blockEntity = world.getBlockEntity(pos);
														if (blockEntity != null)
															return blockEntity.getPersistentData().getDouble(tag);
														return -1;
													}
												}.getValue(world, new BlockPos(positionx, positiony, positionz), "opacity")) << 24 | 0 << 16 | 255 << 8 | 0);
											} else {
												renderShape(shape(), (positionx + 1.485), (positiony + 0.79), (positionz + 0.395), 45, 0, 0, (float) 0.025, (float) 0.025, (float) 0.025, (int) (new Object() {
													public double getValue(LevelAccessor world, BlockPos pos, String tag) {
														BlockEntity blockEntity = world.getBlockEntity(pos);
														if (blockEntity != null)
															return blockEntity.getPersistentData().getDouble(tag);
														return -1;
													}
												}.getValue(world, new BlockPos(positionx, positiony, positionz), "opacity")) << 24 | 0 << 16 | 255 << 8 | 0);
											}
										} else {
											if ((blockstateiterator.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _getip115 ? blockstateiterator.getValue(_getip115) : -1) == 0
													|| (blockstateiterator.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _getip117 ? blockstateiterator.getValue(_getip117) : -1) == 2
													|| (blockstateiterator.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _getip119 ? blockstateiterator.getValue(_getip119) : -1) == 5
													|| (blockstateiterator.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _getip121 ? blockstateiterator.getValue(_getip121) : -1) == 7
													|| (blockstateiterator.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _getip123 ? blockstateiterator.getValue(_getip123) : -1) == 9) {
												renderShape(shape(), (positionx - 0.005), (positiony + 0.79), (positionz + 0.81), 180, 0, 0, (float) 0.025, (float) 0.025, (float) 0.025, (int) (new Object() {
													public double getValue(LevelAccessor world, BlockPos pos, String tag) {
														BlockEntity blockEntity = world.getBlockEntity(pos);
														if (blockEntity != null)
															return blockEntity.getPersistentData().getDouble(tag);
														return -1;
													}
												}.getValue(world, new BlockPos(positionx, positiony, positionz), "opacity")) << 24 | 0 << 16 | 255 << 8 | 0);
											} else {
												renderShape(shape(), (positionx - 0.485), (positiony + 0.79), (positionz + 0.605), 225, 0, 0, (float) 0.025, (float) 0.025, (float) 0.025, (int) (new Object() {
													public double getValue(LevelAccessor world, BlockPos pos, String tag) {
														BlockEntity blockEntity = world.getBlockEntity(pos);
														if (blockEntity != null)
															return blockEntity.getPersistentData().getDouble(tag);
														return -1;
													}
												}.getValue(world, new BlockPos(positionx, positiony, positionz), "opacity")) << 24 | 0 << 16 | 255 << 8 | 0);
											}
										}
									}
									release();
								}
							}
						}
					}
				}
			}
		}
	}
}
