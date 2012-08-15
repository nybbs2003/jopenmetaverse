package com.ngt.jopenmetaverse.shared.sim.events.im;

import com.ngt.jopenmetaverse.shared.sim.inventory.InventoryItem;

public class ItemCreatedCallbackArg {
	boolean success;
	InventoryItem item;
	
	
	public ItemCreatedCallbackArg(boolean success, InventoryItem item) {
		super();
		this.success = success;
		this.item = item;
	}
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public InventoryItem getItem() {
		return item;
	}
	public void setItem(InventoryItem item) {
		this.item = item;
	}
}
