package com.cazsius.solcarrot.lib;

public final class Constants {
	public static final String MOD_ID = "solcarrot";
	public static final String MOD_NAME = "Spice of Life: Carrot Edition";
	public static final String VERSION_NUMBER = "0.0.0";
	public static final String CLIENT_PROXY_CLASS = "com.cazsius.solcarrot.client.ClientProxy";
	public static final String SERVER_PROXY_CLASS = "com.cazsius.solcarrot.common.CommonProxy";
	
	public final class ConfigMessages {
		public static final String MAX_MILESTONES = "Number of Milestones that you want. Set to zero if you want none. This will control what food milestones work.";
		public static final String HEART_PER_MILESTONE = "Number of hearts you gain per milestone.";
		public static final String DEFAULT_HEART_COUNT = "Number of hearts you spawn with.";
		
		public static final String FOOD_MILESTONE = "Based on how many unique foods you've eaten, and give more hearts each time. Maximum of 5 milestones. Please note, how many milestones work is based off the 'MAX_MILESTONES' config option";
	}

}
