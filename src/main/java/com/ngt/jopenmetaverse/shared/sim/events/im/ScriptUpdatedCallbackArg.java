package com.ngt.jopenmetaverse.shared.sim.events.im;

import java.util.List;

import com.ngt.jopenmetaverse.shared.types.UUID;

public class ScriptUpdatedCallbackArg {
	boolean uploadSuccess;
	String uploadStatus;
	boolean compileSuccess;
	List<String> compileMessages;
	UUID itemID;
	UUID assetID;
	
	public ScriptUpdatedCallbackArg(boolean uploadSuccess, String uploadStatus,
			boolean compileSuccess, List<String> compileMessages, UUID itemID,
			UUID assetID) {
		super();
		this.uploadSuccess = uploadSuccess;
		this.uploadStatus = uploadStatus;
		this.compileSuccess = compileSuccess;
		this.compileMessages = compileMessages;
		this.itemID = itemID;
		this.assetID = assetID;
	}
	public boolean isUploadSuccess() {
		return uploadSuccess;
	}
	public void setUploadSuccess(boolean uploadSuccess) {
		this.uploadSuccess = uploadSuccess;
	}
	public String getUploadStatus() {
		return uploadStatus;
	}
	public void setUploadStatus(String uploadStatus) {
		this.uploadStatus = uploadStatus;
	}
	public boolean isCompileSuccess() {
		return compileSuccess;
	}
	public void setCompileSuccess(boolean compileSuccess) {
		this.compileSuccess = compileSuccess;
	}
	public List<String> getCompileMessages() {
		return compileMessages;
	}
	public void setCompileMessages(List<String> compileMessages) {
		this.compileMessages = compileMessages;
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
