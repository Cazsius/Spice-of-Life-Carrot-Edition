package com.cazsius.solcarrot.client;

import com.cazsius.solcarrot.SOLCarrot;
import com.cazsius.solcarrot.SOLCarrotConfig;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@EventBusSubscriber(value = Dist.CLIENT, modid = SOLCarrot.MOD_ID)
public final class FoodItems {
	private static List<Item> foodsBeforeBlacklist;
	private static List<Item> foods;

	/**
	 * @return a list of all item stacks that can be eaten, including blacklisted/hidden ones
	 */
	public static List<Item> getAllFoodsIgnoringBlacklist() {
		return new ArrayList<>(foodsBeforeBlacklist);
	}

	/**
	 * @return a list of all item stacks that can be eaten
	 */
	public static List<Item> getAllFoods() {
		return new ArrayList<>(foods);
	}

	/**
	 * Constructs a list of all food items that are allowed to be eaten,
	 * And applies the blacklist from the config after the list is constructed.
	 */
	public static void setUp() {
		foodsBeforeBlacklist = BuiltInRegistries.ITEM.stream()
				.filter((item) -> item.getDefaultInstance().has(DataComponents.FOOD))
				// sort by name
				.sorted(Comparator.comparing(food -> I18n.get(food.getDescriptionId() + ".name")))
				.collect(Collectors.toList());

		// depending on the other mods involved, config might be loaded before or after this event
		tryApplyBlacklist();
	}

	@SubscribeEvent
	public static void onConfigLoad(ModConfigEvent.Loading event) {
		if (event.getConfig().getType() == ModConfig.Type.CLIENT) return;
	}

	@SubscribeEvent
	public static void onConfigUpdate(ModConfigEvent.Reloading event) {
		if (event.getConfig().getType() == ModConfig.Type.CLIENT) return;

		tryApplyBlacklist();
	}

	private static void tryApplyBlacklist() {
		if (foodsBeforeBlacklist == null) return;
		if (!SOLCarrotConfig.SERVER_SPEC.isLoaded()) return;

		foods = foodsBeforeBlacklist.stream()
				.filter(SOLCarrotConfig::isAllowed)
				.collect(Collectors.toList());
	}
}
