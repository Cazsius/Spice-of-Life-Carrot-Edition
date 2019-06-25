package com.cazsius.solcarrot.client.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;

@SideOnly(Side.CLIENT)
public abstract class UIElement {
	public static void render(UIElement element, int mouseX, int mouseY) {
		render(singletonList(element), mouseX, mouseY);
	}
	
	public static void render(List<UIElement> elements, int mouseX, int mouseY) {
		elements.forEach(UIElement::render);
		
		elements.stream()
			.flatMap(UIElement::getRecursiveChildren)
			.filter(element -> element.hasTooltip() && element.frame.contains(mouseX, mouseY))
			.reduce((one, two) -> two) // last element was rendered last and is thus visually on top
			.ifPresent(element -> element.renderTooltip(mouseX, mouseY));
	}
	
	protected static final Minecraft mc = Minecraft.getMinecraft();
	protected static final FontRenderer fontRenderer = mc.fontRenderer;
	
	public Rectangle frame;
	@Nullable
	public String tooltip;
	protected final List<UIElement> children = new ArrayList<>();
	
	public UIElement(Rectangle frame) {
		this.frame = frame;
	}
	
	/**
	 Renders the element to the screen. Note that no transforms have been applied, so you should take your position into account!
	 */
	protected void render() {
		children.forEach(UIElement::render);
	}
	
	private Stream<UIElement> getRecursiveChildren() {
		return Stream.concat(
			Stream.of(this),
			children.stream().flatMap(UIElement::getRecursiveChildren)
		);
	}
	
	/**
	 @return whether or not the element has a tooltip. This is used to determine which of multiple overlapping tooltips to render.
	 */
	protected boolean hasTooltip() {
		return tooltip != null;
	}
	
	/**
	 Renders the tooltip at the given position.
	 @param mouseX the mouse's x position
	 @param mouseY the mouse's y position
	 */
	protected void renderTooltip(int mouseX, int mouseY) {
		if (tooltip == null) return;
		ScaledResolution resolution = new ScaledResolution(mc);
		GuiUtils.drawHoveringText(
			ItemStack.EMPTY,
			Collections.singletonList(tooltip),
			mouseX, mouseY,
			resolution.getScaledWidth(), resolution.getScaledHeight(),
			-1,
			fontRenderer
		);
	}
	
	/** calculates and sets the frame to the smallest rectangle enclosing all children's frames */
	protected void calculateFrameFromChildren() {
		setMinX(children.stream().mapToInt(UIElement::getMinX).min().orElse(getMinX()));
		setWidth(children.stream().mapToInt(UIElement::getMaxX).max().orElse(getMaxX()) - getMinX());
		
		setMinY(children.stream().mapToInt(UIElement::getMinY).min().orElse(getMinY()));
		setHeight(children.stream().mapToInt(UIElement::getMaxY).max().orElse(getMaxY()) - getMinY());
	}
	
	public final int getCenterX() {
		return frame.x + frame.width / 2;
	}
	
	public final void setCenterX(int centerX) {
		frame.setLocation(centerX - frame.width / 2, frame.y);
	}
	
	public final int getCenterY() {
		return frame.y + frame.height / 2;
	}
	
	public final void setCenterY(int centerY) {
		frame.setLocation(frame.x, centerY - frame.height / 2);
	}
	
	public final int getMinX() {
		return frame.x;
	}
	
	public final void setMinX(int minX) {
		frame.setLocation(minX, frame.y);
	}
	
	public final int getMinY() {
		return frame.y;
	}
	
	public final void setMinY(int minY) {
		frame.setLocation(frame.x, minY);
	}
	
	public final int getMaxX() {
		return frame.x + frame.width;
	}
	
	public final void setMaxX(int maxX) {
		frame.setLocation(maxX - frame.width, frame.y);
	}
	
	public final int getMaxY() {
		return frame.y + frame.height;
	}
	
	public final void setMaxY(int maxY) {
		frame.setLocation(frame.x, maxY - frame.height);
	}
	
	public final int getWidth() {
		return frame.width;
	}
	
	public final void setWidth(int width) {
		frame.width = width;
	}
	
	public final int getHeight() {
		return frame.height;
	}
	
	public final void setHeight(int height) {
		frame.height = height;
	}
	
	public final void setSize(int width, int height) {
		setWidth(width);
		setHeight(height);
	}
}
