package com.random.caveupdate.mixin;

import java.util.Iterator;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootTables;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.DungeonFeature;

@Mixin(DungeonFeature.class)
public class DungeonMixin {

	@Shadow
	private static final Logger LOGGER = LogManager.getLogger();
	
	@Shadow
	private static final EntityType<?>[] MOB_SPAWNER_ENTITIES;
	
	@Shadow
	private static final BlockState AIR;
	
	public boolean generate(ServerWorldAccess serverWorldAccess, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, DefaultFeatureConfig defaultFeatureConfig) {
	    int dungeonType = random.nextInt(4); 
	    int carpetColor = random.nextInt(5);
	    int dungeonHeight = 2 + random.nextInt(4);
		boolean i = true;
	      int j = random.nextInt(3) + 2;
	      int k = -j - 1;
	      int l = j + 1;
	      boolean m = true;
	      boolean n = true;
	      int o = random.nextInt(3) + 2;
	      int p = -o - 1;
	      int q = o + 1;
	      int r = 0;

	      int v;
	      int w;
	      int x;
	      BlockPos blockPos3;
	      for(v = k; v <= l; ++v) {
	         for(w = -1; w <= dungeonHeight + 1; ++w) {
	            for(x = p; x <= q; ++x) {
	               blockPos3 = blockPos.add(v, w, x);
	               Material material = serverWorldAccess.getBlockState(blockPos3).getMaterial();
	               boolean bl = material.isSolid();
	               if (w == -1 && !bl) {
	                  return false;
	               }

	               if (w == dungeonHeight + 1 && !bl) {
	                  return false;
	               }

	               if ((v == k || v == l || x == p || x == q) && w == 0 && serverWorldAccess.isAir(blockPos3) && serverWorldAccess.isAir(blockPos3.up())) {
	                  ++r;
	               }
	            }
	         }
	      }

	      //Structure
	      if (r >= 1 && r <= 5) {
	         for(v = k; v <= l; ++v) {
	            for(w = dungeonHeight; w >= -1; --w) {
	               for(x = p; x <= q; ++x) {
	                  blockPos3 = blockPos.add(v, w, x);
	                  BlockState blockState = serverWorldAccess.getBlockState(blockPos3);
	                  if (v != k && w != -1 && x != p && v != l && w != 4 && x != q) {
	                     if (!blockState.isOf(Blocks.CHEST) && !blockState.isOf(Blocks.SPAWNER)) {
	                    	 serverWorldAccess.setBlockState(blockPos3, AIR, 2);
	                    	 //Chains
	                    	 if (dungeonType != 3 && serverWorldAccess.getBlockState(blockPos3.up()).getMaterial().isSolid()) {
	                    		 if (random.nextInt(6) == 0) {
	                    			 serverWorldAccess.setBlockState(blockPos3, Blocks.CHAIN.getDefaultState(), 2);
	                    		 }
	                    	 }
	                    	 //Debris
	                    	 if ((dungeonType == 0 || dungeonType == 2) && w == 0 && random.nextInt(12) == 0) {
	                			  serverWorldAccess.setBlockState(blockPos3, Blocks.COBBLESTONE_SLAB.getDefaultState(), 2);
	                		  }
	                    	 //Carpets
	                    	 if (dungeonType == 1 && w == 0 && random.nextInt(2) != 0) {
	                		  		switch (carpetColor) {
	    	                		  	case 0:
	    	                		  		serverWorldAccess.setBlockState(blockPos3, Blocks.WHITE_CARPET.getDefaultState(), 2);
	    	                		  		break;
			                		  	case 1:
			                		  		serverWorldAccess.setBlockState(blockPos3, Blocks.GRAY_CARPET.getDefaultState(), 2);
			                		  		break;
			                		  	case 2:
			                		  		serverWorldAccess.setBlockState(blockPos3, Blocks.RED_CARPET.getDefaultState(), 2);
			                		  		break;
			                		  	case 3:
			                		  		serverWorldAccess.setBlockState(blockPos3, Blocks.PURPLE_CARPET.getDefaultState(), 2);
			                		  		break;
			                		  	case 4:
			                		  		serverWorldAccess.setBlockState(blockPos3, Blocks.ORANGE_CARPET.getDefaultState(), 2);
			                		  		break;
			                		  	}
	                		  		}
	                     }
	                  } else if (blockPos3.getY() >= 0 && !serverWorldAccess.getBlockState(blockPos3.down()).getMaterial().isSolid()) {
	                     serverWorldAccess.setBlockState(blockPos3, AIR, 2);
	                  } else if (blockState.getMaterial().isSolid() && !blockState.isOf(Blocks.CHEST)) {
	                	  //Dungeon wall types
	                	  switch (dungeonType) {
	                	  	// Cobblestone (regular)
	                	  case 0:
	                		  if (random.nextInt(4) != 0) {
			                        serverWorldAccess.setBlockState(blockPos3, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 2);
			                     } else {
			                        serverWorldAccess.setBlockState(blockPos3, Blocks.COBBLESTONE.getDefaultState(), 2);
			                     }
	                		  break;
	                		  // Stone bricks
	                	  case 1:
	                		  if (random.nextInt(4) != 0) {
			                        serverWorldAccess.setBlockState(blockPos3, Blocks.MOSSY_STONE_BRICKS.getDefaultState(), 2);
			                     } else if (w == -1 && random.nextInt(6) != 0) {
				                        serverWorldAccess.setBlockState(blockPos3, Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 2);
				                 } else {
			                        serverWorldAccess.setBlockState(blockPos3, Blocks.STONE_BRICKS.getDefaultState(), 2);
			                     }
	                		  break;
	                		  // Infested
	                	  case 2:
	                		  if (random.nextInt(4) != 0) {
			                        serverWorldAccess.setBlockState(blockPos3, Blocks.INFESTED_COBBLESTONE.getDefaultState(), 2);
			                     } else {
			                        serverWorldAccess.setBlockState(blockPos3, Blocks.COBBLESTONE.getDefaultState(), 2);
			                     }
	                		  break;
	                		  // Slimy
	                	  case 3:
	                		  if (random.nextInt(8) == 0) {
			                        serverWorldAccess.setBlockState(blockPos3, Blocks.SLIME_BLOCK.getDefaultState(), 2);
			                     } else {
			                        serverWorldAccess.setBlockState(blockPos3, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 2);
			                     }
	                		  break;
	                	  }
	                  }
	               }
	            }
	         }

	         for(v = 0; v < 2; ++v) {
	            for(w = 0; w < 3; ++w) {
	               x = blockPos.getX() + random.nextInt(j * 2 + 1) - j;
	               int ab = blockPos.getY();
	               int ac = blockPos.getZ() + random.nextInt(o * 2 + 1) - o;
	               BlockPos blockPos4 = new BlockPos(x, ab, ac);
	               if (serverWorldAccess.isAir(blockPos4)) {
	                  int ad = 0;
	                  Iterator var24 = Direction.Type.HORIZONTAL.iterator();

	                  while(var24.hasNext()) {
	                     Direction direction = (Direction)var24.next();
	                     if (serverWorldAccess.getBlockState(blockPos4.offset(direction)).getMaterial().isSolid()) {
	                        ++ad;
	                     }
	                  }

	                  if (ad == 1) {
	                     serverWorldAccess.setBlockState(blockPos4, StructurePiece.method_14916(serverWorldAccess, blockPos4, Blocks.CHEST.getDefaultState()), 2);
	                     LootableContainerBlockEntity.setLootTable(serverWorldAccess, random, blockPos4, LootTables.SIMPLE_DUNGEON_CHEST);
	                     break;
	                  }
	               }
	            }
	         }

	         serverWorldAccess.setBlockState(blockPos, Blocks.SPAWNER.getDefaultState(), 2);
	         BlockEntity blockEntity = serverWorldAccess.getBlockEntity(blockPos);
	         if (blockEntity instanceof MobSpawnerBlockEntity) {
	            ((MobSpawnerBlockEntity)blockEntity).getLogic().setEntityId(this.getMobSpawnerEntity(random));
	            if (dungeonType == 2) {
	            	((MobSpawnerBlockEntity)blockEntity).getLogic().setEntityId(EntityType.SILVERFISH);
	            }
	            if (dungeonType == 3) {
	            	((MobSpawnerBlockEntity)blockEntity).getLogic().setEntityId(EntityType.SLIME);
	            }
	         } else {
	            LOGGER.error("Failed to fetch mob spawner entity at ({}, {}, {})", blockPos.getX(), blockPos.getY(), blockPos.getZ());
	         }

	         return true;
	      } else {
	         return false;
	      }
	   }

	   public EntityType<?> getMobSpawnerEntity(Random random) {
	      return (EntityType)Util.getRandom((Object[])MOB_SPAWNER_ENTITIES, random);
	   }
	
	static {
	      MOB_SPAWNER_ENTITIES = new EntityType[]{EntityType.SKELETON, EntityType.ZOMBIE, EntityType.ZOMBIE, EntityType.SPIDER};
	      AIR = Blocks.CAVE_AIR.getDefaultState();
	   }
}
