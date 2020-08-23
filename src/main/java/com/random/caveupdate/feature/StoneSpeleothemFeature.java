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

public class StoneSpeleothemFeature extends Feature<DefaultFeatureConfig> {
 
    public StoneSpeleothemFeature(Codec<DefaultFeatureConfig> config) {
        super(config);
    }
 
    @Override
	public boolean generate(ServerWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator generator,
			Random random, BlockPos pos, DefaultFeatureConfig config) {
    	Block blockToReplace = world.getBlockState(pos.up()).getBlock();
    	//Block block = world.getBlockState(pos).getBlock();
    	//Stalagmites
        if (blockToReplace.is(Blocks.CAVE_AIR) && this.getSpeleothemType(world, pos) != Blocks.CAVE_AIR) {
        	world.setBlockState(pos.up(), this.getSpeleothemType(world, pos).getDefaultState(), 3);
        	if (random.nextInt(5) == 0) {
        		world.setBlockState(pos.up(2), this.getSpeleothemType(world, pos).getDefaultState(), 3);
        	}
        } else {
	        //Stalagtites
	        blockToReplace = world.getBlockState(pos.down()).getBlock();
	        if (blockToReplace.is(Blocks.CAVE_AIR) && this.getSpeleothemType(world, pos) != Blocks.CAVE_AIR) {
	        	world.setBlockState(pos.down(), this.getSpeleothemType(world, pos).getDefaultState(), 3);
	        	if (random.nextInt(5) == 0) {
	        		world.setBlockState(pos.down(2), this.getSpeleothemType(world, pos).getDefaultState(), 3);
	        	}
	        } else {
	            return false;
	        }
        }
        return true;
    }
    
    public Block getSpeleothemType(ServerWorldAccess world, BlockPos pos) {
    	Block spel = Blocks.CAVE_AIR;
    	Block block = world.getBlockState(pos).getBlock();
    	//Stone
    	if ((block.is(Blocks.STONE) || block.is(CaveUpdate.NATURAL_STONE_WALL))) {
    		spel = CaveUpdate.NATURAL_STONE_WALL;
    	}
    	//Granite
    	if ((block.is(Blocks.GRANITE) || block.is(Blocks.GRANITE_WALL))) {
    		spel = Blocks.GRANITE_WALL;
    	}
    	//Diorite
    	if ((block.is(Blocks.DIORITE) || block.is(Blocks.DIORITE_WALL))) {
    		spel = Blocks.DIORITE_WALL;
    	}
    	//Andesite
    	if ((block.is(Blocks.ANDESITE) || block.is(Blocks.ANDESITE_WALL))) {
    		spel = Blocks.ANDESITE_WALL;
    	}
    	return spel;
    }
}