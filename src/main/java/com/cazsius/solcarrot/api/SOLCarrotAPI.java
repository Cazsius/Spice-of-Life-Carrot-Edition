package com.cazsius.solcarrot.api;

import com.cazsius.solcarrot.SOLCarrot;
import com.cazsius.solcarrot.tracking.CapabilityHandler;
import com.cazsius.solcarrot.tracking.FoodList;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.capabilities.EntityCapability;

/**
 * Provides a stable API for interfacing with Spice of Life: Carrot Edition.
 */
public final class SOLCarrotAPI {
	public static final EntityCapability<FoodCapability, Void> foodCapability = EntityCapability.createVoid(
			SOLCarrot.resourceLocation("food"), FoodCapability.class);

	private SOLCarrotAPI() {
	}

	/**
	 * Retrieves the {@link FoodCapability} for the given player.
	 */
	public static FoodCapability getFoodCapability(Player player) {
		return FoodList.get(player);
	}

	/**
	 * Synchronizes the food list for the given player to the client, updating their max health in the process.
	 */
	public static void syncFoodList(Player player) {
		CapabilityHandler.syncFoodList(player);
	}
}
