package com.cazsius.solcarrot.lib;

public final class Constants {
	public static final String MOD_ID = "solcarrot";
	public static final String MOD_NAME = "Spice of Life: Carrot Edition";
	public static final String VERSION_NUMBER = "1.1.0";
	public static final String CLIENT_PROXY_CLASS = "com.cazsius.solcarrot.client.ClientProxy";
	public static final String SERVER_PROXY_CLASS = "com.cazsius.solcarrot.common.CommonProxy";

	public final class ConfigMessages {
		public static final String HEART_PER_MILESTONE = "Number of hearts you gain per milestone.";
		public static final String DEFAULT_HEART_COUNT = "Number of hearts you spawn with.";
		public static final String FOOD_MILESTONE_ARRAY = "An array containing the milestones: How many unique foods you must eat to get the milestone bonus (defined above). Note that this represents the TOTAL number of foods needed; not the foods since the last milestone!";
		public static final String IS_FOOD_TOOLTIP_ENABLED = "Add tooltips to foods to indicate if they have been eaten?";
	}

	public final class CommandMessages {
		public static final String CLEAR_FOOD_ARRAY = "/clearfoodlist <player>";
		public static final String SIZE_FOOD_ARRAY = "/sizefoodlist <player>";
	}

}
