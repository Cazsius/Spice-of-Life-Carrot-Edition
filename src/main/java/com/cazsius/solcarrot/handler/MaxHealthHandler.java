/*******************************************************************************
 * Special thanks to the Biomes O' Plenty Team, whose open source "Tough as
 * nails" mod taught me to change player's max health. Below is a heavily
 * modified version of their code. 
 ******************************************************************************/
package com.cazsius.solcarrot.handler;

import java.util.UUID;

import com.cazsius.solcarrot.capability.FoodCapability;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class MaxHealthHandler {
	public static final UUID MILESTONE_HEALTH_MODIFIER_ID = UUID.fromString("b20d3436-0d39-4868-96ab-d0a4856e68c6");
	
	@SubscribeEvent
	public static void onPlayerLogin(PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
		World world = player.world;

		if (!world.isRemote) {
			updateFoodHPModifier(player);
		}
	}

	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.Clone event) {
		IAttributeInstance oldMaxHealthInstance = event.getOriginal().getAttributeMap()
				.getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH);
		AttributeModifier modifier = oldMaxHealthInstance.getModifier(MILESTONE_HEALTH_MODIFIER_ID);
		Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();
		multimap.put(SharedMonsterAttributes.MAX_HEALTH.getName(), modifier);
		event.getEntityPlayer().getAttributeMap().applyAttributeModifiers(multimap);
	}

	public static void updateFoodHPModifier(EntityPlayer player) {
		IAttributeInstance maxHealthInstance = player.getAttributeMap()
				.getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH);
		AttributeModifier modifier = maxHealthInstance.getModifier(MILESTONE_HEALTH_MODIFIER_ID);

		int startingHealth = getStartingHearts();
		double healthPenalty = -20 + startingHealth * 2;

		double addedHealthFromFood = getFoodMilestoneHearts(player) * 2 * HandlerConfiguration.getHeartsPerMilestone();
		double overallHealthModifier = healthPenalty + addedHealthFromFood;

		if (modifier == null || modifier.getAmount() != overallHealthModifier) {
			Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();
			modifier = new AttributeModifier(MILESTONE_HEALTH_MODIFIER_ID, "Food Milestone Health Mod",
					overallHealthModifier, 0);
			multimap.put(SharedMonsterAttributes.MAX_HEALTH.getName(), modifier);
			player.getAttributeMap().applyAttributeModifiers(multimap);
			// This should never happen, but let's be defensive.
			if (player.getHealth() > player.getMaxHealth()) {
				player.setHealth(player.getMaxHealth());
			}
		}
	}

	public static int getMaxHearts() {
		return HandlerConfiguration.getDefaultHeartCount() + HandlerConfiguration.getNumMilestones();
	}

	public static int getStartingHearts() {
		return HandlerConfiguration.getDefaultHeartCount();
	}

	public static int getFoodMilestoneHearts(EntityPlayer player) {
		FoodCapability food = player.getCapability(FoodCapability.FOOD_CAPABILITY, null);
		int foodsEaten = food.getCount();
		int milestones = 0;
		int[] milestoneArray = HandlerConfiguration.getMilestoneArray();
		while (milestones < milestoneArray.length && milestoneArray[milestones] <= foodsEaten) {
			milestones++;
		}
		return milestones;
	}

}
