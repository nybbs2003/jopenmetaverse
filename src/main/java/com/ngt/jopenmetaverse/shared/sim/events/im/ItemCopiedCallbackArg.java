package com.ngt.jopenmetaverse.shared.sim.events.im;

import com.ngt.jopenmetaverse.shared.sim.inventory.InventoryBase;

public class ItemCopiedCallbackArg {
	InventoryBase item;
	
	public ItemCopiedCallbackArg(InventoryBase item) {
		super();
		this.item = item;
	}

	public InventoryBase getItem() {
		return item;
	}

	public void setItem(InventoryBase item) {
		this.item = item;
	}
}
