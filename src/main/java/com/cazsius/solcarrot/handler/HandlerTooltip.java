package com.cazsius.solcarrot.handler;

import com.cazsius.solcarrot.capability.FoodCapability;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import squeek.applecore.api.AppleCoreAPI;

import java.util.List;

import static com.cazsius.solcarrot.lib.Localization.localized;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(Side.CLIENT)
public class HandlerTooltip {
	
	private static boolean isValidFood(ItemStack itemStack) {
		return AppleCoreAPI.accessor.isFood(itemStack);
	}

	@SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event) {
		if (!HandlerConfiguration.isFoodTooltipEnabled()) return;
		
		if (event.getEntityPlayer() == null) return;
		EntityPlayer player = event.getEntityPlayer();
		
		if (!isValidFood(event.getItemStack())) return;
		Item foodJustEaten = event.getItemStack().getItem();
		
		FoodCapability food = player.getCapability(FoodCapability.FOOD_CAPABILITY, null);
		assert food != null;
		boolean hasBeenEaten = food.hasEaten(foodJustEaten, event.getItemStack().getMetadata());
		
		List<String> tooltip = event.getToolTip();
		
		if (hasBeenEaten) {
			String formatting = "" + TextFormatting.DARK_GRAY + TextFormatting.ITALIC;
			tooltip.add(formatting + localized("tooltip", "eaten"));
		} else {
			String formatting = "" + TextFormatting.DARK_AQUA + TextFormatting.ITALIC;
			tooltip.add(formatting + localized("tooltip", "not_eaten_1"));
			tooltip.add(formatting + localized("tooltip", "not_eaten_2"));
		}
	}
}