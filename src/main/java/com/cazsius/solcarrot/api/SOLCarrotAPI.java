package com.cazsius.solcarrot.api;

import com.cazsius.solcarrot.tracking.CapabilityHandler;
import com.cazsius.solcarrot.tracking.FoodList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

/**
 Provides a stable API for interfacing with Spice of Life: Carrot Edition.
 */
public final class SOLCarrotAPI {
	@CapabilityInject(FoodCapability.class)
	public static Capability<FoodCapability> foodCapability;
	
	private SOLCarrotAPI() {}
	
	/**
	 Retrieves the {@link com.cazsius.solcarrot.api.FoodCapability} for the given player.
	 */
	public static FoodCapability getFoodCapability(EntityPlayer player) {
		return FoodList.get(player);
	}
	
	/**
	 Synchronizes the food list for the given player to the client, updating their max health in the process.
	 */
	public static void syncFoodList(EntityPlayer player) {
		CapabilityHandler.syncFoodList(player);
	}
}
