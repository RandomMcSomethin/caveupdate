package com.random.caveupdate.items;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class SeedsItem extends Item {

	public BlockState crop;

	public SeedsItem(Settings settings, BlockState plant) {
		super(settings);
		this.crop = plant;
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
	      if (this.isFood()) {
	         ItemStack itemStack = user.getStackInHand(hand);
	         if (user.canConsume(this.getFoodComponent().isAlwaysEdible())) {
	            user.setCurrentHand(hand);
	            return TypedActionResult.consume(itemStack);
	         } else {
	            return TypedActionResult.fail(itemStack);
	         }
	      } else {
	         return TypedActionResult.pass(user.getStackInHand(hand));
	      }
	   }
}
