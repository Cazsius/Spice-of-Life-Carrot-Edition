package com.cazsius.solcarrot.tracking;

import com.cazsius.solcarrot.SOLCarrot;
import com.cazsius.solcarrot.SOLCarrotConfig;
import com.cazsius.solcarrot.api.FoodCapability;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@ParametersAreNonnullByDefault
public final class FoodList implements FoodCapability {
	private static final String NBT_KEY_FOOD_LIST = "foodList";
	
	public static FoodList get(Player player) {
		return player.getData(SOLCarrot.FOOD_ATTACHMENT);
	}
	
	private final Set<FoodInstance> foods = new HashSet<>();
	
	@Nullable
	private ProgressInfo cachedProgressInfo;
	
	public FoodList() {}

	/** used for persistent storage */
	@Override
	public CompoundTag serializeNBT(HolderLookup.Provider provider) {
		var tag = new CompoundTag();
		
		var list = new ListTag();
		foods.stream()
			.map(FoodInstance::encode)
			.filter(Objects::nonNull)
			.map(StringTag::valueOf)
			.forEach(list::add);
		tag.put(NBT_KEY_FOOD_LIST, list);
		
		return tag;
	}

	/** used for persistent storage */
	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
		var list = tag.getListOrEmpty(NBT_KEY_FOOD_LIST);
		
		foods.clear();
		list.stream()
			.map(nbt -> (StringTag) nbt)
			.map(stringTag -> stringTag.asString().get())
			.map(FoodInstance::decode)
			.filter(Objects::nonNull)
			.forEach(foods::add);
		
		invalidateProgressInfo();
	}
	
	/** @return true if the food was not previously known, i.e. if a new food has been tried */
	public boolean addFood(ItemStack food) {
		boolean wasAdded = foods.add(new FoodInstance(food.getItem())) && SOLCarrotConfig.shouldCount(food);
		invalidateProgressInfo();
		return wasAdded;
	}

	@Override
	public boolean hasEaten(Item food) {
		return hasEaten(food.getDefaultInstance());
	}

	@Override
	public boolean hasEaten(ItemStack food) {
		if (food.get(DataComponents.FOOD) == null) return false;
		return foods.contains(new FoodInstance(food.getItem()));
	}
	
	public void clearFood() {
		foods.clear();
		invalidateProgressInfo();
	}
	
	public Set<FoodInstance> getEatenFoods() {
		return new HashSet<>(foods);
	}
	
	// TODO: is this actually desirable? it doesn't filter at all
	@Override
	public int getEatenFoodCount() {
		return foods.size();
	}
	
	public ProgressInfo getProgressInfo() {
		if (cachedProgressInfo == null) {
			cachedProgressInfo = new ProgressInfo(this);
		}
		return cachedProgressInfo;
	}
	
	public void invalidateProgressInfo() {
		cachedProgressInfo = null;
	}

	public static class FoodListNotFoundException extends RuntimeException {
		public FoodListNotFoundException() {
			super("Player must have food capability attached, but none was found.");
		}
	}
}
