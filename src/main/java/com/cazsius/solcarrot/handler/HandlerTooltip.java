package com.cazsius.solcarrot.handler;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import squeek.applecore.api.AppleCoreAPI;

import java.util.*;

import com.cazsius.solcarrot.capability.FoodCapability;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class HandlerTooltip {
	
	public static boolean isValidFood(ItemStack itemStack) {
		return AppleCoreAPI.accessor.isFood(itemStack);
	}

	@SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event) {
		if (HandlerConfiguration.isFoodTooltipEnabled() && event.getEntityPlayer() != null
				&& event.getItemStack() != null && isValidFood(event.getItemStack())) {
			EntityPlayer player = event.getEntityPlayer();
			Item foodJustEaten = event.getItemStack().getItem();
			FoodCapability food = player.getCapability(FoodCapability.FOOD_CAPABILITY, null);
			boolean hasBeenEaten = food.hasEaten(foodJustEaten, event.getItemStack().getMetadata());

			List<String> toolTipStringsToAdd = new ArrayList<String>();

			if (hasBeenEaten) {
				toolTipStringsToAdd.add(TextFormatting.DARK_GRAY.toString() + TextFormatting.ITALIC
						+ I18n.format("solcarrot.tooltip.hpeaten"));
			} else {
				toolTipStringsToAdd.add(TextFormatting.DARK_AQUA.toString() + TextFormatting.ITALIC
						+ I18n.format("solcarrot.tooltip.noteaten1"));
				toolTipStringsToAdd.add(TextFormatting.DARK_AQUA.toString() + TextFormatting.ITALIC
						+ I18n.format("solcarrot.tooltip.noteaten2"));
			}

			event.getToolTip().addAll(toolTipStringsToAdd);

		}
	}
}