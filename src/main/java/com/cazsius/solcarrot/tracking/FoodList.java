package com.cazsius.solcarrot.tracking;

import com.cazsius.solcarrot.SOLCarrot;
import com.cazsius.solcarrot.SOLCarrotConfig;
import com.cazsius.solcarrot.api.FoodCapability;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public final class FoodList implements FoodCapability {
	private static final String NBT_KEY_FOOD_LIST = "foodList";
	public static final MapCodec<FoodList> CODEC = RecordCodecBuilder.mapCodec(instance ->
			instance.group(
					Item.CODEC.listOf().validate(
									foodItem -> {
										for (Holder<Item> item : foodItem) {
											if (item.value().getDefaultInstance().get(DataComponents.FOOD) == null) {
												SOLCarrot.LOGGER.warn("attempting to load item into food list that is no longer edible: {} (ignoring in case it becomes edible again later)", item.unwrapKey().orElseThrow().identifier());
											}
										}

										return DataResult.success(foodItem);
									}
							).fieldOf(NBT_KEY_FOOD_LIST)
							.forGetter(FoodList::getEatenFoods)
			).apply(instance, FoodList::new)
	);

	public static FoodList get(Player player) {
		return player.getData(SOLCarrot.FOOD_ATTACHMENT);
	}

	private final List<Holder<Item>> foods = new ArrayList<>();

	@Nullable
	private ProgressInfo cachedProgressInfo;

	public FoodList(List<Holder<Item>> foodList) {
		this.foods.addAll(foodList);
		invalidateProgressInfo();
	}

	public FoodList() {
		this(new ArrayList<>());
	}

	/**
	 * Used to sync the food list from the server to the client.
	 * @param foodList the list of foods that the player has eaten.
	 */
	public void readFoods(List<Holder<Item>> foodList) {
		foods.clear();
		foods.addAll(foodList);
		invalidateProgressInfo();
	}

	/** @return true if the food was not previously known, i.e. if a new food has been tried */
	public boolean addFood(ItemStack food) {
		if (foods.stream().anyMatch(holder -> holder.is(food.typeHolder())))
			return false;
		boolean wasAdded = foods.add(food.typeHolder()) && SOLCarrotConfig.shouldCount(food);
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
		return foods.stream().anyMatch(holder -> holder.is(food.typeHolder()));
	}

	public void clearFood() {
		foods.clear();
		invalidateProgressInfo();
	}

	public List<Holder<Item>> getEatenFoods() {
		return new ArrayList<>(foods);
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
