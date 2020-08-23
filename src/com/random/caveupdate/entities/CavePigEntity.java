package com.random.caveupdate.entities;

import java.util.Random;

import com.google.common.collect.UnmodifiableIterator;
import com.random.caveupdate.CaveUpdate;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Dismounting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemSteerable;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Saddleable;
import net.minecraft.entity.SaddledComponent;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class CavePigEntity extends AnimalEntity implements ItemSteerable, Saddleable {
	   private static final TrackedData<Boolean> SADDLED;
	   private static final TrackedData<Integer> BOOST_TIME;
	   private static final Ingredient BREEDING_INGREDIENT;
	   private final SaddledComponent saddledComponent;

	   public CavePigEntity(EntityType<? extends CavePigEntity> entityType, World world) {
	      super(entityType, world);
	      this.saddledComponent = new SaddledComponent(this.dataTracker, BOOST_TIME, SADDLED);
	   }

	   protected void initGoals() {
	      this.goalSelector.add(0, new SwimGoal(this));
	      this.goalSelector.add(1, new EscapeDangerGoal(this, 1.25D));
	      this.goalSelector.add(3, new AnimalMateGoal(this, 1.0D));
	      this.goalSelector.add(4, new TemptGoal(this, 1.2D, Ingredient.ofItems(CaveUpdate.CAVE_CARROTS.asItem()), false));
	      this.goalSelector.add(4, new TemptGoal(this, 1.2D, false, BREEDING_INGREDIENT));
	      this.goalSelector.add(5, new FollowParentGoal(this, 1.1D));
	      this.goalSelector.add(6, new WanderAroundFarGoal(this, 1.0D));
	      this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
	      this.goalSelector.add(8, new LookAroundGoal(this));
	   }

	   public static DefaultAttributeContainer.Builder createMobAttributes() {
	      return MobEntity.createMobAttributes()
	    		  .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D)
	    		  .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D);
	   }

	   public Entity getPrimaryPassenger() {
	      return this.getPassengerList().isEmpty() ? null : (Entity)this.getPassengerList().get(0);
	   }

	   public boolean canBeControlledByRider() {
	      Entity entity = this.getPrimaryPassenger();
	      if (!(entity instanceof PlayerEntity)) {
	         return false;
	      } else {
	         PlayerEntity playerEntity = (PlayerEntity)entity;
	         return playerEntity.getMainHandStack().getItem() == CaveUpdate.CAVE_CARROTS.asItem() || playerEntity.getOffHandStack().getItem() == CaveUpdate.CAVE_CARROTS.asItem();
	      }
	   }

	   public void onTrackedDataSet(TrackedData<?> data) {
	      if (BOOST_TIME.equals(data) && this.world.isClient) {
	         this.saddledComponent.boost();
	      }

	      super.onTrackedDataSet(data);
	   }

	   protected void initDataTracker() {
	      super.initDataTracker();
	      this.dataTracker.startTracking(SADDLED, false);
	      this.dataTracker.startTracking(BOOST_TIME, 0);
	   }

	   public void writeCustomDataToTag(CompoundTag tag) {
	      super.writeCustomDataToTag(tag);
	      this.saddledComponent.toTag(tag);
	   }

	   public void readCustomDataFromTag(CompoundTag tag) {
	      super.readCustomDataFromTag(tag);
	      this.saddledComponent.fromTag(tag);
	   }

	   protected SoundEvent getAmbientSound() {
	      return SoundEvents.ENTITY_PIG_AMBIENT;
	   }

	   protected SoundEvent getHurtSound(DamageSource source) {
	      return SoundEvents.ENTITY_PIG_HURT;
	   }

	   protected SoundEvent getDeathSound() {
	      return SoundEvents.ENTITY_PIG_DEATH;
	   }

	   protected void playStepSound(BlockPos pos, BlockState state) {
	      this.playSound(SoundEvents.ENTITY_PIG_STEP, 0.15F, 1.0F);
	   }

	   public ActionResult interactMob(PlayerEntity player, Hand hand) {
		   ItemStack itemStack = player.getStackInHand(hand);
		   if (itemStack.getItem() == Items.BUCKET && !this.isBaby()) {
		         player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
		         ItemStack itemStack2 = ItemUsage.method_30012(itemStack, player, Items.MILK_BUCKET.getStackForRender());
		         player.setStackInHand(hand, itemStack2);
		         return ActionResult.success(this.world.isClient);
		   }
	      //boolean bl = this.isBreedingItem(player.getStackInHand(hand));
	      if (!player.isSneaking() && this.isSaddled() && !this.hasPassengers()) {
	         if (!this.world.isClient) {
	            player.startRiding(this);
	         }

	         return ActionResult.success(this.world.isClient);
	      } else {
	         ActionResult actionResult = super.interactMob(player, hand);
	         if (!actionResult.isAccepted()) {
	            return itemStack.getItem() == Items.SADDLE ? itemStack.useOnEntity(player, this, hand) : ActionResult.PASS;
	         } else {
	            return actionResult;
	         }
	      }
	   }

	   public boolean canBeSaddled() {
	      return this.isAlive() && !this.isBaby();
	   }

	   protected void dropInventory() {
	      super.dropInventory();
	      if (this.isSaddled()) {
	         this.dropItem(Items.SADDLE);
	      }

	   }

	   public boolean isSaddled() {
	      return this.saddledComponent.isSaddled();
	   }

	   public void saddle(SoundCategory sound) {
	      this.saddledComponent.setSaddled(true);
	      if (sound != null) {
	         this.world.playSoundFromEntity((PlayerEntity)null, this, SoundEvents.ENTITY_PIG_SADDLE, sound, 0.5F, 1.0F);
	      }

	   }

	   public Vec3d updatePassengerForDismount(LivingEntity passenger) {
	      Direction direction = this.getMovementDirection();
	      if (direction.getAxis() == Direction.Axis.Y) {
	         return super.updatePassengerForDismount(passenger);
	      } else {
	         int[][] is = Dismounting.getDismountOffsets(direction);
	         BlockPos blockPos = this.getBlockPos();
	         BlockPos.Mutable mutable = new BlockPos.Mutable();
	         UnmodifiableIterator<EntityPose> var6 = passenger.getPoses().iterator();

	         while(var6.hasNext()) {
	            EntityPose entityPose = (EntityPose)var6.next();
	            Box box = passenger.getBoundingBox(entityPose);
	            int[][] var9 = is;
	            int var10 = is.length;

	            for(int var11 = 0; var11 < var10; ++var11) {
	               int[] js = var9[var11];
	               mutable.set(blockPos.getX() + js[0], blockPos.getY(), blockPos.getZ() + js[1]);
	               double d = this.world.getCollisionHeightAt(mutable);
	               if (Dismounting.canDismountInBlock(d)) {
	                  Vec3d vec3d = Vec3d.ofCenter(mutable, d);
	                  if (Dismounting.canPlaceEntityAt(this.world, passenger, box.offset(vec3d))) {
	                     passenger.setPose(entityPose);
	                     return vec3d;
	                  }
	               }
	            }
	         }

	         return super.updatePassengerForDismount(passenger);
	      }
	   }

	   public void onStruckByLightning(LightningEntity lightning) {
	      if (this.world.getDifficulty() != Difficulty.PEACEFUL) {
	         ZombifiedPiglinEntity zombifiedPiglinEntity = (ZombifiedPiglinEntity)EntityType.ZOMBIFIED_PIGLIN.create(this.world);
	         zombifiedPiglinEntity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
	         zombifiedPiglinEntity.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.yaw, this.pitch);
	         zombifiedPiglinEntity.setAiDisabled(this.isAiDisabled());
	         zombifiedPiglinEntity.setBaby(this.isBaby());
	         if (this.hasCustomName()) {
	            zombifiedPiglinEntity.setCustomName(this.getCustomName());
	            zombifiedPiglinEntity.setCustomNameVisible(this.isCustomNameVisible());
	         }

	         zombifiedPiglinEntity.setPersistent();
	         this.world.spawnEntity(zombifiedPiglinEntity);
	         this.remove();
	      } else {
	         super.onStruckByLightning(lightning);
	      }

	   }

	   public void travel(Vec3d movementInput) {
	      this.travel(this, this.saddledComponent, movementInput);
	   }

	   public float getSaddledSpeed() {
	      return (float)this.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED) * 0.225F;
	   }

	   public void setMovementInput(Vec3d movementInput) {
	      super.travel(movementInput);
	   }

	   public boolean consumeOnAStickItem() {
	      return this.saddledComponent.boost(this.getRandom());
	   }

	   public static boolean canSpawnInDark(EntityType<? extends CavePigEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
		      return !world.isSkyVisible(pos) && pos.getY() < 48 && canMobSpawn(type, world, spawnReason, pos, random);
		   }
	   
	   
	   public CavePigEntity createChild(PassiveEntity passiveEntity) {
	      return (CavePigEntity)CaveUpdate.CAVE_PIG.create(this.world);
	   }

	   public boolean isBreedingItem(ItemStack stack) {
	      return BREEDING_INGREDIENT.test(stack);
	   }

	   @Environment(EnvType.CLIENT)
	   public Vec3d method_29919() {
	      return new Vec3d(0.0D, (double)(0.6F * this.getStandingEyeHeight()), (double)(this.getWidth() * 0.4F));
	   }

	   static {
	      SADDLED = DataTracker.registerData(PigEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	      BOOST_TIME = DataTracker.registerData(PigEntity.class, TrackedDataHandlerRegistry.INTEGER);
	      BREEDING_INGREDIENT = Ingredient.ofItems(CaveUpdate.CAVE_CARROTS.asItem(), CaveUpdate.PLUMP_HELMET, CaveUpdate.DIMPLE_CAP);
	   }
	}
