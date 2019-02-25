package com.cazsius.solcarrot.command;

import com.cazsius.solcarrot.lib.ProgressInfo;
import net.minecraft.entity.player.EntityPlayer;

import static com.cazsius.solcarrot.lib.Localization.localized;
import static com.cazsius.solcarrot.lib.Localization.localizedQuantity;

public final class CommandSizeFoodList extends Command {
	
	@Override
	public String getName() {
		return "sizefoodlist";
	}
	
	@Override
	void execute(EntityPlayer player, String[] args) {
		ProgressInfo progressInfo = ProgressInfo.getProgressInfo(player);
		
		String progressDesc = localizedQuantity("command", "sizefoodlist.desc.foods_eaten", progressInfo.foodsEaten);
		
		String milestoneDesc = progressInfo.hasReachedMax()
				? localized("command", "sizefoodlist.desc.milestone.max")
				: localized("command", "sizefoodlist.desc.milestone.more", progressInfo.foodsUntilNextMilestone());
		
		showMessage(player, progressDesc + "\n" + milestoneDesc);
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}
}
