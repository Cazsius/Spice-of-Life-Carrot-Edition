package com.cazsius.solcarrot;

import com.cazsius.solcarrot.lib.Constants;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
@Config(modid = Constants.MOD_ID, name = Constants.MOD_ID + "-" + SOLCarrotConfig.version)
public class SOLCarrotConfig {
	static final String version = "1.5"; // only change this if the new version is not backwards-compatible with the old one.
	
	@Config.Name("Base Hearts")
	@Config.Comment("Number of hearts you start out with.")
	public static int baseHearts = 10;
	
	@Config.Name("Hearts per Milestone")
	@Config.Comment("Number of hearts you gain for reaching a new milestone.")
	public static int heartsPerMilestone = 2;
	
	@Config.Name("Milestones")
	@Config.Comment("A list of numbers of unique foods you need to eat to unlock each milestone, in ascending order.")
	public static int[] milestones = {5, 10, 15, 20, 25};
	
	@Config.Name("Enable Food Status Tooltip")
	@Config.Comment("If true, foods indicate in their tooltips whether or not they have been eaten.")
	public static boolean isFoodTooltipEnabled = true;
	
	@Config.Name("Show Progress Above Hotbar")
	@Config.Comment("Whether the messages notifying you of reaching new milestones should be displayed above the hotbar or in chat.")
	public static boolean shouldShowProgressAboveHotbar = true;
	
	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (!event.getModID().equals(Constants.MOD_ID)) return;
		
		ConfigManager.sync(Constants.MOD_ID, Config.Type.INSTANCE);
	}
}
