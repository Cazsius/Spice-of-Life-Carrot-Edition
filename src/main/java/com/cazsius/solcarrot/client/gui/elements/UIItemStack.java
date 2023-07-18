package com.cazsius.solcarrot.client.gui.elements;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

import java.awt.*;

import static net.minecraft.world.item.TooltipFlag.Default.ADVANCED;
import static net.minecraft.world.item.TooltipFlag.Default.NORMAL;

public class UIItemStack extends UIElement {
	public static final int size = 16;
	
	public ItemStack itemStack;
	
	public UIItemStack(ItemStack itemStack) {
		super(new Rectangle(size, size));
		
		this.itemStack = itemStack;
	}
	
	@Override
	protected void render(GuiGraphics graphics) {
		super.render(graphics);
		
		graphics.renderItem(
			itemStack,
			frame.x + (frame.width - size) / 2,
			frame.y + (frame.height - size) / 2
		);
	}
	
	@Override
	protected boolean hasTooltip() {
		return true;
	}
	
	@Override
	protected void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
		var tooltip = itemStack.getTooltipLines(mc.player, mc.options.advancedItemTooltips ? ADVANCED : NORMAL);
		renderTooltip(graphics, itemStack, tooltip, mouseX, mouseY);
	}
}
