package com.ngt.jopenmetaverse.shared.sim.events.asm;

import com.ngt.jopenmetaverse.shared.types.UUID;

public class ComputeAssetCacheFilenameEventArgs {
	String cacheDir;
	UUID assetID;
	
	public ComputeAssetCacheFilenameEventArgs(String cacheDir, UUID assetID) {
		super();
		this.cacheDir = cacheDir;
		this.assetID = assetID;
	}
	
	public String getCacheDir() {
		return cacheDir;
	}
	public void setCacheDir(String cacheDir) {
		this.cacheDir = cacheDir;
	}
	public UUID getAssetID() {
		return assetID;
	}
	public void setAssetID(UUID assetID) {
		this.assetID = assetID;
	}
}
