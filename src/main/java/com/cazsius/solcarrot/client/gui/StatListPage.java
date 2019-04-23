package com.cazsius.solcarrot.client.gui;

import com.cazsius.solcarrot.capability.ProgressInfo;
import com.cazsius.solcarrot.client.gui.elements.*;

import java.awt.*;

import static com.cazsius.solcarrot.lib.Localization.localized;

final class StatListPage extends Page {
	StatListPage(FoodData foodData, Rectangle frame) {
		super(frame, localized("gui", "food_book.stats"));
		
		ProgressInfo progressInfo = foodData.progressInfo;
		ProgressGraph progressGraph = new ProgressGraph(foodData, getCenterX(), getMinY() + 64);
		children.add(progressGraph);
		
		int majorSpacing = 6;
		int iconHeight = 10;
		
		UIStack overallStack = new UIStack();
		overallStack.axis = UIStack.Axis.VERTICAL;
		overallStack.spacing = majorSpacing;
		
		overallStack.addChild(makeSeparatorLine());
		
		UIImage carrotIcon = new UIImage(GuiFoodBook.carrotImage);
		carrotIcon.setHeight(iconHeight);
		
		String foodsTasted;
		if (progressInfo.shouldShowUneatenFoods) {
			foodsTasted = localized("gui", "food_book.stats.fraction",
				progressInfo.foodsEaten,
				foodData.validFoods.size()
			);
		} else {
			foodsTasted = "" + progressInfo.foodsEaten;
		}
		
		overallStack.addChild(statWithIcon(carrotIcon, foodsTasted, localized("gui", "food_book.stats.foods_tasted")));
		
		overallStack.addChild(makeSeparatorLine());
		
		UIImage heartIcon = new UIImage(GuiFoodBook.heartImage);
		heartIcon.setHeight(iconHeight);
		
		String heartsGained = localized("gui", "food_book.stats.fraction",
			progressInfo.heartsPerMilestone * progressInfo.milestonesAchieved(),
			progressInfo.heartsPerMilestone * progressInfo.milestones.length
		);
		
		overallStack.addChild(statWithIcon(heartIcon, heartsGained, localized("gui", "food_book.stats.hearts_gained")));
		
		overallStack.addChild(makeSeparatorLine());
		
		overallStack.setCenterX(getCenterX());
		overallStack.setMinY(progressGraph.getMaxY() + majorSpacing);
		overallStack.updateFrames();
		children.add(overallStack);
	}
	
	private UIBox makeSeparatorLine() {
		return UIBox.horizontalLine(0, getWidth() / 2, 0, GuiFoodBook.leastBlack);
	}
	
	private UIStack statWithIcon(UIImage icon, String value, String name) {
		UIStack valueStack = new UIStack();
		valueStack.axis = UIStack.Axis.HORIZONTAL;
		valueStack.spacing = 3;
		
		valueStack.addChild(icon);
		valueStack.addChild(new UILabel(value));
		
		UIStack fullStack = new UIStack();
		fullStack.axis = UIStack.Axis.VERTICAL;
		fullStack.spacing = 2;
		
		fullStack.addChild(valueStack);
		UILabel nameLabel = new UILabel(name);
		nameLabel.color = GuiFoodBook.lessBlack;
		fullStack.addChild(nameLabel);
		
		return fullStack;
	}
}
