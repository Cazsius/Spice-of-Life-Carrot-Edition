package com.cazsius.solcarrot.capability;

import com.cazsius.solcarrot.SOLCarrotConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.*;
import squeek.applecore.api.AppleCoreAPI;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashSet;
import java.util.Set;

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
	private ProgressInfo progressInfo = new ProgressInfo(0);
	
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
		for (FoodInstance food : foods) {
			ResourceLocation location = Item.REGISTRY.getNameForObject(food.item);
			if (location == null)
				continue;
			
			String toWrite = location + "@" + food.metadata;
			list.appendTag(new NBTTagString(toWrite));
		}
		return list;
	}
	
	/** used for persistent storage */
	@Override
	public void deserializeNBT(NBTBase nbt) {
		foods.clear();
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
			
			foods.add(new FoodInstance(item, meta));
		}
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
		boolean wasAdded = foods.add(new FoodInstance(food)) && shouldCount(food);
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
	
	private boolean shouldCount(ItemStack food) {
		return AppleCoreAPI.accessor.getFoodValues(food).hunger >= SOLCarrotConfig.minimumFoodValue;
	}
	
	public ProgressInfo getProgressInfo() {
		return progressInfo;
	}
	
	/** don't use this client-side! it'll overwrite it with client-side config values */
	public void updateProgressInfo() {
		int foodsEaten = (int) foods.stream()
			.map(FoodInstance::getItemStack)
			.filter(this::shouldCount)
			.count();
		progressInfo = new ProgressInfo(foodsEaten);
	}
}
