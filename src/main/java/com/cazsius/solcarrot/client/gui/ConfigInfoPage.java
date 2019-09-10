package com.cazsius.solcarrot.client.gui;

import com.cazsius.solcarrot.SOLCarrotConfig;
import com.cazsius.solcarrot.client.FoodItems;
import com.cazsius.solcarrot.client.gui.elements.UIElement;
import com.cazsius.solcarrot.client.gui.elements.UIImage;
import com.cazsius.solcarrot.tracking.FoodInstance;

import java.awt.*;

import static com.cazsius.solcarrot.lib.Localization.localized;

final class ConfigInfoPage extends Page {
	ConfigInfoPage(FoodData foodData, Rectangle frame) {
		super(frame, localized("gui", "food_book.config"));
		
		int totalFoods = FoodItems.getAllFoods().size();
		int validFoods = foodData.validFoods.size();
		int cheapFoods = totalFoods - validFoods;
		int eatenCheapFoods = (int) foodData.foodList.getEatenFoods().stream()
			.map(FoodInstance::getItem)
			.filter(food -> SOLCarrotConfig.isAllowed(food) && !SOLCarrotConfig.isHearty(food))
			.count();
		
		{
			UIImage drumstickIcon = icon(GuiFoodBook.drumstickImage);
			
			int minValue = SOLCarrotConfig.minimumFoodValue;
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
			boolean hasWhitelist = SOLCarrotConfig.hasWhitelist();
			String listKey = hasWhitelist ? "whitelist" : "blacklist";
			
			UIImage listIcon = icon(hasWhitelist ? GuiFoodBook.whitelistImage : GuiFoodBook.blacklistImage);
			
			int allFoods = FoodItems.getAllFoodsIgnoringBlacklist().size();
			int allowedFoods = FoodItems.getAllFoods().size();
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
