package com.cazsius.solcarrot.capability;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.*;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.stream.StreamSupport;

@ParametersAreNonnullByDefault
public final class FoodCapability implements ICapabilitySerializable<NBTBase> {
	private static final String NBT_KEY_FOOD_LIST = "foodList";
	private static final String NBT_KEY_PROGRESS_INFO = "progressInfo";
	
	public static FoodCapability get(EntityPlayer player) {
		FoodCapability foodCapability = player.getCapability(FoodCapability.FOOD_CAPABILITY, null);
		assert foodCapability != null;
		return foodCapability;
	}
	
	@CapabilityInject(FoodCapability.class)
	public static Capability<FoodCapability> FOOD_CAPABILITY;
	
	private final Set<FoodInstance> foods = new HashSet<>();
	private ProgressInfo progressInfo = new ProgressInfo(this);
	
	public FoodCapability() {}
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == FOOD_CAPABILITY;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == FOOD_CAPABILITY ? (T) this : null;
	}
	
	/** used for persistent storage */
	@Override
	public NBTBase serializeNBT() {
		NBTTagList list = new NBTTagList();
		foods.stream()
			.map(FoodInstance::encode)
			.filter(Objects::nonNull)
			.map(NBTTagString::new)
			.forEach(list::appendTag);
		
		return list;
	}
	
	/** used for persistent storage */
	@Override
	public void deserializeNBT(NBTBase nbt) {
		NBTTagList list = (NBTTagList) nbt;
		
		foods.clear();
		StreamSupport.stream(list.spliterator(), false)
			.map(tag -> (NBTTagString) tag)
			.map(NBTTagString::getString)
			.map(FoodInstance::decode)
			.filter(Objects::nonNull)
			.forEach(foods::add);
		
		updateProgressInfo();
	}
	
	/** serializes everything, including the progress info, for sending to clients */
	public NBTTagCompound serializeFullNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setTag(NBT_KEY_FOOD_LIST, serializeNBT());
		tag.setTag(NBT_KEY_PROGRESS_INFO, progressInfo.getNBT());
		return tag;
	}
	
	/** deserializes everything, including the progress info, after receiving it from the server */
	public void deserializeFullNBT(NBTTagCompound tag) {
		deserializeNBT(tag.getTag(NBT_KEY_FOOD_LIST));
		progressInfo = new ProgressInfo(tag.getCompoundTag(NBT_KEY_PROGRESS_INFO));
	}
	
	/** @return true if the food was not previously known, i.e. if a new food has been tried */
	public boolean addFood(ItemStack food) {
		boolean wasAdded = foods.add(new FoodInstance(food)) && progressInfo.shouldCount(food);
		updateProgressInfo();
		return wasAdded;
	}
	
	public boolean hasEaten(ItemStack itemStack) {
		return foods.contains(new FoodInstance(itemStack));
	}
	
	public void clearFood() {
		foods.clear();
		updateProgressInfo();
	}
	
	public Set<FoodInstance> getFoodList() {
		return new HashSet<>(foods);
	}
	
	public ProgressInfo getProgressInfo() {
		return progressInfo;
	}
	
	/** don't use this client-side! it'll overwrite it with client-side config values */
	public void updateProgressInfo() {
		progressInfo = new ProgressInfo(this);
	}
}
