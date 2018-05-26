package com.cazsius.solcarrot.capability;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.item.Item;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class FoodCapability implements ICapabilitySerializable<NBTBase> {

	public Set<FoodInstance> foodList = new HashSet<>();

	public FoodCapability() {
	}

	public void addFood(Item item, int meta) {
		foodList.add(new FoodInstance(item, meta));
	}

	@CapabilityInject(FoodCapability.class)
	public static Capability<FoodCapability> FOOD_CAPABILITY = null;

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == FOOD_CAPABILITY;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == FOOD_CAPABILITY ? (T) this : null;
	}

	@Override
	public NBTBase serializeNBT() {
		NBTTagList list = new NBTTagList();
		for (FoodInstance fInstance : this.foodList) {
			ResourceLocation location = Item.REGISTRY.getNameForObject(fInstance.item());
			if (location == null)
				continue;
			String toWrite = location.toString();
			toWrite += "@" + fInstance.meta();
			list.appendTag(new NBTTagString(toWrite));
		}
		return list;
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		NBTTagList list = (NBTTagList) nbt;
		for (int i = 0; i < list.tagCount(); i++) {
			String toDecompose = ((NBTTagString) list.get(i)).getString();
			int index = toDecompose.indexOf("@");
			if (index < 0)
				continue;
			String name = toDecompose.substring(0, index);
			int meta = Integer.decode(toDecompose.substring(index + 1));
			this.addFood(Item.getByNameOrId(name), meta);
		}
	}

	public int getCount() {
		return foodList.size();
	}

	public boolean hasEaten(Item foodJustEaten, int meta) {
		return foodList.contains(new FoodInstance(foodJustEaten, meta));
	}

	public void clearFood() {
		foodList.clear();
	}

	public void copyFoods(FoodCapability food) {
		clearFood();
		foodList.addAll(food.foodList);
	}

	public List<FoodInstance> getHistory() {
		return new ArrayList<FoodInstance>(foodList);
	}
}
