package com.ngt.jopenmetaverse.shared.sim.events.asm;

import com.ngt.jopenmetaverse.shared.types.UUID;

public class BakedTextureUploadedCallbackArgs {
	UUID newAssetID;
	
	public BakedTextureUploadedCallbackArgs(UUID newAssetID) {
		super();
		this.newAssetID = newAssetID;
	}

	public UUID getNewAssetID() {
		return newAssetID;
	}

	public void setNewAssetID(UUID newAssetID) {
		this.newAssetID = newAssetID;
	}
	
}
