package com.cazsius.solcarrot;

import com.cazsius.solcarrot.tracking.CapabilityHandler;
import com.cazsius.solcarrot.tracking.FoodList;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.server.management.PlayerList;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.regex.Pattern;

import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD;

@Mod.EventBusSubscriber(modid = SOLCarrot.MOD_ID)
public final class SOLCarrotConfig {
	private static String localizationPath(String path) {
		return "config." + SOLCarrot.MOD_ID + "." + path;
	}
	
	public static final Server SERVER;
	public static final ForgeConfigSpec SERVER_SPEC;
	
	static {
		Pair<Server, ForgeConfigSpec> specPair = new Builder().configure(Server::new);
		SERVER = specPair.getLeft();
		SERVER_SPEC = specPair.getRight();
	}
	
	public static final Client CLIENT;
	public static final ForgeConfigSpec CLIENT_SPEC;
	
	static {
		Pair<Client, ForgeConfigSpec> specPair = new Builder().configure(Client::new);
		CLIENT = specPair.getLeft();
		CLIENT_SPEC = specPair.getRight();
	}
	
	public static int baseHearts;
	public static int heartsPerMilestone;
	public static List<Integer> milestones;
	
	public static Set<String> blacklist;
	public static Set<String> whitelist;
	public static int minimumFoodValue;
	
	public static boolean shouldResetOnDeath;
	
	public static void refreshServer() {
		baseHearts = SERVER.baseHearts.get();
		heartsPerMilestone = SERVER.heartsPerMilestone.get();
		milestones = new ArrayList<>(SERVER.milestones.get());
		
		blacklist = new HashSet<>(SERVER.blacklist.get());
		whitelist = new HashSet<>(SERVER.whitelist.get());
		minimumFoodValue = SERVER.minimumFoodValue.get();
		
		shouldResetOnDeath = SERVER.shouldResetOnDeath.get();
	}
	
	public static class Server {
		public final IntValue baseHearts;
		public final IntValue heartsPerMilestone;
		public final ConfigValue<List<? extends Integer>> milestones;
		
		public final ConfigValue<List<? extends String>> blacklist;
		public final ConfigValue<List<? extends String>> whitelist;
		public final IntValue minimumFoodValue;
		
		public final BooleanValue shouldResetOnDeath;
		
		Server(Builder builder) {
			builder.push("milestones");
			
			baseHearts = builder
				.translation(localizationPath("base_hearts"))
				.comment("Number of hearts you start out with.")
				.defineInRange("baseHearts", 10, 0, 1000);
			
			heartsPerMilestone = builder
				.translation(localizationPath("hearts_per_milestone"))
				.comment("Number of hearts you gain for reaching a new milestone.")
				.defineInRange("heartsPerMilestone", 2, 0, 1000);
			
			milestones = builder
				.translation(localizationPath("milestones"))
				.comment("A list of numbers of unique foods you need to eat to unlock each milestone, in ascending order.")
				.defineList("milestones", Lists.newArrayList(5, 10, 15, 20, 25), e -> e instanceof Integer);
			
			builder.pop();
			builder.push("filtering");
			
			blacklist = builder
				.translation(localizationPath("blacklist"))
				.comment("Foods in this list won't affect the player's health nor show up in the food book.")
				.defineList("blacklist", Lists.newArrayList(), e -> e instanceof String);
			
			whitelist = builder
				.translation(localizationPath("whitelist"))
				.comment("When this list contains anything, the blacklist is ignored and instead only foods from here count.")
				.defineList("whitelist", Lists.newArrayList(), e -> e instanceof String);
			
			minimumFoodValue = builder
				.translation(localizationPath("minimum_food_value"))
				.comment("The minimum hunger value foods need to provide in order to count for milestones, in half drumsticks.")
				.defineInRange("minimumFoodValue", 1, 0, 1000);
			
			builder.pop();
			builder.push("miscellaneous");
			
			shouldResetOnDeath = builder
				.translation(localizationPath("reset_on_death"))
				.comment("Whether or not to reset the food list on death, effectively losing all bonus hearts.")
				.define("resetOnDeath", false);
			
			builder.pop();
		}
	}
	
	public static boolean shouldPlayMilestoneSounds;
	public static boolean shouldSpawnIntermediateParticles;
	public static boolean shouldSpawnMilestoneParticles;
	
	public static boolean isFoodTooltipEnabled;
	public static boolean shouldShowProgressAboveHotbar;
	public static boolean shouldShowUneatenFoods;
	
	public static void refreshClient() {
		shouldPlayMilestoneSounds = CLIENT.shouldPlayMilestoneSounds.get();
		shouldSpawnIntermediateParticles = CLIENT.shouldSpawnIntermediateParticles.get();
		shouldSpawnMilestoneParticles = CLIENT.shouldSpawnMilestoneParticles.get();
		
		isFoodTooltipEnabled = CLIENT.isFoodTooltipEnabled.get();
		shouldShowProgressAboveHotbar = CLIENT.shouldShowProgressAboveHotbar.get();
		shouldShowUneatenFoods = CLIENT.shouldShowUneatenFoods.get();
	}
	
