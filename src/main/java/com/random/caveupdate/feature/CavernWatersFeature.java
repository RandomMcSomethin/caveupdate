package com.random.caveupdate.feature;

import java.util.Random;

import com.mojang.serialization.Codec;
import com.random.caveupdate.CaveUpdate;
import com.random.caveupdate.blocks.ModdedTallSeagrassBlock;

import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.TallSeagrassBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
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

public class CavernWatersFeature extends Feature<DefaultFeatureConfig> {

	public CavernWatersFeature(Codec<DefaultFeatureConfig> configCodec) {
		super(configCodec);
		// TODO Auto-generated constructor stub
	}

	public boolean generate(ServerWorldAccess serverWorldAccess, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, DefaultFeatureConfig singleStateFeatureConfig) {
	      while(blockPos.getY() > 19 && serverWorldAccess.isAir(blockPos)) {
	         blockPos = blockPos.down();
	      }

	      if (blockPos.getY() <= 18 || blockPos.getY() > 48) {
	         return false;
	      } else {
	         blockPos = blockPos.down(4);
	         if (structureAccessor.getStructuresWithChildren(ChunkSectionPos.from(blockPos), StructureFeature.VILLAGE).findAny().isPresent()) {
	            return false;
	         } else {
	        	 //Makes multiple areas in a single flat
	        	 Mutable mutPos = blockPos.mutableCopy();
	        	 for (int flatNumber = 0; flatNumber < 48 + random.nextInt(6); flatNumber++) {
		            boolean[] bls = new boolean[2048];
		            int i = random.nextInt(4) + 4;
		            for (int loong = 0; loong < 2; loong++) {
			            //Variation
			            mutPos.setX(mutPos.getX() - 3 + random.nextInt(7));
			            if (random.nextInt(10) == 0) {
			            	mutPos.setY(mutPos.getY() - 1 + random.nextInt(2));
			            }
			            mutPos.setZ(mutPos.getZ() - 3 + random.nextInt(7));
			            
			            Mutable vertPos = new Mutable(mutPos.getX(), mutPos.getY() + 6*loong, mutPos.getZ());
			            
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
			                        Material material = serverWorldAccess.getBlockState(vertPos.add(ab, ad, ac)).getMaterial();
			                        if (ad >= 4 && material.isLiquid()) {
			                           return false;
			                        }
		
			                        if (ad < 4 && !material.isSolid() && serverWorldAccess.getBlockState(vertPos.add(ab, ad, ac)) != Blocks.WATER.getDefaultState()) {
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
			                        serverWorldAccess.setBlockState(vertPos.add(ab, ad, ac), ad >= 4 ? Blocks.WATER.getDefaultState() : Blocks.WATER.getDefaultState(), 2);
			                     }
			                  }
			               }
			            }
		
			            BlockPos vertPos3;
			            for(ab = 0; ab < 16; ++ab) {
			               for(ac = 0; ac < 16; ++ac) {
			                  for(ad = 4; ad < 8; ++ad) {
			                     if (bls[(ab * 16 + ac) * 8 + ad]) {
			                        vertPos3 = vertPos.add(ab, ad - 1, ac);
			                        if (isSoil(serverWorldAccess.getBlockState(vertPos3).getBlock()) && serverWorldAccess.getLightLevel(LightType.SKY, vertPos.add(ab, ad, ac)) > 0) {
			                           Biome biome = serverWorldAccess.getBiome(vertPos3);
			                           if (biome.getSurfaceConfig().getTopMaterial().isOf(Blocks.MYCELIUM)) {
			                              serverWorldAccess.setBlockState(vertPos3, Blocks.MYCELIUM.getDefaultState(), 2);
			                           } else {
			                              serverWorldAccess.setBlockState(vertPos3, CaveUpdate.CAVE_GRASS.getDefaultState(), 2);
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
			                        if (bl2 && (ad < 4 || random.nextInt(2) != 0) && serverWorldAccess.getBlockState(vertPos.add(ab, ad, ac)).getBlock() == Blocks.STONE) {
			                           if (vertPos.add(ab, ad, ac).getY() <= 18 && random.nextInt(6) == 0) {
			                        	   serverWorldAccess.setBlockState(vertPos.add(ab, ad, ac), Blocks.MAGMA_BLOCK.getDefaultState(), 2);
			                           } else if (vertPos.add(ab, ad, ac).getY() <= 18 && random.nextInt(6) == 0) {
			                        	   serverWorldAccess.setBlockState(vertPos.add(ab, ad, ac), Blocks.SAND.getDefaultState(), 3);
			                        	   serverWorldAccess.setBlockState(vertPos.add(ab, ad, ac).up(), CaveUpdate.GLOWING_GRASS.getDefaultState(), 3);
			                           } 
			                           else if (vertPos.add(ab, ad, ac).getY() <= 18 && random.nextInt(7) == 0) {
			                        	   serverWorldAccess.setBlockState(vertPos.add(ab, ad, ac), Blocks.SAND.getDefaultState(), 3);
			                        	   serverWorldAccess.setBlockState(vertPos.add(ab, ad, ac).up(), CaveUpdate.TALL_GLOWING_GRASS.getDefaultState(), 3);
			                        	   serverWorldAccess.setBlockState(vertPos.add(ab, ad, ac).up(2), CaveUpdate.TALL_GLOWING_GRASS.getDefaultState().with(ModdedTallSeagrassBlock.HALF, DoubleBlockHalf.UPPER), 3);
			                           } 
			                           else if (random.nextInt(64) == 0) {
			                        	   serverWorldAccess.setBlockState(vertPos.add(ab, ad, ac).up(), CaveUpdate.AQUAMARINE_ORE.getDefaultState(), 3);
			                           } else {
			                        	serverWorldAccess.setBlockState(vertPos.add(ab, ad, ac), Blocks.STONE.getDefaultState(), 2);
			                           }
			                        }
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
