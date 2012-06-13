package com.ngt.jopenmetaverse.shared.protocol;


    public final class PickInfoReplyPacket extends Packet
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
            public UUID PickID;
            public UUID CreatorID;
            public bool TopPick;
            public UUID ParcelID;
            public byte[] Name;
            public byte[] Desc;
            public UUID SnapshotID;
            public byte[] User;
            public byte[] OriginalName;
            public byte[] SimName;
            public Vector3d PosGlobal;
            public int SortOrder;
            public bool Enabled;

            @Override
			public int getLength()
            {
                                {
                    int length = 100;
                    if (Name != null) { length += Name.length; }
                    if (Desc != null) { length += Desc.length; }
                    if (User != null) { length += User.length; }
                    if (OriginalName != null) { length += OriginalName.length; }
                    if (SimName != null) { length += SimName.length; }
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
                    PickID.FromBytes(bytes, i[0]); i[0] += 16;
                    CreatorID.FromBytes(bytes, i[0]); i[0] += 16;
                    TopPick = (bytes[i[0]++] != 0) ? (bool)true : (bool)false;
                    ParcelID.FromBytes(bytes, i[0]); i[0] += 16;
                    length = bytes[i[0]++];
                    Name = new byte[length];
                    Utils.arraycopy(bytes, i, Name, 0, length); i[0] +=  length;
                    length = (bytes[i[0]++] + (bytes[i[0]++] << 8));
                    Desc = new byte[length];
                    Utils.arraycopy(bytes, i, Desc, 0, length); i[0] +=  length;
                    SnapshotID.FromBytes(bytes, i[0]); i[0] += 16;
                    length = bytes[i[0]++];
                    User = new byte[length];
                    Utils.arraycopy(bytes, i, User, 0, length); i[0] +=  length;
                    length = bytes[i[0]++];
                    OriginalName = new byte[length];
                    Utils.arraycopy(bytes, i, OriginalName, 0, length); i[0] +=  length;
                    length = bytes[i[0]++];
                    SimName = new byte[length];
                    Utils.arraycopy(bytes, i, SimName, 0, length); i[0] +=  length;
                    PosGlobal.FromBytes(bytes, i[0]); i[0] += 24;
                    SortOrder = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    Enabled = (bytes[i[0]++] != 0) ? (bool)true : (bool)false;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                PickID.ToBytes(bytes, i[0]); i[0] += 16;
                CreatorID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = (byte)((TopPick) ? 1 : 0);
                ParcelID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = (byte)Name.length;
                Utils.arraycopy(Name, 0, bytes, i, Name.length); i[0] +=  Name.length;
                bytes[i[0]++] = (byte)(Desc.length % 256);
                bytes[i[0]++] = (byte)((Desc.length >> 8) % 256);
                Utils.arraycopy(Desc, 0, bytes, i, Desc.length); i[0] +=  Desc.length;
                SnapshotID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = (byte)User.length;
                Utils.arraycopy(User, 0, bytes, i, User.length); i[0] +=  User.length;
                bytes[i[0]++] = (byte)OriginalName.length;
                Utils.arraycopy(OriginalName, 0, bytes, i, OriginalName.length); i[0] +=  OriginalName.length;
                bytes[i[0]++] = (byte)SimName.length;
                Utils.arraycopy(SimName, 0, bytes, i, SimName.length); i[0] +=  SimName.length;
                PosGlobal.ToBytes(bytes, i[0]); i[0] += 24;
                Utils.IntToBytes(SortOrder, bytes, i); i += 4;
                bytes[i[0]++] = (byte)((Enabled) ? 1 : 0);
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

        public PickInfoReplyPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.PickInfoReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 184;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
            Data = new DataBlock();
        }

        public PickInfoReplyPacket(byte[] bytes, int[] i) 
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

        public PickInfoReplyPacket(Header head, byte[] bytes, int[] i)
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
