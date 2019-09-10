package com.cazsius.solcarrot.client;

import com.cazsius.solcarrot.SOLCarrot;
import com.cazsius.solcarrot.SOLCarrotConfig;
import com.cazsius.solcarrot.tracking.FoodList;
import com.cazsius.solcarrot.tracking.ProgressInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

import static com.cazsius.solcarrot.lib.Localization.localizedComponent;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = SOLCarrot.MOD_ID)
public final class TooltipHandler {
	@SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event) {
		if (!SOLCarrotConfig.isFoodTooltipEnabled) return;
		
		if (event.getEntityPlayer() == null) return;
		PlayerEntity player = event.getEntityPlayer();
		
		Item food = event.getItemStack().getItem();
		if (!food.isFood()) return;
		
		FoodList foodList = FoodList.get(player);
		boolean hasBeenEaten = foodList.hasEaten(food);
		boolean isAllowed = SOLCarrotConfig.isAllowed(food);
		boolean isHearty = SOLCarrotConfig.isHearty(food);
		
		List<ITextComponent> tooltip = event.getToolTip();
		if (!isAllowed) {
			if (hasBeenEaten) {
				tooltip.add(localizedTooltip("disabled.eaten", TextFormatting.DARK_RED));
			}
			String key = SOLCarrotConfig.hasWhitelist() ? "whitelist" : "blacklist";
			tooltip.add(localizedTooltip("disabled." + key, TextFormatting.DARK_GRAY));
		} else if (isHearty) {
			if (hasBeenEaten) {
				tooltip.add(localizedTooltip("hearty.eaten", TextFormatting.DARK_GREEN));
			} else {
				tooltip.add(localizedTooltip("hearty.not_eaten", TextFormatting.DARK_AQUA));
			}
		} else {
			if (hasBeenEaten) {
				tooltip.add(localizedTooltip("cheap.eaten", TextFormatting.DARK_RED));
			}
			tooltip.add(localizedTooltip("cheap", TextFormatting.DARK_GRAY));
		}
	}
	
	private static ITextComponent localizedTooltip(String path, TextFormatting color) {
		Style style = new Style().setColor(color);
		return localizedComponent("tooltip", path).setStyle(style);
	}
	
	private TooltipHandler() {}
}
