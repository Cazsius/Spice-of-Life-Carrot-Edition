package com.cazsius.solcarrot.api;

import com.cazsius.solcarrot.tracking.FoodList;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

/**
 Provides a stable, (strongly) simplified view of the food list.
 */
public interface FoodCapability {
	/**
	 @return how many unique foods have been eaten.
	 */
	int getEatenFoodCount();
	
	/**
	 @return whether or not the given food has been eaten.
	 */
	boolean hasEaten(ItemStack itemStack);
}
