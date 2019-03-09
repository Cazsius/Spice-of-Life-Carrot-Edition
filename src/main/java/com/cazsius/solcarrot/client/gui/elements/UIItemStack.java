package com.cazsius.solcarrot.client.gui.elements;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.config.GuiUtils;

import java.awt.*;
import java.util.List;
import java.util.Optional;

import static net.minecraft.client.util.ITooltipFlag.TooltipFlags.ADVANCED;
import static net.minecraft.client.util.ITooltipFlag.TooltipFlags.NORMAL;

public class UIItemStack extends UIElement {
	public static final int size = 16;
	
	public ItemStack itemStack;
	
	public UIItemStack(ItemStack itemStack) {
		super(new Rectangle(size, size));
		
		this.itemStack = itemStack;
	}
	
	@Override
	protected void render() {
		super.render();
		
		mc.getRenderItem().renderItemIntoGUI(
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
	protected void renderTooltip(int mouseX, int mouseY) {
		List<String> tooltip = itemStack.getTooltip(mc.player, mc.gameSettings.advancedItemTooltips ? ADVANCED : NORMAL);
		
		GuiUtils.preItemToolTip(itemStack);
		ScaledResolution resolution = new ScaledResolution(mc);
		GuiUtils.drawHoveringText(
			itemStack,
			tooltip,
			mouseX, mouseY,
			resolution.getScaledWidth(), resolution.getScaledHeight(),
			-1,
			Optional.ofNullable(itemStack.getItem().getFontRenderer(itemStack)).orElse(fontRenderer)
		);
		GuiUtils.postItemToolTip();
	}
}
