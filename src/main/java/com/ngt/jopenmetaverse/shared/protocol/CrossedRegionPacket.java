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
                    AgentID.FromBytes(bytes, i[0]); i[0] += 16;
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
            public long SimIP;
            public ushort SimPort;
            public BigInteger RegionHandle;
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
                    SimIP = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    SimPort = (int)Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    RegionHandle = Utils.bytesToULong(bytes, i[0]); i[0] += 8;
                    length = Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    SeedCapability = new byte[length];
                    Utils.arraycopy(bytes, i[0], SeedCapability, 0, length); i[0] +=  length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.uintToBytes(SimIP, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = (byte)((SimPort >> 8) % 256);
                bytes[i[0]++] = (byte)(SimPort % 256);
                Utils.ulongToBytes(RegionHandle, bytes, i[0]); i[0] += 8;
                bytes[i[0]++] = (byte)(SeedCapability.length % 256);
                bytes[i[0]++] = (byte)((SeedCapability.length >> 8) % 256);
                Utils.arraycopy(SeedCapability, 0, bytes, i[0], SeedCapability.length); i[0] +=  SeedCapability.length;
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
                    Position.FromBytes(bytes, i[0]); i[0] += 12;
                    LookAt.FromBytes(bytes, i[0]); i[0] += 12;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Position.ToBytes(bytes, i[0]); i[0] += 12;
                LookAt.ToBytes(bytes, i[0]); i[0] += 12;
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
            int[] i = new int[]{0};
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
