package com.cazsius.solcarrot.client.gui;

import com.cazsius.solcarrot.client.FoodItemStacks;
import com.cazsius.solcarrot.client.gui.elements.UIElement;
import com.cazsius.solcarrot.client.gui.elements.UIImage;

import java.awt.*;

import static com.cazsius.solcarrot.lib.Localization.localized;

final class ConfigInfoPage extends Page {
	ConfigInfoPage(FoodData foodData, Rectangle frame) {
		super(frame, localized("gui", "food_book.config"));
		
		int iconHeight = 11;
		
		int totalFoods = FoodItemStacks.getAllFoods().size();
		int validFoods = foodData.validFoods.size();
		int cheapFoods = totalFoods - validFoods;
		int eatenCheapFoods = (int) foodData.foodCapability.getFoodList().stream()
			.filter(food -> !foodData.progressInfo.shouldCount(food.getItemStack()))
			.count();
		
		{
			UIImage drumstickIcon = new UIImage(GuiFoodBook.drumstickImage);
			drumstickIcon.setHeight(iconHeight);
			
			int minValue = foodData.progressInfo.minimumFoodValue;
			String minValueDesc = "" + (minValue / 2);
			if (minValue % 2 == 1) {
				minValueDesc += ".5";
			}
			
			UIElement minValueStat = statWithIcon(drumstickIcon, minValueDesc, localized("gui", "food_book.config.minimum_food_value"));
			minValueStat.tooltip = localized("gui", "food_book.config.tooltip.minimum_food_value");
			mainStack.addChild(minValueStat);
		}
		
		{
			UIImage cheapIcon = new UIImage(GuiFoodBook.spiderEyeImage);
			cheapIcon.setWidth(12);
			cheapIcon.setHeight(iconHeight);
			
			UIElement cheapStat = statWithIcon(
				cheapIcon,
				fraction(eatenCheapFoods, cheapFoods),
				localized("gui", "food_book.config.eaten_cheap_foods")
			);
			cheapStat.tooltip = localized("gui", "food_book.config.tooltip.eaten_cheap_foods", cheapFoods, eatenCheapFoods);
			mainStack.addChild(cheapStat);
		}
		
		mainStack.addChild(makeSeparatorLine());
		
		UIImage blacklistIcon = new UIImage(GuiFoodBook.blacklistImage);
		blacklistIcon.setHeight(iconHeight);
		
		int blacklisted = FoodItemStacks.getAllFoodsIgnoringBlacklist().size() - totalFoods;
		UIElement blacklistStat = statWithIcon(blacklistIcon, "" + blacklisted, localized("gui", "food_book.config.blacklist"));
		blacklistStat.tooltip = localized("gui", "food_book.config.tooltip.blacklist");
		mainStack.addChild(blacklistStat);
		
		mainStack.addChild(makeSeparatorLine());
		
		updateMainStack();
	}
}
