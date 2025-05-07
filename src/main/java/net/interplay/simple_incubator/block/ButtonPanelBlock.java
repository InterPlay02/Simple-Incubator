
package net.interplay.simple_incubator.block;

import org.checkerframework.checker.units.qual.s;

import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.InteractionResult;
import net.minecraft.util.RandomSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import net.interplay.simple_incubator.procedures.ButtonPanelRightClickedProcedure;
import net.interplay.simple_incubator.procedures.ButtonPanelRedstoneLevelProcedure;
import net.interplay.simple_incubator.procedures.ButtonPanelOnTickUpdateProcedure;
import net.interplay.simple_incubator.block.entity.ButtonPanelBlockEntity;

public class ButtonPanelBlock extends Block implements EntityBlock {
	public static final IntegerProperty BLOCKSTATE = IntegerProperty.create("blockstate", 0, 1);
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

	public ButtonPanelBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(0.5f, 5f).lightLevel(s -> (new Object() {
			public int getLightLevel() {
				if (s.getValue(BLOCKSTATE) == 1)
					return 0;
				return 0;
			}
		}.getLightLevel())).requiresCorrectToolForDrops().noOcclusion().pushReaction(PushReaction.BLOCK).isRedstoneConductor((bs, br, bp) -> false));
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
		return true;
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 0;
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		if (state.getValue(BLOCKSTATE) == 1) {
			return switch (state.getValue(FACING)) {
				default -> Shapes.or(box(3, 2.49477, 3.80168, 13, 3.49477, 9.60168), box(1, -5.9, 1.6, 15, -0.5, 15.4), box(9, 1.09477, 10.10168, 13, 1.39477, 12.90168));
				case NORTH -> Shapes.or(box(3, 2.49477, 6.39832, 13, 3.49477, 12.19832), box(1, -5.9, 0.6, 15, -0.5, 14.4), box(3, 1.09477, 3.09832, 7, 1.39477, 5.89832));
				case EAST -> Shapes.or(box(3.80168, 2.49477, 3, 9.60168, 3.49477, 13), box(1.6, -5.9, 1, 15.4, -0.5, 15), box(10.10168, 1.09477, 3, 12.90168, 1.39477, 7));
				case WEST -> Shapes.or(box(6.39832, 2.49477, 3, 12.19832, 3.49477, 13), box(0.6, -5.9, 1, 14.4, -0.5, 15), box(3.09832, 1.09477, 9, 5.89832, 1.39477, 13));
			};
		}
		return switch (state.getValue(FACING)) {
			default -> Shapes.or(box(3, 2.49477, 3.80168, 13, 3.49477, 9.60168), box(1, -5.9, 1.6, 15, -0.5, 15.4), box(9, 1.09477, 10.10168, 13, 2.09477, 12.90168));
			case NORTH -> Shapes.or(box(3, 2.49477, 6.39832, 13, 3.49477, 12.19832), box(1, -5.9, 0.6, 15, -0.5, 14.4), box(3, 1.09477, 3.09832, 7, 2.09477, 5.89832));
			case EAST -> Shapes.or(box(3.80168, 2.49477, 3, 9.60168, 3.49477, 13), box(1.6, -5.9, 1, 15.4, -0.5, 15), box(10.10168, 1.09477, 3, 12.90168, 2.09477, 7));
			case WEST -> Shapes.or(box(6.39832, 2.49477, 3, 12.19832, 3.49477, 13), box(0.6, -5.9, 1, 14.4, -0.5, 15), box(3.09832, 1.09477, 9, 5.89832, 2.09477, 13));
		};
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, BLOCKSTATE);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context).setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}

	@Override
	public boolean isSignalSource(BlockState state) {
		return true;
	}

	@Override
	public int getSignal(BlockState blockstate, BlockGetter blockAccess, BlockPos pos, Direction direction) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		Level world = (Level) blockAccess;
		return (int) ButtonPanelRedstoneLevelProcedure.execute(world);
	}

	@Override
	public void onPlace(BlockState blockstate, Level world, BlockPos pos, BlockState oldState, boolean moving) {
		super.onPlace(blockstate, world, pos, oldState, moving);
		world.scheduleTick(pos, this, 1);
	}

	@Override
	public void tick(BlockState blockstate, ServerLevel world, BlockPos pos, RandomSource random) {
		super.tick(blockstate, world, pos, random);
		ButtonPanelOnTickUpdateProcedure.execute(world, pos.getX(), pos.getY(), pos.getZ());
		world.scheduleTick(pos, this, 1);
	}

	@Override
	public InteractionResult useWithoutItem(BlockState blockstate, Level world, BlockPos pos, Player entity, BlockHitResult hit) {
		super.useWithoutItem(blockstate, world, pos, entity, hit);
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		double hitX = hit.getLocation().x;
		double hitY = hit.getLocation().y;
		double hitZ = hit.getLocation().z;
		Direction direction = hit.getDirection();
		ButtonPanelRightClickedProcedure.execute(world, x, y, z, blockstate, hitX, hitY, hitZ);
		return InteractionResult.SUCCESS;
	}

	@Override
	public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos) {
		BlockEntity tileEntity = worldIn.getBlockEntity(pos);
		return tileEntity instanceof MenuProvider menuProvider ? menuProvider : null;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ButtonPanelBlockEntity(pos, state);
	}

	@Override
	public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int eventID, int eventParam) {
		super.triggerEvent(state, world, pos, eventID, eventParam);
		BlockEntity blockEntity = world.getBlockEntity(pos);
		return blockEntity == null ? false : blockEntity.triggerEvent(eventID, eventParam);
	}
}
