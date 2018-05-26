package com.cazsius.solcarrot.command;

import com.cazsius.solcarrot.capability.FoodCapability;
import com.cazsius.solcarrot.handler.HandlerConfiguration;
import com.cazsius.solcarrot.lib.Constants;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class CommandSizeFoodArray extends CommandBase {

	@Override
	public String getName() {
		return "sizefoodlist";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return Constants.CommandMessages.SIZE_FOOD_ARRAY;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
		/*
		 * int permLevel = this.getRequiredPermissionLevel(); if(permLevel >= 0) {
		 */

		FoodCapability food = sender.getCommandSenderEntity().getCapability(FoodCapability.FOOD_CAPABILITY, null);

		int foodsEaten = food.getCount();
		int milestone = 0;
		int[] milestoneArray = HandlerConfiguration.getMilestoneArray();
		while (milestone < milestoneArray.length && foodsEaten + 1 > milestoneArray[milestone]) {
			milestone++;
		}

		TextComponentTranslation size;
		TextComponentString size_send;
		if (milestone == milestoneArray.length) {
			size = new TextComponentTranslation("solcarrot.command.sizefoodarray.desc.maxmilestone", foodsEaten,
					(foodsEaten == 1 ? "" : "s"));
			size_send = new TextComponentString(TextFormatting.DARK_AQUA + size.getUnformattedText());
		} else {
			int numFoodsTillNext = milestoneArray[milestone] - foodsEaten;
			size = new TextComponentTranslation("solcarrot.command.sizefoodarray.desc.moremilestone", foodsEaten,
					(foodsEaten == 1 ? "" : "s"), numFoodsTillNext);
			size_send = new TextComponentString(TextFormatting.DARK_AQUA + size.getUnformattedText());
		}

		player.sendStatusMessage(size_send, true);
		// }
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}
}
