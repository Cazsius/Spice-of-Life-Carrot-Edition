package com.cazsius.solcarrot;

import com.cazsius.solcarrot.tracking.CapabilityHandler;
import com.cazsius.solcarrot.tracking.FoodList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.management.PlayerList;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

// new config names + versioning commented out for now
// TODO switch to new names when updating to next mc version

@Mod.EventBusSubscriber(modid = SOLCarrot.MOD_ID)
@Config(modid = SOLCarrot.MOD_ID)//, name = Constants.MOD_ID + "-" + SOLCarrotConfig.version)
public final class SOLCarrotConfig {
	//static final String version = "1.5"; // only change this if the new version is not backwards-compatible with the old one.
	private static final String langPath = "config.solcarrot.";
	
	//@Config.Name("Base Hearts")
	@Config.Name("defaultHeartCount")
	@Config.LangKey(langPath + "base_hearts")
	@Config.Comment("Number of hearts you start out with.")
	public static int baseHearts = 10;
	
	//@Config.Name("Hearts per Milestone")
	@Config.Name("heartsPerMilestone")
	@Config.LangKey(langPath + "hearts_per_milestone")
	@Config.Comment("Number of hearts you gain for reaching a new milestone.")
	public static int heartsPerMilestone = 2;
	
	//@Config.Name("Milestones")
	@Config.Name("Milestone amounts")
	@Config.LangKey(langPath + "milestones")
	@Config.Comment("A list of numbers of unique foods you need to eat to unlock each milestone, in ascending order.")
	public static int[] milestones = {5, 10, 15, 20, 25};
	
	//@Config.Name("Enable Food Status Tooltip")
	@Config.Name("isFoodTooltipEnabled")
	@Config.LangKey(langPath + "is_food_tooltip_enabled")
	@Config.Comment("If true, foods indicate in their tooltips whether or not they have been eaten.")
	public static boolean isFoodTooltipEnabled = true;
	
	//@Config.Name("Show Progress Above Hotbar")
	@Config.Name("shouldShowProgressAboveHotbar")
	@Config.LangKey(langPath + "should_show_progress_above_hotbar")
	@Config.Comment("Whether the messages notifying you of reaching new milestones should be displayed above the hotbar or in chat.")
	public static boolean shouldShowProgressAboveHotbar = true;
	
	@Config.Name("Show Uneaten Foods")
	@Config.LangKey(langPath + "should_show_uneaten_foods")
	@Config.Comment("If true, the food book also lists foods that you haven't eaten, in addition to the ones you have.")
	public static boolean shouldShowUneatenFoods = true;
	
	@Config.Name("Enable Milestone Sounds")
	@Config.LangKey(langPath + "should_play_milestone_sounds")
	@Config.Comment("If true, reaching a new milestone plays a ding sound.")
	public static boolean shouldPlayMilestoneSounds = true;
	
	@Config.Name("Enable Intermediate Particles")
	@Config.LangKey(langPath + "should_spawn_intermediate_particles")
	@Config.Comment("If true, trying a new food spawns particles.")
	public static boolean shouldSpawnIntermediateParticles = true;
	
	@Config.Name("Enable Milestone Particles")
	@Config.LangKey(langPath + "should_spawn_milestone_particles")
	@Config.Comment("If true, reaching a new milestone spawns particles.")
	public static boolean shouldSpawnMilestoneParticles = true;
	
	@Config.Name("Food Blacklist")
	@Config.LangKey(langPath + "food_blacklist")
	@Config.Comment("Foods in this list won't affect the player's health nor show up in the food book.")
	public static String[] foodBlacklist = {};
	
	@Config.Name("Food Whitelist")
	@Config.LangKey(langPath + "food_whitelist")
	@Config.Comment("When this list contains anything, the blacklist is ignored and instead only foods from here count.")
	public static String[] foodWhitelist = {};
	
	@Config.Name("Minimum Food Value")
	@Config.LangKey(langPath + "minimum_food_value")
	@Config.Comment("The minimum hunger value foods need to provide in order to count for milestones, in half drumsticks.")
	public static int minimumFoodValue = 1;
	
	@Config.Name("Reset on Death")
	@Config.LangKey(langPath + "reset_on_death")
	@Config.Comment("Whether or not to reset the food list on death, effectively losing all bonus hearts.")
	public static boolean shouldResetOnDeath = false;
	
	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (!event.getModID().equals(SOLCarrot.MOD_ID)) return;
		
		ConfigManager.sync(SOLCarrot.MOD_ID, Config.Type.INSTANCE);
		
		if (event.isWorldRunning()) {
			PlayerList players = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();
			for (EntityPlayer player : players.getPlayers()) {
				FoodList.get(player).updateProgressInfo();
				CapabilityHandler.syncFoodList(player);
			}
		}
	}
	
	private SOLCarrotConfig() {}
}
