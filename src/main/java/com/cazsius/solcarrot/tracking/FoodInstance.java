package com.cazsius.solcarrot.tracking;

import com.cazsius.solcarrot.SOLCarrot;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;
import java.util.Optional;

public record FoodInstance(Item item) {

	@Nullable
	public static FoodInstance decode(String encoded) {
		ResourceLocation name = ResourceLocation.tryParse(encoded);
		
		// TODO it'd be nice to store (and maybe even count) references to missing items, in case the mod is added back in later
		Item item = BuiltInRegistries.ITEM.getValue(name);
		if (item == null) {
            SOLCarrot.LOGGER.warn("attempting to load item into food list that is no longer registered: {} (removing from list)", encoded);
			return null;
		}
		
		if (item.getDefaultInstance().get(DataComponents.FOOD) == null) {
            SOLCarrot.LOGGER.warn("attempting to load item into food list that is no longer edible: {} (ignoring in case it becomes edible again later)", encoded);
		}
		
		return new FoodInstance(item);
	}
	
	@Nullable
	public String encode() {
		return Optional.ofNullable(BuiltInRegistries.ITEM.getKey(item))
			.map(ResourceLocation::toString)
			.orElse(null);
	}
	
	@Override
	public int hashCode() {
		return item.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof FoodInstance other)) return false;
		
		return item.equals(other.item);
	}
	
	public Item getItem() {
		return item;
	}
}
