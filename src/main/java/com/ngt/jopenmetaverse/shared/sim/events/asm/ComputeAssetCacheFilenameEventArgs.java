package com.ngt.jopenmetaverse.shared.sim.events.asm;

public class ComputeAssetCacheFilenameEventArgs {
	String cacheDir;
	String assetID;
	
	public ComputeAssetCacheFilenameEventArgs(String cacheDir, String assetID) {
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
	public String getAssetID() {
		return assetID;
	}
	public void setAssetID(String assetID) {
		this.assetID = assetID;
	}
}
