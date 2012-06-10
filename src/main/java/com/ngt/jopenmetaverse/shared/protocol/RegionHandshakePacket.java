package com.ngt.jopenmetaverse.shared.protocol;


    public final class RegionHandshakePacket extends Packet
    {
        /// <exclude/>
        public final class RegionInfoBlock extends PacketBlock
        {
            public uint RegionFlags;
            public byte SimAccess;
            public byte[] SimName;
            public UUID SimOwner;
            public bool IsEstateManager;
            public float WaterHeight;
            public float BillableFactor;
            public UUID CacheID;
            public UUID TerrainBase0;
            public UUID TerrainBase1;
            public UUID TerrainBase2;
            public UUID TerrainBase3;
            public UUID TerrainDetail0;
            public UUID TerrainDetail1;
            public UUID TerrainDetail2;
            public UUID TerrainDetail3;
            public float TerrainStartHeight00;
            public float TerrainStartHeight01;
            public float TerrainStartHeight10;
            public float TerrainStartHeight11;
            public float TerrainHeightRange00;
            public float TerrainHeightRange01;
            public float TerrainHeightRange10;
            public float TerrainHeightRange11;

            @Override
			public int getLength()
            {
                get
                {
                    int length = 207;
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
                    RegionFlags = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    SimAccess = (byte)bytes[i++];
                    length = bytes[i++];
                    SimName = new byte[length];
                    Buffer.BlockCopy(bytes, i, SimName, 0, length); i += length;
                    SimOwner.FromBytes(bytes, i); i += 16;
                    IsEstateManager = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    WaterHeight = Utils.BytesToFloat(bytes, i); i += 4;
                    BillableFactor = Utils.BytesToFloat(bytes, i); i += 4;
                    CacheID.FromBytes(bytes, i); i += 16;
                    TerrainBase0.FromBytes(bytes, i); i += 16;
                    TerrainBase1.FromBytes(bytes, i); i += 16;
                    TerrainBase2.FromBytes(bytes, i); i += 16;
                    TerrainBase3.FromBytes(bytes, i); i += 16;
                    TerrainDetail0.FromBytes(bytes, i); i += 16;
                    TerrainDetail1.FromBytes(bytes, i); i += 16;
                    TerrainDetail2.FromBytes(bytes, i); i += 16;
                    TerrainDetail3.FromBytes(bytes, i); i += 16;
                    TerrainStartHeight00 = Utils.BytesToFloat(bytes, i); i += 4;
                    TerrainStartHeight01 = Utils.BytesToFloat(bytes, i); i += 4;
                    TerrainStartHeight10 = Utils.BytesToFloat(bytes, i); i += 4;
                    TerrainStartHeight11 = Utils.BytesToFloat(bytes, i); i += 4;
                    TerrainHeightRange00 = Utils.BytesToFloat(bytes, i); i += 4;
                    TerrainHeightRange01 = Utils.BytesToFloat(bytes, i); i += 4;
                    TerrainHeightRange10 = Utils.BytesToFloat(bytes, i); i += 4;
                    TerrainHeightRange11 = Utils.BytesToFloat(bytes, i); i += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.UIntToBytes(RegionFlags, bytes, i); i += 4;
                bytes[i++] = SimAccess;
                bytes[i++] = (byte)SimName.length;
                Buffer.BlockCopy(SimName, 0, bytes, i, SimName.length); i += SimName.length;
                SimOwner.ToBytes(bytes, i); i += 16;
                bytes[i++] = (byte)((IsEstateManager) ? 1 : 0);
                Utils.FloatToBytes(WaterHeight, bytes, i); i += 4;
                Utils.FloatToBytes(BillableFactor, bytes, i); i += 4;
                CacheID.ToBytes(bytes, i); i += 16;
                TerrainBase0.ToBytes(bytes, i); i += 16;
                TerrainBase1.ToBytes(bytes, i); i += 16;
                TerrainBase2.ToBytes(bytes, i); i += 16;
                TerrainBase3.ToBytes(bytes, i); i += 16;
                TerrainDetail0.ToBytes(bytes, i); i += 16;
                TerrainDetail1.ToBytes(bytes, i); i += 16;
                TerrainDetail2.ToBytes(bytes, i); i += 16;
                TerrainDetail3.ToBytes(bytes, i); i += 16;
                Utils.FloatToBytes(TerrainStartHeight00, bytes, i); i += 4;
                Utils.FloatToBytes(TerrainStartHeight01, bytes, i); i += 4;
                Utils.FloatToBytes(TerrainStartHeight10, bytes, i); i += 4;
                Utils.FloatToBytes(TerrainStartHeight11, bytes, i); i += 4;
                Utils.FloatToBytes(TerrainHeightRange00, bytes, i); i += 4;
                Utils.FloatToBytes(TerrainHeightRange01, bytes, i); i += 4;
                Utils.FloatToBytes(TerrainHeightRange10, bytes, i); i += 4;
                Utils.FloatToBytes(TerrainHeightRange11, bytes, i); i += 4;
            }

        }

        /// <exclude/>
        public final class RegionInfo2Block extends PacketBlock
        {
            public UUID RegionID;

            @Override
			public int getLength()
            {
                get
                {
                    return 16;
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
                try
                {
                    RegionID.FromBytes(bytes, i); i += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                RegionID.ToBytes(bytes, i); i += 16;
            }

        }

        /// <exclude/>
        public final class RegionInfo3Block extends PacketBlock
        {
            public int CPUClassID;
            public int CPURatio;
            public byte[] ColoName;
            public byte[] ProductSKU;
            public byte[] ProductName;

            @Override
			public int getLength()
            {
                get
                {
                    int length = 11;
                    if (ColoName != null) { length += ColoName.length; }
                    if (ProductSKU != null) { length += ProductSKU.length; }
                    if (ProductName != null) { length += ProductName.length; }
                    return length;
                }
            }

            public RegionInfo3Block() { }
            public RegionInfo3Block(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    CPUClassID = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    CPURatio = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    length = bytes[i++];
                    ColoName = new byte[length];
                    Buffer.BlockCopy(bytes, i, ColoName, 0, length); i += length;
                    length = bytes[i++];
                    ProductSKU = new byte[length];
                    Buffer.BlockCopy(bytes, i, ProductSKU, 0, length); i += length;
                    length = bytes[i++];
                    ProductName = new byte[length];
                    Buffer.BlockCopy(bytes, i, ProductName, 0, length); i += length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.IntToBytes(CPUClassID, bytes, i); i += 4;
                Utils.IntToBytes(CPURatio, bytes, i); i += 4;
                bytes[i++] = (byte)ColoName.length;
                Buffer.BlockCopy(ColoName, 0, bytes, i, ColoName.length); i += ColoName.length;
                bytes[i++] = (byte)ProductSKU.length;
                Buffer.BlockCopy(ProductSKU, 0, bytes, i, ProductSKU.length); i += ProductSKU.length;
                bytes[i++] = (byte)ProductName.length;
                Buffer.BlockCopy(ProductName, 0, bytes, i, ProductName.length); i += ProductName.length;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 10;
                length += RegionInfo.length;
                length += RegionInfo2.length;
                length += RegionInfo3.length;
                return length;
            }
        }
        public RegionInfoBlock RegionInfo;
        public RegionInfo2Block RegionInfo2;
        public RegionInfo3Block RegionInfo3;

        public RegionHandshakePacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.RegionHandshake;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 148;
            header.Reliable = true;
            header.Zerocoded = true;
            RegionInfo = new RegionInfoBlock();
            RegionInfo2 = new RegionInfo2Block();
            RegionInfo3 = new RegionInfo3Block();
        }

        public RegionHandshakePacket(byte[] bytes, int[] i) 
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
            RegionInfo.FromBytes(bytes, i);
            RegionInfo2.FromBytes(bytes, i);
            RegionInfo3.FromBytes(bytes, i);
        }

        public RegionHandshakePacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            RegionInfo.FromBytes(bytes, i);
            RegionInfo2.FromBytes(bytes, i);
            RegionInfo3.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += RegionInfo.length;
            length += RegionInfo2.length;
            length += RegionInfo3.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            RegionInfo.ToBytes(bytes, i);
            RegionInfo2.ToBytes(bytes, i);
            RegionInfo3.ToBytes(bytes, i);
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
