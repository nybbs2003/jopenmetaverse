package com.ngt.jopenmetaverse.shared.sim.events.asm;

import java.util.SortedMap;
import com.ngt.jopenmetaverse.shared.sim.AssetManager.ImageCodec;
import com.ngt.jopenmetaverse.shared.sim.events.ManualResetEvent;
import com.ngt.jopenmetaverse.shared.sim.Simulator;
import com.ngt.jopenmetaverse.shared.sim.AssetManager.ImageType;
   /// <summary>
    /// 
    /// </summary>
    public class ImageDownload extends Transfer
    {
    	//ushort
        public int PacketCount;
        public ImageCodec Codec;
        public Simulator Simulator;
        //<ushort, ushort>
        public SortedMap<Integer, Integer> PacketsSeen;
        public ImageType ImageType;
        public int DiscardLevel;
        public float Priority;
        public int InitialDataSize;
        public  ManualResetEvent HeaderReceivedEvent = new ManualResetEvent(false);

        public ImageDownload()
        {
        	super();
        }
    }
