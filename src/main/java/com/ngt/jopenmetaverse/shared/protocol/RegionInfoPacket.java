package com.ngt.jopenmetaverse.shared.protocol;


    public final class RegionInfoPacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID SessionID;

            @Override
			public int getLength()
            {
                get
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
                    SessionID.FromBytes(bytes, i); i += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                AgentID.ToBytes(bytes, i); i += 16;
                SessionID.ToBytes(bytes, i); i += 16;
            }

        }

        /// <exclude/>
        public final class RegionInfoBlock extends PacketBlock
        {
            public byte[] SimName;
            public uint EstateID;
            public uint ParentEstateID;
            public uint RegionFlags;
            public byte SimAccess;
            public byte MaxAgents;
            public float BillableFactor;
            public float ObjectBonusFactor;
            public float WaterHeight;
            public float TerrainRaiseLimit;
            public float TerrainLowerLimit;
            public int PricePerMeter;
            public int RedirectGridX;
            public int RedirectGridY;
            public bool UseEstateSun;
            public float SunHour;

            @Override
			public int getLength()
            {
                get
                {
                    int length = 52;
                    if (SimName != null) { length += SimName.length; }
                    return length;
                }
            }

            public RegionInfoBlock() { }
            public RegionInfoBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    length = bytes[i++];
                    SimName = new byte[length];
                    Buffer.BlockCopy(bytes, i, SimName, 0, length); i += length;
                    EstateID = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    ParentEstateID = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    RegionFlags = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    SimAccess = (byte)bytes[i++];
                    MaxAgents = (byte)bytes[i++];
                    BillableFactor = Utils.BytesToFloat(bytes, i); i += 4;
                    ObjectBonusFactor = Utils.BytesToFloat(bytes, i); i += 4;
                    WaterHeight = Utils.BytesToFloat(bytes, i); i += 4;
                    TerrainRaiseLimit = Utils.BytesToFloat(bytes, i); i += 4;
                    TerrainLowerLimit = Utils.BytesToFloat(bytes, i); i += 4;
                    PricePerMeter = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    RedirectGridX = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    RedirectGridY = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    UseEstateSun = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    SunHour = Utils.BytesToFloat(bytes, i); i += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i++] = (byte)SimName.length;
                Buffer.BlockCopy(SimName, 0, bytes, i, SimName.length); i += SimName.length;
                Utils.UIntToBytes(EstateID, bytes, i); i += 4;
                Utils.UIntToBytes(ParentEstateID, bytes, i); i += 4;
                Utils.UIntToBytes(RegionFlags, bytes, i); i += 4;
                bytes[i++] = SimAccess;
                bytes[i++] = MaxAgents;
                Utils.FloatToBytes(BillableFactor, bytes, i); i += 4;
                Utils.FloatToBytes(ObjectBonusFactor, bytes, i); i += 4;
                Utils.FloatToBytes(WaterHeight, bytes, i); i += 4;
                Utils.FloatToBytes(TerrainRaiseLimit, bytes, i); i += 4;
                Utils.FloatToBytes(TerrainLowerLimit, bytes, i); i += 4;
                Utils.IntToBytes(PricePerMeter, bytes, i); i += 4;
                Utils.IntToBytes(RedirectGridX, bytes, i); i += 4;
                Utils.IntToBytes(RedirectGridY, bytes, i); i += 4;
                bytes[i++] = (byte)((UseEstateSun) ? 1 : 0);
                Utils.FloatToBytes(SunHour, bytes, i); i += 4;
            }

        }

        /// <exclude/>
        public final class RegionInfo2Block extends PacketBlock
        {
            public byte[] ProductSKU;
            public byte[] ProductName;
            public uint MaxAgents32;
            public uint HardMaxAgents;
            public uint HardMaxObjects;

            @Override
			public int getLength()
            {
                get
                {
                    int length = 14;
                    if (ProductSKU != null) { length += ProductSKU.length; }
                    if (ProductName != null) { length += ProductName.length; }
                    return length;
                }
            }

            public RegionInfo2Block() { }
            public RegionInfo2Block(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    length = bytes[i++];
                    ProductSKU = new byte[length];
                    Buffer.BlockCopy(bytes, i, ProductSKU, 0, length); i += length;
                    length = bytes[i++];
                    ProductName = new byte[length];
                    Buffer.BlockCopy(bytes, i, ProductName, 0, length); i += length;
                    MaxAgents32 = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    HardMaxAgents = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    HardMaxObjects = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i++] = (byte)ProductSKU.length;
                Buffer.BlockCopy(ProductSKU, 0, bytes, i, ProductSKU.length); i += ProductSKU.length;
                bytes[i++] = (byte)ProductName.length;
                Buffer.BlockCopy(ProductName, 0, bytes, i, ProductName.length); i += ProductName.length;
                Utils.UIntToBytes(MaxAgents32, bytes, i); i += 4;
                Utils.UIntToBytes(HardMaxAgents, bytes, i); i += 4;
                Utils.UIntToBytes(HardMaxObjects, bytes, i); i += 4;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 10;
                length += AgentData.getLength();
                length += RegionInfo.length;
                length += RegionInfo2.length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public RegionInfoBlock RegionInfo;
        public RegionInfo2Block RegionInfo2;

        public RegionInfoPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.RegionInfo;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 142;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            RegionInfo = new RegionInfoBlock();
            RegionInfo2 = new RegionInfo2Block();
        }

        public RegionInfoPacket(byte[] bytes, int[] i) 
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
                packetEnd = Helpers.ZeroDecode(bytes, packetEnd + 1, zeroBuffer) - 1;
                bytes = zeroBuffer;
            }
            AgentData.FromBytes(bytes, i);
            RegionInfo.FromBytes(bytes, i);
            RegionInfo2.FromBytes(bytes, i);
        }

        public RegionInfoPacket(Header head, byte[] bytes, int[] i)
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
            RegionInfo.FromBytes(bytes, i);
            RegionInfo2.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += RegionInfo.length;
            length += RegionInfo2.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            RegionInfo.ToBytes(bytes, i);
            RegionInfo2.ToBytes(bytes, i);
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
