package com.cazsius.solcarrot.client;

import com.cazsius.solcarrot.SOLCarrot;
import com.cazsius.solcarrot.SOLCarrotConfig;
import com.cazsius.solcarrot.tracking.FoodCapability;
import net.minecraft.entity.player.EntityPlayer;
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
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = SOLCarrot.MOD_ID)
public class TooltipHandler {
	private static boolean isValidFood(ItemStack itemStack) {
		return AppleCoreAPI.accessor.isFood(itemStack);
	}
	
	@SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event) {
		if (!SOLCarrotConfig.isFoodTooltipEnabled) return;
		
		if (event.getEntityPlayer() == null) return;
		EntityPlayer player = event.getEntityPlayer();
		
		ItemStack food = event.getItemStack();
		if (!isValidFood(food)) return;
		
		FoodCapability foodCapability = FoodCapability.get(player);
		boolean hasBeenEaten = foodCapability.hasEaten(food);
		boolean shouldCount = foodCapability.getProgressInfo().shouldCount(food);
		
		String darkGray = "" + TextFormatting.DARK_GRAY + TextFormatting.ITALIC;
		String darkAqua = "" + TextFormatting.DARK_AQUA + TextFormatting.ITALIC;
		
		List<String> tooltip = event.getToolTip();
		if (hasBeenEaten) {
			tooltip.add(darkGray + localized("tooltip", "eaten." + (shouldCount ? "hearty" : "cheap")));
		} else {
			tooltip.add(darkAqua + localized("tooltip", "not_eaten.common"));
			if (shouldCount) {
				tooltip.add(darkAqua + localized("tooltip", "not_eaten.hearty"));
			}
		}
		if (!shouldCount) {
			tooltip.add(darkGray + localized("tooltip", "cheap"));
		}
	}
}