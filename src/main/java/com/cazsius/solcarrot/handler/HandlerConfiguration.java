package com.cazsius.solcarrot.handler;

import java.io.File;

import com.cazsius.solcarrot.lib.Constants;

import net.minecraftforge.common.config.Configuration;

public class HandlerConfiguration {
	private static Configuration config = null;
	private static int maxMilestones;
	private static int foodMilestoneA;
	private static int foodMilestoneB;
	private static int foodMilestoneC;
	private static int foodMilestoneD;
	private static int foodMilestoneE;
	
	private static int defaultHeartCount;
	private static int heartsPerMilestone;
	
	/**
	 * Initializes the configuration file.
	 *
	 * @param file
	 *            The file to read/write config stuff to.
	 */
	public static void initConfig(File file) {
		setConfig(new Configuration(file));
		syncConfig();
	}

	
	/**
	 * Syncs all configuration properties.
	 */
	public static void syncConfig() {
		
		defaultHeartCount = config.getInt("defaultHeartCount", Configuration.CATEGORY_GENERAL, 10, 1, 30, Constants.ConfigMessages.DEFAULT_HEART_COUNT);
		heartsPerMilestone = config.getInt("heartsPerMilestone", Configuration.CATEGORY_GENERAL, 4, 1, 30, Constants.ConfigMessages.HEART_PER_MILESTONE);

		maxMilestones = config.getInt("maxMilestones", Configuration.CATEGORY_GENERAL, 5, 0, 5, Constants.ConfigMessages.MAX_MILESTONES);
		
		if(maxMilestones >= 1) {
		setFoodMilestoneA(config.getInt("foodMilestoneA", Configuration.CATEGORY_GENERAL, 10, 1, 100, Constants.ConfigMessages.FOOD_MILESTONE));
		} if(maxMilestones >= 2) {
		setFoodMilestoneB(config.getInt("foodMilestoneB", Configuration.CATEGORY_GENERAL, 20, 1, 100, Constants.ConfigMessages.FOOD_MILESTONE));
		} if(maxMilestones >= 3) {
		setFoodMilestoneC(config.getInt("foodMilestoneC", Configuration.CATEGORY_GENERAL, 30, 1, 100, Constants.ConfigMessages.FOOD_MILESTONE));
		} if(maxMilestones >= 4) {
		setFoodMilestoneD(config.getInt("foodMilestoneD", Configuration.CATEGORY_GENERAL, 40, 1, 100, Constants.ConfigMessages.FOOD_MILESTONE));
		} if(maxMilestones >= 5) {
		setFoodMilestoneE(config.getInt("foodMilestoneE", Configuration.CATEGORY_GENERAL, 50, 1, 100, Constants.ConfigMessages.FOOD_MILESTONE));
		}

		if (getConfig().hasChanged()) {
			getConfig().save();
		}
	}

	public static Configuration getConfig() {
		return config;
	}

	public static void setConfig(Configuration config) {
		HandlerConfiguration.config = config;
	}

	public static int getFoodMilestoneA() {
		return foodMilestoneA;
	}
	
	public static void setFoodMilestoneA(int foodMilestoneOne) {
		HandlerConfiguration.foodMilestoneA = foodMilestoneOne;
	}

	public static int getFoodMilestoneB() {
		return foodMilestoneB;
	}

	public static void setFoodMilestoneB(int foodMilestoneTwo) {
		HandlerConfiguration.foodMilestoneB = foodMilestoneTwo;
	}

	public static int getFoodMilestoneC() {
		return foodMilestoneC;
	}

	public static void setFoodMilestoneC(int foodMilestoneThree) {
		HandlerConfiguration.foodMilestoneC = foodMilestoneThree;
	}

	public static int getFoodMilestoneD() {
		return foodMilestoneD;
	}

	public static void setFoodMilestoneD(int foodMilestoneFour) {
		HandlerConfiguration.foodMilestoneD = foodMilestoneFour;
	}

	public static int getFoodMilestoneE() {
		return foodMilestoneE;
	}

	public static void setFoodMilestoneE(int foodMilestoneFive) {
		HandlerConfiguration.foodMilestoneE = foodMilestoneFive;
	}


	public static int getDefaultHeartCount() {
		return defaultHeartCount;
	}


	public static void setDefaultHeartCount(int defaultHeartCount) {
		HandlerConfiguration.defaultHeartCount = defaultHeartCount;
	}


	public static int getHeartsPerMilestone() {
		return heartsPerMilestone;
	}


	public static void setHeartsPerMilestone(int heartsPerMilestone) {
		HandlerConfiguration.heartsPerMilestone = heartsPerMilestone;
	}

}
