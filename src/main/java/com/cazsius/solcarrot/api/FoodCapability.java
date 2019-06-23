package com.cazsius.solcarrot.api;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

/**
 Provides a stable, (strongly) simplified view of the food list.
 */
public interface FoodCapability extends ICapabilitySerializable<NBTBase> {
	/**
	 @return how many unique foods have been eaten.
	 */
	int getEatenFoodCount();
	
	/**
	 @return whether or not the given food has been eaten.
	 */
	boolean hasEaten(ItemStack itemStack);
}
