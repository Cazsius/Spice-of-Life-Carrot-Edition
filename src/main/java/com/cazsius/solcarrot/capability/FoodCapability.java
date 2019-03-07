package com.cazsius.solcarrot.capability;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.*;

import java.util.*;

public final class FoodCapability implements ICapabilitySerializable<NBTBase> {
	public static FoodCapability get(EntityPlayer player) {
		FoodCapability foodCapability = player.getCapability(FoodCapability.FOOD_CAPABILITY, null);
		assert foodCapability != null;
		return foodCapability;
	}
	
	@CapabilityInject(FoodCapability.class)
	public static Capability<FoodCapability> FOOD_CAPABILITY;
	
	private final Set<FoodInstance> foodList = new HashSet<>();
	
	public FoodCapability() {}
	
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
		for (FoodInstance food : foodList) {
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
			
			addFood(new FoodInstance(item, meta));
		}
	}
	
	public void addFood(ItemStack itemStack) {
		addFood(new FoodInstance(itemStack));
	}
	
	private void addFood(FoodInstance food) {
		foodList.add(food);
	}
	
	public int getCount() {
		return foodList.size();
	}
	
	public boolean hasEaten(ItemStack itemStack) {
		return foodList.contains(new FoodInstance(itemStack));
	}
	
	public void clearFood() {
		foodList.clear();
	}
	
	public void copyFoods(FoodCapability food) {
		foodList.clear();
		foodList.addAll(food.foodList);
	}
	
	public List<FoodInstance> getEatenFoods() {
		return new ArrayList<>(foodList);
	}
}
