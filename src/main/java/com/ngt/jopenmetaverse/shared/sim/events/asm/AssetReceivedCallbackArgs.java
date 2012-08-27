package com.ngt.jopenmetaverse.shared.sim.events.asm;

import com.ngt.jopenmetaverse.shared.sim.asset.Asset;

public class AssetReceivedCallbackArgs {
	AssetDownload transfer;
	Asset asset;
	
	public AssetReceivedCallbackArgs(AssetDownload transfer, Asset asset) {
		super();
		this.transfer = transfer;
		this.asset = asset;
	}
	public AssetDownload getTransfer() {
		return transfer;
	}
	public void setTransfer(AssetDownload transfer) {
		this.transfer = transfer;
	}
	public Asset getAsset() {
		return asset;
	}
	public void setAsset(Asset asset) {
		this.asset = asset;
	}
}
