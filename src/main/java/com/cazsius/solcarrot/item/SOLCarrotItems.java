package com.cazsius.solcarrot.item;

import com.cazsius.solcarrot.SOLCarrot;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD;

@Mod.EventBusSubscriber(modid = SOLCarrot.MOD_ID, bus = MOD)
public final class SOLCarrotItems {
	private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SOLCarrot.MOD_ID);
	
	public static final RegistryObject<Item> FOOD_BOOK = ITEMS.register("food_book", FoodBookItem::new);
	
	public static void setUp() {
		var eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ITEMS.register(eventBus);
	}
	
	@SubscribeEvent
	public static void buildCreativeTabs(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
			event.accept(FOOD_BOOK);
		}
	}
}
