package com.cazsius.solcarrot.client.gui;

import com.cazsius.solcarrot.SOLCarrotConfig;
import com.cazsius.solcarrot.client.FoodItems;
import com.cazsius.solcarrot.tracking.FoodList;
import com.cazsius.solcarrot.tracking.ProgressInfo;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** collects the information the food book needs in a convenient single location */
@OnlyIn(Dist.CLIENT)
final class FoodData {
	public final FoodList foodList;
	public final ProgressInfo progressInfo;
	public final List<Item> validFoods;
	public final List<Item> eatenFoods;
	public final List<Item> uneatenFoods;
	
	FoodData(FoodList foodList) {
		this.foodList = foodList;
		this.progressInfo = foodList.getProgressInfo();
		this.validFoods = FoodItems.getAllFoods().stream()
			.filter(SOLCarrotConfig::isHearty)
			.collect(Collectors.toList());
		this.eatenFoods = new ArrayList<>();
		this.uneatenFoods = new ArrayList<>();
		for (Item food : validFoods) {
			(foodList.hasEaten(food) ? eatenFoods : uneatenFoods).add(food);
		}
	}
}
