package com.cazsius.solcarrot.capability;

import com.cazsius.solcarrot.SOLCarrotConfig;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Arrays;

/** contains all relevant variables for current and future progress, so we don't have to sync config to clients */
public class ProgressInfo {
	private static final String NBT_KEY_FOODS_EATEN = "foodsEaten";
	private static final String NBT_KEY_MILESTONES = "milestones";
	private static final String NBT_KEY_HEARTS_PER_MILESTONE = "heartsPerMilestone";
	private static final String NBT_KEY_BASE_HEARTS = "baseHearts";
	private static final String NBT_KEY_SHOULD_SHOW_UNEATEN_FOODS = "shouldShowUneatenFoods";
	private static final String NBT_KEY_MINIMUM_FOOD_VALUE = "minimumFoodValue";
	
	/** the number of unique foods eaten */
	public final int foodsEaten;
	/** the number of foods you need to eat for each milestone */
	public final int[] milestones;
	/** the number of hearts you start out with */
	public final int baseHearts;
	/** the number of hearts you get for each milestone */
	public final int heartsPerMilestone;
	/** whether or not to also show information about the foods left to try */
	public final boolean shouldShowUneatenFoods;
	/** the minimum hunger value foods need to provide in order to count */
	public final int minimumFoodValue;
	
	ProgressInfo(NBTTagCompound tag) {
		foodsEaten = tag.getInteger(NBT_KEY_FOODS_EATEN);
		milestones = tag.getIntArray(NBT_KEY_MILESTONES);
		baseHearts = tag.getInteger(NBT_KEY_BASE_HEARTS);
		heartsPerMilestone = tag.getInteger(NBT_KEY_HEARTS_PER_MILESTONE);
		shouldShowUneatenFoods = tag.getBoolean(NBT_KEY_SHOULD_SHOW_UNEATEN_FOODS);
		minimumFoodValue = tag.getInteger(NBT_KEY_MINIMUM_FOOD_VALUE);
	}
	
	ProgressInfo(int foodsEaten) {
		this.foodsEaten = foodsEaten;
		this.milestones = SOLCarrotConfig.milestones;
		this.baseHearts = SOLCarrotConfig.baseHearts;
		this.heartsPerMilestone = SOLCarrotConfig.heartsPerMilestone;
		this.shouldShowUneatenFoods = SOLCarrotConfig.shouldShowUneatenFoods;
		this.minimumFoodValue = SOLCarrotConfig.minimumFoodValue;
	}
	
	public boolean hasReachedMax() {
		return foodsEaten >= milestones[milestones.length - 1];
	}
	
	/** the next milestone to reach, or a negative value if the maximum has been reached */
	public int nextMilestone() {
		return hasReachedMax() ? -1 : milestones[milestonesAchieved()];
	}
	
	/** the number of foods remaining until the next milestone, or a negative value if the maximum has been reached */
	public int foodsUntilNextMilestone() {
		return nextMilestone() - foodsEaten;
	}
	
	/** the number of milestones achieved, doubling as the index of the next milestone */
	public int milestonesAchieved() {
		return (int) Arrays.stream(milestones)
			.filter(milestone -> foodsEaten >= milestone).count();
	}
	
	NBTTagCompound getNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger(NBT_KEY_FOODS_EATEN, foodsEaten);
		tag.setIntArray(NBT_KEY_MILESTONES, milestones);
		tag.setInteger(NBT_KEY_BASE_HEARTS, baseHearts);
		tag.setInteger(NBT_KEY_HEARTS_PER_MILESTONE, heartsPerMilestone);
		tag.setBoolean(NBT_KEY_SHOULD_SHOW_UNEATEN_FOODS, shouldShowUneatenFoods);
		tag.setInteger(NBT_KEY_MINIMUM_FOOD_VALUE, minimumFoodValue);
		return tag;
	}
}
