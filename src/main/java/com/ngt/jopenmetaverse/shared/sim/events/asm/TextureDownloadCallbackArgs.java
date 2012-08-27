package com.ngt.jopenmetaverse.shared.sim.events.asm;

import com.ngt.jopenmetaverse.shared.sim.asset.AssetTexture;
import com.ngt.jopenmetaverse.shared.sim.asset.pipeline.TexturePipeline.TextureRequestState;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;

public class TextureDownloadCallbackArgs extends EventArgs
{
	protected TextureRequestState state;
	protected AssetTexture assetTexture;
	
	public TextureDownloadCallbackArgs(TextureRequestState state,
			AssetTexture assetTexture) {
		super();
		this.state = state;
		this.assetTexture = assetTexture;
	}
	
	public TextureRequestState getState() {
		return state;
	}
	public void setState(TextureRequestState state) {
		this.state = state;
	}
	public AssetTexture getAssetTexture() {
		return assetTexture;
	}
	public void setAssetTexture(AssetTexture assetTexture) {
		this.assetTexture = assetTexture;
	}
}
