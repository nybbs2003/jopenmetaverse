package com.ngt.jopenmetaverse.shared.sim.events.asm;

import java.util.SortedMap;
import java.util.TreeMap;

import com.ngt.jopenmetaverse.shared.sim.AssetManager.ChannelType;
import com.ngt.jopenmetaverse.shared.sim.AssetManager.SourceType;
import com.ngt.jopenmetaverse.shared.sim.AssetManager.StatusCode;
import com.ngt.jopenmetaverse.shared.sim.AssetManager.TargetType;
import com.ngt.jopenmetaverse.shared.sim.events.ManualResetEvent;
import com.ngt.jopenmetaverse.shared.sim.events.MethodDelegate;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.sim.Simulator;

  /// <summary>
    /// 
    /// </summary>
    public class AssetDownload extends Transfer
    {
        public UUID AssetID;
        public ChannelType Channel;
        public SourceType Source;
        public TargetType Target;
        public StatusCode Status;
        public float Priority;
        public Simulator Simulator;
        public SortedMap<Integer, byte[]> pmap;
        public MethodDelegate<Void, AssetReceivedCallbackArgs> Callback;
        public ManualResetEvent HeaderReceivedEvent = new ManualResetEvent(false);

        public AssetDownload()
        {
           super();
           pmap = new TreeMap<Integer, byte[]>();
        }
    }

