package com.random.caveupdate.items;

import java.util.Optional;
import java.util.Random;

import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class UsableItem extends Item {

	public enum UseEvents {
		RECALL
	}
	
	public UseEvents ev;

	public UsableItem(Settings settings, UseEvents event) {
		super(settings);
		this.ev = event;
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		BlockPos pos = user.getBlockPos();
		if (this.ev == UseEvents.RECALL) {
			if (world.isClient) {
				for (int i = 0; i < 5; i++) {
					world.addParticle(ParticleTypes.FIREWORK, (double)pos.getX() + user.getRandom().nextDouble(), (double)pos.getY() + user.getRandom().nextDouble(), (double)pos.getZ() + user.getRandom().nextDouble(), 0.0D, 0.0D, 0.0D);
				}
				world.playSound(user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, user.getSoundCategory(), 1.0F, 0.1F, false);
	            //GameRenderer.showFloatingItem(user.getStackInHand(hand));
			}
			BlockPos respawnPos = null;
			if (!world.isClient) {
				respawnPos = user.getServer().getWorld(world.getRegistryKey()).getSpawnPos();
				BlockPos sleepPos = (BlockPos)user.getSleepingPosition().orElse((BlockPos)null);
				if (sleepPos != null) {
					respawnPos = sleepPos;
				}
			}
			if (respawnPos != null) {
				user.teleport(respawnPos.getX(), respawnPos.getY(), respawnPos.getZ());
				return TypedActionResult.success(user.getStackInHand(hand));
			}
		}
		return TypedActionResult.pass(user.getStackInHand(hand));
	}
}
