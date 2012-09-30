package com.ngt.jopenmetaverse.shared.sim.imaging;

import com.ngt.jopenmetaverse.shared.sim.cache.LoadCachedImageResult;

public class DecodedTgaImage extends LoadCachedImageResult{

	public DecodedTgaImage() {
		super();
	}

	public DecodedTgaImage(byte[] data, boolean hasAlpha, boolean fullAlpha,
			boolean isMask) {
		super(data, hasAlpha, fullAlpha, isMask);
	}
}
