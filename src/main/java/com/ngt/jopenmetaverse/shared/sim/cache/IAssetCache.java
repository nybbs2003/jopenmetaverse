package com.ngt.jopenmetaverse.shared.sim.cache;

import com.ngt.jopenmetaverse.shared.sim.events.asm.ImageDownload;
import com.ngt.jopenmetaverse.shared.types.UUID;

public interface IAssetCache {

	/// <summary>
	/// Allows setting weather to periodicale prune the cache if it grows too big
	/// Default is enabled, when caching is enabled
	/// </summary>
	public boolean getAutoPruneEnabled();
	public void setAutoPruneEnabled(boolean value);

	/// <summary>
	/// How long (in ms) between cache checks (default is 5 min.) 
	/// </summary>
	public void setAutoPruneInterval(double value);

	public double getAutoPruneInterval();



	/// <summary>
	/// Return bytes read from the local asset cache, null if it does not exist
	/// </summary>
	/// <param name="assetID">UUID of the asset we want to get</param>
	/// <returns>Raw bytes of the asset, or null on failure</returns>
	public byte[] getCachedAssetBytes(UUID assetID);

	/// <summary>
	/// Returns ImageDownload object of the
	/// image from the local image cache, null if it does not exist
	/// </summary>
	/// <param name="imageID">UUID of the image we want to get</param>
	/// <returns>ImageDownload object containing the image, or null on failure</returns>
	public ImageDownload getCachedImage(UUID imageID);

	/// <summary>
	/// Saves an asset to the local cache
	/// </summary>
	/// <param name="assetID">UUID of the asset</param>
	/// <param name="assetData">Raw bytes the asset consists of</param>
	/// <returns>Weather the operation was successfull</returns>
	public boolean saveAssetToCache(UUID assetID, byte[] assetData);

	/// <summary>
	/// Checks if the asset exists in the local cache
	/// </summary>
	/// <param name="assetID">UUID of the asset</param>
	/// <returns>True is the asset is stored in the cache, otherwise false</returns>
	public boolean hasAsset(UUID assetID);

	/// <summary>
	/// Wipes out entire cache
	/// </summary>
	public void clear();


	/// <summary>
	/// Brings cache size to the 90% of the max size
	/// </summary>
	public void prune();


	/// <summary>
	/// Asynchronously brings cache size to the 90% of the max size
	/// </summary>
	public void beginPrune();

}
