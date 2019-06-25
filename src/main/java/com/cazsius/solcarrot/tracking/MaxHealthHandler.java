package com.cazsius.solcarrot.tracking;

import com.cazsius.solcarrot.SOLCarrot;
import com.cazsius.solcarrot.SOLCarrotConfig;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

import javax.annotation.Nullable;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = SOLCarrot.MOD_ID)
public final class MaxHealthHandler {
	private static final UUID MILESTONE_HEALTH_MODIFIER_ID = UUID.fromString("b20d3436-0d39-4868-96ab-d0a4856e68c6");
	
	@SubscribeEvent
	public static void onPlayerLogin(PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
		
		updateFoodHPModifier(player);
	}
	
	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.Clone event) {
		AttributeModifier prevModifier = getHealthModifier(event.getOriginal());
		if (prevModifier == null) return;
		
		updateHealthModifier(event.getEntityPlayer(), prevModifier);
	}
	
	/** @return whether or not the player reached a new milestone in this update */
	public static boolean updateFoodHPModifier(EntityPlayer player) {
		if (player.world.isRemote) return false;
		
		AttributeModifier prevModifier = getHealthModifier(player);
		
		int healthPenalty = 2 * (SOLCarrotConfig.baseHearts - 10);
		
		ProgressInfo progressInfo = FoodList.get(player).getProgressInfo();
		int milestonesAchieved = progressInfo.milestonesAchieved();
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
			
			return true;
		} else {
			return false;
		}
	}
	
	@Nullable
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
	
	private MaxHealthHandler() {}
}
