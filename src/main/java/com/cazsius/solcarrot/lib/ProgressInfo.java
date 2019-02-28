package com.cazsius.solcarrot.lib;

import com.cazsius.solcarrot.SOLCarrotConfig;
import com.cazsius.solcarrot.capability.FoodCapability;
import net.minecraft.entity.player.EntityPlayer;

public class ProgressInfo {
	public static ProgressInfo getProgressInfo(EntityPlayer player) {
		return getProgressInfo(FoodCapability.get(player).getCount());
	}
	
	public static ProgressInfo getProgressInfo(int foodsEaten) {
		int[] milestones = SOLCarrotConfig.milestones;
		for (int i = 0; i < milestones.length; i++) {
			if (foodsEaten < milestones[i])
				return new ProgressInfo(i, milestones[i], foodsEaten);
		}
		
		return new ProgressInfo(milestones.length, -1, foodsEaten);
	}
	
	/** the number of milestones achieved, doubling as the index of the next milestone */
	public final int milestonesAchieved;
	/** the total number of foods required for the next milestone, or -1 if max has been reached */
	public final int nextMilestone;
	/** the number of unique foods eaten */
	public final int foodsEaten;
	
	private ProgressInfo(int milestonesAchieved, int nextMilestone, int foodsEaten) {
		this.milestonesAchieved = milestonesAchieved;
		this.nextMilestone = nextMilestone;
		this.foodsEaten = foodsEaten;
	}
	
	public boolean hasReachedMax() {
		return nextMilestone < 0;
	}
	
	public int foodsUntilNextMilestone() {
		return nextMilestone - foodsEaten;
	}
}
