package com.cazsius.solcarrot.handler;

import java.io.File;

import com.cazsius.solcarrot.lib.Constants;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class HandlerConfiguration {
	private static Configuration config = null;
	private static int maxMilestones;
	private static int[] milestoneArray;

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

		defaultHeartCount = config.getInt("defaultHeartCount", Configuration.CATEGORY_GENERAL, 10, 1, 30,
				Constants.ConfigMessages.DEFAULT_HEART_COUNT);
		heartsPerMilestone = config.getInt("heartsPerMilestone", Configuration.CATEGORY_GENERAL, 4, 1, 30,
				Constants.ConfigMessages.HEART_PER_MILESTONE);

		int[] defaultMilestones = { 5, 10, 15 };

		Property milestoneProperty = config.get(Configuration.CATEGORY_GENERAL, "Milestone amounts", defaultMilestones,
				Constants.ConfigMessages.FOOD_MILESTONE_ARRAY);
		milestoneArray = milestoneProperty.getIntList();

		maxMilestones = milestoneArray.length;

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

	public static int getNumMilestones() {
		return maxMilestones;
	}

	public static int[] getMilestoneArray() {
		return milestoneArray;
	}

}
