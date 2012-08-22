package com.ngt.jopenmetaverse.shared.sim.asset;

import com.ngt.jopenmetaverse.shared.types.Enums.AssetType;
import com.ngt.jopenmetaverse.shared.types.UUID;

/// <summary>
/// Represents an <seealso cref="AssetWearable"/> that represents an avatars body ie: Hair, Etc.
/// </summary>
public class AssetBodypart extends AssetWearable
{
    /// <summary>Override the base classes AssetType</summary>
	@Override
    public AssetType getAssetType() { return AssetType.Bodypart; } 

    /// <summary>Initializes a new instance of an AssetBodyPart object</summary>
    public AssetBodypart() { }

    /// <summary>Initializes a new instance of an AssetBodyPart object with parameters</summary>
    /// <param name="assetID">A unique <see cref="UUID"/> specific to this asset</param>
    /// <param name="assetData">A byte array containing the raw asset data</param>
    public AssetBodypart(UUID assetID, byte[] assetData) 
    { super(assetID, assetData); }
}