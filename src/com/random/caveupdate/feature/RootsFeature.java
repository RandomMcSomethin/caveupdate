package com.random.caveupdate.feature;

import java.util.Random;

import com.mojang.serialization.Codec;
import com.random.caveupdate.CaveUpdate;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public class RootsFeature extends Feature<DefaultFeatureConfig> {
 
    public RootsFeature(Codec<DefaultFeatureConfig> config) {
        super(config);
    }
 
    @Override
	public boolean generate(ServerWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator generator,
			Random random, BlockPos pos, DefaultFeatureConfig config) {
    	Block blockToReplace = world.getBlockState(pos.up()).getBlock();
    	Block block = world.getBlockState(pos).getBlock();
	        //Roots
	        blockToReplace = world.getBlockState(pos.down()).getBlock();
	        if ((blockToReplace.is(Blocks.CAVE_AIR) || (blockToReplace.is(Blocks.AIR))) && (block.is(Blocks.DIRT) || block.is(Blocks.GRASS))) {
	        	world.setBlockState(pos.down(), CaveUpdate.ROOTS.getDefaultState(), 3);
	        } else {
	            return false;
	        }
        return true;
    }
}