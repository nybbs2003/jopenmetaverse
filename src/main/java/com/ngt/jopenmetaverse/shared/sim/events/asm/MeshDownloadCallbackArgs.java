package com.ngt.jopenmetaverse.shared.sim.events.asm;

import com.ngt.jopenmetaverse.shared.sim.asset.AssetMesh;

public class MeshDownloadCallbackArgs {
	boolean success;
	AssetMesh assetMesh;
	
	public MeshDownloadCallbackArgs(boolean success, AssetMesh assetMesh) {
		super();
		this.success = success;
		this.assetMesh = assetMesh;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public AssetMesh getAssetMesh() {
		return assetMesh;
	}
	public void setAssetMesh(AssetMesh assetMesh) {
		this.assetMesh = assetMesh;
	}
}
