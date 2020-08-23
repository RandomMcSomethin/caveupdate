package com.random.caveupdate.feature;

import java.util.Random;

import com.mojang.serialization.Codec;
import com.random.caveupdate.CaveUpdate;

import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.LightType;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.StructureFeature;

public class DioritePlateauFeature extends Feature<DefaultFeatureConfig> {

	public DioritePlateauFeature(Codec<DefaultFeatureConfig> configCodec) {
		super(configCodec);
		// TODO Auto-generated constructor stub
	}

	public boolean generate(ServerWorldAccess serverWorldAccess, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, DefaultFeatureConfig singleStateFeatureConfig) {
	      while(blockPos.getY() > 19 && serverWorldAccess.isAir(blockPos)) {
	         blockPos = blockPos.down();
	      }

	      if (blockPos.getY() <= 18) {
	         return false;
	      } else {
	         blockPos = blockPos.down(4);
	         if (structureAccessor.getStructuresWithChildren(ChunkSectionPos.from(blockPos), StructureFeature.VILLAGE).findAny().isPresent()) {
	            return false;
	         } else {
	        	 //Makes multiple areas in a single feature
	        	 int plateauDepth = 3 + random.nextInt(2);
	        	 Mutable mutPos = blockPos.mutableCopy();
	        	 for (int flatNumber = 0; flatNumber < 32 + random.nextInt(12); flatNumber++) {
		            boolean[] bls = new boolean[2048];
		            int i = random.nextInt(4) + 4;
		            
		            //Variation
		            if (flatNumber != 13) {
			            mutPos.setX(mutPos.getX() - 4 + random.nextInt(9));
			            mutPos.setZ(mutPos.getZ() - 4 + random.nextInt(9));
		            } else {
		            	mutPos.setY(mutPos.getY() - plateauDepth);
		            }
		            int ab;
		            for(ab = 0; ab < i; ++ab) {
		               double d = random.nextDouble() * 8.0D + 3.0D;
		               double e = random.nextDouble() * 12.0D + 2.0D;
		               double f = random.nextDouble() * 8.0D + 3.0D;
		               double g = random.nextDouble() * (16.0D - d - 2.0D) + 1.0D + d / 2.0D;
		               double h = random.nextDouble() * (8.0D - e - 4.0D) + 2.0D + e / 2.0D;
		               double k = random.nextDouble() * (16.0D - f - 2.0D) + 1.0D + f / 2.0D;
	
		               for(int l = 1; l < 15; ++l) {
		                  for(int m = 1; m < 15; ++m) {
		                     for(int n = 1; n < 7; ++n) {
		                        double o = ((double)l - g) / (d / 2.0D);
		                        double p = ((double)n - h) / (e / 2.0D);
		                        double q = ((double)m - k) / (f / 2.0D);
		                        double r = o * o + p * p + q * q;
		                        if (r < 1.0D) {
		                           bls[(l * 16 + m) * 8 + n] = true;
		                        }
		                     }
		                  }
		               }
		            }
	
		            int ad;
		            int ac;
		            boolean bl2;
		            for(ab = 0; ab < 16; ++ab) {
		               for(ac = 0; ac < 16; ++ac) {
		                  for(ad = 0; ad < 8; ++ad) {
		                     bl2 = !bls[(ab * 16 + ac) * 8 + ad] && (ab < 15 && bls[((ab + 1) * 16 + ac) * 8 + ad] || ab > 0 && bls[((ab - 1) * 16 + ac) * 8 + ad] || ac < 15 && bls[(ab * 16 + ac + 1) * 8 + ad] || ac > 0 && bls[(ab * 16 + (ac - 1)) * 8 + ad] || ad < 7 && bls[(ab * 16 + ac) * 8 + ad + 1] || ad > 0 && bls[(ab * 16 + ac) * 8 + (ad - 1)]);
		                     if (bl2) {
		                        Material material = serverWorldAccess.getBlockState(mutPos.add(ab, ad, ac)).getMaterial();
		                        if (ad >= 4 && material.isLiquid()) {
		                           return false;
		                        }
	
		                        if (ad < 4 && !material.isSolid() && serverWorldAccess.getBlockState(mutPos.add(ab, ad, ac)) != Blocks.CAVE_AIR.getDefaultState()) {
		                           return false;
		                        }
		                     }
		                  }
		               }
		            }
	
		            for(ab = 0; ab < 16; ++ab) {
		               for(ac = 0; ac < 16; ++ac) {
		                  for(ad = 0; ad < 8; ++ad) {
		                     if (bls[(ab * 16 + ac) * 8 + ad]) {
		                        serverWorldAccess.setBlockState(mutPos.add(ab, ad, ac), ad >= 4 ? Blocks.CAVE_AIR.getDefaultState() : Blocks.CAVE_AIR.getDefaultState(), 2);
		                     }
		                  }
		               }
		            }
	
		            BlockPos mutPos3;
		            for(ab = 0; ab < 16; ++ab) {
		               for(ac = 0; ac < 16; ++ac) {
		                  for(ad = 4; ad < 8; ++ad) {
		                     if (bls[(ab * 16 + ac) * 8 + ad]) {
		                        mutPos3 = mutPos.add(ab, ad - 1, ac);
		                        if (isSoil(serverWorldAccess.getBlockState(mutPos3).getBlock()) && serverWorldAccess.getLightLevel(LightType.SKY, mutPos.add(ab, ad, ac)) > 0) {
		                           Biome biome = serverWorldAccess.getBiome(mutPos3);
		                           if (biome.getSurfaceConfig().getTopMaterial().isOf(Blocks.MYCELIUM)) {
		                              serverWorldAccess.setBlockState(mutPos3, Blocks.MYCELIUM.getDefaultState(), 2);
		                           } else {
		                              serverWorldAccess.setBlockState(mutPos3, CaveUpdate.CAVE_GRASS.getDefaultState(), 2);
		                           }
		                        }
		                     }
		                  }
		               }
		            }
	
		               for(ab = 0; ab < 16; ++ab) {
		                  for(ac = 0; ac < 16; ++ac) {
		                     for(ad = 0; ad < 8; ++ad) {
		                        bl2 = !bls[(ab * 16 + ac) * 8 + ad] && (ab < 15 && bls[((ab + 1) * 16 + ac) * 8 + ad] || ab > 0 && bls[((ab - 1) * 16 + ac) * 8 + ad] || ac < 15 && bls[(ab * 16 + ac + 1) * 8 + ad] || ac > 0 && bls[(ab * 16 + (ac - 1)) * 8 + ad] || ad < 7 && bls[(ab * 16 + ac) * 8 + ad + 1] || ad > 0 && bls[(ab * 16 + ac) * 8 + (ad - 1)]);
		                        if (bl2 && (ad < 4 || random.nextInt(2) != 0) && serverWorldAccess.getBlockState(mutPos.add(ab, ad, ac)).getBlock() == Blocks.STONE) {
		                           serverWorldAccess.setBlockState(mutPos.add(ab, ad, ac), Blocks.DIORITE.getDefaultState(), 2);
		                        }
		                     }
		                  }
		               }
	        	 	}
	            return true;
	         }
	      }
	   }
}
