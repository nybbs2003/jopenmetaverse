package com.ngt.jopenmetaverse.shared.sim.events.asm.archive;

import com.ngt.jopenmetaverse.shared.sim.asset.AssetPrim;


public class SceneObjectLoadedCallbackArgs 
{
	AssetPrim linkset;
	long bytesRead; 
	long totalBytes;
	
	public SceneObjectLoadedCallbackArgs() {
		super();
	}
	
	public SceneObjectLoadedCallbackArgs(AssetPrim linkset, long bytesRead,
			long totalBytes) {
		super();
		this.linkset = linkset;
		this.bytesRead = bytesRead;
		this.totalBytes = totalBytes;
	}

	public AssetPrim getLinkset() {
		return linkset;
	}
	public void setLinkset(AssetPrim linkset) {
		this.linkset = linkset;
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
