package com.cazsius.solcarrot.item;

import com.cazsius.solcarrot.SOLCarrot;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;

import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD;

@Mod.EventBusSubscriber(modid = SOLCarrot.MOD_ID, bus = MOD)
public final class SOLCarrotItems {
	@SubscribeEvent
	public static void registerItems(RegisterEvent event) {
		event.register(ForgeRegistries.Keys.ITEMS, helper
			-> helper.register("food_book", new FoodBookItem()));
	}
}
