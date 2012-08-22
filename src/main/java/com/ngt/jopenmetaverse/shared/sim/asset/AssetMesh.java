package com.ngt.jopenmetaverse.shared.sim.asset;

import java.io.ByteArrayInputStream;

import com.ngt.jopenmetaverse.shared.exception.NotImplementedException;
import com.ngt.jopenmetaverse.shared.structureddata.OSDMap;
import com.ngt.jopenmetaverse.shared.structureddata.OSDType;
import com.ngt.jopenmetaverse.shared.structureddata.llsd.BinaryLLSDOSDParser;
import com.ngt.jopenmetaverse.shared.types.Enums.AssetType;
import com.ngt.jopenmetaverse.shared.types.UUID;
/// <summary>
/// Represents Mesh asset
/// </summary>
import com.ngt.jopenmetaverse.shared.util.JLogger;
import com.ngt.jopenmetaverse.shared.util.Utils;
public class AssetMesh extends Asset
{
	/// <summary>Override the base classes AssetType</summary>
	@Override
	public AssetType getAssetType() { return AssetType.Mesh; } 

	/// <summary>
	/// Decoded mesh data
	/// </summary>
	public OSDMap MeshData;

	/// <summary>Initializes a new instance of an AssetMesh object</summary>
	public AssetMesh() { }

	/// <summary>Initializes a new instance of an AssetMesh object with parameters</summary>
	/// <param name="assetID">A unique <see cref="UUID"/> specific to this asset</param>
	/// <param name="assetData">A byte array containing the raw asset data</param>
	public AssetMesh(UUID assetID, byte[] assetData)
	{
		super(assetID, assetData);
	}

	/// <summary>
	/// TODO: Encodes Collada file into LLMesh format
	/// </summary>
	@Override
	public void Encode() throws NotImplementedException { throw new NotImplementedException("Need to Impelemnt"); }

	/// <summary>
	/// Decodes mesh asset. See <see cref="OpenMetaverse.Rendering.FacetedMesh.TryDecodeFromAsset"/>
	/// to furter decode it for rendering</summary>
	/// <returns>true</returns>
	@Override
	public boolean Decode()
	{
		try
		{
			MeshData = new OSDMap();

			ByteArrayInputStream data = new ByteArrayInputStream(AssetData);
			int totalAvailable = data.available();

			OSDMap header = (OSDMap)BinaryLLSDOSDParser.DeserializeLLSDBinary(data);
			int start = totalAvailable - data.available();

			for(String partName : header.keys())
			{
				if (header.get(partName).getType() != OSDType.Map)
				{
					MeshData.put(partName,  header.get(partName));
					continue;
				}

				OSDMap partInfo = (OSDMap)header.get(partName);
				if (partInfo.get("offset").asInteger() < 0 
						|| partInfo.get("size").asInteger() == 0)
				{
					MeshData.put(partName, partInfo);
					continue;
				}

				byte[] part = new byte[partInfo.get("size").asInteger()];
				Utils.arraycopy(AssetData, partInfo.get("offset").asInteger() + start
						, part, 0, part.length);

				MeshData.put(partName, BinaryLLSDOSDParser.DeserializeLLSDBinary(data));


				//                        using (MemoryStream input = new MemoryStream(part))
				//                        {
				//                            using (MemoryStream output = new MemoryStream())
				//                            {
				//                                using (ZOutputStream zout = new ZOutputStream(output))
				//                                {
				//                                    byte[] buffer = new byte[2048];
				//                                    int len;
				//                                    while ((len = input.Read(buffer, 0, buffer.Length)) > 0)
				//                                    {
				//                                        zout.Write(buffer, 0, len);
				//                                    }
				//                                    zout.Flush();
				//                                    output.Seek(0, SeekOrigin.Begin);
				//                                    MeshData[partName] = OSDParser.DeserializeLLSDBinary(output);
				//                                }
				//                            }
				//                        }
			}
			return true;
		}
		catch (Exception ex)
		{
			JLogger.error("Failed to decode mesh asset\n" + Utils.getExceptionStackTraceAsString(ex));
			return false;
		}
	}
}