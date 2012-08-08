package com.ngt.jopenmetaverse.shared.sim.asset;

import com.ngt.jopenmetaverse.shared.types.Enums.AssetType;
import com.ngt.jopenmetaverse.shared.types.UUID;

public abstract class Asset {
	  /// <summary>A byte array containing the raw asset data</summary>
    public byte[] AssetData;
    /// <summary>True if the asset it only stored on the server temporarily</summary>
    public boolean Temporary;
    /// <summary>A unique ID</summary>
    private UUID _AssetID;
    /// <summary>The assets unique ID</summary>
    public UUID AssetID()
    {
        return _AssetID;
    }

    public void setAssetID(UUID value)
    {
        _AssetID = value;
    }
    
    /// <summary>
    /// The "type" of asset, Notecard, Animation, etc
    /// </summary>
    public abstract AssetType getAssetType();

    /// <summary>
    /// Construct a new Asset object
    /// </summary>
    public Asset() { }

    /// <summary>
    /// Construct a new Asset object
    /// </summary>
    /// <param name="assetID">A unique <see cref="UUID"/> specific to this asset</param>
    /// <param name="assetData">A byte array containing the raw asset data</param>
    public Asset(UUID assetID, byte[] assetData)
    {
        _AssetID = assetID;
        AssetData = assetData;
    }

    /// <summary>
    /// Regenerates the <code>AssetData</code> byte array from the properties 
    /// of the derived class.
    /// </summary>
    public abstract void Encode();

    /// <summary>
    /// Decodes the AssetData, placing it in appropriate properties of the derived
    /// class.
    /// </summary>
    /// <returns>True if the asset decoding succeeded, otherwise false</returns>
    public abstract boolean Decode();
}
