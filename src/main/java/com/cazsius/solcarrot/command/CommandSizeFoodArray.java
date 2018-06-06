package com.cazsius.solcarrot.command;

import com.cazsius.solcarrot.capability.FoodCapability;
import com.cazsius.solcarrot.handler.HandlerConfiguration;
import com.cazsius.solcarrot.lib.Constants;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentBase;
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
		
		FoodCapability food = sender.getCommandSenderEntity().getCapability(FoodCapability.FOOD_CAPABILITY, null);
		int foodsEaten = food.getCount();
		
		int[] milestones = HandlerConfiguration.getMilestoneArray();
		
		int nextMilestone = 0;
		boolean hasReachedMax = true;
		for (int milestone : milestones) {
			if (foodsEaten < milestone) {
				nextMilestone = milestone;
				hasReachedMax = false;
				break;
			}
		}
		
		ITextComponent progressDesc;
		if (foodsEaten == 1) {
			progressDesc = new TextComponentTranslation("solcarrot.command.sizefoodarray.desc.foodsEatenSingular");
		} else {
			progressDesc = new TextComponentTranslation("solcarrot.command.sizefoodarray.desc.foodsEatenPlural", foodsEaten);
		}
		
		ITextComponent milestoneDesc;
		if (hasReachedMax) {
			milestoneDesc = new TextComponentTranslation("solcarrot.command.sizefoodarray.desc.milestoneMax");
		} else {
			int numFoodsTillNext = nextMilestone - foodsEaten;
			milestoneDesc = new TextComponentTranslation("solcarrot.command.sizefoodarray.desc.milestoneMore", numFoodsTillNext);
		}
		
		boolean showAboveHotbar = HandlerConfiguration.shouldShowProgressAboveHotbar();
		
		ITextComponent textToSend = progressDesc;
		textToSend.appendText(showAboveHotbar ? " " : "\n"); // above-hotbar mode is single-line only :(
		textToSend.appendSibling(milestoneDesc);
		
		textToSend.getStyle().setColor(TextFormatting.DARK_AQUA);
		
		if (showAboveHotbar) {
			// messages above hotbar are stripped of their formatting, so we have to pretend we aren't formatted...
			textToSend = new TextComponentString(textToSend.getFormattedText());
		}
		
		player.sendStatusMessage(textToSend, showAboveHotbar);
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}
}
