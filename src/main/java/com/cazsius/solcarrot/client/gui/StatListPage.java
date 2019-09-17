package com.cazsius.solcarrot.client.gui;

import com.cazsius.solcarrot.SOLCarrotConfig;
import com.cazsius.solcarrot.client.gui.elements.UIBox;
import com.cazsius.solcarrot.tracking.ProgressInfo;

import java.awt.*;

import static com.cazsius.solcarrot.lib.Localization.localized;

final class StatListPage extends Page {
	StatListPage(FoodData foodData, Rectangle frame) {
		super(frame, localized("gui", "food_book.stats"));
		
		ProgressInfo progressInfo = foodData.progressInfo;
		ProgressGraph progressGraph = new ProgressGraph(foodData, getCenterX(), (int) mainStack.frame.getMinY() + 43);
		children.add(progressGraph);
		
		mainStack.addChild(new UIBox(progressGraph.frame, new Color(0, 0, 0, 0))); // invisible placeholder box
		
		mainStack.addChild(makeSeparatorLine());
		
		String foodsTasted;
		if (SOLCarrotConfig.shouldShowUneatenFoods()) {
			foodsTasted = fraction(progressInfo.foodsEaten, foodData.validFoods.size());
		} else {
			foodsTasted = "" + progressInfo.foodsEaten;
		}
		
		mainStack.addChild(statWithIcon(
			icon(FoodBookScreen.carrotImage),
			foodsTasted,
			localized("gui", "food_book.stats.foods_tasted")
		));
		
		mainStack.addChild(makeSeparatorLine());
		
		int heartsPerMilestone = SOLCarrotConfig.getHeartsPerMilestone();
		String heartsGained = fraction(
			heartsPerMilestone * progressInfo.milestonesAchieved(),
			heartsPerMilestone * SOLCarrotConfig.getMilestoneCount()
		);
		
		mainStack.addChild(statWithIcon(
			icon(FoodBookScreen.heartImage),
			heartsGained,
			localized("gui", "food_book.stats.hearts_gained")
		));
		
		mainStack.addChild(makeSeparatorLine());
		
		updateMainStack();
	}
}
