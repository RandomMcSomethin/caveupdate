package com.random.caveupdate.blocks;

import java.util.Iterator;
import java.util.Random;

import com.random.caveupdate.CaveUpdate;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class CaveReedsBlock extends Block implements Fertilizable {
	   public static final IntProperty AGE;
	   protected static final VoxelShape SHAPE;

	   public CaveReedsBlock(AbstractBlock.Settings settings) {
	      super(settings);
	      this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(AGE, 0));
	   }

	   public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
	      return SHAPE;
	   }

	   public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
	      if (!state.canPlaceAt(world, pos)) {
	         world.breakBlock(pos, true);
	      }

	   }
	   
	   @Override
	   public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		   super.onPlaced(world, pos, state, placer, itemStack);
		   if (!state.canPlaceAt(world, pos)) {
		         world.removeBlock(pos, true);
		      }
	   }

	   //** Check height **
	   public int checkHeight(ServerWorld world, BlockPos pos) {
		   if (world.isAir(pos.up())) {
			   int i;
			   for(i = 1; world.getBlockState(pos.down(i)).isOf(this); ++i) {}
			   return i;
		   } else { return 0; }
	   }
	   
	   public int checkHeight(World world, BlockPos pos) {
		   if (world.isAir(pos.up())) {
			   int i;
			   for(i = 1; world.getBlockState(pos.down(i)).isOf(this); ++i) {}
			   return i;
		   } else { return 0; }
	   }
	   
	 //** Check planted block **
	   @SuppressWarnings("unused")
	public Block plantedOn(ServerWorld world, BlockPos pos) {
		   Mutable checkPos = pos.mutableCopy();
		   if (world.getBlockState(pos.down()) == CaveUpdate.CAVE_REEDS.getDefaultState()) {
			   int i;
			   for(i = 1; world.getBlockState(checkPos.down()).isOf(this); ++i) {
				   checkPos.move(Direction.DOWN);
			   }
		   } return world.getBlockState(checkPos.down()).getBlock();
	   }
	   
	   @SuppressWarnings("unused")
	public Block plantedOn(World world, BlockPos pos) {
		   Mutable checkPos = pos.mutableCopy();
		   if (world.getBlockState(pos.down()) == CaveUpdate.CAVE_REEDS.getDefaultState()) {
			   int i;
			   for(i = 1; world.getBlockState(checkPos.down()).isOf(this); ++i) {
				   checkPos.move(Direction.DOWN);
			   }
		   } return world.getBlockState(checkPos.down()).getBlock();
	   }
	   
	   public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
	      if (world.isAir(pos.up())) {
	         int i = this.checkHeight(world, pos);
	         if (i < 3) {
	            int j = (Integer)state.get(AGE);
	            int growthRate = 10;
	            if (this.plantedOn(world, pos) == CaveUpdate.CAVE_GRASS) {
	            	growthRate = 7;
	            }
	            if (j == growthRate) {
	               world.setBlockState(pos.up(), this.getDefaultState());
	               world.setBlockState(pos, (BlockState)state.with(AGE, 0), 4);
	            } else {
	               world.setBlockState(pos, (BlockState)state.with(AGE, j + 1), 4);
	            }
	         }
	      }

	   }
	   
	   @Environment(EnvType.CLIENT)
	   public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
	      super.randomDisplayTick(state, world, pos, random);
	      if (random.nextInt(10) == 0 && this.plantedOn(world, pos) == CaveUpdate.CAVE_GRASS) {
	         world.addParticle(ParticleTypes.CRIMSON_SPORE, (double)pos.getX() + random.nextDouble(), (double)pos.getY() + + random.nextDouble(), (double)pos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
	      }

	   }

	   public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
	      if (!state.canPlaceAt(world, pos)) {
	         world.getBlockTickScheduler().schedule(pos, this, 1);
	      }

	      return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
	   }

	   public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
	      BlockState blockState = world.getBlockState(pos.down());
	      if (blockState.getBlock() == this) {
	         return true;
	      } else {
	         if (blockState.isOf(CaveUpdate.CAVE_GRASS) || blockState.isOf(Blocks.DIRT) || blockState.isOf(Blocks.COARSE_DIRT) || blockState.isOf(Blocks.PODZOL)
	        		 || blockState.isOf(Blocks.STONE) || blockState.isOf(Blocks.GRANITE) || blockState.isOf(Blocks.DIORITE) || blockState.isOf(Blocks.ANDESITE)) {
	            BlockPos blockPos = pos.down();
	            Iterator<Direction> var6 = Direction.Type.HORIZONTAL.iterator();

	            while(var6.hasNext()) {
	               Direction direction = (Direction)var6.next();
	               //BlockState blockState2 = world.getBlockState(blockPos.offset(direction));
	               FluidState fluidState = world.getFluidState(blockPos.offset(direction));
	               if (fluidState.isIn(FluidTags.LAVA) && !world.isSkyVisible(blockPos)) {
	                  return true;
	               }
	            }
	         }

	         return false;
	      }
	   }

	   protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
	      builder.add(AGE);
	   }

	   static {
	      AGE = Properties.AGE_15;
	      SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	   }

	@Override
	public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
		return true;
	}

	@Override
	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
		return this.canPlaceAt(state, world, pos) && this.checkHeight(world, pos) < 3;
	}

	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		this.randomTick(state, world, pos, random);
	}
}
