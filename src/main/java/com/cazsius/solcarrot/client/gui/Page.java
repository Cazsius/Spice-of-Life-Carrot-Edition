package com.cazsius.solcarrot.client.gui;

import com.cazsius.solcarrot.client.gui.elements.UIElement;
import com.cazsius.solcarrot.client.gui.elements.UILabel;

import java.awt.*;

abstract class Page extends UIElement {
	Page(Rectangle frame, String header) {
		super(frame);
		
		UILabel headerLabel = new UILabel(header);
		headerLabel.setCenterX(getCenterX());
		headerLabel.setMinY(getMinY() + 22);
		children.add(headerLabel);
	}
}
