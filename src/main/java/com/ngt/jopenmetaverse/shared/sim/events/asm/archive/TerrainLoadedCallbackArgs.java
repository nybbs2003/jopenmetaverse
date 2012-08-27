package com.ngt.jopenmetaverse.shared.sim.events.asm.archive;

public class TerrainLoadedCallbackArgs {

	float[][] terrain;
	long bytesRead;
	long totalBytes;
	
	public TerrainLoadedCallbackArgs() {
		super();
	}
	
	public TerrainLoadedCallbackArgs(float[][] terrain, long bytesRead,
			long totalBytes) {
		super();
		this.terrain = terrain;
		this.bytesRead = bytesRead;
		this.totalBytes = totalBytes;
	}
	public float[][] getTerrain() {
		return terrain;
	}
	public void setTerrain(float[][] terrain) {
		this.terrain = terrain;
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
