package com.ngt.jopenmetaverse.shared.sim.events.asm.archive;

import com.ngt.jopenmetaverse.shared.sim.asset.Asset;


public class AssetLoadedCallbackArgs 
{
	Asset asset;
	long bytesRead;
	long totalBytes;
	
	
	public AssetLoadedCallbackArgs() {
		super();
	}
	public AssetLoadedCallbackArgs(Asset asset, long bytesRead, long totalBytes) {
		super();
		this.asset = asset;
		this.bytesRead = bytesRead;
		this.totalBytes = totalBytes;
	}
	public Asset getAsset() {
		return asset;
	}
	public void setAsset(Asset asset) {
		this.asset = asset;
	}
	public long getBytesRead() {
		return bytesRead;
	}
	public void setBytesRead(long bytesRead) {
		this.bytesRead = bytesRead;
	}
	public long getTotalBytes() {
		return totalBytes;
	}
	public void setTotalBytes(long totalBytes) {
		this.totalBytes = totalBytes;
	}
	
}
