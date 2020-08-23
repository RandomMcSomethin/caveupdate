package com.random.caveupdate.helper;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class ModPredicates {
	//Always remember: God made you special, and He loves you very much.  
		/**
	    * A shortcut to always return {@code true} a context predicate, used as
	    * {@code settings.solidBlock(Blocks::always)}.
	    */
	   public static boolean always(BlockState state, BlockView world, BlockPos pos) {
	      return true;
	   }
}
