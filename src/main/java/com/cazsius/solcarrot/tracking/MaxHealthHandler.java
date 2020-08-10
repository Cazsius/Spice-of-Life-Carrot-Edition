package com.cazsius.solcarrot.tracking;

import com.cazsius.solcarrot.SOLCarrot;
import com.cazsius.solcarrot.SOLCarrotConfig;
import net.minecraft.entity.ai.attributes.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = SOLCarrot.MOD_ID)
public final class MaxHealthHandler {
	private static final UUID MILESTONE_HEALTH_MODIFIER_ID = UUID.fromString("b20d3436-0d39-4868-96ab-d0a4856e68c6");
	
	@SubscribeEvent
	public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		updateFoodHPModifier(event.getPlayer());
	}
	
	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.Clone event) {
		AttributeModifier prevModifier = getHealthModifier(event.getOriginal());
		if (prevModifier == null) return;
		
		updateHealthModifier(event.getPlayer(), prevModifier);
	}
	
	/** @return whether or not the player reached a new milestone in this update */
	public static boolean updateFoodHPModifier(PlayerEntity player) {
		if (player.world.isRemote) return false;
		
		AttributeModifier prevModifier = getHealthModifier(player);
		
		int healthPenalty = 2 * (SOLCarrotConfig.getBaseHearts() - 10);
		
		ProgressInfo progressInfo = FoodList.get(player).getProgressInfo();
		int milestonesAchieved = progressInfo.milestonesAchieved();
		int addedHealthFromFood = milestonesAchieved * 2 * SOLCarrotConfig.getHeartsPerMilestone();
		
		double totalHealthModifier = healthPenalty + addedHealthFromFood;
		boolean hasChanged = prevModifier == null || prevModifier.getAmount() != totalHealthModifier;
		
		AttributeModifier modifier = new AttributeModifier(
			MILESTONE_HEALTH_MODIFIER_ID,
			"Health Gained from Trying New Foods",
			totalHealthModifier,
			AttributeModifier.Operation.ADDITION
		);
		
		updateHealthModifier(player, modifier);
		
		return hasChanged;
	}
	
	@Nullable
	private static AttributeModifier getHealthModifier(PlayerEntity player) {
		return maxHealthAttribute(player).getModifier(MILESTONE_HEALTH_MODIFIER_ID);
	}
	
	private static void updateHealthModifier(PlayerEntity player, AttributeModifier modifier) {
		float oldMax = player.getMaxHealth();
		
		ModifiableAttributeInstance attribute = maxHealthAttribute(player);
		attribute.removeModifier(modifier);
		attribute.applyPersistentModifier(modifier);
		
		float newHealth = player.getHealth() * player.getMaxHealth() / oldMax;
		// because apparently it doesn't update unless changed
		player.setHealth(1f);
		// adjust current health proportionally to increase in max health
		player.setHealth(newHealth);
	}
	
	private static ModifiableAttributeInstance maxHealthAttribute(PlayerEntity player) {
		return Objects.requireNonNull(player.getAttribute(Attributes.MAX_HEALTH));
	}
	
	private MaxHealthHandler() {}
}
