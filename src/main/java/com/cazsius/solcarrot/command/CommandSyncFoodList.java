package com.cazsius.solcarrot.command;

import com.cazsius.solcarrot.handler.HandlerCapability;
import net.minecraft.entity.player.EntityPlayer;

import static com.cazsius.solcarrot.lib.Localization.localized;

public class CommandSyncFoodList extends Command {
	@Override
	public String getName() {
		return "syncfoodlist";
	}
	
	@Override
	void execute(EntityPlayer player, String[] args) {
		HandlerCapability.syncFoodList(player);
		showMessage(player, localized("command", "syncfoodlist.success"));
	}
}
