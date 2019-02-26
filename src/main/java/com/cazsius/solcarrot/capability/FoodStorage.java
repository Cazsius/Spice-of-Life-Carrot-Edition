package com.cazsius.solcarrot.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class FoodStorage implements IStorage<FoodCapability> {
	
	@Override
	public NBTBase writeNBT(Capability<FoodCapability> capability, FoodCapability instance, EnumFacing side) {
		return instance.serializeNBT();
	}
	
	@Override
	public void readNBT(Capability<FoodCapability> capability, FoodCapability instance, EnumFacing side, NBTBase nbt) {
		instance.deserializeNBT(nbt);
	}
}
