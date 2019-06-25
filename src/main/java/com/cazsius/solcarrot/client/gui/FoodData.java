package com.cazsius.solcarrot.client.gui;

import com.cazsius.solcarrot.client.FoodItemStacks;
import com.cazsius.solcarrot.tracking.FoodList;
import com.cazsius.solcarrot.tracking.ProgressInfo;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** collects the information the food book needs in a convenient single location */
@SideOnly(Side.CLIENT)
final class FoodData {
	public final FoodList foodList;
	public final ProgressInfo progressInfo;
	public final List<ItemStack> validFoods;
	public final List<ItemStack> eatenFoods;
	public final List<ItemStack> uneatenFoods;
	
	FoodData(FoodList foodList) {
		this.foodList = foodList;
		this.progressInfo = foodList.getProgressInfo();
		this.validFoods = FoodItemStacks.getAllFoods().stream()
			.filter(progressInfo.configInfo::isHearty)
			.collect(Collectors.toList());
		this.eatenFoods = new ArrayList<>();
		this.uneatenFoods = new ArrayList<>();
		for (ItemStack food : validFoods) {
			(foodList.hasEaten(food) ? eatenFoods : uneatenFoods).add(food);
		}
	}
}
