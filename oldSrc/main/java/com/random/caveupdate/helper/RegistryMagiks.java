package com.random.caveupdate.helper;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item.Settings;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class RegistryMagiks {
	//Special methods
    public static void RegisterBlockWithItem(String internal, Block block, Settings settings) {
    	Registry.register(Registry.BLOCK, new Identifier("caveupdate", internal), block);
    	Registry.register(Registry.ITEM, new Identifier("caveupdate", internal), new BlockItem(block, settings));
    }
}
