package com.cazsius.solcarrot.client.gui;

import com.cazsius.solcarrot.client.FoodItemStacks;
import com.cazsius.solcarrot.client.gui.elements.UIElement;
import com.cazsius.solcarrot.client.gui.elements.UIImage;
import com.cazsius.solcarrot.tracking.FoodInstance;
import com.cazsius.solcarrot.tracking.ProgressInfo;

import java.awt.*;

import static com.cazsius.solcarrot.lib.Localization.localized;

final class ConfigInfoPage extends Page {
	ConfigInfoPage(FoodData foodData, Rectangle frame) {
		super(frame, localized("gui", "food_book.config"));
		
		ProgressInfo.ConfigInfo configInfo = foodData.progressInfo.configInfo;
		
		int totalFoods = FoodItemStacks.getAllFoods().size();
		int validFoods = foodData.validFoods.size();
		int cheapFoods = totalFoods - validFoods;
		int eatenCheapFoods = (int) foodData.foodList.getEatenFoods().stream()
			.map(FoodInstance::getItemStack)
			.filter(food -> configInfo.isAllowed(food) && !configInfo.isHearty(food))
			.count();
		
		{
			UIImage drumstickIcon = icon(GuiFoodBook.drumstickImage);
			
			int minValue = configInfo.minimumFoodValue;
			String minValueDesc = "" + (minValue / 2);
			if (minValue % 2 == 1) {
				minValueDesc += ".5";
			}
			
			UIElement minValueStat = statWithIcon(drumstickIcon, minValueDesc, localized("gui", "food_book.config.minimum_food_value"));
			minValueStat.tooltip = localized("gui", "food_book.config.tooltip.minimum_food_value");
			mainStack.addChild(minValueStat);
		}
		
		{
			UIImage cheapIcon = icon(GuiFoodBook.spiderEyeImage);
			cheapIcon.setWidth(12);
			
			UIElement cheapStat = statWithIcon(
				cheapIcon,
				fraction(eatenCheapFoods, cheapFoods),
				localized("gui", "food_book.config.eaten_cheap_foods")
			);
			cheapStat.tooltip = localized("gui", "food_book.config.tooltip.eaten_cheap_foods", cheapFoods, eatenCheapFoods);
			mainStack.addChild(cheapStat);
		}
		
		mainStack.addChild(makeSeparatorLine());
		
		{
			boolean hasWhitelist = configInfo.hasWhitelist();
			String listKey = hasWhitelist ? "whitelist" : "blacklist";
			
			UIImage listIcon = icon(hasWhitelist ? GuiFoodBook.whitelistImage : GuiFoodBook.blacklistImage);
			
			int allFoods = FoodItemStacks.getAllFoodsIgnoringBlacklist().size();
			int allowedFoods = FoodItemStacks.getAllFoods().size();
			String fraction = hasWhitelist
				? fraction(allowedFoods, allFoods)
				: fraction(allFoods - allowedFoods, allFoods);
			UIElement listStat = statWithIcon(
				listIcon,
				fraction,
				localized("gui", "food_book.config." + listKey)
			);
			listStat.tooltip = localized("gui", "food_book.config.tooltip." + listKey);
			mainStack.addChild(listStat);
		}
		
		mainStack.addChild(makeSeparatorLine());
		
		updateMainStack();
	}
}
