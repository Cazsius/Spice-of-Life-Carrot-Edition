package com.cazsius.solcarrot.tracking;

import com.cazsius.solcarrot.SOLCarrot;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

public final class FoodInstance {
	public final Item item;
	
	public FoodInstance(Item item) {
		this.item = item;
	}
	
	@Nullable
	public static FoodInstance decode(String encoded) {
		ResourceLocation name = new ResourceLocation(encoded);
		
		// TODO it'd be nice to store (and maybe even count) references to missing items, in case the mod is added back in later
		Item item = ForgeRegistries.ITEMS.getValue(name);
		if (item == null) {
			SOLCarrot.LOGGER.warn("attempting to load item into food list that is no longer registered: " + encoded + " (removing from list)");
			return null;
		}
		
		if (!item.isEdible()) {
			SOLCarrot.LOGGER.warn("attempting to load item into food list that is no longer edible: " + encoded + " (ignoring in case it becomes edible again later)");
		}
		
		return new FoodInstance(item);
	}
	
	@Nullable
	public String encode() {
		return Optional.ofNullable(ForgeRegistries.ITEMS.getKey(item))
			.map(ResourceLocation::toString)
			.orElse(null);
	}
	
	@Override
	public int hashCode() {
		return item.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof FoodInstance)) return false;
		FoodInstance other = (FoodInstance) obj;
		
		return item.equals(other.item);
	}
	
	public Item getItem() {
		return item;
	}
}
