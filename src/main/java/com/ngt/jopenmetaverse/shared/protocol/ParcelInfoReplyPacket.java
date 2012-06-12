package com.ngt.jopenmetaverse.shared.protocol;


    public final class ParcelInfoReplyPacket extends Packet
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
                    AgentID.FromBytes(bytes, i); i += 16;
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
            public UUID ParcelID;
            public UUID OwnerID;
            public byte[] Name;
            public byte[] Desc;
            public int ActualArea;
            public int BillableArea;
            public byte Flags;
            public float GlobalX;
            public float GlobalY;
            public float GlobalZ;
            public byte[] SimName;
            public UUID SnapshotID;
            public float Dwell;
            public int SalePrice;
            public int AuctionID;

            @Override
			public int getLength()
            {
                                {
                    int length = 84;
                    if (Name != null) { length += Name.length; }
                    if (Desc != null) { length += Desc.length; }
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
                    ParcelID.FromBytes(bytes, i); i += 16;
                    OwnerID.FromBytes(bytes, i); i += 16;
                    length = bytes[i++];
                    Name = new byte[length];
                    Utils.arraycopy(bytes, i, Name, 0, length); i += length;
                    length = bytes[i++];
                    Desc = new byte[length];
                    Utils.arraycopy(bytes, i, Desc, 0, length); i += length;
                    ActualArea = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    BillableArea = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    Flags = (byte)bytes[i++];
                    GlobalX = Utils.BytesToFloat(bytes, i); i += 4;
                    GlobalY = Utils.BytesToFloat(bytes, i); i += 4;
                    GlobalZ = Utils.BytesToFloat(bytes, i); i += 4;
                    length = bytes[i++];
                    SimName = new byte[length];
                    Utils.arraycopy(bytes, i, SimName, 0, length); i += length;
                    SnapshotID.FromBytes(bytes, i); i += 16;
                    Dwell = Utils.BytesToFloat(bytes, i); i += 4;
                    SalePrice = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    AuctionID = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                ParcelID.ToBytes(bytes, i); i += 16;
                OwnerID.ToBytes(bytes, i); i += 16;
                bytes[i++] = (byte)Name.length;
                Utils.arraycopy(Name, 0, bytes, i, Name.length); i += Name.length;
                bytes[i++] = (byte)Desc.length;
                Utils.arraycopy(Desc, 0, bytes, i, Desc.length); i += Desc.length;
                Utils.IntToBytes(ActualArea, bytes, i); i += 4;
                Utils.IntToBytes(BillableArea, bytes, i); i += 4;
                bytes[i++] = Flags;
                Utils.FloatToBytes(GlobalX, bytes, i); i += 4;
                Utils.FloatToBytes(GlobalY, bytes, i); i += 4;
                Utils.FloatToBytes(GlobalZ, bytes, i); i += 4;
                bytes[i++] = (byte)SimName.length;
                Utils.arraycopy(SimName, 0, bytes, i, SimName.length); i += SimName.length;
                SnapshotID.ToBytes(bytes, i); i += 16;
                Utils.FloatToBytes(Dwell, bytes, i); i += 4;
                Utils.IntToBytes(SalePrice, bytes, i); i += 4;
                Utils.IntToBytes(AuctionID, bytes, i); i += 4;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += AgentData.getLength();
                length += Data.getLength();
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public DataBlock Data;

        public ParcelInfoReplyPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.ParcelInfoReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 55;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            Data = new DataBlock();
        }

        public ParcelInfoReplyPacket(byte[] bytes, int[] i) 
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

        public ParcelInfoReplyPacket(Header head, byte[] bytes, int[] i)
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
