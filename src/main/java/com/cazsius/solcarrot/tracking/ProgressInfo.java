package com.cazsius.solcarrot.tracking;

import com.cazsius.solcarrot.SOLCarrotConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import squeek.applecore.api.AppleCoreAPI;

import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/** contains all relevant variables for current and future progress, so we don't have to sync config to clients */
public final class ProgressInfo {
	private static final String NBT_KEY_FOODS_EATEN = "foodsEaten";
	private static final String NBT_KEY_CONFIG_INFO = "configInfo";
	
	/** the number of unique foods eaten */
	public final int foodsEaten;
	/** information about the config */
	public final ConfigInfo configInfo;
	
	ProgressInfo(NBTTagCompound tag) {
		foodsEaten = tag.getInteger(NBT_KEY_FOODS_EATEN);
		configInfo = new ConfigInfo(tag.getCompoundTag(NBT_KEY_CONFIG_INFO));
	}
	
	ProgressInfo(FoodList foodList) {
		configInfo = new ConfigInfo();
		foodsEaten = (int) foodList.getEatenFoods().stream()
			.map(FoodInstance::getItemStack)
			.filter(configInfo::shouldCount)
			.count();
	}
	
	public boolean hasReachedMax() {
		return foodsEaten >= configInfo.milestones[configInfo.milestones.length - 1];
	}
	
	/** the next milestone to reach, or a negative value if the maximum has been reached */
	public int nextMilestone() {
		return hasReachedMax() ? -1 : configInfo.milestones[milestonesAchieved()];
	}
	
	/** the number of foods remaining until the next milestone, or a negative value if the maximum has been reached */
	public int foodsUntilNextMilestone() {
		return nextMilestone() - foodsEaten;
	}
	
	/** the number of milestones achieved, doubling as the index of the next milestone */
	public int milestonesAchieved() {
		return (int) Arrays.stream(configInfo.milestones)
			.filter(milestone -> foodsEaten >= milestone).count();
	}
	
	NBTTagCompound getNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger(NBT_KEY_FOODS_EATEN, foodsEaten);
		tag.setTag(NBT_KEY_CONFIG_INFO, configInfo.getTag());
		return tag;
	}
	
	public static final class ConfigInfo {
		private static final String NBT_KEY_MILESTONES = "milestones";
		private static final String NBT_KEY_HEARTS_PER_MILESTONE = "heartsPerMilestone";
		private static final String NBT_KEY_BASE_HEARTS = "baseHearts";
		private static final String NBT_KEY_SHOULD_SHOW_UNEATEN_FOODS = "shouldShowUneatenFoods";
		private static final String NBT_KEY_MINIMUM_FOOD_VALUE = "minimumFoodValue";
		private static final String NBT_KEY_BLACKLIST = "blacklist";
		private static final String NBT_KEY_WHITELIST = "whitelist";
		
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
		/** blacklisted foods */
		public final String[] blacklist;
		/** whitelisted foods */
		public final String[] whitelist;
		
		ConfigInfo(NBTTagCompound tag) {
			milestones = tag.getIntArray(NBT_KEY_MILESTONES);
			baseHearts = tag.getInteger(NBT_KEY_BASE_HEARTS);
			heartsPerMilestone = tag.getInteger(NBT_KEY_HEARTS_PER_MILESTONE);
			shouldShowUneatenFoods = tag.getBoolean(NBT_KEY_SHOULD_SHOW_UNEATEN_FOODS);
			minimumFoodValue = tag.getInteger(NBT_KEY_MINIMUM_FOOD_VALUE);
			blacklist = decodeList(tag.getString(NBT_KEY_BLACKLIST));
			whitelist = decodeList(tag.getString(NBT_KEY_WHITELIST));
		}
		
		public ConfigInfo() {
			milestones = SOLCarrotConfig.milestones;
			baseHearts = SOLCarrotConfig.baseHearts;
			heartsPerMilestone = SOLCarrotConfig.heartsPerMilestone;
			shouldShowUneatenFoods = SOLCarrotConfig.shouldShowUneatenFoods;
			minimumFoodValue = SOLCarrotConfig.minimumFoodValue;
			blacklist = SOLCarrotConfig.foodBlacklist;
			whitelist = SOLCarrotConfig.foodWhitelist;
		}
		
		NBTTagCompound getTag() {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setIntArray(NBT_KEY_MILESTONES, milestones);
			tag.setInteger(NBT_KEY_BASE_HEARTS, baseHearts);
			tag.setInteger(NBT_KEY_HEARTS_PER_MILESTONE, heartsPerMilestone);
			tag.setBoolean(NBT_KEY_SHOULD_SHOW_UNEATEN_FOODS, shouldShowUneatenFoods);
			tag.setInteger(NBT_KEY_MINIMUM_FOOD_VALUE, minimumFoodValue);
			tag.setString(NBT_KEY_BLACKLIST, encodeList(blacklist));
			tag.setString(NBT_KEY_WHITELIST, encodeList(whitelist));
			return tag;
		}
		
		private static String encodeList(String[] list) {
			StringBuilder encoder = new StringBuilder();
			for (String element : list) {
				encoder.append(element);
				encoder.append('|');
			}
			return encoder.toString();
		}
		
		private static String[] decodeList(String raw) {
			return Stream.of(raw.split("\\|"))
				.filter(entry -> !entry.isEmpty())
				.toArray(String[]::new);
		}
		
		public boolean hasWhitelist() {
			return whitelist.length > 0;
		}
		
		public boolean isAllowed(ItemStack food) {
			String id = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(food.getItem())).toString();
			String withMeta = id + ":" + food.getMetadata();
			if (hasWhitelist()) {
				return matchesAnyPattern(id, whitelist) || matchesAnyPattern(withMeta, whitelist);
			} else {
				return !matchesAnyPattern(id, blacklist) && !matchesAnyPattern(withMeta, blacklist);
			}
		}
		
		public boolean shouldCount(ItemStack food) {
			return isHearty(food) && isAllowed(food);
		}
		
		public boolean isHearty(ItemStack food) {
			return AppleCoreAPI.accessor.getFoodValues(food).hunger >= minimumFoodValue;
		}
		
		private static boolean matchesAnyPattern(String query, String[] patterns) {
			for (String glob : patterns) {
				StringBuilder pattern = new StringBuilder(glob.length());
				for (String part : glob.split("\\*", -1)) {
					if (!part.isEmpty()) { // not necessary
						pattern.append(Pattern.quote(part));
					}
					pattern.append(".*");
				}
				
				// delete last ".*" wildcard
				pattern.delete(pattern.length() - 2, pattern.length());
				
				if (Pattern.matches(pattern.toString(), query)) {
					return true;
				}
			}
			return false;
		}
	}
}
