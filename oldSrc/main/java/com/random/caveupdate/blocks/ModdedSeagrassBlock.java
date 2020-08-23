package com.random.caveupdate.blocks;

import java.util.Random;

import com.random.caveupdate.CaveUpdate;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SeagrassBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class ModdedSeagrassBlock extends SeagrassBlock {

	public ModdedSeagrassBlock(Settings settings) {
		super(settings);
	}
	
	public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
	      return true;
	   }

	   public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
	      return true;
	   }
	
	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
	      BlockState blockState = CaveUpdate.TALL_GLOWING_GRASS.getDefaultState();
	      BlockState blockState2 = (BlockState)blockState.with(ModdedTallSeagrassBlock.HALF, DoubleBlockHalf.UPPER);
	      BlockPos blockPos = pos.up();
	      if (world.getBlockState(blockPos).isOf(Blocks.WATER)) {
	         world.setBlockState(pos, blockState, 2);
	         world.setBlockState(blockPos, blockState2, 2);
	      }

	   }

}
