package com.ngt.jopenmetaverse.shared.sim.cache;
public class LoadCachedImageResult
{
	public byte[] data;
	public boolean hasAlpha;
	public boolean fullAlpha;
	public boolean isMask;
	
	public LoadCachedImageResult() {
		super();
	}

	public LoadCachedImageResult(byte[] data, boolean hasAlpha,
			boolean fullAlpha, boolean isMask) {
		super();
		this.data = data;
		this.hasAlpha = hasAlpha;
		this.fullAlpha = fullAlpha;
		this.isMask = isMask;
	}
}