	public static class Client {
		public final BooleanValue shouldPlayMilestoneSounds;
		public final BooleanValue shouldSpawnIntermediateParticles;
		public final BooleanValue shouldSpawnMilestoneParticles;
		
		public final BooleanValue isFoodTooltipEnabled;
		public final BooleanValue shouldShowProgressAboveHotbar;
		public final BooleanValue shouldShowUneatenFoods;
		
		Client(Builder builder) {
			builder.push("milestone celebration");
			
			shouldPlayMilestoneSounds = builder
				.translation(localizationPath("should_play_milestone_sounds"))
				.comment("If true, reaching a new milestone plays a ding sound.")
				.define("shouldPlayMilestoneSounds", true);
			
			shouldSpawnIntermediateParticles = builder
				.translation(localizationPath("should_spawn_intermediate_particles"))
				.comment("If true, trying a new food spawns particles.")
				.define("shouldSpawnIntermediateParticles", true);
			
			shouldSpawnMilestoneParticles = builder
				.translation(localizationPath("should_spawn_milestone_particles"))
				.comment("If true, reaching a new milestone spawns particles.")
				.define("shouldSpawnMilestoneParticles", true);
			
			builder.pop();
			builder.push("miscellaneous");
			
			isFoodTooltipEnabled = builder
				.translation(localizationPath("is_food_tooltip_enabled"))
				.comment("If true, foods indicate in their tooltips whether or not they have been eaten.")
				.define("isFoodTooltipEnabled", true);
			
			shouldShowProgressAboveHotbar = builder
				.translation(localizationPath("should_show_progress_above_hotbar"))
				.comment("Whether the messages notifying you of reaching new milestones should be displayed above the hotbar or in chat.")
				.define("shouldShowProgressAboveHotbar", true);
			
			shouldShowUneatenFoods = builder
				.translation(localizationPath("should_show_uneaten_foods"))
				.comment("If true, the food book also lists foods that you haven't eaten, in addition to the ones you have.")
				.define("shouldShowUneatenFoods", true);
			
			builder.pop();
		}
	}
	
	@SubscribeEvent
	public static void refresh(ModConfig.ModConfigEvent event) {
		ForgeConfigSpec spec = event.getConfig().getSpec();
		if (spec == CLIENT_SPEC) refreshClient();
		if (spec == SERVER_SPEC) refreshServer();
	}
	
	// TODO: make sure this is actually called
	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (!event.getModID().equals(SOLCarrot.MOD_ID)) return;
		
		if (event.isWorldRunning()) {
			PlayerList players = ServerLifecycleHooks.getCurrentServer().getPlayerList();
			for (PlayerEntity player : players.getPlayers()) {
				FoodList.get(player).invalidateProgressInfo();
				CapabilityHandler.syncFoodList(player);
			}
		}
	}
	
	static void setUp() {
		ModLoadingContext context = ModLoadingContext.get();
		context.registerConfig(ModConfig.Type.SERVER, SERVER_SPEC);
		context.registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC);
	}
	
	public static int highestMilestone() {
		return milestones.get(milestones.size() - 1);
	}
	
	public static boolean hasWhitelist() {
		return !whitelist.isEmpty();
	}
	
	public static boolean isAllowed(Item food) {
		String id = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(food)).toString();
		if (hasWhitelist()) {
			return matchesAnyPattern(id, whitelist);
		} else {
			return !matchesAnyPattern(id, blacklist);
		}
	}
	
	public static boolean shouldCount(Item food) {
		return isHearty(food) && isAllowed(food);
	}
	
	public static boolean isHearty(Item food) {
		Food foodInfo = food.getFood();
		assert foodInfo != null;
		return foodInfo.getHealing() >= minimumFoodValue;
	}
	
	private static boolean matchesAnyPattern(String query, Collection<String> patterns) {
		for (String glob : patterns) {
			StringBuilder pattern = new StringBuilder(glob.length());
			for (String part : glob.split("\\*", -1)) {
				if (!part.isEmpty()) { // not necessary
					pattern.append(Pattern.quote(part));
				}
				pattern.append(".*");
			}
			
			// delete extraneous trailing ".*" wildcard
			pattern.delete(pattern.length() - 2, pattern.length());
			
			if (Pattern.matches(pattern.toString(), query)) {
				return true;
			}
		}
		return false;
	}
}
/*
@Mod.EventBusSubscriber(modid = SOLCarrot.MOD_ID, bus = MOD)
final class Setup {
	@SubscribeEvent
	public static void setUp(FMLCommonSetupEvent event) {
		SOLCarrotConfig.setUp();
	}
}
*/