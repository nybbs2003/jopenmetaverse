package com.ngt.jopenmetaverse.shared.protocol;


    public final class ClassifiedInfoUpdatePacket extends Packet
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
        public final class DataBlock extends PacketBlock
        {
            public UUID ClassifiedID;
            public uint Category;
            public byte[] Name;
            public byte[] Desc;
            public UUID ParcelID;
            public uint ParentEstate;
            public UUID SnapshotID;
            public Vector3d PosGlobal;
            public byte ClassifiedFlags;
            public int PriceForListing;

            @Override
			public int getLength()
            {
                get
                {
                    int length = 88;
                    if (Name != null) { length += Name.length; }
                    if (Desc != null) { length += Desc.length; }
                    return length;
                }
            }

            public DataBlock() { }
            public DataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    ClassifiedID.FromBytes(bytes, i); i += 16;
                    Category = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    length = bytes[i++];
                    Name = new byte[length];
                    Buffer.BlockCopy(bytes, i, Name, 0, length); i += length;
                    length = (bytes[i++] + (bytes[i++] << 8));
                    Desc = new byte[length];
                    Buffer.BlockCopy(bytes, i, Desc, 0, length); i += length;
                    ParcelID.FromBytes(bytes, i); i += 16;
                    ParentEstate = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    SnapshotID.FromBytes(bytes, i); i += 16;
                    PosGlobal.FromBytes(bytes, i); i += 24;
                    ClassifiedFlags = (byte)bytes[i++];
                    PriceForListing = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                ClassifiedID.ToBytes(bytes, i); i += 16;
                Utils.UIntToBytes(Category, bytes, i); i += 4;
                bytes[i++] = (byte)Name.length;
                Buffer.BlockCopy(Name, 0, bytes, i, Name.length); i += Name.length;
                bytes[i++] = (byte)(Desc.length % 256);
                bytes[i++] = (byte)((Desc.length >> 8) % 256);
                Buffer.BlockCopy(Desc, 0, bytes, i, Desc.length); i += Desc.length;
                ParcelID.ToBytes(bytes, i); i += 16;
                Utils.UIntToBytes(ParentEstate, bytes, i); i += 4;
                SnapshotID.ToBytes(bytes, i); i += 16;
                PosGlobal.ToBytes(bytes, i); i += 24;
                bytes[i++] = ClassifiedFlags;
                Utils.IntToBytes(PriceForListing, bytes, i); i += 4;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 10;
                length += AgentData.getLength();
                length += Data.getLength();
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public DataBlock Data;

        public ClassifiedInfoUpdatePacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.ClassifiedInfoUpdate;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 45;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
            Data = new DataBlock();
        }

        public ClassifiedInfoUpdatePacket(byte[] bytes, int[] i) 
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
            Data.FromBytes(bytes, i);
        }

        public ClassifiedInfoUpdatePacket(Header head, byte[] bytes, int[] i)
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
            Data.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += Data.getLength();
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            Data.ToBytes(bytes, i);
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
