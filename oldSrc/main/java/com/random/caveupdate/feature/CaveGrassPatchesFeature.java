package com.random.caveupdate.feature;

import java.util.BitSet;
import java.util.Random;

import com.mojang.serialization.Codec;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

public class CaveGrassPatchesFeature extends OreFeature {

	public CaveGrassPatchesFeature(Codec<OreFeatureConfig> configCodec) {
		super(configCodec);
	}
	
	@Override
	protected boolean generateVeinPart(WorldAccess world, Random random, OreFeatureConfig config, double startX, double endX, double startZ, double endZ, double startY, double endY, int x, int y, int z, int size, int i) {
	      int j = 0;
	      BitSet bitSet = new BitSet(size * i * size);
	      BlockPos.Mutable mutable = new BlockPos.Mutable();
	      double[] ds = new double[config.size * 4];

	      int m;
	      double o;
	      double p;
	      double q;
	      double r;
	      for(m = 0; m < config.size; ++m) {
	         float f = (float)m / (float)config.size;
	         o = MathHelper.lerp((double)f, startX, endX);
	         p = MathHelper.lerp((double)f, startY, endY);
	         q = MathHelper.lerp((double)f, startZ, endZ);
	         r = random.nextDouble() * (double)config.size / 16.0D;
	         double l = ((double)(MathHelper.sin(3.1415927F * f) + 1.0F) * r + 1.0D) / 2.0D;
	         ds[m * 4 + 0] = o;
	         ds[m * 4 + 1] = p;
	         ds[m * 4 + 2] = q;
	         ds[m * 4 + 3] = l;
	      }

	      for(m = 0; m < config.size - 1; ++m) {
	         if (ds[m * 4 + 3] > 0.0D) {
	            for(int n = m + 1; n < config.size; ++n) {
	               if (ds[n * 4 + 3] > 0.0D) {
	                  o = ds[m * 4 + 0] - ds[n * 4 + 0];
	                  p = ds[m * 4 + 1] - ds[n * 4 + 1];
	                  q = ds[m * 4 + 2] - ds[n * 4 + 2];
	                  r = ds[m * 4 + 3] - ds[n * 4 + 3];
	                  if (r * r > o * o + p * p + q * q) {
	                     if (r > 0.0D) {
	                        ds[n * 4 + 3] = -1.0D;
	                     } else {
	                        ds[m * 4 + 3] = -1.0D;
	                     }
	                  }
	               }
	            }
	         }
	      }

	      for(m = 0; m < config.size; ++m) {
	         double t = ds[m * 4 + 3];
	         if (t >= 0.0D) {
	            double u = ds[m * 4 + 0];
	            double v = ds[m * 4 + 1];
	            double w = ds[m * 4 + 2];
	            int aa = Math.max(MathHelper.floor(u - t), x);
	            int ab = Math.max(MathHelper.floor(v - t), y);
	            int ac = Math.max(MathHelper.floor(w - t), z);
	            int ad = Math.max(MathHelper.floor(u + t), aa);
	            int ae = Math.max(MathHelper.floor(v + t), ab);
	            int af = Math.max(MathHelper.floor(w + t), ac);

	            for(int ag = aa; ag <= ad; ++ag) {
	               double ah = ((double)ag + 0.5D - u) / t;
	               if (ah * ah < 1.0D) {
	                  for(int ai = ab; ai <= ae; ++ai) {
	                     double aj = ((double)ai + 0.5D - v) / t;
	                     if (ah * ah + aj * aj < 1.0D) {
	                        for(int ak = ac; ak <= af; ++ak) {
	                           double al = ((double)ak + 0.5D - w) / t;
	                           if (ah * ah + aj * aj + al * al < 1.0D) {
	                              int am = ag - x + (ai - y) * size + (ak - z) * size * i;
	                              if (!bitSet.get(am)) {
	                                 bitSet.set(am);
	                                 mutable.set(ag, ai, ak);
	                                 BlockPos up = mutable.up();
	                                 if ((world.getBlockState(mutable).getBlock().is(Blocks.DIRT) || world.getBlockState(mutable).getBlock().is(Blocks.STONE)) && world.getBlockState(up).getBlock().is(Blocks.CAVE_AIR)) {
	                                    world.setBlockState(mutable, config.state, 2);
	                                    ++j;
	                                 }
	                              }
	                           }
	                        }
	                     }
	                  }
	               }
	            }
	         }
	      }

	      return j > 0;
	   }
}
