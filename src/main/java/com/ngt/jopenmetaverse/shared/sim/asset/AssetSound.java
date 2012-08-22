package com.ngt.jopenmetaverse.shared.sim.asset;

import com.ngt.jopenmetaverse.shared.types.Enums.AssetType;
import com.ngt.jopenmetaverse.shared.types.UUID;

/// <summary>
/// Represents a Sound Asset
/// </summary>
public class AssetSound extends Asset
{
    /// <summary>Override the base classes AssetType</summary>
	@Override
    public AssetType getAssetType() { return AssetType.Sound; }

    /// <summary>Initializes a new instance of an AssetSound object</summary>
    public AssetSound() { }

    /// <summary>Initializes a new instance of an AssetSound object with parameters</summary>
    /// <param name="assetID">A unique <see cref="UUID"/> specific to this asset</param>
    /// <param name="assetData">A byte array containing the raw asset data</param>
    public AssetSound(UUID assetID, byte[] assetData)
    {
        super(assetID, assetData);
    }

    /// <summary>
    /// TODO: Encodes a sound file
    /// </summary>
    @Override
    public void Encode() { }

    /// <summary>
    /// TODO: Decode a sound file
    /// </summary>
    /// <returns>true</returns>
    @Override
    public boolean Decode() { return true; }
}