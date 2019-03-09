package com.cazsius.solcarrot.lib;

import com.cazsius.solcarrot.SOLCarrot;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import squeek.applecore.api.AppleCoreAPI;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(value = Side.CLIENT, modid = SOLCarrot.MOD_ID)
public class FoodItemStacks {
	private static List<ItemStack> foods;
	
	/** @return a list of all item stacks that can be eaten */
	public static List<ItemStack> getAllFoods() {
		return foods;
	}
	
	@SubscribeEvent
	public static void init(SOLCarrot.InitializationEvent event) {
		foods = ForgeRegistries.ITEMS.getValuesCollection().stream()
			.flatMap(FoodItemStacks::getSubItems)
			.filter(itemStack -> AppleCoreAPI.accessor.isFood(itemStack))
			.sorted(Comparator.comparing(food -> I18n.format(food.getTranslationKey() + ".name")))
			.collect(Collectors.toList());
	}
	
	private static Stream<ItemStack> getSubItems(Item item) {
		NonNullList<ItemStack> subItems = NonNullList.create();
		Arrays.stream(item.getCreativeTabs())
			.filter(Objects::nonNull)
			.forEach(creativeTab -> item.getSubItems(creativeTab, subItems));
		return subItems.stream();
	}
}
