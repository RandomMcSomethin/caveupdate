package com.random.caveupdate.blocks;

import java.util.Random;

import com.random.caveupdate.CaveUpdate;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractPlantStemBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.VineLogic;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;

public class RootsBlock extends AbstractPlantStemBlock {
	   protected static final VoxelShape SHAPE = Block.createCuboidShape(4.0D, 9.0D, 4.0D, 12.0D, 16.0D, 12.0D);

	   public RootsBlock(AbstractBlock.Settings settings) {
	      super(settings, Direction.DOWN, SHAPE, false, 0.0D);
	   }

	   public int method_26376(Random random) {
	      return VineLogic.method_26381(random);
	   }

	   public Block getPlant() {
	      return CaveUpdate.ROOTS;
	   }

	   public boolean chooseStemState(BlockState state) {
	      return VineLogic.isValidForWeepingStem(state);
	   }
	}

