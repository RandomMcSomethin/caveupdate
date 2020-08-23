package com.random.caveupdate.feature;

import java.util.Random;

import com.mojang.serialization.Codec;

import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public class PebblesFeature extends Feature<DefaultFeatureConfig> {
 
    public PebblesFeature(Codec<DefaultFeatureConfig> config) {
        super(config);
    }
 
    @Override
	public boolean generate(ServerWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator generator,
			Random random, BlockPos pos, DefaultFeatureConfig config) {
    	Block blockToReplace = world.getBlockState(pos.up()).getBlock();
    	Block block = world.getBlockState(pos).getBlock();
	        //Roots
	        if ((blockToReplace.is(Blocks.CAVE_AIR) || (blockToReplace.is(Blocks.AIR))) && (block.is(Blocks.STONE))) {
	        	world.setBlockState(pos.up(), Blocks.STONE_BUTTON.getDefaultState().with(AbstractButtonBlock.FACE, WallMountLocation.FLOOR), 3);
	        } else {
	            return false;
	        }
        return true;
    }
}