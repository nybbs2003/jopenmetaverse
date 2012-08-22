package com.ngt.jopenmetaverse.shared.sim.asset;

import java.io.UnsupportedEncodingException;

import com.ngt.jopenmetaverse.shared.types.Enums.AssetType;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;
   /// <summary>
    /// Represents an LSL Text object containing a string of UTF encoded characters
    /// </summary>
    public class AssetScriptText extends Asset
    {
        /// <summary>Override the base classes AssetType</summary>
    	@Override
        public AssetType getAssetType() { return AssetType.LSLText; } 

        /// <summary>A string of characters represting the script contents</summary>
        public String Source;

        /// <summary>Initializes a new AssetScriptText object</summary>
        public AssetScriptText() { }

        /// <summary>
        /// Initializes a new AssetScriptText object with parameters
        /// </summary>
        /// <param name="assetID">A unique <see cref="UUID"/> specific to this asset</param>
        /// <param name="assetData">A byte array containing the raw asset data</param>
        public AssetScriptText(UUID assetID, byte[] assetData) 
        { super(assetID, assetData); }

        /// <summary>
        /// Encode a string containing the scripts contents into byte encoded AssetData
        /// </summary>
        @Override
        public void Encode()
        {
            AssetData = Utils.stringToBytesWithTrailingNullByte(Source);
        }

        /// <summary>
        /// Decode a byte array containing the scripts contents into a string
        /// </summary>
        /// <returns>true if decoding is successful</returns>
        @Override
        public boolean Decode() throws UnsupportedEncodingException
        {
            Source = Utils.bytesWithTrailingNullByteToString(AssetData);
            return true;
        }
    }