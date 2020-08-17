package com.random.caveupdate.blocks;

import java.util.Random;

import com.random.caveupdate.CaveUpdate;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemConvertible;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class DimpleCapsBlock extends CaveCropBlock {
	public static final IntProperty AGE = Properties.AGE_3;;
	private static final VoxelShape[] AGE_TO_SHAPE = new VoxelShape[]{Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D), Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D), Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D)};;

	   public DimpleCapsBlock(AbstractBlock.Settings settings) {
	      super(settings);
	   }

	   @Environment(EnvType.CLIENT)
	   public ItemConvertible getSeedsItem() {
	      return CaveUpdate.DIMPLE_CAPS.asItem();
	   }

	   @Override
	   public IntProperty getAgeProperty() {
		      return AGE;
		   }
	   
	   @Override
	public int getMaxAge() {
		   return 3;
		   }

	   @Override
		   public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		      if (random.nextInt(3) != 0) {
		         super.randomTick(state, world, pos, random);
		      }

		   }

	   @Override
		   public int getGrowthAmount(World world) {
		      return super.getGrowthAmount(world) / 3;
		   }

	   @Override
		   public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		      builder.add(AGE);
		   }

	   @Override
		   public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		      return AGE_TO_SHAPE[(Integer)state.get(this.getAgeProperty())];
		   }
}
