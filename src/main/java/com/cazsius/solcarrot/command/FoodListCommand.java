package com.cazsius.solcarrot.command;

import com.cazsius.solcarrot.lib.Localization;
import com.cazsius.solcarrot.tracking.*;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.*;

import static net.minecraft.command.Commands.argument;
import static net.minecraft.command.Commands.literal;

public final class FoodListCommand {
	private static final String name = "foodlist";
	private static final Style feedbackStyle = new Style().setColor(TextFormatting.DARK_AQUA);
	
	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(literal(name)
			.then(withPlayerArgumentOrSender(literal("size"), FoodListCommand::showFoodListSize))
			.then(withPlayerArgumentOrSender(literal("sync"), FoodListCommand::syncFoodList))
			.then(withPlayerArgumentOrSender(literal("clear"), FoodListCommand::clearFoodList))
		);
	}
	
	@FunctionalInterface
	private interface CommandWithPlayer {
		int run(CommandContext<CommandSource> context, PlayerEntity target) throws CommandSyntaxException;
	}
	
	static ArgumentBuilder<CommandSource, ?> withPlayerArgumentOrSender(ArgumentBuilder<CommandSource, ?> base, CommandWithPlayer command) {
		String target = "target";
		return base
			.executes((context) -> command.run(context, context.getSource().asPlayer()))
			.then(argument(target, EntityArgument.player())
				.executes((context) -> command.run(context, EntityArgument.getPlayer(context, target)))
			);
	}
	
	static int showFoodListSize(CommandContext<CommandSource> context, PlayerEntity target) {
		ProgressInfo progressInfo = FoodList.get(target).getProgressInfo();
		
		ITextComponent progressDesc = localizedQuantityComponent("size.desc.foods_eaten", progressInfo.foodsEaten);
		sendFeedback(context.getSource(), progressDesc);
		
		ITextComponent milestoneDesc = progressInfo.hasReachedMax()
			? localizedComponent("size.desc.milestone.max")
			: localizedComponent("size.desc.milestone.more", progressInfo.foodsUntilNextMilestone());
		sendFeedback(context.getSource(), milestoneDesc);
		
		return Command.SINGLE_SUCCESS;
	}
	
	static int syncFoodList(CommandContext<CommandSource> context, PlayerEntity target) {
		CapabilityHandler.syncFoodList(target);
		
		sendFeedback(context.getSource(), localizedComponent("sync.success"));
		return Command.SINGLE_SUCCESS;
	}
	
	static int clearFoodList(CommandContext<CommandSource> context, PlayerEntity target) {
		boolean isOp = context.getSource().hasPermissionLevel(2);
		boolean isTargetingSelf = isTargetingSelf(context, target);
		if (!isOp && isTargetingSelf)
			throw new CommandException(localizedComponent("no_permissions"));
		
		FoodList.get(target).clearFood();
		CapabilityHandler.syncFoodList(target);
		
		ITextComponent feedback = localizedComponent("clear.success");
		sendFeedback(context.getSource(), feedback);
		if (!isTargetingSelf) {
			target.sendMessage(feedback.setStyle(feedbackStyle));
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	static void sendFeedback(CommandSource source, ITextComponent message) {
		source.sendFeedback(message.setStyle(feedbackStyle), true);
	}
	
	static boolean isTargetingSelf(CommandContext<CommandSource> context, PlayerEntity target) {
		return target.isEntityEqual(context.getSource().getEntity());
	}
	
	static ITextComponent localizedComponent(String path, Object... args) {
		return Localization.localizedComponent("command", localizationPath(path), args);
	}
	
	static ITextComponent localizedQuantityComponent(String path, int number) {
		return Localization.localizedQuantityComponent("command", localizationPath(path), number);
	}
	
	static String localizationPath(String path) {
		return FoodListCommand.name + "." + path;
	}
}
