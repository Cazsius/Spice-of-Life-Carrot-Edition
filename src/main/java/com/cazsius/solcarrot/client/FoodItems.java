package com.cazsius.solcarrot.client;

import com.cazsius.solcarrot.SOLCarrot;
import com.cazsius.solcarrot.SOLCarrotConfig;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.stream.Collectors;

import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = SOLCarrot.MOD_ID, bus = MOD)
public final class FoodItems {
	private static List<Item> foodsBeforeBlacklist;
	private static List<Item> foods;
	
	/** @return a list of all item stacks that can be eaten, including blacklisted/hidden ones */
	public static List<Item> getAllFoodsIgnoringBlacklist() {
		return new ArrayList<>(foodsBeforeBlacklist);
	}
	
	/** @return a list of all item stacks that can be eaten */
	public static List<Item> getAllFoods() {
		return new ArrayList<>(foods);
	}
	
	@SubscribeEvent
	public static void setUp(FMLCommonSetupEvent event) {
		foodsBeforeBlacklist = ForgeRegistries.ITEMS.getValues().stream()
			.filter(Item::isEdible)
			// sort by name
			.sorted(Comparator.comparing(food -> I18n.get(food.getDescriptionId() + ".name")))
			.collect(Collectors.toList());
		
		applyBlacklist();
	}
	
	@SubscribeEvent
	public static void onConfigUpdate(ModConfig.ModConfigEvent event) {
		if (event.getConfig().getType() == ModConfig.Type.CLIENT) return;
		
		applyBlacklist();
	}
	
	private static void applyBlacklist() {
		foods = foodsBeforeBlacklist.stream()
			.filter(SOLCarrotConfig::isAllowed)
			.collect(Collectors.toList());
	}
}
