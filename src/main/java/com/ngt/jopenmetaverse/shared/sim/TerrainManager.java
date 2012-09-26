package com.ngt.jopenmetaverse.shared.sim;

import java.util.Observable;

import com.ngt.jopenmetaverse.shared.protocol.BitPack;
import com.ngt.jopenmetaverse.shared.protocol.LayerDataPacket;
import com.ngt.jopenmetaverse.shared.protocol.PacketType;
import com.ngt.jopenmetaverse.shared.sim.TerrainCompressor.TerrainPatch;
import com.ngt.jopenmetaverse.shared.sim.events.EventObservable;
import com.ngt.jopenmetaverse.shared.sim.events.EventObserver;
import com.ngt.jopenmetaverse.shared.sim.events.PacketReceivedEventArgs;
import com.ngt.jopenmetaverse.shared.sim.events.terrain.LandPatchReceivedEventArgs;
import com.ngt.jopenmetaverse.shared.types.Vector2;
import com.ngt.jopenmetaverse.shared.util.JLogger;
import com.ngt.jopenmetaverse.shared.util.Utils;

public class TerrainManager 
{	
	/// <summary>Raises the LandPatchReceived event</summary>
	/// <param name="e">A LandPatchReceivedEventArgs object containing the
	/// data returned from the simulator</param>
	private EventObservable<LandPatchReceivedEventArgs> onLandPatchReceived = new EventObservable<LandPatchReceivedEventArgs>();
	public void registerOnLandPatchReceived(EventObserver<LandPatchReceivedEventArgs> o)
	{
		onLandPatchReceived.addObserver(o);
	}
	public void unregisterOnLandPatchReceived(EventObserver<LandPatchReceivedEventArgs> o) 
	{
		onLandPatchReceived.deleteObserver(o);
	}
	
//	  //region EventHandling
//      /// <summary>The event subscribers. null if no subcribers</summary>
//      private EventHandler<LandPatchReceivedEventArgs> m_LandPatchReceivedEvent;
//
//      /// <summary>Raises the LandPatchReceived event</summary>
//      /// <param name="e">A LandPatchReceivedEventArgs object containing the
//      /// data returned from the simulator</param>
//      protected virtual void OnLandPatchReceived(LandPatchReceivedEventArgs e)
//      {
//          EventHandler<LandPatchReceivedEventArgs> handler = m_LandPatchReceivedEvent;
//          if (handler != null)
//              handler(this, e);
//      }
//
//      /// <summary>Thread sync lock object</summary>
//      private readonly object m_LandPatchReceivedLock = new object();
//
//      /// <summary>Raised when the simulator responds sends </summary>
//      public event EventHandler<LandPatchReceivedEventArgs> LandPatchReceived 
//      {
//          add { lock (m_LandPatchReceivedLock) { m_LandPatchReceivedEvent += value; } }
//          remove { lock (m_LandPatchReceivedLock) { m_LandPatchReceivedEvent -= value; } }
//      }
//      //endregion

      private GridClient Client;

      /// <summary>
      /// Default constructor
      /// </summary>
      /// <param name="client"></param>
      public TerrainManager(GridClient client)
      {
          Client = client;
//          Client.network.RegisterCallback(PacketType.LayerData, LayerDataHandler);
          Client.network.RegisterCallback(PacketType.LayerData, new EventObserver<PacketReceivedEventArgs>(){
			@Override
			public void handleEvent(Observable o, PacketReceivedEventArgs arg) {
				LayerDataHandler(o, arg);
			}
          });

      }
            
      
//      private void printIntArray(int[] array)
//      {
//   		int count = 0;
//   		while(count < array.length)
//   		{
//   			System.out.println("");
//   			for(int x =0 ; x < 50 && count <  array.length	 ; x++)
//   			{
//   				System.out.print((int)array[count++] + " ");
//   			}
//   		}
//   	}
      
