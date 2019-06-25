package com.cazsius.solcarrot.item;

import com.cazsius.solcarrot.SOLCarrot;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;

import static net.minecraftforge.fml.relauncher.Side.CLIENT;

@Mod.EventBusSubscriber(modid = SOLCarrot.MOD_ID)
public final class SOLCarrotItems {
	private static final List<Item> ITEMS = new ArrayList<>(); // has to be on top so it's loaded first
	
	public static final ItemFoodBook foodBook = item(new ItemFoodBook(), "food_book");
	
	private static <I extends Item> I item(I item, String name) {
		item.setRegistryName(SOLCarrot.resourceLocation(name));
		item.setTranslationKey(SOLCarrot.namespaced(name));
		ITEMS.add(item);
		return item;
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();
		ITEMS.forEach(registry::register);
	}
	
	@SubscribeEvent
	@SideOnly(CLIENT)
	public static void registerModels(@SuppressWarnings("unused") ModelRegistryEvent event) {
		ITEMS.forEach(item -> {
			ResourceLocation registryName = item.getRegistryName();
			assert registryName != null;
			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(registryName, "inventory"));
		});
	}
	
	private SOLCarrotItems() {}
}
