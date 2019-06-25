package com.cazsius.solcarrot.client.gui;

import com.cazsius.solcarrot.client.gui.elements.UIItemStack;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

final class ItemListPage extends Page {
	private static final int itemsPerRow = 5;
	private static final int rowsPerPage = 6;
	private static final int itemsPerPage = itemsPerRow * rowsPerPage;
	private static final int itemSpacing = UIItemStack.size + 4;
	
	static List<ItemListPage> pages(Rectangle frame, String header, List<ItemStack> items) {
		List<ItemListPage> pages = new ArrayList<>();
		for (int startIndex = 0; startIndex < items.size(); startIndex += ItemListPage.itemsPerPage) {
			int endIndex = Math.min(startIndex + ItemListPage.itemsPerPage, items.size());
			pages.add(new ItemListPage(frame, header, items.subList(startIndex, endIndex)));
		}
		return pages;
	}
	
	private ItemListPage(Rectangle frame, String header, List<ItemStack> items) {
		super(frame, header);
		
		int minX = (1 - itemsPerRow) * itemSpacing / 2;
		int minY = (1 - rowsPerPage) * itemSpacing / 2 - 4;
		
		for (int i = 0; i < items.size(); i++) {
			ItemStack itemStack = items.get(i);
			int x = minX + itemSpacing * (i % itemsPerRow);
			int y = minY + itemSpacing * ((i / itemsPerRow) % rowsPerPage);
			
			UIItemStack view = new UIItemStack(itemStack);
			view.setCenterX(getCenterX() + x);
			view.setCenterY(getCenterY() + y);
			children.add(view);
		}
	}
}
