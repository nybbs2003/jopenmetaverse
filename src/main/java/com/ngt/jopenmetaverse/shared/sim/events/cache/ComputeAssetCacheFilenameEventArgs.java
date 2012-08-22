package com.ngt.jopenmetaverse.shared.sim.events.cache;

import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

public class ComputeAssetCacheFilenameEventArgs extends EventArgs{

	protected String cacheDir;
	protected UUID assetID;
	
	public ComputeAssetCacheFilenameEventArgs() {
		super();
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
