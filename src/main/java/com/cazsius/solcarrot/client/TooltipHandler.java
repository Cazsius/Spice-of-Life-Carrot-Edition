package com.cazsius.solcarrot.client;

import com.cazsius.solcarrot.SOLCarrot;
import com.cazsius.solcarrot.SOLCarrotConfig;
import com.cazsius.solcarrot.tracking.FoodList;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import static com.cazsius.solcarrot.lib.Localization.localizedComponent;

@EventBusSubscriber(value = Dist.CLIENT, modid = SOLCarrot.MOD_ID)
public final class TooltipHandler {
	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onItemTooltip(ItemTooltipEvent event) {
		if (!SOLCarrotConfig.isFoodTooltipEnabled()) return;

		Player player = event.getEntity();
		if (player == null) return;

		ItemStack food = event.getItemStack();
		if (food.get(DataComponents.FOOD) == null) return;

		FoodList foodList = FoodList.get(player);
		boolean hasBeenEaten = foodList.hasEaten(food);
		boolean isAllowed = SOLCarrotConfig.isAllowed(food.getItem());
		boolean isHearty = SOLCarrotConfig.isHearty(food);

		var tooltip = event.getToolTip();
		if (!isAllowed) {
			if (hasBeenEaten) {
				tooltip.add(localizedTooltip("disabled.eaten", ChatFormatting.DARK_RED));
			}
			String key = SOLCarrotConfig.hasWhitelist() ? "whitelist" : "blacklist";
			tooltip.add(localizedTooltip("disabled." + key, ChatFormatting.DARK_GRAY));
		} else if (isHearty) {
			if (hasBeenEaten) {
				tooltip.add(localizedTooltip("hearty.eaten", ChatFormatting.DARK_GREEN));
			} else {
				tooltip.add(localizedTooltip("hearty.not_eaten", ChatFormatting.DARK_AQUA));
			}
		} else {
			if (hasBeenEaten) {
				tooltip.add(localizedTooltip("cheap.eaten", ChatFormatting.DARK_RED));
			}
			tooltip.add(localizedTooltip("cheap", ChatFormatting.DARK_GRAY));
		}
	}

	private static MutableComponent localizedTooltip(String path, ChatFormatting color) {
		return localizedComponent("tooltip", path).withStyle(color);
	}

	private TooltipHandler() {
	}
}
