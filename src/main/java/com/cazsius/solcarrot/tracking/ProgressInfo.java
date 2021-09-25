package com.cazsius.solcarrot.tracking;

import com.cazsius.solcarrot.SOLCarrotConfig;

/** contains all relevant variables for current progress */
public final class ProgressInfo {
	/** the number of unique foods eaten */
	public final int foodsEaten;
	
	ProgressInfo(FoodList foodList) {
		foodsEaten = (int) foodList.getEatenFoods().stream()
			.filter(food -> SOLCarrotConfig.shouldCount(food.item))
			.count();
	}
	
	public boolean hasReachedMax() {
		return foodsEaten >= SOLCarrotConfig.highestMilestone();
	}
	
	/** the next milestone to reach, or a negative value if the maximum has been reached */
	public int nextMilestone() {
		return hasReachedMax() ? -1 : SOLCarrotConfig.milestone(milestonesAchieved());
	}
	
	/** the number of foods remaining until the next milestone, or a negative value if the maximum has been reached */
	public int foodsUntilNextMilestone() {
		return nextMilestone() - foodsEaten;
	}
	
	/** the number of milestones achieved, doubling as the index of the next milestone */
	public int milestonesAchieved() {
		return (int) SOLCarrotConfig.getMilestones().stream()
			.filter(milestone -> foodsEaten >= milestone).count();
	}
}
