package net.interplay.simple_incubator.procedures;

import org.joml.Vector3f;
import org.joml.Matrix4f;

import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.core.SectionPos;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.gui.Font;
import net.minecraft.client.Minecraft;

import net.interplay.simple_incubator.init.SimpleIncubatorModItems;
import net.interplay.simple_incubator.init.SimpleIncubatorModBlocks;

import javax.annotation.Nullable;

import java.util.Map;
import java.util.HashMap;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

@EventBusSubscriber(value = Dist.CLIENT)
public class RenderReceiverTextsProcedure {
	private static RenderLevelStageEvent provider = null;
	private static Map<EntityType, Entity> data = new HashMap<>();

	public static void renderBackground(String texts, double x, double y, double z, float yaw, float pitch, float roll, float scale, int color) {
		Minecraft minecraft = Minecraft.getInstance();
		Font font = minecraft.font;
		MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
		Vec3 pos = provider.getCamera().getPosition();
		PoseStack poseStack = provider.getPoseStack();
		poseStack.pushPose();
		poseStack.translate(x - pos.x(), y - pos.y(), z - pos.z());
		poseStack.mulPose(com.mojang.math.Axis.YN.rotationDegrees(yaw));
		poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(pitch));
		poseStack.mulPose(com.mojang.math.Axis.ZN.rotationDegrees(roll));
		poseStack.scale(scale, -scale, 1.0F);
		poseStack.translate((font.width(texts) - 1) * -0.5F, (font.lineHeight - 1) * -0.5F, 0.0F);
		Matrix4f matrix4f = poseStack.last().pose();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		font.drawInBatch(texts, 0.0F, 0.0F, 0, false, matrix4f, bufferSource, Font.DisplayMode.SEE_THROUGH, color, LightTexture.FULL_BRIGHT);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		poseStack.popPose();
	}

	public static void renderBlock(BlockState blockState, double x, double y, double z, float yaw, float pitch, float roll, float scale, boolean glowing) {
		BlockPos blockPos = BlockPos.containing(x, y, z);
		Vec3 pos = provider.getCamera().getPosition();
		int packedLight = glowing ? LightTexture.FULL_BRIGHT : LevelRenderer.getLightColor(Minecraft.getInstance().level, blockPos);
		PoseStack poseStack = provider.getPoseStack();
		poseStack.pushPose();
		poseStack.translate(x - pos.x(), y - pos.y(), z - pos.z());
		poseStack.mulPose(com.mojang.math.Axis.YN.rotationDegrees(yaw));
		poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(pitch));
		poseStack.mulPose(com.mojang.math.Axis.ZN.rotationDegrees(roll));
		poseStack.scale(scale, scale, scale);
		poseStack.translate(-0.5F, -0.5F, -0.5F);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		renderBlockModel(blockState, blockPos, poseStack, packedLight);
		renderBlockEntity(blockState, blockPos, poseStack, packedLight);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		poseStack.popPose();
	}

	private static void renderBlockEntity(BlockState blockState, BlockPos blockPos, PoseStack poseStack, int packedLight) {
		if (blockState.getBlock() instanceof EntityBlock entityBlock) {
			Minecraft minecraft = Minecraft.getInstance();
			ClientLevel level = minecraft.level;
			BlockEntity blockEntity = entityBlock.newBlockEntity(blockPos, blockState);
			if (blockEntity != null) {
				BlockEntityRenderer blockEntityRenderer = minecraft.getBlockEntityRenderDispatcher().getRenderer(blockEntity);
				if (blockEntityRenderer != null) {
					MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
					blockEntity.setLevel(level);
					blockEntityRenderer.render(blockEntity, 0.0F, poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY);
				}
			}
		}
	}

	private static void renderBlockModel(BlockState blockState, BlockPos blockPos, PoseStack poseStack, int packedLight) {
		if (blockState.getRenderShape() == RenderShape.MODEL) {
			Minecraft minecraft = Minecraft.getInstance();
			ClientLevel level = minecraft.level;
			MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
			BlockRenderDispatcher dispatcher = minecraft.getBlockRenderer();
			ModelBlockRenderer renderer = dispatcher.getModelRenderer();
			BakedModel bakedModel = dispatcher.getBlockModel(blockState);
			ModelData modelData = bakedModel.getModelData(level, blockPos, blockState, ModelData.builder().build());
			PoseStack.Pose pose = poseStack.last();
			int color = minecraft.getBlockColors().getColor(blockState, level, blockPos);
			float red = (color >> 16 & 255) / 255.0F;
			float green = (color >> 8 & 255) / 255.0F;
			float blue = (color & 255) / 255.0F;
			for (RenderType renderType : bakedModel.getRenderTypes(blockState, RandomSource.create(42L), modelData)) {
				renderer.renderModel(pose, bufferSource.getBuffer(Sheets.translucentCullBlockSheet()), blockState, bakedModel, red, green, blue, packedLight, OverlayTexture.NO_OVERLAY, modelData, renderType);
			}
		}
	}

	public static void renderEntity(EntityType type, double x, double y, double z, float yaw, float pitch, float roll, float scale, boolean glowing) {
		if (type == null)
			return;
		Entity entity;
		ClientLevel level = Minecraft.getInstance().level;
		if (data.containsKey(type)) {
			entity = data.get(type);
			if (entity.level() != level) {
				entity = type.create(level);
				data.put(type, entity);
			}
		} else {
			entity = type.create(level);
			data.put(type, entity);
		}
		renderEntity(entity, 0.0F, x, y, z, yaw, pitch, roll, scale, glowing ? LightTexture.FULL_BRIGHT : LevelRenderer.getLightColor(level, BlockPos.containing(x, y, z)));
	}

	public static void renderEntity(Entity entity, double x, double y, double z, float yaw, float pitch, float roll, float scale, boolean glowing) {
		float partialTick = provider.getPartialTick().getGameTimeDeltaPartialTick(false);
		int packedLight = glowing ? LightTexture.FULL_BRIGHT : Minecraft.getInstance().getEntityRenderDispatcher().getPackedLightCoords(entity, partialTick);
		renderEntity(entity, partialTick, x, y, z, yaw, pitch, roll, scale, packedLight);
	}

	private static void renderEntity(Entity entity, float partialTick, double x, double y, double z, float yaw, float pitch, float roll, float scale, int packedLight) {
		if (entity == null)
			return;
		Minecraft minecraft = Minecraft.getInstance();
		MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
		EntityRenderer renderer = minecraft.getEntityRenderDispatcher().getRenderer(entity);
		Vec3 pos = provider.getCamera().getPosition();
		float offset = (entity.getBbHeight() / 2.0F) * scale;
		PoseStack poseStack = provider.getPoseStack();
		poseStack.pushPose();
		poseStack.translate(x - pos.x(), y + offset - pos.y(), z - pos.z());
		poseStack.mulPose(com.mojang.math.Axis.YN.rotationDegrees(yaw));
		poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(pitch));
		poseStack.mulPose(com.mojang.math.Axis.ZN.rotationDegrees(roll));
		poseStack.translate(0.0F, -offset, 0.0F);
		poseStack.scale(scale, scale, scale);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		renderer.render(entity, entity.getViewYRot(partialTick), partialTick, poseStack, bufferSource, packedLight);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		poseStack.popPose();
	}

	public static void renderItem(ItemStack itemStack, double x, double y, double z, float yaw, float pitch, float roll, float scale, boolean flipping, boolean glowing) {
		Minecraft minecraft = Minecraft.getInstance();
		MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
		ItemRenderer renderer = minecraft.getItemRenderer();
		Vec3 pos = provider.getCamera().getPosition();
		int packedLight = glowing ? LightTexture.FULL_BRIGHT : LevelRenderer.getLightColor(minecraft.level, BlockPos.containing(x, y, z));
		PoseStack poseStack = provider.getPoseStack();
		poseStack.pushPose();
		poseStack.translate(x - pos.x(), y - pos.y(), z - pos.z());
		poseStack.mulPose(com.mojang.math.Axis.YN.rotationDegrees(yaw));
		poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(pitch));
		poseStack.mulPose(com.mojang.math.Axis.ZN.rotationDegrees(roll));
		poseStack.scale(scale, scale, scale);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		renderer.renderStatic(null, itemStack, ItemDisplayContext.FIXED, flipping, poseStack, bufferSource, minecraft.level, packedLight, OverlayTexture.NO_OVERLAY, 0);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		poseStack.popPose();
	}

	public static void renderLine(double x1, double y1, double z1, double x2, double y2, double z2, int color) {
		MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
		Vec3 pos = provider.getCamera().getPosition();
		Vector3f normal = new Vec3(x2 - x1, y2 - y1, z2 - z1).normalize().toVector3f();
		Matrix4f matrix4f = provider.getPoseStack().last().pose();
		VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.lines());
		vertexConsumer.addVertex(matrix4f, (float) (x1 - pos.x()), (float) (y1 - pos.y()), (float) (z1 - pos.z())).setColor(color).setNormal(normal.x(), normal.y(), normal.z());
		vertexConsumer.addVertex(matrix4f, (float) (x2 - pos.x()), (float) (y2 - pos.y()), (float) (z2 - pos.z())).setColor(color).setNormal(normal.x(), normal.y(), normal.z());
	}

	public static void renderTexts(String texts, double x, double y, double z, float yaw, float pitch, float roll, float scale, int color, boolean glowing) {
		Minecraft minecraft = Minecraft.getInstance();
		Font font = minecraft.font;
		MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
		Vec3 pos = provider.getCamera().getPosition();
		int packedLight = glowing ? LightTexture.FULL_BRIGHT : LevelRenderer.getLightColor(minecraft.level, BlockPos.containing(x, y, z));
		PoseStack poseStack = provider.getPoseStack();
		poseStack.pushPose();
		poseStack.translate(x - pos.x(), y - pos.y(), z - pos.z());
		poseStack.mulPose(com.mojang.math.Axis.YN.rotationDegrees(yaw));
		poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(pitch));
		poseStack.mulPose(com.mojang.math.Axis.ZN.rotationDegrees(roll));
		poseStack.scale(scale, -scale, 1.0F);
		poseStack.translate((font.width(texts) - 1) * -0.5F, (font.lineHeight - 1) * -0.5F, 0.0F);
		Matrix4f matrix4f = poseStack.last().pose();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		font.drawInBatch(texts, 0.0F, 0.0F, color, false, matrix4f, bufferSource, Font.DisplayMode.NORMAL, 0, packedLight);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		poseStack.popPose();
	}

	@SubscribeEvent
	public static void renderModels(RenderLevelStageEvent event) {
		provider = event;
		if (provider.getStage() == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
			ClientLevel level = Minecraft.getInstance().level;
			Entity entity = provider.getCamera().getEntity();
			Vec3 pos = entity.getPosition(provider.getPartialTick().getGameTimeDeltaPartialTick(false));
			RenderSystem.depthMask(true);
			RenderSystem.enableDepthTest();
			execute(provider, level);
			RenderSystem.defaultBlendFunc();
			RenderSystem.disableBlend();
			RenderSystem.enableCull();
			RenderSystem.enableDepthTest();
			RenderSystem.depthMask(true);
		}
	}

	public static void execute(LevelAccessor world) {
		execute(null, world);
	}

	private static void execute(@Nullable Event event, LevelAccessor world) {
		String theMob = "";
		double secondTitleYLevel = 0;
		double firstLineYLevel = 0;
		double thirdTitleYLevel = 0;
		double textRotationWest = 0;
		double secondLineYLevel = 0;
		double firstTitleYLevel = 0;
		double textRotationSouth = 0;
		double textRotationEast = 0;
		double textRotationNorth = 0;
		double thirdLineYLevel = 0;
		double mkVersion = 0;
		textRotationNorth = 0;
		textRotationSouth = 180;
		textRotationEast = 90;
		textRotationWest = -90;
		firstTitleYLevel = 0.85;
		firstLineYLevel = 0.8;
		secondTitleYLevel = 0.7;
		secondLineYLevel = 0.65;
		thirdTitleYLevel = 0.55;
		thirdLineYLevel = 0.5;
		mkVersion = 0.4;
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
							if (blockstateiterator.getBlock() == SimpleIncubatorModBlocks.INCUBATION_RECEIVER.get()) {
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
									renderTexts((Component.translatable("block.simple_incubator.incubation_receiver.antena").getString()), (positionx + 0.5), (positiony + firstTitleYLevel), (positionz + 0.751), (float) textRotationNorth, 0, 0,
											(float) 0.005, 255 << 24 | 255 << 16 | 255 << 8 | 255, false);
									if ((world.getBlockState(BlockPos.containing(positionx, positiony, positionz - (new Object() {
										public double getValue(LevelAccessor world, BlockPos pos, String tag) {
											BlockEntity blockEntity = world.getBlockEntity(pos);
											if (blockEntity != null)
												return blockEntity.getPersistentData().getDouble(tag);
											return -1;
										}
									}.getValue(world, new BlockPos(positionx, positiony, positionz), "blockFound"))))).getBlock() == SimpleIncubatorModBlocks.INCUBATION_TRANSMITTER.get()) {
										renderTexts((Component.translatable("block.simple_incubator.incubation_receiver.connected").getString()), (positionx + 0.5), (positiony + firstLineYLevel), (positionz + 0.751), (float) textRotationNorth, 0, 0,
												(float) 0.005, 255 << 24 | 50 << 16 | 200 << 8 | 0, false);
									} else {
										renderTexts((Component.translatable("block.simple_incubator.incubation_receiver.disconnected").getString()), (positionx + 0.5), (positiony + firstLineYLevel), (positionz + 0.751), (float) textRotationNorth, 0,
												0, (float) 0.005, 255 << 24 | 200 << 16 | 0 << 8 | 0, false);
									}
									renderTexts((Component.translatable("block.simple_incubator.incubation_receiver.spawning").getString()), (positionx + 0.5), (positiony + secondTitleYLevel), (positionz + 0.751), (float) textRotationNorth, 0, 0,
											(float) 0.005, 255 << 24 | 255 << 16 | 255 << 8 | 255, false);
									if (!((new Object() {
										public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
											if (world instanceof ILevelExtension _ext) {
												IItemHandler _itemHandler = _ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
												if (_itemHandler != null)
													return _itemHandler.getStackInSlot(slotid).copy();
											}
											return ItemStack.EMPTY;
										}
									}.getItemStack(world, BlockPos.containing(positionx, positiony, positionz - (new Object() {
										public double getValue(LevelAccessor world, BlockPos pos, String tag) {
											BlockEntity blockEntity = world.getBlockEntity(pos);
											if (blockEntity != null)
												return blockEntity.getPersistentData().getDouble(tag);
											return -1;
										}
									}.getValue(world, new BlockPos(positionx, positiony, positionz), "blockFound"))), 0)).is(ItemTags.create(ResourceLocation.parse("simple_incubator:eggs"))))) {
										theMob = Component.translatable("block.simple_incubator.incubation_receiver.nothing").getString();
									} else {
										theMob = ((new Object() {
											public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
												if (world instanceof ILevelExtension _ext) {
													IItemHandler _itemHandler = _ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
													if (_itemHandler != null)
														return _itemHandler.getStackInSlot(slotid).copy();
												}
												return ItemStack.EMPTY;
											}
										}.getItemStack(world, BlockPos.containing(positionx, positiony, positionz - (new Object() {
											public double getValue(LevelAccessor world, BlockPos pos, String tag) {
												BlockEntity blockEntity = world.getBlockEntity(pos);
												if (blockEntity != null)
													return blockEntity.getPersistentData().getDouble(tag);
												return -1;
											}
										}.getValue(world, new BlockPos(positionx, positiony, positionz), "blockFound"))), 0)).getDisplayName().getString()).substring((int) ((new Object() {
											public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
												if (world instanceof ILevelExtension _ext) {
													IItemHandler _itemHandler = _ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
													if (_itemHandler != null)
														return _itemHandler.getStackInSlot(slotid).copy();
												}
												return ItemStack.EMPTY;
											}
										}.getItemStack(world, BlockPos.containing(positionx, positiony, positionz - (new Object() {
											public double getValue(LevelAccessor world, BlockPos pos, String tag) {
												BlockEntity blockEntity = world.getBlockEntity(pos);
												if (blockEntity != null)
													return blockEntity.getPersistentData().getDouble(tag);
												return -1;
											}
										}.getValue(world, new BlockPos(positionx, positiony, positionz), "blockFound"))), 0)).getDisplayName().getString()).indexOf("[") + "[".length(), (int) ((new Object() {
											public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
												if (world instanceof ILevelExtension _ext) {
													IItemHandler _itemHandler = _ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
													if (_itemHandler != null)
														return _itemHandler.getStackInSlot(slotid).copy();
												}
												return ItemStack.EMPTY;
											}
										}.getItemStack(world, BlockPos.containing(positionx, positiony, positionz - (new Object() {
											public double getValue(LevelAccessor world, BlockPos pos, String tag) {
												BlockEntity blockEntity = world.getBlockEntity(pos);
												if (blockEntity != null)
													return blockEntity.getPersistentData().getDouble(tag);
												return -1;
											}
										}.getValue(world, new BlockPos(positionx, positiony, positionz), "blockFound"))), 0)).getDisplayName().getString()).indexOf("]"));
									}
									renderTexts(theMob, (positionx + 0.5), (positiony + secondLineYLevel), (positionz + 0.751), (float) textRotationNorth, 0, 0, (float) 0.003, 255 << 24 | 0 << 16 | 150 << 8 | 255, false);
									renderTexts((Component.translatable("block.simple_incubator.incubation_receiver.operation").getString()), (positionx + 0.5), (positiony + thirdTitleYLevel), (positionz + 0.751), (float) textRotationNorth, 0, 0,
											(float) 0.005, 255 << 24 | 255 << 16 | 255 << 8 | 255, false);
									if ((new Object() {
										public boolean getValue(LevelAccessor world, BlockPos pos, String tag) {
											BlockEntity blockEntity = world.getBlockEntity(pos);
											if (blockEntity != null)
												return blockEntity.getPersistentData().getBoolean(tag);
											return false;
										}
									}.getValue(world, new BlockPos(positionx, positiony, positionz), "shouldSpawnParticles")) == true) {
										renderTexts((Component.translatable("block.simple_incubator.incubation_receiver.active").getString()), (positionx + 0.5), (positiony + thirdLineYLevel), (positionz + 0.751), (float) textRotationNorth, 0, 0,
												(float) 0.005, 255 << 24 | 50 << 16 | 200 << 8 | 0, false);
									} else {
										renderTexts((Component.translatable("block.simple_incubator.incubation_receiver.deactivated").getString()), (positionx + 0.5), (positiony + thirdLineYLevel), (positionz + 0.751), (float) textRotationNorth, 0,
												0, (float) 0.005, 255 << 24 | 200 << 16 | 0 << 8 | 0, false);
									}
									if ((new Object() {
										public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
											if (world instanceof ILevelExtension _ext) {
												IItemHandler _itemHandler = _ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
												if (_itemHandler != null)
													return _itemHandler.getStackInSlot(slotid).copy();
											}
											return ItemStack.EMPTY;
										}
									}.getItemStack(world, new BlockPos(positionx, positiony, positionz), 0)).getItem() == SimpleIncubatorModItems.RANGE_UPDATE_MODULE_MK_2.get()) {
										renderTexts("Mk2", (positionx + 0.5), (positiony + mkVersion), (positionz + 0.751), (float) textRotationNorth, 0, 0, (float) 0.005, 255 << 24 | 255 << 16 | 255 << 8 | 255, false);
									} else if ((new Object() {
										public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
											if (world instanceof ILevelExtension _ext) {
												IItemHandler _itemHandler = _ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
												if (_itemHandler != null)
													return _itemHandler.getStackInSlot(slotid).copy();
											}
											return ItemStack.EMPTY;
										}
									}.getItemStack(world, new BlockPos(positionx, positiony, positionz), 0)).getItem() == SimpleIncubatorModItems.RANGE_UPDATE_MODULE_MK_3.get()) {
										renderTexts("Mk3", (positionx + 0.5), (positiony + mkVersion), (positionz + 0.751), (float) textRotationNorth, 0, 0, (float) 0.005, 255 << 24 | 255 << 16 | 255 << 8 | 255, false);
									} else if ((new Object() {
										public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
											if (world instanceof ILevelExtension _ext) {
												IItemHandler _itemHandler = _ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
												if (_itemHandler != null)
													return _itemHandler.getStackInSlot(slotid).copy();
											}
											return ItemStack.EMPTY;
										}
									}.getItemStack(world, new BlockPos(positionx, positiony, positionz), 0)).getItem() == SimpleIncubatorModItems.RANGE_UPDATE_MODULE_MK_4.get()) {
										renderTexts("Mk4", (positionx + 0.5), (positiony + mkVersion), (positionz + 0.751), (float) textRotationNorth, 0, 0, (float) 0.005, 255 << 24 | 255 << 16 | 255 << 8 | 255, false);
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
									renderTexts((Component.translatable("block.simple_incubator.incubation_receiver.antena").getString()), (positionx + 0.5), (positiony + firstTitleYLevel), (positionz + 0.249), (float) textRotationSouth, 0, 0,
											(float) 0.005, 255 << 24 | 255 << 16 | 255 << 8 | 255, false);
									if ((world.getBlockState(BlockPos.containing(positionx, positiony, positionz + new Object() {
										public double getValue(LevelAccessor world, BlockPos pos, String tag) {
											BlockEntity blockEntity = world.getBlockEntity(pos);
											if (blockEntity != null)
												return blockEntity.getPersistentData().getDouble(tag);
											return -1;
										}
									}.getValue(world, new BlockPos(positionx, positiony, positionz), "blockFound")))).getBlock() == SimpleIncubatorModBlocks.INCUBATION_TRANSMITTER.get()) {
										renderTexts((Component.translatable("block.simple_incubator.incubation_receiver.connected").getString()), (positionx + 0.5), (positiony + firstLineYLevel), (positionz + 0.249), (float) textRotationSouth, 0, 0,
												(float) 0.005, 255 << 24 | 50 << 16 | 200 << 8 | 0, false);
									} else {
										renderTexts((Component.translatable("block.simple_incubator.incubation_receiver.disconnected").getString()), (positionx + 0.5), (positiony + firstLineYLevel), (positionz + 0.249), (float) textRotationSouth, 0,
												0, (float) 0.005, 255 << 24 | 200 << 16 | 0 << 8 | 0, false);
									}
									renderTexts((Component.translatable("block.simple_incubator.incubation_receiver.spawning").getString()), (positionx + 0.5), (positiony + secondTitleYLevel), (positionz + 0.249), (float) textRotationSouth, 0, 0,
											(float) 0.005, 255 << 24 | 255 << 16 | 255 << 8 | 255, false);
									if (!((new Object() {
										public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
											if (world instanceof ILevelExtension _ext) {
												IItemHandler _itemHandler = _ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
												if (_itemHandler != null)
													return _itemHandler.getStackInSlot(slotid).copy();
											}
											return ItemStack.EMPTY;
										}
									}.getItemStack(world, BlockPos.containing(positionx, positiony, positionz + new Object() {
										public double getValue(LevelAccessor world, BlockPos pos, String tag) {
											BlockEntity blockEntity = world.getBlockEntity(pos);
											if (blockEntity != null)
												return blockEntity.getPersistentData().getDouble(tag);
											return -1;
										}
									}.getValue(world, new BlockPos(positionx, positiony, positionz), "blockFound")), 0)).is(ItemTags.create(ResourceLocation.parse("simple_incubator:eggs"))))) {
										theMob = Component.translatable("block.simple_incubator.incubation_receiver.nothing").getString();
									} else {
										theMob = ((new Object() {
											public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
												if (world instanceof ILevelExtension _ext) {
													IItemHandler _itemHandler = _ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
													if (_itemHandler != null)
														return _itemHandler.getStackInSlot(slotid).copy();
												}
												return ItemStack.EMPTY;
											}
										}.getItemStack(world, BlockPos.containing(positionx, positiony, positionz + new Object() {
											public double getValue(LevelAccessor world, BlockPos pos, String tag) {
												BlockEntity blockEntity = world.getBlockEntity(pos);
												if (blockEntity != null)
													return blockEntity.getPersistentData().getDouble(tag);
												return -1;
											}
										}.getValue(world, new BlockPos(positionx, positiony, positionz), "blockFound")), 0)).getDisplayName().getString()).substring((int) ((new Object() {
											public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
												if (world instanceof ILevelExtension _ext) {
													IItemHandler _itemHandler = _ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
													if (_itemHandler != null)
														return _itemHandler.getStackInSlot(slotid).copy();
												}
												return ItemStack.EMPTY;
											}
										}.getItemStack(world, BlockPos.containing(positionx, positiony, positionz + new Object() {
											public double getValue(LevelAccessor world, BlockPos pos, String tag) {
												BlockEntity blockEntity = world.getBlockEntity(pos);
												if (blockEntity != null)
													return blockEntity.getPersistentData().getDouble(tag);
												return -1;
											}
										}.getValue(world, new BlockPos(positionx, positiony, positionz), "blockFound")), 0)).getDisplayName().getString()).indexOf("[") + "[".length(), (int) ((new Object() {
											public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
												if (world instanceof ILevelExtension _ext) {
													IItemHandler _itemHandler = _ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
													if (_itemHandler != null)
														return _itemHandler.getStackInSlot(slotid).copy();
												}
												return ItemStack.EMPTY;
											}
										}.getItemStack(world, BlockPos.containing(positionx, positiony, positionz + new Object() {
											public double getValue(LevelAccessor world, BlockPos pos, String tag) {
												BlockEntity blockEntity = world.getBlockEntity(pos);
												if (blockEntity != null)
													return blockEntity.getPersistentData().getDouble(tag);
												return -1;
											}
										}.getValue(world, new BlockPos(positionx, positiony, positionz), "blockFound")), 0)).getDisplayName().getString()).indexOf("]"));
									}
									renderTexts(theMob, (positionx + 0.5), (positiony + secondLineYLevel), (positionz + 0.249), (float) textRotationSouth, 0, 0, (float) 0.003, 255 << 24 | 0 << 16 | 150 << 8 | 255, false);
									renderTexts((Component.translatable("block.simple_incubator.incubation_receiver.operation").getString()), (positionx + 0.5), (positiony + thirdTitleYLevel), (positionz + 0.249), (float) textRotationSouth, 0, 0,
											(float) 0.005, 255 << 24 | 255 << 16 | 255 << 8 | 255, false);
									if ((new Object() {
										public boolean getValue(LevelAccessor world, BlockPos pos, String tag) {
											BlockEntity blockEntity = world.getBlockEntity(pos);
											if (blockEntity != null)
												return blockEntity.getPersistentData().getBoolean(tag);
											return false;
										}
									}.getValue(world, new BlockPos(positionx, positiony, positionz), "shouldSpawnParticles")) == true) {
										renderTexts((Component.translatable("block.simple_incubator.incubation_receiver.active").getString()), (positionx + 0.5), (positiony + thirdLineYLevel), (positionz + 0.249), (float) textRotationSouth, 0, 0,
												(float) 0.005, 255 << 24 | 50 << 16 | 200 << 8 | 0, false);
									} else {
										renderTexts((Component.translatable("block.simple_incubator.incubation_receiver.deactivated").getString()), (positionx + 0.5), (positiony + thirdLineYLevel), (positionz + 0.249), (float) textRotationSouth, 0,
												0, (float) 0.005, 255 << 24 | 200 << 16 | 0 << 8 | 0, false);
									}
									if ((new Object() {
										public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
											if (world instanceof ILevelExtension _ext) {
												IItemHandler _itemHandler = _ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
												if (_itemHandler != null)
													return _itemHandler.getStackInSlot(slotid).copy();
											}
											return ItemStack.EMPTY;
										}
									}.getItemStack(world, new BlockPos(positionx, positiony, positionz), 0)).getItem() == SimpleIncubatorModItems.RANGE_UPDATE_MODULE_MK_2.get()) {
										renderTexts("Mk2", (positionx + 0.5), (positiony + mkVersion), (positionz + 0.249), (float) textRotationSouth, 0, 0, (float) 0.005, 255 << 24 | 255 << 16 | 255 << 8 | 255, false);
									} else if ((new Object() {
										public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
											if (world instanceof ILevelExtension _ext) {
												IItemHandler _itemHandler = _ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
												if (_itemHandler != null)
													return _itemHandler.getStackInSlot(slotid).copy();
											}
											return ItemStack.EMPTY;
										}
									}.getItemStack(world, new BlockPos(positionx, positiony, positionz), 0)).getItem() == SimpleIncubatorModItems.RANGE_UPDATE_MODULE_MK_3.get()) {
										renderTexts("Mk3", (positionx + 0.5), (positiony + mkVersion), (positionz + 0.249), (float) textRotationSouth, 0, 0, (float) 0.005, 255 << 24 | 255 << 16 | 255 << 8 | 255, false);
									} else if ((new Object() {
										public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
											if (world instanceof ILevelExtension _ext) {
												IItemHandler _itemHandler = _ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
												if (_itemHandler != null)
													return _itemHandler.getStackInSlot(slotid).copy();
											}
											return ItemStack.EMPTY;
										}
									}.getItemStack(world, new BlockPos(positionx, positiony, positionz), 0)).getItem() == SimpleIncubatorModItems.RANGE_UPDATE_MODULE_MK_4.get()) {
										renderTexts("Mk4", (positionx + 0.5), (positiony + mkVersion), (positionz + 0.249), (float) textRotationSouth, 0, 0, (float) 0.005, 255 << 24 | 255 << 16 | 255 << 8 | 255, false);
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
									renderTexts((Component.translatable("block.simple_incubator.incubation_receiver.antena").getString()), (positionx + 0.249), (positiony + firstTitleYLevel), (positionz + 0.5), (float) textRotationEast, 0, 0,
											(float) 0.005, 255 << 24 | 255 << 16 | 255 << 8 | 255, false);
									if ((world.getBlockState(BlockPos.containing(positionx + new Object() {
										public double getValue(LevelAccessor world, BlockPos pos, String tag) {
											BlockEntity blockEntity = world.getBlockEntity(pos);
											if (blockEntity != null)
												return blockEntity.getPersistentData().getDouble(tag);
											return -1;
										}
									}.getValue(world, new BlockPos(positionx, positiony, positionz), "blockFound"), positiony, positionz))).getBlock() == SimpleIncubatorModBlocks.INCUBATION_TRANSMITTER.get()) {
										renderTexts((Component.translatable("block.simple_incubator.incubation_receiver.connected").getString()), (positionx + 0.249), (positiony + firstLineYLevel), (positionz + 0.5), (float) textRotationEast, 0, 0,
												(float) 0.005, 255 << 24 | 50 << 16 | 200 << 8 | 0, false);
									} else {
										renderTexts((Component.translatable("block.simple_incubator.incubation_receiver.disconnected").getString()), (positionx + 0.249), (positiony + firstLineYLevel), (positionz + 0.5), (float) textRotationEast, 0,
												0, (float) 0.005, 255 << 24 | 200 << 16 | 0 << 8 | 0, false);
									}
									renderTexts((Component.translatable("block.simple_incubator.incubation_receiver.spawning").getString()), (positionx + 0.249), (positiony + secondTitleYLevel), (positionz + 0.5), (float) textRotationEast, 0, 0,
											(float) 0.005, 255 << 24 | 255 << 16 | 255 << 8 | 255, false);
									if (!((new Object() {
										public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
											if (world instanceof ILevelExtension _ext) {
												IItemHandler _itemHandler = _ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
												if (_itemHandler != null)
													return _itemHandler.getStackInSlot(slotid).copy();
											}
											return ItemStack.EMPTY;
										}
									}.getItemStack(world, BlockPos.containing(positionx + new Object() {
										public double getValue(LevelAccessor world, BlockPos pos, String tag) {
											BlockEntity blockEntity = world.getBlockEntity(pos);
											if (blockEntity != null)
												return blockEntity.getPersistentData().getDouble(tag);
											return -1;
										}
									}.getValue(world, new BlockPos(positionx, positiony, positionz), "blockFound"), positiony, positionz), 0)).is(ItemTags.create(ResourceLocation.parse("simple_incubator:eggs"))))) {
										theMob = Component.translatable("block.simple_incubator.incubation_receiver.nothing").getString();
									} else {
										theMob = ((new Object() {
											public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
												if (world instanceof ILevelExtension _ext) {
													IItemHandler _itemHandler = _ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
													if (_itemHandler != null)
														return _itemHandler.getStackInSlot(slotid).copy();
												}
												return ItemStack.EMPTY;
											}
										}.getItemStack(world, BlockPos.containing(positionx + new Object() {
											public double getValue(LevelAccessor world, BlockPos pos, String tag) {
												BlockEntity blockEntity = world.getBlockEntity(pos);
												if (blockEntity != null)
													return blockEntity.getPersistentData().getDouble(tag);
												return -1;
											}
										}.getValue(world, new BlockPos(positionx, positiony, positionz), "blockFound"), positiony, positionz), 0)).getDisplayName().getString()).substring((int) ((new Object() {
											public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
												if (world instanceof ILevelExtension _ext) {
													IItemHandler _itemHandler = _ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
													if (_itemHandler != null)
														return _itemHandler.getStackInSlot(slotid).copy();
												}
												return ItemStack.EMPTY;
											}
										}.getItemStack(world, BlockPos.containing(positionx + new Object() {
											public double getValue(LevelAccessor world, BlockPos pos, String tag) {
												BlockEntity blockEntity = world.getBlockEntity(pos);
												if (blockEntity != null)
													return blockEntity.getPersistentData().getDouble(tag);
												return -1;
											}
										}.getValue(world, new BlockPos(positionx, positiony, positionz), "blockFound"), positiony, positionz), 0)).getDisplayName().getString()).indexOf("[") + "[".length(), (int) ((new Object() {
											public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
												if (world instanceof ILevelExtension _ext) {
													IItemHandler _itemHandler = _ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
													if (_itemHandler != null)
														return _itemHandler.getStackInSlot(slotid).copy();
												}
												return ItemStack.EMPTY;
											}
										}.getItemStack(world, BlockPos.containing(positionx + new Object() {
											public double getValue(LevelAccessor world, BlockPos pos, String tag) {
												BlockEntity blockEntity = world.getBlockEntity(pos);
												if (blockEntity != null)
													return blockEntity.getPersistentData().getDouble(tag);
												return -1;
											}
										}.getValue(world, new BlockPos(positionx, positiony, positionz), "blockFound"), positiony, positionz), 0)).getDisplayName().getString()).indexOf("]"));
									}
									renderTexts(theMob, (positionx + 0.249), (positiony + secondLineYLevel), (positionz + 0.5), (float) textRotationEast, 0, 0, (float) 0.003, 255 << 24 | 0 << 16 | 150 << 8 | 255, false);
									renderTexts((Component.translatable("block.simple_incubator.incubation_receiver.operation").getString()), (positionx + 0.249), (positiony + thirdTitleYLevel), (positionz + 0.5), (float) textRotationEast, 0, 0,
											(float) 0.005, 255 << 24 | 255 << 16 | 255 << 8 | 255, false);
									if ((new Object() {
										public boolean getValue(LevelAccessor world, BlockPos pos, String tag) {
											BlockEntity blockEntity = world.getBlockEntity(pos);
											if (blockEntity != null)
												return blockEntity.getPersistentData().getBoolean(tag);
											return false;
										}
									}.getValue(world, new BlockPos(positionx, positiony, positionz), "shouldSpawnParticles")) == true) {
										renderTexts((Component.translatable("block.simple_incubator.incubation_receiver.active").getString()), (positionx + 0.249), (positiony + thirdLineYLevel), (positionz + 0.5), (float) textRotationEast, 0, 0,
												(float) 0.005, 255 << 24 | 50 << 16 | 200 << 8 | 0, false);
									} else {
										renderTexts((Component.translatable("block.simple_incubator.incubation_receiver.deactivated").getString()), (positionx + 0.249), (positiony + thirdLineYLevel), (positionz + 0.5), (float) textRotationEast, 0, 0,
												(float) 0.005, 255 << 24 | 200 << 16 | 0 << 8 | 0, false);
									}
									if ((new Object() {
										public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
											if (world instanceof ILevelExtension _ext) {
												IItemHandler _itemHandler = _ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
												if (_itemHandler != null)
													return _itemHandler.getStackInSlot(slotid).copy();
											}
											return ItemStack.EMPTY;
										}
									}.getItemStack(world, new BlockPos(positionx, positiony, positionz), 0)).getItem() == SimpleIncubatorModItems.RANGE_UPDATE_MODULE_MK_2.get()) {
										renderTexts("Mk2", (positionx + 0.249), (positiony + mkVersion), (positionz + 0.5), (float) textRotationEast, 0, 0, (float) 0.005, 255 << 24 | 255 << 16 | 255 << 8 | 255, false);
									} else if ((new Object() {
										public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
											if (world instanceof ILevelExtension _ext) {
												IItemHandler _itemHandler = _ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
												if (_itemHandler != null)
													return _itemHandler.getStackInSlot(slotid).copy();
											}
											return ItemStack.EMPTY;
										}
									}.getItemStack(world, new BlockPos(positionx, positiony, positionz), 0)).getItem() == SimpleIncubatorModItems.RANGE_UPDATE_MODULE_MK_3.get()) {
										renderTexts("Mk3", (positionx + 0.249), (positiony + mkVersion), (positionz + 0.5), (float) textRotationEast, 0, 0, (float) 0.005, 255 << 24 | 255 << 16 | 255 << 8 | 255, false);
									} else if ((new Object() {
										public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
											if (world instanceof ILevelExtension _ext) {
												IItemHandler _itemHandler = _ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
												if (_itemHandler != null)
													return _itemHandler.getStackInSlot(slotid).copy();
											}
											return ItemStack.EMPTY;
										}
									}.getItemStack(world, new BlockPos(positionx, positiony, positionz), 0)).getItem() == SimpleIncubatorModItems.RANGE_UPDATE_MODULE_MK_4.get()) {
										renderTexts("Mk4", (positionx + 0.249), (positiony + mkVersion), (positionz + 0.5), (float) textRotationEast, 0, 0, (float) 0.005, 255 << 24 | 255 << 16 | 255 << 8 | 255, false);
									}
								} else {
									renderTexts((Component.translatable("block.simple_incubator.incubation_receiver.antena").getString()), (positionx + 0.751), (positiony + firstTitleYLevel), (positionz + 0.5), (float) textRotationWest, 0, 0,
											(float) 0.005, 255 << 24 | 255 << 16 | 255 << 8 | 255, false);
									if ((world.getBlockState(BlockPos.containing(positionx - (new Object() {
										public double getValue(LevelAccessor world, BlockPos pos, String tag) {
											BlockEntity blockEntity = world.getBlockEntity(pos);
											if (blockEntity != null)
												return blockEntity.getPersistentData().getDouble(tag);
											return -1;
										}
									}.getValue(world, new BlockPos(positionx, positiony, positionz), "blockFound")), positiony, positionz))).getBlock() == SimpleIncubatorModBlocks.INCUBATION_TRANSMITTER.get()) {
										renderTexts((Component.translatable("block.simple_incubator.incubation_receiver.connected").getString()), (positionx + 0.751), (positiony + firstLineYLevel), (positionz + 0.5), (float) textRotationWest, 0, 0,
												(float) 0.005, 255 << 24 | 50 << 16 | 200 << 8 | 0, false);
									} else {
										renderTexts((Component.translatable("block.simple_incubator.incubation_receiver.disconnected").getString()), (positionx + 0.751), (positiony + firstLineYLevel), (positionz + 0.5), (float) textRotationWest, 0,
												0, (float) 0.005, 255 << 24 | 200 << 16 | 0 << 8 | 0, false);
									}
									renderTexts((Component.translatable("block.simple_incubator.incubation_receiver.spawning").getString()), (positionx + 0.751), (positiony + secondTitleYLevel), (positionz + 0.5), (float) textRotationWest, 0, 0,
											(float) 0.005, 255 << 24 | 255 << 16 | 255 << 8 | 255, false);
									if (!((new Object() {
										public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
											if (world instanceof ILevelExtension _ext) {
												IItemHandler _itemHandler = _ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
												if (_itemHandler != null)
													return _itemHandler.getStackInSlot(slotid).copy();
											}
											return ItemStack.EMPTY;
										}
									}.getItemStack(world, BlockPos.containing(positionx - (new Object() {
										public double getValue(LevelAccessor world, BlockPos pos, String tag) {
											BlockEntity blockEntity = world.getBlockEntity(pos);
											if (blockEntity != null)
												return blockEntity.getPersistentData().getDouble(tag);
											return -1;
										}
									}.getValue(world, new BlockPos(positionx, positiony, positionz), "blockFound")), positiony, positionz), 0)).is(ItemTags.create(ResourceLocation.parse("simple_incubator:eggs"))))) {
										theMob = Component.translatable("block.simple_incubator.incubation_receiver.nothing").getString();
									} else {
										theMob = ((new Object() {
											public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
												if (world instanceof ILevelExtension _ext) {
													IItemHandler _itemHandler = _ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
													if (_itemHandler != null)
														return _itemHandler.getStackInSlot(slotid).copy();
												}
												return ItemStack.EMPTY;
											}
										}.getItemStack(world, BlockPos.containing(positionx - (new Object() {
											public double getValue(LevelAccessor world, BlockPos pos, String tag) {
												BlockEntity blockEntity = world.getBlockEntity(pos);
												if (blockEntity != null)
													return blockEntity.getPersistentData().getDouble(tag);
												return -1;
											}
										}.getValue(world, new BlockPos(positionx, positiony, positionz), "blockFound")), positiony, positionz), 0)).getDisplayName().getString()).substring((int) ((new Object() {
											public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
												if (world instanceof ILevelExtension _ext) {
													IItemHandler _itemHandler = _ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
													if (_itemHandler != null)
														return _itemHandler.getStackInSlot(slotid).copy();
												}
												return ItemStack.EMPTY;
											}
										}.getItemStack(world, BlockPos.containing(positionx - (new Object() {
											public double getValue(LevelAccessor world, BlockPos pos, String tag) {
												BlockEntity blockEntity = world.getBlockEntity(pos);
												if (blockEntity != null)
													return blockEntity.getPersistentData().getDouble(tag);
												return -1;
											}
										}.getValue(world, new BlockPos(positionx, positiony, positionz), "blockFound")), positiony, positionz), 0)).getDisplayName().getString()).indexOf("[") + "[".length(), (int) ((new Object() {
											public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
												if (world instanceof ILevelExtension _ext) {
													IItemHandler _itemHandler = _ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
													if (_itemHandler != null)
														return _itemHandler.getStackInSlot(slotid).copy();
												}
												return ItemStack.EMPTY;
											}
										}.getItemStack(world, BlockPos.containing(positionx - (new Object() {
											public double getValue(LevelAccessor world, BlockPos pos, String tag) {
												BlockEntity blockEntity = world.getBlockEntity(pos);
												if (blockEntity != null)
													return blockEntity.getPersistentData().getDouble(tag);
												return -1;
											}
										}.getValue(world, new BlockPos(positionx, positiony, positionz), "blockFound")), positiony, positionz), 0)).getDisplayName().getString()).indexOf("]"));
									}
									renderTexts(theMob, (positionx + 0.751), (positiony + secondLineYLevel), (positionz + 0.5), (float) textRotationWest, 0, 0, (float) 0.003, 255 << 24 | 0 << 16 | 150 << 8 | 255, false);
									renderTexts((Component.translatable("block.simple_incubator.incubation_receiver.operation").getString()), (positionx + 0.751), (positiony + thirdTitleYLevel), (positionz + 0.5), (float) textRotationWest, 0, 0,
											(float) 0.005, 255 << 24 | 255 << 16 | 255 << 8 | 255, false);
									if ((new Object() {
										public boolean getValue(LevelAccessor world, BlockPos pos, String tag) {
											BlockEntity blockEntity = world.getBlockEntity(pos);
											if (blockEntity != null)
												return blockEntity.getPersistentData().getBoolean(tag);
											return false;
										}
									}.getValue(world, new BlockPos(positionx, positiony, positionz), "shouldSpawnParticles")) == true) {
										renderTexts((Component.translatable("block.simple_incubator.incubation_receiver.active").getString()), (positionx + 0.751), (positiony + thirdLineYLevel), (positionz + 0.5), (float) textRotationWest, 0, 0,
												(float) 0.005, 255 << 24 | 50 << 16 | 200 << 8 | 0, false);
									} else {
										renderTexts((Component.translatable("block.simple_incubator.incubation_receiver.deactivated").getString()), (positionx + 0.751), (positiony + thirdLineYLevel), (positionz + 0.5), (float) textRotationWest, 0, 0,
												(float) 0.005, 255 << 24 | 200 << 16 | 0 << 8 | 0, false);
									}
									if ((new Object() {
										public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
											if (world instanceof ILevelExtension _ext) {
												IItemHandler _itemHandler = _ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
												if (_itemHandler != null)
													return _itemHandler.getStackInSlot(slotid).copy();
											}
											return ItemStack.EMPTY;
										}
									}.getItemStack(world, new BlockPos(positionx, positiony, positionz), 0)).getItem() == SimpleIncubatorModItems.RANGE_UPDATE_MODULE_MK_2.get()) {
										renderTexts("Mk2", (positionx + 0.751), (positiony + mkVersion), (positionz + 0.5), (float) textRotationWest, 0, 0, (float) 0.005, 255 << 24 | 255 << 16 | 255 << 8 | 255, false);
									} else if ((new Object() {
										public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
											if (world instanceof ILevelExtension _ext) {
												IItemHandler _itemHandler = _ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
												if (_itemHandler != null)
													return _itemHandler.getStackInSlot(slotid).copy();
											}
											return ItemStack.EMPTY;
										}
									}.getItemStack(world, new BlockPos(positionx, positiony, positionz), 0)).getItem() == SimpleIncubatorModItems.RANGE_UPDATE_MODULE_MK_3.get()) {
										renderTexts("Mk3", (positionx + 0.751), (positiony + mkVersion), (positionz + 0.5), (float) textRotationWest, 0, 0, (float) 0.005, 255 << 24 | 255 << 16 | 255 << 8 | 255, false);
									} else if ((new Object() {
										public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
											if (world instanceof ILevelExtension _ext) {
												IItemHandler _itemHandler = _ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
												if (_itemHandler != null)
													return _itemHandler.getStackInSlot(slotid).copy();
											}
											return ItemStack.EMPTY;
										}
									}.getItemStack(world, new BlockPos(positionx, positiony, positionz), 0)).getItem() == SimpleIncubatorModItems.RANGE_UPDATE_MODULE_MK_4.get()) {
										renderTexts("Mk4", (positionx + 0.751), (positiony + mkVersion), (positionz + 0.5), (float) textRotationWest, 0, 0, (float) 0.005, 255 << 24 | 255 << 16 | 255 << 8 | 255, false);
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
