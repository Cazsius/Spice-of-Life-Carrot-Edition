package com.cazsius.solcarrot.tracking;

import com.cazsius.solcarrot.SOLCarrot;
import com.cazsius.solcarrot.SOLCarrotConfig;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import javax.annotation.Nullable;
import java.util.Objects;

@EventBusSubscriber(modid = SOLCarrot.MOD_ID)
public final class MaxHealthHandler {
	private static final boolean HAS_FIRST_AID = ModList.get().isLoaded("firstaid");
	private static final Identifier MILESTONE_HEALTH_MODIFIER_ID = SOLCarrot.resourceLocation("health_gained");

	@SubscribeEvent
	public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		updateFoodHPModifier(event.getEntity());
	}

	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.Clone event) {
		var prevModifier = getHealthModifier(event.getOriginal());
		if (prevModifier == null) return;

		updateHealthModifier(event.getEntity(), prevModifier);
	}

	/**
	 * @return whether the player reached a new milestone in this update
	 */
	public static boolean updateFoodHPModifier(Player player) {
		var prevModifier = getHealthModifier(player);

		int healthPenalty = 2 * (SOLCarrotConfig.getBaseHearts() - 10);

		ProgressInfo progressInfo = FoodList.get(player).getProgressInfo();
		int milestonesAchieved = progressInfo.milestonesAchieved();
		int addedHealthFromFood = milestonesAchieved * 2 * SOLCarrotConfig.getHeartsPerMilestone();

		double totalHealthModifier = healthPenalty + addedHealthFromFood;
		boolean hasChanged = prevModifier == null || prevModifier.amount() != totalHealthModifier;

		if (!player.level().isClientSide()) {
			AttributeModifier modifier = new AttributeModifier(
					MILESTONE_HEALTH_MODIFIER_ID,
					totalHealthModifier,
					AttributeModifier.Operation.ADD_VALUE
			);

			updateHealthModifier(player, modifier);
		}

		return hasChanged;
	}

	@Nullable
	private static AttributeModifier getHealthModifier(Player player) {
		return maxHealthAttribute(player).getModifier(MILESTONE_HEALTH_MODIFIER_ID);
	}

	private static void updateHealthModifier(Player player, AttributeModifier modifier) {
		var oldMax = player.getMaxHealth();

		var attribute = maxHealthAttribute(player);
		attribute.removeModifier(modifier);
		attribute.addPermanentModifier(modifier);

		var newHealth = player.getHealth() * player.getMaxHealth() / oldMax;
		if (!HAS_FIRST_AID) { // This workaround breaks First Aid because it tries to distribute the health change and may kill the player that way.
			// because apparently it doesn't update unless changed
			player.setHealth(1f);
		}
		// adjust current health proportionally to increase in max health
		player.setHealth(newHealth);
	}

	private static AttributeInstance maxHealthAttribute(Player player) {
		return Objects.requireNonNull(player.getAttribute(Attributes.MAX_HEALTH));
	}

	private MaxHealthHandler() {
	}
}
