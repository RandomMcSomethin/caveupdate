package com.random.caveupdate.feature;

import java.util.Random;

import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.VineBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public class CaveVinesFeature extends Feature<DefaultFeatureConfig> {
	private static final Direction[] DIRECTIONS = Direction.values();
	
	public CaveVinesFeature(Codec<DefaultFeatureConfig> configCodec) {
		super(configCodec);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean generate(ServerWorldAccess serverWorldAccess, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, DefaultFeatureConfig defaultFeatureConfig) {
	      BlockPos.Mutable mutable = blockPos.mutableCopy();
	      if (blockPos.getY() < 32) { return false; }
	      for(int i = blockPos.getY(); i < 256; ++i) {
	         mutable.set(blockPos);
	         mutable.move(random.nextInt(4) - random.nextInt(4), 0, random.nextInt(4) - random.nextInt(4));
	         mutable.setY(i);
	         if (serverWorldAccess.isAir(mutable)) {
	        	 //System.out.println("Found air");
	            Direction[] var9 = DIRECTIONS;
	            int var10 = var9.length;

	            for(int var11 = 0; var11 < var10; ++var11) {
	               Direction direction = var9[var11];
	               //System.out.println("Checking for direction " + direction.asString());
	               if (serverWorldAccess.isAir(mutable) && !serverWorldAccess.isSkyVisible(mutable) && direction != Direction.UP && serverWorldAccess.getBlockState(mutable.offset(direction.getOpposite())).isSolidBlock(serverWorldAccess, mutable.offset(direction.getOpposite()))) {
	            	   //System.out.println("Placed a vine facing " + direction.asString());
	                  serverWorldAccess.setBlockState(mutable, (BlockState)Blocks.VINE.getDefaultState().with(VineBlock.getFacingProperty(direction.getOpposite()), true), 2);
	                  break;
	               }
	            }
	         }
	      }
		return true;
	}

}