      private void DecompressLand(Simulator simulator, BitPack bitpack, TerrainPatch.GroupHeader group)
      {
          int x;
          int y;
          int[] patches = new int[32 * 32];
          int count = 0;

          while (true)
          {
              TerrainPatch.Header header = TerrainCompressor.DecodePatchHeader(bitpack);

              if (header.QuantWBits == TerrainCompressor.END_OF_PATCHES)
                  break;

              x = header.getX();
              y = header.getY();

              if (x >= TerrainCompressor.PATCHES_PER_EDGE || y >= TerrainCompressor.PATCHES_PER_EDGE)
              {
                  JLogger.warn(String.format(
                      "Invalid LayerData land packet, x=%d, y=%d, dc_offset=%f, range=%d, quant_wbits=%d, patchids=%d, count=%d",
                      x, y, header.DCOffset, header.Range, header.QuantWBits, header.PatchIDs, count));
                  return;
              }

              // Decode this patch
              TerrainCompressor.DecodePatch(patches, bitpack, header, group.PatchSize);

              //TODO only for debugging
//              System.out.println("Decoded Patch: ");
//              printIntArray(patches);
              
              // Decompress this patch
              float[] heightmap = TerrainCompressor.DecompressPatch(patches, header, group);

              count++;

              if (Client.settings.STORE_LAND_PATCHES)
              {
                  TerrainPatch patch = new TerrainPatch();
                  patch.Data = heightmap;
                  patch.X = x;
                  patch.Y = y;
                  simulator.Terrain[y * 16 + x] = patch;
              }
              
              try { onLandPatchReceived.raiseEvent(new LandPatchReceivedEventArgs(simulator, x, y, group.PatchSize, heightmap)); }
              catch (Exception e) { JLogger.error(Utils.getExceptionStackTraceAsString(e)); }

          }
      }

      private void DecompressWind(Simulator simulator, BitPack bitpack, TerrainPatch.GroupHeader group)
      {
          int[] patches = new int[32 * 32];

          // Ignore the simulator stride value
          group.Stride = group.PatchSize;

          // Each wind packet contains the wind speeds and direction for the entire simulator
          // stored as two float arrays. The first array is the X value of the wind speed at
          // each 16x16m block, second is the Y value.
          // wind_speed = distance(x,y to 0,0)
          // wind_direction = vec2(x,y)

          // X values
          TerrainPatch.Header header = TerrainCompressor.DecodePatchHeader(bitpack);
          TerrainCompressor.DecodePatch(patches, bitpack, header, group.PatchSize);
          float[] xvalues = TerrainCompressor.DecompressPatch(patches, header, group);

          // Y values
          header = TerrainCompressor.DecodePatchHeader(bitpack);
          TerrainCompressor.DecodePatch(patches, bitpack, header, group.PatchSize);
          float[] yvalues = TerrainCompressor.DecompressPatch(patches, header, group);

          if (simulator.Client.settings.STORE_LAND_PATCHES)
          {
              for (int i = 0; i < 256; i++)
                  simulator.WindSpeeds[i] = new Vector2(xvalues[i], yvalues[i]);
          }
      }

      private void DecompressCloud(Simulator simulator, BitPack bitpack, TerrainPatch.GroupHeader group)
      {
          // FIXME:
          JLogger.warn("DecompressCloud not implemented");
      }

      private void LayerDataHandler(Object sender, PacketReceivedEventArgs e)
      {
          LayerDataPacket layer = (LayerDataPacket)e.getPacket();
          BitPack bitpack = new BitPack(layer.LayerData.Data, 0);
          TerrainPatch.GroupHeader header = new TerrainPatch.GroupHeader();
          TerrainPatch.LayerType type = TerrainPatch.LayerType.get(layer.LayerID.Type);

          // Stride
          header.Stride = bitpack.UnpackBits(16);
          // Patch size
          header.PatchSize = bitpack.UnpackBits(8);
          // Layer type
          header.Type = TerrainPatch.LayerType.get((byte)bitpack.UnpackBits(8));

          switch (type)
          {
              case Land:
                  if (onLandPatchReceived != null || Client.settings.STORE_LAND_PATCHES)
                      DecompressLand(e.getSimulator(), bitpack, header);
                  break;
              case Water:
                  JLogger.error("Got a Water LayerData packet, implement me!");
                  break;
              case Wind:
                  DecompressWind(e.getSimulator(), bitpack, header);
                  break;
              case Cloud:
                  DecompressCloud(e.getSimulator(), bitpack, header);
                  break;
              default:
                  JLogger.warn("Unrecognized LayerData type " + type.toString());
                  break;
          }
      }
  }
