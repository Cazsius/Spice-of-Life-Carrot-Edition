package com.cazsius.solcarrot.client.gui;

import com.cazsius.solcarrot.capability.ProgressInfo;
import com.cazsius.solcarrot.client.gui.elements.*;
import com.cazsius.solcarrot.lib.FoodItemStacks;

import java.awt.*;

import static com.cazsius.solcarrot.lib.Localization.localized;

final class ProgressGraph extends UIElement {
	private static final int segmentLength = 48;
	
	private ProgressInfo progressInfo;
	
	public ProgressGraph(ProgressInfo progressInfo, int centerX, int lineY) {
		super(new Rectangle(centerX, lineY, 2 * segmentLength, 1)); // kinda wrong; will adjust later
		
		this.progressInfo = progressInfo;
		
		int leftEdge = centerX - segmentLength * 3 / 4;
		int leftPoint = centerX - segmentLength / 2;
		int rightPoint = centerX + segmentLength / 2;
		int rightEdge = centerX + segmentLength * 3 / 4;
		int padding = 4;
		
		int milestonesAchieved = progressInfo.milestonesAchieved();
		int previousMilestone = milestonesAchieved > 0 ? progressInfo.milestones[milestonesAchieved - 1] : 0;
		int nextMilestone = progressInfo.nextMilestone();
		boolean hasReachedMax = progressInfo.hasReachedMax();
		boolean hasSurpassedMax = hasReachedMax && progressInfo.foodsEaten > previousMilestone;
		int progress = segmentLength * (progressInfo.foodsEaten - previousMilestone) / (nextMilestone - previousMilestone);
		int progressX = leftPoint + (hasSurpassedMax ? segmentLength : progress);
		
		UIImage carrotIcon = new UIImage(GuiFoodBook.carrotImage);
		carrotIcon.setCenterY(lineY);
		if (milestonesAchieved > 0) {
			carrotIcon.setMaxX(leftEdge - padding);
		} else {
			int distanceToLeftBookEdge = 36;
			carrotIcon.setCenterX(leftPoint - distanceToLeftBookEdge / 2);
		}
		children.add(carrotIcon);
		
		children.add(UIBox.horizontalLine(leftPoint, progressX, lineY, GuiFoodBook.fullBlack));
		
		UILabel previousMilestoneLabel = new UILabel("" + previousMilestone);
		previousMilestoneLabel.color = GuiFoodBook.fullBlack;
		previousMilestoneLabel.tooltip = localized("gui", "food_book.stats.tooltip.previous_milestone");
		previousMilestoneLabel.setCenterX(leftPoint);
		previousMilestoneLabel.setMaxY(lineY - 3);
		children.add(previousMilestoneLabel);
		children.add(UIBox.verticalLine(leftPoint, lineY - 2, lineY - 1, GuiFoodBook.fullBlack));
		
		if (milestonesAchieved > 0) {
			addHeartsView(leftPoint, previousMilestoneLabel.getMinY() - 4, true);
			
			children.add(UIBox.horizontalLine(leftEdge, leftPoint, lineY, GuiFoodBook.fullBlack));
		}
		
		UILabel foodsEatenLabel = new UILabel("" + progressInfo.foodsEaten);
		foodsEatenLabel.color = GuiFoodBook.fullBlack;
		foodsEatenLabel.tooltip = localized("gui", "food_book.stats.tooltip.foods_tasted");
		foodsEatenLabel.setCenterX(progressX);
		foodsEatenLabel.setMinY(lineY + 7);
		children.add(foodsEatenLabel);
		children.add(UIBox.verticalLine(progressX, lineY + 1, lineY + 5, GuiFoodBook.fullBlack));
		
		if (!hasReachedMax) {
			UILabel nextMilestoneLabel = new UILabel("" + nextMilestone);
			nextMilestoneLabel.color = GuiFoodBook.lessBlack;
			nextMilestoneLabel.tooltip = localized("gui", "food_book.stats.tooltip.next_milestone");
			nextMilestoneLabel.setCenterX(rightPoint);
			nextMilestoneLabel.setMaxY(lineY - 3);
			children.add(nextMilestoneLabel);
			children.add(UIBox.verticalLine(rightPoint, lineY - 2, lineY - 1, GuiFoodBook.lessBlack));
			
			addHeartsView(rightPoint, previousMilestoneLabel.getMinY() - 4, false);
		}
		
		if (!hasSurpassedMax) {
			// all the edge cases!
			children.add(UIBox.horizontalLine(progressX + 1, rightPoint, lineY, hasReachedMax ? GuiFoodBook.leastBlack : GuiFoodBook.lessBlack));
		}
		
		boolean isLastMilestoneVisible = milestonesAchieved + 1 >= progressInfo.milestones.length;
		// if the last milestone is visible, there are no more milestones beyond the right edge, so the line is fainter.
		children.add(UIBox.horizontalLine(rightPoint + 1, rightEdge, lineY, isLastMilestoneVisible ? GuiFoodBook.leastBlack : GuiFoodBook.lessBlack));
		
		int totalFoodCount = FoodItemStacks.getAllFoods().size();
		if (progressInfo.foodsEaten < totalFoodCount) {
			UILabel totalFoodsLabel = new UILabel("" + totalFoodCount);
			totalFoodsLabel.color = GuiFoodBook.leastBlack;
			totalFoodsLabel.tooltip = localized("gui", "food_book.stats.tooltip.total_foods");
			totalFoodsLabel.setMinX(rightEdge + padding);
			totalFoodsLabel.setCenterY(lineY);
			children.add(totalFoodsLabel);
		}
		
		calculateFrameFromChildren();
	}
	
	private void addHeartsView(int centerX, int maxY, boolean isOpaque) {
		UIStack heartsView = new UIStack();
		
		heartsView.tooltip = localized("gui", "food_book.stats.tooltip.hearts_per_milestone");
		
		int hearts = progressInfo.heartsPerMilestone;
		if (hearts <= 3) {
			heartsView.spacing = -1;
			for (int i = 0; i < hearts; i++) {
				UIImage heartImage = new UIImage(GuiFoodBook.heartImage);
				heartImage.alpha = isOpaque ? 1f : 0.5f;
				heartsView.addChild(heartImage);
			}
		} else {
			heartsView.spacing = 1;
			UILabel label = new UILabel("" + hearts);
			label.color = isOpaque ? GuiFoodBook.fullBlack : GuiFoodBook.lessBlack;
			heartsView.addChild(label);
			UIImage heartImage = new UIImage(GuiFoodBook.heartImage);
			heartImage.alpha = isOpaque ? 1f : 0.5f;
			heartsView.addChild(heartImage);
		}
		
		heartsView.setCenterX(centerX);
		heartsView.setMaxY(maxY);
		heartsView.updateFrames();
		
		children.add(heartsView);
	}
}