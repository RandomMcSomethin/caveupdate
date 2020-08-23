package com.random.caveupdate.mixin;

import java.util.BitSet;
import java.util.Random;
import java.util.function.Function;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.carver.CaveCarver;

@Mixin(CaveCarver.class)
public class VerticalBiomesMixin {
	
	@Shadow
	protected void carveCave(Chunk chunk, Function<BlockPos, Biome> function, long l, int i, int l2, int m, double d, double e, double f, float t, double g, BitSet bitSet) {}
	
	@Shadow
	protected void carveTunnels(Chunk chunk, Function<BlockPos, Biome> function, long l, int i, int l2, int m, double d, double e, double f, float u, float s, float t, int j, int v, double g, BitSet bitSet) {}
	
	public boolean carve(Chunk chunk, Function<BlockPos, Biome> function, Random random, int i, int j, int k, int l, int m, BitSet bitSet, ProbabilityConfig probabilityConfig) {
	      int n = (8 * 2 - 1) * 16; //Branch factor: 4.  Find a better way to do this.  
	      int o = random.nextInt(random.nextInt(random.nextInt(this.getMaxCaveCount()) + 1) + 1);
	      if (!this.isBeneathSea(random)) {
	    	  o = random.nextInt(random.nextInt(random.nextInt(this.getMaxCaveCount()/8) + 1) + 1);
	      }

	      for(int p = 0; p < o; ++p) {
	         double d = (double)(j * 16 + random.nextInt(16));
	         double e = (double)this.getCaveY(random);
	         double f = (double)(k * 16 + random.nextInt(16));
	         int q = 1;
	         float t;
	         if (random.nextInt(2) == 0) {
	            //double g = 0.5D;
	            t = 1.0F + random.nextFloat() * 6.0F;
	            this.carveCave(chunk, function, random.nextLong(), i, l, m, d, e, f, t, 0.5D, bitSet);
	            q += 2 + random.nextInt(4);
	         }

	         for(int r = 0; r < q; ++r) {
	            float s = random.nextFloat() * 6.2831855F;
	            //t = (random.nextFloat() - 0.5F) / 4.0F;
	            t = random.nextFloat() * 6.2831855F;
	            float u = this.getTunnelSystemWidth(random);
	            int v = n - random.nextInt(n / 4);
	            //int w = 0;
	            this.carveTunnels(chunk, function, random.nextLong(), i, l, m, d, e, f, u, s, t, 0, v, this.getTunnelSystemHeightWidthRatio(), bitSet);
	         }
	      }
	      return true;
	}
	
	public float getTunnelSystemWidth(Random random) {
	      float f = random.nextFloat() * 1.2F + random.nextFloat();
	      if (random.nextInt(10) == 0 && this.isBeneathSea(random)) {
	         f *= random.nextFloat() * random.nextFloat() * 8.0F;
	      }

	      return f;
	   }
	
	public double getTunnelSystemHeightWidthRatio() {
	      return (double)(0.5D + Math.random());
	   }
	
	public int getMaxCaveCount() {
	      return 20;
	   }
	
	public boolean isBeneathSea(Random random) {
		return this.getCaveY(random) < 32;
	}
	
	public int getCaveY(Random random) {
	      return random.nextInt(random.nextInt(188) + 8);
	   }
}
