package com.cazsius.solcarrot.capability;

import com.cazsius.solcarrot.SOLCarrotConfig;
import javafx.util.Pair;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Arrays;
import java.util.stream.IntStream;

/** contains all relevant variables for current and future progress, so we don't have to sync config to clients */
public class ProgressInfo {
	private static final String NBT_KEY_FOODS_EATEN = "foodsEaten";
	private static final String NBT_KEY_MILESTONES = "milestones";
	private static final String NBT_KEY_HEARTS_PER_MILESTONE = "heartsPerMilestone";
	private static final String NBT_KEY_BASE_HEARTS = "baseHearts";
	
	/** the number of unique foods eaten */
	public final int foodsEaten;
	/** the number of foods you need to eat for each milestone */
	public final int[] milestones;
	/** the number of hearts you start out with */
	public final int baseHearts;
	/** the number of hearts you get for each milestone */
	public final int heartsPerMilestone;
	
	ProgressInfo(NBTTagCompound tag) {
		foodsEaten = tag.getInteger(NBT_KEY_FOODS_EATEN);
		milestones = tag.getIntArray(NBT_KEY_MILESTONES);
		baseHearts = tag.getInteger(NBT_KEY_BASE_HEARTS);
		heartsPerMilestone = tag.getInteger(NBT_KEY_HEARTS_PER_MILESTONE);
	}
	
	ProgressInfo(int foodsEaten) {
		this.foodsEaten = foodsEaten;
		this.milestones = SOLCarrotConfig.milestones;
		this.baseHearts = SOLCarrotConfig.baseHearts;
		this.heartsPerMilestone = SOLCarrotConfig.heartsPerMilestone;
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
	
	public NBTTagCompound getNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger(NBT_KEY_FOODS_EATEN, foodsEaten);
		tag.setIntArray(NBT_KEY_MILESTONES, milestones);
		tag.setInteger(NBT_KEY_BASE_HEARTS, baseHearts);
		tag.setInteger(NBT_KEY_HEARTS_PER_MILESTONE, heartsPerMilestone);
		return tag;
	}
}