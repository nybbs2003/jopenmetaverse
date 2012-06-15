package com.ngt.jopenmetaverse.shared.protocol;


    public final class ClassifiedInfoReplyPacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;

            @Override
			public int getLength()
            {
                                {
                    return 16;
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
            }

        }

        /// <exclude/>
        public final class DataBlock extends PacketBlock
        {
            public UUID ClassifiedID;
            public UUID CreatorID;
            public long CreationDate;
            public long ExpirationDate;
            public long Category;
            public byte[] Name;
            public byte[] Desc;
            public UUID ParcelID;
            public long ParentEstate;
            public UUID SnapshotID;
            public byte[] SimName;
            public Vector3d PosGlobal;
            public byte[] ParcelName;
            public byte ClassifiedFlags;
            public int PriceForListing;

            @Override
			public int getLength()
            {
                                {
                    int length = 114;
                    if (Name != null) { length += Name.length; }
                    if (Desc != null) { length += Desc.length; }
                    if (SimName != null) { length += SimName.length; }
                    if (ParcelName != null) { length += ParcelName.length; }
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
                    ClassifiedID.FromBytes(bytes, i[0]); i[0] += 16;
                    CreatorID.FromBytes(bytes, i[0]); i[0] += 16;
                    CreationDate = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    ExpirationDate = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    Category = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    Name = new byte[length];
                    Utils.arraycopy(bytes, i[0], Name, 0, length); i[0] +=  length;
                    length = Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    Desc = new byte[length];
                    Utils.arraycopy(bytes, i[0], Desc, 0, length); i[0] +=  length;
                    ParcelID.FromBytes(bytes, i[0]); i[0] += 16;
                    ParentEstate = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    SnapshotID.FromBytes(bytes, i[0]); i[0] += 16;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    SimName = new byte[length];
                    Utils.arraycopy(bytes, i[0], SimName, 0, length); i[0] +=  length;
                    PosGlobal.FromBytes(bytes, i[0]); i[0] += 24;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    ParcelName = new byte[length];
                    Utils.arraycopy(bytes, i[0], ParcelName, 0, length); i[0] +=  length;
                    ClassifiedFlags = (byte)bytes[i[0]++];
                    PriceForListing = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                ClassifiedID.ToBytes(bytes, i[0]); i[0] += 16;
                CreatorID.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.uintToBytes(CreationDate, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(ExpirationDate, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(Category, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = (byte)Name.length;
                Utils.arraycopy(Name, 0, bytes, i[0], Name.length); i[0] +=  Name.length;
                bytes[i[0]++] = (byte)(Desc.length % 256);
                bytes[i[0]++] = (byte)((Desc.length >> 8) % 256);
                Utils.arraycopy(Desc, 0, bytes, i[0], Desc.length); i[0] +=  Desc.length;
                ParcelID.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.uintToBytes(ParentEstate, bytes, i[0]); i[0] += 4;
                SnapshotID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = (byte)SimName.length;
                Utils.arraycopy(SimName, 0, bytes, i[0], SimName.length); i[0] +=  SimName.length;
                PosGlobal.ToBytes(bytes, i[0]); i[0] += 24;
                bytes[i[0]++] = (byte)ParcelName.length;
                Utils.arraycopy(ParcelName, 0, bytes, i[0], ParcelName.length); i[0] +=  ParcelName.length;
                bytes[i[0]++] = ClassifiedFlags;
                Utils.intToBytes(PriceForListing, bytes, i[0]); i[0] += 4;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += AgentData.getLength();
                length += Data.length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public DataBlock Data;

        public ClassifiedInfoReplyPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.ClassifiedInfoReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 44;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
            Data = new DataBlock();
        }

        public ClassifiedInfoReplyPacket(byte[] bytes, int[] i) 
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
            Data.FromBytes(bytes, i);
        }

        public ClassifiedInfoReplyPacket(Header head, byte[] bytes, int[] i)
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
            length += Data.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
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
