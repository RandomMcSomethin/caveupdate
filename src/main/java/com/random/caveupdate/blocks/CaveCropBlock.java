package com.random.caveupdate.blocks;

import java.util.Random;

import com.random.caveupdate.CaveUpdate;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

public class CaveCropBlock extends CropBlock {

	public CaveCropBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
	      return floor.isOf(CaveUpdate.CAVE_FARMLAND);
	   }
	
	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
	      if (!world.isSkyVisible(pos)) {
	         int i = this.getAge(state);
	         if (i < this.getMaxAge()) {
	            float f = getAvailableMoisture(this, world, pos);
	            if (random.nextInt((int)(25.0F / f) + 1) == 0) {
	               world.setBlockState(pos, this.withAge(i + 1), 2);
	            }
	         }
	      } else {
	    	  world.breakBlock(pos, true);
	      }

	   }
	
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		BlockPos blockPos = pos.down();
	      return this.canPlantOnTop(world.getBlockState(blockPos), world, blockPos) && !world.isSkyVisible(pos);
	   }
	
	protected static float getAvailableMoisture(Block block, BlockView world, BlockPos pos) {
	      float f = 1.0F;
	      BlockPos blockPos = pos.down();

	      for(int i = -1; i <= 1; ++i) {
	         for(int j = -1; j <= 1; ++j) {
	            float g = 0.0F;
	            BlockState blockState = world.getBlockState(blockPos.add(i, 0, j));
	            if (blockState.isOf(CaveUpdate.CAVE_FARMLAND)) {
	               g = 1.0F;
	               if ((Integer)blockState.get(CaveFarmlandBlock.MOISTURE) > 0) {
	                  g = 3.0F;
	               }
	            }

	            if (i != 0 || j != 0) {
	               g /= 4.0F;
	            }

	            f += g;
	         }
	      }

	      BlockPos blockPos2 = pos.north();
	      BlockPos blockPos3 = pos.south();
	      BlockPos blockPos4 = pos.west();
	      BlockPos blockPos5 = pos.east();
	      boolean bl = block == world.getBlockState(blockPos4).getBlock() || block == world.getBlockState(blockPos5).getBlock();
	      boolean bl2 = block == world.getBlockState(blockPos2).getBlock() || block == world.getBlockState(blockPos3).getBlock();
	      if (bl && bl2) {
	         f /= 2.0F;
	      } else {
	         boolean bl3 = block == world.getBlockState(blockPos4.north()).getBlock() || block == world.getBlockState(blockPos5.north()).getBlock() || block == world.getBlockState(blockPos5.south()).getBlock() || block == world.getBlockState(blockPos4.south()).getBlock();
	         if (bl3) {
	            f /= 2.0F;
	         }
	      }

	      return f;
	   }

}
