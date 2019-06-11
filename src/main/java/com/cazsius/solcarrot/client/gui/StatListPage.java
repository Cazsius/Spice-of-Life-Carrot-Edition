package com.cazsius.solcarrot.client.gui;

import com.cazsius.solcarrot.client.gui.elements.UIBox;
import com.cazsius.solcarrot.client.gui.elements.UIImage;
import com.cazsius.solcarrot.tracking.ProgressInfo;

import java.awt.*;

import static com.cazsius.solcarrot.lib.Localization.localized;

final class StatListPage extends Page {
	StatListPage(FoodData foodData, Rectangle frame) {
		super(frame, localized("gui", "food_book.stats"));
		
		ProgressInfo progressInfo = foodData.progressInfo;
		ProgressGraph progressGraph = new ProgressGraph(foodData, getCenterX(), getMinY() + 60);
		children.add(progressGraph);
		
		int iconHeight = 11;
		
		mainStack.addChild(new UIBox(progressGraph.frame, new Color(0, 0, 0, 0))); // invisible placeholder box
		
		mainStack.addChild(makeSeparatorLine());
		
		UIImage carrotIcon = new UIImage(GuiFoodBook.carrotImage);
		carrotIcon.setHeight(iconHeight);
		
		String foodsTasted;
		if (progressInfo.shouldShowUneatenFoods) {
			foodsTasted = fraction(progressInfo.foodsEaten, foodData.validFoods.size());
		} else {
			foodsTasted = "" + progressInfo.foodsEaten;
		}
		
		mainStack.addChild(statWithIcon(carrotIcon, foodsTasted, localized("gui", "food_book.stats.foods_tasted")));
		
		mainStack.addChild(makeSeparatorLine());
		
		UIImage heartIcon = new UIImage(GuiFoodBook.heartImage);
		heartIcon.setHeight(iconHeight);
		
		String heartsGained = fraction(
			progressInfo.heartsPerMilestone * progressInfo.milestonesAchieved(),
			progressInfo.heartsPerMilestone * progressInfo.milestones.length
		);
		
		mainStack.addChild(statWithIcon(heartIcon, heartsGained, localized("gui", "food_book.stats.hearts_gained")));
		
		mainStack.addChild(makeSeparatorLine());
		
		updateMainStack();
	}
}
