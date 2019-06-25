package com.cazsius.solcarrot.client;

import com.cazsius.solcarrot.SOLCarrot;
import com.cazsius.solcarrot.tracking.ProgressInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import squeek.applecore.api.AppleCoreAPI;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = SOLCarrot.MOD_ID)
public final class FoodItemStacks {
	private static List<ItemStack> foodsBeforeBlacklist;
	private static List<ItemStack> foods;
	
	/** @return a list of all item stacks that can be eaten, including blacklisted/hidden ones */
	public static List<ItemStack> getAllFoodsIgnoringBlacklist() {
		return new ArrayList<>(foodsBeforeBlacklist);
	}
	
	/** @return a list of all item stacks that can be eaten */
	public static List<ItemStack> getAllFoods() {
		return new ArrayList<>(foods);
	}
	
	@SubscribeEvent
	public static void postInit(SOLCarrot.PostInitializationEvent event) {
		foodsBeforeBlacklist = ForgeRegistries.ITEMS.getValuesCollection().stream()
			.flatMap(FoodItemStacks::getSubItems)
			.filter(itemStack -> AppleCoreAPI.accessor.isFood(itemStack))
			// sort by name, using metadata as tiebreaker
			.sorted(Comparator.comparing(ItemStack::getMetadata))
			.sorted(Comparator.comparing(food -> I18n.format(food.getTranslationKey() + ".name")))
			.collect(Collectors.toList());
		
		applyBlacklist();
	}
	
	@SubscribeEvent
	public static void onPostConfigChanged(ConfigChangedEvent.PostConfigChangedEvent event) {
		if (!event.getModID().equals(SOLCarrot.MOD_ID)) return;
		
		applyBlacklist();
	}
	
	private static void applyBlacklist() {
		ProgressInfo.ConfigInfo configInfo = new ProgressInfo.ConfigInfo();
		foods = foodsBeforeBlacklist.stream()
			.filter(configInfo::isAllowed)
			.collect(Collectors.toList());
	}
	
	private static String getRegistryName(ItemStack itemStack) {
		ResourceLocation registryName = itemStack.getItem().getRegistryName();
		assert registryName != null;
		return registryName.toString();
	}
	
	private static Stream<ItemStack> getSubItems(Item item) {
		NonNullList<ItemStack> subItems = NonNullList.create();
		Arrays.stream(item.getCreativeTabs())
			.filter(Objects::nonNull)
			.forEach(creativeTab -> item.getSubItems(creativeTab, subItems));
		return subItems.stream();
	}
	
	private FoodItemStacks() {}
}
