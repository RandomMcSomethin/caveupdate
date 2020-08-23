package com.random.caveupdate.feature;

import java.util.Random;

import com.mojang.serialization.Codec;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.loot.LootTables;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public class CaveChestFeature extends Feature<DefaultFeatureConfig> {
 
    public CaveChestFeature(Codec<DefaultFeatureConfig> config) {
        super(config);
    }
 
    @Override
	public boolean generate(ServerWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator generator,
			Random random, BlockPos pos, DefaultFeatureConfig config) {
    	Block blockToReplace = world.getBlockState(pos.up()).getBlock();
    	//Block block = world.getBlockState(pos).getBlock();
    	//Stalagmites
        if (blockToReplace.is(Blocks.CAVE_AIR) && world.getBlockState(pos).isOpaqueFullCube(world, pos)) {
        	world.setBlockState(pos.up(), Blocks.CHEST.getDefaultState(), 3);
        	LootableContainerBlockEntity.setLootTable(world, random, pos.up(), LootTables.VILLAGE_PLAINS_CHEST);
        } else { return false; }
        return true;
    }

}