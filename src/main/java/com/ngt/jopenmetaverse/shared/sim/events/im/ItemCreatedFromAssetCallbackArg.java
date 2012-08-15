package com.ngt.jopenmetaverse.shared.sim.events.im;

import com.ngt.jopenmetaverse.shared.types.UUID;

public class ItemCreatedFromAssetCallbackArg {
	boolean success;
	String status;
	UUID itemID;
	UUID assetID;
	
	public ItemCreatedFromAssetCallbackArg(boolean success, String status,
			UUID itemID, UUID assetID) {
		super();
		this.success = success;
		this.status = status;
		this.itemID = itemID;
		this.assetID = assetID;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public UUID getItemID() {
		return itemID;
	}
	public void setItemID(UUID itemID) {
		this.itemID = itemID;
	}
	public UUID getAssetID() {
		return assetID;
	}
	public void setAssetID(UUID assetID) {
		this.assetID = assetID;
	}
	
	
}
