package com.cazsius.solcarrot.capability;

import java.util.ArrayList;

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

	ArrayList<Item> foodList = new ArrayList<>();

	public FoodCapability() {
	}

	public void addFood(Item item) {
		if (!foodList.contains(item)) {
			foodList.add(item);
		}
	}

	@CapabilityInject(FoodCapability.class)
	public static Capability<FoodCapability> FOOD_CAPABILITY = null;

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == FOOD_CAPABILITY;
	}

	@Override
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

}
