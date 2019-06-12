package com.cazsius.solcarrot.client.gui;

import com.cazsius.solcarrot.client.gui.elements.*;

import java.awt.*;

import static com.cazsius.solcarrot.lib.Localization.localized;

abstract class Page extends UIElement {
	final UIStack mainStack;
	final int spacing = 6;
	
	Page(Rectangle frame, String header) {
		super(frame);
		
		mainStack = new UIStack();
		mainStack.axis = UIStack.Axis.VERTICAL;
		mainStack.spacing = spacing;
		
		UILabel headerLabel = new UILabel(header);
		mainStack.addChild(headerLabel);
		
		mainStack.addChild(makeSeparatorLine());
		
		children.add(mainStack);
		updateMainStack();
	}
	
	void updateMainStack() {
		mainStack.setCenterX(getCenterX());
		mainStack.setMinY(getMinY() + 17);
		mainStack.updateFrames();
	}
	
	String fraction(int numerator, int denominator) {
		return localized("gui", "food_book.fraction",
			numerator,
			denominator
		);
	}
	
	UIElement makeSeparatorLine() {
		return UIBox.horizontalLine(0, getWidth() / 2, 0, GuiFoodBook.leastBlack);
	}
	
	UIImage icon(UIImage.Image image) {
		UIImage icon = new UIImage(image);
		icon.setWidth(11);
		icon.setHeight(11);
		return icon;
	}
	
	UIElement statWithIcon(UIImage icon, String value, String name) {
		UIStack valueStack = new UIStack();
		valueStack.axis = UIStack.Axis.HORIZONTAL;
		valueStack.spacing = 3;
		
		valueStack.addChild(icon);
		valueStack.addChild(new UILabel(value));
		
		UIStack fullStack = new UIStack();
		fullStack.axis = UIStack.Axis.VERTICAL;
		fullStack.spacing = 2;
		
		fullStack.addChild(valueStack);
		UILabel nameLabel = new UILabel(name);
		nameLabel.color = GuiFoodBook.lessBlack;
		fullStack.addChild(nameLabel);
		
		return fullStack;
	}
}
