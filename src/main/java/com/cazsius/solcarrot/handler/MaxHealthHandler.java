/*******************************************************************************
 * Special thanks to the Biomes O' Plenty Team, whose open source "Tough as
 * nails" mod taught me to change player's max health. Below is a heavily
 * modified version of their code. 
 ******************************************************************************/
package com.cazsius.solcarrot.handler;

import com.cazsius.solcarrot.SOLCarrotConfig;
import com.cazsius.solcarrot.lib.ProgressInfo;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

import java.util.UUID;

@Mod.EventBusSubscriber
public class MaxHealthHandler {
	private static final UUID MILESTONE_HEALTH_MODIFIER_ID = UUID.fromString("b20d3436-0d39-4868-96ab-d0a4856e68c6");
	
	@SubscribeEvent
	public static void onPlayerLogin(PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
		
		if (!player.world.isRemote) {
			updateFoodHPModifier(player);
		}
	}
	
	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.Clone event) {
		updateHealthModifier(event.getEntityPlayer(), getHealthModifier(event.getOriginal()));
	}
	
	public static void updateFoodHPModifier(EntityPlayer player) {
		AttributeModifier prevModifier = getHealthModifier(player);
		
		int healthPenalty = 2 * (SOLCarrotConfig.baseHearts - 10);
		
		int milestonesAchieved = ProgressInfo.getProgressInfo(player).milestonesAchieved;
		int addedHealthFromFood = milestonesAchieved * 2 * SOLCarrotConfig.heartsPerMilestone;
		
		double totalHealthModifier = healthPenalty + addedHealthFromFood;
		
		if (prevModifier == null || prevModifier.getAmount() != totalHealthModifier) {
			AttributeModifier modifier = new AttributeModifier(
					MILESTONE_HEALTH_MODIFIER_ID,
					"Health Gained from Trying New Foods",
					totalHealthModifier,
					0
			);
			
			float oldMax = player.getMaxHealth();
			updateHealthModifier(player, modifier);
			
			// adjust current health proportionally to increase in max health
			player.setHealth(player.getHealth() * player.getMaxHealth() / oldMax);
		}
	}
	
	private static AttributeModifier getHealthModifier(EntityPlayer player) {
		return maxHealthAttribute(player).getModifier(MILESTONE_HEALTH_MODIFIER_ID);
	}
	
	private static void updateHealthModifier(EntityPlayer player, AttributeModifier modifier) {
		IAttributeInstance attribute = maxHealthAttribute(player);
		attribute.removeModifier(modifier);
		attribute.applyModifier(modifier);
	}
	
	private static IAttributeInstance maxHealthAttribute(EntityPlayer player) {
		return player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
	}
}
