package com.cazsius.solcarrot.capability;

import java.util.ArrayList;
import java.util.HashSet;
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

	public Set<Item> foodList = new HashSet<>();

	public FoodCapability() {
	}

	public void addFood(Item item) {
		foodList.add(item);
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
		for (Item items : this.foodList) {
			list.appendTag(new NBTTagString(((ResourceLocation) Item.REGISTRY.getNameForObject(items)).toString()));
		}
		return list;
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		NBTTagList list = (NBTTagList) nbt;
		for (int i = 0; i < list.tagCount(); i++) {
			this.addFood(Item.getByNameOrId(((NBTTagString) list.get(i)).getString()));
		}
	}

	public int getCount() {
		return foodList.size();
	}

	public boolean hasEaten(Item foodJustEaten) 
	{
		System.out.println(foodJustEaten);
		System.out.println(foodList);
		return foodList.contains(foodJustEaten);
	}

}
