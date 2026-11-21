package com.legacy.blue_skies.entities.passive;

import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import com.legacy.blue_skies.registries.SkiesItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;

public class CosmicFoxEntity extends TamableAnimal {
   public Block spawnableBlock = SkiesBlocks.lunar_grass_block;

   public CosmicFoxEntity(EntityType<? extends CosmicFoxEntity> type, Level world) {
      super(type, world);
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
      this.goalSelector.addGoal(1, new PanicGoal(this, 2.0));
      this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, Ingredient.of(new ItemLike[]{SkiesItems.crescent_fruit}), false));
      this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 2.0, 5.0F, 2.0F, false));
      this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.4));
      this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
      this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
      this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
   }

   public static Builder registerAttributes() {
      return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 6.0).add(Attributes.MOVEMENT_SPEED, 0.2F);
   }

   public void move(MoverType type, Vec3 pos) {
      if (type == MoverType.SELF) {
         if (!this.isOrderedToSit()) {
            super.move(type, pos);
         } else {
            super.move(type, new Vec3(0.0, pos.y(), 0.0));
         }
      }
   }

   public InteractionResult mobInteract(Player player, InteractionHand hand) {
      ItemStack itemstack = player.getItemInHand(hand);
      Item item = itemstack.getItem();
      if (this.level().isClientSide) {
         boolean flag = this.isOwnedBy(player) || this.isTame() || item == SkiesItems.crescent_fruit && !this.isTame();
         return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
      }

      if (this.isTame()) {
         if (this.isFood(itemstack) && this.getHealth() < this.getMaxHealth()) {
            if (!player.isCreative()) {
               itemstack.shrink(1);
            }

            this.heal(item.getFoodProperties().getNutrition());
            return InteractionResult.SUCCESS;
         } else {
            InteractionResult actionresulttype = super.mobInteract(player, hand);
            if ((!actionresulttype.consumesAction() || this.isBaby()) && this.isOwnedBy(player)) {
               this.setOrderedToSit(!this.isOrderedToSit());
               this.jumping = false;
               this.navigation.stop();
               this.setTarget((LivingEntity)null);
               return InteractionResult.SUCCESS;
            } else {
               return actionresulttype;
            }
         }
      } else if (item == SkiesItems.crescent_fruit) {
         if (!player.isCreative()) {
            itemstack.shrink(1);
         }

         if (this.random.nextInt(3) == 0 && !EventHooks.onAnimalTame(this, player)) {
            this.tame(player);
            this.navigation.stop();
            this.setTarget((LivingEntity)null);
            this.setOrderedToSit(true);
            this.level().broadcastEntityEvent(this, (byte)7);
         } else {
            this.level().broadcastEntityEvent(this, (byte)6);
         }

         return InteractionResult.SUCCESS;
      } else {
         return super.mobInteract(player, hand);
      }
   }

   protected SoundEvent getAmbientSound() {
      return SoundEvents.FOX_AMBIENT;
   }

   protected SoundEvent getHurtSound(DamageSource source) {
      return SoundEvents.FOX_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SoundEvents.FOX_DEATH;
   }

   public AgeableMob getBreedOffspring(ServerLevel p_241840_1_, AgeableMob p_241840_2_) {
      return (AgeableMob)SkiesEntityTypes.COSMIC_FOX.create(this.level());
   }

   protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
      return this.isBaby() ? this.getBbHeight() : (this.isOrderedToSit() ? 0.3F : 0.5F);
   }

   public boolean isFood(ItemStack stack) {
      return this.isTame() && stack.getItem() == SkiesItems.crescent_fruit || super.isFood(stack);
   }

   public void setTame(boolean tamed) {
      super.setTame(tamed);
      this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(tamed ? 20.0 : 6.0);
      this.setHealth(this.getMaxHealth());
   }
}
