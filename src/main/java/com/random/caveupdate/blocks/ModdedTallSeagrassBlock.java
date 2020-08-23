package com.random.caveupdate.blocks;

import com.random.caveupdate.CaveUpdate;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.TallSeagrassBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class ModdedTallSeagrassBlock extends TallSeagrassBlock {

	public ModdedTallSeagrassBlock(Settings settings) {
		super(settings);
	}
	
	@Environment(EnvType.CLIENT)
	   public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
	      return new ItemStack(CaveUpdate.GLOWING_GRASS);
	   }

}
