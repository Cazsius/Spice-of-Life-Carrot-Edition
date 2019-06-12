package com.cazsius.solcarrot.client;

import com.cazsius.solcarrot.SOLCarrot;
import com.cazsius.solcarrot.SOLCarrotConfig;
import com.cazsius.solcarrot.tracking.FoodCapability;
import com.cazsius.solcarrot.tracking.ProgressInfo;
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
		ProgressInfo.ConfigInfo configInfo = foodCapability.getConfigInfo();
		boolean hasBeenEaten = foodCapability.hasEaten(food);
		boolean isAllowed = configInfo.isAllowed(food);
		boolean isHearty = configInfo.isHearty(food);
		
		List<String> tooltip = event.getToolTip();
		if (!isAllowed) {
			if (hasBeenEaten) {
				tooltip.add(TextFormatting.DARK_RED + localizedTooltip("disabled.eaten"));
			}
			String key = configInfo.hasWhitelist() ? "whitelist" : "blacklist";
			tooltip.add(TextFormatting.DARK_GRAY + localizedTooltip("disabled." + key));
		} else if (isHearty) {
			if (hasBeenEaten) {
				tooltip.add(TextFormatting.DARK_GREEN + localizedTooltip("hearty.eaten"));
			} else {
				tooltip.add(TextFormatting.DARK_AQUA + localizedTooltip("hearty.not_eaten"));
			}
		} else {
			if (hasBeenEaten) {
				tooltip.add(TextFormatting.DARK_RED + localizedTooltip("cheap.eaten"));
			}
			tooltip.add(TextFormatting.DARK_GRAY + localizedTooltip("cheap"));
		}
	}
	
	private static String localizedTooltip(String path) {
		return localized("tooltip", path);
	}
}