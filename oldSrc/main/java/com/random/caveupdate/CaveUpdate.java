package com.random.caveupdate;

import java.util.function.Consumer;

import com.random.caveupdate.blocks.CaveCarrotsBlock;
import com.random.caveupdate.blocks.CaveFarmlandBlock;
import com.random.caveupdate.blocks.CaveGrassBlock;
import com.random.caveupdate.blocks.CaveMushroomPlantBlock;
import com.random.caveupdate.blocks.CaveReedsBlock;
import com.random.caveupdate.blocks.DimpleCapsBlock;
import com.random.caveupdate.blocks.ModdedDoorBlock;
import com.random.caveupdate.blocks.ModdedOreBlock;
import com.random.caveupdate.blocks.ModdedSeagrassBlock;
import com.random.caveupdate.blocks.ModdedStairsBlock;
import com.random.caveupdate.blocks.ModdedTallSeagrassBlock;
import com.random.caveupdate.blocks.ModdedTrapdoorBlock;
import com.random.caveupdate.blocks.PlumpHelmetsBlock;
import com.random.caveupdate.blocks.RootsBlock;
import com.random.caveupdate.entities.CavePigEntity;
import com.random.caveupdate.feature.AndesiteFlatsFeature;
import com.random.caveupdate.feature.CaveChestFeature;
import com.random.caveupdate.feature.CaveGrassPatchesFeature;
import com.random.caveupdate.feature.CaveVinesFeature;
import com.random.caveupdate.feature.CavernWatersFeature;
import com.random.caveupdate.feature.DioritePlateauFeature;
import com.random.caveupdate.feature.GraniteCanyonFeature;
import com.random.caveupdate.feature.PebblesFeature;
import com.random.caveupdate.feature.RootsFeature;
import com.random.caveupdate.feature.StoneSpeleothemFeature;
import com.random.caveupdate.helper.EventListeners;
import com.random.caveupdate.helper.ModPredicates;
import com.random.caveupdate.helper.RegistryMagiks;
import com.random.caveupdate.items.UsableItem;
import com.random.caveupdate.items.UsableItem.UseEvents;
import com.random.caveupdate.mixin.SpawnRestrictionAccessor;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.FoodComponents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MushroomStewItem;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.ChanceRangeDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.placer.ColumnPlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;

public class CaveUpdate implements ModInitializer {
	
