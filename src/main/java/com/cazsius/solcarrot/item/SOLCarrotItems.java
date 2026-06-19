package com.cazsius.solcarrot.item;

import com.cazsius.solcarrot.SOLCarrot;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = SOLCarrot.MOD_ID)
public final class SOLCarrotItems {
	private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SOLCarrot.MOD_ID);

	public static final DeferredItem<FoodBookItem> FOOD_BOOK = ITEMS.registerItem("food_book", (properties) ->
			new FoodBookItem(properties.setId(getKey("food_book"))));

	private static ResourceKey<Item> getKey(String name) {
		return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(SOLCarrot.MOD_ID, name));
	}

	public static void setUp(IEventBus eventBus) {
		ITEMS.register(eventBus);
	}

	@SubscribeEvent
	public static void buildCreativeTabs(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
			event.accept(FOOD_BOOK);
		}
	}
}
