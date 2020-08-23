package com.random.caveupdate.blocks;

import com.random.caveupdate.CaveUpdate;

import net.minecraft.block.BlockState;
import net.minecraft.block.MushroomPlantBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

public class CaveMushroomPlantBlock extends MushroomPlantBlock {

	public CaveMushroomPlantBlock(Settings settings) {
		super(settings);
		// TODO Auto-generated constructor stub
	}
	
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
	      BlockPos blockPos = pos.down();
	      BlockState blockState = world.getBlockState(blockPos);
	      if (!blockState.isOf(CaveUpdate.CAVE_GRASS)) {
	         return false;
	      } else {
	         return true;
	      }
	   }

}
