package com.ngt.jopenmetaverse.shared.protocol;


    public final class CrossedRegionPacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID SessionID;

            @Override
			public int getLength()
            {
                                {
                    return 32;
                }
            }

            public AgentDataBlock() { }
            public AgentDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    AgentID.FromBytes(bytes, i); i += 16;
                    SessionID.FromBytes(bytes, i[0]); i[0] += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                AgentID.ToBytes(bytes, i[0]); i[0] += 16;
                SessionID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        /// <exclude/>
        public final class RegionDataBlock extends PacketBlock
        {
            public uint SimIP;
            public ushort SimPort;
            public ulong RegionHandle;
            public byte[] SeedCapability;

            @Override
			public int getLength()
            {
                                {
                    int length = 16;
                    if (SeedCapability != null) { length += SeedCapability.length; }
                    return length;
                }
            }

            public RegionDataBlock() { }
            public RegionDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    SimIP = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    SimPort = (ushort)((bytes[i++] << 8) + bytes[i++]);
                    RegionHandle = (ulong)((ulong)bytes[i++] + ((ulong)bytes[i++] << 8) + ((ulong)bytes[i++] << 16) + ((ulong)bytes[i++] << 24) + ((ulong)bytes[i++] << 32) + ((ulong)bytes[i++] << 40) + ((ulong)bytes[i++] << 48) + ((ulong)bytes[i++] << 56));
                    length = (bytes[i++] + (bytes[i++] << 8));
                    SeedCapability = new byte[length];
                    Utils.arraycopy(bytes, i, SeedCapability, 0, length); i += length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.UIntToBytes(SimIP, bytes, i); i += 4;
                bytes[i++] = (byte)((SimPort >> 8) % 256);
                bytes[i++] = (byte)(SimPort % 256);
                Utils.UInt64ToBytes(RegionHandle, bytes, i); i += 8;
                bytes[i++] = (byte)(SeedCapability.length % 256);
                bytes[i++] = (byte)((SeedCapability.length >> 8) % 256);
                Utils.arraycopy(SeedCapability, 0, bytes, i, SeedCapability.length); i += SeedCapability.length;
            }

        }

        /// <exclude/>
        public final class InfoBlock extends PacketBlock
        {
            public Vector3 Position;
            public Vector3 LookAt;

            @Override
			public int getLength()
            {
                                {
                    return 24;
                }
            }

            public InfoBlock() { }
            public InfoBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    Position.FromBytes(bytes, i); i += 12;
                    LookAt.FromBytes(bytes, i); i += 12;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Position.ToBytes(bytes, i); i += 12;
                LookAt.ToBytes(bytes, i); i += 12;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 8;
                length += AgentData.getLength();
                length += RegionData.length;
                length += Info.length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public RegionDataBlock RegionData;
        public InfoBlock Info;

        public CrossedRegionPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.CrossedRegion;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Medium;
            header.ID = 7;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
            RegionData = new RegionDataBlock();
            Info = new InfoBlock();
        }

        public CrossedRegionPacket(byte[] bytes, int[] i) 
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(bytes, i, packetEnd, null);
        }

        @Override
		public void FromBytes(byte[] bytes, int[] i, int[] packetEnd, byte[] zeroBuffer)
        {
            header.FromBytes(bytes, i, packetEnd);
            if (header.Zerocoded && zeroBuffer != null)
            {
                packetEnd[0] = Helpers.ZeroDecode(bytes, packetEnd[0] + 1, zeroBuffer) - 1;
                bytes = zeroBuffer;
            }
            AgentData.FromBytes(bytes, i);
            RegionData.FromBytes(bytes, i);
            Info.FromBytes(bytes, i);
        }

        public CrossedRegionPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            AgentData.FromBytes(bytes, i);
            RegionData.FromBytes(bytes, i);
            Info.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 8;
            length += AgentData.getLength();
            length += RegionData.length;
            length += Info.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            RegionData.ToBytes(bytes, i);
            Info.ToBytes(bytes, i);
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
			public byte[][] ToBytesMultiple()
        {
            return new byte[][] { ToBytes() };
        }
    }

    /// <exclude/>
