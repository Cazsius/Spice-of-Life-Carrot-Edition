package com.cazsius.solcarrot.command;

import com.cazsius.solcarrot.capability.FoodCapability;
import com.cazsius.solcarrot.handler.HandlerCapability;
import com.cazsius.solcarrot.handler.MaxHealthHandler;
import net.minecraft.entity.player.EntityPlayer;

import static com.cazsius.solcarrot.lib.Localization.localized;

public final class CommandClearFoodList extends Command {
	
	@Override
	public String getName() {
		return "clearfoodlist";
	}
	
	@Override
	void execute(EntityPlayer player, FoodCapability foodCapability, String[] args) {
		foodCapability.clearFood();
		HandlerCapability.syncFoodList(player);
		MaxHealthHandler.updateFoodHPModifier(player);
		showMessage(player, localized("command", "clearfoodlist.success"));
	}
}
