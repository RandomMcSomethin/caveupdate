package com.random.caveupdate.helper;

import com.random.caveupdate.CaveUpdate;

import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.loot.BinomialLootTableRange;
import net.minecraft.loot.ConstantLootTableRange;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.util.Identifier;

public class EventListeners {
	
	public static final Identifier ELDER_GUARDIAN_LOOT_TABLE = new Identifier("minecraft", "entities/elder_guardian");
	 
	//More special methods
	public static void injectLoot() {
		LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, supplier, setter) -> {
		    if (ELDER_GUARDIAN_LOOT_TABLE.equals(id)) {
		    	FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
		                .rolls(ConstantLootTableRange.create(1))
		                .withFunction(SetCountLootFunction.builder(BinomialLootTableRange.create(6, 0.5f)).build())
		                .withEntry(ItemEntry.builder(CaveUpdate.AQUAMARINE).build());
		 
		        supplier.pool(poolBuilder);
		    }
		});
	}
}
