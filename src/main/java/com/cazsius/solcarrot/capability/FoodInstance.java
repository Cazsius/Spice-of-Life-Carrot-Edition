package com.cazsius.solcarrot.capability;

import net.minecraft.item.Item;

public class FoodInstance {
	private int meta;
	private Item i;
	
	public FoodInstance(Item i, int meta) {
		this.i = i;
		this.meta = meta;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((i == null) ? 0 : i.hashCode());
		result = prime * result + meta;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof FoodInstance))
			return false;
		FoodInstance other = (FoodInstance) obj;
		
		if (i == null) {
			return other.i == null; // both null
		} else {
			return meta == other.meta && i.equals(other.i);
		}
	}
	
	public Item item() {
		return i;
	}
	
	public int meta() {
		return meta;
	}
	
}
