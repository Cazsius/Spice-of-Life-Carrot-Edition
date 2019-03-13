package com.cazsius.solcarrot.client.gui;

import com.cazsius.solcarrot.capability.ProgressInfo;
import com.cazsius.solcarrot.client.gui.elements.UILabel;
import com.cazsius.solcarrot.lib.FoodItemStacks;

import java.awt.*;

import static com.cazsius.solcarrot.lib.Localization.localized;
import static com.cazsius.solcarrot.lib.Localization.localizedQuantity;

class StatListPage extends Page {
	StatListPage(Rectangle frame, ProgressInfo progressInfo) {
		super(frame, localized("gui", "food_book.stats"));
		
		ProgressGraph progressGraph = new ProgressGraph(progressInfo, getCenterX(), getMinY() + 72);
		children.add(progressGraph);
		
		String foodsTastedDescription;
		if (progressInfo.shouldShowUneatenFoods) {
			foodsTastedDescription = localized("gui", "food_book.stats.foods_tasted.fraction",
				progressInfo.foodsEaten,
				FoodItemStacks.getAllFoods().size()
			);
		} else {
			foodsTastedDescription = localizedQuantity("gui", "food_book.stats.foods_tasted",
				progressInfo.foodsEaten
			);
		}
		UILabel foodsTastedLabel = new UILabel(foodsTastedDescription);
		foodsTastedLabel.setCenterX(getCenterX());
		foodsTastedLabel.setCenterY(getMinY() + 100);
		children.add(foodsTastedLabel);
		
		UILabel heartsGainedLabel = new UILabel(localized("gui", "food_book.stats.hearts_gained",
			progressInfo.milestonesAchieved(),
			progressInfo.heartsPerMilestone * progressInfo.milestones.length
		));
		heartsGainedLabel.setCenterX(getCenterX());
		heartsGainedLabel.setCenterY(getMinY() + 120);
		children.add(heartsGainedLabel);
	}
}
