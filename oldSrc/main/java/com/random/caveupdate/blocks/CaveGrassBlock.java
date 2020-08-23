package com.random.caveupdate.blocks;

import java.util.Random;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.block.SpreadableBlock;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.light.ChunkLightProvider;

public class CaveGrassBlock extends SpreadableBlock {

	public CaveGrassBlock(Settings settings) {
		super(settings);
		
	}

	private static boolean canSurvive(BlockState state, WorldView worldView, BlockPos pos) {
	      BlockPos blockPos = pos.up();
	      BlockState blockState = worldView.getBlockState(blockPos);
	      if (worldView.isSkyVisible(blockPos)) { return false; }
	      if (blockState.isOf(Blocks.SNOW) && (Integer)blockState.get(SnowBlock.LAYERS) == 1) {
	         return true;
	      } else if (blockState.getFluidState().getLevel() == 8) {
	         return false;
	      } else {
	         int i = ChunkLightProvider.getRealisticOpacity(worldView, state, pos, blockState, blockPos, Direction.UP, blockState.getOpacity(worldView, blockPos));
	         return i < worldView.getMaxLightLevel();
	      }
	   }
	
	private static boolean canSpread(BlockState state, WorldView worldView, BlockPos pos) {
	      BlockPos blockPos = pos.up();
	      return canSurvive(state, worldView, pos) && !worldView.getFluidState(blockPos).isIn(FluidTags.WATER) && !worldView.isSkyVisible(blockPos);
	   }
	
	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
	      if (!canSurvive(state, world, pos)) {
	         world.setBlockState(pos, Blocks.DIRT.getDefaultState());
	      } else {
	         if (!world.isSkyVisible(pos)) {
	            BlockState blockState = this.getDefaultState();

	            for(int i = 0; i < 4; ++i) {
	               BlockPos blockPos = pos.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
	               if (world.getBlockState(blockPos).isOf(Blocks.DIRT) && canSpread(blockState, world, blockPos)) {
	                  world.setBlockState(blockPos, (BlockState)blockState.with(SNOWY, world.getBlockState(blockPos.up()).isOf(Blocks.SNOW)));
	               }
	            }
	         }

	      }
	   }
	
	@Environment(EnvType.CLIENT)
	   public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
	      super.randomDisplayTick(state, world, pos, random);
	      if (random.nextInt(10) == 0) {
	         world.addParticle(ParticleTypes.MYCELIUM, (double)pos.getX() + random.nextDouble(), (double)pos.getY() + 1.1D, (double)pos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
	      }

	   }
}
