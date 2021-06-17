package com.cazsius.solcarrot.tracking;

import com.cazsius.solcarrot.SOLCarrotConfig;
import com.cazsius.solcarrot.api.FoodCapability;
import com.cazsius.solcarrot.api.SOLCarrotAPI;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.*;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
public final class FoodList implements FoodCapability {
	private static final String NBT_KEY_FOOD_LIST = "foodList";
	
	public static FoodList get(PlayerEntity player) {
		return (FoodList) player.getCapability(SOLCarrotAPI.foodCapability)
			.orElseThrow(FoodListNotFoundException::new);
	}
	
	private final Set<FoodInstance> foods = new HashSet<>();
	
	@Nullable
	private ProgressInfo cachedProgressInfo;
	
	public FoodList() {}
	
	private final LazyOptional<FoodList> capabilityOptional = LazyOptional.of(() -> this);
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
		return capability == SOLCarrotAPI.foodCapability ? capabilityOptional.cast() : LazyOptional.empty();
	}
	
	/** used for persistent storage */
	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT tag = new CompoundNBT();
		
		ListNBT list = new ListNBT();
		foods.stream()
			.map(FoodInstance::encode)
			.filter(Objects::nonNull)
			.map(StringNBT::valueOf)
			.forEach(list::add);
		tag.put(NBT_KEY_FOOD_LIST, list);
		
		return tag;
	}
	
	/** used for persistent storage */
	@Override
	public void deserializeNBT(CompoundNBT tag) {
		ListNBT list = tag.getList(NBT_KEY_FOOD_LIST, Constants.NBT.TAG_STRING);
		
		foods.clear();
		list.stream()
			.map(nbt -> (StringNBT) nbt)
			.map(StringNBT::getAsString)
			.map(FoodInstance::decode)
			.filter(Objects::nonNull)
			.forEach(foods::add);
		
		invalidateProgressInfo();
	}
	
	/** @return true if the food was not previously known, i.e. if a new food has been tried */
	public boolean addFood(Item food) {
		boolean wasAdded = foods.add(new FoodInstance(food)) && SOLCarrotConfig.shouldCount(food);
		invalidateProgressInfo();
		return wasAdded;
	}
	
	@Override
	public boolean hasEaten(Item food) {
		if (!food.isEdible()) return false;
		return foods.contains(new FoodInstance(food));
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
	
	public static final class Storage implements Capability.IStorage<FoodCapability> {
		@Override
		public INBT writeNBT(Capability<FoodCapability> capability, FoodCapability instance, Direction side) {
			return instance.serializeNBT();
		}
		
		@Override
		public void readNBT(Capability<FoodCapability> capability, FoodCapability instance, Direction side, INBT tag) {
			instance.deserializeNBT((CompoundNBT) tag);
		}
	}
	
	public static class FoodListNotFoundException extends RuntimeException {
		public FoodListNotFoundException() {
			super("Player must have food capability attached, but none was found.");
		}
	}
}
