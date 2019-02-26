package com.cazsius.solcarrot.capability;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class FoodInstance {
	public final Item item;
	public final int metadata;
	
	public FoodInstance(Item item, int metadata) {
		this.item = item;
		this.metadata = metadata;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((item == null) ? 0 : item.hashCode());
		result = prime * result + metadata;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof FoodInstance))
			return false;
		FoodInstance other = (FoodInstance) obj;
		
		return ItemStack.areItemStacksEqual(getItemStack(), other.getItemStack());
	}
	
	public ItemStack getItemStack() {
		return new ItemStack(item, 1, metadata);
	}
}
