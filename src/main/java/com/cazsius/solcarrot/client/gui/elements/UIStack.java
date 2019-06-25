package com.cazsius.solcarrot.client.gui.elements;

import java.awt.*;
import java.util.List;

public final class UIStack extends UIElement {
	public Axis axis = Axis.HORIZONTAL;
	public int spacing = 1;
	
	public UIStack() {
		super(new Rectangle(0, 0));
	}
	
	public void addChild(UIElement child) {
		children.add(child);
		updateFrames();
	}
	
	public List<UIElement> getChildren() {
		return children;
	}
	
	public void updateFrames() {
		switch (axis) {
			case HORIZONTAL:
				int x = 0;
				int height = children.stream().mapToInt(child -> child.frame.height).max().orElse(0);
				for (UIElement child : children) {
					child.frame.x = frame.x + x;
					child.frame.y = frame.y + (height - child.frame.height) / 2;
					if (child instanceof UIStack) {
						((UIStack) child).updateFrames();
					}
					x += child.frame.width;
					x += spacing;
				}
				frame.width = x - spacing;
				frame.height = height;
				break;
			case VERTICAL:
				int y = 0;
				int width = children.stream().mapToInt(child -> child.frame.width).max().orElse(0);
				for (UIElement child : children) {
					child.frame.x = frame.x + (width - child.frame.width) / 2;
					child.frame.y = frame.y + y;
					if (child instanceof UIStack) {
						((UIStack) child).updateFrames();
					}
					y += child.frame.height;
					y += spacing;
				}
				frame.width = width;
				frame.height = y - spacing;
				break;
			default:
				throw new AssertionError();
		}
	}
	
	public enum Axis {
		HORIZONTAL, VERTICAL
	}
}