	public static final FoodComponent CAVE_CARROT_STEW_FOOD = (new FoodComponent.Builder()).hunger(6).saturationModifier(1.2F).statusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 2400, 0), 1.0F).alwaysEdible().build();
	public static final FoodComponent PLUMP_HELMET_STEW_FOOD = (new FoodComponent.Builder()).hunger(7).saturationModifier(2.2F).statusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 2400, 0), 1.0F).build();
	public static final FoodComponent COOKED_PLUMP_HELMET_FOOD = (new FoodComponent.Builder()).hunger(5).saturationModifier(0.6F).build();
	
	public static final Block NATURAL_STONE_WALL = new WallBlock(FabricBlockSettings.of(Material.STONE).hardness(1.2f));
    public static final Block ROOTS = new RootsBlock(FabricBlockSettings.of(Material.PLANT).noCollision().breakInstantly());
    public static final Block CAVE_GRASS = new CaveGrassBlock(FabricBlockSettings.of(Material.SOLID_ORGANIC, MaterialColor.WHITE).breakByTool(FabricToolTags.SHOVELS).ticksRandomly().strength(0.6F).sounds(BlockSoundGroup.GRASS));
    public static final Block CAVE_FARMLAND = new CaveFarmlandBlock(FabricBlockSettings.of(Material.SOLID_ORGANIC, MaterialColor.WHITE).breakByTool(FabricToolTags.SHOVELS).ticksRandomly().strength(0.6F).sounds(BlockSoundGroup.GRASS));
    public static final Block CAVE_CARROTS = new CaveCarrotsBlock(FabricBlockSettings.of(Material.PLANT).noCollision().breakInstantly());
    public static final Block PLUMP_HELMETS = new PlumpHelmetsBlock(FabricBlockSettings.of(Material.PLANT).noCollision().breakInstantly());
    public static final Block DIMPLE_CAPS = new DimpleCapsBlock(FabricBlockSettings.of(Material.PLANT).noCollision().breakInstantly());
    public static final Block PLUMP_HELMET_WILD = new CaveMushroomPlantBlock(FabricBlockSettings.of(Material.PLANT).noCollision().breakInstantly());
    public static final Block CAVE_REEDS = new CaveReedsBlock(FabricBlockSettings.of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.GRASS));
    public static final Block AQUAMARINE_ORE = new ModdedOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(3.0F, 3.0F), 3, 7);
    public static final Block AQUAMARINE_BLOCK = new Block(FabricBlockSettings.of(Material.METAL).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.METAL));
    public static final Block GLOWING_GRASS = new ModdedSeagrassBlock(FabricBlockSettings.of(Material.REPLACEABLE_UNDERWATER_PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.WET_GRASS).lightLevel(8).postProcess(ModPredicates::always).emissiveLighting(ModPredicates::always));
    public static final Block TALL_GLOWING_GRASS = new ModdedTallSeagrassBlock(FabricBlockSettings.of(Material.REPLACEABLE_UNDERWATER_PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.WET_GRASS).lightLevel(12).postProcess(ModPredicates::always).emissiveLighting(ModPredicates::always));
    
    public static final Block CAVE_REED_PLANKS = new Block(FabricBlockSettings.of(Material.NETHER_WOOD, MaterialColor.PINK).strength(2.0F, 3.0F).sounds(BlockSoundGroup.WOOD));
    public static final Block CAVE_REED_SLAB = new SlabBlock(FabricBlockSettings.of(Material.NETHER_WOOD, MaterialColor.NETHER).strength(2.0F, 3.0F).sounds(BlockSoundGroup.WOOD));
    public static final Block CAVE_REED_STAIRS = new ModdedStairsBlock(CAVE_REED_PLANKS.getDefaultState(), FabricBlockSettings.copy(CAVE_REED_PLANKS));
    public static final Block CAVE_REED_DOOR = new ModdedDoorBlock(FabricBlockSettings.copy(CAVE_REED_PLANKS).nonOpaque());
    public static final Block CAVE_REED_TRAPDOOR = new ModdedTrapdoorBlock(FabricBlockSettings.copy(CAVE_REED_PLANKS).nonOpaque());
    public static final Block CAVE_REED_FENCE = new FenceBlock(FabricBlockSettings.copy(CAVE_REED_PLANKS));
    public static final Block CAVE_REED_GATE = new FenceGateBlock(FabricBlockSettings.copy(CAVE_REED_PLANKS));
    
    public static final Item CAVE_CARROT_STEW = new MushroomStewItem(new Item.Settings().maxCount(1).group(ItemGroup.FOOD).food(CAVE_CARROT_STEW_FOOD));
    public static final Item PLUMP_HELMET_STEW = new MushroomStewItem(new Item.Settings().maxCount(1).group(ItemGroup.FOOD).food(PLUMP_HELMET_STEW_FOOD));
    public static final Item COOKED_PLUMP_HELMET = new Item(new Item.Settings().group(ItemGroup.FOOD).food(COOKED_PLUMP_HELMET_FOOD));
    public static final Item PLUMP_HELMET = new Item(new Item.Settings().group(ItemGroup.FOOD).food(FoodComponents.POTATO));
    public static final Item DIMPLE_CAP = new Item(new Item.Settings().group(ItemGroup.MISC).food(FoodComponents.SPIDER_EYE));
    public static final Item AQUAMARINE = new Item(new Item.Settings().group(ItemGroup.MISC));
    public static final Item TOTEM_OF_RECALL = new UsableItem(new Item.Settings().group(ItemGroup.TOOLS), UseEvents.RECALL);
    
    public static final RandomPatchFeatureConfig CAVE_REEDS_CONFIG = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(CAVE_REEDS.getDefaultState()), new ColumnPlacer(2, 2))).tries(20).spreadX(4).spreadY(0).spreadZ(4).cannotProject().build();
    
 // Entity
    public static final EntityType<CavePigEntity> CAVE_PIG = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier("caveupdate", "cave_pig"),
            FabricEntityTypeBuilder.create(SpawnGroup.AMBIENT, CavePigEntity::new).dimensions(EntityDimensions.fixed(0.8f, 0.8f)).build()
    );
    
    private static final Feature<DefaultFeatureConfig> STONE_SPELEOTHEM = Registry.register(
    		Registry.FEATURE,
    		new Identifier("caveupdate", "stone_speleothem"),
    		new StoneSpeleothemFeature(DefaultFeatureConfig.CODEC)
    	);
    private static final Feature<DefaultFeatureConfig> UNDERGROUND_CHEST = Registry.register(
    		Registry.FEATURE,
    		new Identifier("caveupdate", "undergrounod_chest"),
    		new CaveChestFeature(DefaultFeatureConfig.CODEC)
    	);
    //Cave """"""""biomes""""""""
    private static final Feature<DefaultFeatureConfig> ANDESITE_FLATS = Registry.register(
    		Registry.FEATURE,
    		new Identifier("caveupdate", "andesite_flats"),
    		new AndesiteFlatsFeature(DefaultFeatureConfig.CODEC)
    	);
    private static final Feature<DefaultFeatureConfig> GRANITE_CANYON = Registry.register(
    		Registry.FEATURE,
    		new Identifier("caveupdate", "granite_canyon"),
    		new GraniteCanyonFeature(DefaultFeatureConfig.CODEC)
    	);
    private static final Feature<DefaultFeatureConfig> DIORITE_PLATEAU = Registry.register(
    		Registry.FEATURE,
    		new Identifier("caveupdate", "diorite_plateau"),
    		new DioritePlateauFeature(DefaultFeatureConfig.CODEC)
    	);
    private static final Feature<DefaultFeatureConfig> CAVERN_WATERS = Registry.register(
    		Registry.FEATURE,
    		new Identifier("caveupdate", "cavern_waters"),
    		new CavernWatersFeature(DefaultFeatureConfig.CODEC)
    	);
    
    private static final Feature<OreFeatureConfig> CAVE_GRASS_PATCHES = Registry.register(
    		Registry.FEATURE,
    		new Identifier("caveupdate", "cave_grass_patches"),
    		new CaveGrassPatchesFeature(OreFeatureConfig.CODEC)
    	);
    private static final Feature<DefaultFeatureConfig> STONE_PEBBLES = Registry.register(
    		Registry.FEATURE,
    		new Identifier("caveupdate", "stone_pebbles"),
    		new PebblesFeature(DefaultFeatureConfig.CODEC)
    	);
    private static final Feature<DefaultFeatureConfig> ROOT = Registry.register(
    		Registry.FEATURE,
    		new Identifier("caveupdate", "root"),
    		new RootsFeature(DefaultFeatureConfig.CODEC)
    	);
    private static final Feature<DefaultFeatureConfig> CAVE_VINES = Registry.register(
    		Registry.FEATURE,
    		new Identifier("caveupdate", "cave_vines"),
    		new CaveVinesFeature(DefaultFeatureConfig.CODEC)
    	);
    
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            // Cave dirt to cave farm land
            if (player.getStackInHand(hand).getItem().isIn(FabricToolTags.HOES)) {
            	//System.out.println("Player has hoe");
            	if (hitResult.getSide() != Direction.DOWN && !player.isSpectator() && world.getBlockState(hitResult.getBlockPos()) == CaveUpdate.CAVE_GRASS.getDefaultState()) {
            		//System.out.println("Tilling complete!");
            		world.playSound(player, hitResult.getBlockPos(), SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    if (!world.isClient) {
                       world.setBlockState(hitResult.getBlockPos(), CaveUpdate.CAVE_FARMLAND.getDefaultState(), 11);
                       if (player != null) {
                    	   player.getStackInHand(hand).damage(1, (LivingEntity)player, (Consumer<LivingEntity>)((p) -> {
                             p.sendToolBreakStatus(hand);
                          }));
                       }
                       if (Math.random() < 0.1F) {
                    	   world.spawnEntity(new ItemEntity(world, hitResult.getBlockPos().getX(), hitResult.getBlockPos().getY(), hitResult.getBlockPos().getZ(), new ItemStack(CAVE_CARROTS.asItem())));
                       }
                       if (Math.random() < 0.1F) {
                    	   world.spawnEntity(new ItemEntity(world, hitResult.getBlockPos().getX(), hitResult.getBlockPos().getY(), hitResult.getBlockPos().getZ(), new ItemStack(PLUMP_HELMETS.asItem())));
                       }
                       if (Math.random() < 0.1F) {
                    	   world.spawnEntity(new ItemEntity(world, hitResult.getBlockPos().getX(), hitResult.getBlockPos().getY(), hitResult.getBlockPos().getZ(), new ItemStack(DIMPLE_CAPS.asItem())));
                       }
                    }

                    return ActionResult.SUCCESS;
            	}

            }
            return ActionResult.PASS;
        });
		
        //Block/item registry
		Registry.register(Registry.BLOCK, new Identifier("caveupdate", "roots"), ROOTS);
		Registry.register(Registry.BLOCK, new Identifier("caveupdate", "cave_grass"), CAVE_GRASS);
		Registry.register(Registry.BLOCK, new Identifier("caveupdate", "cave_farmland"), CAVE_FARMLAND);
		Registry.register(Registry.BLOCK, new Identifier("caveupdate", "cave_carrots"), CAVE_CARROTS);
		Registry.register(Registry.BLOCK, new Identifier("caveupdate", "plump_helmets"), PLUMP_HELMETS);
		Registry.register(Registry.BLOCK, new Identifier("caveupdate", "plump_helmet_wild"), PLUMP_HELMET_WILD);
		Registry.register(Registry.BLOCK, new Identifier("caveupdate", "dimple_caps"), DIMPLE_CAPS);
		Registry.register(Registry.BLOCK, new Identifier("caveupdate", "cave_reeds"), CAVE_REEDS);
		Registry.register(Registry.BLOCK, new Identifier("caveupdate", "natural_stone_wall"), NATURAL_STONE_WALL);
		Registry.register(Registry.ITEM, new Identifier("caveupdate", "natural_stone_wall"), new BlockItem(NATURAL_STONE_WALL, new Item.Settings().group(ItemGroup.DECORATIONS)));
		Registry.register(Registry.ITEM, new Identifier("caveupdate", "roots"), new BlockItem(ROOTS, new Item.Settings().group(ItemGroup.DECORATIONS)));
		Registry.register(Registry.ITEM, new Identifier("caveupdate", "cave_grass"), new BlockItem(CAVE_GRASS, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
		Registry.register(Registry.ITEM, new Identifier("caveupdate", "cave_farmland"), new BlockItem(CAVE_FARMLAND, new Item.Settings().group(ItemGroup.DECORATIONS)));
		Registry.register(Registry.ITEM, new Identifier("caveupdate", "cave_carrot"), new BlockItem(CAVE_CARROTS, new Item.Settings().food(FoodComponents.CARROT).group(ItemGroup.FOOD)));
		Registry.register(Registry.ITEM, new Identifier("caveupdate", "cave_carrot_stew"), CAVE_CARROT_STEW);
		Registry.register(Registry.ITEM, new Identifier("caveupdate", "plump_helmet_stew"), PLUMP_HELMET_STEW);
		Registry.register(Registry.ITEM, new Identifier("caveupdate", "cooked_plump_helmet"), COOKED_PLUMP_HELMET);
		Registry.register(Registry.ITEM, new Identifier("caveupdate", "plump_helmet_spawn"), new BlockItem(PLUMP_HELMETS, new Item.Settings().group(ItemGroup.MISC)));
		Registry.register(Registry.ITEM, new Identifier("caveupdate", "plump_helmet"), PLUMP_HELMET);
		Registry.register(Registry.ITEM, new Identifier("caveupdate", "dimple_cap_spawn"), new BlockItem(DIMPLE_CAPS, new Item.Settings().group(ItemGroup.MISC)));
		Registry.register(Registry.ITEM, new Identifier("caveupdate", "dimple_cap"), DIMPLE_CAP);
		Registry.register(Registry.ITEM, new Identifier("caveupdate", "cave_reed"), new BlockItem(CAVE_REEDS, new Item.Settings().group(ItemGroup.MISC).fireproof()));
		RegistryMagiks.RegisterBlockWithItem("glowing_grass", GLOWING_GRASS, new Item.Settings().group(ItemGroup.DECORATIONS));
		Registry.register(Registry.BLOCK, new Identifier("caveupdate", "tall_glowing_grass"), TALL_GLOWING_GRASS);
		
		//Cave reed variants
		RegistryMagiks.RegisterBlockWithItem("cave_reed_planks", CAVE_REED_PLANKS, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS));
		RegistryMagiks.RegisterBlockWithItem("cave_reed_slab", CAVE_REED_SLAB, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS));
		RegistryMagiks.RegisterBlockWithItem("cave_reed_stairs", CAVE_REED_STAIRS, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS));
		RegistryMagiks.RegisterBlockWithItem("cave_reed_door", CAVE_REED_DOOR, new Item.Settings().group(ItemGroup.REDSTONE));
		RegistryMagiks.RegisterBlockWithItem("cave_reed_trapdoor", CAVE_REED_TRAPDOOR, new Item.Settings().group(ItemGroup.REDSTONE));
		RegistryMagiks.RegisterBlockWithItem("cave_reed_fence", CAVE_REED_FENCE, new Item.Settings().group(ItemGroup.DECORATIONS));
		RegistryMagiks.RegisterBlockWithItem("cave_reed_fence_gate", CAVE_REED_GATE, new Item.Settings().group(ItemGroup.REDSTONE));
		
		//Ores and stuff
		RegistryMagiks.RegisterBlockWithItem("aquamarine_ore", AQUAMARINE_ORE, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS));
		RegistryMagiks.RegisterBlockWithItem("aquamarine_block", AQUAMARINE_BLOCK, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS));
		Registry.register(Registry.ITEM, new Identifier("caveupdate", "aquamarine"), AQUAMARINE);
		
		//Unique items
		//Registry.register(Registry.ITEM, new Identifier("caveupdate", "totem_of_recall"), TOTEM_OF_RECALL);
		
		Registry.register(Registry.ITEM, new Identifier("caveupdate", "cave_pig_spawn_egg"), new SpawnEggItem(
                CAVE_PIG, 0xB1B3AD, 0x3E4146, new Item.Settings().group(ItemGroup.MISC)));

		SpawnRestrictionAccessor.callRegister(CAVE_PIG, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, CavePigEntity::canSpawnInDark);
		FabricDefaultAttributeRegistry.register(CAVE_PIG, CavePigEntity.createMobAttributes());
        
		//Spawning
		for(Biome biome : Registry.BIOME) {
        	if (biome.getCategory() != Biome.Category.NETHER && biome.getCategory() != Biome.Category.THEEND) {
        		biome.getEntitySpawnList(CAVE_PIG.getSpawnGroup())
            	.add(new Biome.SpawnEntry(CAVE_PIG, 150, 1, 4));
        	}
		}

        for(Biome biome : Registry.BIOME) {
        	if (biome.getCategory() != Biome.Category.NETHER && biome.getCategory() != Biome.Category.THEEND) {
	        	biome.addFeature(GenerationStep.Feature.UNDERGROUND_STRUCTURES, 
	            		Feature.MONSTER_ROOM.configure(FeatureConfig.DEFAULT).createDecoratedFeature
	            		(Decorator.DUNGEONS.configure(new ChanceDecoratorConfig(2)))
	            	);
        	}
		}
        
        //Underground biomes
        //Registry.BIOME.forEach(CaveUpdate::registerJungleFeatures);
        for(Biome biome : Registry.BIOME) {
        	if (biome.getCategory() == Biome.Category.JUNGLE) {
        		biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(
        				new OreFeatureConfig(
        					    OreFeatureConfig.Target.NATURAL_STONE,
        					    Blocks.MOSSY_COBBLESTONE.getDefaultState(),
        					    8 //Ore vein size
        				   )).createDecoratedFeature(
        					Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(
        					    64, //Number of veins per chunk
        					    24, //Bottom Offset
        					    48, //Min y level
        					    255 //Max y level
        				))));
        		biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION,	CAVE_VINES
        				.configure(new DefaultFeatureConfig())
        				.createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(3500, 0, 0, 255)))
        			);
        	}
        	if (biome.getCategory() == Biome.Category.ICY) {
        		biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(
        				new OreFeatureConfig(
        					    OreFeatureConfig.Target.NATURAL_STONE,
        					    Blocks.ICE.getDefaultState(),
        					    8 //Ore vein size
        				   )).createDecoratedFeature(
        					Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(
        					    64, //Number of veins per chunk
        					    24, //Bottom Offset
        					    48, //Min y level
        					    255 //Max y level
        				))));
        		biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(
        				new OreFeatureConfig(
        					    OreFeatureConfig.Target.NATURAL_STONE,
        					    Blocks.SNOW_BLOCK.getDefaultState(),
        					    24 //Ore vein size
        				   )).createDecoratedFeature(
        					Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(
        					    64, //Number of veins per chunk
        					    24, //Bottom Offset
        					    48, //Min y level
        					    255 //Max y level
        				))));
        	}
        	if (biome.getCategory() == Biome.Category.PLAINS) {
        		biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(
        				new OreFeatureConfig(
        					    OreFeatureConfig.Target.NATURAL_STONE,
        					    Blocks.COARSE_DIRT.getDefaultState(),
        					    24 //Ore vein size
        				   )).createDecoratedFeature(
        					Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(
        					    64, //Number of veins per chunk
        					    48, //Bottom Offset
        					    64, //Min y level
        					    255 //Max y level
        				))));
        	}
        	if (biome.getCategory() == Biome.Category.DESERT) {
        		biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(
        				new OreFeatureConfig(
        					    OreFeatureConfig.Target.NATURAL_STONE,
        					    Blocks.SANDSTONE.getDefaultState(),
        					    48 //Ore vein size
        				   )).createDecoratedFeature(
        					Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(
        					    64, //Number of veins per chunk
        					    32, //Bottom Offset
        					    48, //Min y level
        					    255 //Max y level
        				))));
        	}
        	if (biome.getCategory() == Biome.Category.MESA) {
        		biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(
        				new OreFeatureConfig(
        					    OreFeatureConfig.Target.NATURAL_STONE,
        					    Blocks.RED_SANDSTONE.getDefaultState(),
        					    48 //Ore vein size
        				   )).createDecoratedFeature(
        					Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(
        					    64, //Number of veins per chunk
        					    32, //Bottom Offset
        					    48, //Min y level
        					    255 //Max y level
        				))));
        		biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(
        				new OreFeatureConfig(
        					    OreFeatureConfig.Target.NATURAL_STONE,
        					    Blocks.RED_STAINED_GLASS.getDefaultState(),
        					    12 //Ore vein size
        				   )).createDecoratedFeature(
        					Decorator.CHANCE_RANGE.configure(new ChanceRangeDecoratorConfig(
        					    10, //Chance of vein per chunk
        					    0, //Bottom Offset
        					    12, //Min y level
        					    48 //Max y level
        				))));
        		biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(
        				new OreFeatureConfig(
        					    OreFeatureConfig.Target.NATURAL_STONE,
        					    Blocks.YELLOW_STAINED_GLASS.getDefaultState(),
        					    12 //Ore vein size
        				   )).createDecoratedFeature(
        					Decorator.CHANCE_RANGE.configure(new ChanceRangeDecoratorConfig(
        					    10, //Chance of vein per chunk
        					    0, //Bottom Offset
        					    12, //Min y level
        					    48 //Max y level
        				))));
        		biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(
        				new OreFeatureConfig(
        					    OreFeatureConfig.Target.NATURAL_STONE,
        					    Blocks.WHITE_STAINED_GLASS.getDefaultState(),
        					    12 //Ore vein size
        				   )).createDecoratedFeature(
        					Decorator.CHANCE_RANGE.configure(new ChanceRangeDecoratorConfig(
        					    10, //Chance of vein per chunk
        					    0, //Bottom Offset
        					    12, //Min y level
        					    48 //Max y level
        				))));
        		biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(
        				new OreFeatureConfig(
        					    OreFeatureConfig.Target.NATURAL_STONE,
        					    Blocks.BLACK_STAINED_GLASS.getDefaultState(),
        					    12 //Ore vein size
        				   )).createDecoratedFeature(
        					Decorator.CHANCE_RANGE.configure(new ChanceRangeDecoratorConfig(
        					    10, //Chance of vein per chunk
        					    0, //Bottom Offset
        					    12, //Min y level
        					    48 //Max y level
        				))));
        	}
        	//Aquamarine
        	if (biome.getCategory() == Biome.Category.OCEAN) {
        		biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(
        				new OreFeatureConfig(
        					    OreFeatureConfig.Target.NATURAL_STONE,
        					    CaveUpdate.AQUAMARINE_ORE.getDefaultState(),
        					    2 //Ore vein size
        				   )).createDecoratedFeature(
        					Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(
        					    2, //Number of veins per chunk
        					    0, //Bottom Offset
        					    5, //Min y level
        					    64 //Max y level
        				))));
        	}
        }
        
      //Cave decor/biomes
        for(Biome biome : Registry.BIOME) {
        	if (biome.getCategory() != Biome.Category.NETHER && biome.getCategory() != Biome.Category.THEEND) {
	        	biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION,	STONE_SPELEOTHEM
	    				.configure(new DefaultFeatureConfig())
	    				.createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(750, 0, 0, 96)))
	    			);
	        	biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION,	STONE_PEBBLES
	    				.configure(new DefaultFeatureConfig())
	    				.createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(750, 0, 0, 96)))
	    			);
	        	biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION,	UNDERGROUND_CHEST
	    				.configure(new DefaultFeatureConfig())
	    				.createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(8, 0, 0, 96)))
	    			); 
	        	biome.addFeature(GenerationStep.Feature.LAKES,	ANDESITE_FLATS
	    				.configure(new DefaultFeatureConfig())
	    				.createDecoratedFeature(Decorator.CHANCE_RANGE.configure(new ChanceRangeDecoratorConfig(0.1F, 5, 0, 48)))
	    			); 
	        	biome.addFeature(GenerationStep.Feature.LAKES,	GRANITE_CANYON
	    				.configure(new DefaultFeatureConfig())
	    				.createDecoratedFeature(Decorator.CHANCE_RANGE.configure(new ChanceRangeDecoratorConfig(0.1F, 5, 0, 32)))
	    			); 
	        	biome.addFeature(GenerationStep.Feature.LAKES,	DIORITE_PLATEAU
	    				.configure(new DefaultFeatureConfig())
	    				.createDecoratedFeature(Decorator.CHANCE_RANGE.configure(new ChanceRangeDecoratorConfig(0.1F, 5, 0, 48)))
	    			); 
	        	biome.addFeature(GenerationStep.Feature.LAKES,	CAVERN_WATERS
	    				.configure(new DefaultFeatureConfig())
	    				.createDecoratedFeature(Decorator.LAVA_LAKE.configure(new ChanceDecoratorConfig(100)))
	    			); 
	        	biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES,
	            	    CAVE_GRASS_PATCHES.configure(
	    			new OreFeatureConfig(
	    			    OreFeatureConfig.Target.NATURAL_STONE, //Doesn't matter here lol
	    			    CaveUpdate.CAVE_GRASS.getDefaultState(),
	    			    32 //Ore vein size
	    		   )).createDecoratedFeature(
	    			Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(
	    			    4, //Number of veins per chunk
	    			    0, //Bottom Offset
	    			    0, //Min y level
	    			    48 //Max y level
	    		))));
	        	biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION,	ROOT
	    				.configure(new DefaultFeatureConfig())
	    				.createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(1200, 0, 0, 255)))
	    			);
	        	biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.RANDOM_PATCH.configure(CAVE_REEDS_CONFIG)
	        			.createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(4000, 0, 0, 48))));
        	}
		}
        
        // Loot
        EventListeners.injectLoot();
        
		System.out.println("Hello Fabric world!");
	}
}
