package com.cazsius.solcarrot.capability;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.*;

import java.util.*;

public class FoodCapability implements ICapabilitySerializable<NBTBase> {
	
	public static FoodCapability get(EntityPlayer player) {
		FoodCapability foodCapability = player.getCapability(FoodCapability.FOOD_CAPABILITY, null);
		assert foodCapability != null;
		return foodCapability;
	}
	
	public Set<FoodInstance> foodList = new HashSet<>();
	
	public FoodCapability() {}
	
	public void addFood(Item item, int meta) {
		foodList.add(new FoodInstance(item, meta));
	}
	
	@CapabilityInject(FoodCapability.class)
	public static Capability<FoodCapability> FOOD_CAPABILITY;
	
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
		for (FoodInstance food : this.foodList) {
			ResourceLocation location = Item.REGISTRY.getNameForObject(food.item);
			if (location == null)
				continue;
			
			String toWrite = location + "@" + food.metadata;
			list.appendTag(new NBTTagString(toWrite));
		}
		return list;
	}
	
	@Override
	public void deserializeNBT(NBTBase nbt) {
		NBTTagList list = (NBTTagList) nbt;
		for (int i = 0; i < list.tagCount(); i++) {
			String toDecompose = ((NBTTagString) list.get(i)).getString();
			
			String[] parts = toDecompose.split("@");
			String name = parts[0];
			int meta;
			if (parts.length > 1) {
				meta = Integer.decode(parts[1]);
			} else {
				meta = 0;
			}
			
			Item item = Item.getByNameOrId(name);
			if (item == null)
				continue; // TODO it'd be nice to store (and maybe even count) references to missing items, in case the mod is added back in later
			
			this.addFood(item, meta);
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
		return new ArrayList<>(foodList);
	}
}
