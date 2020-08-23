package com.random.caveupdate.blocks;

import java.util.Random;

import net.minecraft.block.OreBlock;
import net.minecraft.util.math.MathHelper;

public class ModdedOreBlock extends OreBlock {

	public int minXP;
	public int maxXP;

	public ModdedOreBlock(Settings settings, int minXP, int maxXP) {
		super(settings);
		this.minXP = minXP;
		this.maxXP = maxXP;
	}

	@Override
	public int getExperienceWhenMined(Random random) {
		return MathHelper.nextInt(random, minXP, maxXP);
	}
	
}
